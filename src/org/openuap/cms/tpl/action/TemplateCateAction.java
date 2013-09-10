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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.tpl.manager.TemplateCateManager;
import org.openuap.cms.tpl.model.TemplateCategory;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 模板分类控制器
 * </p>
 * 
 * <p>
 * $Id: TemplateCateAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateCateAction extends AdminAction {
	private String cateTreeViewName;
	private String cateTemplateFramesetViewName;

	private String cateTemplateHeaderViewName;
	private String cateTemplateXmlViewName;

	private String cateTemplateEditXmlViewName;

	private String cateEditTreeViewName;

	private String defaultScreensPath;
	// 模板分类管理者
	private TemplateCateManager templateCateManager;
	
	public TemplateCateAction(){
		initDefaultViewName();
	}
	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/tpl/cate/";
		cateTreeViewName = defaultScreensPath + "template_cate.html";
		cateEditTreeViewName = defaultScreensPath
				+ "template_cate_edit_tree.html";
		cateTemplateXmlViewName = defaultScreensPath + "template_cate_tree.xml";
		cateTemplateEditXmlViewName = defaultScreensPath
				+ "template_cate_edit_tree.xml";
		cateTemplateFramesetViewName = defaultScreensPath
				+ "cate_template_list_frameset.html";
		cateTemplateHeaderViewName = defaultScreensPath
				+ "cate_template_list_header.html";
	}

	/**
	 * 
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
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(cateTreeViewName, model);
		List templateCates = templateCateManager
				.getTemplateCategories(new Long(0));
		model.put("templateCates", templateCates);
		model.put("tcManager", this.templateCateManager);
		return mv;
	}

	/**
	 * 
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
	 */
	public ModelAndView doListByCateFrameset(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(cateTemplateFramesetViewName, model);
		String tplCateId = helper.getString("tcid");
		if (tplCateId == null) {
			tplCateId = "0";
		}
		//
		model.put("tcid", tplCateId);
		return mv;

	}

	/**
	 * 
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
	 */
	public ModelAndView doCategoryEdit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(cateEditTreeViewName, model);
		List templateCates = templateCateManager
				.getTemplateCategories(new Long(0));
		model.put("templateCates", templateCates);
		model.put("tcManager", this.templateCateManager);
		return mv;
	}

	/**
	 * 
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
	 */
	public ModelAndView doTplCateXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String parentId = helper.getString("parentId");
		if (parentId == null) {
			parentId = "0";
		}
		//
		Long pid = new Long(parentId);
		//
		ModelAndView mv = new ModelAndView(cateTemplateXmlViewName, model);
		List templateCates = templateCateManager.getTemplateCategories(pid);
		model.put("templateCates", templateCates);
		//
		setNoCacheHeader(response);
		model.put("responseType", "text/xml");
		return mv;
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
	 */
	public ModelAndView doTplCateEditXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String parentId = helper.getString("parentId");
		if (parentId == null) {
			parentId = "0";
		}
		//
		Long pid = new Long(parentId);
		//
		ModelAndView mv = new ModelAndView(cateTemplateEditXmlViewName, model);
		List templateCates = templateCateManager.getTemplateCategories(pid);
		model.put("templateCates", templateCates);
		model.put("tcManager", this.templateCateManager);
		//
		setNoCacheHeader(response);
		model.put("responseType", "text/xml");
		return mv;
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
	 *            Map
	 * @return ModelAndView
	 */
	public ModelAndView doListByCateHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(cateTemplateHeaderViewName, model);
		String tplCateId = helper.getString("tcid");
		if (tplCateId == null) {
			tplCateId = "0";
		}
		Long tcid = new Long(tplCateId);
		TemplateCategory tc = templateCateManager.getTemplateCategoryById(tcid);
		//
		model.put("tc", tc);
		return mv;

	}

	public void setTemplateCateManager(TemplateCateManager templateCateManager) {
		this.templateCateManager = templateCateManager;
	}

}
