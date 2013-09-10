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
package org.openuap.cms.user.action;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.resource.ConstantLoader;
import org.openuap.base.util.resource.ConstantLoader.Constant;
import org.openuap.cms.core.action.UserAwareAction;
import org.openuap.cms.user.security.SecurityUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 用户模块基础控制器.
 * </p>
 * 
 * <p>
 * $Id: UserBaseAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class UserBaseAction extends UserAwareAction {

	
	public UserBaseAction() {
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		model.put("securityUtil", new SecurityUtil());
		//
		// 如果通过请求参数指定了语言
		String lang = request.getParameter("lang");
		Locale locale = null;
		if (lang != null) {
			locale = new Locale(lang);
		}
		// 设置国际化语言文件
		setBaseLang(request, model, "UserAdminLanguage.xml", locale);
		return super.beforePerform(request, response, helper, model);
	}

	
	

	/**
	 * 设置国际化语言常量
	 * 
	 * @param request
	 * 
	 * @param model
	 * 
	 * @param fileName
	 * 
	 * @param locale
	 * @deprecated Use
	 *             {@link #setBaseLang(HttpServletRequest,Map,String,Locale)}
	 *             instead
	 * 
	 */
	protected void setLang(HttpServletRequest request, Map model,
			String fileName, Locale locale) {
		setBaseLang(request, model, fileName, locale);
	}

	/**
	 * 设置国际化语言常量
	 * 
	 * @param request
	 * 
	 * @param model
	 * 
	 * @param fileName
	 * 
	 * @param locale
	 * 
	 */
	protected void setBaseLang(HttpServletRequest request, Map model,
			String fileName, Locale locale) {
		ConstantLoader.Constant lang;
		if (locale != null) {
			// 如果指定locale则使用指定的语言
			lang = ConstantLoader.load(fileName, locale);
		} else {
			// 装载缺省语言资源文件
			lang = ConstantLoader.load(fileName);
		}

		model.put("lang", lang);
	}

	/**
	 * 获得模型中的语言定义
	 * 
	 * @param model
	 * 
	 * @return
	 */
	protected ConstantLoader.Constant getLang(Map model) {
		return (Constant) model.get("lang");
	}
}
