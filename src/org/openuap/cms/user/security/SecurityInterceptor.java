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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.user.config.UserConfig;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.support.AbstractDbAuthFactory;
import org.openuap.passport.sso.AuthFactory;
import org.openuap.passport.sso.AuthToken;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.passport.sso.context.SecurityContextHolder;
import org.openuap.passport.sso.context.SecurityContextImpl;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <p>
 * 安全拦截器
 * </p>
 * 
 * <p>
 * $Id: SecurityInterceptor.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 从Cookie中获取用户信息，这样以后应用就可以在SecurityContextHolder中获的用户认证对象
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		IUserSession userSession = this.getUserSession(request, response);
		IUserSession adminUserSession = this.getAdminUserSession(request, response);
		//
		SecurityContextImpl sc = new SecurityContextImpl();
		sc.setAuthToken(userSession);
		sc.setAdminAuthToken(adminUserSession);
		//
		SecurityContextHolder.setContext(sc);
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	
	protected IUser getUser(HttpServletRequest request,
			HttpServletResponse response) {
		return getUserSession(request,response).getUser();
	}

	/**
	 * 获得用户信息，通过Cookie进行验证， 缺省用户即匿名用户信息
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return User 如果认证失败则获得,
	 */
	protected IUserSession getUserSession(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		
		AuthToken authToken;
		Object obj = session.getAttribute(AuthFactory.SESSION_AUTHORIZATION);
		if (obj != null) {
			authToken = (AuthToken) obj;
			return (IUserSession) authToken;
		} else {
			try {
				String domain=UserConfig.getInstance().getPassportDomain();
				int age=-2;
				authToken = getAuthService().getAuthToken(request, response,domain,age);				
				return (IUserSession) authToken;
			} catch (UnauthorizedException ex) {
				IUserSession userSession =((AbstractDbAuthFactory)getAuthService()).createUserSession();
				IUserSession defaultUserSession = userSession.getDefaultUser();
				
				return defaultUserSession;

			}

		}
	}
	protected IUserSession getAdminUserSession(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		AuthToken authToken;
		Object obj = session.getAttribute(AuthFactory.SESSION_ADMIN_AUTHORIZATION);
		if (obj != null) {
			authToken = (AuthToken) obj;
			return (IUserSession) authToken;
		} else {
			try {
				String domain=UserConfig.getInstance().getPassportDomain();
				int age=-2;
				authToken = getAuthService().getAdminAuthToken(request, response,domain,age);				
				return (IUserSession) authToken;
			} catch (UnauthorizedException ex) {
				IUserSession userSession =((AbstractDbAuthFactory)getAuthService()).createUserSession();
				IUserSession defaultUserSession = userSession.getDefaultUser();
				
				return defaultUserSession;

			}

		}
	}
	/**
	 * 获得认证服务对象
	 * 具体应用可以重写此方法获得自己的认证服务对象
	 * @return
	 */
	protected AuthFactory getAuthService() {
		AuthFactory authService = (AuthFactory) ObjectLocator
				.lookup("authService", CmsPlugin.PLUGIN_ID);
		return authService;
	}
	/**
	 * 得到Cookie保存域
	 * @return
	 */
	protected String getDomain(){
		return UserConfig.getInstance().getPassportDomain();
	}
}
