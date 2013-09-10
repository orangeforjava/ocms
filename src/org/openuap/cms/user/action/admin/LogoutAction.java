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
package org.openuap.cms.user.action.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.user.config.UserConfig;
import org.openuap.cms.user.security.SecurityUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 后台注销控制器
 * </p>
 * 
 * <p>
 * $Id: LogoutAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class LogoutAction extends CMSBaseAction {
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		// 注销成功后返回地址
		String done = helper.getString("done", helper.getBaseURL());

		UserConfig userConfig = UserConfig.getInstance();
		// TODO 销毁掉CMS自己的Session数据
		// 注销通行证
		SecurityUtil.getAuthService().logoutAdmin(request, response,
				userConfig.getPassportDomain());
		helper.sendRedirect(done);
		return null;
	}
}
