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
package org.openuap.cms.badwords.action.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.orm.EntityException;
import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.badwords.filter.BadwordsFilter;
import org.openuap.cms.badwords.manager.BadwordsManager;
import org.openuap.cms.badwords.model.Badwords;
import org.openuap.cms.core.action.AdminAction;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 敏感词管理控制器.
 * </p>
 *  
 * <p>
 * $Id: BadwordsAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class BadwordsAction extends AdminAction {
	
	private BadwordsManager badwordsManager;

	private String indexViewName;
	private String editViewName;
	private String testViewName;
	private String testRsViewName;
	private String defaultScreensPath;
	
	public BadwordsAction() {
		initDefaultViewName();
	}
	protected void initDefaultViewName(){
		defaultScreensPath = "/plugin/cms/base/screens/badwords/";
		editViewName=defaultScreensPath+"edit_badwords.html";
		indexViewName=defaultScreensPath+"index.html";
		testViewName=defaultScreensPath+"test_badwords.html";
		testRsViewName=defaultScreensPath+"test_badwords_result.html";
	}
	/**
	 * 查看敏感词
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView perform(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		PageBuilder pb = new PageBuilder();
		QueryInfo qi = new QueryInfo();
		//
		beforeProcessExtendInfo(request, response, helper, model, pb, qi);
		model.put("badwordses", badwordsManager.findAll());
		afterProcessExtendInfo(request, response, helper, model, pb, qi);
		return new ModelAndView(indexViewName, model);
	}

	public ModelAndView doAdd(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		helper.setAction("Edit");
		helper.set("id", 0);
		helper.sendRedirect();
		return null;
	}
	/**
	 * 编辑敏感词
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doEdit(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		int id = helper.getInt("id", 0);
		Badwords badwords = null;
		if (id != 0) {
			badwords = badwordsManager.getBadwordsById(id);
		} else {
			badwords = new Badwords();
			badwords.setPos(999);
			badwords.setScope(Badwords.SCOPE_ALL);
			badwords.setStatus(0);
			badwords.setType(Badwords.TYPE_HIGHLIGNT);
		}
		if (badwords == null) {
			return errorPage(request, response, helper, "badwords_not_exist", model);
		}

		model.put("badwords", badwords);
		return new ModelAndView(editViewName, model);
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doSave(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		int id = helper.getInt("id", 0);
		Badwords badwords = null;
		if (id != 0) {
			badwords = badwordsManager.getBadwordsById(id);
		} else {
			badwords = new Badwords();
			badwords.setPos(999);
			badwords.setScope(Badwords.SCOPE_ALL);
			badwords.setStatus(0);
			badwords.setType(Badwords.TYPE_HIGHLIGNT);
		}
		if (badwords == null) {
			return errorPage(request, response, helper, "badwords_not_exist", model);
		}
		helper.setProperty(badwords);
		try {
			badwordsManager.saveBadwords(badwords);
			return perform(request, response, helper, model);
		} catch (EntityException ex) {
			return errorPage(request, response, helper, "unexcept_exception:" + ex.getMessage(), model);
		}
	}

	public ModelAndView doDel(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		int id = helper.getInt("id", 0);
		badwordsManager.deleteBadwordsById(id);
		return perform(request, response, helper, model);
	}

	public ModelAndView doTest(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		int id = helper.getInt("id", 0);
		Badwords badwords = null;
		if (id != 0) {
			badwords = badwordsManager.getBadwordsById(id);
		}
		if (badwords == null) {
			return errorPage(request, response, helper, "badwords_not_exist", model);
		}

		model.put("badwords", badwords);
		return new ModelAndView(this.testViewName, model);
	}

	public ModelAndView doTestResult(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		int id = helper.getInt("id", 0);
		Badwords badwords = null;
		if (id != 0) {
			badwords = badwordsManager.getBadwordsById(id);
		}
		if (badwords == null) {
			return errorPage(request, response, helper, "badwords_not_exist", model);
		}
		helper.setProperty(badwords);
		String input = helper.getString("input", "");
		StringBuffer buf = new StringBuffer();
		long start = System.currentTimeMillis();
		int count = BadwordsFilter.doFilter(buf, input, badwords);
		long spare = System.currentTimeMillis() - start;
		model.put("spare", new Long(spare));
		model.put("badwords", badwords);
		model.put("input", input);
		model.put("output", buf.toString());
		model.put("count", new Integer(count));
		return new ModelAndView(testRsViewName, model);
	}

	public void setBadwordsManager(BadwordsManager badwordsManager) {
		this.badwordsManager = badwordsManager;
	}

	public void setIndexViewName(String indexViewName) {
		this.indexViewName = indexViewName;
	}
	public void setEditViewName(String editViewName) {
		this.editViewName = editViewName;
	}
	public void setTestViewName(String testViewName) {
		this.testViewName = testViewName;
	}
	public void setTestRsViewName(String testRsViewName) {
		this.testRsViewName = testRsViewName;
	}
	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

}
