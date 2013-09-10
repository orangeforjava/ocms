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
package org.openuap.cms.core.action;

import org.openuap.base.web.mvc.BaseFormController;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.IUserSession;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.AuthFactory;

/**
 * <p>
 * 带有用户信息的表单控制器
 * </p>
 * <p>
 * $Id: UserAwareFormAction.java 4021 2011-03-22 14:48:53Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * 
 */
public class UserAwareFormAction extends BaseFormController {

	private IUserManager userManager;
	protected AuthFactory authService;

	public IUserManager getUserManager() {
		if (userManager == null) {
			userManager = SecurityUtil.getUserManager();
		}
		return userManager;
	}

	/**
	 * 是否已经登录
	 * 
	 * @return boolean
	 */
	public boolean isLogin() {
		return SecurityUtil.getUserSession().isLogin();
	}

	public IUser getUser() {
		return SecurityUtil.getUser();
	}

	public IUserSession getUserSession() {
		return SecurityUtil.getUserSession();
	}

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

	public AuthFactory getAuthService() {
		if (authService == null) {
			authService = SecurityUtil.getAuthService();
		}
		return authService;
	}

	public void setAuthService(AuthFactory authService) {
		this.authService = authService;
	}
}
