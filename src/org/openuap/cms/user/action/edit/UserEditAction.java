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

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.user.action.UserBaseFormAction;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.AbstractUser;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.ui.UserStatus;
import org.openuap.cms.user.ui.UserType;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 用户编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: UserEditAction.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserEditAction extends UserBaseFormAction {

	private String defaultScreensPath;

	//
	private String operationViewName;

	//
	private IUserManager baseUserManager;

	/**
	 * 
	 */
	public UserEditAction() {
		initDefaultProperty();
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!this.getUserSession().isLogin()) {
			// 必须登录
			String loginUrl = this.getAuthService().getLoginUrl();
			helper.sendRedirect(loginUrl + "?done=" + helper.getBaseURL()
					+ "user/edit.jhtml");
		}
		return super.beforePerform(request, response, helper, model);
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/user/edit/";
		operationViewName = defaultScreensPath + "user_result.html";
		this.setFormView(defaultScreensPath + "user_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(IUser.class);
		this.setCommandName("user");
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		
		//
		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		model.put("op", "edit");
		//
		try {
			IUser user = (IUser) command;
			if(!this.getUser().getUserId().equals(user.getUserId())){
				model.put("rs", "failed");
				model.put("msgs", "您在此只能更改自己的密码.");
				return mv;
			}
			String pwd = request.getParameter("password");

			if (pwd != null && !pwd.trim().equals("")) {
				baseUserManager.saveUserWithChangePwd(user);
			} else {
				baseUserManager.saveUser(user);
			}
			model.put("rs", "success");

		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("rs", "failed");
			model.put("msgs", ex);
		}
		return mv;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		//
		String pwd2 = request.getParameter("pwd2");
		AbstractUser user = (AbstractUser) command;

		if (pwd2!=null&&user.getPassword() != null && !user.getPassword().equals(pwd2)) {
			errors.reject("password_not_equal", "the password is not equal.");
		}

	}

	protected Object formBackingObject(HttpServletRequest request) {

		IUser user = this.getUser();
		return user;

	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {

		Map ref = new HashMap();
		//
		ref.put("userStatues", UserStatus.ALL_USER_STATUS);
		ref.put("userTypes", UserType.SYS_USER_TYPES);
		return ref;
	}

	/**
	 * check the user name is exist
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	public ModelAndView doCheckUserName(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String name = helper.getString("name");
		int count = baseUserManager.getUserByNameCount(name);
		//
		PrintWriter writer = response.getWriter();
		if (count == 0) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setBaseUserManager(IUserManager baseUserManager) {
		this.baseUserManager = baseUserManager;
	}
}
