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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.data.AbstractDataImporter;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 内容模型数据导入器.
 * </p>
 * <p>
 * $Id: CmDataImporter.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 3.0
 */
public class CmDataImporter extends AbstractDataImporter {

	public void importData(Map parameters) {
		String fileName = (String) parameters.get(FILE_NAME);
		// System.out.println("fileName="+fileName);
		File file = null;
		if (fileName == null) {
			// 如果没有指定哪个文件，则寻找最新的文件导入
			file = getDefaultImportFile();
		} else {
			file = new File(dataDir, fileName);
		}
		if (file != null && file.exists()) {
			try {
				// System.out.println("file="+file.getName());
				importContentTable(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ContentTable importContentTable(File xmlFile) throws Exception {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(xmlFile);
		ContentTable ct = null;
		try {
			ContentTableManager ctManager = (ContentTableManager) ObjectLocator
					.lookup("contentTableManager", CmsPlugin.PLUGIN_ID);
			ct = new ContentTable();
			//
			Element ctElement = document.getRootElement();
			String tableId = ctElement.attributeValue("tableId");
			//
			String name = ctElement.element("name").getTextTrim();
			ct.setName(name);
			//
			String system = ctElement.element("system").getTextTrim();
			ct.setSystem(new Integer(system));
			//
			String tableName = ctElement.element("tableName").getTextTrim();
			ct.setTableName(tableName);
			//
			String entityName = ctElement.element("entityName").getTextTrim();
			ct.setEntityName(entityName);
			//
			String entityPublishName = ctElement.element("entityPublishName")
					.getTextTrim();
			ct.setEntityPublishName(entityPublishName);
			//
			String allowIndex = ctElement.element("allowIndex").getTextTrim();
			ct.setAllowIndex(new Integer(allowIndex));
			//
			String lastIndexDate = ctElement.element("lastIndexDate")
					.getTextTrim();
			ct.setLastIndexDate(new Long(lastIndexDate));
			ct.setDsnid(0L);
			ContentTable old_ct = ctManager.getContentTableByName(ct.getName());

			Long tid = null;
			if (old_ct != null) {
				tid = old_ct.getTableId();
				ct.setTableId(tid);
				ctManager.saveContentTable(ct);
			} else {
				tid = ctManager.addContentTable(ct);
				ct.setTableId(tid);
			}
			//
			Element fieldsElement = ctElement.element("fields");
			Set fieldSet = Collections.synchronizedSet(new HashSet());
			if (fieldsElement != null) {
				Iterator fieldIterator = fieldsElement.elementIterator("field");
				if (fieldIterator != null) {
					while (fieldIterator.hasNext()) {
						Element fieldElement = (Element) fieldIterator.next();
						ContentField field = importContentField(fieldElement,
								ct);
						//
						//
						fieldSet.add(field);
					}
				}
			}
			ct.setContentFieldsSet(fieldSet);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}

	protected ContentField importContentField(final Element fieldElement,
			final ContentTable ct) {
		ContentField field = new ContentField();
		String contentFieldId = fieldElement.attribute("contentFieldId")
				.getText();
		//
		String fieldTitle = fieldElement.element("fieldTitle").getTextTrim();
		field.setFieldTitle(fieldTitle);
		//
		String fieldName = fieldElement.element("fieldName").getTextTrim();
		field.setFieldName(fieldName);
		//
		String fieldType = fieldElement.element("fieldType").getTextTrim();
		field.setFieldType(fieldType);
		//
		String fieldSize = fieldElement.element("fieldSize").getTextTrim();
		field.setFieldSize(fieldSize);
		//
		String fieldInput = fieldElement.element("fieldInput").getTextTrim();
		field.setFieldInput(fieldInput);
		//
		String fieldDefaultValue = fieldElement.element("fieldDefaultValue")
				.getTextTrim();
		field.setFieldDefaultValue(fieldDefaultValue);
		//
		String fieldInputFilter = fieldElement.element("fieldInputFilter")
				.getTextTrim();
		field.setFieldInputFilter(fieldInputFilter);
		//FIX:BUG 修正了原来忘记导入这个字段属性了
		String fieldInputPicker = fieldElement.element("fieldInputPicker")
				.getTextTrim();
		field.setFieldInputPicker(fieldInputPicker);
		//
		String fieldInputPickerExtra = fieldElement.element(
				"fieldInputPickerExtra").getTextTrim();
		field.setFieldInputPickerExtra(fieldInputPickerExtra);
		//
		String fieldInputTpl = fieldElement.element("fieldInputTpl")
				.getTextTrim();
		field.setFieldInputTpl(fieldInputTpl);
		//
		String fieldDescription = fieldElement.element("fieldDescription")
				.getTextTrim();
		field.setFieldDescription(fieldDescription);
		//
		String fieldOrder = fieldElement.element("fieldOrder").getTextTrim();
		field.setFieldOrder(new Integer(fieldOrder));
		//
		String fieldListDisplay = fieldElement.element("fieldListDisplay")
				.getTextTrim();
		field.setFieldListDisplay(new Integer(fieldListDisplay));
		//
		String mainField = fieldElement.element("mainField").getTextTrim();
		field.setMainField(new Integer(mainField));
		//
		String titleField = fieldElement.element("titleField").getTextTrim();
		field.setTitleField(new Integer(titleField));
		//
		String keywordsField = fieldElement.element("keywordsField")
				.getTextTrim();
		field.setKeywordsField(new Integer(keywordsField));
		//
		String photoField = fieldElement.element("photoField").getTextTrim();
		field.setPhotoField(new Integer(photoField));
		//
		String fieldSearchable = fieldElement.element("fieldSearchable")
				.getTextTrim();
		field.setFieldSearchable(new Integer(fieldSearchable));
		//
		String enableContribution = fieldElement.element("enableContribution")
				.getTextTrim();
		field.setEnableContribution(new Integer(enableContribution));
		//
		String enableCollection = fieldElement.element("enableCollection")
				.getTextTrim();
		field.setEnableCollection(new Integer(enableCollection));
		//
		String enablePublish = fieldElement.element("enablePublish")
				.getTextTrim();
		field.setEnablePublish(new Integer(enablePublish));
		//
		String enableStatics = fieldElement.element("enableStatics")
				.getTextTrim();
		field.setEnableStatics(new Integer(enableStatics));
		//
		String indexType = fieldElement.element("indexType").getTextTrim();
		field.setIndexType(indexType);
		//
		String storeType = fieldElement.element("storeType").getTextTrim();
		field.setStoreType(storeType);
		//
		String termVectorType = fieldElement.element("termVectorType")
				.getTextTrim();
		field.setTermVectorType(termVectorType);

		ContentFieldManager fieldManager = (ContentFieldManager) ObjectLocator
				.lookup("contentFieldManager", CmsPlugin.PLUGIN_ID);
		Long tableId = ct.getTableId();
		field.setContentTable(ct);
		//

		if (fieldManager != null) {
			ContentField old_field = fieldManager.getContentFieldByName(
					tableId, fieldName);
			if (old_field != null) {
				field.setContentFieldId(old_field.getContentFieldId());
				fieldManager.saveContentField(field);
			} else {
				Long fieldId = fieldManager.addContentField(field);
				field.setContentFieldId(fieldId);
			}
		}
		return field;
	}
}
