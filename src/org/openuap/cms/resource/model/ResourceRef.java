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
 * 资源引用实体.
 * </p>
 * 
 * <p>
 * $Id: ResourceRef.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class ResourceRef extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6648767435117700261L;

	private int hashValue = 0;

	private ResourceRefKey id;

	/** 内容索引id. */
	private Long indexId;

	/** 结点id. */
	private Long nodeId;

	/** 资源id. */
	private Long resourceId;

	/**
	 * Simple constructor of AbstractCmsResourceRef instances.
	 * 
	 * @param nodeId
	 *            Integer
	 * @param indexId
	 *            Integer
	 * @param resourceId
	 *            Integer
	 */
	public ResourceRef(Long nodeId, Long indexId, Long resourceId) {
		this.nodeId = nodeId;
		this.indexId = indexId;
		this.resourceId = resourceId;
	}

	/**
	 * Constructor of AbstractCmsResourceRef instances given a composite primary
	 * key.
	 */
	public ResourceRef() {

	}

	public ResourceRef(ResourceRefKey id) {
		this.setId(id);
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
		if (!(rhs instanceof ResourceRef)) {
			return false;
		}
		ResourceRef that = (ResourceRef) rhs;
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

	public Long getIndexId() {
		return indexId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public ResourceRefKey getId() {
		return id;
	}

	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public void setId(ResourceRefKey id) {
		this.id = id;
	}

}
