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
package org.openuap.cms.util.sql;

/**
 * <p>
 * 属性类型.
 * </p>
 * 
 * <p>
 * $Id: FieldType.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class FieldType {
	private String title;
	private String name;
	private int type;

	public FieldType() {
	}

	public FieldType(int type, String title, String name) {
		this.type = type;
		this.name = name;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

}
