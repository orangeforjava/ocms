/*
 * Copyright 2002-2009 the original author or authors.
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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.AuthToken;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 管理员后台表单控制器
 * </p>
 * 
 * <p>
 * $Id: AdminFormAction.java 4021 2011-03-22 14:48:53Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class AdminFormAction extends CMSBaseFormAction {

	public AdminFormAction() {
		this.setSuccessView("/plugin/cms/base/success.html");
		this.setErrorView("/plugin/cms/base/error.html");
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = super.beforePerform(request, response, helper, model);
		if (mv != null) {
			return mv;
		}
		try {
			// 判断是否管理员已登录
			AuthToken authToken = SecurityUtil.getAuthService()
					.getAdminAuthToken(request, response, null, -2);
		} catch (UnauthorizedException e) {
			// 未登录后台，转向登录界面
			String done = helper.getString("done", helper.getEncodedFullURI());
			// System.out.println("done="+done);
			helper.sendRedirect(helper.getBaseURL() + "admin/login.jhtml?done="
					+ encodeURL(done));

		}
		model.put("noLayout", true);
		return null;
	}
}
