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
package org.openuap.cms.user.dao;

import java.util.List;
import java.util.Map;

import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.Permissions;

/**
 * <p>
 * 权限Dao接口.
 * </p>
 * 
 * <p>
 * $Id: PermissionDao.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface PermissionDao {
	/**
	 * 获得用户最终权限集合
	 * 
	 * @param userId
	 *            用户Id
	 * @return
	 */
	public Map getUserFinalPermissions(Long userId);

	/**
	 * 获得指定用户对指定资源类型的最终操作权限
	 * 
	 * @param userId
	 *            用户Id
	 * @param objectType
	 *            资源类型
	 * @return
	 */
	public Map getUserFinalPermissions(Long userId, String objectType);

	/**
	 * 获得指定用户的对指定资源类新的具体权限
	 * 
	 * @param userId
	 *            用户id
	 * @param objectType
	 *            对象类型
	 * @return
	 */
	public Map getUserFinalObjPermissions(Long userId, String objectType);

	/**
	 * 
	 * @param userId
	 * @param objectType
	 * @return
	 */
	public Map getUserPermissions(Long userId, String objectType);

	/**
	 * 
	 * @param userId
	 * @param objectType
	 * @return
	 */
	public Map getUserObjPermissions(Long userId, String objectType);

	public Map getRolePermissions(Long roleId);

	public Map getRolePermissions(Long roleId, String objectType);

	public Map getRoleObjPermissions(Long roleId, String objectType);

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

	public Map getUserPermissions(Long userId);

	public Permissions getUserFinalPermissions(Long userId, String objectType,
			String objectId);

	public Permissions getUserPermissions(Long userId, String objectType,
			String objectId);

	public Permissions getRolePermissions(Long roleId, String objectType,
			String objectId);

	public boolean hasPermission(Long userId, String objectType,
			String objectId, long perm);

	public void setUserPermission(Long userId, String objectType,
			String objectId, long permissions);

	public void removeUserPermission(Long userId, String objectType,
			String objectId);

	public void setRolePermission(Long roleId, String objectType,
			String objectId, long permissions);

	public void removeRolePermission(Long roleId, String objectType,
			String objectId);

	public void removeUserAllPermission(Long userId);

	public void removeRoleAllPermission(Long roleId);

}
