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
package org.openuap.cms.schedule.action;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.resource.ConstantLoader;
import org.openuap.base.util.resource.ConstantLoader.Constant;
import org.openuap.base.web.mvc.BaseController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 计划任务基础控制器
 * 
 * $Id: ScheduleBaseAction.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @author Joseph
 * 
 */
public abstract class ScheduleBaseAction extends BaseController {
	
	protected String host;

	public ModelAndView beforePerform(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws Exception {
		// 如果通过请求参数指定了语言
		String lang = request.getParameter("lang");
		Locale locale = null;
		if (lang != null) {
			locale = new Locale(lang);
		}
		// 设置国际化语言文件
		setLang(request, model, "ScheduleLanguage.xml", locale);
		//
		model.put("host", host);
		return null;
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
	protected void setLang(HttpServletRequest request, Map<Object, Object> model, String fileName, Locale locale) {
		ConstantLoader.Constant lang;
		if (locale != null) {
			// 如果指定locale则使用指定的语言
			lang = ConstantLoader.load(fileName, locale);
		} else {
			// 允许根据用户浏览器语言设置变更
			Locale rlocale = request.getLocale();
			//
			lang = ConstantLoader.load(fileName, rlocale);

		}
		model.put("scheduleLang", lang);
	}

	/**
	 * 获得模型中的语言定义
	 * 
	 * @param model
	 * 
	 * @return
	 */
	protected ConstantLoader.Constant getLang(Map model) {
		return (Constant) model.get("scheduleLang");
	}
}
