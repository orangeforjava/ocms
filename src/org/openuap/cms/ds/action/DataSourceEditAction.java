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
package org.openuap.cms.ds.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.ds.manager.DataSourceManager;
import org.openuap.cms.ds.model.DataSourceModel;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.setup.DatabaseSetupUtil;
import org.openuap.runtime.setup.DatabaseType;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 数据源编辑控制器
 * </p>
 * 
 * <p>
 * $Id: DataSourceEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DataSourceEditAction extends AdminFormAction {

	private String defaultScreensPath;

	private String defaultViewName;

	private String rsViewName;
	private DataSourceManager dataSourceManager;

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	public DataSourceEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/ds/";
		rsViewName = defaultScreensPath + "ds_edit_result.htm";
		this.setFormView(defaultScreensPath + "ds_edit.htm");

		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(DataSourceModel.class);
		this.setCommandName("ds");
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
		ModelAndView mv = new ModelAndView(rsViewName);
		try {
			DataSourceModel ds = (DataSourceModel) command;
			if (ds.getId() == null) {
				ds.setStatus(0);
				dataSourceManager.addDataSource(ds);
			} else {
				dataSourceManager.saveDataSource(ds);
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
		DatabaseType[] databases = new DatabaseType[] { DatabaseType.MySQL,
				DatabaseType.MySQL3, DatabaseType.MSSQL, DatabaseType.Oracle9i };
		model.put("databases", databases);
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
		DataSourceModel ds = null;
		if (tagId != null) {
			Long id = new Long(tagId);
			ds = dataSourceManager.getDataSourceById(id);
		} else {
			ds = new DataSourceModel();
		}
		return ds;
	}

	/**
	 * 绑定并校验
	 * <p>
	 * TODO 使用国际化以及新的校验机制
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
	private Properties getConfigProperties(String databaseName) {
		return DatabaseSetupUtil.getProperties("org/openuap/runtime/setup/database/" + databaseName.toLowerCase()
				+ ".properties", org.openuap.runtime.setup.HibernateDatabaseDetails.class);
	}
}
