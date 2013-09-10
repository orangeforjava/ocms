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

package org.openuap.cms.editor.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.openuap.cms.editor.FCKeditor;

/**
 * TODO check, if usage sample in comment is correct !!! (Advanced usage) <br><br>
 * 
 * FCKeditor Tag class to access the
 * {@linkplain net.fckeditor.FCKeditor container}.<br>
 * <p>
 * <b>Simple usage</b>:
 * 
 * <pre>
 * &lt;FCK:editor
 * 	instanceName=&quot;EditorAccessibility&quot;
 * 	width=&quot;80%&quot;
 * 	height=&quot;120&quot;
 * 	toolbarSet=&quot;Accessibility&quot;
 * &quot;&gt;This is another test. &lt;BR&gt;&lt;BR&gt;The &quot;Second&quot; row.&lt;/BR&gt;&lt;/FCK:editor&quot;&gt;
 * </pre>
 * 
 * <p>
 * In this example we set all the attribute for the fckedit tag.
 * 
 * <p>
 * <b>Advanced usage of the tag</b>:
 * 
 * <pre>
 * &lt;FCK:editor instanceName=&quot;EditorDefault&quot; basePath=&quot;/fckeditor/&quot;
 * 	styleNames=&quot;;Style 1;Style 2; Style 3&quot; 
 * 	fontNames=&quot;;Arial;Courier New;Times New Roman;Verdana&quot; &gt;
 * 	This is some &lt;B&gt;sample text&lt;/B&gt;.
 * &lt;/FCK:editor&gt;
 * </pre>
 * 
 * In this example we set the id and the basePath of the editor (since it is
 * /fckeditor/ we could have omitted it because it's already the default value).<br>
 * Then we used the the optional attributes to set some advanced configuration
 * settings.
 * 
 * @version $Id: EditorTag.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class EditorTag extends TagSupport {

	private static final long serialVersionUID = -173091731589866140L;

	private String instanceName;
	private String width;
	private String height;
	private String toolbarSet;
	private String value;
	private String type;
	
	private String basePath;

	/** The underlying FCKeditor object */
	private FCKeditor fckEditor;

	/**
	 * Sets the name for the given editor instance
	 * 
	 * @param instanceName
	 *            some name without whitespaces
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * Sets the width of the textarea
	 * 
	 * @param width
	 *            width of the editor instance
	 * 
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * Sets the height of the textarea
	 * 
	 * @param height
	 *            height of the editor instance
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * Sets the name of the toolbar to display
	 * 
	 * @param toolbarSet
	 *            toolbar set of the editor instance
	 */
	public void setToolbarSet(String toolbarSet) {
		this.toolbarSet = toolbarSet;
	}

	/**
	 * Sets the editor document content
	 * 
	 * @param value
	 *            any HTML string
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Sets the dir where the FCKeditor files reside on the server
	 * 
	 * @param basePath
	 *            basePath of the editor instance (e.g. /fckeditor)
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	void setConfigParamAll(Map<String, String> map) {
		fckEditor.getConfig().putAll(map);
	}

	/**
	 * Initializes the FCKeditor container and Sets attributes
	 * 
	 * @return EVAL_BODY_INCLUDE
	 */
	public int doStartTag() throws JspException {
		fckEditor = new FCKeditor(
				(HttpServletRequest) pageContext.getRequest(), instanceName,
				width, height, toolbarSet, value, type,basePath);
		fckEditor.setValue(value);

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {

		JspWriter out = pageContext.getOut();

		try {
			out.println(fckEditor);
		} catch (IOException ioe) {
			throw new JspException(
					"Error: IOException while writing to the user");
		}

		return EVAL_PAGE;
	}

	public void setType(String type) {
		this.type = type;
	}

}