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

import org.openuap.cms.editor.handlers.ConnectorHandler;

/**
 * Interface to provide a way to build a user dependent 'BaseDir.
 *
 * @version $Id: UserPathBuilder.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public interface UserPathBuilder {

	/**
	 * Getter for the user dependent 'BaseDir'.<br>
	 * <b>Important:</b>
	 * <ul>
	 * <li> If the implementation returns <code>null</code>, {@link ConnectorHandler} will used
	 * the default one! That's useful, if the implementation doesn't bother you.</li>
	 * <li>The returned directory string has to start with '/', but has to end without '/'.</li>
	 * <li>The dir has to be within the context</li>
	 * </ul>
	 * 
	 * @param request
	 * @return <code>null</code> or the 'BaseDir' string for the current user.
	 */
	public String getUserFilesPath(final HttpServletRequest request);
}
