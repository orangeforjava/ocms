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
package org.openuap.cms.core.action;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.resource.ConstantLoader;
import org.openuap.base.util.resource.ConstantLoader.Constant;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.security.SecurityUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * CMS基础表单控制器
 * </p>
 * 
 * <p>
 * $Id: CMSBaseFormAction.java 4021 2011-03-22 14:48:53Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class CMSBaseFormAction extends UserAwareFormAction {

	private IUserManager userManager;

	public CMSBaseFormAction() {
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		// 如果通过请求参数指定了语言
		String lang = request.getParameter("lang");
		Locale locale = null;
		if (lang != null) {
			locale = new Locale(lang);
		}
		// 设置语言
		setLang(request, model, "CMSAdminLanguage.xml", locale);
		//
		model.put("securityUtil", new SecurityUtil());
		//
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
	 * 
	 */
	protected void setLang(HttpServletRequest request, Map model,
			String fileName, Locale locale) {
		ConstantLoader.Constant lang;
		if (locale != null) {
			// 如果指定locale则使用指定的语言
			lang = ConstantLoader.load(fileName, locale);
		} else {
			if (CMSConfig.getInstance().enableChangeLanguage()) {
				// 是否允许根据用户浏览器语言设置变更
				Locale rlocale = request.getLocale();
				//
				lang = ConstantLoader.load(fileName, rlocale);
			} else {
				// 获得缺省语言
				java.util.Locale defLocale = CMSConfig.getInstance()
						.getDefaultLocale();
				if (defLocale != null) {
					// 如果存在缺省语言
					lang = ConstantLoader.load(fileName, defLocale);
				} else {
					// 装载缺省语言资源文件
					lang = ConstantLoader.load(fileName);
				}
			}
		}
		model.put("lang", lang);
	}

	/**
	 * 获得模型中的语言定义
	 * 
	 * @param model
	 *            Map
	 * @return Constant
	 */
	protected ConstantLoader.Constant getLang(Map model) {
		return (Constant) model.get("lang");
	}
	/**
	 * 得到编码URL
	 * @param url
	 * @return
	 */
	public String encodeURL(String url) {
		try {
			return java.net.URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}
	/**
	 * 得到解码URL
	 * @param url
	 * @return
	 */
	public String decodeURL(String url) {
		try {
			return java.net.URLDecoder.decode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}
	public ModelAndView sendContent(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model,String content)
			throws Exception {
		String contentViewName="/content.htm";
		ModelAndView mv=new ModelAndView(contentViewName);
		model.put("content", content);
		return mv;
	}
}
