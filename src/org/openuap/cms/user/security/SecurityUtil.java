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

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.IUser;
import org.openuap.passport.sso.AuthFactory;
import org.openuap.passport.sso.AuthToken;
import org.openuap.passport.sso.context.SecurityContextHolder;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 安全帮助类
 * </p>

 * <p>
 * 要使用此帮助方法必须使用Filter或者控制器拦截器在SecurityContextHolder设置信息
 * </p>
 * 
 * <p>
 * $Id: SecurityUtil.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SecurityUtil {
	/**
	 * 获得认证的用户
	 * 
	 * @return IUserSession
	 */
	public static IUserSession getUserSession() {
		AuthToken auth = SecurityContextHolder.getContext().getAuthToken();
		if (auth instanceof IUserSession) {
			return (IUserSession) auth;
		}
		return null;
	}

	public static IUserSession getAdminUserSession() {
		AuthToken auth = SecurityContextHolder.getContext().getAdminAuthToken();
		if (auth instanceof IUserSession) {
			return (IUserSession) auth;
		}
		return null;
	}

	/**
	 * 获得当前的用户
	 * 
	 * @return 当前登录用户
	 */
	public static IUser getUser() {
		AuthToken auth = SecurityContextHolder.getContext().getAuthToken();

		if (auth != null) {
			if (auth instanceof IUserSession) {
				return ((IUserSession) auth).getUser();
			} else {
				String name = auth.getName();
				IUserManager userManager = getUserManager();
				if (userManager != null) {
					return userManager.getUserByName(name);
				}
			}
		}
		return null;
	}

	/**
	 * 判断用户是否具有指定权限
	 * 
	 * @param objectType
	 * @param objectId
	 * @param permission
	 * @return
	 */
	public static boolean hasPermission(String objectType, String objectId,
			long permission) {
		boolean rs = false;
		//首先检查管理用户
		IUserSession au = getAdminUserSession();
		if (au != null) {
			rs = au.hasPermission(objectType, objectId, permission);
		}
		if (!rs) {
			IUserSession au1 = getUserSession();
			rs = au1.hasPermission(objectType, objectId, permission);
		}
		return rs;
	}

	/**
	 * 判断用户是否具有指定权限
	 * 
	 * @param objectType
	 * 
	 * @param objectId
	 * 
	 * @param permission，多个权限之间用","分割
	 * 
	 * @return boolean
	 */
	public static boolean hasPermission(String objectType, String objectId,
			String permission) {
		if (permission == null || permission.equals("")) {
			return false;
		}
		String[] pstrAry = permission.split(",");

		long perm = 0L;
		for (int i = 0; i < pstrAry.length; i++) {
			String pstr = pstrAry[i];
			long lp = Long.parseLong(pstr);
			perm |= lp;
		}
		return hasPermission(objectType, objectId, perm);
	}
	public static boolean hasOnePermission(String objectType, String objectId,
			String permission) {
		if (permission == null || permission.equals("")) {
			return false;
		}
		String[] pstrAry = permission.split(",");

		long perm = 0L;
		for (int i = 0; i < pstrAry.length; i++) {
			String pstr = pstrAry[i];
			long lp = Long.parseLong(pstr);
			if(hasPermission(objectType, objectId, perm)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 依据BeanId获得用户管理器对象
	 * 
	 * @return
	 */
	public static IUserManager getUserManager() {
		IUserManager baseUserManager = (IUserManager) ObjectLocator.lookup(
				"baseUserManager", CmsPlugin.PLUGIN_ID);
		return baseUserManager;
	}

	/**
	 * 检查当前用户是否是匿名用户
	 * 
	 * @return
	 */
	public static boolean isAnonymous() {
		AuthToken auth = SecurityContextHolder.getContext().getAuthToken();
		if (auth != null) {
			return auth.isAnonymous();
		}
		return false;
	}

	public static boolean isAdminAnonymous() {
		AuthToken auth = SecurityContextHolder.getContext().getAdminAuthToken();
		if (auth != null) {
			return auth.isAnonymous();
		}
		return false;
	}

	/**
	 * 获得认证服务对象
	 * 
	 * @return
	 */
	public static AuthFactory getAuthService() {
		AuthFactory authService = (AuthFactory) ObjectLocator.lookup(
				"authService", CmsPlugin.PLUGIN_ID);
		return authService;
	}
}
