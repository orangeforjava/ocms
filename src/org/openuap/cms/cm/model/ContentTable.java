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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.openuap.base.dao.hibernate.BaseObject;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * <p>
 * Title: ContentTable
 * </p>
 * 
 * <p>
 * Description:内容模型，内容元数据信息.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: <a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: ContentTable.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * @preserve private
 * @author Weiping Ju
 * @version 1.0
 */
public class ContentTable extends BaseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1654576077015781843L;

	private int hashValue = 0;

	/** 模型id. */
	private Long tableId;

	/** 模型属性集. */
	private Set<ContentField> contentFieldsSet;

	/** 模型名称. */
	private String name;

	/** . */
	private Long dsnid;

	/** 是否为系统保留模型. */
	private Integer system;

	/** 物理表名称. */
	private String tableName;

	/** 内容实体名称. */
	private String entityName;

	/** 内容发布实体名. */
	private String entityPublishName;

	/** 是否允许建立索引. */
	private int allowIndex;

	/** 最后索引日期. */
	private long lastIndexDate;

	/**
	 * 
	 */
	public ContentTable() {
	}

	/**
	 * 
	 * 
	 * @param tableid
	 */
	public ContentTable(Long tableid) {
		this.setTableId(tableid);
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getTableId() {

		return tableId;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param tableId
	 *            Integer
	 */
	public void setTableId(Long tableId) {
		this.hashValue = 0;

		this.tableId = tableId;
	}

	/**
	 * Return the value of the Name column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * Set the value of the Name column.
	 * 
	 * @param name
	 *            String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the value of the DSNID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getDsnid() {
		return this.dsnid;
	}

	/**
	 * Set the value of the DSNID column.
	 * 
	 * @param dsnid
	 *            Integer
	 */
	public void setDsnid(Long dsnid) {
		this.dsnid = dsnid;
	}

	/**
	 * Return the value of the TableID collection.
	 * 
	 * @return 
	 */
	public Set getContentFieldsSet() {
		return this.contentFieldsSet;
	}

	public Integer getSystem() {
		return system==null?0:system;
	}

	public String getTableName() {
		return tableName;
	}

	public String getEntityName() {
		return entityName==null?"":entityName;
	}

	public String getEntityPublishName() {
		return entityPublishName==null?"":entityPublishName;
	}

	/**
	 * Set the value of the TableID collection.
	 * 
	 * @param contentFieldsSet
	 *            Set
	 */
	public void setContentFieldsSet(java.util.Set contentFieldsSet) {
		this.contentFieldsSet = contentFieldsSet;
	}

	public void setSystem(Integer system) {
		this.system = system;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setEntityPublishName(String entityPublishName) {
		this.entityPublishName = entityPublishName;
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
		if (!(rhs instanceof ContentTable)) {
			return false;
		}
		ContentTable that = (ContentTable) rhs;
		if (this.getTableId() == null || that.getTableId() == null) {
			return false;
		}
		return (this.getTableId().equals(that.getTableId()));
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
			int tableidValue = this.getTableId() == null ? 0 : this.getTableId().hashCode();
			result = result * 37 + tableidValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
		XStream xstream = new XStream();
		xstream.alias("contentTable", ContentTable.class);
		xstream.alias("contentField", ContentField.class);
		ContentTable ct = new ContentTable(new Long("-100"));
		//
		ContentField cf = new ContentField();
		cf.setContentTable(null);
		// cf.setContentFieldId(new Integer(-1001));

		cf.setEnableCollection(new Integer("1"));
		cf.setEnableContribution(new Integer("1"));
		cf.setEnablePublish(new Integer("1"));
		cf.setFieldDefaultValue("");
		cf.setFieldDescription("");
		cf.setFieldInput("text");
		cf.setFieldInputFilter("");
		cf.setFieldInputPicker("upload");
		cf.setFieldInputTpl("");
		cf.setFieldListDisplay(new Integer("1"));
		cf.setFieldName("companyName");
		cf.setFieldOrder(new Integer(0));
		cf.setFieldSearchable(new Integer("1"));
		cf.setFieldSize("");
		cf.setFieldTitle("公司名");
		cf.setFieldType("varchar");
		cf.setKeywordsField(new Integer("0"));
		cf.setMainField(new Integer("0"));
		cf.setPhotoField(new Integer("0"));
		cf.setTitleField(new Integer("0"));
		// cf.
		//
		ContentField cf2 = new ContentField();
		cf2.setContentTable(null);
		cf2.setContentFieldId(new Long(-1002));
		cf2.setFieldName("companyAddress");

		//
		ct.setTableId(new Long(-100));
		ct.setDsnid(new Long(1));
		ct.setName("公司地址");
		ct.setSystem(new Integer("1"));
		ct.setTableName("app_member");
		//
		Set cfs = new HashSet();
		cfs.add(cf);
		cfs.add(cf2);
		ct.setContentFieldsSet(cfs);
		String xml = xstream.toXML(ct);
		System.out.println(xml);
		File file = new File("c:\\myXStream.xml");
		//
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		// file.getParentFile().mkdirs();
		// FileWriter writer = new FileWriter(file);
		System.out.println(out.getEncoding());
		xstream.toXML(ct, out);

		InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
		ContentTable ct_new = null;
		ct_new = (ContentTable) xstream.fromXML(in);
		// System.out.println(ct_new);
		// if(ct_new!=null){
		// Set ctfs_new=ct_new.getContentFieldsSet();
		// if(ctfs_new!=null){
		// Iterator it=ctfs_new.iterator();
		// while(it.hasNext()){
		// ContentField cf_new=(ContentField) it.next();
		// System.out.println(cf_new);
		// }
		// }
		// }

	}

	/**
	 * 
	 * @return
	 */
	public String getContentTableName() {
		if (entityName != null) {
			return entityName;
		} else {
			return "Content_" + this.getTableId();
		}
	}

	public Object getPrimaryKeyValue() {
		return tableId;
	}

	public int getAllowIndex() {
		return allowIndex;
	}

	public void setAllowIndex(int allowIndex) {
		this.allowIndex = allowIndex;
	}

	/**
	 * @return the lastIndexDate
	 */
	public long getLastIndexDate() {
		return lastIndexDate;
	}

	/**
	 * @param lastIndexDate
	 *            the lastIndexDate to set
	 */
	public void setLastIndexDate(long lastIndexDate) {
		this.lastIndexDate = lastIndexDate;
	}
}
