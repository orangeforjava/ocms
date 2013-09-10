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
package org.openuap.cms.user.action.config;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.user.config.UserConfig;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 通行证配置控制器
 * </p>
 * 
 * <p>
 * $Id: ConfigAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * @author Joseph
 * 
 */
public class ConfigAction extends AdminAction {

	private String defaultView = null;

	public ConfigAction() {
		defaultView = "plugin/cms/base/screens/user/config/config.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) throws Exception {
		ModelAndView mv = new ModelAndView(defaultView);
		this.config.load();
		model.put("config", config);
		return mv;
	}
	/**
	 * 保存通行证配置变更
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doSave(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		Boolean passportEnable = helper.getBoolean("passport.enable", false);
		Iterator iter = helper.toMap().keySet().iterator();
		//
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key.indexOf(".") >= 0) {
				if (!helper.getString(key, "").trim().equals("")) {
					this.config.setProperty(key, helper.getString(key, ""));
				} else {
					this.config.clearProperty(key);
				}
			}
		}
		//
		Iterator keys = this.config.getKeys();
		if (keys != null) {
			while (keys.hasNext()) {
				Object key = keys.next();
				//
				if (helper.getString((String) key) == null
						&& this.config.getProperty((String) key)!=null&&this.config.getProperty((String) key).toString()
								.equalsIgnoreCase("true")) {
					//只处理passport部分
					if (((String) key).indexOf("passport") >= 0) {
						this.config.setProperty((String) key, Boolean.FALSE);
					}
				}
			}
		}
		try {
			//
			this.config.save();
			this.config.load();
			UserConfig.getInstance().reload();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return successPage(request, response, helper, "change_config", model);
	}
	
}
