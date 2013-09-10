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
package org.openuap.cms.node.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.ui.NodeWorkbench;
import org.openuap.cms.node.ui.NodeWorkbenchPluginManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 结点模块控制器.
 * </p>
 * 
 * <p>
 * $Id: NodeModuleAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeModuleAction extends CMSBaseAction {

	private String defaultScreensPath;

	private String moduleViewName;
	private String naviMenuViewName;
	private NodeManager nodeManager;

	private NodeWorkbenchPluginManager nodeWorkbenchPluginManager;

	public NodeModuleAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		this.defaultScreensPath = "/plugin/cms/base/screens/node/ui/";
		this.moduleViewName = defaultScreensPath + "modules.html";
		this.naviMenuViewName=defaultScreensPath + "navi_menu.htm";
	}

	/**
	 * 
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Long nodeId = helper.getLong("nodeId", 0L);
		if (!nodeId.equals(0L)) {
			ModelAndView mv = new ModelAndView(moduleViewName);
			Node node = nodeManager.getNode(nodeId);
			if (node != null) {
				//
				NodeWorkbench workbench = nodeWorkbenchPluginManager
						.getNodeWorkbench();
				//
				model.put("node", node);
				model.put("nodeId", nodeId);
				model.put("workbench", workbench);
				return mv;
			}
		}
		return null;
	}
	public ModelAndView doMenu(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Long nodeId = helper.getLong("nodeId", 0L);
		if (!nodeId.equals(0L)) {
			ModelAndView mv = new ModelAndView(naviMenuViewName);
			Node node = nodeManager.getNode(nodeId);
			if (node != null) {
				//
				NodeWorkbench workbench = nodeWorkbenchPluginManager
						.getNodeWorkbench();
				//
				model.put("node", node);
				model.put("nodeId", nodeId);
				model.put("workbench", workbench);
				return mv;
			}
		}
		return null;
	}
	public void setNodeWorkbenchPluginManager(
			NodeWorkbenchPluginManager nodeWorkbenchPluginManager) {
		this.nodeWorkbenchPluginManager = nodeWorkbenchPluginManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}
	
	
}
