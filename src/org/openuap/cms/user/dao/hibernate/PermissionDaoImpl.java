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
package org.openuap.cms.user.dao.hibernate;

import org.openuap.cms.user.dao.PermissionDao;
import org.openuap.cms.user.model.AbstractRolePermission;
import org.openuap.cms.user.model.AbstractUserPermission;
import org.openuap.cms.user.model.BaseRolePermission;
import org.openuap.cms.user.model.BaseRolePermissionId;
import org.openuap.cms.user.model.BaseUserPermission;
import org.openuap.cms.user.model.BaseUserPermissionId;
import org.openuap.cms.user.model.RolePermissionId;
import org.openuap.cms.user.model.UserPermissionId;


/**
 * <p>
 * 权限DAO实现.
 * </p>
 * 
 * <p>
 * $Id: PermissionDaoImpl.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 *
 */
public class PermissionDaoImpl extends AbstractPermissionDaoImpl implements PermissionDao{

	@Override
	public AbstractRolePermission createNewRolePermission() {
		return new BaseRolePermission();
	}

	@Override
	public RolePermissionId createNewRolePermissionId() {
		return new BaseRolePermissionId();
	}

	@Override
	public UserPermissionId createNewUserPemissionId() {
		return new BaseUserPermissionId();
	}

	@Override
	public AbstractUserPermission createNewUserPermission() {
		return new BaseUserPermission();
	}

}
