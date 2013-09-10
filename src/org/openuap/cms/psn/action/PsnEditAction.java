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
package org.openuap.cms.psn.action;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.psn.cache.PsnCache;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.psn.security.PsnPermissionConstant;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 发布点编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: PsnEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PsnEditAction extends AdminFormAction {
	/** 缺省模板路径. */
	private String defaultScreensPath;

	/** PSN管理. */
	private PsnManager psnManager;

	/**
	 * 缺省构造函数
	 * 
	 */
	public PsnEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/psn/";
		this.setFormView(defaultScreensPath + "psn_add.html");
		this.setSuccessView(defaultScreensPath + "psn_operation_result.html");
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(Psn.class);
		this.setCommandName("psn");
	}

	/**
	 * 重写显示编辑界面方法，添加校验权限内容
	 */
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		String op = request.getParameter("op");
		if (op != null && op.equalsIgnoreCase("Edit")) {
			// 检查编辑权限
			if (!SecurityUtil.hasPermission(PsnPermissionConstant.OBJECT_TYPE
					.toString(), "-1", PsnPermissionConstant.EditPsn)) {
				throw new UnauthorizedException();
			}

		} else {
			// 检查添加权限
			if (!SecurityUtil.hasPermission(PsnPermissionConstant.OBJECT_TYPE
					.toString(), "-1", PsnPermissionConstant.AddPsn)) {
				throw new UnauthorizedException();
			}
		}
		return showForm(request, errors, getFormView(), null, helper, model);
	}

	/**
	 * 保存PSN
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
	 * @return ModelAndView
	 * @throws Exception
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {

		Psn psn = (Psn) command;
		String op = request.getParameter("op");
		if (op != null && op.equalsIgnoreCase("edit")) {
			model.put("op", "edit");
			// 检查编辑权限
			if (!SecurityUtil.hasPermission(PsnPermissionConstant.OBJECT_TYPE
					.toString(), "-1", PsnPermissionConstant.EditPsn)) {
				throw new UnauthorizedException();
			}

		} else {
			model.put("op", "add");
			// 检查添加权限
			if (!SecurityUtil.hasPermission(PsnPermissionConstant.OBJECT_TYPE
					.toString(), "-1", PsnPermissionConstant.AddPsn)) {
				throw new UnauthorizedException();
			}

		}
		// process the psn value
		String psn_type = psn.getPsnType();
		if (psn_type.equals("local")) {
			String psn_psn = psn.getLocalPath();
			psn.setPsn("relative::" + psn_psn);

		} else if (psn_type.equals("ftp")) {
			psn.setPsn(psn.getPsnFtp().toString());
		}
		try {
			if (op != null && op.equalsIgnoreCase("edit")) {
				// update the value to db
				psnManager.savePsn(psn);
			} else {
				// insert the value to db
				psnManager.addPsn(psn);
			}
			// 清除缓存
			PsnCache.clearAll();
			model.put("rs", "success");
			return new ModelAndView(this.getSuccessView(), model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.put("rs", "failed");
			model.put("msgs", e.getMessage());
			return new ModelAndView(this.getSuccessView(), model);
		}

	}

	/**
	 * 取回form对象
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String op = request.getParameter("op");
		String psnid = request.getParameter("psnid");
		if (op != null && op.equalsIgnoreCase("edit") && psnid != null) {
			// if edit,need get the command from db
			Long id = new Long(psnid);
			Psn psn = this.psnManager.getPsnById(id);
			if (psn != null) {
				String psn_psn = psn.getPsn();
				if (psn_psn != null && psn_psn.startsWith("ftp")) {
					psn.getPsnFtp().setPsn(psn_psn);
					psn.setPsnType("ftp");
				} else {
					psn.setLocalPath(psn_psn);
				}
				return psn;
			} else {
				return super.formBackingObject(request);
			}
		} else {
			// create the new command object
			return super.formBackingObject(request);
		}
	}

	/**
	 * 引用数据
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		String op = request.getParameter("op");
		Map model = new HashMap();
		if (op != null && op.equalsIgnoreCase("edit")) {
			model.put("op", op);
		}
		return model;
	}

	/**
	 * 绑定并校验 TODO 使用国际化以及新的校验机制
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		// at here,i use the hardcode validate method only for simply
		// and also we may need some database fucntion,if use the
		// validator external,it would need the service manager
		// it will make the things complex
		// also the client validate,i use the handwrite javascript
		// not use the common validator way,also for simply.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name_empty",
				"the name shouldn't be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "url_empty",
				"the url shouldn't be empty.");

		Psn psn = (Psn) command;
		//
		// check the name
		// TODO 允许多级上级目录，但是这个要考虑安全性
		Matcher matcherName = Pattern
				.compile("(\\*|\\\\|\\/|:|\\?|\"|<|>|\\|)").matcher(
						psn.getName());
		if (matcherName.find()) {
			errors
					.rejectValue("name", "invalid_psn_name",
							"the psn name value invalid,shouldn't contain the invalid char.");
		}

		// checke the url,it must
		// https?:\\/\\/((\\w+\\.\\w+)+|\\w+)(:\\d+)?(\\/\\p{Print}*)*
		/***********************************************************************
		 * if (psn.getUrl() != null) { String regex =
		 * "https?:\\/\\/(([\\w\\d]+\\.[\\w\\d]+)+|\\w+)(:\\d+)?(\\/\\p{Print}*)*"
		 * ; Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		 * Matcher matcher = pattern.matcher(psn.getUrl()); //pattern. if
		 * (!matcher.matches()) { errors.rejectValue("url", "psn_url_invalid",
		 * "the psn url value is invalid."); } }
		 **********************************************************************/

		// check the psn name should dupliate if new
		String op = request.getParameter("op");
		if (op != null && op.equalsIgnoreCase("Edit")) {
			// now name not permitted modified.
		} else {
			if (this.psnManager.getPsnByName(psn.getName()) != null) {
				errors.rejectValue("name", "psn_name_exist",
						"the psn name has exist.");
			}
		}
		// get the psnType,local or ftp
		// String psnType = request.getParameter("psnType");
		String psnType = psn.getPsnType();
		if (psnType.equals("local")) {
			// String localPath = request.getParameter("localPath");
			// 如果是Web根路径，则使用.
			if (!StringUtils.hasText(psn.getLocalPath())) {
				errors.rejectValue("localPath", "psn_localpath_empty",
						"the psn local path shouldn't be empty.");
				// psn shoudn't empty,and will be a valid path
				// here,i use the regular expression to hander it
				// now maybe use perl5(oro),java regular(jdk1.4+)
				// because the folder name,should not be
				// \/:*?"<>|
				// if relative:the path will relative to the web root!
				// String psn_psn = psn.getPsn();
				Matcher matcher = Pattern.compile(
						"(\\*|\\\\|\\/|:|\\?|\"|<|>|\\|)").matcher(
						psn.getLocalPath());
				if (matcher.find()) {
					errors
							.rejectValue("localPath", "invalid_psn_localpath",
									"the psn value invalid,shouldn't contain the invalid char.");
				}
			}

		} else if (psnType.equals("ftp")) {
			String ftp_host = psn.getPsnFtp().getFtpHost();
			String ftp_port = psn.getPsnFtp().getFtpPort();
			String ftp_user = psn.getPsnFtp().getFtpUser();
			String ftp_pass = psn.getPsnFtp().getFtpPass();
			String ftp_path = psn.getPsnFtp().getFtpPath();
			// check the ftp_host value
			String hostRegex = "((\\w+\\.+\\w+)+|\\w+)";

			if (StringUtils.hasText(ftp_host)) {
				ftp_host = ftp_host.trim();
				if (!ftp_host.matches(hostRegex)) {
					errors
							.rejectValue("psnFtp.ftpHost",
									"psn_ftp_host_invalid",
									"the ftp_host value invalid,shouldn't contain the invalid char.");
				}
			} else {
				errors.rejectValue("psnFtp.ftpHost", "psn_ftp_host_empty",
						"the ftp_host value shouldn't empty.");
			}
			// check the ftp_port

			if (ftp_port != null) {
				String portRegex = "\\d+";
				if (!ftp_port.matches(portRegex)) {
					errors
							.rejectValue("psnFtp.ftpPort",
									"psn_ftp_port_invalid",
									"the ftp_port value shouldn't contain the invalid char.");
				}
			}
			// check the ftp_user
			if (StringUtils.hasText(ftp_user)) {
				String userRegex = "\\w+";
				if (!ftp_user.matches(userRegex)) {
					errors
							.rejectValue("psnFtp.ftpUser", "ftp_user_invalid",
									"the ftp_user value shouldn't contain the invalid char.");
				} else {
					if (StringUtils.hasText(ftp_pass)) {
						String passRegex = "\\p{Print}*\\s*";
						if (!ftp_pass.matches(passRegex)) {
							errors
									.rejectValue("psnFtp.ftpPass",
											"ftp_pass_invalid",
											"the ftp_pass value shouldn't contain the invalid char.");
						}
					}
				}
			} else {
				if (StringUtils.hasText(ftp_pass)) {
					errors
							.rejectValue("psnFtp.ftpPass", "ftp_pass_invalid",
									"the ftp_pass value should be empty because no ftp_user.");
				}
			}

		} else {
			// error psn type
			errors.rejectValue("psnType", "psn_type_invalid",
					"the ftp_type is invalid.");
		}

	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

}
