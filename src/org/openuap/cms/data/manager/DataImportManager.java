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
package org.openuap.cms.data.manager;

import java.util.List;

import org.openuap.cms.data.DataImporter;

/**
 * <p>
 * 数据导入管理接口
 * </p>
 * 
 * <p>
 * $Id: DataImportManager.java 3947 2010-11-01 14:23:26Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface DataImportManager extends DataManager {

	public List getDataImporters();
	
	public DataImporter getDateImporter(String name);
	
	public void importData(DataImporter importer);

	public void importAllData();

	public void importData(String name);

	public void importData(String name, String fileName);

	/**
	 * 获得数据文件列表
	 * 
	 * @param name
	 *            插件对象名
	 * @return 数据文件列表
	 */
	public List getDataFiles(String name);
}
