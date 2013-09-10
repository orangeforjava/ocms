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
package org.openuap.cms.user.model;

/**
 * <p>
 * 抽象用户权限字段
 * </p>
 * 
 * <p>
 * $Id: AbstractUserPermField.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractUserPermField implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4880980232290649969L;
	
	private Long fieldId;
	
	private Long userId;

	private String objectType;

	private String objectId;

	
	private String fieldName;
	
	private String fieldValue;
	
	private String fieldTitle;
	
	private String fieldType;
	
	private Integer fieldOrder;
	
	private String fieldOwner;
	
	private AbstractUserPermission abstractUserPermission;
	
	
	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(Integer fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	

	

	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((fieldId == null) ? 0 : fieldId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractUserPermField other = (AbstractUserPermField) obj;
		if (fieldId == null) {
			if (other.fieldId != null)
				return false;
		} else if (!fieldId.equals(other.fieldId))
			return false;
		return true;
	}

	public AbstractUserPermission getBaseUserPermission() {
		return abstractUserPermission;
	}

	public void setBaseUserPermission(AbstractUserPermission abstractUserPermission) {
		this.abstractUserPermission = abstractUserPermission;
	}

	public String getFieldOwner() {
		return fieldOwner;
	}

	public void setFieldOwner(String fieldOwner) {
		this.fieldOwner = fieldOwner;
	}


}
