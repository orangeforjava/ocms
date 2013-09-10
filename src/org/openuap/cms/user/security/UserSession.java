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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openuap.base.util.json.JSONException;
import org.openuap.base.util.json.JSONObject;
import org.openuap.cms.user.model.BaseUser;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.PermissionObject;
import org.openuap.cms.user.model.Permissions;

/**
 * <p>
 * 会话用户对象
 * </p>
 * 
 * <p>
 * $Id: UserSession.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @ersion 1.0
 */
public class UserSession implements IUserSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8050949535572560924L;

	private boolean login;

	private String cleartext;

	private String ip;

	private IUser user;
	
	private List roles;
	
	private Map permissions;
	

	public Map getPermissions() {
		return permissions;
	}

	public void setPermissions(Map permissions) {
		this.permissions = permissions;
	}

	public void setRoles(List roles) {
		this.roles = roles;
	}

	public UserSession() {

	}

	public UserSession(IUser user) {
		this.user = user;
	}

	public String getCleartext() {
		return cleartext;
	}

	public IUserSession getDefaultUser() {
		//
		BaseUser user = new BaseUser();
		user.setId(0L);
		user.setName("anonymous-"+System.currentTimeMillis());
		user.setType(IUser.ANONYMOUS_TYPE);
		return new UserSession(user);
	}

	public String getIp() {
		return this.ip;
	}

	public boolean isLogin() {
		return !isAnonymous();
	}

	public void setCleartext(String cleartext) {
		this.cleartext = cleartext;

	}

	public void setIp(String ip) {
		this.ip = ip;

	}

	public void setLogin(boolean login) {
		this.login = login;

	}

	public String getUserID() {
		return this.user.getUserId().toString();
	}

	public boolean isAnonymous() {
		return this.user.getUserId() == 0;
	}

	public JSONObject toJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCreatedBy() {
		return this.user.getCreatedBy();
	}

	public Long getCreationDate() {
		return this.user.getCreationDate();
	}

	public String getEmail() {
		return this.user.getEmail();
	}

	public String getEncodeUserName() {
		return this.user.getEncodeUserName();
	}

	public String getGuid() {
		return this.user.getGuid();
	}

	public Long getLastLoginDate() {
		return this.user.getLastLoginDate();
	}

	public String getLastLoginIp() {
		return this.user.getLastLoginIp();
	}

	public Long getLoginTimes() {
		return this.user.getLoginTimes();
	}

	public String getMobile() {
		return this.user.getMobile();
	}

	public Long getModificationDate() {
		return this.user.getModificationDate();
	}

	public String getName() {
		return this.user.getName();
	}

	public String getNickName() {
		return this.user.getNickName();
	}

	public String getPassword() {
		return this.user.getPassword();
	}



	public void setLastLoginDate(Long lastLoginDate) {
		this.user.setLastLoginDate(lastLoginDate);

	}



	public boolean hasPermission(String objectType, String objectId,
			long permission) {
		if (isAdmin()) {
			return true;
		}
		
		if (permissions != null) {
			//对对象id为0的情况，如果有任何类型匹配的都检查其是否有权限，只要有一个有权限即可
			if(objectId.equals("0")){
				Iterator<PermissionObject> pokeys=permissions.keySet().iterator();
				while(pokeys.hasNext()){
					PermissionObject po=pokeys.next();
					if(objectType.equals(po.getObjectType())){
						//类型匹配即可
						String oid=po.getObjectId();
						Object o = permissions.get(po);
						if(o instanceof Permissions){
							Permissions p = (Permissions) o;
							if(p.hasPermission(objectType, oid, permission)){
								return true;
							}
						}
					}
				}
			}
			PermissionObject po = new PermissionObject(objectType, objectId);
			Object o = permissions.get(po);
			if (o == null) {
				if (!objectId.equals("-1")) {
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
	
	public boolean isAccountNonExpired() {
		return !isStatus(IUser.EXPIRED_STATUS);
	}

	public boolean isAccountNonLocked() {
		return !isStatus(IUser.LOCKED_STATUS);
	}

	public boolean isAdmin() {
		int type = user.getType();
		if ((type & IUser.ADMIN_TYPE) != 0) {
			return true;
		}
		return false;
	}

	public boolean isCredentialsNonExpired() {
		return !isStatus(IUser.CREDENTIALS_EXPIRED_STATUS);
	}

	public boolean isEnabled() {
		return !isStatus(IUser.DISABLE_STATUS);
	}

	public boolean isMemberUser() {
		int type = user.getType();
		if ((type & IUser.SYS_USER_TYPE) != 0) {
			return true;
		}
		return false;
	}

	public boolean isSysUser() {
		int type = user.getType();
		if ((type & IUser.SYS_USER_TYPE) != 0) {
			return true;
		}
		return false;
	}
	private boolean isStatus(int target) {
		int status = user.getUserStatus();
		if ((target & status) != 0) {
			return true;
		}
		return false;
	}

	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}
}
