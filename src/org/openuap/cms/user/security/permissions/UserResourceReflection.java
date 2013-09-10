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
package org.openuap.cms.user.security.permissions;

import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.manager.IResourceReflection;

/**
 * <p>
 * 用户资源反射，提供用户资源的权限信息
 * </p>
 * 
 * <p>
 * $Id: UserResourceReflection.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserResourceReflection implements IResourceReflection {

	private IUserManager userManager;
	private String resourceSelectionUrl;
	private String caption;
	
	public UserResourceReflection() {

		this.resourceSelectionUrl = "admin/user.jhtml?action=SelUserList";
		caption="用户选择";
	}

	public String getResourceDescription(String id) {
		if (userManager != null && id != null) {
			IUser user = userManager.getUserById(new Long(id));
			return user.getName();
		}
		return id;
	}

	public String getResourceSelectionUrl() {
		return resourceSelectionUrl;
	}

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

	public void setResourceSelectionUrl(String resourceSelectionUrl) {
		this.resourceSelectionUrl = resourceSelectionUrl;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

}
