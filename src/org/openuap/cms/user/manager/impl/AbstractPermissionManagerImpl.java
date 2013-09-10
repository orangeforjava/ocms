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
package org.openuap.cms.user.manager.impl;

import java.util.List;
import java.util.Map;

import org.openuap.cms.user.dao.PermissionDao;
import org.openuap.cms.user.dao.UserDao;
import org.openuap.cms.user.manager.IPermissionManager;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.Permissions;

/**
 * <p>
 * 抽象权限管理类.
 * </p>
 *
 * <p>
 * $Id: AbstractPermissionManagerImpl.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractPermissionManagerImpl implements
		IPermissionManager {

	private PermissionDao permissionDao;

	private UserDao userDao;

	/**
	 * 
	 */
	public AbstractPermissionManagerImpl() {
	}

	public Map getUserFinalPermissions(long userId) {
		return this.permissionDao.getUserFinalPermissions(userId);
	}

	public Map getUserFinalPermissions(long userId, String objectType) {
		return this.permissionDao.getUserFinalPermissions(userId, objectType);
	}

	public Map getRolePermissions(long roleId) {
		return permissionDao.getRolePermissions(roleId);
	}

	public Map getUserPermissions(long userId) {
		return permissionDao.getUserPermissions(userId);
	}

	public Permissions getUserFinalPermissions(long userId, String objectType,
			String objectId) {
		return permissionDao.getUserFinalPermissions(userId, objectType,
				objectId);
	}

	public Permissions getRolePermissions(long roleId, String objectType,
			String objectId) {
		return permissionDao.getRolePermissions(roleId, objectType, objectId);
	}

	public boolean hasPermission(long userId, String objectType,
			String objectId, long perm) {
		IUser user = userDao.getUserById(userId);
		if (user == null) {
			return false;
		} else {
			if (user.getType() == IUser.ADMIN_TYPE) {
				return true;
			}
		}
		return permissionDao.hasPermission(userId, objectType, objectId, perm);
	}

	public void setUserPermission(long userId, String objectType,
			String objectId, long permissions) {
		permissionDao.setUserPermission(userId, objectType, objectId,
				permissions);
	}

	public void removeUserPermission(long userId, String objectType,
			String objectId) {
		permissionDao.removeUserPermission(userId, objectType, objectId);
	}

	public void setRolePermission(long roleId, String objectType,
			String objectId, long permissions) {
		permissionDao.setRolePermission(roleId, objectType, objectId,
				permissions);
	}

	public void removeRolePermission(long roleId, String objectType,
			String objectId) {
		permissionDao.removeRolePermission(roleId, objectType, objectId);
	}

	public void removeUserAllPermission(long userId) {
		permissionDao.removeUserAllPermission(userId);
	}

	public void removeRoleAllPermission(long roleId) {
		permissionDao.removeRoleAllPermission(roleId);
	}

	public void setPermissionDao(PermissionDao dao) {
		this.permissionDao = dao;
	}

	public void setUserDao(UserDao dao) {
		this.userDao = dao;
	}

	public boolean hasAllObjectPermission(long userId, String objectType,
			long perm) {

		return this.hasPermission(userId, objectType, "-1", perm);
	}

	public boolean hasAllPermission(long userId, long perm) {
		return this.hasPermission(userId, "-1", "-1", perm);
	}

	public Map getUserPermissions(long userId, String objectType) {
		return permissionDao.getUserPermissions(userId, objectType);
	}

	public Permissions getUserPermissions(long userId, String objectType,
			String objectId) {
		return permissionDao.getUserPermissions(userId, objectType, objectId);
	}

	public Map getRolePermissions(long roleId, String objectType) {
		return permissionDao.getRolePermissions(roleId, objectType);
	}

	public Map getUserObjPermissions(long userId, String objectType) {
		return permissionDao.getUserObjPermissions(userId, objectType);
	}

	public Map getUserFinalObjPermissions(long userId, String objectType) {
		return permissionDao.getUserFinalObjPermissions(userId, objectType);
	}

	public Map getRoleObjPermissions(long roleId, String objectType) {
		return permissionDao.getRoleObjPermissions(roleId, objectType);
	}

	public PermissionDao getPermissionDao() {
		return permissionDao;
	}

	/**
	 * 获得指定对象的所拥有的角色
	 * 
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public List<IRole> getObjRoles(String objectId, String objectType) {
		return permissionDao.getObjRoles(objectId, objectType);
	}

	/**
	 * 获得指定对象所拥有的用户
	 * 
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public List<IUser> getObjUsers(String objectId, String objectType) {
		return permissionDao.getObjUsers(objectId, objectType);
	}

	public UserDao getUserDao() {
		return userDao;
	}

}
