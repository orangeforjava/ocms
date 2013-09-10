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
package org.openuap.cms.user.security.manager.support;

import java.util.List;

import org.openuap.cms.user.security.manager.IResourceReflection;
import org.openuap.cms.user.security.manager.PermissionResourceItem;
import org.openuap.cms.user.security.manager.PermissionResourceType;
import org.openuap.cms.user.security.manager.ResourcePermissionControlPanel;

/**
 * <p>
 * 缺省资源权限控制面板
 * </p>
 * 
 * <p>
 * $Id: DefaultResourcePermissionControlPanel.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultResourcePermissionControlPanel implements
		ResourcePermissionControlPanel {

	private List<PermissionResourceItem> permissionResourceItems;
	private PermissionResourceType permissionResourceType;
	private IResourceReflection resourceReflection;
	public List<PermissionResourceItem> getPermissionResourceItems() {
		return permissionResourceItems;
	}

	public PermissionResourceType getPermissionResourceType() {
		return permissionResourceType;
	}

	public void setPermissionResourceItems(
			List<PermissionResourceItem> permissionResourceItems) {
		this.permissionResourceItems = permissionResourceItems;
	}

	public void setPermissionResourceType(
			PermissionResourceType permissionResourceType) {
		this.permissionResourceType = permissionResourceType;
	}

	public IResourceReflection getResourceReflection() {
		return resourceReflection;
	}

	public void setResourceReflection(IResourceReflection resourceReflection) {
		this.resourceReflection = resourceReflection;
	}

}
