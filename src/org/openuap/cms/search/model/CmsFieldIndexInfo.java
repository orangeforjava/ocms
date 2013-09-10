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
package org.openuap.cms.search.model;

/**
 * 
 * <p>
 * 内容属性索引信息
 * </p>
 * 
 * <p>
 * $Id: CmsFieldIndexInfo.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class CmsFieldIndexInfo {
	private long fieldId;
	
	private int indexType;
	private boolean indexEnable;
	/**
	 * @return the fieldId
	 */
	public long getFieldId() {
		return fieldId;
	}
	/**
	 * @param fieldId the fieldId to set
	 */
	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
	}
	
	/**
	 * @return the indexEnable
	 */
	public boolean isIndexEnable() {
		return indexEnable;
	}
	/**
	 * @param indexEnable the indexEnable to set
	 */
	public void setIndexEnable(boolean indexEnable) {
		this.indexEnable = indexEnable;
	}
	/**
	 * @return the indexType
	 */
	public int getIndexType() {
		return indexType;
	}
	/**
	 * @param indexType the indexType to set
	 */
	public void setIndexType(int indexType) {
		this.indexType = indexType;
	}
	
}
