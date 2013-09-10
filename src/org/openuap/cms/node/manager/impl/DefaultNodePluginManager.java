/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.node.manager.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.manager.NodePluginManager;
import org.openuap.cms.node.model.CustomNodeDescriptor;
import org.openuap.plugin.PluginManager;
import org.openuap.plugin.registry.Extension;
import org.openuap.plugin.registry.ExtensionPoint;
import org.openuap.plugin.registry.Extension.Parameter;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 缺省结点插件管理者实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultNodePluginManager.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultNodePluginManager implements NodePluginManager {

	public static final String CUSTOM_NODE_EXTENSION_POINT = "custom-node";

	public List<CustomNodeDescriptor> getCustomNodeDescriptors() {
		PluginManager pluginManager = WebPluginManagerUtils.getPluginManager("base");
		List<CustomNodeDescriptor> customNodeDescriptors = new ArrayList<CustomNodeDescriptor>();
		if (pluginManager != null) {
			ExtensionPoint applicationExtPoint = pluginManager.getRegistry().getExtensionPoint(
					CmsPlugin.PLUGIN_ID, CUSTOM_NODE_EXTENSION_POINT);
			for (Iterator it = applicationExtPoint.getConnectedExtensions().iterator(); it
					.hasNext();) {
				Extension ext = (Extension) it.next();
				//
				Parameter param = ext.getParameter("beanName");
				Parameter iconParam = ext.getParameter("iconPath");

				String beanName = param.valueAsString();
				String iconPath = iconParam.valueAsString();
				Object obj = ObjectLocator.lookup(beanName, ext.getDeclaringPluginDescriptor()
						.getId());
				CustomNodeDescriptor customNodeDescriptor = new CustomNodeDescriptor();
				customNodeDescriptor.setNodeManager((NodeManager) obj);
				customNodeDescriptor.setIconPath(iconPath);
				customNodeDescriptors.add(customNodeDescriptor);
			}
		}
		if (!customNodeDescriptors.isEmpty()) {
			return customNodeDescriptors;
		}
		return null;
	}

}
