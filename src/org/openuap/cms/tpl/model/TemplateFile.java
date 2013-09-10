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

import java.util.Date;

/**
 * <p>
 * 模板文件实体.
 * </p>
 * 
 * <p>
 * $Id: TemplateFile.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class TemplateFile implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2997573581723131285L;

	private String name;

	private String path;

	private String icon;

	private long size;

	private Date createDate;

	private String property;

	private boolean folder;

	private boolean existChildren;

	private String encodedPath;

	public TemplateFile() {
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getIcon() {
		return icon;
	}

	public String getProperty() {
		return property;
	}

	public long getSize() {
		return size;
	}

	public boolean isFolder() {
		return folder;
	}

	public boolean isExistChildren() {
		return existChildren;
	}

	public String getEncodedPath() {
		return encodedPath;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public void setExistChildren(boolean existChildren) {
		this.existChildren = existChildren;
	}

	public void setEncodedPath(String encodedPath) {
		this.encodedPath = encodedPath;
	}
}
