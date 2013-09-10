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
import org.openuap.cms.data.DataImporter;
import org.openuap.cms.data.manager.AbstractDataManager;
import org.openuap.cms.data.manager.DataImportManager;
import org.openuap.plugin.PluginManager;
import org.openuap.plugin.registry.Extension;
import org.openuap.plugin.registry.ExtensionPoint;
import org.openuap.plugin.registry.Extension.Parameter;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 数据导入管理实现
 * </p>
 * 
 * <p>
 * $Id: DataImportManagerImpl.java 3947 2010-11-01 14:23:26Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DataImportManagerImpl extends AbstractDataManager implements DataImportManager {

	private List<DataImporter> importers = new ArrayList<DataImporter>();

	private Map<String, DataImporter> importerMap = new HashMap<String, DataImporter>();

	private boolean inited = false;

	public List getDataFiles(String name) {
		DataImporter importer=importerMap.get(name);
		if(importer!=null){
			return importer.getImportFiles();
		}
		return null;
	}

	public List getDataImporters() {
		refreshData();
		return importers;
	}
	
	public void importAllData() {
		if(importers!=null){
			int size=importers.size();
			for(int i=0;i<size;i++){
				DataImporter importer=importers.get(i);
				Map parameters = new HashMap();
				importer.importData(parameters);
			}
		}

	}

	public void importData(DataImporter importer) {
		Map parameters = new HashMap();
		importer.importData(parameters);
	}

	public void importData(String name) {
		DataImporter importer=importerMap.get(name);
		if(importer!=null){
			importData(importer);
		}
	}

	public void importData(String name, String fileName) {
		DataImporter importer=importerMap.get(name);
		if(importer!=null){
			Map parameters = new HashMap();
			parameters.put("fileName", fileName);
			importer.importData(parameters);
		}

	}

	public void refreshData() {
		try {
			importers.clear();
			importerMap.clear();
			//
			PluginManager pluginManager = WebPluginManagerUtils.getPluginManager("");
			//
			File dataDir = this.getDataDir();
			//
			ExtensionPoint dataExportPoint = pluginManager.getRegistry().getExtensionPoint(CmsPlugin.PLUGIN_ID,
					CmsPlugin.IMPORT_DATA_EXTENSION_POINT);
			//
			for (Iterator it = dataExportPoint.getConnectedExtensions().iterator(); it.hasNext();) {
				// 获得扩展了此扩展点的扩展
				Extension ext = (Extension) it.next();
				// 获得此扩展插件Id
				String pluginId = ext.getDeclaringPluginDescriptor().getId();
				Parameter nameParam = ext.getParameter(CmsPlugin.IMPORT_DATA_NAME_PARAM);
				Parameter titleParam = ext.getParameter(CmsPlugin.IMPORT_DATA_TITLE_PARAM);
				Parameter beanParam = ext.getParameter(CmsPlugin.IMPORT_DATA_BEAN_PARAM);
				if (beanParam != null && nameParam != null && titleParam != null) {
					String beanName = beanParam.valueAsString();
					String title = titleParam.valueAsString();
					String name = nameParam.valueAsString();
					//
					Object oimporter = ObjectLocator.lookup(beanName, pluginId);
					if (oimporter != null && oimporter instanceof DataImporter) {
						DataImporter importer = (DataImporter) oimporter;
						//
						importer.setName(name);
						importer.setTitle(title);
						importer.setDataDir(dataDir);
						importers.add(importer);
						importerMap.put(name, importer);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DataImporter getDateImporter(String name) {
		refreshData();
		return importerMap.get(name);
	}
}
