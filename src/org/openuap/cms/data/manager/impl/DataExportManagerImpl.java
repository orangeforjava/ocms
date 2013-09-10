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
package org.openuap.cms.data.manager.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.data.DataExporter;
import org.openuap.cms.data.manager.AbstractDataManager;
import org.openuap.cms.data.manager.DataExportManager;
import org.openuap.plugin.PluginManager;
import org.openuap.plugin.registry.Extension;
import org.openuap.plugin.registry.ExtensionPoint;
import org.openuap.plugin.registry.Extension.Parameter;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * 数据备份管理者实现
 * </p>
 * 
 * <p>
 * $Id: DataExportManagerImpl.java 3947 2010-11-01 14:23:26Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DataExportManagerImpl extends AbstractDataManager implements
		DataExportManager, InitializingBean {

	private List<DataExporter> exporters = new ArrayList<DataExporter>();

	private Map<String, DataExporter> exporterMap = new HashMap<String, DataExporter>();

	private boolean inited = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openuap.cms.data.manager.DataExportManager#exportAllData()
	 */
	public void exportAllData() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openuap.cms.data.manager.DataExportManager#exportData(org.openuap
	 * .cms.data.model.DataExporter)
	 */
	public void exportData(DataExporter exporter) {
		Map parameters = new HashMap();
		exporter.exportData(parameters);
	}

	public void exportData(String name) {
		DataExporter exporter = exporterMap.get(name);
		if (exporter != null) {
			Map parameters = new HashMap();
			exporter.exportData(parameters);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openuap.cms.data.manager.DataExportManager#getDataExporters()
	 */
	public List getDataExporters() {
		//
		refreshData();
		return this.exporters;
	}

	public void refreshData() {
		try {
			exporters.clear();
			exporterMap.clear();
			//
			PluginManager pluginManager = WebPluginManagerUtils
					.getPluginManager("");
			//
			File dataDir = this.getDataDir();
			//
			ExtensionPoint dataExportPoint = pluginManager.getRegistry()
					.getExtensionPoint(CmsPlugin.PLUGIN_ID,
							CmsPlugin.EXPORT_DATA_EXTENSION_POINT);
			// System.out.println("dataExportPoint="+dataExportPoint);
			for (Iterator it = dataExportPoint.getConnectedExtensions()
					.iterator(); it.hasNext();) {
				// 获得扩展了此扩展点的扩展
				Extension ext = (Extension) it.next();
				// 获得此扩展插件Id
				String pluginId = ext.getDeclaringPluginDescriptor().getId();
				Parameter nameParam = ext
						.getParameter(CmsPlugin.EXPORT_DATA_NAME_PARAM);
				Parameter titleParam = ext
						.getParameter(CmsPlugin.EXPORT_DATA_TITLE_PARAM);
				Parameter beanParam = ext
						.getParameter(CmsPlugin.EXPORT_DATA_BEAN_PARAM);
				if (beanParam != null && nameParam != null
						&& titleParam != null) {
					String beanName = beanParam.valueAsString();
					String title = titleParam.valueAsString();
					String name = nameParam.valueAsString();
					// 获得以bean名称命名的导出对象
					Object oexporter = ObjectLocator.lookup(beanName, pluginId);
					if (oexporter != null && oexporter instanceof DataExporter) {
						DataExporter exporter = (DataExporter) oexporter;
						//
						exporter.setName(name);
						exporter.setTitle(title);
						exporter.setDataDir(dataDir);
						exporters.add(exporter);
						exporterMap.put(name, exporter);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<DataExporter> getExporters() {
		return exporters;
	}

	public void setExporters(List<DataExporter> exporters) {
		this.exporters = exporters;
	}

	public Map<String, DataExporter> getExporterMap() {
		return exporterMap;
	}

	public void setExporterMap(Map<String, DataExporter> exporterMap) {
		this.exporterMap = exporterMap;
	}

	public void afterPropertiesSet() throws Exception {
		refreshData();
		inited = true;
	}

}
