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
package org.openuap.cms.core.action;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.resource.ConstantLoader;
import org.openuap.base.util.resource.ConstantLoader.Constant;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.openuap.runtime.setup.BootstrapUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * *
 * <p>
 * CMS基础控制器.
 * </p>
 * <p>
 * $Id: CMSBaseAction.java 4021 2011-03-22 14:48:53Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class CMSBaseAction extends UserAwareAction {
	protected String jsonOpViewName = "/plugin/cms/base/screens/op_result_json.htm";

	public CMSBaseAction() {
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
		// 设置国际化语言文件
		setLang(request, model, "CMSAdminLanguage.xml", locale);
		// 设置CMS的缺省布局模板
		// model.put("layout", "/plugin/cms/platform/base/layouts/none.html");
		// 放置内容模型
		model.put("securityUtil", new SecurityUtil());
		return super.beforePerform(request, response, helper, model);
	}

	/**
	 * 工具方法，显示日期
	 * 
	 * @param milliseconds
	 * 
	 * @param pattern
	 * 
	 * @return String
	 */
	public String getDateDisplay(long milliseconds, String pattern) {
		Date date;
		if (milliseconds == 0) {
			date = new Date(System.currentTimeMillis());
		} else {
			date = new Date(milliseconds);
		}
		String p = pattern;
		if (p == null || p.equals("")) {
			p = "yyyy年MM月dd日 hh时mm分ss秒";
		}
		SimpleDateFormat sf = new SimpleDateFormat(p);
		return sf.format(date);
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
	 * 
	 * @return
	 */
	protected ConstantLoader.Constant getLang(Map model) {
		return (Constant) model.get("lang");
	}

	// /TODO 下面代码将被移到基础库之中
	/**
	 * 得到编码的全路径URL
	 * 
	 * @param helper
	 * @return
	 */
	public String getFullEncodedURL(ControllerHelper helper) {
		String baseURL = getScriptName(helper);
		StringBuffer url = new StringBuffer(baseURL);
		String queryString = helper.getRequest().getQueryString();
		if (queryString != null) {
			url.append("?");
			url.append(encodeURL(queryString));
		}
		return url.toString();
	}

	/**
	 * 得到全路径的URL
	 * 
	 * @param helper
	 * @return
	 */
	public String getFullURL(ControllerHelper helper) {
		String baseURL = getScriptName(helper);
		StringBuffer url = new StringBuffer(baseURL);
		String queryString = helper.getRequest().getQueryString();
		if (queryString != null) {
			url.append("?");
			url.append(queryString);
		}
		return url.toString();
	}

	/**
	 * 得到当前脚本名称
	 * 
	 * @param helper
	 * @return
	 */
	public String getScriptName(ControllerHelper helper) {
		//
		BaseApplicationConfiguration config = BootstrapUtils
				.getBootstrapManager("base").getApplicationConfig();
		String homeUrl = "";
		if (config.getBoolean("sys.url.use-custom-url", false)) {
			homeUrl = config.getString("sys.url.homepage", "");
			//
		}
		if (!homeUrl.equals("") && homeUrl.endsWith("/")) {
			homeUrl = homeUrl.substring(0, homeUrl.length() - 1);
		}
		String path = helper.getRequest().getPathInfo();// 例如:?
		String context = helper.getRequest().getContextPath();// 例如:/forum,如果是ROOT则返回""
		String uri = helper.getRequest().getRequestURI();// 例如:/user/info.htm

		//
		if (path != null) {
			if (!homeUrl.equals("")) {
				if (!context.equals("")) {
					return homeUrl
							+ uri.substring(context.length(), uri.length());
				} else {
					return homeUrl
							+ uri.substring(0, uri.length() - path.length());
				}
			} else {
				return uri.substring(0, uri.length() - path.length());
			}
		} else {
			if (!homeUrl.equals("")) {
				if (!context.equals("")) {
					return homeUrl
							+ uri.substring(context.length(), uri.length());
				} else {
					return homeUrl + uri;
				}
			} else {
				if (!context.equals("")) {
					return uri.substring(context.length() + 1, uri.length());
				} else {
					return uri.substring(1);
				}
			}

		}
	}

	/**
	 * 得到编码URL
	 * 
	 * @param url
	 * @return
	 */
	public String encodeURL(String url) {
		try {
			return java.net.URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}

	/**
	 * 得到解码URL
	 * 
	 * @param url
	 * @return
	 */
	public String decodeURL(String url) {
		try {
			return java.net.URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}

	/**
	 * 直接向浏览器返回纯内容
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sendContent(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model,
			String content) throws Exception {
		String contentViewName = "/content.htm";
		ModelAndView mv = new ModelAndView(contentViewName);
		model.put("content", content);
		return mv;
	}
}
