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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.user.manager.IRoleManager;
import org.openuap.cms.user.model.IRole;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 角色编辑控制器.
 * </p>
 *
 * 
 * <p>
 * $Id: RoleEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class RoleEditAction extends AdminFormAction {

	private String defaultScreensPath;

	//
	private String operationViewName;

	//
	private IRoleManager baseRoleManager;

	public RoleEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/user/";
		operationViewName = defaultScreensPath + "role_operation_result.html";
		this.setFormView(defaultScreensPath + "role_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(IRole.class);
		this.setCommandName("role");

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
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors, ControllerHelper helper, Map model) throws Exception {
		//
		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		try {
			IRole role = (IRole) command;
			String mode = request.getParameter("mode");
			String ref = request.getParameter("ref");
			
			model.put("ref", ref);
			if (mode.equals("add")) {
				model.put("op", "add");
				role.setCreationDate(new Long(System.currentTimeMillis()));
				role.setModificationDate(new Long(System.currentTimeMillis()));
				role.setPos(new Integer(0));
				role.setStatus(new Integer(0)); // normal
				baseRoleManager.addRole(role);
				model.put("rs", "1");
			} else {
				model.put("op", "edit");
				role.setModificationDate(new Long(System.currentTimeMillis()));
				baseRoleManager.saveRole(role);
				model.put("rs", "1");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if (ex instanceof UnauthorizedException) {
				throw (UnauthorizedException) ex;
			}
			model.put("rs", "0");
			model.put("ex", ex);
		}
		return mv;
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name_empty", "the name shouldn't be empty.");
	}

	protected Object formBackingObject(HttpServletRequest request) {
		String mode = request.getParameter("mode");
		String roleId = request.getParameter("roleId");
		if (mode != null && mode.equals("edit")) {
			Long rid = new Long(roleId);
			IRole role = baseRoleManager.getRoleById(rid);
			return role;
		} else {
			IRole role =baseRoleManager.createRole();
			return role;
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map ref = new HashMap();
		String mode = request.getParameter("mode");
		if (mode == null) {
			mode = "add";
		}
		ref.put("mode", mode);
		return ref;
	}

	public void setBaseRoleManager(IRoleManager baseRoleManager) {
		this.baseRoleManager = baseRoleManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setOperationViewName(String operationViewName) {
		this.operationViewName = operationViewName;
	}

}
