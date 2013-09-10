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
package org.openuap.cms.node.permission.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.permission.manager.NodePermissionManager;
import org.openuap.cms.node.security.NodePermissionConstant;
import org.openuap.cms.publish.security.PublishPermissionConstant;
import org.openuap.cms.resource.security.ResourcePermissionConstant;
import org.openuap.cms.user.manager.IPermissionManager;
import org.openuap.cms.user.manager.IRoleManager;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 缺省结点权限管理实现
 * </p>
 * 
 * <p>
 * $Id: DefaultNodePermissionManager.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultNodePermissionManager implements NodePermissionManager {
	private IPermissionManager permissionManager;
	private IRoleManager roleManager;

	private NodeManager nodeManager;

	public List<IUser> getInheritUsers(Long nodeId) {
		Node node = getNodeManager().getNode(nodeId);
		List<IUser> rs = new ArrayList<IUser>();
		if (node != null) {
			Long pid = node.getParentId();
			if (pid != 0L) {
				rs.addAll(getNodeUsers(pid));
				rs.addAll(getInheritUsers(pid));
			}
		}
		return rs;
	}

	public List<IRole> getNodeRoles(Long nodeId) {
		return getPermissionManager().getObjRoles(nodeId.toString(),
				NodePermissionConstant.OBJECT_TYPE);
	}

	public List<IUser> getNodeUsers(Long nodeId) {
		return getPermissionManager().getObjUsers(nodeId.toString(),
				NodePermissionConstant.OBJECT_TYPE);
	}

	public void initNodeRole(Long nodeId) {
		// 建立角色
		createAllRole(nodeId);

	}

	public void reinitNodeRole(Long nodeId) {
		initNodeRole(nodeId);
	}

	protected void createAllRole(Long nodeId) {
		createRole(nodeId, ROLE_ANONYMOUS, ROLE_ANONYMOUS_NAME);
		createRole(nodeId, ROLE_MEMBER, ROLE_MEMBER_NAME);
		createRole(nodeId, ROLE_INPUT, ROLE_INPUT_NAME);
		createRole(nodeId, ROLE_EDITOR, ROLE_EDITOR_NAME);
		createRole(nodeId, ROLE_SUPER_EDITOR, ROLE_SUPER_EDITOR_NAME);
		createRole(nodeId, ROLE_MAINTAIN, ROLE_MAINTAIN_NAME);
		createRole(nodeId, ROLE_ADMIN, ROLE_ADMIN_NAME);
	}

	protected void createRole(Long nodeId, String name, String title) {
		String guid = nodeId + "-" + name;
		IRole role = getRoleManager().getRoleByGuid(nodeId + "-" + name);
		if (role == null) {
			role = getRoleManager().createRole();
			long now = System.currentTimeMillis();
			role.setGuid(guid);
			role.setTitle(title);
			role.setName(name);
			role.setCreationDate(now);
			role.setDescription(title);
			role.setModificationDate(now);
			role.setStatus(0);
			role.setPos(0);
			try {
				Long roleId = getRoleManager().addRole(role);
				role.setRoleId(roleId);
			} catch (UnauthorizedException e) {
				e.printStackTrace();
			}
		}
		//
		Long roleId = role.getRoleId();
		if (name.equals(ROLE_ANONYMOUS)) {
			// 匿名用户暂不设置权限
			getPermissionManager()
					.setRolePermission(roleId,
							PublishPermissionConstant.OBJECT_TYPE,
							nodeId.toString(), 0);
			getPermissionManager().setRolePermission(roleId,
					NodePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					NodePermissionConstant.ROLE_ANONYMOUS);
		} else if (name.equals(ROLE_MEMBER)) {
			// 会员用户暂时给予查看权限
			getPermissionManager().setRolePermission(roleId,
					PublishPermissionConstant.OBJECT_TYPE, nodeId.toString(),
					PublishPermissionConstant.ROLE_MEMBER);
			getPermissionManager().setRolePermission(roleId,
					NodePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					NodePermissionConstant.ROLE_MEMBER);
		} else if (name.equals(ROLE_INPUT)) {
			// 撰稿人赋予写作权限
			getPermissionManager().setRolePermission(roleId,
					PublishPermissionConstant.OBJECT_TYPE, nodeId.toString(),
					PublishPermissionConstant.ROLE_INPUT);
			getPermissionManager().setRolePermission(roleId,
					NodePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					NodePermissionConstant.ROLE_INPUT);
		} else if (name.equals(ROLE_EDITOR)) {
			// 编辑给予写作、发布权限
			getPermissionManager().setRolePermission(roleId,
					PublishPermissionConstant.OBJECT_TYPE, nodeId.toString(),
					PublishPermissionConstant.ROLE_EDITOR);
			getPermissionManager().setRolePermission(roleId,
					NodePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					NodePermissionConstant.ROLE_EDITOR);
			getPermissionManager().setRolePermission(roleId,
					ResourcePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					ResourcePermissionConstant.Admin);
		} else if (name.equals(ROLE_SUPER_EDITOR)) {
			// 总编赋予写作、发布、编辑模板、编辑结点权限
			getPermissionManager().setRolePermission(
					roleId,
					PublishPermissionConstant.OBJECT_TYPE,
					nodeId.toString(),
					PublishPermissionConstant.ROLE_EDITOR
							| PublishPermissionConstant.ROLE_MAINTAIN);
			getPermissionManager().setRolePermission(roleId,
					NodePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					NodePermissionConstant.ROLE_SUPER_EDITOR);
			//资源权限
			getPermissionManager().setRolePermission(roleId,
					ResourcePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					ResourcePermissionConstant.Admin);
			
		} else if (name.equals(ROLE_MAINTAIN)) {
			// 站点维护员，编辑结点设置，编辑模板，如果有必要的话
			getPermissionManager().setRolePermission(roleId,
					PublishPermissionConstant.OBJECT_TYPE, nodeId.toString(),
					PublishPermissionConstant.ROLE_MAINTAIN);
			getPermissionManager().setRolePermission(roleId,
					NodePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					NodePermissionConstant.ROLE_MAINTAIN);
		} else if (name.equals(ROLE_ADMIN)) {
			// 站点管理员，所有权限
			getPermissionManager().setRolePermission(roleId,
					PublishPermissionConstant.OBJECT_TYPE, nodeId.toString(),
					PublishPermissionConstant.ROLE_ADMIN);
			getPermissionManager().setRolePermission(roleId,
					NodePermissionConstant.OBJECT_TYPE, nodeId.toString(),
					NodePermissionConstant.ROLE_ADMIN);
			
		}

	}
	
	protected void createRolePermission(Long nodeId, String name) {
		//
		String guid = nodeId + "-" + name;
		IRole role = getRoleManager().getRoleByGuid(guid);
		if (role != null) {

		}

	}

	public NodeManager getNodeManager() {
		if (nodeManager == null) {
			nodeManager = (NodeManager) ObjectLocator.lookup("nodeManager",
					CmsPlugin.PLUGIN_ID);
		}
		return nodeManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public IPermissionManager getPermissionManager() {
		if (permissionManager == null) {
			permissionManager = (IPermissionManager) ObjectLocator.lookup(
					"permissionManager", CmsPlugin.PLUGIN_ID);
		}
		return permissionManager;
	}

	public void setPermissionManager(IPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

	public IRoleManager getRoleManager() {
		if (roleManager == null) {
			roleManager = (IRoleManager) ObjectLocator.lookup(
					"baseRoleManager",  CmsPlugin.PLUGIN_ID);
		}
		return roleManager;
	}

	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}

}
