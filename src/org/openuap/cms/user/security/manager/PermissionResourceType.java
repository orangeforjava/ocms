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
package org.openuap.cms.user.security.manager;

/**
 * <p>
 * 权限资源
 * </p>
 * 
 * <p>
 * $Id: PermissionResourceType.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PermissionResourceType implements java.io.Serializable{

	public static final String ABSTRACT_PERMISSION_TYPE="0";
	public static final String CONCRETE_PERMISSION_TYPE="1";
	/**
	 * 
	 */
	private static final long serialVersionUID = -1510623016277907468L;
	/** 类型识别，作为整型区分,资源类型标识数字,这个必须唯一. */
	private String key;
	/** 类型英文名，为了兼容原来的文件定义方式,原来要求唯一. */
	private String name;
	/** 用来标识是否是具体内容类型. */
	private String type;
	/** 资源类型的显示名称. */
	private String title;
	/** 资源的反射代理.*/
	private IResourceReflection resourceReflection;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public IResourceReflection getResourceReflection() {
		return resourceReflection;
	}

	public void setResourceReflection(IResourceReflection resourceReflection) {
		this.resourceReflection = resourceReflection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PermissionResourceType other = (PermissionResourceType) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}
