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
package org.openuap.cms.stat.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 获得内容的统计信息，包括计数与流量信息
 * </p>
 * 
 * <p>
 * $Id: StatAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class StatAction extends CMSBaseAction {
	/** 动态内容管理. */
	private DynamicContentManager dynamicContentManager;
	

	private String displayStatViewName;

	private String defaultScreensPath;

	public StatAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/stat/";
		displayStatViewName = defaultScreensPath + "stat_json.html";
	}
	@Override
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) throws Exception{
		// TODO Auto-generated method stub
		ModelAndView mv = new ModelAndView(displayStatViewName, model);
		model.put("responseType", "text/javaScript");
		String indexId = request.getParameter("id");
		String callback=request.getParameter("callback");
		model.put("callback", callback);
		if (indexId != null) {
			Long iid = new Long(indexId);
			ContentIndex ci = dynamicContentManager.getContentIndexById(iid);
			
			if(ci!=null){
				model.put("ci",ci);
			}
			
		}
		return mv;
	}

	public void setDynamicContentManager(DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

}
