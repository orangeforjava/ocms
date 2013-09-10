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
package org.openuap.cms.cm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.cm.event.ContentModelEvent;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.cm.util.ContentModelHelper;
import org.openuap.cms.cm.util.DynamicContentHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 内容模型管理控制器.
 * </p>
 * 
 * <p>
 * $Id: ContentTableAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */

public class ContentTableAction extends AdminAction {

	private ContentModelHelper contentModelHelper;

	//
	private String defaultViewName;

	private String defaultScreensPath;

	//
	private ContentTableManager contentTableManager;

	private String rsViewName;

	/**
	 * 
	 */
	public ContentTableAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/cm/";
		defaultViewName = defaultScreensPath + "content_table.html";
		rsViewName = defaultScreensPath + "conent_operation_result.html";
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
	 * it will show the content table list.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return ModelAndView
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		// 获得所有内容模型信息
		List<ContentTable> tables = contentTableManager.getAllContentTable();
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		String messageCode = request.getParameter("messageCode");
		if (messageCode != null) {
			model.put("messageCode", messageCode);
		}
		model.put("tables", tables);
		return mv;
	}

	/**
	 * 更新表定义
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doUpdateSchema(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		ModelAndView mv = new ModelAndView(rsViewName);
		// 单条模式
		String tid = request.getParameter("tableId");
		// 批量模式
		String multi = helper.getString("multi", "0");
		String[] pData = request.getParameterValues("pData");
		if (multi.equals("1")) {
			model.put("op", "updateSchemaBatch");
			// 错误
			List errors = new ArrayList();
			if (pData != null) {
				for (int i = 0; i < pData.length; i++) {
					String table_id = pData[i];
					Long id = new Long(table_id);
					try {
						ContentTable ct = contentTableManager
								.getContentTableById(id);
						contentModelHelper.updateContentModel(ct, true, true);
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
			model.put("op", "updateSchema");
			try {
				if (tid != null) {
					Long id = new Long(tid);
					ContentTable ct = contentTableManager
							.getContentTableById(id);
					contentModelHelper.updateContentModel(ct, true, true);
					model.put("rs", "success");
				}
			} catch (Exception e) {
				model.put("rs", "failed");
				model.put("msgs", e);
			}
		}
		return mv;
	}

	/**
	 * 新建内容模型
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doAdd(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String tname = request.getParameter("tname");
		String tbname = request.getParameter("tbname");
		String ename = request.getParameter("ename");
		String system = request.getParameter("system");
		if (tname != null) {
			ContentTable ct = new ContentTable();
			ct.setName(tname);
			ct.setDsnid(new Long(0));
			// ct.setSystem(new Integer("0"));
			if (tbname == null || tbname.equals("")) {
				ct.setTableName("");
			} else {
				ct.setTableName(tbname);
			}
			if (ename != null) {
				ct.setEntityName(ename);
				ct.setEntityPublishName(ename + "Publish");
			}
			if (system != null) {
				ct.setSystem(new Integer(1));
			} else {
				ct.setSystem(new Integer(0));
			}
			Long tableId = this.contentTableManager.addContentTable(ct);
			// if a new content model created,it need you create some relatiion
			// table
			// this thing maybe process by event
			// or write some code here
			// now i want to process it here,another way will test later
			ct = this.contentTableManager.getContentTableById(tableId);
			contentModelHelper.updateContentModel(ct, true, true);
			//
			helper.sendRedirect(helper.getBaseURL()
					+ "admin/contentEdit.jhtml?tableId=" + tableId);
			// helper.forwardRequest(helper.getBaseURL()+"admin/contentEdit.html?talbeId="
			// + tableId);
			return null;
		} else {
			this.errorPage(request, response, helper,
					"add_content_table_failed.", model);
		}
		return null;
	}

	/**
	 * 删除内容模型
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doDelete(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String tid = request.getParameter("tableId");
		if (tid != null) {
			Long id = new Long(tid);
			ContentTable ct = contentTableManager.getContentTableById(id);
			this.contentTableManager.deleteContentTable(id);
			//
			contentModelHelper.dropContentModel(ct, true, true);
			//
			helper.sendRedirect(helper.getBaseURL()
					+ "admin/content.jhtml?messageCode="
					+ "delete_content_table_success");
			// helper.forwardRequest();
		} else {
			this.errorPage(request, response, helper,
					"add_content_table_failed.", model);
		}
		return null;
	}

	/**
	 * 装载内容模型
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doLoadSystemModel(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		boolean success = contentTableManager.initData();
		List tables = contentTableManager.getAllContentTable();
		model.put("success", new Boolean(success));
		model.put("tables", tables);
		return mv;
	}

	/**
	 * 导出内容模型
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
	public ModelAndView doExportModel(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		String tid = request.getParameter("tableId");
		if (tid != null) {
			Long id = new Long(tid);
			boolean success = contentTableManager.exportModel(id);
			List tables = contentTableManager.getAllContentTable();
			model.put("success", new Boolean(success));
			model.put("tables", tables);
		}
		return mv;
	}

	/**
	 * 设置内容模型的索引属性，因为新CMS系统如果内容模型选择了允许发布调用，就必须支持索引
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doSetIndexProp(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(rsViewName);
		// 单条模式
		String tid = request.getParameter("tableId");
		// 批量模式
		String multi = helper.getString("multi", "0");
		String[] pData = request.getParameterValues("pData");
		if (multi.equals("1")) {
			model.put("op", "SetIndexPropBatch");
			// 错误
			List errors = new ArrayList();
			if (pData != null) {
				for (int i = 0; i < pData.length; i++) {
					String table_id = pData[i];
					Long id = new Long(table_id);
					try {
						ContentTable ct = contentTableManager
								.getContentTableById(id);
						contentTableManager.setIndexProp(ct);
						ContentModelEvent event = new ContentModelEvent(
								ContentModelEvent.CM_UPDATED, ct,
								new HashMap(), this);
						WebPluginManagerUtils.dispatcherEvent(false, "base",
								event);
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
			model.put("op", "SetIndexProp");
			try {
				if (tid != null) {
					Long id = new Long(tid);
					ContentTable ct = contentTableManager
							.getContentTableById(id);
					contentTableManager.setIndexProp(ct);
					ContentModelEvent event = new ContentModelEvent(
							ContentModelEvent.CM_UPDATED, ct, new HashMap(),
							this);
					WebPluginManagerUtils.dispatcherEvent(false, "base", event);
				}
				model.put("rs", "success");
			} catch (Exception e) {
				model.put("rs", "failed");
				model.put("msgs", e);
			}
		}

		return mv;
	}

	public ModelAndView doGetContentCount(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		long tid = helper.getLong("tid", -1);
		if (tid != -1) {
			Long nums = DynamicContentHelper.getContentCount(tid);
			return this.sendContent(request, response, helper, model, nums
					.toString());
		}
		return null;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setContentModelHelper(ContentModelHelper contentModelHelper) {
		this.contentModelHelper = contentModelHelper;
	}
}
