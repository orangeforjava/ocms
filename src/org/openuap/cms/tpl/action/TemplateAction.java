/*
 * Copyright 2005-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openuap.cms.tpl.action;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.tpl.manager.TemplateManager;
import org.openuap.cms.util.ui.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 模板管理控制器.
 * </p>
 * 
 * <p>
 * $Id: TemplateAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateAction extends AdminAction {

	private String defaultViewName;

	//
	private String defaultScreensPath;

	private String templateXmlViewName;

	private String templateListViewName;

	private String cateTemplateListViewName;

	//
	private String templateUploadViewName;

	private String templateOperationViewName;
	//	

	private String tplDirSelDialogViewName;

	private String tplDirSelViewName;

	private String tplDirSelXmlViewName;

	private String tplViewName;

	private String tplSelDialogViewName;

	// 模板管理者
	private TemplateManager templateManager;

	/**
	 * 
	 */
	public TemplateAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/tpl/";
		defaultViewName = defaultScreensPath + "template.html";

		templateXmlViewName = defaultScreensPath + "template_tree.xml";

		templateListViewName = defaultScreensPath + "template_list.html";
		cateTemplateListViewName = defaultScreensPath
				+ "cate_template_list.html";

		templateUploadViewName = defaultScreensPath + "template_upload.html";
		templateOperationViewName = defaultScreensPath
				+ "template_operation_result.html";

		//
		tplSelDialogViewName = defaultScreensPath + "tpl_select_dialog.html";
		tplViewName = defaultScreensPath + "tpl_select.html";
		tplDirSelDialogViewName = defaultScreensPath
				+ "tpl_folder_select_dialog.html";
		tplDirSelViewName = defaultScreensPath + "tpl_folder_select.html";
		tplDirSelXmlViewName = defaultScreensPath
				+ "tpl_folder_select_tree.xml";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		return super.beforePerform(request, response, helper, model);
	}

	/**
	 * the default action is show the template tree view.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return ModelAndView
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		List folders = templateManager.getChildFolders("");
		//
		model.put("folders", folders);
		//
		return mv;
	}

	/**
	 * show the template xml tree.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doTplXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String path = helper.getString("path");
		if (path == null) {
			path = "";
		}
		//
		ModelAndView mv = new ModelAndView(templateXmlViewName, model);
		List folders = templateManager.getChildFolders(path);
		model.put("folders", folders);
		//
		setNoCacheHeader(response);
		model.put("responseType", "text/xml");

		return mv;

	}

	/**
	 * show list template.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		String path = helper.getString("path");
		String extra = helper.getString("extra");
		if (path == null) {
			path = "";
		}
		if (extra != null && extra.equals("updir")) {
			path.replaceAll("\\/\\/", "/");
			if (!path.equals("") && !path.equals("/")) {
				//
				if (path.endsWith("/")) {
					path = path.substring(0, path.length() - 2);
				}

				int pos = path.lastIndexOf("/");

				if (pos > -1) {
					path = path.substring(0, pos);
				}
			}
		}
		ModelAndView mv = new ModelAndView(templateListViewName, model);
		List tplFiles = templateManager.getChildTemplates(path);
		model.put("tplFiles", tplFiles);
		model.put("path", path);
		//
		return mv;
	}

	/**
	 * 列出分类下的模板
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doListByCate(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(cateTemplateListViewName, model);
		String tplCateId = helper.getString("tcid");
		String where = request.getParameter("where");
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		//
		Integer start = new Integer(0);
		Integer limit = new Integer(10);
		//
		if (where == null) {
			where = "";
		}
		if (order == null) {
			order = "";
		}
		if (pageNum != null) {
			limit = new Integer(pageNum);
		} else {
			pageNum = "20";
		}
		if (page != null) {
			start = new Integer((Integer.parseInt(page) - 1) * limit.intValue());
		} else {
			page = "1";
		}
		PageBuilder pb = new PageBuilder(limit.intValue());
		QueryInfo qi = new QueryInfo(where, order, limit, start);

		//
		if (tplCateId == null) {
			tplCateId = "0";
		}
		Long tcid = new Long(tplCateId);

		List templates = templateManager.getTemplates(tcid, qi, pb);
		model.put("templates", templates);
		pb.page(Integer.parseInt(page));
		model.put("pb", pb);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("where", where);
		model.put("action", this);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	public ModelAndView doMove(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String targetFile = helper.getString("targetFile");
		String targetPath = helper.getString("targetPath");
		String path = helper.getString("path");
		try {
			String userTplPath = CMSConfig.getInstance().getUserTemplatePath();
			String srcFilePath = userTplPath + "/" + path + "/" + targetFile;
			srcFilePath = StringUtil.normalizePath(srcFilePath);
			File srcFile = new File(srcFilePath);
			//
			String destFilePath = userTplPath + "/" + targetPath + "/"
					+ targetFile;
			destFilePath = StringUtil.normalizePath(destFilePath);
			File destFile = new File(destFilePath);
			FileUtil.cut(srcFile, destFile);
			PrintWriter writer = response.getWriter();
			writer.print("1");
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			PrintWriter writer = response.getWriter();
			writer.print("0");
			writer.flush();
			writer.close();
		}
		return null;
	}

	/**
	 * copy file.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	public ModelAndView doCopy(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String targetFile = helper.getString("targetFile");
		String targetPath = helper.getString("targetPath");
		String path = helper.getString("path");
		try {
			String userTplPath = CMSConfig.getInstance().getUserTemplatePath();
			String srcFilePath = userTplPath + "/" + path + "/" + targetFile;
			srcFilePath = StringUtil.normalizePath(srcFilePath);
			File srcFile = new File(srcFilePath);
			//
			String destFilePath = userTplPath + "/" + targetPath + "/"
					+ targetFile;
			destFilePath = StringUtil.normalizePath(destFilePath);
			File destFile = new File(destFilePath);
			FileUtil.copy(srcFile, destFile);
			PrintWriter writer = response.getWriter();
			writer.print("1");
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			PrintWriter writer = response.getWriter();
			writer.print("0");
			writer.flush();
			writer.close();
		}
		return null;
	}

	/**
	 * change the file name
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	public ModelAndView doChangeFileName(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String targetFile = helper.getString("targetFile");
		String newFile = helper.getString("newFile");
		String path = helper.getString("path");
		try {
			String userTplPath = CMSConfig.getInstance().getUserTemplatePath();
			String srcFilePath = userTplPath + "/" + path + "/" + targetFile;
			srcFilePath = StringUtil.normalizePath(srcFilePath);
			File srcFile = new File(srcFilePath);
			//
			String destFilePath = userTplPath + "/" + path + "/" + newFile;
			destFilePath = StringUtil.normalizePath(destFilePath);
			File destFile = new File(destFilePath);
			boolean success = FileUtil.renameFile(srcFile, destFile);
			PrintWriter writer = response.getWriter();
			if (success) {
				writer.print("1");
			} else {
				writer.print("0");
			}
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			PrintWriter writer = response.getWriter();
			writer.print("0");
			writer.flush();
			writer.close();
		}
		return null;
	}

	public ModelAndView doChangeDirName(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String targetFile = helper.getString("targetFile");
		String newFile = helper.getString("newFile");
		String path = helper.getString("path");
		try {
			String userTplPath = CMSConfig.getInstance().getUserTemplatePath();
			String srcFilePath = userTplPath + "/" + path + "/" + targetFile;
			srcFilePath = StringUtil.normalizePath(srcFilePath);
			File srcFile = new File(srcFilePath);
			//
			String destFilePath = userTplPath + "/" + path + "/" + newFile;
			destFilePath = StringUtil.normalizePath(destFilePath);
			File destFile = new File(destFilePath);
			boolean success = FileUtil.renameFile(srcFile, destFile);
			PrintWriter writer = response.getWriter();
			if (success) {
				writer.print("1");
			} else {
				writer.print("0");
			}
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			PrintWriter writer = response.getWriter();
			writer.print("0");
			writer.flush();
			writer.close();
		}
		return null;
	}

	public ModelAndView doMkDir(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String dirName = helper.getString("dirName");
		String path = helper.getString("path");
		boolean success = false;
		if (dirName != null) {
			try {
				String userTplPath = CMSConfig.getInstance()
						.getUserTemplatePath();
				String dirPath = userTplPath + "/" + path + "/" + dirName;
				dirPath = StringUtil.normalizePath(dirPath);
				File srcFile = new File(dirPath);
				if (!srcFile.exists()) {
					success = srcFile.mkdir();
				}
			} catch (Exception ex) {
				success = false;
			}
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	public ModelAndView doDel(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String targetFile = helper.getString("targetFile");
		String path = helper.getString("path");
		boolean success = false;
		if (targetFile != null) {
			try {
				String userTplPath = CMSConfig.getInstance()
						.getUserTemplatePath();
				String fullPath = userTplPath + "/" + path + "/" + targetFile;
				fullPath = StringUtil.normalizePath(fullPath);
				File srcFile = new File(fullPath);
				if (srcFile.exists() && srcFile.isFile()) {
					success = srcFile.delete();
				}
			} catch (Exception ex) {
				success = false;
			}
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	public ModelAndView doView(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String targetFile = helper.getString("targetFile");
		String path = helper.getString("path");
		if (targetFile != null) {
			CMSConfig config = CMSConfig.getInstance();
			String userTplPath = config.getUserTemplatePath();
			String fullPath = userTplPath + "/" + path + "/" + targetFile;
			fullPath = StringUtil.normalizePath(fullPath);
			File srcFile = new File(fullPath);
			String contentType = FileUtil.getContentType(targetFile);
			String fileType = FileUtil.getExtension(targetFile);
			if (contentType != null) {
				//
				String downloadType = "(zip|rar|exe|doc|xsl|ppt|js)";
				String fileName = FileUtil.getFileName(fullPath);
				if (fileType.matches(downloadType)) {
					String attName = new String(fileName.getBytes(),
							"iso-8859-1");
					response.setContentType("application/x-msdownload");
					String header = "attachment; filename=" + attName;
					response.setHeader("Content-Disposition", header);

				} else {
					response.setContentType(contentType);
				}
			}
			OutputStream out = response.getOutputStream();
			FileUtil.returnFile(srcFile, out);
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doUpload(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(templateUploadViewName, model);
		String path = helper.getString("path");
		if (path == null) {
			path = "";
		}
		model.put("path", path);
		return mv;
	}

	public ModelAndView doUploadSubmit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String path = helper.getString("path");
		ModelAndView mv = new ModelAndView(templateOperationViewName, model);
		model.put("operation", "upload");
		model.put("path", path);
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest
					.getFile("uploadFile");
			String fileName = multipartFile.getOriginalFilename();
			String fileType = FileUtil.getExtension(fileName);
			String userTplPath = CMSConfig.getInstance().getUserTemplatePath();
			String fullPath = userTplPath + "/" + path + "/" + fileName;
			fullPath = StringUtil.normalizePath(fullPath);
			File destFile = new File(fullPath);
			destFile = this.getUploadDestFile(destFile, fileName, 1);
			String allowTypes = getAcceptFileType();
			boolean matched = fileType.matches("(" + allowTypes + ")");
			if (matched) {
				multipartFile.transferTo(destFile);
				model.put("result", "success");
				model.put("msg", "上传文件(" + fileName + ")成功！");
			} else {
				model.put("result", "failed");
				model.put("msg", "上传文件失败:错误的文件类型,系统允许的类型为(" + allowTypes
						+ "),您上传的文件类型为(" + fileType + ")");
			}
			//
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("result", "failed");
			model.put("msg", "上传文件失败,出现意外错误:" + ex.getLocalizedMessage());
		}
		return mv;
	}

	public ModelAndView doIsFileExists(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException {
		String targetFile = helper.getString("targetFile");
		String path = helper.getString("path");
		//
		PrintWriter writer = response.getWriter();
		if (targetFile != null) {
			CMSConfig config = CMSConfig.getInstance();
			String userTplPath = config.getUserTemplatePath();
			String fullPath = userTplPath + "/" + path + "/" + targetFile;
			fullPath = StringUtil.normalizePath(fullPath);
			File file = new File(fullPath);

			if (file.exists()) {

				writer.print("2");
			} else {
				writer.print("0");
			}
		} else {
			writer.print("1");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * make dir and view
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doTplMkDir(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String dirname = helper.getString("dirname");
		String path = helper.getString("path");
		if (dirname != null) {
			if (path != null) {
				//
				String templatePath = CMSConfig.getInstance()
						.getUserTemplatePath();
				templatePath += File.separator + path + File.separator
						+ dirname;
				templatePath = StringUtil.normalizePath(templatePath);
				File dir = new File(templatePath);
				if (!dir.exists()) {
					dir.mkdir();
				}

			}
		}

		return doTplListFile(request, response, helper, model);
	}

	/**
	 * show the select template dialog.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doSelTplDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String tpl = helper.getString("tpl");
		ModelAndView mv = new ModelAndView(tplSelDialogViewName, model);
		model.put("tpl", tpl);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 *            Map
	 * @return ModelAndView
	 */
	public ModelAndView doTplListFile(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		//
		ModelAndView mv = new ModelAndView(tplViewName, model);
		// get the template path from cms system property.
		String templatePath = CMSConfig.getInstance().getUserTemplatePath();
		//
		String subdir = helper.getString("PATH");
		String extra = request.getParameter("extra");

		if (subdir != null) {
			if (extra != null && extra.equals("updir")) {
				int pos = subdir.lastIndexOf("/");
				if (pos > -1) {
					subdir = subdir.substring(0, pos);
				}
			}
			templatePath += File.separator + subdir;
		}
		//
		templatePath = StringUtil.normalizePath(templatePath);

		File dir = new File(templatePath);
		List fileList = new ArrayList();
		if (dir.isDirectory() && dir.canRead()) {
			File files[] = dir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					String filename = pathname.getName();
					// only accept the directory or *.html
					if (pathname.isDirectory() || filename.endsWith(".html")
							|| filename.endsWith(".js")) {
						return true;
					}
					return false;
				}
			});

			if (files != null) {
				// sort
				Arrays.sort(files, new java.util.Comparator() {
					public int compare(Object o1, Object o2) {
						File file1 = (File) o1;
						File file2 = (File) o2;
						if (file1.isDirectory() && file2.isFile()) {
							return -1;
						}
						if (file1.isFile() && file2.isDirectory()) {
							return 1;
						}
						if ((file1.isDirectory() && file2.isDirectory())
								|| (file1.isFile() && file2.isFile())) {
							return file1.getName().compareToIgnoreCase(
									file2.getName());
						}
						return 0;
					}

					public boolean equals(Object obj) {
						return false;
					}
				});
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					String name = file.getName();
					String type = FileUtil.getContentType(name);
					String icon = FileUtil.getIcon2(file);
					fileList.add(new FileItem(file.isDirectory(), name, type,
							icon));
				}
			}
			//
		}
		model.put("fileList", fileList);
		model.put("PATH", subdir);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return ModelAndView
	 */
	public ModelAndView doSelTplDirDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		ModelAndView mv = new ModelAndView(this.tplDirSelDialogViewName, model);
		return mv;
	}

	public ModelAndView doSelTplDir(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(tplDirSelViewName, model);
		List folders = templateManager.getChildFolders("");
		model.put("folders", folders);
		return mv;

	}

	public ModelAndView doSelTplDirXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String path = helper.getString("path");
		//
		ModelAndView mv = new ModelAndView(tplDirSelXmlViewName, model);
		List folders = templateManager.getChildFolders(path);
		model.put("folders", folders);
		//
		setNoCacheHeader(response);
		model.put("responseType", "text/xml");

		return mv;

	}

	public void setTemplateManager(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setTemplateListViewName(String templateListViewName) {
		this.templateListViewName = templateListViewName;
	}

	public void setTemplateOperationViewName(String templateOperationViewName) {
		this.templateOperationViewName = templateOperationViewName;
	}

	public void setTemplateUploadViewName(String templateUploadViewName) {
		this.templateUploadViewName = templateUploadViewName;
	}

	public void setTemplateXmlViewName(String templateXmlViewName) {
		this.templateXmlViewName = templateXmlViewName;
	}

	// ////////////////////////////////////////////////////////////////////////////
	protected String getAcceptFileType() {
		String imgTypes = CMSConfig.getInstance().getUploadFileImageType();
		String flashTypes = CMSConfig.getInstance().getUploadFileFlashType();
		String attachTypes = CMSConfig.getInstance().getUploadFileAttachType();
		String mediaTypes = CMSConfig.getInstance().getUploadFileMediaType();
		String otherTypes = "css|js|txt|html|htm|shtml|xml|bak|pdf";
		return imgTypes + "|" + flashTypes + "|" + attachTypes + "|"
				+ mediaTypes + "|" + otherTypes;
	}

	protected File getUploadDestFile(File destFile, String destName, int i) {

		if (destFile.exists()) {
			String newName = destName + "(" + i + ")";
			int pos = destName.lastIndexOf(".");
			if (pos > -1) {
				String name = destName.substring(0, pos);
				String extension = destName.substring(pos + 1);
				newName = name + "(" + i + ")." + extension;
			}
			File tryFile = new File(destFile.getParentFile(), newName);
			if (tryFile.exists()) {
				return getUploadDestFile(destFile, destName, i + 1);
			} else {
				return tryFile;
			}
		} else {
			return destFile;
		}
	}

	private String getPath(String tplName) {
		int pos = tplName.lastIndexOf("/");
		if (pos != -1) {
			return tplName.substring(0, pos);
		} else {
			return "/";
		}
	}

	private String getTplFileName(String tplName) {
		int pos = tplName.lastIndexOf("/");
		if (pos != -1) {
			return tplName.substring(pos + 1);
		} else {
			return tplName;
		}
	}

}
