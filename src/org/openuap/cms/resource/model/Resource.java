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
package org.openuap.cms.resource.model;

import java.io.Serializable;
import java.util.Set;

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * 
 * <p>
 * 资源实体.
 * </p>
 * 
 * <p>
 * $Id: Resource.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class Resource extends BaseObject implements Serializable {

	public static final String IMG_TYPE = "img";

	public static final String FLASH_TYPE = "flash";

	public static final String ATT_TYPE = "att";

	public static final String SATT_TYPE = "satt";

	private int hashValue = 0;

	/** 资源id. */
	private Long resourceId;

	/** 资源所属结点id. */
	private Long nodeId;

	/** The value of the simple parentid property. */
	private Long parentId;

	/** 资源类型. 0，代表系统，1代表会员. */
	private Integer type;

	/** 资源分类，图片，Flash，附件,受控附件. */
	private String category;

	/** 资源名称. */
	private String name;

	/** 资源存放路径. */
	private String path;

	/** 资源尺寸. */
	private Integer size;

	/** 资源信息. */
	private String info;

	/** 产生日期. */
	private Long creationDate;

	/** 修改日期. */
	private Long modifiedDate;

	/** 来源. */
	private String src;

	/** 标题. */
	private String title;

	/** 资源对应的用户id. */
	private Long creationUserId;

	private Set contentIndexSet;

	/** 资源对应的用户名. */
	private String userName;

	/** 下载次数. */
	private Integer downloadTimes;

	/** 资源自定义分类. */
	private String customCategory;

	/**
	 * Simple constructor of AbstractCmsResource instances.
	 */
	public Resource() {
	}

	/**
	 * Constructor of AbstractCmsResource instances given a simple primary key.
	 * 
	 * @param resourceid
	 *            Integer
	 */
	public Resource(Long resourceid) {
		this.setResourceId(resourceid);
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getResourceId() {

		return resourceId;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param resourceId
	 *            Integer
	 */
	public void setResourceId(Long resourceId) {
		this.hashValue = 0;

		this.resourceId = resourceId;
	}

	/**
	 * Return the value of the NodeID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getNodeId() {

		return nodeId;
	}

	/**
	 * Set the value of the NodeID column.
	 * 
	 * @param nodeId
	 *            Integer
	 */
	public void setNodeId(Long nodeId) {

		this.nodeId = nodeId;
	}

	/**
	 * Return the value of the ParentID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getParentId() {

		return parentId;
	}

	/**
	 * Set the value of the ParentID column.
	 * 
	 * @param parentId
	 *            Integer
	 */
	public void setParentId(Long parentId) {

		this.parentId = parentId;
	}

	/**
	 * Return the value of the Type column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 * Set the value of the Type column.
	 * 
	 * @param type
	 *            Byte
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * Return the value of the Category column.
	 * 
	 * @return java.lang.String
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * Set the value of the Category column.
	 * 
	 * @param category
	 *            String
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Return the value of the Name column.
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
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
	 * Return the value of the Path column.
	 * 
	 * @return java.lang.String
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Set the value of the Path column.
	 * 
	 * @param path
	 *            String
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Return the value of the Size column.
	 * 
	 * @return java.lang.Integer
	 */
	public Integer getSize() {
		return this.size;
	}

	/**
	 * Set the value of the Size column.
	 * 
	 * @param size
	 *            Integer
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * Return the value of the Info column.
	 * 
	 * @return java.lang.String
	 */
	public String getInfo() {
		return this.info;
	}

	/**
	 * Set the value of the Info column.
	 * 
	 * @param info
	 *            String
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * Return the value of the CreationDate column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getCreationDate() {

		return creationDate;
	}

	/**
	 * Set the value of the CreationDate column.
	 * 
	 * @param creationDate
	 *            Integer
	 */
	public void setCreationDate(Long creationDate) {

		this.creationDate = creationDate;
	}

	/**
	 * Return the value of the ModifiedDate column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getModifiedDate() {

		return modifiedDate;
	}

	/**
	 * Set the value of the ModifiedDate column.
	 * 
	 * @param modifiedDate
	 *            Integer
	 */
	public void setModifiedDate(Long modifiedDate) {

		this.modifiedDate = modifiedDate;
	}

	/**
	 * Return the value of the Src column.
	 * 
	 * @return java.lang.String
	 */
	public String getSrc() {
		return this.src;
	}

	/**
	 * Set the value of the Src column.
	 * 
	 * @param src
	 *            String
	 */
	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * Return the value of the Title column.
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set the value of the Title column.
	 * 
	 * @param title
	 *            String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Return the value of the CreationUserID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getCreationUserId() {

		return creationUserId;
	}

	public Set getContentIndexSet() {
		return contentIndexSet;
	}

	public String getUserName() {
		return userName;
	}

	public Integer getDownloadTimes() {
		return downloadTimes;
	}

	public String getCustomCategory() {
		return customCategory;
	}

	/**
	 * Set the value of the CreationUserID column.
	 * 
	 * @param creationUserId
	 *            Integer
	 */
	public void setCreationUserId(Long creationUserId) {

		this.creationUserId = creationUserId;
	}

	public void setContentIndexSet(Set contentIndexSet) {
		this.contentIndexSet = contentIndexSet;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setDownloadTimes(Integer downloadTimes) {
		this.downloadTimes = downloadTimes;
	}

	public void setCustomCategory(String customCategory) {
		this.customCategory = customCategory;
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
		if (!(rhs instanceof Resource)) {
			return false;
		}
		Resource that = (Resource) rhs;
		if (this.getResourceId() == null || that.getResourceId() == null) {
			return false;
		}
		return (this.getResourceId().equals(that.getResourceId()));
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
			int resourceidValue = this.getResourceId() == null ? 0 : this
					.getResourceId().hashCode();
			result = result * 37 + resourceidValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

}
