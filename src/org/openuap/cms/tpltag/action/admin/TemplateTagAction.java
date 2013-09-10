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
package org.openuap.cms.tpltag.action.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.tpltag.manager.TemplateTagManager;
import org.openuap.cms.tpltag.model.TemplateTag;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 模板标记管理控制器
 * </p>
 * 
 * <p>
 * $Id: TemplateTagAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateTagAction extends AdminAction {

	private String defaultScreensPath;

	private String defaultViewName;

	private String rsViewName;

	private TemplateTagManager templateTagManager;

	public TemplateTagAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/tpltag/admin/";
		defaultViewName = defaultScreensPath + "tpltag_list.html";
		rsViewName = defaultScreensPath + "tpltag_operation_result.html";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		return super.beforePerform(request, response, helper, model);
	}
	/**
	 * 显示模板标记列表
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		// 获得所有内容模型信息
		List<TemplateTag> tags = templateTagManager.getAllTags();
		ModelAndView mv = new ModelAndView(defaultViewName, model);

		model.put("tags", tags);
		return mv;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setRsViewName(String rsViewName) {
		this.rsViewName = rsViewName;
	}

	public void setTemplateTagManager(TemplateTagManager templateTagManager) {
		this.templateTagManager = templateTagManager;
	}

}
