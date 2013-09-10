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
 * <p>
 * 模版变量实体.
 * </p>
 * 
 * <p>
 * $Id: TemplateVar.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class TemplateVar extends BaseObject implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3534207276360145046L;

	private int hashValue = 0;

	//
	private long id;

	private String title;

	private String name;

	private String value;

	private boolean global;

	private String nodeScope;

	public TemplateVar() {
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof TemplateVar)) {
			return false;
		}
		TemplateVar that = (TemplateVar) o;
		if (this.getId() == 0 || that.getId() == 0) {
			return false;
		}
		return (this.getId() == (that.getId()));

	}

	/**
	 * 
	 * @return int
	 */
	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int idValue = new Long(this.getId()).hashCode();
			result = result * 37 + idValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public boolean isGlobal() {
		return global;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNodeScope() {
		return nodeScope;
	}

	public String getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setNodeScope(String nodeScope) {
		this.nodeScope = nodeScope;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}
}
