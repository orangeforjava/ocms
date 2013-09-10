/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.survey.engine.filter;

import java.util.HashMap;
import java.util.Map;

import org.openuap.cms.survey.engine.SurveyAreaEngine;
import org.openuap.cms.survey.engine.SurveyEngine;
import org.openuap.tpl.engine.plugin.FreeMarkerEngineContentFilter;

import freemarker.template.Configuration;

/**
 * <p>
 * 调查问卷内容过滤器
 * </p>
 * 
 * <p>
 * $Id: SurveyAreaFreeMarkerEngineContentFilter.java 3951 2010-11-02 10:13:17Z
 * orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class SurveyFreeMarkerEngineContentFilter implements
		FreeMarkerEngineContentFilter {

	private int priority = 5;
	private SurveyAreaEngine surveyAreaEngine;
	private SurveyEngine surveyEngine;
	
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
		tagMacros.put("vote", "/plugin/cms/survey/macros/survey-macros.html");
		//
		return tagMacros;
	}

	public int getPriority() {
		return priority;
	}

	public Map getSharedVariables(String tplContent, Map model,
			Configuration configuration) {
		Map<String, Object> inModel = new HashMap<String, Object>();
		// System.out.println("survey="+model.get("survey"));
		inModel.put("survey", model.get("survey"));
		inModel.put("surveyRecord", model.get("surveyRecord"));
		inModel.put("questionPage", model.get("questionPage"));
		inModel.put("pageId", model.get("pageId"));
		inModel.put("pages", model.get("pages"));
		inModel.put("sid", model.get("sid"));
		inModel.put("rid", model.get("rid"));
		inModel.put("surveyAreaEngine", surveyAreaEngine);
		inModel.put("surveyEngine", surveyEngine);
		return inModel;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setSurveyAreaEngine(SurveyAreaEngine surveyAreaEngine) {
		this.surveyAreaEngine = surveyAreaEngine;
	}

	public void setSurveyEngine(SurveyEngine surveyEngine) {
		this.surveyEngine = surveyEngine;
	}
	
}
