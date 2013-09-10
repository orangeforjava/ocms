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
package org.openuap.cms.resource.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.resource.manager.ResourceManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 资源选择控制器.
 * </p>
 * 
 * <p>
 * $Id: ResourceSelectAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ResourceSelectAction extends AdminAction {

	private String defaultScreensPath;

	private String rsSelDialogViewName;

	private String rsSelViewName;

	private String rsSelFramesetViewName;

	//
	private NodeManager nodeManager;

	private ResourceManager resourceManager;

	/**
	 * 
	 */
	public ResourceSelectAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/resource/dialog/";
		rsSelDialogViewName = defaultScreensPath + "resource_select_dialog.html";
		rsSelViewName = defaultScreensPath + "resource_select.html";
		rsSelFramesetViewName = defaultScreensPath + "resource_select_frameset.html";
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
	public ModelAndView doResourceFrameset(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rsSelFramesetViewName, model);
		String nodeId = request.getParameter("nodeId");
		String category = request.getParameter("category");
		// 自定义分类
		String customCategory = request.getParameter("customCategory");
		if (nodeId == null) {
			nodeId = "0";
		}
		if (category == null) {
			category = "img";
		}
		if (customCategory == null) {
			customCategory = "";
		}
		model.put("nodeId", nodeId);
		model.put("category", category);
		model.put("customCategory", customCategory);
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
	public ModelAndView doResourceDialog(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rsSelDialogViewName, model);
		String nodeId = request.getParameter("nodeId");
		String page = request.getParameter("page");
		String pagesize = request.getParameter("pageSize");
		String category = request.getParameter("category");
		String haveLinks = request.getParameter("haveLinks");
		String customCategory = request.getParameter("customCategory");
		if (nodeId == null) {
			nodeId = "0";
		}
		if (page == null) {
			page = "1";
		}
		if (pagesize == null) {
			pagesize = "18";
		}
		if (category == null) {
			category = "img";
		}
		if (haveLinks == null) {
			haveLinks = "2";
		}
		Long nid = new Long(nodeId);
		Long ipage = new Long(page);
		Long pageSize = new Long(pagesize);
		// List resources = null;
		// int start=(ipage.intValue()-1)*ilength.intValue();
		long totalCount = 0;
		if (nodeId.equals("0")) {
			if (customCategory != null && !customCategory.equals("")) {
				totalCount = resourceManager.getResourceCountByCata(category, customCategory);
			} else {
				totalCount = resourceManager.getResourceCountByCata(category);
			}
		} else {
			if (customCategory != null && !customCategory.equals("")) {
				totalCount = resourceManager.getResourceCountByNodeCata(nid, category,
						customCategory);
			} else {
				totalCount = resourceManager.getResourceCountByNodeCata(nid, category);
			}

		}
		Node node = nodeManager.getNodeById(nid);
		model.put("nodeManager", nodeManager);
		model.put("node", node);
		model.put("action", this);
		model.put("page", ipage);
		model.put("pageSize", pageSize);
		model.put("category", category);
		model.put("totalCount", new Long(totalCount));
		model.put("haveLinks", haveLinks);
		//
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
	public ModelAndView doResourceList(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rsSelViewName, model);
		String nodeId = request.getParameter("nodeId");
		String page = request.getParameter("page");
		String pagesize = request.getParameter("pageSize");
		String category = request.getParameter("category");
		String haveLinks = request.getParameter("haveLinks");
		//
		String customCategory = request.getParameter("customCategory");
		//
		if (nodeId == null) {
			nodeId = "0";
		}
		if (page == null) {
			page = "1";
		}
		if (pagesize == null) {
			pagesize = "18";
		}
		if (category == null) {
			category = "img";
		}
		if (haveLinks == null) {
			haveLinks = "2";
		}
		Long nid = new Long(nodeId);
		Long ipage = new Long(page);
		Long pageSize = new Long(pagesize);
		List resources = null;
		int start = (ipage.intValue() - 1) * pageSize.intValue();
		long totalCount = 0;
		if (nodeId.equals("0")) {
			if (customCategory != null && !customCategory.equals("")) {
				resources = resourceManager.getResourcesByCata(category, customCategory, new Long(
						start), pageSize);
			} else {
				resources = resourceManager.getResourcesByCata(category, new Long(start), pageSize);
			}
		} else {
			if (customCategory != null && !customCategory.equals("")) {
				resources = resourceManager.getResourcesByNodeCata(nid, category, customCategory,
						new Long(start), pageSize);
			} else {
				resources = resourceManager.getResourcesByNodeCata(nid, category, new Long(start),
						pageSize);
			}
		}
		Node node = nodeManager.getNodeById(nid);
		model.put("resources", resources);
		model.put("node", node);
		model.put("action", this);
		model.put("page", ipage);
		model.put("category", category);
		model.put("totalCount", new Long(totalCount));
		model.put("haveLinks", haveLinks);
		//
		return mv;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setRsSelDialogViewName(String rsSelDialogViewName) {
		this.rsSelDialogViewName = rsSelDialogViewName;
	}

	public void setRsSelViewName(String rsSelViewName) {
		this.rsSelViewName = rsSelViewName;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
}
