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
package org.openuap.cms.editor.handlers;

import javax.servlet.http.HttpServletRequest;

import org.openuap.cms.editor.requestcycle.UserPathBuilder;

/**
 * Handler for some base properties.<br>
 * It's a kind of wrapper to some basic properties handled by the {@link PropertiesLoader}.
 * 
 * @version $Id: ConnectorHandler.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class ConnectorHandler {

	/**
     * Getter for the base dir (using for user files).
     * 
     * @return {@link UserPathBuilder#getUserFilesPath(HttpServletRequest)} or the default base dir, if
     *         {@link UserPathBuilder}} isn't set.
     */
    public static String getUserFilesPath(final HttpServletRequest servletRequest) {
    	String userFilePath = RequestCycleHandler.getUserFilePath(servletRequest);
    	return (userFilePath != null) ? userFilePath : getDefaultUserFilesPath();
    }

	/**
	 * Getter for the default handling of single extensions.
	 * 
	 * @return the forceSingleExtension
	 */
	public static boolean isForceSingleExtension() {
		return Boolean.valueOf(PropertiesLoader.getProperty("connector.forceSingleExtension"));
	}

	/**
	 * Getter for the value to instruct the connector to return the full URL of a file/folder in the
	 * XML response rather than the absolute URL.
	 * 
	 * @return Boolean value of the property 'connector.fullUrl'.
	 */
	public static boolean isFullUrl() {
		return Boolean.valueOf(PropertiesLoader.getProperty("connector.fullUrl"));
	}

	/**
	 * Getter for the default userFilesPath.
	 * 
	 * @return Default userfiles path (/userfiles)
	 */
	public static String getDefaultUserFilesPath() {
		return PropertiesLoader.getProperty("connector.userFilesPath");
	}
	
	/**
	 * Getter for the value to instruct the Connector to check, if the uploaded image is really one.
	 * 
	 * @return Boolean value of the property 'connector.secureImageUploads'.
	 */
	public static boolean isSecureImageUploads() {
		return Boolean.valueOf(PropertiesLoader.getProperty("connector.secureImageUploads"));
	}
}
