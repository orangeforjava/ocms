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

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * 
 * <p>
 * 抽象用户角色类.
 * </p>
 * 
 * <p>
 * $Id: AbstractUserRole.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractUserRole extends BaseObject implements IUserRole,
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3672151576865880987L;

	//
	private Long userId;

	private Long roleId;

	private UserRoleId id;

	// Constructors

	/** default constructor */
	public AbstractUserRole() {
	}

	/**
	 * constructor with id
	 * 
	 * @param id
	 *            UserRoleId
	 */
	public AbstractUserRole(UserRoleId id) {
		this.id = id;
	}

	public UserRoleId getId() {
		return this.id;
	}

	public void setId(UserRoleId id) {
		this.id = id;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof AbstractUserRole)) {
			return false;
		}
		AbstractUserRole that = (AbstractUserRole) o;
		if (this.getId() == null || that.getId() == null) {
			return false;
		}
		return (this.getId().equals(that.getId()));

	}

	public int hashCode() {
		return getId().hashCode();
	}

	public Long getRoleId() {
		return roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
