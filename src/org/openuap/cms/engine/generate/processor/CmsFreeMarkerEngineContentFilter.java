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
package org.openuap.cms.engine.generate.processor;

import java.util.HashMap;
import java.util.Map;

import org.openuap.base.util.resource.ConstantLoader;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.tpl.engine.plugin.FreeMarkerEngineContentFilter;

import freemarker.template.Configuration;

/**
 * <p>
 * CMS 内容处理过滤器
 * </p>
 * <p>
 * $Id: CmsFreeMarkerEngineContentFilter.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CmsFreeMarkerEngineContentFilter implements
		FreeMarkerEngineContentFilter {

	private int priority = 5;
	private PsnManager psnManager;

	private CmsMacroEngine cmsMacroEngine;

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
		cmsMacros.put("cms", "/plugin/cms/base/macros/cms-macros.html");
		cmsMacros.put("util", "/plugin/cms/base/macros/util-macros.html");
		cmsMacros.put("pager", "/plugin/cms/base/macros/pager-macros.html");
		return cmsMacros;
	}

	public Map getSharedVariables(String tplContent, Map model,
			Configuration configuration) {
		Map<String, Object> inModel = new HashMap<String, Object>();
		// 装载语言包
		ConstantLoader.Constant lang = ConstantLoader
				.load("CMSInternalLanguage.xml");
		if (lang != null) {
			inModel.put("lang", lang);
		}
		// 放置结点信息
		Object oNode = model.get("node");
		if (oNode != null && oNode instanceof Node) {
			Node node = (Node) oNode;
			inModel.put("currentNodeId", node.getNodeId().toString());
			inModel.put("currentNode", node);
			inModel.put("psn", psnManager.getPsn(node.getContentUrl()));
		}
		// 放置当前内容索引信息
		Object oContentIndex = model.get("ci");
		if (oContentIndex != null && oContentIndex instanceof ContentIndex) {
			ContentIndex ci = (ContentIndex) oContentIndex;
			inModel.put("currentIndexId", ci == null ? new Long(0) : ci
					.getIndexId());
			inModel.put("currentIndex", ci);
		}
		Object url = model.get("url");
		if (url != null) {
			inModel.put("currentUrl", url);
		}
		Object page = model.get("page");
		if (page != null) {
			// 为了兼容性，暂时使用字符串类型！
			inModel.put("currentPage", page.toString());
		}
		inModel.put("cmsMacroEngine", cmsMacroEngine);
		//
		String baseUrl = CMSConfig.getInstance().getStringProperty("cms.url",
				CMSConfig.getInstance().getBaseUrl());
		inModel.put("baseUrl", baseUrl);
		return inModel;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setCmsMacroEngine(CmsMacroEngine cmsMacroEngine) {
		this.cmsMacroEngine = cmsMacroEngine;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
