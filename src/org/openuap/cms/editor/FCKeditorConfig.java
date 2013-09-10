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

package org.openuap.cms.editor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openuap.cms.editor.tool.Utils;

/**
 * Contains the configuration settings for the FCKEditor.<br>
 * Adding element to this collection you can override the settings specified in
 * the config.js file.
 * 
 * @version $Id: FCKeditorConfig.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class FCKeditorConfig extends HashMap<String, String> {

	private static final long serialVersionUID = -4831190504944866644L;
	private final Log logger = LogFactory.getLog(FCKeditorConfig.class);

	/**
	 * Initialize the configuration collection
	 */
	public FCKeditorConfig() {
		super();
	}

	/**
	 * Generate the url parameter sequence used to pass this configuration to
	 * the editor.
	 * 
	 * @return html endocode sequence of configuration parameters
	 */
	public String getUrlParams() {
		StringBuffer osParams = new StringBuffer();
		try {
			for (Map.Entry<String, String> entry : this.entrySet()) {
				if (Utils.isNotEmpty(entry.getValue())) {
					osParams.append("&");
					osParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
					osParams.append("=");
					osParams.append(URLEncoder
							.encode(entry.getValue(), "UTF-8"));
				}
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("Configuration parameters could not be encoded", e);
		}

		if (osParams.length() > 0)
			osParams.deleteCharAt(0);
		return osParams.toString();
	}
}
