/**
 * $Id: DefaultFckeditorMacro.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
package org.openuap.cms.editor.macros.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openuap.cms.editor.FCKeditor;
import org.openuap.cms.editor.handlers.PropertiesLoader;
import org.openuap.cms.editor.handlers.RequestCycleHandler;
import org.openuap.cms.editor.macros.FckeditorMacro;
import org.openuap.cms.editor.tool.Compatibility;

/**
 * @author Joseph
 * 
 */
public class DefaultFckeditorMacro implements FckeditorMacro {

	private static Set<String> commands = new HashSet<String>(3);
	static {
		commands.add(FILE_UPLOAD);
		commands.add(FILE_BROWSING);
		commands.add(COMPATIBLE_BROWSER);
	}

	public String checkTag(HttpServletRequest request, String command) {
		String response = new String();

		if (command.equals(FILE_UPLOAD)) {
			if (RequestCycleHandler.isEnabledForFileUpload(request))
				response = PropertiesLoader
						.getProperty(PROPERTY_MESSAGE_FILE_UPLOAD_ENABLED);
			else
				response = PropertiesLoader
						.getProperty(PROPERTY_MESSAGE_FILE_UPLOAD_DISABLED);
		}

		if (command.equals(FILE_BROWSING)) {
			if (RequestCycleHandler.isEnabledForFileBrowsing(request))
				response = PropertiesLoader
						.getProperty(PROPERTY_MESSAGE_FILE_BROWSING_ENABLED);
			else
				response = PropertiesLoader
						.getProperty(PROPERTY_MESSAGE_FILE_BROWSING_DISABLED);
		}
		if (command.equals(COMPATIBLE_BROWSER)) {
			if (Compatibility.isCompatibleBrowser(request))
				response = PropertiesLoader
						.getProperty(PROPERTY_MESSAGE_COMPATIBLE_BROWSER);
			else
				response = PropertiesLoader
						.getProperty(PROPERTY_MESSAGE_NOT_COMPATIBLE_BROWSER);
		}
		return response;
	}

	public void configTag(HttpServletRequest request, Map params) {
		// TODO Auto-generated method stub

	}

	public String editorTag(HttpServletRequest request, String instanceName,
			String width, String height, String toolbarSet, String value,
			String basePath,String type, Map params) {
		FCKeditor fckEditor = new FCKeditor(request, instanceName, width,
				height, toolbarSet, value,type, basePath);
		fckEditor.setValue(value);
		if (params != null) {
			fckEditor.getConfig().putAll(params);
		}
		return fckEditor.toString();
	}

}
