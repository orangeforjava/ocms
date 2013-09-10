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

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * <p>
 * 资源分类对象.
 * </p>
 * 
 * <p>
 * $Id: ResouceCategory.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class ResouceCategory extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3684899746771701189L;

	//
	private int hashValue = 0;

	//
	private Long categoryId;

	private String categoryName;

	private String categoryDesc;

	private Long parentCategoryId;

	private Integer status;

	private Integer pos;

	public ResouceCategory() {
	}

	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof ResouceCategory)) {
			return false;
		}
		ResouceCategory that = (ResouceCategory) rhs;
		if (this.getCategoryId() == null || that.getCategoryId() == null) {
			return false;
		}
		return (this.getCategoryId().equals(that.getCategoryId()));

	}

	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int resourceidValue = this.getCategoryId() == null ? 0 : this
					.getCategoryId().hashCode();
			result = result * 37 + resourceidValue;
			this.hashValue = result;
		}
		return this.hashValue;

	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public Integer getPos() {
		return pos;
	}

	public Integer getStatus() {
		return status;
	}

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
}
