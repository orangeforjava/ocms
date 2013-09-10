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
package org.openuap.cms.editor.tool;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openuap.cms.editor.handlers.ConnectorHandler;
import org.openuap.cms.editor.util.ImageInfo;


/**
 * Some static helper methods in conjunction with files.
 *
 * @version $Id: UtilsFile.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class UtilsFile {
	
	private static final Log logger = LogFactory.getLog(UtilsFile.class);

	/**
     * Do a cleanup of the file name to avoid possible problems. <br>
     * The force single Extension property will be respected!
     * 
     * @param fileName
     * @return folder name where \ / | : ? * &quot; &lt; &gt; 'control chars' replaced by '_'
     */
    public static String sanitizeFileName(final String fileName) {
    	if (fileName == null)
    		return null;
    	if (fileName.equals(""))
    		return "";
    
    	String name = (ConnectorHandler.isForceSingleExtension()) ? UtilsFile.forceSingleExtension(fileName)
    	        : fileName;
    
    	// Remove \ / | : ? * " < > with _
    	return name.replaceAll("\\/|\\/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
    }

	/**
     * Do a cleanup of the folder name to avoid possible problems.
     * 
     * @param folderName
     * @return folder name where . \ / | : ? * &quot; &lt; &gt; 'control chars' replaced by '_'
     */
    public static String sanitizeFolderName(final String folderName) {
    	if (folderName == null)
    		return null;
    	if (folderName.equals(""))
    		return "";
    
    	// Remove . \ / | : ? * " < > with _
    	return folderName.replaceAll("\\.|\\/|\\/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
    }

	/**
	 * Checks if the underlying file of the InputStrem is an image.
	 * 
	 * @param in
	 * @return <code>True</code>, if the underlying file is an image, or <code>false</code>.
	 */
	public static boolean isImage(final InputStream in) {
    	ImageInfo ii = new ImageInfo();
    	ii.setInput(in);
    	return ii.check();
    }

	/**
     * TODO - document me!
     * 
     * @param path
     * @return <code>true</code> if path corresponds to rules or
     *         <code>false</code>.
     */
    public static boolean isValidPath(final String path) {
    	if (Utils.isEmpty(path))
    		return false;
    	if (!path.startsWith("/"))
    		return false;
    	if (!path.endsWith("/"))
    		return false;
    	
    	if (!path.equals(FilenameUtils.separatorsToUnix(FilenameUtils
    			.normalize(path))))
    		return false;
    	
    	return true;
    }

	/**
     * TODO - document me!
     * 
     * @param filename
     * @return string with a single dot only
     */
    public static String forceSingleExtension(final String filename) {
    	return filename.replaceAll("\\.(?![^.]+$)", "_");
    }

	/**
     * TODO - document me!
     * 
     * @param filename
     * @return <code>true</code> if filename contains severals dots else
     *         <code>false</code>
     */
    public static boolean isSingleExtension(final String filename) {
    	return filename.matches("[^\\.]+\\.[^\\.]+");
    }
    
    /**
	 * TODO - document me!
	 * @param dir
	 */
	public static void checkDirAndCreate(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
			logger.debug("Dir '"+dir+"' successfully created");
		}
	}

}
