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
package org.openuap.cms.cm.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.data.AbstractDataExporter;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 内容模型导出器.
 * </p>
 * 
 * <p>
 * $Id: CmDataExporter.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 3.0
 */
public class CmDataExporter extends AbstractDataExporter {

	public void exportData(Map parameters) {
		try {
			ContentTableManager ctManager = (ContentTableManager) ObjectLocator
					.lookup("contentTableManager", CmsPlugin.PLUGIN_ID);
			String ids = null;
			if (parameters != null) {
				ids = (String) parameters.get("ids");
			}
			if (ids == null) {
				ids = "all";
			}
			if (ids.equals("all")) {
				List<ContentTable> cts = ctManager.getAllContentTable();
				for(ContentTable ct:cts){
					//不要使用中文，在Linux环境中会有问题
					String fileName ="cm-"+ct.getEntityName()+".xml";
					File file=new File(dataDir, fileName);
					String xml=exportContentTable(ct);
					IOUtils.write(xml, new FileOutputStream(file), "UTF-8");
				}
			} else {
				final String[] idary = ids.split(",");
				for (int i = 0; i < idary.length; i++) {
					String id = idary[i];
					ContentTable ct=ctManager.getContentTableById(new Long(id));
					//更改为实体名
					String fileName =ct.getEntityName();
					File file=new File(dataDir, fileName);
					String xml=exportContentTable(ct);
					IOUtils.write(xml, new FileOutputStream(file), "UTF-8");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param ct
	 * @return
	 */
	public String exportContentTable(ContentTable ct) {
		Document document = DocumentHelper.createDocument();
		Element ctElement = document.addElement("ContentTable");
		ctElement.addAttribute("tableId", ct.getTableId().toString());
		Element nameElement = ctElement.addElement("name");
		nameElement.addCDATA(ct.getContentTableName());
		Element systemElement = ctElement.addElement("system");
		systemElement.addText(ct.getSystem().toString());
		Element tableNameElement = ctElement.addElement("tableName");
		tableNameElement.addText(ct.getTableName());
		Element entityNameElement = ctElement.addElement("entityName");
		entityNameElement.addText(ct.getEntityName());
		Element entityPublishNameElement = ctElement
				.addElement("entityPublishName");
		entityPublishNameElement.addText(ct.getEntityPublishName());

		Element allowIndexElement = ctElement.addElement("allowIndex");
		allowIndexElement.addText(new Integer(ct.getAllowIndex()).toString());

		Element lastIndexDateElement = ctElement.addElement("lastIndexDate");
		lastIndexDateElement
				.addText(new Long(ct.getLastIndexDate()).toString());

		//
		Element fieldsElement = ctElement.addElement("fields");
		Set fieldsSet = ct.getContentFieldsSet();
		if (fieldsSet != null && fieldsSet.size() > 0) {
			Iterator fieldsIterator = fieldsSet.iterator();
			while (fieldsIterator.hasNext()) {
				ContentField field = (ContentField) fieldsIterator.next();
				Element fieldElement = fieldsElement.addElement("field");
				fieldElement.addAttribute("contentFieldId", field
						.getContentFieldId().toString());
				fieldElement.addElement("fieldTitle").addCDATA(
						field.getFieldTitle());
				fieldElement.addElement("fieldName").addCDATA(
						field.getFieldName());
				fieldElement.addElement("fieldType").addText(
						field.getFieldType());
				fieldElement.addElement("fieldSize").addText(
						field.getFieldSize());
				fieldElement.addElement("fieldInput").addText(
						field.getFieldInput());
				fieldElement.addElement("fieldDefaultValue").addCDATA(
						field.getFieldDefaultValue());
				fieldElement.addElement("fieldInputFilter").addCDATA(
						field.getFieldInputFilter());
				fieldElement.addElement("fieldInputPicker").addCDATA(
						field.getFieldInputPicker());
				fieldElement.addElement("fieldInputPickerExtra").addCDATA(
						field.getFieldInputPickerExtra());
				fieldElement.addElement("fieldInputTpl").addCDATA(
						field.getFieldInputTpl());
				fieldElement.addElement("fieldDescription").addCDATA(
						field.getFieldDescription());
				fieldElement.addElement("fieldOrder").addText(
						field.getFieldOrder().toString());
				fieldElement.addElement("fieldListDisplay").addText(
						field.getFieldListDisplay().toString());
				fieldElement.addElement("mainField").addText(
						field.getMainField().toString());
				fieldElement.addElement("titleField").addText(
						field.getTitleField().toString());
				fieldElement.addElement("keywordsField").addText(
						field.getKeywordsField().toString());
				fieldElement.addElement("photoField").addText(
						field.getPhotoField().toString());

				fieldElement.addElement("fieldSearchable").addText(
						field.getFieldSearchable().toString());
				fieldElement.addElement("enableContribution").addText(
						field.getEnableContribution().toString());
				fieldElement.addElement("enableCollection").addText(
						field.getEnableCollection().toString());

				fieldElement.addElement("enablePublish").addText(
						field.getEnablePublish().toString());
				fieldElement.addElement("enableStatics").addText(
						field.getEnableStatics().toString());
				fieldElement.addElement("indexType").addText(
						field.getIndexType());
				fieldElement.addElement("storeType").addText(
						field.getStoreType());
				fieldElement.addElement("termVectorType").addText(
						field.getStoreType());
			}
		}
		StringWriter rs = new StringWriter();
		try {
			/** 格式化输出,类型IE浏览一样 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(rs, format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs.toString();
	}

	public int getBackupFileNum() {

		return 0;
	}
}
