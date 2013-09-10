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
import org.openuap.cms.tpl.manager.TemplateManager;
import org.openuap.cms.tpl.model.Template;
import org.openuap.cms.tpl.model.TemplateCategory;
import org.openuap.cms.user.model.IUser;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * <p>
 * 分类模板管理控制器.
 * </p>
 * 
 * <p>
 * $Id: CateTemplateEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0 
 */
public class CateTemplateEditAction extends CMSBaseFormAction {

	private String defaultScreensPath;

	private String operationViewName;

	private TemplateManager templateManager;

	private TemplateCateManager templateCateManager;

	/**
	 * 
	 */
	public CateTemplateEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/screens/admin/tpl/";
		operationViewName = defaultScreensPath + "cate_template_edit_op.html";
		this.setFormView(defaultScreensPath + "cate_template_edit.html");

		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(Template.class);
		this.setCommandName("tpl");
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {

		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		String op = request.getParameter("op");
		Template tpl = (Template) command;
		if (op != null && op.equals("edit")) {
			// edit
			try {
				tpl.setModifiedDate(new Long(System.currentTimeMillis()));
				templateManager.saveTemplate(tpl);
				Long tid = tpl.getId();
				model.put("tid", tid);
				model.put("tcid", tpl.getTcid());
				model.put("rs", "success");
				model.put("op", "tpl_edit");
			} catch (Exception ex) {
				ex.printStackTrace();
				model.put("rs", "failed");
				model.put("msgs", ex.getMessage());
			}
		} else {
			// add
			try {
				long now = System.currentTimeMillis();
				IUser user = this.getUser();
				tpl.setCreationDate(new Long(now));
				tpl.setCreationUid(user.getUserId());
				tpl.setCreationUserName(user.getName());
				//
				tpl.setLastModifiedUid(user.getUserId());
				tpl.setModifiedDate(new Long(now));
				tpl.setTplPink(new Long(0L));
				tpl.setTplSort(new Long(0L));
				tpl.setTplStatus(new Long(0L));
				tpl.setTplTop(new Long(0L));
				tpl.setTplType(new Long(0L));
				Long tid = templateManager.addTemplate(tpl);
				model.put("tid", tid);
				model.put("tcid", tpl.getTcid());
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
		String tplId = request.getParameter("tplId");
		String cateId = request.getParameter("cateId");
		if (cateId == null) {
			cateId = "0";
		}
		if (tplId != null) {
			Long tid = new Long(tplId);
			Template tpl = templateManager.getTemplateById(tid);
			return tpl;
		} else {
			Template tc = new Template();
			Long tcid = new Long(cateId);
			tc.setTcid(tcid);
			return tc;
		}
	}

	/**
	 * 
	 * @param request
	 *            
	 * @param command
	 *            
	 * @param errors
	 *            
	 * @return 
	 * @throws 
	 */
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map ref = new HashMap();
		String op = request.getParameter("op");
		Template tpl = (Template) command;
		Long tcid = tpl.getTcid();
		if (tcid.longValue() != 0L) {
			TemplateCategory tc = templateCateManager
					.getTemplateCategoryById(tcid);
			ref.put("tc", tc);
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

	public void setTemplateManager(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}
}
