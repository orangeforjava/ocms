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
package org.openuap.cms.user.action.edit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.user.action.UserBaseAction;
import org.openuap.cms.user.model.IUser;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 修改密码控制器.
 * </p>
 * 
 * <p>
 * $Id: UserChangePwdAction.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserChangePwdAction extends UserBaseAction {

	//
	private String defaultScreensPath;
	private String defaultViewName;
	private String resultViewName;

	//

	public UserChangePwdAction() {
		initDefaultProperty();

	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/user/edit/";
		defaultViewName = defaultScreensPath + "user_changepwd.htm";
		resultViewName = defaultScreensPath + "user_result.html";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = super.beforePerform(request, response, helper, model);
		if (mv != null) {
			return mv;
		}
		// 修改密码必须登录
		if (!this.isLogin()) {
			//
			return this.loginForm(request, response, helper, model);
		}
		//
		return null;
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		IUser user=this.getUser();
		model.put("user",user);
		return mv;
	}

	public ModelAndView doChange(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(resultViewName, model);
		model.put("op", "changePwd");
		try {
			IUser user = this.getUser();
			String passwordOld = request.getParameter("passwordOld");
			if (passwordOld != null) {
				String pold = getDigestedPassword(passwordOld);
				if (!pold.equals(user.getPassword())) {
					model.put("rs", "failed");
					model.put("msgs", "原密码不正确！");
					return mv;
				}
			}
			String password = request.getParameter("password");
			password = getDigestedPassword(password);
			//
			user.setPassword(password);
			this.getUserManager().saveUser(user);
			// TODO 同步信息，向通行证发送密码同步消息
			//
			model.put("rs", "success");
		} catch (Exception ex) {
			model.put("rs", "failed");
			model.put("msgs", ex);
		}
		return mv;
	}

	public void setResultViewName(String resultViewName) {
		this.resultViewName = resultViewName;
	}

	public String getDigestedPassword(String password) {
		return StringUtil.digest(password, "MD5");
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}
}
