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

import java.io.Serializable;
import org.openuap.base.dao.hibernate.BaseObject;

/**
 * <p>
 * 模板引用实体.
 * </p>
 * 
 * <p>
 * $Id: TemplateRef.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class TemplateRef extends BaseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3972150147408502422L;

	private TemplateRefId id;

	private Long indexId;

	private Long nodeId;

	private Long templateId;

	private Long templateCateId;

	public TemplateRef(Long indexId, Long templateId) {
		this.indexId = indexId;
		this.templateId = templateId;
		id = new TemplateRefId(indexId, templateId);
	}

	public TemplateRef() {
	}

	public TemplateRef(TemplateRefId id) {
		this.id = id;
	}

	public TemplateRefId getId() {
		return id;
	}

	public Long getIndexId() {
		return indexId;
	}

	public Long getTemplateCateId() {
		return templateCateId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setId(TemplateRefId id) {
		this.id = id;
	}

	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setTemplateCateId(Long templateCateId) {
		this.templateCateId = templateCateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public boolean equals(Object o) {
		if (o instanceof TemplateRef) {
			TemplateRef that = (TemplateRef) o;
			return this.getId().equals(that.getId());
		}
		return false;
	}

	public int hashCode() {
		return this.getId().hashCode();
	}

}
