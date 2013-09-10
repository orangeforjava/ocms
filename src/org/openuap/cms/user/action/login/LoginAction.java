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
package org.openuap.cms.user.action.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.DateUtil;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.user.action.UserBaseAction;
import org.openuap.cms.user.config.UserConfig;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.workbench.ui.Workbench;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.plugin.Plugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.StringUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 前台登录控制器.
 * </p>
 * <p>
 * $Id: LoginAction.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class LoginAction extends UserBaseAction {

	private String loginViewName = "/plugin/cms/base/screens/user/login_new.html";

	private IUserManager userManager;

	/**
	 * 
	 */
	public LoginAction() {
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Workbench workbench = getWorkbench();
		model.put("workbench", workbench);
		// 登录成功后返回地址
		String done = helper.getString("done", "");
		if (!done.equals("")) {
			done = java.net.URLDecoder.decode(done, "UTF-8");
		}
		// 判断是否已经登录
		if (isLogin()) {
			helper.sendRedirect(done == null ? helper.getBaseURL() : (helper
					.getBaseURL()
					+ "" + done));
		}
		UserConfig userConfig = UserConfig.getInstance();
		model.put("userConfig", userConfig);
		// 是否是管理员登录
		String force = helper.getString("force", "0");
		if (userConfig.isEnablePassport() && !force.equals("1")) {
			// 通行证登录
			String domain = userConfig.getPassportDomain();
			this.getAuthService().logout(request, response, domain);
			//
			String loginUrl = userConfig.getLoginUrl();
			String fullURI = request.getRequestURI();
			String baseUrl = helper.getBaseURL();
			String fullURL = baseUrl.substring(0, baseUrl.length()) + fullURI;
			//
			String redirectUrl = null;
			//
			if (!loginUrl.trim().equals("")
					&& !loginUrl.equalsIgnoreCase(fullURL)) {
				if (StringUtils.hasText(done)) {
					if (done.toLowerCase().startsWith("http://")
							|| done.toLowerCase().startsWith("https://")) {
						redirectUrl = loginUrl
								+ java.net.URLEncoder.encode(done, "UTF-8");
					} else {
						redirectUrl = loginUrl
								+ ""
								+ java.net.URLEncoder.encode(baseUrl + done,
										"UTF-8");
					}
				} else {
					redirectUrl = loginUrl
							+ java.net.URLEncoder.encode(helper.getBaseURL(),
									"UTF-8");
				}
				//
				helper.sendRedirect(redirectUrl);
			}
		}
		if (userConfig.isEnableSecureCode()) {
			String code = StringUtil.randomInt(4) + "";
			String key = StringUtil.encrypt(code);
			helper.getCookies().addTemporaryData("code", code)
					.addTemporaryData("time",
							DateUtil.currentTimeSeconds() + "")
					.setTemporaryCookie();
			model.put("key", key);
		}
		//
		if (!done.equals("")) {
			done = java.net.URLEncoder.encode(done, "UTF-8");
		}
		model.put("done", done);
		return new ModelAndView(loginViewName, model);
	}

	/**
	 * 执行登录
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doLogin(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String username = helper.getString("username", "").trim();
		String encodedUserName = StringUtil.encodeURL(username, "UTF-8");
		String password = helper.getString("password");
		int autoLoginAge = helper.getInt("age", -2);
		UserConfig userConfig = UserConfig.getInstance();
		String passportDomain = userConfig.getPassportDomain();
		if (userConfig.isEnableSecureCode()) {
			//
			try {
				String code = helper.getString("code", "");
				String validCode = helper.getCookies().getTemporaryData("code");
				int time = Integer.parseInt(helper.getCookies()
						.getTemporaryData("time"));
				if (!code.equals(validCode)) {
					return errorPage(request, response, helper,
							"invalid_secure_code", model);
				} else if (time < DateUtil.currentTimeSeconds()
						- userConfig.getSecureLoginDuration()) {
					return errorPage(request, response, helper,
							"secure_code_expired", model);
				}
			} catch (Exception ex) {
				return errorPage(request, response, helper,
						"secure_code_expired", model);
			}
		}
		//
		if (username.length() < 1) {
			return errorPage(request, response, helper, "username_empty", model);
		}
		try {
			getAuthService().createAuthToken(username, password);
			if (userConfig.isEnablePassport()) {
				// 若启用通行证则按照通行证域保存
				this.getAuthService()
						.login(request, response, username, encodedUserName,
								password, passportDomain, autoLoginAge);
			} else {
				this.getAuthService().login(request, response, username,
						encodedUserName, password, null, autoLoginAge);
			}
			helper.getCookies().removeTemporaryData("code")
					.removeTemporaryData("time").setTemporaryCookie();
			String done = helper.getString("done");
			if (done != null) {
				done = java.net.URLDecoder.decode(done, "utf-8");
				while (done.charAt(0) == '/') {
					done = done.substring(1);
				}
				done = helper.getBaseURL() + done;
			} else {
				done = helper.getBaseURL();
			}
			done = java.net.URLDecoder.decode(done, "utf-8");
			// System.out.println("dologin done="+done);
			helper.sendRedirect(done);
			return null;
		} catch (UnauthorizedException e) {
			return errorPage(request, response, helper, e.getMessage(), model);
		}
	}

	/**
	 * 校验安全码
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
		int time = Integer.parseInt(helper.getCookies()
				.getTemporaryData("time"));
		if (!code.equals(validCode)) {
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
	 * 重写错误页面
	 */
	public ModelAndView errorPage(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, String code,
			Map model) {
		try {
			// String errorMsg=this.getLang(model).get(code,code);
			// System.out.println("error code="+code);
			model.put("errorMsg", code);
			return perform(request, response, helper, model);
		} catch (Exception e) {
			e.printStackTrace();
			return super.errorPage(request, response, helper, code, model);
		}
	}

	public void setLoginViewName(String loginViewName) {
		this.loginViewName = loginViewName;
	}

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

	public String getLoginViewName() {
		return loginViewName;
	}

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
}
