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
package org.openuap.cms.util.ui;

/**
 * <p>
 * 文件项
 * </p>
 * 
 * 
 * <p>
 * $Id: FileItem.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class FileItem {
	private String name;
	private boolean dir;
	private String type;
	private String icon;

	public FileItem() {
	}

	public FileItem(boolean dir, String name, String type, String icon) {
		this.dir = dir;
		this.name = name;
		this.type = type;
		this.icon = icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDir(boolean dir) {
		this.dir = dir;
	}

	public String getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isDir() {
		return dir;
	}
}
