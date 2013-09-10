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
package org.openuap.cms.tpl.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.CMSBaseFormAction;
import org.openuap.cms.tpl.manager.TemplateCateManager;
import org.openuap.cms.tpl.model.TemplateCategory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 模板分类控制器.
 * </p>
 * 
 * <p>
 * $Id: TemplateCateEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateCateEditAction extends CMSBaseFormAction {

	private String defaultScreensPath;
	private String operationViewName;

	private TemplateCateManager templateCateManager;

	/**
	 * 
	 */
	public TemplateCateEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/tpl/";
		operationViewName = defaultScreensPath + "template_edit_op.html";
		this.setFormView(defaultScreensPath + "template_cate_edit.html");

		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(TemplateCategory.class);
		this.setCommandName("tplCate");
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {

		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		String op = request.getParameter("op");
		TemplateCategory tc = (TemplateCategory) command;
		if (op != null && op.equals("edit")) {
			// edit
			try {
				tc.setModifiedDate(new Long(System.currentTimeMillis()));
				templateCateManager.saveTemplateCategory(tc);
				Long tcid = tc.getId();
				model.put("tcid", tcid);
				model.put("rs", "success");
				model.put("op", "tplCate_edit");
			} catch (Exception ex) {
				ex.printStackTrace();
				model.put("rs", "failed");
				model.put("msgs", ex.getMessage());
			}
		} else {
			// add
			try {
				long now = System.currentTimeMillis();
				tc.setCateStatus(new Long(0L));
				tc.setCreationDate(new Long(now));
				//
				tc.setCreationUid(this.getUser().getUserId());
				tc.setModifiedDate(new Long(now));
				Long tcid = templateCateManager.addTemplateCategory(tc);
				model.put("tcid", tcid);
				model.put("rs", "success");
				model.put("op", "tplCate_add");
			} catch (Exception ex1) {
				ex1.printStackTrace();
				model.put("rs", "failed");
				model.put("msgs", ex1.getMessage());
			}
		}
		return mv;
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {

	}

	protected Object formBackingObject(HttpServletRequest request) {
		String tplCateId = request.getParameter("tplCateId");
		String parentId = request.getParameter("parentId");
		if (parentId == null) {
			parentId = "0";
		}
		if (tplCateId != null) {
			Long tcid = new Long(tplCateId);
			TemplateCategory tc = templateCateManager
					.getTemplateCategoryById(tcid);
			return tc;
		} else {
			TemplateCategory tc = new TemplateCategory();
			Long pid = new Long(parentId);
			tc.setParentId(pid);
			return tc;
		}
	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param command
	 *            Object
	 * @param errors
	 *            Errors
	 * @return Map
	 * @throws Exception
	 */
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map ref = new HashMap();
		String op = request.getParameter("op");
		TemplateCategory tc = (TemplateCategory) command;
		Long pid = tc.getParentId();
		if (pid.longValue() != 0L) {
			TemplateCategory ptc = templateCateManager
					.getTemplateCategoryById(pid);
			ref.put("ptc", ptc);
		}
		//
		if (op != null && op.equals("edit")) {
			ref.put("op", op);
		} else {
			ref.put("op", "add");
		}
		return ref;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setTemplateCateManager(TemplateCateManager templateCateManager) {
		this.templateCateManager = templateCateManager;
	}

	public void setOperationViewName(String operationViewName) {
		this.operationViewName = operationViewName;
	}
}
