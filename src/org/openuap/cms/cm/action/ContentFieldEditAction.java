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

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.event.ContentModelEvent;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.cm.util.ContentModelHelper;
import org.openuap.cms.core.action.AdminFormAction;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.util.IndexType;
import org.openuap.cms.util.sql.FieldType;
import org.openuap.cms.util.ui.FieldInputFilter;
import org.openuap.cms.util.ui.FieldInputPicker;
import org.openuap.cms.util.ui.FieldInputType;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 内容属性编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: ContentFieldEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentFieldEditAction extends AdminFormAction {

	private String defaultScreensPath;

	private ContentFieldManager contentFieldManager;

	private ContentTableManager contentTableManager;

	private ContentModelHelper contentModelHelper;

	public ContentFieldEditAction() {
		initDefaultProperty();
	}

	/**
	 * 设置缺省值
	 */
	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/cm/";
		this.setFormView(defaultScreensPath + "content_field_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(ContentField.class);
		this.setCommandName("contentField");
	}

	/**
	 * 表单提交
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		// 验证权限
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		//
		String mode = request.getParameter("mode");
		ContentField cf = (ContentField) command;
		if (mode != null && mode.equalsIgnoreCase("edit")) {
			contentFieldManager.saveContentField(cf);
		} else {
			//
			cf.setFieldOrder(new Integer(0));
			byte t = 0;
			cf.setFieldListDisplay(new Integer(t));
			cf.setMainField(new Integer(t));
			cf.setTitleField(new Integer(t));
			cf.setKeywordsField(new Integer(t));
			cf.setPhotoField(new Integer(t));
			//
			//
			contentFieldManager.addContentField(cf);
		}
		ContentTable ct = contentTableManager.getContentTableById(cf
				.getContentTable().getTableId());
		contentModelHelper.updateContentModel(ct, true, true);
		ContentModelEvent event = new ContentModelEvent(
				ContentModelEvent.CM_UPDATED, ct, new HashMap(), this);
		WebPluginManagerUtils.dispatcherEvent(false, "base", event);
		//
		String messageCode = StringUtil.encodeURL(
				"content_field_modify_success", "UTF-8");
		helper.sendRedirect(helper.getBaseURL()
				+ "admin/fieldEdit.jhtml?tableId="
				+ cf.getContentTable().getTableId()
				+ "&mode=edit&contentFieldId=" + cf.getContentFieldId()
				+ "&messageCode=" + messageCode);
		return null;

	}

	/**
	 * @param request
	 * @return Object
	 * @throws Exception
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String fieldId = request.getParameter("contentFieldId");
		String tableId = request.getParameter("tableId");
		if (fieldId != null) {
			Long id = new Long(fieldId);
			ContentField contentField = contentFieldManager
					.getContentFieldById(id);
			if (tableId != null) {
				Long tid = new Long(tableId);
				ContentTable ct = contentTableManager.getContentTableById(tid);
				contentField.setContentTable(ct);

			}
			return contentField;
		} else {
			ContentField contentField = new ContentField();
			if (tableId != null) {
				Long tid = new Long(tableId);
				ContentTable ct = contentTableManager.getContentTableById(tid);
				contentField.setContentTable(ct);

			}
			return contentField;
		}
	}

	/**
	 * 引用对象数据
	 * 
	 * @param request
	 * @return Map
	 * @throws Exception
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		//
		Map model = new HashMap();
		String mode = request.getParameter("mode");
		// remember the edit mode
		if (mode != null) {
			model.put("mode", mode);
		} else {
			model.put("mode", "new");
		}
		//
		String messageCode = request.getParameter("messageCode");
		if (messageCode != null) {
			model
					.put("messageCode", StringUtil.decodeURL(messageCode,
							"UTF-8"));
		}

		// 1.the reference the field type,the define may be store a xml file
		// but now,we can simple hard code
		// and this will use the hibernate dialect.
		List fieldTypes = this.getFieldType();
		model.put("fieldTypes", fieldTypes);
		//
		List fieldInputTypes = this.getFieldInputType();
		model.put("fieldInputTypes", fieldInputTypes);
		//
		List fieldInputFilter = this.getFieldInputFilter();
		model.put("fieldInputFilter", fieldInputFilter);
		//
		List fieldInputPicker = this.getFieldInputPicker();
		model.put("fieldInputPicker", fieldInputPicker);

		model.put("indexTypes", IndexType.ALL_INDEX_TYPE);
		model.put("storeTypes", IndexType.ALL_STORE_TYPE);
		model.put("termVectorTypes", IndexType.ALL_TREMVECTOR_TYPE);

		return model;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setContentModelHelper(ContentModelHelper contentModelHelper) {
		this.contentModelHelper = contentModelHelper;
	}

	/**
	 * @return List
	 */
	protected List getFieldType() {
		List typeList = new ArrayList();
		typeList.add(new FieldType(Types.VARCHAR, "字符", "varchar"));
		typeList.add(new FieldType(Types.BOOLEAN, "布尔型", "boolean"));
		typeList.add(new FieldType(Types.INTEGER, "整型", "integer"));
		typeList.add(new FieldType(Types.NUMERIC, "长整型", "long"));
		typeList.add(new FieldType(Types.TINYINT, "字节类型", "byte"));
		typeList.add(new FieldType(Types.FLOAT, "浮点型", "float"));
		typeList.add(new FieldType(Types.CLOB, "大型文本", "text"));
		typeList.add(new FieldType(Types.BINARY, "二进制数据", "binary"));
		typeList.add(new FieldType(-1860, "其他结点内容(不用设长度)", "contentlink"));
		typeList.add(new FieldType(-1861, "多图（不用设长度）", "text"));
		return typeList;
	}

	/**
	 * @return List
	 */
	protected List getFieldInputType() {
		List typeList = new ArrayList();
		typeList.add(new FieldInputType("text", "单行文本"));
		typeList.add(new FieldInputType("textaera", "多行文本"));
		typeList.add(new FieldInputType("select", "选择框"));
		typeList.add(new FieldInputType("radio", "单选按钮"));
		typeList.add(new FieldInputType("checkbox", "检查框"));
		typeList.add(new FieldInputType("password", "密码框"));
		typeList.add(new FieldInputType("RichEditor", "网页内容编辑器"));
		typeList.add(new FieldInputType("MultiImg", "多图"));
		return typeList;
	}

	protected List getFieldInputFilter() {
		List typeList = new ArrayList();
		typeList.add(new FieldInputFilter("", "无限制", ""));
		typeList.add(new FieldInputFilter("notnull", "不能为空", ""));
		typeList.add(new FieldInputFilter("num", "限数字", "\\d+"));
		typeList.add(new FieldInputFilter("num_letter", "限数字或字母", "\\w+"));
		typeList.add(new FieldInputFilter("unique", "值唯一", ""));

		return typeList;
	}

	protected List getFieldInputPicker() {
		List typeList = new ArrayList();
		typeList.add(new FieldInputPicker("", "无"));
		typeList.add(new FieldInputPicker("color", "颜色"));
		typeList.add(new FieldInputPicker("date", "时间"));
		typeList.add(new FieldInputPicker("upload", "图片录入"));
		typeList.add(new FieldInputPicker("upload_attach", "附件录入"));
		typeList.add(new FieldInputPicker("upload_sattach", "受控附件录入"));
		typeList.add(new FieldInputPicker("flash", "Flash录入"));
		typeList.add(new FieldInputPicker("tpl", "模板选择"));
		typeList.add(new FieldInputPicker("psn", "发布点(PSN)对象选择"));
		typeList.add(new FieldInputPicker("content", "基于结点内容"));
		return typeList;

	}
}
