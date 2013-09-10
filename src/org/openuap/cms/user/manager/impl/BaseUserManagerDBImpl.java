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

import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.AbstractUser;
import org.openuap.cms.user.model.BaseUser;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.IUserSession;
import org.openuap.cms.user.security.UserSession;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * 基础用户管理实现.
 * </p>
 * 
 * 
 * <p>
 * $Id: BaseUserManagerDBImpl.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class BaseUserManagerDBImpl extends AbstractUserManagerImpl implements IUserManager, ApplicationListener, ApplicationContextAware {

	public AbstractUser createUser() {
		return new BaseUser();
	}

	public IUserSession getUserSession(IUser user) {
		UserSession userSession=new UserSession(user);
		return userSession;
	}

	

	

	
}
