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
package org.openuap.cms.user.action.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.user.action.UserBaseAction;
import org.openuap.cms.user.model.IUser;
import org.openuap.passport.sso.AuthToken;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 用户登录状态控制器.
 * </p>
 * 
 * <p>
 * $Id: UserStatusAction.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class UserStatusAction extends UserBaseAction {

	// 保存用户登录信息的Cookie
	public static final String CAS_HASHED_REMEMBER_ME_COOKIE_KEY = "CAS_HASHED_REMEMBER_ME_COOKIE";
	// 临时用来登录的Cookie
	public static final String CAS_TEMP_LOGIN_COOKIE_KEY = "CAS_TEMP_LOGIN_COOKIE";
	//
	private String cookieName = CAS_HASHED_REMEMBER_ME_COOKIE_KEY;
	private String tempCookieName = CAS_TEMP_LOGIN_COOKIE_KEY;

	/**
	 * 
	 */
	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		return super.beforePerform(request, response, helper, model);

	}

	protected String getUserName(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		// AcegiAuthUser authUser = (AcegiAuthUser) model.get("authUser");
		// if (authUser != null) {
		// return authUser.getUsername();
		// }
		Cookie[] cookies = request.getCookies();
		//
		if ((cookies == null) || (cookies.length == 0)) {
			return null;
		}
		for (int i = 0; i < cookies.length; i++) {
			// 判断临时登录，一般从注册过来
			if (tempCookieName.equals(cookies[i].getName())) {
				String cookieValue = cookies[i].getValue();
				for (int j = 0; j < cookieValue.length() % 4; j++) {
					cookieValue = cookieValue + "=";
				}
				if (Base64.isArrayByteBase64(cookieValue.getBytes())) {
					String cookieAsPlainText = new String(Base64
							.decodeBase64(cookieValue.getBytes()));
					String[] cookieTokens = StringUtils
							.delimitedListToStringArray(cookieAsPlainText, ":");
					if (cookieTokens.length == 3) {
						return cookieTokens[0];
					}
				}
			}
			// 自动登录Cookie
			if (cookieName.equals(cookies[i].getName())) {
				String cookieValue = cookies[i].getValue();
				//
				for (int j = 0; j < cookieValue.length() % 4; j++) {
					cookieValue = cookieValue + "=";
				}

				if (Base64.isArrayByteBase64(cookieValue.getBytes())) {
					String cookieAsPlainText = new String(Base64
							.decodeBase64(cookieValue.getBytes()));
					String[] cookieTokens = StringUtils
							.delimitedListToStringArray(cookieAsPlainText, ":");

					if (cookieTokens.length == 3) {
						return cookieTokens[0];
					}
				}
			}

		}
		AuthToken authToken;
		authToken = this.getUserSession();
		if (authToken != null) {
			return authToken.getName();
		}
		return null;
	}

	public ModelAndView doCheckPassword(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException {
		String clientid=helper.getString("clientid");
		String value=helper.getString(clientid,"");
		ModelAndView mv=new ModelAndView("/content.htm");
		if(StringUtil.hasText(value)){
			IUser user=this.getUser();
			String pwd=user.getPassword();
			String pwd2=StringUtil.digest(value, "md5");
			if(pwd2.equals(pwd)){
				model.put("content","1");
			}else{
				model.put("content","0");
			}
		}else{
			model.put("content","0");
		}		
		return mv;
	}

	/**
	 * 提供JSON格式的用户信息，用户名，ip,realIp
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException {
		//
		String userName = getUserName(request, response, helper, model);
		// response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//
		String ip = helper.getRemoteAddr();
		String realIp = helper.getRealIP();
		PrintWriter writer = response.getWriter();

		if (userName != null && this.isLogin()) {
			// 用户已经登录
			StringBuffer rs = new StringBuffer();
			rs.append("{");
			rs.append("\"userName\":\"" + userName + "\",");
			rs.append("\"ip\":\"" + ip + "\",");
			rs.append("\"realIp\":\"" + realIp + "\"");
			rs.append("}");
			writer.print(rs);
		} else {
			// 直接解析Cookie
			StringBuffer rs = new StringBuffer();
			rs.append("{");
			rs.append("\"userName\":\"-1\",");
			rs.append("\"ip\":\"" + ip + "\",");
			rs.append("\"realIp\":\"" + realIp + "\"");
			rs.append("}");
			writer.print(rs);
		}
		writer.flush();
		writer.close();
		return null;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public void setTempCookieName(String tempCookieName) {
		this.tempCookieName = tempCookieName;
	}
}
