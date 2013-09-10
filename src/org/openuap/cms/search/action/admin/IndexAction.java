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
package org.openuap.cms.search.action.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.web.mvc.BaseController;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.search.index.IndexEngine;
import org.openuap.cms.search.index.IndexParameter;
import org.openuap.cms.search.index.RSEMHelper;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 索引控制器.
 * </p>
 * 
 * <p>
 * $Id: IndexAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class IndexAction extends AdminAction {

	private IndexEngine indexEngine;

	private ContentTableManager contentTableManager;

	private RSEMHelper rsemHelper;

	private String rsViewName;

	private String defaultScreensPath;

	public IndexAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/search/";
		rsViewName = defaultScreensPath + "operation_result.html";
	}

	/**
	 * 更新RSEM定义
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doUpdateRSEM(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String tid = request.getParameter("tableId");
		// 引用地址
		String r = request.getParameter("r");
		ModelAndView mv = new ModelAndView(rsViewName);
		String rs = "success";
		// 批量模式
		String multi = helper.getString("multi", "0");
		String[] pData = request.getParameterValues("pData");
		if (multi.equals("1")) {
			model.put("op", "UpdateRSEMBatch");
			// 错误
			List errors = new ArrayList();
			if (pData != null) {
				for (int i = 0; i < pData.length; i++) {
					try {
						String table_id = pData[i];
						Long id = new Long(table_id);
						ContentTable ct = getContentTableManager()
								.getContentTableById(id);
						rsemHelper.updateRSEM(ct);
					} catch (Exception e) {
						errors.add(e);
					}
				}
			}
			if (errors.size() > 0) {
				// 有错误
				model.put("rs", "failed");
				model.put("msgs", errors);
			} else {
				model.put("rs", "success");
			}
		} else {
			model.put("op", "UpdateRSEM");

			if (tid != null) {
				try {
					Long id = new Long(tid);
					ContentTable ct = getContentTableManager()
							.getContentTableById(id);
					rsemHelper.updateRSEM(ct);

				} catch (Exception e) {
					rs = "failed";
					model.put("msgs", e);
					e.printStackTrace();
				}

			}
			model.put("rs", rs);
		}
		//
		model.put("r", r);
		return mv;
	}

	/**
	 * 更新索引
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doUpdateIndex(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		String tid = request.getParameter("tableId");
		String r = request.getParameter("r");
		ModelAndView mv = new ModelAndView(rsViewName);
		String rs = "success";
		// 批量模式
		String multi = helper.getString("multi", "0");
		String[] pData = request.getParameterValues("pData");
		if (multi.equals("1")) {
			model.put("op", "UpdateIndexBatch");
			// 错误
			List errors = new ArrayList();
			if (pData != null) {
				for (int i = 0; i < pData.length; i++) {
					String table_id = pData[i];
					IndexParameter indexParameter = new IndexParameter(0, table_id);
					indexEngine.addIndexContent(indexParameter, errors);
				}
			}
			if (errors.size() > 0) {
				rs = "failed";
				model.put("msgs", errors);
			}
		} else {
			model.put("op", "UpdateIndex");
			if (tid != null) {
				try {
					IndexParameter indexParameter = new IndexParameter(0, tid);
					List errors = new ArrayList();
					indexEngine.addIndexContent(indexParameter, errors);
					if (errors.size() > 0) {
						rs = "failed";
						model.put("msgs", errors);
					}
				} catch (Exception e) {
					rs = "failed";
					model.put("msgs", e);
					e.printStackTrace();
				}
			}
			
		}
		//
		model.put("rs", rs);
		model.put("r", r);
		return mv;
	}

	@Override
	public ModelAndView perform(HttpServletRequest arg0,
			HttpServletResponse arg1, ControllerHelper arg2, Map arg3)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the contentTableManager
	 */
	public ContentTableManager getContentTableManager() {
		// if (contentTableManager == null) {
		// contentTableManager = (ContentTableManager)
		// ObjectLocator.lookup("contentTableManager",
		// CmsPlugin.PLUGIN_ID);
		// }
		return contentTableManager;
	}

	/**
	 * @param contentTableManager
	 *            the contentTableManager to set
	 */
	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	/**
	 * @return the indexEngine
	 */
	public IndexEngine getIndexEngine() {
		return indexEngine;
	}

	/**
	 * @param indexEngine
	 *            the indexEngine to set
	 */
	public void setIndexEngine(IndexEngine indexEngine) {
		this.indexEngine = indexEngine;
	}

	/**
	 * @return the rsemHelper
	 */
	public RSEMHelper getRsemHelper() {
		return rsemHelper;
	}

	/**
	 * @param rsemHelper
	 *            the rsemHelper to set
	 */
	public void setRsemHelper(RSEMHelper rsemHelper) {
		this.rsemHelper = rsemHelper;
	}

}
