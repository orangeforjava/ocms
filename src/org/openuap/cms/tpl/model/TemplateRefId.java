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

/**
 * <p>
 * 模板引用id
 * </p>
 * 
 * <p>
 * $Id: TemplateRefId.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class TemplateRefId implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2553715173138175080L;

	private Long indexId;

	private Long templateId;

	public TemplateRefId() {
	}

	public TemplateRefId(Long indexId, Long templateId) {
		this.indexId = indexId;
		this.templateId = templateId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof TemplateRefId)) {
			return false;
		}
		TemplateRefId castOther = (TemplateRefId) other;

		return ((this.getIndexId() == castOther.getIndexId()) || (this
				.getIndexId() != null
				&& castOther.getIndexId() != null && this.getIndexId().equals(
				castOther.getIndexId())))
				&& ((this.getTemplateId() == castOther.getTemplateId()) || (this
						.getTemplateId() != null
						&& castOther.getTemplateId() != null && this
						.getTemplateId().equals(castOther.getTemplateId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getIndexId() == null ? 0 : this.getIndexId().hashCode());
		result = 37
				* result
				+ (getTemplateId() == null ? 0 : this.getTemplateId()
						.hashCode());
		return result;
	}

	public Long getIndexId() {
		return indexId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

}
