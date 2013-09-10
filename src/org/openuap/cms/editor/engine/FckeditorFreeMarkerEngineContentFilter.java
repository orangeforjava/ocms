/**
 * $Id: FckeditorFreeMarkerEngineContentFilter.java 3992 2011-01-05 06:34:18Z orangeforjava $
 */
package org.openuap.cms.editor.engine;

import java.util.HashMap;
import java.util.Map;

import org.openuap.cms.editor.macros.FckeditorMacro;
import org.openuap.tpl.engine.plugin.FreeMarkerEngineContentFilter;

import freemarker.template.Configuration;

/**
 * fckeditor内容过滤器
 * 
 * @author Joseph
 * 
 */
public class FckeditorFreeMarkerEngineContentFilter implements
		FreeMarkerEngineContentFilter {

	private int priority = 5;
	

	private FckeditorMacro fckeditorMacro;

	public String afterProcessContent(String tplContent, Map model,
			Configuration configuration) {
		return tplContent;
	}

	public String beforeProcessContent(String tplContent, Map model,
			Configuration configuration) {
		return tplContent;
	}

	public Map getMacroDefinitions() {
		Map cmsMacros = new HashMap();
		cmsMacros.put("editor", "/plugin/cms/base/macros/fckeditorMacros.html");
		return cmsMacros;
	}

	public Map getSharedVariables(String tplContent, Map model,
			Configuration configuration) {
		Map<String, Object> inModel = new HashMap<String, Object>();
		
		inModel.put("fckeditorMacro", fckeditorMacro);
		//
		return inModel;
	}
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setFckeditorMacro(FckeditorMacro fckeditorMacro) {
		this.fckeditorMacro = fckeditorMacro;
	}

}
