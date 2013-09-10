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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openuap.cms.editor.requestcycle.UserAction;
import org.openuap.cms.editor.requestcycle.UserPathBuilder;



/**
 * Handler for classes which implement the interfaces from the package
 * {@link net.fckeditor.requestcycle}.
 * 
 * @version $Id: RequestCycleHandler.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class RequestCycleHandler {
	private static Log logger = LogFactory.getLog(RequestCycleHandler.class);
	private static UserAction userAction = null;
	private static UserPathBuilder userPathBuilder = null;

	static {
		// If there are more objects to instantiate in future, we could solve the following by reflection!
		
		// 1. try to instantiate the UserAction object
		String fqcn = PropertiesLoader.getProperty("connector.userActionImpl");
		if (fqcn == null)
			logger.warn("No property found for UserAction implementation, any user action is disabled!");
		else {
			try {
				Class<?> clazz = Class.forName(fqcn);
				userAction = (UserAction) clazz.newInstance();
				logger.info("UserAction object successful instanciated!");
			} catch (Exception e) {
				logger.error("Couldn't instanciate the class [".concat(fqcn).concat(
				        "], any user action of the ConnectorServlet is disabled!"), e);
			}
		}

		// 2. try to instantiate the UserPathBuilder object
		fqcn = PropertiesLoader.getProperty("connector.userPathBuilderImpl");
		if (fqcn == null)
			logger.warn("No property found for UserPathBuilder implementation! "
					.concat("The default of users's 'BaseDir' will be used in the ConnectorServlet!"));
		else {
			try {
				Class<?> clazz = Class.forName(fqcn);
				userPathBuilder = (UserPathBuilder) clazz.newInstance();
				logger.info("UserPathBuilder object successfull instanciated!");
			} catch (Exception e) {
				logger.error("Couldn't instanciate the class [".concat(fqcn)
				        .concat("], The default of users's 'BaseDir' will be used in the ConnectorServlet!"), e);
			}
		}
	}

	/**
	 * Just a wrapper to {@link UserAction#isEnabledForFileBrowsing(HttpServletRequest)}.
	 * 
	 * @param servletRequest
	 * @return {@link UserAction#isEnabledForFileBrowsing(HttpServletRequest)} or false, if
	 *         sessionData isn't set.
	 */
	public static boolean isEnabledForFileBrowsing(final HttpServletRequest servletRequest) {
		return (userAction != null && userAction.isEnabledForFileBrowsing(servletRequest));
	}

	/**
	 * Just a wrapper to {@link UserAction#isEnabledForFileUpload(HttpServletRequest)}.
	 * 
	 * @param request
	 * @return {@link UserAction#isEnabledForFileUpload(HttpServletRequest)} or false, if userAction
	 *         isn't set.
	 */
	public static boolean isEnabledForFileUpload(final HttpServletRequest request) {
		return (userAction != null && userAction.isEnabledForFileUpload(request));
	}

	/**
	 * Getter for the user's file path.<br>
	 * Method is used by other handlers only!
	 * 
	 * @param request
	 * @return {@link UserPathBuilder#getUserFilesPath(HttpServletRequest)} or null, if
	 *         userPathBuilder is null.
	 */
	protected static String getUserFilePath(final HttpServletRequest request) {
		return (userPathBuilder != null) ? userPathBuilder.getUserFilesPath(request) : null;
	}
}
