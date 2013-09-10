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
package org.openuap.cms.tpl.model;

import org.openuap.base.dao.hibernate.*;

/**
 * 
 * <p>
 * 模板分类实体.
 * </p>
 * 
 * <p>
 * $Id: TemplateCategory.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class TemplateCategory extends BaseObject implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 280606876835252836L;

	// Constructors
	private int hashValue = 0;

	private Long id;

	private Long parentId;

	private String cateName;

	private String cateTitle;

	private String cateDesc;

	private Long cateStatus;

	private Long creationUid;

	private Long creationDate;

	private Long modifiedDate;

	public TemplateCategory() {
	}

	/**
	 * minimal constructor
	 * 
	 * @param parentId
	 *            long
	 * @param cateName
	 *            String
	 * @param cateStatus
	 *            long
	 * @param creationUid
	 *            long
	 * @param creationDate
	 *            long
	 * @param modifiedDate
	 *            long
	 */
	public TemplateCategory(Long parentId, String cateName, Long cateStatus,
			Long creationUid, Long creationDate, Long modifiedDate) {
		this.parentId = parentId;
		this.cateName = cateName;
		this.cateStatus = cateStatus;
		this.creationUid = creationUid;
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
	}

	/**
	 * full constructor
	 * 
	 * @param parentId
	 *            long
	 * @param cateName
	 *            String
	 * @param cateTitle
	 *            String
	 * @param cateStatus
	 *            long
	 * @param creationUid
	 *            long
	 * @param creationDate
	 *            long
	 * @param modifiedDate
	 *            long
	 */
	public TemplateCategory(Long parentId, String cateName, String cateTitle,
			Long cateStatus, Long creationUid, Long creationDate,
			Long modifiedDate) {
		this.parentId = parentId;
		this.cateName = cateName;
		this.cateTitle = cateTitle;
		this.cateStatus = cateStatus;
		this.creationUid = creationUid;
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Template)) {
			return false;
		}
		Template that = (Template) o;
		if (this.getId().longValue() == 0 || that.getId().longValue() == 0) {
			return false;
		}
		return (this.getId().equals(that.getId()));

	}

	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int idValue = this.getId().hashCode();
			result = result * 37 + idValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public String getCateDesc() {
		return cateDesc;
	}

	public String getCateName() {
		return cateName;
	}

	public Long getCateStatus() {
		return cateStatus;
	}

	public String getCateTitle() {
		return cateTitle;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public Long getCreationUid() {
		return creationUid;
	}

	public Long getId() {
		return id;
	}

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreationUid(Long creationUid) {
		this.creationUid = creationUid;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public void setCateTitle(String cateTitle) {
		this.cateTitle = cateTitle;
	}

	public void setCateStatus(Long cateStatus) {
		this.cateStatus = cateStatus;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public void setCateDesc(String cateDesc) {
		this.cateDesc = cateDesc;
	}

}
