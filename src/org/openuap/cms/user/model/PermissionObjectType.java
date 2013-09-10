/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.user.model;

/**
 * <p>
 * 权限对象类型.
 * </p>
 * 
 * <p>
 * $Id: PermissionObjectType.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PermissionObjectType {
	
	/** the permission type key. */
	private String objectType;

	/** the type name. */
	private String name;

	/** the permission title. */
	private String title;

	/** 0-only abstract 1-abstract and concreate */
	private int type;

	public PermissionObjectType() {
	}

	public PermissionObjectType(String objectType, String name, String title, int type) {
		this.objectType = objectType;
		this.title = title;
		this.name = name;
		this.type = type;
	}

	public String getObjectType() {
		return objectType;
	}

	public String getTitle() {
		return title;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj instanceof PermissionObjectType) {
			PermissionObjectType that = (PermissionObjectType) obj;
			if (that.getObjectType().equals(this.getObjectType())) {
				return true;
			}
		}
		return false;
	}
}
