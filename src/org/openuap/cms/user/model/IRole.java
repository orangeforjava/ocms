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

/**
 * <p>
 * 角色接口.
 * </p>
 * 
 * 
 * <p>
 * $Id: IRole.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface IRole {
	/**
	 * 获得角色Id
	 * @return
	 */
	public Long getRoleId();
	/**
	 * 设置角色Id
	 * @param roleId
	 */
	public void setRoleId(Long roleId);
	/**
	 * 获得角色名称
	 * @return
	 */
	public String getName();
	/**
	 * 设置角色名称
	 * @param name
	 */
	public void setName(String name);
	
	public String getDescription();

	public void setDescription(String description);

	public Long getCreationDate();

	public void setCreationDate(Long creationDate);

	public Long getModificationDate();

	public Integer getPos();

	public Integer getStatus();

	public void setModificationDate(Long modificationDate);

	public void setPos(Integer sort);

	public void setStatus(Integer status);

	public String getTitle();

	public void setTitle(String title);

	public Set getUsers();

	public void setUsers(Set users);
	
	public String getGuid();
	
	public void setGuid(String guid);

}
