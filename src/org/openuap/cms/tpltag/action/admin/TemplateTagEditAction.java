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
package org.openuap.cms.tpltag.action.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.tpltag.manager.TemplateTagManager;
import org.openuap.cms.tpltag.model.TemplateTag;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 模板标签编辑控制器
 * </p>
 * 
 * <p>
 * $Id: TemplateTagEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateTagEditAction extends AdminFormAction {

	private String defaultScreensPath;

	private String defaultViewName;

	private String rsViewName;

	private TemplateTagManager templateTagManager;
	
	private ContentTableManager contentTableManager;
	
	/** 结点管理. */
	private NodeManager nodeManager;

	



	public NodeManager getNodeManager() {
		return nodeManager;
	}



	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}



	public TemplateTagEditAction() {
		initDefaultProperty();
	}

	

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/tpltag/admin/";
		rsViewName = defaultScreensPath + "tpltag_op_result.html";
		defaultViewName=defaultScreensPath + "tpltag_edit.html";
		this.setFormView(defaultViewName);

		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(TemplateTag.class);
		this.setCommandName("tplTag");
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
		ModelAndView mv=new ModelAndView(rsViewName);
		
		try {
			TemplateTag tag = (TemplateTag) command;
			if(tag.getId()==null||tag.getId().longValue()==0L){
				//新增
				model.put("op", "add");
				long now=System.currentTimeMillis();
				tag.setCreationDate(now);
				tag.setEditedDate(now);
				//tag.setEditorId(this.getUser().getUserId());
				//tag.setEditorName(this.getUser().getName());
				tag.setPos(0);
				tag.setStatus(0);
				tag.setUserId(this.getUser().getUserId());
				tag.setUserName(this.getUser().getName());
				tag.setCacheType(0);
				tag.setOutType(0);
				templateTagManager.addTag(tag);
				model.put("rs", "sucess");
			}else{
				//编辑
				model.put("op", "edit");
				long now=System.currentTimeMillis();
				tag.setEditedDate(now);
				tag.setEditorId(this.getUser().getUserId());
				tag.setEditorName(this.getUser().getName());
				templateTagManager.saveTag(tag);
				model.put("rs", "sucess");
			}
		} catch (Exception e) {
			model.put("rs", "failed");
			model.put("msgs", e.getMessage());
			this.log.error(e);
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
		//内容模型
		List models=getContentTableManager().getAllContentTableFromCache();
		//
		model.put("models",models);
		//结点管理器
		model.put("nodeManager", nodeManager);
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
		TemplateTag tag = null;
		if (tagId != null) {
			Long id = new Long(tagId);
			tag = templateTagManager.getTagById(id);
		} else {
			tag = new TemplateTag();
		}
		return tag;
	}

	/**
	 * 绑定并校验 TODO 使用国际化以及新的校验机制
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {

	}
	public ContentTableManager getContentTableManager() {
		if(contentTableManager==null){
			contentTableManager=(ContentTableManager) ObjectLocator
			.lookup("contentTableManager", CmsPlugin.PLUGIN_ID);
		}
		return contentTableManager;
	}



	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
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

	public void setTemplateTagManager(TemplateTagManager templateTagManager) {
		this.templateTagManager = templateTagManager;
	}
}
