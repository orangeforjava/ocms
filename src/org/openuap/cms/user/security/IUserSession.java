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

import java.util.Map;

import org.openuap.base.util.json.JSONable;
import org.openuap.cms.user.model.IUser;
import org.openuap.passport.sso.AuthToken;

/**
 * <p>
 * 用户会话对象接口
 * </p>
 * 
 * <p>
 * $Id: IUserSession.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface IUserSession extends AuthToken, JSONable {
	/**
	 * 获得用户对象
	 * @return
	 */
	public IUser getUser();
	/**
	 * 设置用户对象
	 * @param user
	 */
	public void setUser(IUser user);

	public void setLogin(boolean login);
	/**
	 * 用户是否已经登录
	 * @return
	 */
	public boolean isLogin();


	/**
	 * 获得缺省的会员对象
	 * 
	 * @return
	 */
	public IUserSession getDefaultUser();
	/**
	 * 用户帐号是否非过期
	 * @return
	 */
	public boolean isAccountNonExpired();
	/**
	 * 帐号是否非被锁定
	 * @return
	 */
	public boolean isAccountNonLocked();
	/**
	 * 密码是否非过期
	 * @return
	 */
	public boolean isCredentialsNonExpired();
	/**
	 * 帐号是否可用
	 * @return
	 */
	public boolean isEnabled();
	/**
	 * 是否为管理员
	 * @return
	 */
	public boolean isAdmin();
	/**
	 * 是否为系统用户
	 * @return
	 */
	public boolean isSysUser();
	/**
	 * 是否为会员用户
	 * @return
	 */
	public boolean isMemberUser();
	/**
	 * 判断是否具有指定操作的权限
	 * @param objectType
	 * @param objectId
	 * @param permission
	 * @return
	 */
	public boolean hasPermission(String objectType, String objectId,
			long permission);
	
	
	public Map getPermissions();
	
	public void setPermissions(Map permissions);
	/**
	 * 设置用户的最后登录日期
	 * @param lastLoginDate
	 */
	public void setLastLoginDate(Long lastLoginDate);
	
	/**
	 * 获得用户最后登录日期
	 * 
	 * @return
	 */
	public Long getLastLoginDate();

}
