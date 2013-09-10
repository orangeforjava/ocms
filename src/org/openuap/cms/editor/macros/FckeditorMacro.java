/**
 * $Id: FckeditorMacro.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
package org.openuap.cms.editor.macros;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Joseph
 * 
 */
public interface FckeditorMacro {
	public static final String FILE_UPLOAD = "FileUpload";
	public static final String FILE_BROWSING = "FileBrowsing";
	public static final String COMPATIBLE_BROWSER = "CompatibleBrowser";
	public static final String PROPERTY_MESSAGE_FILE_BROWSING_DISABLED = "message.enabled_tag.connector.file_browsing.disabled";
	public static final String PROPERTY_MESSAGE_FILE_BROWSING_ENABLED = "message.enabled_tag.connector.file_browsing.enabled";
	public static final String PROPERTY_MESSAGE_FILE_UPLOAD_DISABLED = "message.enabled_tag.connector.file_upload.disalbed";
	public static final String PROPERTY_MESSAGE_FILE_UPLOAD_ENABLED = "message.enabled_tag.connector.file_upload.enabled";
	public static final String PROPERTY_MESSAGE_NOT_COMPATIBLE_BROWSER = "message.enabled_tag.compatible_browser.no";
	public static final String PROPERTY_MESSAGE_COMPATIBLE_BROWSER = "message.enabled_tag.compatible_browser.yes";

	public String checkTag(HttpServletRequest request, String command);

	public void configTag(HttpServletRequest request, Map params);

	public String editorTag(HttpServletRequest request, String instanceName,
			String width, String height, String toolbarSet, String value,
			String basePath,String type,Map params);
}
