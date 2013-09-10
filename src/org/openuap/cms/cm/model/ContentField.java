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
package org.openuap.cms.cm.model;

import java.io.Serializable;

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * 
 * <p>
 * 内容元数据属性对象。
 * </p>
 * 
 * <p>
 * $Id: ContentField.java 4010 2011-01-14 11:32:51Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class ContentField extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2204402827820974430L;

	/**
	 * 构造函数
	 */
	public ContentField() {
	}

	private int hashValue = 0;

	/** 属性id. */
	private Long contentFieldId;

	/** 内容模型. */
	private ContentTable contentTable;

	/** 内容模型id. */
	private Long tableId;

	/** 属性标题. */
	private String fieldTitle;

	/** 字段名. */
	private String fieldName;

	/** 属性类型. */
	private String fieldType;

	/** 属性大小. */
	private String fieldSize;

	/** 属性UI录入形式. */
	private String fieldInput;

	/** 属性缺省值. */
	private String fieldDefaultValue;

	/** 属性输入过滤器. */
	private String fieldInputFilter;

	/** 输入内容采集器. */
	private String fieldInputPicker;

	/** 输入内容采集器附加信息. */
	private String fieldInputPickerExtra;

	/** 属性输入模板. */
	private String fieldInputTpl;

	/** 属性描述. */
	private String fieldDescription;

	/** 属性排序. */
	private Integer fieldOrder;

	/** 属性是否在列表中显示. */
	private Integer fieldListDisplay;

	/** 是否是主内容. */
	private Integer mainField;

	/** 是否是标题属性. */
	private Integer titleField;

	/** 是否是关键字属性. */
	private Integer keywordsField;

	/** 是否是首图属性. */
	private Integer photoField;

	/** 是否是可搜索属性. */
	private Integer fieldSearchable;

	/** 是否允许投稿. */
	private Integer enableContribution;
	/** 是否为附加内容分类. */
	private Integer otherCategory=0;
	/** 是否允许采集. */
	private Integer enableCollection;

	/** 是否允许发布. */
	private Integer enablePublish;

	/** 是否是统计字段，统计字段不允许输入.0-否，1-是. */
	private Integer enableStatics;

	/** 索引类型，. */
	private String indexType;
	/** 索引存储类型. */
	private String storeType;
	/** 词条向量类型. */
	private String termVectorType;

	/**
	 * @return the termVectorType
	 */
	public String getTermVectorType() {
		return termVectorType == null ? "" : termVectorType;
	}

	/**
	 * @param termVectorType
	 *            the termVectorType to set
	 */
	public void setTermVectorType(String termVectorType) {
		this.termVectorType = termVectorType;
	}

	/**
	 * @return the indexType
	 */
	public String getIndexType() {
		return indexType == null ? "" : indexType;
	}

	/**
	 * @param indexType
	 *            the indexType to set
	 */
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	/**
	 * @return the storeType
	 */
	public String getStoreType() {
		return storeType == null ? "" : storeType;
	}

	/**
	 * @param storeType
	 *            the storeType to set
	 */
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	/**
	 * 
	 * 
	 * @param contentFieldId
	 * 
	 */
	public ContentField(Long contentFieldId) {
		this.setContentFieldId(contentFieldId);
	}

	/**
	 * 
	 * 
	 * @return java.lang.Integer
	 */
	public Long getContentFieldId() {

		return contentFieldId;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param contentFieldId
	 *            Integer
	 */
	public void setContentFieldId(Long contentFieldId) {
		this.hashValue = 0;

		this.contentFieldId = contentFieldId;
	}

	/**
	 * Return the value of the TableID column.
	 * 
	 * @return CmsContentTable
	 */
	public ContentTable getContentTable() {
		return this.contentTable;
	}

	/**
	 * Set the value of the TableID column.
	 * 
	 * @param contentTable
	 *            ContentTable
	 */
	public void setContentTable(ContentTable contentTable) {
		this.contentTable = contentTable;
	}

	/**
	 * Return the value of the FieldName column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldName() {

		return fieldName;
	}

	/**
	 * Set the value of the FieldName column.
	 * 
	 * @param fieldName
	 *            String
	 */
	public void setFieldName(java.lang.String fieldName) {

		this.fieldName = fieldName;
	}

	/**
	 * Return the value of the FieldType column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldType() {

		return fieldType == null ? "" : fieldType;
	}

	/**
	 * Set the value of the FieldType column.
	 * 
	 * @param fieldType
	 *            String
	 */
	public void setFieldType(java.lang.String fieldType) {

		this.fieldType = fieldType;
	}

	/**
	 * Return the value of the FieldSize column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldSize() {

		return fieldSize == null ? "" : fieldSize;
	}

	/**
	 * Set the value of the FieldSize column.
	 * 
	 * @param fieldSize
	 *            String
	 */
	public void setFieldSize(java.lang.String fieldSize) {

		this.fieldSize = fieldSize;
	}

	/**
	 * Return the value of the FieldInput column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldInput() {

		return fieldInput == null ? "" : fieldInput;
	}

	/**
	 * Set the value of the FieldInput column.
	 * 
	 * @param fieldInput
	 *            String
	 */
	public void setFieldInput(java.lang.String fieldInput) {

		this.fieldInput = fieldInput;
	}

	/**
	 * Return the value of the FieldDefaultValue column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldDefaultValue() {

		return fieldDefaultValue == null ? "" : fieldDefaultValue;
	}

	/**
	 * Set the value of the FieldDefaultValue column.
	 * 
	 * @param fieldDefaultValue
	 *            String
	 */
	public void setFieldDefaultValue(java.lang.String fieldDefaultValue) {

		this.fieldDefaultValue = fieldDefaultValue;
	}

	/**
	 * Return the value of the FieldInputFilter column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldInputFilter() {

		return fieldInputFilter == null ? "" : fieldInputFilter;
	}

	/**
	 * Set the value of the FieldInputFilter column.
	 * 
	 * @param fieldInputFilter
	 *            String
	 */
	public void setFieldInputFilter(java.lang.String fieldInputFilter) {

		this.fieldInputFilter = fieldInputFilter;
	}

	/**
	 * Return the value of the FieldInputPicker column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldInputPicker() {

		return fieldInputPicker == null ? "" : fieldInputPicker;
	}

	/**
	 * Set the value of the FieldInputPicker column.
	 * 
	 * @param fieldInputPicker
	 *            String
	 */
	public void setFieldInputPicker(java.lang.String fieldInputPicker) {

		this.fieldInputPicker = fieldInputPicker;
	}

	/**
	 * Return the value of the FieldInputTpl column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldInputTpl() {

		return fieldInputTpl == null ? "" : fieldInputTpl;
	}

	/**
	 * Set the value of the FieldInputTpl column.
	 * 
	 * @param fieldInputTpl
	 *            String
	 */
	public void setFieldInputTpl(java.lang.String fieldInputTpl) {

		this.fieldInputTpl = fieldInputTpl;
	}

	/**
	 * Return the value of the FieldDescription column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFieldDescription() {

		return fieldDescription == null ? "" : fieldDescription;
	}

	/**
	 * Set the value of the FieldDescription column.
	 * 
	 * @param fieldDescription
	 *            String
	 */
	public void setFieldDescription(java.lang.String fieldDescription) {

		this.fieldDescription = fieldDescription;
	}

	/**
	 * Return the value of the FieldOrder column.
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getFieldOrder() {

		return fieldOrder == null ? 0 : fieldOrder;
	}

	/**
	 * Set the value of the FieldOrder column.
	 * 
	 * @param fieldOrder
	 *            Integer
	 */
	public void setFieldOrder(java.lang.Integer fieldOrder) {

		this.fieldOrder = fieldOrder;
	}

	/**
	 * Return the value of the FieldListDisplay column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getFieldListDisplay() {

		return fieldListDisplay == null ? 0 : fieldListDisplay;
	}

	/**
	 * Set the value of the FieldListDisplay column.
	 * 
	 * @param fieldListDisplay
	 *            Byte
	 */
	public void setFieldListDisplay(Integer fieldListDisplay) {

		this.fieldListDisplay = fieldListDisplay;
	}

	/**
	 * Return the value of the IsMainField column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getMainField() {

		return mainField == null ? 0 : mainField;
	}

	/**
	 * Set the value of the IsMainField column.
	 * 
	 * @param mainField
	 *            Byte
	 */
	public void setMainField(Integer mainField) {

		this.mainField = mainField;
	}

	/**
	 * Return the value of the IsTitleField column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getTitleField() {

		return titleField;
	}

	/**
	 * Set the value of the IsTitleField column.
	 * 
	 * @param titleField
	 *            Byte
	 */
	public void setTitleField(Integer titleField) {

		this.titleField = titleField;
	}

	/**
	 * Return the value of the FieldSearchable column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getFieldSearchable() {

		return fieldSearchable == null ? 0 : fieldSearchable;
	}

	/**
	 * Set the value of the FieldSearchable column.
	 * 
	 * @param fieldSearchable
	 *            Byte
	 */
	public void setFieldSearchable(Integer fieldSearchable) {

		this.fieldSearchable = fieldSearchable;
	}

	/**
	 * Return the value of the EnableContribution column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getEnableContribution() {

		return enableContribution == null ? 0 : enableContribution;
	}

	/**
	 * Set the value of the EnableContribution column.
	 * 
	 * @param enableContribution
	 *            Byte
	 */
	public void setEnableContribution(Integer enableContribution) {

		this.enableContribution = enableContribution;
	}

	/**
	 * Return the value of the EnablePublish column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getEnablePublish() {

		return enablePublish == null ? 0 : enablePublish;
	}

	public String getFieldTitle() {
		return fieldTitle == null ? "" : fieldTitle;
	}

	public Integer getEnableCollection() {
		return enableCollection == null ? 0 : enableCollection;
	}

	public Integer getKeywordsField() {
		return keywordsField == null ? 0 : keywordsField;
	}

	public Integer getPhotoField() {
		return photoField == null ? 0 : photoField;
	}

	public Long getTableId() {
		return tableId;
	}

	public String getFieldInputPickerExtra() {
		return fieldInputPickerExtra == null ? "" : fieldInputPickerExtra;
	}

	public Integer getEnableStatics() {
		return enableStatics == null ? 0 : enableStatics;
	}

	/**
	 * Set the value of the EnablePublish column.
	 * 
	 * @param enablePublish
	 *            Byte
	 */
	public void setEnablePublish(Integer enablePublish) {

		this.enablePublish = enablePublish;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public void setEnableCollection(Integer enableCollection) {
		this.enableCollection = enableCollection;
	}

	public void setPhotoField(Integer photoField) {
		this.photoField = photoField;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public void setFieldInputPickerExtra(String fieldInputPickerExtra) {
		this.fieldInputPickerExtra = fieldInputPickerExtra;
	}

	public void setEnableStatics(Integer enableStatics) {
		this.enableStatics = enableStatics;
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof ContentField)) {
			return false;
		}
		ContentField that = (ContentField) rhs;
		if (this.getContentFieldId() == null
				|| that.getContentFieldId() == null) {
			return false;
		}
		return (this.getContentFieldId().equals(that.getContentFieldId()));
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int contentfieldidValue = this.getContentFieldId() == null ? 0
					: this.getContentFieldId().hashCode();
			result = result * 37 + contentfieldidValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	/**
	 * 转化指定的值为本域指定的类型
	 * 
	 * @param src
	 * @return
	 */
	public Object convertFieldValue(Object src) {
		if (fieldType.equals("integer")) {
			return new Integer(src.toString());
		} else if (fieldType.equals("long")) {
			return new Long(src.toString());
		} else if (fieldType.equals("float")) {
			return new Float(src.toString());
		} else if (fieldType.equals("boolean")) {
			return new Boolean(src.toString());
		} else {
			return src.toString();
		}

	}
	
	public Integer getOtherCategory() {
		return otherCategory;
	}

	public void setOtherCategory(Integer otherCategory) {
		this.otherCategory = otherCategory;
	}

	public void setKeywordsField(Integer keywordsField) {
		this.keywordsField = keywordsField;
	}

}
