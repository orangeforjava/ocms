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
package org.openuap.cms.user.security.manager;

import java.util.List;

/**
 * <p>
 * 基于插件的权限管理
 * </p>
 * 
 * <p>
 * $Id: PermissionPluginManager.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface PermissionPluginManager {
	
	/** 扩展插件ID. */
	public final static String PERMISSION_PLUGIN_ID = "org.openuap.cms";
	/** 权限扩展点名称常量. */
	public final static String PERMISSION_EXTENSION_ID = "permissions";

	/**
	 * 获得所有扩展了权限扩展点的插件权限控制面板.
	 * 
	 * @return
	 */
	public List<PluginPermissionControlPanel> getPluginPermissionControlPanels();
}
