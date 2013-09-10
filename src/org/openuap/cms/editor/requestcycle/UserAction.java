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
package org.openuap.cms.editor.requestcycle;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface that provides the authorization of the following file based user actions:
 * <ul>
 * <li>{@link #isEnabledForFileBrowsing(HttpServletRequest)}: Enables the user to browse/select
 * file.</li>
 * <li>{@link #isEnabledForFileUpload(HttpServletRequest)}: Enables the user to upload files.</li>
 * </ul>
 * 
 * @version $Id: UserAction.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public interface UserAction {

	/**
	 * Authenticates/enables the current user for uploading file.<br>
	 * If the implementation doesn't bother you, just return <code>true</code>.
	 * 
	 * @param request
	 *            Servlet request from user
	 * @return <code>true</code> if user can upload to the server else <code>false</code>
	 */
	public boolean isEnabledForFileUpload(final HttpServletRequest request);

	/**
	 * Authenticates/enables the current user for browsing files.<br>
	 * If the implementation doesn't bother you, just return <code>true</code>.
	 * 
	 * @param request
	 *            Servlet request from user
	 * @return <code>true</code> if user can browse the server else <code>false</code>
	 */
	public boolean isEnabledForFileBrowsing(final HttpServletRequest request);

}
