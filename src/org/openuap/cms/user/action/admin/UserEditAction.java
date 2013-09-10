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

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.AbstractUser;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.user.security.permissions.UserPermissionConstant;
import org.openuap.cms.user.ui.UserStatus;
import org.openuap.cms.user.ui.UserType;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 用户编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: UserEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class UserEditAction extends AdminFormAction {

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

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/user/";
		operationViewName = defaultScreensPath + "user_operation_result.html";
		this.setFormView(defaultScreensPath + "user_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(IUser.class);
		this.setCommandName("user");
	}

	/**
	 * 提交保存
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.AddUser)) {
			throw new UnauthorizedException();
		}
		//
		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		try {
			IUser user = (IUser) command;
			String mode = request.getParameter("mode");
			String pwd = request.getParameter("pwd1");
			if (mode.equals("add")) {
				model.put("op", "add");
				user.setPos(new Integer(0));
				user.setPassword(pwd);
				user.setCreationDate(new Long(System.currentTimeMillis()));
				user.setLoginTimes(new Long(0));
				user.setLastLoginDate(new Long(-1));
				baseUserManager.addUser(user);
				model.put("rs", "success");
			} else {
				model.put("op", "edit");
				if (pwd != null && !pwd.trim().equals("")) {
					user.setPassword(pwd);
					baseUserManager.saveUserWithChangePwd(user);
				} else {
					baseUserManager.saveUser(user);
				}
				model.put("rs", "success");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("rs", "failed");
			model.put("ex", ex);
		}
		return mv;
	}

	/**
	 * 数据校验
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.AddUser)) {
			errors.reject("no_permission", "no_permission");
		}

		String mode = request.getParameter("mode");
		String pwd1 = request.getParameter("pwd1");
		String pwd2 = request.getParameter("pwd2");
		AbstractUser user = (AbstractUser) command;
		if (mode.equals("add")) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
					"name_empty", "the name shouldn't be empty.");
			//
			if (!StringUtils.hasText(pwd1)) {
				errors.reject("password_empty",
						"the password shouldn't be empty");
			}
			if (pwd1 != null && !pwd1.equals(pwd2)) {
				errors.reject("password_not_equal",
						"the password is not equal.");
			}
			// http://jira.openuap.org/browse/CMS-15
			String name = request.getParameter("name");
			int count = baseUserManager.getUserByNameCount(name);
			if (count > 0) {
				errors.reject("name_is_exist", "用户名已经存在，请选择别的用户名.");
			}
		} else if (mode.equals("edit")) {
			if (pwd1 != null && !pwd1.equals(pwd2)) {
				errors.reject("password_not_equal",
						"the password is not equal.");
			}
		}
	}

	protected Object formBackingObject(HttpServletRequest request) {
		String mode = request.getParameter("mode");
		String userId = request.getParameter("userId");
		if (mode != null && mode.equals("edit")) {
			Long uid = new Long(userId);
			IUser user = baseUserManager.getUserById(uid);
			return user;
		} else {
			IUser user = baseUserManager.createUser();
			return user;
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {

		Map ref = new HashMap();
		String mode = request.getParameter("mode");
		if (mode == null) {
			mode = "add";
		}
		ref.put("mode", mode);
		ref.put("userStatues", UserStatus.ALL_USER_STATUS);
		ref.put("userTypes", UserType.SYS_USER_TYPES);
		return ref;
	}

	/**
	 * 用户名检查
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
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
