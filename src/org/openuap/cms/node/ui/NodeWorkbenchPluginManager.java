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
package org.openuap.cms.node.ui;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openuap.cms.CmsPlugin;
import org.openuap.plugin.Plugin;
import org.openuap.plugin.PluginManager;
import org.openuap.plugin.registry.Extension;
import org.openuap.plugin.registry.ExtensionPoint;
import org.openuap.plugin.registry.PluginDescriptor;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.setup.event.PluginDispatcherInitedEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * 结点关联的插件管理器
 * </p>
 * 
 * <p>
 * $Id: NodeWorkbenchPluginManager.java 3923 2010-10-26 11:50:24Z orangeforjava
 * $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeWorkbenchPluginManager implements ApplicationListener,
		InitializingBean {

	private NodeWorkbench nodeWorkbench;

	public static final String WORKBENCH_UI_EXPOINT_ID = "node-workbench-ui";

	public static final String WORKBENCH_UI_EXPOINT_PATH = "path";

	public static final String WORKBENCH_UI_EXPOINT_NAME = "name";

	private Log log = LogFactory.getLog(NodeWorkbenchPluginManager.class);

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof PluginDispatcherInitedEvent) {
			//
			installNodePluginUI();
		}

	}

	protected void installNodePluginUI() {
		PluginManager pluginManager = WebPluginManagerUtils
				.getPluginManager("base");

		ExtensionPoint extPoint = pluginManager
				.getRegistry()
				.getExtensionPoint(CmsPlugin.PLUGIN_ID, WORKBENCH_UI_EXPOINT_ID);
		try {
			for (Iterator it = extPoint.getConnectedExtensions().iterator(); it
					.hasNext();) {
				// 获得扩展了此扩展点的扩展
				Extension ext = (Extension) it.next();
				// 获得此扩展插件Id
				PluginDescriptor pdesc = ext.getDeclaringPluginDescriptor();
				String id = pdesc.getId();
				Plugin applicationPlugin = pluginManager.getPlugin(id);
				if (applicationPlugin.isSetup()) {
					// 判断插件是否已经安装
					String name = ext.getParameter(WORKBENCH_UI_EXPOINT_NAME)
							.valueAsString();
					// 获得其Workbench ui定义资源
					String path = ext.getParameter(WORKBENCH_UI_EXPOINT_PATH)
							.valueAsString();
					if (path != null) {
						InputStream in = null;
						try {

							in = WebPluginManagerUtils.getServletContext()
									.getResourceAsStream(
											"WEB-INF/config/" + path);
						} catch (Exception e) {
							in = pluginManager.getPluginClassLoader(pdesc)
									.getResourceAsStream(path);
						}
						//
						try {
							SAXReader saxReader = new SAXReader();
							Document doc = saxReader.read(in);
							Element adminElement = (Element) doc
									.selectSingleNode("/workbench");
							// 放入到准备重写的模型中
							nodeWorkbench.addModel(name, adminElement, false);
						} catch (Exception e) {
							e.printStackTrace();
							log
									.error(
											"Failure when parsing main admin-sidebar.xml file",
											e);
						}
						try {
							in.close();
						} catch (Exception ignored) {
							// Ignore.
						}
					}
				}
			}
			// 重建菜单
			nodeWorkbench.rebuildModel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void afterPropertiesSet() throws Exception {
		nodeWorkbench = new NodeWorkbench(
				(WebApplicationPlugin) WebPluginManagerUtils.getPlugin("base",
						CmsPlugin.PLUGIN_ID));
		//
	}

	public NodeWorkbench getNodeWorkbench() {
		return nodeWorkbench;
	}

	public void setNodeWorkbench(NodeWorkbench nodeWorkbench) {
		this.nodeWorkbench = nodeWorkbench;
	}
}
