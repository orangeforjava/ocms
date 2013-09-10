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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.cache.ContentModelCache;
import org.openuap.cms.cm.event.ContentModelEvent;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.cm.util.ContentModelHelper;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 内容表编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: ContentTableEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentTableEditAction extends AdminFormAction {
	//
	private ContentModelHelper contentModelHelper;

	private ContentTableManager contentTableManager;

	private ContentFieldManager contentFieldManager;

	private String defaultScreensPath;

	private String orderFieldViewName;

	private String rsViewName;

	public ContentTableEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/cm/";
		rsViewName = defaultScreensPath + "conent_operation_result.html";
		this.setFormView(defaultScreensPath + "content_table_edit.html");

		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(ContentTable.class);
		this.setCommandName("contentTable");
		//
		orderFieldViewName = defaultScreensPath + "content_order.html";
	}

	/**
	 * 提交修改
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		//
		ContentTable ct = (ContentTable) command;
		Long cid = null;
		if (ct.getTableId() == null || ct.getTableId().longValue() == 0L) {
			model.put("op", "add");
			try {
				ct.setDsnid(new Long(0));
				if (ct.getEntityName() != null) {
					ct.setEntityPublishName(ct.getEntityName() + "Publish");
				}
				cid = this.contentTableManager.addContentTable(ct);
				ct.setTableId(cid);
				contentModelHelper.updateContentModel(ct, true, true);
				ContentModelEvent event=new ContentModelEvent(ContentModelEvent.CM_UPDATED,ct,new HashMap(),this);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
				model.put("cid", cid);
				model.put("rs", "success");
			} catch (Exception e) {
				model.put("rs", "failed");
				model.put("msgs", e);
				e.printStackTrace();
			}

		} else {
			model.put("op", "edit");
			try {
				this.contentTableManager.saveContentTable(ct);
				cid = ct.getTableId();
				//
				processStatusUpdate(request, ct, "fieldListDisplay");
				processStatusUpdate(request, ct, "fieldSearchable");
				processStatusUpdate(request, ct, "enableContribution");
				processStatusUpdate(request, ct, "enableCollection");
				processStatusUpdate(request, ct, "enablePublish");
				processStatusUpdate(request, ct, "otherCategory");
				//
				ContentModelEvent event=new ContentModelEvent(ContentModelEvent.CM_UPDATED,ct,new HashMap(),this);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
				//
				model.put("rs", "success");
				model.put("cid", cid);
			} catch (Exception e) {
				model.put("rs", "failed");
				model.put("msgs", e);
				e.printStackTrace();
			}
		}
		//
		ModelAndView mv = new ModelAndView(rsViewName);
		//
		return mv;
	}
	
	private void processStatusUpdate(HttpServletRequest request,
			ContentTable ct, String statusName) {
		String[] statuses = request.getParameterValues(statusName);
		int f = 0, t = 1;
		contentFieldManager.updateStatusOfTable(ct.getTableId(), statusName,
				new Integer(f));
		if (statuses != null) {
			for (int i = 0; i < statuses.length; i++) {
				String cf_id = statuses[i];
				Long id = new Long(cf_id);
				contentFieldManager
						.updateStatus(id, statusName, new Integer(t));
			}

		}
	}

	protected Map referenceData(HttpServletRequest request) throws Exception {
		//
		Map model = new HashMap();
		String messageCode = request.getParameter("messageCode");
		if (messageCode != null) {
			model
					.put("messageCode", StringUtil.decodeURL(messageCode,
							"UTF-8"));
		}
		return model;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @return
	 * @throws
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String tableId = request.getParameter("tableId");
		ContentTable ct = null;
		if (tableId != null) {
			Long id = new Long(tableId);
			ct = contentTableManager.getContentTableById(id);
		} else {
			ct = new ContentTable();
		}
		return ct;
	}

	/**
	 * delete the field
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
	 * @throws
	 */
	public ModelAndView doDelField(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}

		//
		String tableId = request.getParameter("tableId");
		String contentFieldId = request.getParameter("contentFieldId");
		if (contentFieldId != null) {
			Long cf_id = new Long(contentFieldId);
			contentFieldManager.deleteContentField(cf_id);
			if (tableId != null) {
				Long tid = new Long(tableId);
				ContentTable ct = contentTableManager.getContentTableById(tid);
				contentModelHelper.updateContentModel(ct, true, true);
				ContentModelEvent event=new ContentModelEvent(ContentModelEvent.CM_UPDATED,ct,new HashMap(),this);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			}
		}
		if (tableId != null) {
			String messageCode = StringUtil.encodeURL(
					"content_table_field_delete_success", "UTF-8");
			helper.sendRedirect(helper.getBaseURL()
					+ "admin/contentEdit.jhtml?tableId=" + tableId
					+ "&messageCode=" + messageCode);

		}
		return null;
	}

	/**
	 * set the assign field to title
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
	 * @throws
	 */
	public ModelAndView doSetTitle(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}

		String tableId = request.getParameter("tableId");
		String contentFieldId = request.getParameter("contentFieldId");
		int f = 0, t = 1;
		if (tableId != null) {
			Long tid = new Long(tableId);
			contentFieldManager.updateStatusOfTable(tid, "titleField",
					new Integer(f));
			if (contentFieldId != null) {
				Long fid = new Long(contentFieldId);
				contentFieldManager.updateStatus(fid, "titleField",
						new Integer(t));
			}
			ContentModelEvent event=new ContentModelEvent(ContentModelEvent.CM_UPDATED,null,new HashMap(),this);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			String messageCode = StringUtil.encodeURL(
					"content_table_field_update_titlefield_success", "UTF-8");
			helper.sendRedirect(helper.getBaseURL()
					+ "admin/contentEdit.jhtml?tableId=" + tableId
					+ "&messageCode=" + messageCode);

		}

		return null;
	}

	/**
	 * set the assign field to mainfield
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
	 * @throws
	 */
	public ModelAndView doSetMain(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		String tableId = request.getParameter("tableId");
		String contentFieldId = request.getParameter("contentFieldId");
		int f = 0, t = 1;
		if (tableId != null) {
			Long tid = new Long(tableId);
			contentFieldManager.updateStatusOfTable(tid, "mainField",
					new Integer(f));
			if (contentFieldId != null) {
				Long fid = new Long(contentFieldId);
				contentFieldManager.updateStatus(fid, "mainField", new Integer(
						t));
				ContentModelEvent event=new ContentModelEvent(ContentModelEvent.CM_UPDATED,null,new HashMap(),this);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			}
			String messageCode = StringUtil.encodeURL(
					"content_table_field_update_mainfield_success", "UTF-8");
			helper.sendRedirect(helper.getBaseURL()
					+ "admin/contentEdit.jhtml?tableId=" + tableId
					+ "&messageCode=" + messageCode);

		}

		return null;
	}

	public ModelAndView doSetKeywords(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}

		String tableId = request.getParameter("tableId");
		String contentFieldId = request.getParameter("contentFieldId");
		int f = 0, t = 1;
		if (tableId != null) {
			Long tid = new Long(tableId);
			contentFieldManager.updateStatusOfTable(tid, "keywordsField",
					new Integer(f));
			if (contentFieldId != null) {
				Long fid = new Long(contentFieldId);
				contentFieldManager.updateStatus(fid, "keywordsField",
						new Integer(t));
				ContentModelEvent event=new ContentModelEvent(ContentModelEvent.CM_UPDATED,null,new HashMap(),this);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			}
			String messageCode = StringUtil.encodeURL(
					"content_table_field_update_mainfield_success", "UTF-8");
			helper.sendRedirect(helper.getBaseURL()
					+ "admin/contentEdit.jhtml?tableId=" + tableId
					+ "&messageCode=" + messageCode);

		}

		return null;

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doSetPhoto(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}

		String tableId = request.getParameter("tableId");
		String contentFieldId = request.getParameter("contentFieldId");
		int f = 0, t = 1;
		if (tableId != null) {
			Long tid = new Long(tableId);
			contentFieldManager.updateStatusOfTable(tid, "photoField",
					new Integer(f));
			if (contentFieldId != null) {
				Long fid = new Long(contentFieldId);
				contentFieldManager.updateStatus(fid, "photoField",
						new Integer(t));
				
			}
			ContentModelEvent event=new ContentModelEvent(ContentModelEvent.CM_UPDATED,null,new HashMap(),this);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			String messageCode = StringUtil.encodeURL(
					"content_table_field_update_photofield_success", "UTF-8");
			helper.sendRedirect(helper.getBaseURL()
					+ "admin/contentEdit.jhtml?tableId=" + tableId
					+ "&messageCode=" + messageCode);

		}

		return null;

	}

	/**
	 * show the order field view
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
	 * @throws
	 */
	public ModelAndView doOrderField(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}

		ModelAndView mv = new ModelAndView(orderFieldViewName, model);
		String tableId = request.getParameter("tableId");
		if (tableId != null) {
			Long id = new Long(tableId);
			List cfs = contentFieldManager.getContentFieldOfTable(id,
					" fieldOrder");
			model.put("cfs", cfs);
			model.put("tableId", tableId);
		}

		return mv;
	}

	/**
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
	 * @throws
	 */
	public ModelAndView doOrderFieldSubmit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}

		String fieldString = request.getParameter("FieldString");
		if (fieldString != null) {
			StringTokenizer st = new StringTokenizer(fieldString, ",");
			int i = 0;
			while (st.hasMoreTokens()) {
				String sid = st.nextToken();
				Long id = new Long(sid);
				contentFieldManager.updateStatus(id, "fieldOrder", new Integer(
						i));
				i++;
			}
			ContentModelCache.clear();
		}
		return null;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setContentModelHelper(ContentModelHelper contentModelHelper) {
		this.contentModelHelper = contentModelHelper;
	}

	public void setRsViewName(String rsViewName) {
		this.rsViewName = rsViewName;
	}

}
