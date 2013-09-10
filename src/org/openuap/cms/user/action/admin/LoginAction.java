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
package org.openuap.cms.user.action.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.DateUtil;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.user.config.UserConfig;
import org.openuap.cms.user.security.IUserSession;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.workbench.ui.Workbench;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.plugin.Plugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.StringUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 后台登录控制器
 * </p>
 * 
 * <p>
 * $Id: LoginAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class LoginAction extends CMSBaseAction {

	private String loginViewName = "/plugin/cms/base/screens/login_new.html";

	/**
	 * 检查是否进行了前端登录，若没有前端登录，则跳转到前端登录部分
	 */
	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = super.beforePerform(request, response, helper, model);
		if (mv != null) {
			return mv;
		}
		if (SecurityUtil.isAnonymous()) {
			// 如果是匿名则转向前端登录
			String done = helper.getString("done", helper.getBaseURL());
			// System.out.println("done cms="+done);
			String loginUrl = SecurityUtil.getAuthService().getLoginUrl();
			helper.sendRedirect(helper.getBaseURL() + loginUrl + "?done="
					+ done);
		}
		return null;
	}

	/**
	 * 显示后台登录界面
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(loginViewName);
		// 登录成功后返回地址
		Workbench workbench = getWorkbench();
		model.put("workbench", workbench);
		String done = helper.getString("done", "");
		if (done != null) {
			done = helper.decodeURL(done);
		}
		//
		IUserSession userSession = this.getUserSession();
		model.put("userSession", userSession);
		model.put("done",done);
		//
		String code = StringUtil.randomInt(4) + "";
		String key = StringUtil.encrypt(code);
		helper.getCookies().addTemporaryData("code", code).addTemporaryData(
				"time", DateUtil.currentTimeSeconds() + "")
				.setTemporaryCookie();
		model.put("key", key);
		return mv;
	}

	/**
	 * 执行登录
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doLogin(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String username = helper.getString("username", "").trim();
		String encodedUserName = StringUtil.encodeURL(username, "UTF-8");
		String password = helper.getString("password");
		int autoLoginAge = helper.getInt("age", -2);
		UserConfig userConfig = UserConfig.getInstance();
		String passportDomain = userConfig.getPassportDomain();
		// 后台强制校验码
		try {
			String code = helper.getString("code", "");
			String validCode = helper.getCookies().getTemporaryData("code");
			int time = Integer.parseInt(helper.getCookies().getTemporaryData(
					"time"));
			if (!code.equals(validCode)) {
				return errorPage(request, response, helper,
						"invalid_secure_code", model);
			} else if (time < DateUtil.currentTimeSeconds()
					- userConfig.getSecureLoginDuration()) {
				return errorPage(request, response, helper,
						"secure_code_expired", model);
			}
		} catch (Exception ex) {
			return errorPage(request, response, helper, "secure_code_expired",
					model);
		}
		//
		if (username.length() < 1) {
			return errorPage(request, response, helper, "username_empty", model);
		}
		try {
			SecurityUtil.getAuthService().createAdminAuthToken(username,
					password);
			if (userConfig.isEnablePassport()) {
				SecurityUtil.getAuthService().loginAdmin(request, response,
						username, encodedUserName, password, passportDomain,
						autoLoginAge);
			} else {
				SecurityUtil.getAuthService()
						.loginAdmin(request, response, username,
								encodedUserName, password, null, autoLoginAge);
			}
			helper.getCookies().removeTemporaryData("code")
					.removeTemporaryData("time").setTemporaryCookie();
			String done = helper.getString("done", helper.getBaseURL());
			helper.sendRedirect(done);
			return null;
		} catch (UnauthorizedException e) {
			return errorPage(request, response, helper, e.getMessage(), model);
		}
	}

	/**
	 * 重写错误页面
	 */
	public ModelAndView errorPage(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, String code,
			Map model) {
		try {
			// String errorMsg=this.getLang(model).get(code,code);
			model.put("errorMsg", code);
			return perform(request, response, helper, model);
		} catch (Exception e) {
			e.printStackTrace();
			return super.errorPage(request, response, helper, code, model);
		}
	}

	/**
	 * 检查安全验证码
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ModelAndView doCheckSecurityCode(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, ServletException {
		boolean success = false;
		String code = helper.getString("code", "");
		String validCode = helper.getCookies().getTemporaryData("code");
		UserConfig userConfig = UserConfig.getInstance();
		int time = Integer.parseInt(helper.getCookies()
				.getTemporaryData("time"));
		if (!code.equals(validCode)) {
			success = false;
		} else if (time < DateUtil.currentTimeSeconds()
				- userConfig.getSecureLoginDuration()) {
			success = false;
		} else {
			success = true;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}
	/**
	 * 获得Workbench对象
	 * @return
	 */
	private Workbench getWorkbench() {
		Plugin plugin = WebPluginManagerUtils.getPlugin(this
				.getServletContext(), CmsPlugin.PLUGIN_ID);
		if (plugin != null && plugin instanceof CmsPlugin) {
			CmsPlugin wPlugin = (CmsPlugin) plugin;
			Workbench workbench = wPlugin.getWorkbench();
			return workbench;
		}
		return null;
	}

	public void setLoginViewName(String loginViewName) {
		this.loginViewName = loginViewName;
	}
}
