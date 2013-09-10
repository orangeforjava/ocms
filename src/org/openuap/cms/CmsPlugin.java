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
package org.openuap.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.jcs.engine.control.CompositeCacheManager;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openuap.cms.workbench.ui.Workbench;
import org.openuap.cms.workbench.ui.WorkbenchProperty;
import org.openuap.event.Event;
import org.openuap.plugin.Plugin;
import org.openuap.plugin.PluginManager;
import org.openuap.plugin.registry.Extension;
import org.openuap.plugin.registry.ExtensionPoint;
import org.openuap.plugin.registry.PluginDescriptor;
import org.openuap.plugin.registry.Extension.Parameter;
import org.openuap.runtime.config.ApplicationConfiguration;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.setup.BootstrapUtils;
import org.openuap.runtime.setup.event.PluginDispatcherInitedEvent;

/**
 * <p>
 * CMS插件对象，负责初始化上下文，安装资源
 * </p>
 * 
 * <p>
 * $Id: CmsPlugin.java 4035 2011-04-15 02:16:43Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class CmsPlugin extends WebApplicationPlugin {

	public static final String PLUGIN_ID = "org.openuap.cms";
	/** 后台Workbench-UI扩展点. */
	public static final String WORKBENCH_UI_EXPOINT_ID = "workbench-ui";
	/** 后台Workbench属性扩展点. */
	public static final String WORKBENCH_UI_PROPERTY_EXPOINT_ID = "workbench-ui-property";

	public static final String WORKBENCH_UI_EXPOINT_PATH = "path";

	public static final String WORKBENCH_UI_EXPOINT_NAME = "name";

	public static final String EXPORT_DATA_EXTENSION_POINT = "data-export";
	public static final String IMPORT_DATA_EXTENSION_POINT = "data-import";
	//
	public static final String EXPORT_DATA_NAME_PARAM = "name";
	public static final String EXPORT_DATA_TITLE_PARAM = "title";
	public static final String EXPORT_DATA_BEAN_PARAM = "bean";
	//
	public static final String IMPORT_DATA_NAME_PARAM = "name";
	public static final String IMPORT_DATA_TITLE_PARAM = "title";
	public static final String IMPORT_DATA_BEAN_PARAM = "bean";

	private Workbench workbench = null;
	private WorkbenchProperty workbenchProperty = null;

	public String getPluginId() {

		return PLUGIN_ID;
	}

	protected void doStart() throws Exception {
		super.doStart();
		workbench = new Workbench(this);
		startCache();
	}

	protected void doStop() throws Exception {
		stopCache();
		super.doStop();
	}

	/**
	 * 启动缓存管理器
	 */
	private void startCache() {
		CompositeCacheManager ccm = CompositeCacheManager
				.getUnconfiguredInstance();
		Properties props = new Properties();
		try {
			// 装载缓存配置信息
			String configPath = BootstrapUtils.getBootstrapManager("base")
					.getApplicationConfig().getString("sys.cache.config.path");
			File cacheConfigFile = new File(configPath);
			InputStream inputStream = new FileInputStream(cacheConfigFile);
			props.load(inputStream);
			parseProperties(props);
			ccm.configure(props);
			inputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			this.log.error(ex);
		}
	}

	/**
	 * 停止缓存服务
	 * 
	 * @throws Exception
	 */
	private void stopCache() throws Exception {
		CompositeCacheManager.getInstance().shutDown();
	}

	/**
	 * 解析配置属性
	 * 
	 * @param props
	 */
	private void parseProperties(Properties props) {
		if (props != null) {
			//
			java.util.Enumeration keyEnum = props.propertyNames();
			while (keyEnum.hasMoreElements()) {

				String key = (String) keyEnum.nextElement();
				String value = props.getProperty(key);
				value = evaluate(value);
				props.setProperty(key, value);
			}
		}
	}

	/**
	 * 二次解析配置属性
	 * 
	 * @param value
	 * 
	 * @return String
	 */
	private String evaluate(String value) {
		int pos = value.indexOf("${");
		if (pos < 0) {
			return value;
		}
		int end = value.indexOf("}");
		if (end < pos + 2) {
			return value;
		}
		String alias = value.substring(pos + 2, end).trim();
		//
		String alias_value = null;
		if (alias
				.equalsIgnoreCase(ApplicationConfiguration.DEFAULT_HOME_PROPERTY)) {

			alias_value = BootstrapUtils.getBootstrapManager("base")
					.getUAPHome();
		} else {
			alias_value = BootstrapUtils.getBootstrapManager()
					.getApplicationConfig().getString(alias);
		}
		if (alias_value == null) {
			return value;
		} else {
			value = value.substring(0, pos) + alias_value
					+ value.substring(end + 1);
			return value;
		}
	}

	/**
	 * 
	 */
	public void onPluginEvent(Event event) {
		if (event instanceof PluginDispatcherInitedEvent) {
			//
			installPluginUI();
		}
		this.publishEvent(event);
	}

	/**
	 * 安装后台UI
	 */
	protected void installPluginUI() {
		PluginManager pluginManager = this.getManager();
		ExtensionPoint extPoint = pluginManager.getRegistry()
				.getExtensionPoint(PLUGIN_ID, WORKBENCH_UI_EXPOINT_ID);
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

					String epath = ext.getParameter(WORKBENCH_UI_EXPOINT_PATH)
							.valueAsString();
					if (epath != null) {
						// 修改其可以先从类路径装载，若无法装载到，则从WEB-INFO下装载
						// 并且支持以","分割的多个UI定义文件
						String[] paths = epath.split(",");
						int index = 1;
						for (String path : paths) {
							InputStream in = null;
							try {
								// System.out.println("getServletContext="+getServletContext().getRealPath("WEB-INF/config/"
								// + path+"]"));
								in = this.getServletContext()
										.getResourceAsStream(
												"WEB-INF/config/" + path);
							} catch (Exception e) {
								// e.printStackTrace();

								in = pluginManager.getPluginClassLoader(pdesc)
										.getResourceAsStream(path);
							}
							if (in == null) {
								in = pluginManager.getPluginClassLoader(pdesc)
										.getResourceAsStream(path);
							}
							//
							try {
								SAXReader saxReader = new SAXReader();
								// System.out.println("in="+in);
								Document doc = saxReader.read(in);
								Element adminElement = (Element) doc
										.selectSingleNode("/workbench");
								//
								workbench.addModel(name + "_" + index++,
										adminElement);
							} catch (Exception e) {
								e.printStackTrace();
								log
										.error(
												"Failure when parsing main workbench-ui.xml file",
												e);
							}
							try {
								if (in != null) {
									in.close();
								}
							} catch (Exception ignored) {
								// Ignore.
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		ExtensionPoint propExtPoint = pluginManager.getRegistry()
				.getExtensionPoint(PLUGIN_ID, WORKBENCH_UI_PROPERTY_EXPOINT_ID);
		try {
			Collection extensions = propExtPoint.getConnectedExtensions();
			if (extensions != null && extensions.size() > 0) {
				for (Iterator it = propExtPoint.getConnectedExtensions()
						.iterator(); it.hasNext();) {
					// 获得扩展了此扩展点的扩展
					Extension ext = (Extension) it.next();
					String title = ext.getParameter("title").valueAsString();
					Parameter pDescription = ext.getParameter("description");
					String description = (pDescription == null) ? ""
							: pDescription.valueAsString();
					//
					Parameter pIcon = ext.getParameter("icon");
					String icon = (pIcon == null) ? "" : pIcon.valueAsString();
					String adminTitle = ext.getParameter("admin-title")
							.valueAsString();
					String adminUrl = ext.getParameter("admin-url")
							.valueAsString();
					String appTitle = ext.getParameter("app-title")
							.valueAsString();
					String appUrl = ext.getParameter("app-url").valueAsString();
					String logoutUrl = ext.getParameter("logout-url")
							.valueAsString();
					Parameter pStatus = ext.getParameter("status-url");
					String statusUrl = (pStatus == null) ? "admin/workbench.jhtml?action=Status"
							: pStatus.valueAsString();
					//
					this.workbenchProperty = new WorkbenchProperty();
					this.workbenchProperty.setAdminTitle(adminTitle);
					this.workbenchProperty.setAdminUrl(adminUrl);
					this.workbenchProperty.setDescription(description);
					this.workbenchProperty.setIcon(icon);
					this.workbenchProperty.setAppTitle(appTitle);
					this.workbenchProperty.setAppUrl(appUrl);
					this.workbenchProperty.setLogoutUrl(logoutUrl);
					this.workbenchProperty.setTitle(title);
					this.workbenchProperty.setStatusUrl(statusUrl);

				}
			} else {
				this.workbenchProperty = new WorkbenchProperty();
				this.workbenchProperty.setAdminTitle("OpenUAP CMS 管理后台");
				this.workbenchProperty
						.setAdminUrl("admin/index.jhtml?action=Home");
				this.workbenchProperty.setDescription("");
				this.workbenchProperty.setIcon("");
				this.workbenchProperty.setAppTitle("");
				this.workbenchProperty.setAppUrl("");
				this.workbenchProperty
						.setLogoutUrl("admin/login.jhtml?action=Logout");
				this.workbenchProperty.setTitle("OpenUAP CMS");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WorkbenchProperty getWorkbenchProperty() {
		return workbenchProperty;
	}

	public Workbench getWorkbench() {
		return workbench;
	}
}
