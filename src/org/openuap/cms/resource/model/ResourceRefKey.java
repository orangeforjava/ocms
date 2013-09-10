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

/**
 * 
 * <p>
 * 资源引用键实体.
 * </p>
 * 
 * <p>
 * $Id: ResourceRefKey.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class ResourceRefKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8513327330912592969L;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private volatile int hashValue = 0;

	/** The value of the IndexID component of this composite id. */
	private java.lang.Long indexId;

	/** The value of the NodeID component of this composite id. */
	private java.lang.Long nodeId;

	/** The value of the ResourceID component of this composite id. */
	private java.lang.Long resourceId;

	/**
	 * Simple constructor of CmsResourceRefKey instances.
	 */
	public ResourceRefKey() {
	}

	/**
	 * Returns the value of the indexid property.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getIndexId() {

		return indexId;
	}

	/**
	 * Sets the value of the indexid property.
	 * 
	 * @param indexId
	 *            Integer
	 */
	public void setIndexId(Long indexId) {
		hashValue = 0;

		this.indexId = indexId;
	}

	/**
	 * Returns the value of the nodeid property.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getNodeId() {

		return nodeId;
	}

	/**
	 * Sets the value of the nodeid property.
	 * 
	 * @param nodeId
	 *            Integer
	 */
	public void setNodeId(Long nodeId) {
		hashValue = 0;

		this.nodeId = nodeId;
	}

	/**
	 * Returns the value of the resourceid property.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getResourceId() {

		return resourceId;
	}

	/**
	 * Sets the value of the resourceid property.
	 * 
	 * @param resourceId
	 *            Integer
	 */
	public void setResourceId(Long resourceId) {
		hashValue = 0;

		this.resourceId = resourceId;
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * id components.
	 * 
	 * @param rhs
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof ResourceRefKey)) {
			return false;
		}
		ResourceRefKey that = (ResourceRefKey) rhs;
		if (this.getIndexId() == null || that.getIndexId() == null) {
			return false;
		}
		if (!this.getIndexId().equals(that.getIndexId())) {
			return false;
		}
		if (this.getNodeId() == null || that.getNodeId() == null) {
			return false;
		}
		if (!this.getNodeId().equals(that.getNodeId())) {
			return false;
		}
		if (this.getResourceId() == null || that.getResourceId() == null) {
			return false;
		}
		if (!this.getResourceId().equals(that.getResourceId())) {
			return false;
		}
		return true;
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
			int indexidValue = this.getIndexId() == null ? 0 : this
					.getIndexId().hashCode();
			result = result * 37 + indexidValue;
			int nodeidValue = this.getNodeId() == null ? 0 : this.getNodeId()
					.hashCode();
			result = result * 37 + nodeidValue;
			int resourceidValue = this.getResourceId() == null ? 0 : this
					.getResourceId().hashCode();
			result = result * 37 + resourceidValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}
}
