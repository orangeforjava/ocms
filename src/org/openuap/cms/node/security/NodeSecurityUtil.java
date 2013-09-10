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
package org.openuap.cms.node.security;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.user.manager.IPermissionManager;
import org.openuap.cms.user.security.IUserSession;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 基于结点的权限判断帮助类
 * </p>
 * 
 * <p>
 * $Id: NodeSecurityUtil.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeSecurityUtil {

	private static IPermissionManager permissionManager = null;
	private static NodeManager nodeManager = null;

	/**
	 * 判断用户是否具有指定的权限 不同之处在于按照站点之间的继承关系计算 类似于论坛的总斑竹，斑竹，副斑竹管理关系具有级别关系
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param objType
	 *            对象类型标识
	 * @param userId
	 *            用户id
	 * @param permission
	 *            权限
	 * @return
	 */
	public static boolean hasPermission(Long nodeId, String objType,
			long permission) {
		IUserSession userSession = SecurityUtil.getUserSession();
		String uid = userSession.getUserID();
		Long userId = Long.parseLong(uid);
		if (userSession.isAdmin()) {
			// 管理员全部授权
			return true;
		}
		if (nodeId.equals(0L)) {
			return SecurityUtil.hasPermission(objType, "0", permission);
		}
		boolean rs = getPermissionManager().hasPermission(userId, objType,
				nodeId.toString(), permission);
		if (!rs) {
			Node node = getNodeManager().getNode(nodeId);
			if (node != null) {
				long pid = node.getParentId();
				if (pid != 0L) {
					return hasPermission(pid, objType, permission);
				}
			}
		}
		return rs;
	}

	public static boolean hasPermission(String objType, String nodeId,
			long permission) {
		Long nid = new Long(nodeId);
		return hasPermission(nid, objType, permission);
	}

	public static IPermissionManager getPermissionManager() {
		if (permissionManager == null) {
			permissionManager = (IPermissionManager) ObjectLocator.lookup(
					"permissionManager", CmsPlugin.PLUGIN_ID);
		}
		return permissionManager;
	}

	public static NodeManager getNodeManager() {
		if (nodeManager == null) {
			nodeManager = (NodeManager) ObjectLocator.lookup("nodeManager",
					CmsPlugin.PLUGIN_ID);
		}
		return nodeManager;
	}

}
