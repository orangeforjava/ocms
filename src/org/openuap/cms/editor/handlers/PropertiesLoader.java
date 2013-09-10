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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openuap.cms.editor.tool.Utils;




/**
 * Handler to hold the basic properties.<br>
 * The main default file is 'default.properties' in the deepth of the classpath and should be
 * untouched. If there is a file named 'fckeditor.properties' in the root of the classpath, it will
 * be loaded. Values which are loaded before, will be overwritten.<br>
 * If you won't use an extra properties file to adjust the defaults, you can use
 * {@link #setProperty(String, String)} instead.
 * 
 * @version $Id: PropertiesLoader.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class PropertiesLoader {
	private static final Log logger = LogFactory.getLog(PropertiesLoader.class);
	private static Properties properties = new Properties();

	static {
		try {
			// 1. load system defaults
			properties.load(new BufferedInputStream(PropertiesLoader.class
			        .getResourceAsStream("default.properties")));

			// 2. load user defaults
			InputStream in = PropertiesLoader.class.getResourceAsStream("/fckeditor.properties");
			if (in == null)
				logger.warn("Can't find user properties!");
			else {
				try {
					properties.load(new BufferedInputStream(in));
					logger.info("User's properties loaded successfully!");
				} catch (IOException e) {
					logger.error("Error while loading user properties!", e);
					throw new RuntimeException("Can't load user properties!", e);
				}
			}
		} catch (IOException e) {
			logger.error("Error while loading default properties!", e);
			throw new RuntimeException("Can't load default properties!", e);
		}
	}

	/**
	 * Getter for a property of 'key'.
	 * 
	 * @param key
	 *            the propery key
	 * @return the value in this property list with the specified key value.
	 * @see Properties#getProperty(String)
	 */
	public static String getProperty(final String key) {
		return properties.getProperty(key);
	}

	/**
	 * Setter for a property. If the property already exists, the value will be overwritten.<br>
	 * Hint: This method is intended for an alternative way to set user defaults programmatically
	 * instead of using the 'fckeditor.properties'. It should never used inside FCKeditor.Java !!!
	 * 
	 * @param key
	 *            key the propery key
	 * @param value
	 * @throws IllegalArgumentException
	 *             if 'key' is empty.
	 * @see Properties#setProperty(String, String)
	 */
	public static void setProperty(final String key, final String value) {
		if (Utils.isEmpty(key))
			new IllegalArgumentException("The 'key' of a property schouldn't be null!");
		properties.setProperty(key, value);
	}
}
