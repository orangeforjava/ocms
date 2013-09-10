/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2003-2008 Frederico Caldeira Knabben
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 */
package org.openuap.cms.editor.connector;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openuap.cms.editor.handlers.CommandHandler;
import org.openuap.cms.editor.handlers.ConnectorHandler;
import org.openuap.cms.editor.handlers.ExtensionsHandler;
import org.openuap.cms.editor.handlers.RequestCycleHandler;
import org.openuap.cms.editor.handlers.ResourceTypeHandler;
import org.openuap.cms.editor.response.UploadResponse;
import org.openuap.cms.editor.response.XmlResponse;
import org.openuap.cms.editor.tool.Utils;
import org.openuap.cms.editor.tool.UtilsFile;
import org.openuap.cms.editor.tool.UtilsResponse;

/**
 * Servlet to upload and browse files.<br>
 * 
 * This servlet accepts 4 commands used to retrieve and create files and folders
 * from a server directory. The allowed commands are:
 * <ul>
 * <li>GetFolders: Retrieve the list of directory under the current folder
 * <li>GetFoldersAndFiles: Retrive the list of files and directory under the
 * current folder
 * <li>CreateFolder: Create a new directory under the current folder
 * <li>FileUpload: Send a new file to the server (must be sent with a POST)
 * </ul>
 * 
 * @version $Id: ConnectorServlet.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class ConnectorServlet extends HttpServlet {

	private static final long serialVersionUID = -5742008970929377161L;
	private static final Log logger = LogFactory.getLog(ConnectorServlet.class);

	/**
	 * Initialize the servlet.<br>
	 * The default directory for user files will be constructed.
	 */
	public void init() throws ServletException, IllegalArgumentException {
		// check, if 'baseDir' exists
		String realDefaultUserFilesPath = getServletContext().getRealPath(
				ConnectorHandler.getDefaultUserFilesPath());

		File defaultUserFilesDir = new File(realDefaultUserFilesPath);
		UtilsFile.checkDirAndCreate(defaultUserFilesDir);

		logger.info("ConnectorServlet successful initialized!");
	}

	/**
	 * Manage the Get requests (GetFolders, GetFoldersAndFiles, CreateFolder).<br>
	 * 
	 * The servlet accepts commands sent in the following format:<br>
	 * connector?Command=CommandName&Type=ResourceType&CurrentFolder=FolderPath<br>
	 * <br>
	 * It executes the commands and then return the results to the client in XML
	 * format.
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("Entering ConnectorServlet#doGet");

		//response.setc("UTF-8");
		response.setContentType("application/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();

		String commandStr = request.getParameter("Command");
		String typeStr = request.getParameter("Type");
		String currentFolderStr = request.getParameter("CurrentFolder");

		logger.debug("Parameter Command: " + commandStr);
		logger.debug("Parameter Type: " + typeStr);
		logger.debug("Parameter CurrentFolder:" + currentFolderStr);

		XmlResponse xr;

		if (!RequestCycleHandler.isEnabledForFileBrowsing(request))
			xr = new XmlResponse(XmlResponse.EN_ERROR,
					Messages.NOT_AUTHORIZED_FOR_BROWSING);
		else if (!CommandHandler.isValidForGet(commandStr))
			xr = new XmlResponse(XmlResponse.EN_ERROR, Messages.INVALID_COMMAND);
		else if (typeStr != null && !ResourceTypeHandler.isValid(typeStr))
			xr = new XmlResponse(XmlResponse.EN_ERROR, Messages.INVALID_TYPE);
		else if (!UtilsFile.isValidPath(currentFolderStr))
			xr = new XmlResponse(XmlResponse.EN_ERROR,
					Messages.INVALID_CURRENT_FOLDER);
		else {
			CommandHandler command = CommandHandler.getCommand(commandStr);
			ResourceTypeHandler resourceType = ResourceTypeHandler
					.getDefaultResourceType(typeStr);

			String typePath = UtilsResponse.constructResponseUrl(request,
					resourceType, currentFolderStr, false, false);
			String typeDirPath = getServletContext().getRealPath(typePath);

			File typeDir = new File(typeDirPath);
			UtilsFile.checkDirAndCreate(typeDir);

			File currentDir = new File(typeDir, currentFolderStr);

			if (!currentDir.exists())
				xr = new XmlResponse(XmlResponse.EN_INVALID_FOLDER_NAME);
			else {

				xr = new XmlResponse(command, resourceType, currentFolderStr,
						UtilsResponse.constructResponseUrl(request,
								resourceType, currentFolderStr, true,
								ConnectorHandler.isFullUrl()));

				if (command.equals(CommandHandler.GET_FOLDERS))
					xr.setFolders(currentDir);
				else if (command.equals(CommandHandler.GET_FOLDERS_AND_FILES))
					xr.setFoldersAndFiles(currentDir);
				else if (command.equals(CommandHandler.CREATE_FOLDER)) {
					String newFolderStr = UtilsFile.sanitizeFolderName(request
							.getParameter("NewFolderName"));
					logger.debug("Parameter NewFolderName:" + newFolderStr);

					File newFolder = new File(currentDir, newFolderStr);
					int errorNumber = XmlResponse.EN_UKNOWN;

					if (newFolder.exists())
						errorNumber = XmlResponse.EN_ALREADY_EXISTS;
					else {
						try {
							errorNumber = (newFolder.mkdir()) ? XmlResponse.EN_OK
									: XmlResponse.EN_INVALID_FOLDER_NAME;
						} catch (SecurityException e) {
							errorNumber = XmlResponse.EN_SECURITY_ERROR;
						}
					}
					xr.setError(errorNumber);
				}
			}
		}

		out.print(xr);
		out.flush();
		out.close();
		logger.debug("Exiting ConnectorServlet#doGet");
	}

	/**
	 * Manage the Post requests (FileUpload).<br>
	 * 
	 * The servlet accepts commands sent in the following format:<br>
	 * connector?Command=FileUpload&Type=ResourceType&CurrentFolder=FolderPath<br>
	 * <br>
	 * It store the file (renaming it in case a file with the same name exists)
	 * and then return an HTML file with a javascript command in it.
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("Entering Connector#doPost");

		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();

		String commandStr = request.getParameter("Command");
		String typeStr = request.getParameter("Type");
		String currentFolderStr = request.getParameter("CurrentFolder");

		logger.debug("Parameter Command: " + commandStr);
		logger.debug("Parameter Type:" + typeStr);
		logger.debug("Parameter CurrentFolder: }" + currentFolderStr);

		UploadResponse ur;

		// if this is a QuickUpload-Request, 'commandStr' and 'currentFolderStr'
		// are empty
		if (Utils.isEmpty(commandStr) && Utils.isEmpty(currentFolderStr)) {
			commandStr = "QuickUpload";
			currentFolderStr = "/";
		}

		if (!RequestCycleHandler.isEnabledForFileUpload(request))
			ur = new UploadResponse(UploadResponse.EN_SECURITY_ERROR, null,
					null, Messages.NOT_AUTHORIZED_FOR_UPLOAD);
		else if (!CommandHandler.isValidForPost(commandStr))
			ur = new UploadResponse(UploadResponse.EN_ERROR, null, null,
					Messages.INVALID_COMMAND);
		else if (typeStr != null && !ResourceTypeHandler.isValid(typeStr))
			ur = new UploadResponse(UploadResponse.EN_ERROR, null, null,
					Messages.INVALID_TYPE);
		else if (!UtilsFile.isValidPath(currentFolderStr))
			ur = UploadResponse.UR_INVALID_CURRENT_FOLDER;
		else {
			ResourceTypeHandler resourceType = ResourceTypeHandler
					.getDefaultResourceType(typeStr);

			String typePath = UtilsResponse.constructResponseUrl(request,
					resourceType, currentFolderStr, false, false);
			String typeDirPath = getServletContext().getRealPath(typePath);

			File typeDir = new File(typeDirPath);
			UtilsFile.checkDirAndCreate(typeDir);

			File currentDir = new File(typeDir, currentFolderStr);

			if (!currentDir.exists())
				ur = UploadResponse.UR_INVALID_CURRENT_FOLDER;
			else {

				String newFilename = null;
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				try {

					List<FileItem> items = upload.parseRequest(request);

					// We upload only one file at the same time
					FileItem uplFile = items.get(0);
					String rawName = UtilsFile.sanitizeFileName(uplFile
							.getName());
					String filename = FilenameUtils.getName(rawName);
					String baseName = FilenameUtils.removeExtension(filename);
					String extension = FilenameUtils.getExtension(filename);

					if (!ExtensionsHandler.isAllowed(resourceType, extension))
						ur = new UploadResponse(
								UploadResponse.EN_INVALID_EXTENSION);
					else {

						// construct an unique file name
						File pathToSave = new File(currentDir, filename);
						int counter = 1;
						while (pathToSave.exists()) {
							newFilename = baseName.concat("(").concat(
									String.valueOf(counter)).concat(")")
									.concat(".").concat(extension);
							pathToSave = new File(currentDir, newFilename);
							counter++;
						}

						if (Utils.isEmpty(newFilename))
							ur = new UploadResponse(UploadResponse.EN_OK,
									UtilsResponse.constructResponseUrl(request,
											resourceType, currentFolderStr,
											true, ConnectorHandler.isFullUrl())
											.concat(filename));
						else
							ur = new UploadResponse(UploadResponse.EN_RENAMED,
									UtilsResponse.constructResponseUrl(request,
											resourceType, currentFolderStr,
											true, ConnectorHandler.isFullUrl())
											.concat(newFilename), newFilename);

						// secure image check
						if (resourceType.equals(ResourceTypeHandler.IMAGE)
								&& ConnectorHandler.isSecureImageUploads()) {
							if (UtilsFile.isImage(uplFile.getInputStream()))
								uplFile.write(pathToSave);
							else {
								uplFile.delete();
								ur = new UploadResponse(
										UploadResponse.EN_INVALID_EXTENSION);
							}
						} else
							uplFile.write(pathToSave);

					}
				} catch (Exception e) {
					ur = new UploadResponse(UploadResponse.EN_SECURITY_ERROR);
				}
			}

		}

		out.print(ur);
		out.flush();
		out.close();

		logger.debug("Exiting Connector#doPost");
	}

}
