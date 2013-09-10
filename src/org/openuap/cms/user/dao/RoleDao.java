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
package org.openuap.cms.user.dao;

import java.util.List;

import org.openuap.base.dao.hibernate.PolymorphicDao;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUserRole;

/**
 * <p>
 * 角色Dao接口.
 * </p>
 * 
 * <p>
 * $Id: RoleDao.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface RoleDao extends PolymorphicDao {

	public long addRole(IRole role);

	public void saveRole(IRole role);

	public void deleteRole(IRole role);

	public void deleteRoleById(long roleId);

	public IRole getRoleById(long roleId);
	
	public IRole getRoleByGuid(String guid);

	public List getUserRoles(long userId);

	public List getUserRoles(long userId, long start, long limit, String where, String order, PageBuilder pb);

	public List getRoleUsers(long roleId);

	public List getRoleUsers(long roleId, long start, long limit, String where, String order, PageBuilder pb);

	public List getAllRole();

	public List getRoles(long start, long length, String where, String order, PageBuilder pb);

	public int getAllRoleCount();

	public int getUserRoleCount(long userId);

	public int getRoleUserCount(long roleId);

	public void saveUserRole(IUserRole userRole);

	public void deleteUserRole(IUserRole userRole);

	public void deleteUserRole(long userId, long roleId);

	public void deleteAllRole(long userId);

	public void deleteAllUser(long roleId);
}
