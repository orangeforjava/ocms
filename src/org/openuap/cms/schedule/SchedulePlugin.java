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
package org.openuap.cms.schedule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openuap.plugin.PluginClassLoader;
import org.openuap.plugin.PluginManager;
import org.openuap.plugin.registry.Extension;
import org.openuap.plugin.registry.ExtensionPoint;
import org.openuap.plugin.registry.Extension.Parameter;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * <p>计划任务插件</p>
 * $Id: SchedulePlugin.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @author Joseph
 * 
 */
public class SchedulePlugin extends WebApplicationPlugin {
	
	public static final String PLUGIN_ID = "org.openuap.schedule";
	
	public static final String APP_CONTEXT_EXTENSION_POINT = "app-context";

	public static final String WEB_CONTEXT_EXTENSION_POINT = "web-context";

	public static final String RESOURCE_DEPLOY_FILE_PATH = "resource-deploy-file";

	public static final String HOME_RESOURCE_DEPLOY_FILE_PATH = "home-resource-deploy-file";
	
	public static final String PLUGIN_HOME_RESOURCE_DEPLOY_FILE_PATH = "plugin-home-resource-deploy-file";

	public void refreshApplicationContext() {
		PluginManager pluginManager = WebPluginManagerUtils
				.getPluginManager(this.getServletContext());
		String[] contextPaths;
		List<String> ctxPaths = new ArrayList<String>();
		if (pluginManager != null) {
			try {
				// 获得boot应用扩展点
				long s1 = System.currentTimeMillis();
				ExtensionPoint applicationExtPoint = pluginManager
						.getRegistry().getExtensionPoint(PLUGIN_ID,
								APP_CONTEXT_EXTENSION_POINT);
				for (Iterator<Extension> it = applicationExtPoint
						.getConnectedExtensions().iterator(); it.hasNext();) {
					Extension ext = (Extension) it.next();
					//
					Parameter param = ext.getParameter("path");
					//
					String path = param.valueAsString();
					String[] paths = path.split(",");
					for (int j = 0; j < paths.length; j++) {
						ctxPaths.add(paths[j]);
					}
				}
				contextPaths = new String[ctxPaths.size()];
				contextPaths = ctxPaths.toArray(new String[ctxPaths.size()]);
				//
				long e1 = System.currentTimeMillis();
				long t1 = e1 - s1;
				System.out.println("寻找上下文定义耗时:" + t1 + "豪秒");
				PluginClassLoader pcl = getManager().getPluginClassLoader(
						getDescriptor());
				this.applicationContext = new XmlWebApplicationContext();
				this.applicationContext.setServletContext(this
						.getServletContext());
				if (parentContext != null) {
					applicationContext.setParent(parentContext);
				}
				if (pcl != null) {
					applicationContext.setClassLoader(pcl);
				}
				//
				applicationContext.setConfigLocations(contextPaths);
				applicationContext.refresh();
				long e2 = System.currentTimeMillis();
				long t2 = e2 - e1;
				System.out.println("初始化上下文定义耗时:" + t2 + "豪秒");
				this.getServletContext().setAttribute(this.getPluginId(),
						applicationContext);
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
		//
		if (applicationContext != null) {
			this.updateSchema();
		}
	}

	public void refreshServletApplicationContext() {
		PluginManager pluginManager = WebPluginManagerUtils
				.getPluginManager(this.getServletContext());
		String[] contextPaths;
		List<String> ctxPaths = new ArrayList<String>();
		if (pluginManager != null) {
			try {
				// 获得扩展点
				ExtensionPoint applicationExtPoint = pluginManager
						.getRegistry().getExtensionPoint(PLUGIN_ID,
								WEB_CONTEXT_EXTENSION_POINT);
				for (Iterator<Extension> it = applicationExtPoint
						.getConnectedExtensions().iterator(); it.hasNext();) {
					Extension ext = it.next();
					//
					Parameter param = ext.getParameter("path");
					//
					String path = param.valueAsString();
					String[] paths = path.split(",");
					for (int j = 0; j < paths.length; j++) {
						ctxPaths.add(paths[j]);
					}
				}
				contextPaths = new String[ctxPaths.size()];
				contextPaths = ctxPaths.toArray(new String[ctxPaths.size()]);
				//
				WebApplicationContext applicationContext = this
						.getApplicationContext();
				if (applicationContext == null) {
					applicationContext = this.servletParentContext;
				}
				PluginClassLoader pcl = getManager().getPluginClassLoader(
						getDescriptor());
				servletAplicationContext = new XmlWebApplicationContext();
				servletAplicationContext.setServletContext(this
						.getServletContext());
				if (applicationContext != null)
					servletAplicationContext.setParent(applicationContext);
				if (pcl != null)
					servletAplicationContext.setClassLoader(pcl);
				//
				servletAplicationContext.setConfigLocations(contextPaths);
				servletAplicationContext.refresh();
				// this.getServletContext().setAttribute(this.getPluginId(),
				// servletAplicationContext);

			} catch (Exception ex1) {
				this.log.error(ex1);
				ex1.printStackTrace();
			}
		}
	}
	public String getPluginId() {
		return PLUGIN_ID;
	}
}
