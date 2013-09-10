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

import java.util.Set;

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * 
 * <p>
 * 抽象角色类.
 * </p>
 * 
 * <p>
 * $Id: AbstractRole.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractRole extends BaseObject implements IRole,
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9180465226269521821L;

	//
	public static final int SYS_ROLE = 0x1;

	public static final int CUSTOM_ROLE = 0x10;

	//
	private int hashValue = 0;

	private Set users;

	private Long roleId;

	private String name;

	private String title;

	private String description;

	private Long creationDate;

	private Long modificationDate;

	private Integer pos;

	private Integer status;

	private String guid;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public AbstractRole() {
	}

	/**
	 * constructor with id
	 * 
	 * @param roleId
	 * 
	 */
	public AbstractRole(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public Long getModificationDate() {
		return this.modificationDate;
	}

	public Integer getPos() {
		return pos;
	}

	public Integer getStatus() {
		return status;
	}

	public void setModificationDate(Long modificationDate) {
		this.modificationDate = modificationDate;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof AbstractRole)) {
			return false;
		}
		AbstractRole that = (AbstractRole) o;
		if (this.getRoleId() == 0 || that.getRoleId() == 0) {
			return false;
		}
		return (this.getRoleId() == that.getRoleId());

	}

	public int hashCode() {
		if (this.hashValue == 0) {
			Integer result = 17;
			int idValue = this.getRoleId() == 0 ? 0
					: new Long(this.getRoleId()).hashCode();
			result = result * 37 + idValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public Set getUsers() {
		return users;
	}

	public String getTitle() {
		return title;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
