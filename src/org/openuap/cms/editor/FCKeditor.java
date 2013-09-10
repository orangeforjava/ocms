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

import javax.servlet.http.HttpServletRequest;

import org.openuap.cms.editor.handlers.PropertiesLoader;
import org.openuap.cms.editor.tool.Compatibility;
import org.openuap.cms.editor.tool.Utils;
import org.openuap.cms.editor.tool.XHtmlTagTool;

/**
 * FCKeditor control class.<br>
 * 
 * It creates the html code for the FCKeditor based on the following things:
 * <ul>
 * <li>browser's capabilities</li>
 * <li>different properties settings managed by the {@link PropertiesLoader}</li>
 * <li>settings from the 'caller', eg. jsp-pages</li>
 * </ul>
 * 
 * @version $Id: FCKeditor.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class FCKeditor {

	private FCKeditorConfig config;
	private String instanceName;
	private String value;
	private String basePath;
	private String type;
	//
	private HttpServletRequest request;

	// defaults
	private String toolbarSet = PropertiesLoader.getProperty("fckeditor.toolbarSet");
	private String width = PropertiesLoader.getProperty("fckeditor.width");
	private String height = PropertiesLoader.getProperty("fckeditor.height");
	private String defaultBasePath = PropertiesLoader.getProperty("fckeditor.basePath");
	
	/**
	 * Main constructor.<br>
	 * All important settings are done here and will be preset by there defaults taken from
	 * {@link PropertiesLoader}.
	 * 
	 * @param request
	 *            request object
	 * @param instanceName
	 *            unique name
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param toolbarSet
	 *            toolbarSet name
	 */
	public FCKeditor(final HttpServletRequest request, final String instanceName,
	        final String width, final String height, final String toolbarSet, final String value,
	        final String type,final String basePath) {
		this.request = request;
		this.instanceName = instanceName;
		if (Utils.isNotEmpty(width))
			this.width = width;
		if (Utils.isNotEmpty(height))
			this.height = height;
		if (Utils.isNotEmpty(toolbarSet))
			this.toolbarSet = toolbarSet;
		if (Utils.isNotEmpty(value))
			this.value = value;
		if (Utils.isNotEmpty(basePath))
			this.basePath = request.getContextPath().concat(basePath);
		else
			this.basePath = request.getContextPath().concat(defaultBasePath);
		if(Utils.isNotEmpty(type)){
			this.type=type;
		}
		config = new FCKeditorConfig();
	}

	/**
	 * Just a wrapper to {@link FCKeditor}.
	 * 
	 * @param request
	 *            request object
	 * @param instanceName
	 *            unique name
	 */

	public FCKeditor(final HttpServletRequest request, final String instanceName) {
		this(request, instanceName, null, null, null, null,null, null);
	}

	/**
	 * Set the unique name of the editor
	 * 
	 * @param value
	 *            name
	 */
	public void setInstanceName(final String value) {
		instanceName = value;
	}

	/**
	 * Set the initial value to be edited.<br>
	 * In HTML code
	 * 
	 * @param value
	 *            value
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * Set the dir where the FCKeditor files reside on the server.<br>
	 * <b>Remarks</b>:<br>
	 * Avoid using relative paths. It is preferable to set the base path starting from the root (/).<br>
	 * Always finish the path with a slash (/).
	 * 
	 * @param value
	 *            path
	 */
	public void setBasePath(final String value) {
		basePath = value;
	}

	/**
	 * Set the name of the toolbar to display
	 * 
	 * @param value
	 *            toolbar name
	 */
	public void setToolbarSet(final String value) {
		toolbarSet = value;
	}

	/**
	 * Set the width of the textarea
	 * 
	 * @param value
	 *            width
	 */
	public void setWidth(final String value) {
		width = value;
	}

	/**
	 * Set the height of the textarea
	 * 
	 * @param value
	 *            height
	 */
	public void setHeight(final String value) {
		height = value;
	}

	/**
	 * Get the advanced configuation set.<br>
	 * Adding element to this collection you can override the settings specified in the config.js
	 * file.
	 * 
	 * @return configuration collection
	 */
	public FCKeditorConfig getConfig() {
		return config;
	}

	/**
	 * Set the advanced configuation set.
	 * 
	 * @param value
	 *            configuration collection
	 */
	public void setConfig(FCKeditorConfig value) {
		config = value;
	}

	private String escapeXml(String txt) {
		if (Utils.isEmpty(txt))
			return txt;
		txt = txt.replaceAll("&", "&#38;");
		txt = txt.replaceAll("<", "&#60;");
		txt = txt.replaceAll(">", "&#62;");
		txt = txt.replaceAll("\"", "&#34;");
		txt = txt.replaceAll("'", "&#39;");
		return txt;
	}

	/**
	 * Minimum implementation, see ticket #27 for detailed information.
	 */
	public String create() {
		return createHtml();
	}

	@Override
	public String toString() {
		return createHtml();
	}

	/**
	 * Generate the HTML Code for the editor. <br>
	 * Evalute the browser capabilities and generate the editor if compatible, or a simple textarea
	 * otherwise.
	 * 
	 * @return html code
	 */
	public String createHtml() {
		StringBuffer strEditor = new StringBuffer();

		strEditor.append("<div>");
		String encodedValue = escapeXml(value.replaceAll("((\r?\n)+|\t*)", ""));

		if (Compatibility.check(request.getHeader("user-agent"))) {
			strEditor.append(createInputForVariable(instanceName, instanceName, encodedValue));

			// create config html
			String configStr = config.getUrlParams();
			if (Utils.isNotEmpty(configStr))
				// configStr = configStr.substring(1);
				strEditor.append(createInputForVariable(null, instanceName.concat("___Config"),
				        configStr));

			// create IFrame
			String sLink = basePath.concat("/editor/"+type+".html?InstanceName=").concat(
			        instanceName);
			//System.out.println("sLink="+sLink);
			if (Utils.isNotEmpty(toolbarSet))
				sLink += "&Toolbar=".concat(toolbarSet);
			XHtmlTagTool iframeTag = new XHtmlTagTool("iframe", XHtmlTagTool.SPACE);
			iframeTag.addAttribute("id", instanceName.concat("___Frame"));
			iframeTag.addAttribute("src", sLink);
			iframeTag.addAttribute("width", width);
			iframeTag.addAttribute("height", height);
			iframeTag.addAttribute("frameborder", "no");
			iframeTag.addAttribute("scrolling", "no");
			strEditor.append(iframeTag);

		} else {
			XHtmlTagTool textareaTag = new XHtmlTagTool("textarea", encodedValue);
			textareaTag.addAttribute("name", instanceName);
			textareaTag.addAttribute("rows", "4");
			textareaTag.addAttribute("cols", "40");
			textareaTag.addAttribute("wrap", "virtual");
			textareaTag.addAttribute("style", "width: ".concat(width).concat("; height: ").concat(
			        height));
		}
		strEditor.append("</div>");
		return strEditor.toString();
	}

	private String createInputForVariable(final String name, final String id, final String value) {
		XHtmlTagTool tag = new XHtmlTagTool("input");
		if (Utils.isNotEmpty(id))
			tag.addAttribute("id", id);
		if (Utils.isNotEmpty(name))
			tag.addAttribute("name", name);
		tag.addAttribute("value", value);
		tag.addAttribute("type", "hidden");
		return tag.toString();
	}
	
}
