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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Compatibility check.
 * 
 * @version $Id: Compatibility.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class Compatibility {

	/**
	 * Checks, if a browser's user agent string is compatible for the FCKeditor. <br>
	 * Adapted from: http://dev.fckeditor.net/browser/FCKeditor/releases/stable/fckeditor.php
	 * 
	 * @param userAgentString
	 * @return true, if compatible, otherwise false
	 */
	public static boolean check(final String userAgentString) {
		if (Utils.isEmpty(userAgentString))
			return false;

		float version;

		// IE 5.5+, check special keys like 'Opera' and 'mac', because there are some
		// other browsers, containing 'MSIE' in there agent string!
		if (userAgentString.indexOf("Opera") < 0 && userAgentString.indexOf("mac") < 0) {
			version = getBrowserVersion(userAgentString, ".*MSIE ([\\d]+.[\\d]+).*");
			if (version != -1f && version >= 5.5f)
				return true;
		}

		// for mozilla only, because all firefox versions are supported
		version = getBrowserVersion(userAgentString, ".*Gecko/([\\d]+).*");
		if (version != -1f && version >= 20030210f)
			return true;

		// Opera 9.5+
		version = getBrowserVersion(userAgentString, "Opera/([\\d]+.[\\d]+).*");
		if (version != -1f && version >= 9.5f)
			return true;
		version = getBrowserVersion(userAgentString, ".*Opera ([\\d]+.[\\d]+)");
		if (version != -1f && version >= 9.5f)
			return true;

		// Safari 3+
		version = getBrowserVersion(userAgentString, ".*AppleWebKit/([\\d]+).*");
		if (version != -1f && version >= 522f)
			return true;

		return false;
	}

	/**
	 * Just a wrapper to {@link #check(String)}.
	 * 
	 * @param request
	 */
	public static boolean isCompatibleBrowser(final HttpServletRequest request) {
		return (request == null) ? false : check(request.getHeader("user-agent"));
	}

	/**
	 * Helper method to get the the browser version from 'userAgent' with the regular expression
	 * 'regex'. The first group of the matches has to be the version number!
	 * 
	 * @param userAgent
	 * @param regex
	 * @return The browser version, or -1f, if version con't find out.
	 */
	private static float getBrowserVersion(final String userAgent, final String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(userAgent);
		if (matcher.matches()) {
			try {
				return Float.parseFloat(matcher.group(1));
			} catch (NumberFormatException e) {
				return -1f;
			}
		}
		return -1f;
	}

}
