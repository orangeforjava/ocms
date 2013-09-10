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
package org.openuap.cms.config.action;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.config.security.ConfigPermissionConstant;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.user.security.permissions.UserPermissionConstant;
import org.openuap.license.License;
import org.openuap.license.LicenseContainer;
import org.openuap.license.LicenseManager;
import org.openuap.license.LicenseUtils;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.setup.cms.CmsLicenseUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 系统配置控制器.
 * </p>
 * 
 * <p>
 * $Id: SysConfigAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SysConfigAction extends AdminAction {

	private String defaultViewName;

	private String defaultScreensPath;

	private String sysConfigViewName;

	private String licenseViewName;

	public SysConfigAction() {
		initDefaultViewName();
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		ModelAndView mv = super.beforePerform(request, response, helper, model);
		if (mv != null) {
			return mv;
		}

		return null;
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/config/";
		defaultViewName = defaultScreensPath + "config.html";
		sysConfigViewName = defaultScreensPath + "sys_config.htm";
		licenseViewName = defaultScreensPath + "license.html";
	}

	/**
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
	 * @throws UnauthorizedException
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		if (!SecurityUtil.hasPermission(ConfigPermissionConstant.OBJECT_TYPE,
				"-1", ConfigPermissionConstant.ViewConfig)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		CMSConfig config = CMSConfig.getInstance();
		model.put("config", config);
		return mv;
	}

	public ModelAndView doSysConfig(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(sysConfigViewName, model);
		CMSConfig config = CMSConfig.getInstance();
		model.put("config", config);
		return mv;

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doChange(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		if (!SecurityUtil.hasPermission(ConfigPermissionConstant.OBJECT_TYPE,
				"-1", ConfigPermissionConstant.EditConfig)) {
			throw new UnauthorizedException();
		}
		try {

			//
			Iterator iter = helper.toMap().keySet().iterator();
			//

			while (iter.hasNext()) {
				String key = (String) iter.next();
				if (key.indexOf(".") >= 0) {
					this.config.setProperty(key, helper.getString(key, ""));
				}
			}
			this.config.save();
			CMSConfig.getInstance().refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		CMSConfig config = CMSConfig.getInstance();
		model.put("config", config);
		return mv;
	}

	/**
	 * 显示协议信息
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doViewLicense(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		License l = CmsLicenseUtils.getLicense();
		LicenseContainer lc = LicenseManager.getLicenseContainer();
		// LicenseUtils lu=new ForumsLicenseUtils();

		model.put("lic", l);
		model.put("lc", lc);
		model.put("action", this);
		return new ModelAndView(licenseViewName, model);
	}

	public void setLicenseViewName(String licenseViewName) {
		this.licenseViewName = licenseViewName;
	}

	// //~
	public String getProductDisplayName(String product) {
		return LicenseUtils.getProductDisplayName(product);
	}

	public String getEditionDisplayName(String product, String edition) {
		return LicenseUtils.getEditionDisplayName(product, edition);
	}

	public String getPropertyDisplayName(String key) {
		return LicenseUtils.getPropertyDisplayName(key);
	}

	public boolean isProductInstalled(String product) {
		return LicenseUtils.isProductInstalled(product);
	}

	public String getSystemProperty(String key) {
		return System.getProperty(key);
	}
}
