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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openuap.cms.user.security.manager.PermissionPluginManager;
import org.openuap.cms.user.security.manager.PluginPermissionControlPanel;
import org.openuap.plugin.PluginManager;
import org.openuap.plugin.registry.Extension;
import org.openuap.plugin.registry.ExtensionPoint;
import org.openuap.plugin.registry.Extension.Parameter;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;

/**
 * <p>
 * 缺省的基于插件的权限管理器
 * </p>
 * 
 * <p>
 * $Id: DefaultPermissionPluginManager.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultPermissionPluginManager implements PermissionPluginManager {

	private String permissionPluginId = PERMISSION_PLUGIN_ID;
	private String permissionExtensionId = PERMISSION_EXTENSION_ID;
	private String beanIdParam="beanId";

	public String getPermissionPluginId() {
		return permissionPluginId;
	}

	public void setPermissionPluginId(String permissionPluginId) {
		this.permissionPluginId = permissionPluginId;
	}

	public String getPermissionExtensionId() {
		return permissionExtensionId;
	}

	public void setPermissionExtensionId(String permissionExtensionId) {
		this.permissionExtensionId = permissionExtensionId;
	}
	
	public List<PluginPermissionControlPanel> getPluginPermissionControlPanels() {
		//
		PluginManager pluginManager = WebPluginManagerUtils
				.getPluginManager("base");
		ExtensionPoint applicationExtPoint = null;
		List <PluginPermissionControlPanel> cps=new ArrayList<PluginPermissionControlPanel>();
		if (pluginManager != null) {
			try {
				try {
					applicationExtPoint = pluginManager.getRegistry()
							.getExtensionPoint(this.getPermissionPluginId(),
									getPermissionExtensionId());
				} catch (Exception e1) {
				}
				//
				if (applicationExtPoint != null) {
					for (Iterator<Extension> it = applicationExtPoint.getConnectedExtensions().iterator(); it.hasNext();) {
						Extension ext = it.next();
						Parameter param = ext.getParameter(getBeanIdParam());
						String beanId = param.valueAsString();
						WebApplicationPlugin plugin=(WebApplicationPlugin)pluginManager.getPlugin(ext.getDeclaringPluginDescriptor().getId());
						//System.out.println("plugin="+plugin);
						Object bean=plugin.lookup(beanId);
						//System.out.println("beanId="+beanId);
						//System.out.println("bean="+bean);
						if(bean instanceof PluginPermissionControlPanel){
							cps.add((PluginPermissionControlPanel) bean);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cps;
	}

	public String getBeanIdParam() {
		return beanIdParam;
	}

	public void setBeanIdParam(String beanIdParam) {
		this.beanIdParam = beanIdParam;
	}

}
