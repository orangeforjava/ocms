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
package org.openuap.cms.user.manager.impl;

import org.openuap.cms.user.manager.IRoleManager;
import org.openuap.cms.user.model.AbstractUserRole;
import org.openuap.cms.user.model.BaseRole;
import org.openuap.cms.user.model.BaseUserRole;
import org.openuap.cms.user.model.BaseUserRoleId;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.UserRoleId;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * 基础角色管理实现.
 * </p>
 *
 * <p>
 * $Id: BaseRoleManagerDBImpl.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class BaseRoleManagerDBImpl extends AbstractRoleManagerImpl implements
		IRoleManager, ApplicationListener, ApplicationContextAware {

	public AbstractUserRole createUserRole() {
		return new BaseUserRole();
	}

	public UserRoleId createUserRoleId() {
		return new BaseUserRoleId();
	}

	public IRole createRole() {
		return new BaseRole();
	}

}
