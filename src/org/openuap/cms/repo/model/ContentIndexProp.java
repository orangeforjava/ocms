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
package org.openuap.cms.repo.model;

import java.io.Serializable;

/**
 * <p>
 * 内容索引属性Bean
 * </p>
 * <p>
 * $Id: ContentIndexProp.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentIndexProp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6956925009270165501L;

	private Long indexId;

	private String name;

	private String propValue;

	/**
	 * @return the indexId
	 */
	public Long getIndexId() {
		return indexId;
	}

	/**
	 * @param indexId
	 *            the indexId to set
	 */
	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the propValue
	 */
	public String getPropValue() {
		return propValue;
	}

	/**
	 * @param propValue
	 *            the propValue to set
	 */
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((indexId == null) ? 0 : indexId.hashCode());
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ContentIndexProp other = (ContentIndexProp) obj;
		if (indexId == null) {
			if (other.indexId != null)
				return false;
		} else if (!indexId.equals(other.indexId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
