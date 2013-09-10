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
package org.openuap.cms.cm.manager;

import java.util.List;

import org.openuap.cms.cm.dao.ContentTableDao;
import org.openuap.cms.cm.model.ContentTable;

/**
 * 
 * <p>
 * 内容模型管理.
 * </p>

 * 
 * <p>
 * $Id: ContentTableManager.java 3963 2010-12-06 14:56:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ContentTableManager {

	public void setContentTableDao(ContentTableDao dao);

	public Long addContentTable(ContentTable contentTable);

	public void saveContentTable(ContentTable contentTable);

	public void deleteContentTable(Long tableId);

	/**
	 * 得到所有内容表
	 * 
	 * @return 内容表对象列表
	 */
	public List<ContentTable> getAllContentTable();

	public List<ContentTable> getUserContentTables();

	public List<ContentTable> getSysContentTables();

	public ContentTable getContentTableById(Long tableId);

	public ContentTable getContentTableByName(String name);

	public long getContentTableCount();

	public ContentTable getCTByTableName(String tableName);

	public boolean initData();

	/**
	 * 输入内容模型
	 * 
	 * @param name
	 *            String
	 * @return boolean
	 */
	public boolean importModel(String name);

	/**
	 * 输出内容模型
	 * 
	 * @param tableId
	 *            Long
	 * @return boolean
	 */
	public boolean exportModel(Long tableId);
	
	public ContentTable getContentTableFromCache(Long tableId);
	
	public List<ContentTable> getUserContentTablesFromCache();

	public List<ContentTable> getSysContentTablesFromCache();
	
	public List<ContentTable> getAllContentTableFromCache();
	/**
	 * 设置内容模型的索引属性
	 * @param contentTable
	 */
	public void setIndexProp(ContentTable contentTable);
}
