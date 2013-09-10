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
package org.openuap.cms.security;

import org.openuap.base.util.StringUtil;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.user.security.manager.AbstractPluginPermissionControlPanel;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
/**
 * <p>
 * Cms插件权限控制器面板.
 * </p>
 * 
 * <p>
 * $Id: CmsPluginPermissionControlPanel.java 3920 2010-10-26 11:41:54Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CmsPluginPermissionControlPanel extends
		AbstractPluginPermissionControlPanel {
	/**
	 * 获得权限配置插件目录
	 */
	protected String getPluginHome() {
		WebApplicationPlugin wap = (WebApplicationPlugin) WebPluginManagerUtils
				.getPlugin("base", CmsPlugin.PLUGIN_ID);
		if (wap != null) {
			return StringUtil.normalizePath(wap.getPluginHomePath());
		}
		return null;
	}
	/**
	 * 获取插件
	 */
	protected WebApplicationPlugin getPlugin() {
		WebApplicationPlugin wap = (WebApplicationPlugin) WebPluginManagerUtils
				.getPlugin("base", CmsPlugin.PLUGIN_ID);
		return wap;
	}
}
