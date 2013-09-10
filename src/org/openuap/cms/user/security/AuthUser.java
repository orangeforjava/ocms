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
package org.openuap.cms.user.security;

import java.util.List;
import java.util.Map;

import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.PermissionObject;
import org.openuap.cms.user.model.Permissions;
import org.openuap.passport.sso.AuthToken;

/**
 * <p>
 * 认证用户类.
 * </p>
 * 
 * 
 * <p>
 * $Id: AuthUser.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class AuthUser implements AuthToken {

	private List roles;

	private Map permissions;

	private IUser iUser;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1701727809516955618L;

	public AuthUser(IUser user) {
		this.iUser = user;
	}

	public AuthUser(IUser user, Map permissions) {
		this.iUser = user;
		this.permissions = permissions;
	}

	public boolean isAccountNonExpired() {
		return !isStatus(IUser.EXPIRED_STATUS);
	}

	public boolean isAccountNonLocked() {

		return !isStatus(IUser.LOCKED_STATUS);
	}

	public IUser getUser() {
		return iUser;
	}

	public boolean isCredentialsNonExpired() {
		return !isStatus(IUser.CREDENTIALS_EXPIRED_STATUS);
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean isEnabled() {
		return !isStatus(IUser.DISABLE_STATUS);
	}

	public String getUsername() {
		return iUser.getName();
	}

	private boolean isStatus(int target) {
		int status = iUser.getUserStatus();
		if ((target & status) != 0) {
			return true;
		}
		return false;
	}

	public String getPassword() {
		return iUser.getPassword();
	}

	/**
	 * 返回用户是否是管理员
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		int type = iUser.getType();
		if ((type & IUser.ADMIN_TYPE) != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 返回是否是系统后台用户
	 * 
	 * @return
	 */
	public boolean isSysUser() {
		int type = iUser.getType();
		if ((type & IUser.SYS_USER_TYPE) != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 返回是否为会员用户
	 * 
	 * @return
	 */
	public boolean isMemberUser() {
		int type = iUser.getType();
		if ((type & IUser.SYS_USER_TYPE) != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 返回用户是否具备指定权限 管理员具备所有权限
	 * 
	 * @param objectType
	 *            对象类型
	 * 
	 * @param objectId
	 *            对象Id
	 * 
	 * @param permission
	 *            权限值
	 * 
	 * @return
	 */
	public boolean hasPermission(String objectType, String objectId,
			long permission) {
		if (isAdmin()) {
			return true;
		}
		if (permissions != null) {
			PermissionObject po = new PermissionObject(objectType, objectId);
			Object o = permissions.get(po);
			if (o == null) {
				if (!objectId.equals(new Integer(-1))) {
					o = permissions.get(new PermissionObject(objectType, "-1"));
				}
			}
			if (o != null && o instanceof Permissions) {
				Permissions p = (Permissions) o;
				return p.hasPermission(objectType, objectId, permission);
			}
		}
		return false;
	}

	public List getRoles() {
		return roles;
	}

	public void setRoles(List roles) {
		this.roles = roles;
	}

	public String getUserID() {
		return iUser.getUserId().toString();
	}

	public boolean isAnonymous() {
		return iUser.getUserId() == 0L;
	}

	public String getName() {
		return iUser.getName();
	}

	public String getCleartext() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCleartext(String cleartext) {
		// TODO Auto-generated method stub

	}

	public void setIp(String ip) {
		// TODO Auto-generated method stub

	}
}
