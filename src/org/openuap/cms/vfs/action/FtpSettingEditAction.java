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
package org.openuap.cms.vfs.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.vfs.manager.FtpSettingManager;
import org.openuap.cms.vfs.model.FtpSetting;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * FTP设置编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: FtpSettingEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class FtpSettingEditAction extends AdminFormAction {
	private String defaultScreensPath;

	private String defaultViewName;

	private String rsViewName;
	private FtpSettingManager ftpSettingManager;
	public FtpSettingEditAction(){
		initDefaultProperty();
	}
	public void setFtpSettingManager(FtpSettingManager ftpSettingManager) {
		this.ftpSettingManager = ftpSettingManager;
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/vfs/";
		rsViewName = defaultScreensPath + "ftpsetting_edit_result.html";
		this.setFormView(defaultScreensPath + "ftpsetting_edit.html");

		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(FtpSetting.class);
		this.setCommandName("ftp");
		//
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		return super.beforePerform(request, response, helper, model);
	}

	/**
	 * 提交修改
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(rsViewName);
		try {
			FtpSetting ftp = (FtpSetting) command;
			if (ftp.getId() == null) {
				ftp.setCert("");
				ftp.setEncoding("utf-8");
				ftp.setStatus(0);
				ftp.setUsername(this.getUser().getName());
				ftpSettingManager.addFtpSetting(ftp);

			} else {
				ftpSettingManager.saveFtpSetting(ftp);
			}
			model.put("rs", "success");
		} catch (Exception e) {
			model.put("rs", "failed");
			model.put("msgs", e.getMessage());
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 重写显示编辑界面方法，添加校验权限内容
	 */
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		return showForm(request, errors, getFormView(), null, helper, model);
	}

	protected Map referenceData(HttpServletRequest request) throws Exception {
		//
		Map model = new HashMap();
		// 内容模型
		String[] modes = FtpSetting.FTP_MODES;
		String[] types = FtpSetting.FTP_TYPES;
		//
		model.put("modes", modes);
		model.put("types", types);
		return model;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @return
	 * @throws
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String tagId = request.getParameter("id");
		FtpSetting ftp = null;
		if (tagId != null) {
			Long id = new Long(tagId);
			ftp = ftpSettingManager.getFtpSettingById(id);
		} else {
			ftp = new FtpSetting();
		}
		return ftp;
	}

	/**
	 * 绑定并校验 TODO 使用国际化以及新的校验机制
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {

	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setRsViewName(String rsViewName) {
		this.rsViewName = rsViewName;
	}
}
