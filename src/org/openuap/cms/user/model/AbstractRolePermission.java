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
package org.openuap.cms.user.model;

/**
 * 
 * <p>
 * 抽象角色权限类.
 * </p>
 * 
 * 
 * <p>
 * $Id: AbstractRolePermission.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractRolePermission extends
		RolePermissionWithFieldsObject implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8213928925861180455L;

	/** 角色Id. */
	private Long roleId;
	/** 权限对象类型. */
	private String objectType;
	/** 权限对象Id. */
	private String objectId;
	/** 角色权限Id. */
	private RolePermissionId id;
	/** 整型权限值. */
	private Long permission;

	/**
	 * 
	 * 
	 */
	public AbstractRolePermission() {
	}

	/**
	 * constructor with id
	 * 
	 * @param id
	 * 
	 */
	public AbstractRolePermission(RolePermissionId id) {
		this.id = id;
	}

	public RolePermissionId getId() {
		return this.id;
	}

	public void setId(RolePermissionId id) {
		this.id = id;
	}

	public Long getPermission() {
		return this.permission;
	}

	public void setPermission(Long permission) {
		this.permission = permission;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof AbstractRolePermission)) {
			return false;
		}
		AbstractRolePermission that = (AbstractRolePermission) o;
		if (this.getId() == null || that.getId() == null) {
			return false;
		}
		return (this.getId().equals(that.getId()));

	}

	public int hashCode() {
		return this.getId().hashCode();
	}

	public String getObjectId() {
		return objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
