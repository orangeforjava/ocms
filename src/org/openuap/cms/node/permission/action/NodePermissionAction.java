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
package org.openuap.cms.node.permission.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.permission.manager.NodePermissionManager;
import org.openuap.cms.node.security.NodePermissionConstant;
import org.openuap.cms.node.security.NodeSecurityUtil;
import org.openuap.cms.user.manager.IRoleManager;
import org.openuap.cms.user.model.IRole;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 结点权限控制器.
 * </p>
 * 
 * <p>
 * $Id: NodePermissionAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodePermissionAction extends AdminAction {

	private String defaultScreensPath;

	private String nodePermissionViewName;

	private String rsViewName;

	private NodePermissionManager nodePermissionManager;

	private NodeManager nodeManager;

	public String getDefaultScreensPath() {
		return defaultScreensPath;
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/node/permission/";
		nodePermissionViewName = defaultScreensPath + "node_permission.html";
		rsViewName = defaultScreensPath + "op_result.html";
		;
	}

	/**
	 * 查看结点的角色信息
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Long nid = helper.getLong("id", 0L);
		if (!NodeSecurityUtil.hasPermission(nid,
				NodePermissionConstant.OBJECT_TYPE,
				NodePermissionConstant.ViewPermission)) {
			throw new UnauthorizedException();
		}
		//
		Node node = nodeManager.getNode(nid);
		if (node != null) {
			//
			ModelAndView mv = new ModelAndView(nodePermissionViewName);
			List<IRole> roles = nodePermissionManager.getNodeRoles(nid);
			IRoleManager roleManager = nodePermissionManager.getRoleManager();
			model.put("roles", roles);
			model.put("nodeId", nid);
			model.put("node", node);
			model.put("roleManager", roleManager);
			return mv;
		}
		return null;
	}

	public ModelAndView doInitRole(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Long nid = helper.getLong("id", 0L);
		if (!NodeSecurityUtil.hasPermission(nid,
				NodePermissionConstant.OBJECT_TYPE,
				NodePermissionConstant.EditPermission)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(rsViewName);
		model.put("op", "init");
		model.put("id", nid);
		try {
			nodePermissionManager.initNodeRole(nid);
			model.put("rs", "success");
		} catch (Exception e) {
			// e.printStackTrace();
			model.put("rs", "failed");
			model.put("msgs", e.getMessage());
		}
		return mv;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public NodePermissionManager getNodePermissionManager() {
		return nodePermissionManager;
	}

	public void setNodePermissionManager(
			NodePermissionManager nodePermissionManager) {
		this.nodePermissionManager = nodePermissionManager;
	}

	public NodePermissionAction() {
		initDefaultViewName();
	}

	public String getNodePermissionViewName() {
		return nodePermissionViewName;
	}

	public void setNodePermissionViewName(String nodePermissionViewName) {
		this.nodePermissionViewName = nodePermissionViewName;
	}

	public NodeManager getNodeManager() {
		return nodeManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

}
