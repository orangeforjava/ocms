/*
 * Copyright 2002-2009 the original author or authors.
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
package org.openuap.cms.workbench.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.workbench.ui.Workbench;
import org.openuap.cms.workbench.ui.WorkbenchProperty;
import org.openuap.plugin.Plugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 工作台控制器
 * </p>
 * <p>
 * $Id: WorkbenchAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class WorkbenchAction extends AdminAction {

	private String defaultScreensPath;

	private String workbenchViewName;

	private String topViewName;

	private String botViewName;

	private String mainViewName;

	private String toogleViewName;

	private String naviMenuViewName;

	private String statusViewName;
	
	private NodeManager nodeManager;
	/**
	 * 
	 * 
	 */
	public WorkbenchAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		this.defaultScreensPath = "/plugin/cms/workbench3/";
		this.workbenchViewName = defaultScreensPath + "index.htm";
		this.topViewName = defaultScreensPath + "top.html";
		this.botViewName = defaultScreensPath + "bottom.html";
		this.mainViewName = defaultScreensPath + "main.html";
		this.toogleViewName = defaultScreensPath + "toogle.html";
		this.naviMenuViewName = defaultScreensPath + "navi_menu.htm";
		this.statusViewName = defaultScreensPath + "status.html";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		super.beforePerform(request, response, helper, model);
		Workbench workbench = getWorkbench();
		WorkbenchProperty workbenchProperty = this.getWorkbenchProperty();
		model.put("workbench", workbench);
		model.put("workbenchProperty", workbenchProperty);
		return null;
	}

	/**
	 * 显示WorkBench主视图
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(workbenchViewName, model);
		model.put("nodeManager", nodeManager);
		return mv;
	}
	/**
	 * 返回当前位置
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doCurrentPos(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView("/content.htm", model);
		String id=helper.getString("id","");
		String menu=getWorkbench().getCurrentPos(id);
		model.put("content", menu);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doTop(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(topViewName, model);
		//
		Workbench workbench = getWorkbench();
		model.put("workbench", workbench);
		return mv;
	}

	public ModelAndView doStatus(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(statusViewName, model);
		//
		Workbench workbench = getWorkbench();
		model.put("workbench", workbench);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doBot(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(botViewName, model);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doToogle(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(toogleViewName, model);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doMain(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(mainViewName, model);
		String perspective = request.getParameter("perspective");
		model.put("perspective", perspective);
		return mv;
	}

	/**
	 * 导航菜单
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doNavMenu(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(naviMenuViewName, model);
		String pageID = request.getParameter("pageID");
		String sideBarID = request.getParameter("sideBarID");
		if (pageID != null) {
			model.put("pageID", pageID);
		}
		if (sideBarID != null) {
			model.put("sideBarID", sideBarID);
		}
		return mv;
	}

	private Workbench getWorkbench() {
		Plugin plugin = WebPluginManagerUtils.getPlugin(this
				.getServletContext(), CmsPlugin.PLUGIN_ID);
		if (plugin != null && plugin instanceof CmsPlugin) {
			CmsPlugin wPlugin = (CmsPlugin) plugin;
			Workbench workbench = wPlugin.getWorkbench();
			return workbench;
		}
		return null;
	}
	
	public NodeManager getNodeManager() {
		return nodeManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	private WorkbenchProperty getWorkbenchProperty() {
		Plugin plugin = WebPluginManagerUtils.getPlugin(this
				.getServletContext(), CmsPlugin.PLUGIN_ID);
		if (plugin != null && plugin instanceof CmsPlugin) {
			CmsPlugin wPlugin = (CmsPlugin) plugin;
			WorkbenchProperty workbenchProperty = wPlugin
					.getWorkbenchProperty();
			return workbenchProperty;
		}
		return null;
	}

	public void setStatusViewName(String statusViewName) {
		this.statusViewName = statusViewName;
	}
}
