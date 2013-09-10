/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.user.security.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.DateUtil;
import org.openuap.cms.user.config.UserConfig;
import org.openuap.cms.user.manager.IPermissionManager;
import org.openuap.cms.user.manager.ISecurityUserManager;
import org.openuap.cms.user.security.IUserSession;
import org.openuap.passport.sso.AuthFactory;
import org.openuap.passport.sso.AuthToken;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.passport.sso.context.SecurityContextHolder;
import org.openuap.passport.sso.support.PassportCookieParser;
import org.openuap.passport.sso.support.SimpleCookieAuthFactory;
import org.openuap.passport.sso.support.StringUtil;
import org.openuap.util.Strings;

/**
 * <p>
 * 抽象的基于数据库的认证工厂.
 * </p>
 *  
 * <p>
 * $Id: AbstractDbAuthFactory.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractDbAuthFactory extends SimpleCookieAuthFactory
		implements AuthFactory {
	/** 会员管理. */
	private ISecurityUserManager userManager;
	/** 权限管理. */
	private IPermissionManager permissionManager;

	public AbstractDbAuthFactory() {
		init();
	}

	public AbstractDbAuthFactory(String key, String encoding, boolean useJce) {
		StringUtil.init(key, encoding, useJce);
	}

	protected void init() {
		UserConfig uc = UserConfig.getInstance();
		String key = uc.getSecurityKey();
		String encoding = "UTF-8";
		boolean useJce = false;
		StringUtil.init(key, encoding, useJce);
	}

	public boolean logout(HttpServletRequest request,
			HttpServletResponse response, String domain) {
		SecurityContextHolder.getContext().setAuthToken(null);
		return super.logout(request, response, domain);
	}

	public AuthToken createAuthToken(String userName, String password)
			throws UnauthorizedException {
		return createAuthToken(userName, password, "0", null);
	}

	/**
	 * 根据用户名密码获得认证令牌
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @return AuthToken
	 * @throws UnauthorizedException
	 */
	public AuthToken createAuthToken(String userName, String password,
			String method, String salt) throws UnauthorizedException {
		//
		IUserSession session = null;
		//
		try {
			// 获得Session对象，必须提供的方法
			session = getUserManager().getUserSessionByUserName(userName);
			//

			if (session == null) {
				// 未发现用户
				throw new UnauthorizedException("noexits.user");
			}
			// 校验密码
			if (!verifyPassword(password, session.getPassword(), method, salt)) {
				throw new UnauthorizedException("invalid.password");
			}
			// 检查帐号相关状态
			if (!session.isAccountNonExpired()) {
				throw new UnauthorizedException("account.expired");
			}
			if (!session.isAccountNonLocked()) {
				throw new UnauthorizedException("account.locked");
			}
			if (!session.isCredentialsNonExpired()) {
				throw new UnauthorizedException("credentials.expired");
			}
			if (!session.isEnabled()) {
				throw new UnauthorizedException("account.disabled");
			}
			// 放置权限信息，可以为空
			IPermissionManager pm = getPermissionManager();
			if (pm != null) {
				// 获得用户的权限
				Map permissions = getPermissionManager()
						.getUserFinalPermissions(session.getUser().getUserId());
				session.setPermissions(permissions);
			}
			// 设置原始密码
			session.setCleartext(password);
			return session;
		} catch (Exception ex) {
			throw new UnauthorizedException(ex.getMessage(), ex);
		}
	}

	/**
	 * 根据用户名密码获得认证令牌
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @return AuthToken
	 * @throws UnauthorizedException
	 */
	public AuthToken createAdminAuthToken(String username, String password)
			throws UnauthorizedException {
		//
		IUserSession session = null;
		//
		try {
			session = getUserManager().getUserSessionByUserName(username);
			if (session == null) {
				// 未发现用户
				throw new UnauthorizedException("noexits.user");
			}
			// 校验密码
			if (!verifyPassword(password, session.getPassword(), "0", null)) {
				throw new UnauthorizedException("invalid.password");
			}
			// 检查帐号相关状态
			if (!session.isAccountNonExpired()) {
				throw new UnauthorizedException("account.expired");
			}
			if (!session.isAccountNonLocked()) {
				throw new UnauthorizedException("account.locked");
			}
			if (!session.isCredentialsNonExpired()) {
				throw new UnauthorizedException("credentials.expired");
			}
			if (!session.isEnabled()) {
				throw new UnauthorizedException("account.disabled");
			}
			if (!session.isSysUser() && !session.isAdmin()) {
				// 非管理员，非系统人员
				throw new UnauthorizedException("account.nopermission");
			}
			// 放置权限信息，可以为空
			IPermissionManager pm = getPermissionManager();
			if (pm != null) {
				// 获得用户的权限
				Map permissions = getPermissionManager()
						.getUserFinalPermissions(session.getUser().getUserId());
				session.setPermissions(permissions);
			}
			//
			session.setCleartext(password);
			//
			return session;
		} catch (Exception ex) {
			throw new UnauthorizedException(ex.getMessage(), ex);
		}
	}

	protected void putUserCache(IUserSession session) {

	}

	public void setUserManager(ISecurityUserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * 获得Member管理器
	 * 
	 * @return the memberManager
	 */
	public ISecurityUserManager getUserManager() {
		return userManager;
	}

	/**
	 * 校验密码
	 * 
	 * @param password
	 * 
	 * @param dbpwd
	 *            数据库中保存的密码，都是
	 * 
	 * @return boolean
	 */
	protected boolean verifyPassword(String password, String dbpwd,
			String method, String salt) {
		if ("0".equals(method)) {
			return md5(password).equals(dbpwd);
		} else if ("1".equals(method)) {
			return password.equals(dbpwd);
		} else if ("2".equals(method)) {
			return password.equals(md5(md5(dbpwd) + salt));
		}
		return md5(password).equals(dbpwd);
	}

	/**
	 * 得到编码过的密码
	 * 
	 * @param password
	 *            String
	 * @return String
	 */
	protected String md5(String password) {
		return StringUtil.digest(password, "MD5");
	}

	public String getPassportDomain() {
		UserConfig uc = UserConfig.getInstance();
		return uc.getPassportDomain();
	}

	/**
	 * 子类必须提供获得IUserSession对象的方法
	 * 
	 * @return
	 */
	public abstract IUserSession createUserSession();

	public String getLoginUrl() {

		String loginUrl = null;
		if (loginUrl == null) {
			loginUrl = "user/login.jhtml";
		}
		return loginUrl;
	}

	public String getLogoutUrl() {
		String logoutUrl = null;
		if (logoutUrl == null) {
			logoutUrl = "user/logout.jhtml";
		}
		return logoutUrl;
	}

	public IPermissionManager getPermissionManager() {
		return permissionManager;
	}

	public void setPermissionManager(IPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

	public void postLogin(PassportCookieParser passportCookieParser,
			AuthToken authToken, String domain, int age) {
		// 获得最后阅读日期

		int last = Strings.asInteger(passportCookieParser
				.getTemporaryData(COOKIE_LAST_READ_TIME), 0);
		// System.out.println("last="+last);
		if (last != 0) {
			((IUserSession) authToken).setLastLoginDate(new Long(DateUtil
					.getDate(last).getTime()));
		} else {
			String userName = authToken.getName();
			// System.out.println("userName="+userName);
			Long last_read_time = userManager
					.getUserSessionByUserName(userName).getLastLoginDate();
			if (last_read_time != null) {
				int lrt = (int) (last_read_time / 1000);
				//
				((IUserSession) authToken).setLastLoginDate(new Long(DateUtil
						.getDate(lrt).getTime()));
				//
				passportCookieParser.addTemporaryData(COOKIE_LAST_READ_TIME,
						String.valueOf(lrt)).addTemporaryData(
						COOKIE_UPDATE_TIME,
						String.valueOf(DateUtil.unixTimeStamp()))
						.setTemporaryCookie(domain);

			}
			// 更新用户登录信息
			userManager.updateLoginInfo(authToken.getUserID(),
					passportCookieParser.getRealIP(), System
							.currentTimeMillis());
		}
	}
}
