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
package org.openuap.cms.publish.config.action;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.AdminAction;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 内容发布配置
 * </p>
 * 
 * <p>
 * $Id: PublishConfigAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishConfigAction extends AdminAction {

	private String defaultViewName;

	private String defaultScreensPath;

	public PublishConfigAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/publish/config/";
		defaultViewName = defaultScreensPath + "config.html";
	}
	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		CMSConfig config = CMSConfig.getInstance();
		model.put("config", config);
		return mv;
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doChange(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		
		try {
			
			//
			Iterator iter = helper.toMap().keySet().iterator();
			//

			while (iter.hasNext()) {
				String key = (String) iter.next();
				if (key.indexOf(".") >= 0) {
					this.config.setProperty(key, helper.getString(key, ""));
				}
			}
			this.config.save();
			CMSConfig.getInstance().refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		CMSConfig config = CMSConfig.getInstance();
		model.put("config", config);
		return mv;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}
}
