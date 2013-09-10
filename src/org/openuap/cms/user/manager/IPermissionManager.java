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
package org.openuap.cms.user.manager;

import java.util.List;
import java.util.Map;

import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.Permissions;

/**
 * <p>
 * 权限管理接口.
 * </p>
 * 
 * <p>
 * $Id: IPermissionManager.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface IPermissionManager {

	public Map getUserFinalPermissions(long userId);

	public Map getUserFinalPermissions(long userId, String objectType);

	public Map getUserFinalObjPermissions(long userId, String objectType);

	public Map getUserPermissions(long userId, String objectType);

	public Map getUserObjPermissions(long userId, String objectType);

	public Map getRolePermissions(long roleId);

	public Map getRolePermissions(long roleId, String objectType);

	public Map getRoleObjPermissions(long roleId, String objectType);

	public Map getUserPermissions(long userId);

	public Permissions getUserFinalPermissions(long userId, String objectType, String objectId);

	public Permissions getUserPermissions(long userId, String objectType, String objectId);

	public Permissions getRolePermissions(long roleId, String objectType, String objectId);

	public boolean hasPermission(long userId, String objectType, String objectId, long perm);

	public boolean hasAllObjectPermission(long userId, String objectType, long perm);

	public boolean hasAllPermission(long userId, long perm);

	public void setUserPermission(long userId, String objectType, String objectId, long permissions);

	public void removeUserPermission(long userId, String objectType, String objectId);

	public void setRolePermission(long roleId, String objectType, String objectId, long permissions);

	public void removeRolePermission(long roleId, String objectType, String objectId);

	public void removeUserAllPermission(long userId);

	public void removeRoleAllPermission(long roleId);
	
	/**
	 * 获得指定对象的所拥有的角色
	 * 
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public List<IRole> getObjRoles(String objectId, String objectType);

	/**
	 * 获得指定对象所拥有的用户
	 * 
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public List<IUser> getObjUsers(String objectId, String objectType);

}
