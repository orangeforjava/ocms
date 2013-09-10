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
package org.openuap.cms.node.permission.manager;

import java.util.List;

import org.openuap.cms.user.manager.IRoleManager;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;

/**
 * <p>
 * 结点权限管理接口.
 * </p>
 * 
 * <p>
 * $Id: NodePermissionManager.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface NodePermissionManager {

	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String ROLE_MEMBER = "ROLE_MEMBER";
	public static final String ROLE_INPUT = "ROLE_INPUT";
	public static final String ROLE_EDITOR = "ROLE_EDITOR";
	public static final String ROLE_SUPER_EDITOR = "ROLE_SUPER_EDITOR";
	public static final String ROLE_MAINTAIN = "ROLE_MAINTAIN";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	public static final String ROLE_ANONYMOUS_NAME = "匿名用户";
	public static final String ROLE_MEMBER_NAME = "站点会员";
	public static final String ROLE_INPUT_NAME = "撰稿人";
	public static final String ROLE_EDITOR_NAME = "编辑";
	public static final String ROLE_SUPER_EDITOR_NAME = "主编";
	public static final String ROLE_MAINTAIN_NAME = "站点维护员";
	public static final String ROLE_ADMIN_NAME = "站点管理员";

	/**
	 * 获得站点的角色列表
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<IRole> getNodeRoles(Long nodeId);

	/**
	 * 获得站点的用户列表
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<IUser> getNodeUsers(Long nodeId);

	/**
	 * 获得继承而来的用户列表
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<IUser> getInheritUsers(Long nodeId);

	/**
	 * 初始化结点的角色，结点建立的时候进行
	 * 
	 * @param nodeId
	 */
	public void initNodeRole(Long nodeId);

	/**
	 * 重新初始化结点角色
	 * 
	 * @param nodeId
	 */
	public void reinitNodeRole(Long nodeId);
	
	public IRoleManager getRoleManager() ;
}
