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
package org.openuap.cms.keywords.action.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.orm.EntityException;
import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.keywords.cache.KeywordsCache;
import org.openuap.cms.keywords.manager.KeywordsManager;
import org.openuap.cms.keywords.model.Keywords;
import org.openuap.cms.keywords.security.KeywordsPermissionConstant;
import org.openuap.cms.node.security.NodePermissionConstant;
import org.openuap.cms.node.security.NodeSecurityUtil;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.util.ui.ColorType;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 关键词控制器.
 * </p>
 * 
 * <p>
 * $Id: KeywordsAction.java 4034 2011-03-22 17:58:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class KeywordsAction extends AdminAction {

	private String defaultScreensPath;
	private String indexViewName;
	private String editViewName;

	private KeywordsManager manager;

	public KeywordsAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/keywords/";
		indexViewName = defaultScreensPath + "index.html";
		editViewName = defaultScreensPath + "edit.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(
				KeywordsPermissionConstant.OBJECT_TYPE, "-1",
				KeywordsPermissionConstant.ViewKeywords)) {
			throw new UnauthorizedException();
		}
		PageBuilder pb = new PageBuilder();
		QueryInfo qi = new QueryInfo();
		//
		beforeProcessExtendInfo(request, response, helper, model, pb, qi);
		model.put("keywordses", manager.getKeywords(qi, pb));
		afterProcessExtendInfo(request, response, helper, model, pb, qi);
		return new ModelAndView(indexViewName, model);
	}

	public ModelAndView doAdd(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(
				KeywordsPermissionConstant.OBJECT_TYPE, "-1",
				KeywordsPermissionConstant.AddKeywords)) {
			throw new UnauthorizedException();
		}
		helper.setAction("Edit");
		helper.set("id", 0);
		helper.sendRedirect();
		return null;
	}

	public ModelAndView doDel(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(
				KeywordsPermissionConstant.OBJECT_TYPE, "-1",
				KeywordsPermissionConstant.DeleteKeywords)) {
			throw new UnauthorizedException();
		}
		int id = helper.getInt("id", 0);
		int[] ids = helper.getInts("ids");
		if (id != 0) {
			try {
				manager.deleteKeywordsById(id);
				KeywordsCache.remove();
				this.sendContent(request, response, helper, model, "1");
			} catch (Exception e) {
				this.sendContent(request, response, helper, model, e
						.getMessage());
			}
		} else if (ids != null) {
			for (int iid : ids) {
				try {
					manager.deleteKeywordsById(iid);
					KeywordsCache.remove();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return this.successPage(request, response, helper,
					"del_keywords_success", model);
		}
		return this.sendContent(request, response, helper, model, "1");
	}

	/**
	 * 编辑敏感词
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doEdit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(
				KeywordsPermissionConstant.OBJECT_TYPE, "-1",
				KeywordsPermissionConstant.EditKeywords)) {
			throw new UnauthorizedException();
		}
		int id = helper.getInt("id", 0);
		Keywords keywords = null;
		if (id != 0) {
			keywords = manager.getKeywordsById(id);
		} else {
			keywords = new Keywords();
			keywords.setPos(999);
			keywords.setScope(0);
			keywords.setStatus(0);
			keywords.setNodeId(-1L);
			keywords.setNums(0);
			keywords.setCreateUser(this.getUser().getName());
			keywords.setCreatedDate(System.currentTimeMillis());
			//
		}
		if (keywords == null) {
			return errorPage(request, response, helper, "keywords_not_exist",
					model);
		}
		model.put("colorTypes", ColorType.DEFAULT_COLOR_TYPES);
		model.put("keywords", keywords);
		return new ModelAndView(editViewName, model);
	}

	/**
	 * 保存关键词
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doSave(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		int id = helper.getInt("id", 0);
		Keywords keywords = null;
		if (id != 0) {
			keywords = manager.getKeywordsById(id);
		} else {
			keywords = new Keywords();
			keywords.setPos(999);
			keywords.setScope(0);
			keywords.setStatus(0);
			keywords.setNodeId(-1L);
			keywords.setCreateUser(this.getUser().getName());
			keywords.setCreatedDate(System.currentTimeMillis());
		}
		if (keywords == null) {
			return errorPage(request, response, helper, "keywords_not_exist",
					model);
		}
		helper.setProperty(keywords);
		try {
			manager.saveKeywords(keywords);
			KeywordsCache.remove();
			return perform(request, response, helper, model);
		} catch (EntityException ex) {
			return errorPage(request, response, helper, "unexcept_exception:"
					+ ex.getMessage(), model);
		}
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setIndexViewName(String indexViewName) {
		this.indexViewName = indexViewName;
	}

	public void setEditViewName(String editViewName) {
		this.editViewName = editViewName;
	}

	public void setManager(KeywordsManager manager) {
		this.manager = manager;
	}

}
