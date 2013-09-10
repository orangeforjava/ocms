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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.user.action.UserBaseAction;
import org.openuap.cms.user.config.UserConfig;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 前台注销控制器
 * </p>
 * 
 * <p>
 * $Id: LogoutAction.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class LogoutAction extends UserBaseAction {

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String done = helper.getString("done", "");
		//
		done = java.net.URLDecoder.decode(done, "UTF-8");
		while (done.startsWith("/")) {
			done = done.substring(1);
		}
		//
		UserConfig userConfig = UserConfig.getInstance();
		String domain = userConfig.getPassportDomain();
		String redirectUrl = null;
		if (done.toLowerCase().startsWith("http://")
				|| done.toLowerCase().startsWith("https://")) {
			redirectUrl = java.net.URLEncoder.encode(done, "UTF-8");
		} else {
			redirectUrl = java.net.URLEncoder.encode(
					helper.getBaseURL() + done, "UTF-8");
		}
		// System.out.println("redirectUrl="+redirectUrl);
		try {
			// 认证服务退出
			getAuthService().logout(request, response, domain);
			if (userConfig.isEnablePassport()) {
				String logoutUrl = userConfig.getLogoutUrl();
				// 同时要退出通行证
				if (StringUtils.hasText(logoutUrl)) {
					//
					helper.sendRedirect(logoutUrl + redirectUrl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		helper.sendRedirect(helper.getBaseURL()+done);
		return null;
	}
}
