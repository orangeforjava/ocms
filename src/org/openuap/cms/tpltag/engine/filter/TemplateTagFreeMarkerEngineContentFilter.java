/*
 * Copyright 2005-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openuap.cms.tpltag.engine.filter;

import java.util.HashMap;
import java.util.Map;

import org.openuap.cms.tpltag.engine.TemplateTagEngine;
import org.openuap.tpl.engine.plugin.FreeMarkerEngineContentFilter;

import freemarker.template.Configuration;

/**
 * <p>
 * 模板标记
 * </p>
 * 
 * <p>
 * $Id: TemplateTagFreeMarkerEngineContentFilter.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateTagFreeMarkerEngineContentFilter implements
		FreeMarkerEngineContentFilter {

	private int priority = 5;
	private TemplateTagEngine templateTagEngine;

	public String afterProcessContent(String tplContent, Map model,
			Configuration configuration) {
		return null;
	}

	public String beforeProcessContent(String tplContent, Map model,
			Configuration configuration) {
		return null;
	}

	public Map getMacroDefinitions() {
		Map tagMacros = new HashMap();
		tagMacros.put("tag", "/plugin/cms/tpltag/macros/tpltag-macros.html");
		//
		return tagMacros;
	}

	public int getPriority() {
		return priority;
	}

	public Map getSharedVariables(String tplContent, Map model,
			Configuration configuration) {
		Map<String, Object> inModel = new HashMap<String, Object>();
		inModel.put("templateTagEngine", templateTagEngine);
		return inModel;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setTemplateTagEngine(TemplateTagEngine templateTagEngine) {
		this.templateTagEngine = templateTagEngine;
	}

}
