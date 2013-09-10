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
package org.openuap.cms.cm.manager.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.cache.ContentModelCache;
import org.openuap.cms.cm.dao.ContentFieldDao;
import org.openuap.cms.cm.dao.ContentTableDao;
import org.openuap.cms.cm.event.ContentModelEvent;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.cm.model.ContentTables;
import org.openuap.cms.config.CMSConfig;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * :内容表管理DB实现.
 * </p>
 * 
 * <p>
 * $Id: ContentTableManagerDBImpl.java 3963 2010-12-06 14:56:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentTableManagerDBImpl implements ContentTableManager, ApplicationListener {

	private static final String INIT_DATA_FILE_NAME = "contentTableData";

	private ContentTableDao contentTableDao;

	private ContentFieldDao contentFieldDao;

	public ContentTableManagerDBImpl() {
	}

	public void setContentTableDao(ContentTableDao dao) {
		this.contentTableDao = dao;
	}

	public void setContentFieldDao(ContentFieldDao contentFieldDao) {
		this.contentFieldDao = contentFieldDao;
	}

	public Long addContentTable(ContentTable contentTable) {
		return contentTableDao.addContentTable(contentTable);
	}

	public void saveContentTable(ContentTable contentTable) {
		this.contentTableDao.saveContentTable(contentTable);
	}

	public void deleteContentTable(Long tableId) {
		contentTableDao.deleteContentTable(tableId);
	}

	public List<ContentTable> getAllContentTable() {
		return contentTableDao.getAllContentTable();
	}

	public ContentTable getContentTableById(Long tableId) {
		return contentTableDao.getContentTableById(tableId);
	}

	public ContentTable getContentTableByName(String name) {
		return contentTableDao.getContentTableByName(name);
	}

	public long getContentTableCount() {
		return contentTableDao.getContentTableCount();
	}

	/**
	 * 
	 * @param tableName
	 *            String
	 * @return ContentTable
	 */
	public ContentTable getCTByTableName(String tableName) {
		return contentTableDao.getCTByTableName(tableName);
	}

	public List<ContentTable> getUserContentTables() {
		return contentTableDao.getUserContentTables();
	}

	public List<ContentTable> getSysContentTables() {
		return contentTableDao.getSysContentTables();
	}

	/**
	 * 初始化固定的数据模型
	 * 
	 * @return boolean
	 */
	public boolean initData() {
		XStream xstream = new XStream();
		ClassLoader cl = WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID);
		if (cl != null) {
			xstream.setClassLoader(cl);
		}
		xstream.alias("contentTables", ContentTables.class);
		xstream.alias("contentTable", ContentTable.class);
		xstream.alias("contentField", ContentField.class);

		//
		try {
			String dataPath = CMSConfig.getInstance().getInitDataPath();
			dataPath = StringUtil.normalizePath(dataPath);
			File dataDir = new File(dataPath);
			if (dataDir.exists() && dataDir.isDirectory()) {
				File[] files = dataDir.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						if (pathname.isFile()) {
							String fileName = pathname.getName();
							if (fileName.startsWith(INIT_DATA_FILE_NAME)) {
								if (fileName.endsWith(".xml")) {
									return true;
								}
							}
						}
						return false;
					}
				});
				if (files != null) {
					for (int index = 0; index < files.length; index++) {
						File file = files[index];
						InputStreamReader in = new InputStreamReader(
								new FileInputStream(file), "UTF-8");
						ContentTable ct = (ContentTable) xstream.fromXML(in);
						if (ct != null) {

							ContentTable old_ct = this.getContentTableByName(ct
									.getName());
							Long tid = null;
							if (old_ct != null) {
								tid = old_ct.getTableId();
								ct.setTableId(tid);
								this.saveContentTable(ct);
							} else {

								tid = this.addContentTable(ct);
								ct.setTableId(tid);
							}
							//
							Collection<ContentField> cfs = ct
									.getContentFieldsSet();

							if (cfs != null && cfs.size() > 0) {
								Iterator<ContentField> cfsIt = cfs.iterator();
								while (cfsIt.hasNext()) {
									Long cfid = null;
									ContentField cf = (ContentField) cfsIt
											.next();
									ContentField cf_old = contentFieldDao
											.getContentFieldByName(tid, cf
													.getFieldName());
									if (cf_old != null) {
										cfid = cf_old.getContentFieldId();
										cf.setContentFieldId(cfid);
										cf.setContentTable(ct);
										contentFieldDao.saveContentField(cf);
									} else {
										cf.setContentTable(ct);
										contentFieldDao.addContentField(cf);
									}
								}

							}
						}
					}
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param tableId
	 *            模型Id
	 * @return boolean
	 */
	public boolean exportModel(Long tableId) {
		//
		XStream xstream = new XStream();
		ClassLoader cl = WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID);
		if (cl != null) {
			xstream.setClassLoader(cl);
		}
		xstream.alias("contentTable", ContentTable.class);
		xstream.alias("contentField", ContentField.class);

		//
		xstream.setMode(XStream.ID_REFERENCES);
		//
		ContentTable ct = getContentTableById(tableId);
		//
		if (ct != null) {

			String tbName = ct.getTableName();
			if (tbName == null || tbName.equals("")) {
				tbName = "cms_content_" + tableId;
			}
			String dataPath = CMSConfig.getInstance().getInitDataPath();
			dataPath = StringUtil.normalizePath(dataPath);
			File dir = new File(dataPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = INIT_DATA_FILE_NAME + "_" + tbName + ".xml";
			File file = new File(dataPath, fileName);
			try {
				OutputStreamWriter out = new OutputStreamWriter(
						new FileOutputStream(file), "UTF-8");

				xstream.toXML(ct, out);
				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public boolean importModel(String name) {
		return false;
	}

	public ContentTable getContentTableFromCache(Long tableId) {
		return ContentModelCache.getContentTable(tableId);
	}

	public List<ContentTable> getSysContentTablesFromCache() {
		return ContentModelCache.getSystemContentTables();
	}

	public List<ContentTable> getUserContentTablesFromCache() {
		return ContentModelCache.getUserContentTables();
	}
	public List<ContentTable> getAllContentTableFromCache() {
		return ContentModelCache.getAllContentTables();
	}
	public void setIndexProp(ContentTable ct) {
		ct.setAllowIndex(1);
		Set<ContentField> fields = ct.getContentFieldsSet();
		if (fields != null) {
			for (ContentField field : fields) {
				String indexType = field.getIndexType();
				String fieldType = field.getFieldType();
				if (!StringUtils.hasText(indexType)
						|| indexType.equalsIgnoreCase(Index.NO.toString())) {
					if (fieldType.equalsIgnoreCase("varchar")
							|| fieldType.equalsIgnoreCase("text")) {
						field.setIndexType(Index.TOKENIZED.toString());
					}else{
						field.setIndexType(Index.UN_TOKENIZED.toString());
					}					
				}
				field.setStoreType(Store.COMPRESS.toString());
				contentFieldDao.saveContentField(field);
			}
		}
		this.saveContentTable(ct);
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContentModelEvent) {
			ContentModelEvent cmEvent = (ContentModelEvent) event;
			String type = cmEvent.getEventType();
			if (type.equals(ContentModelEvent.CM_CREATED)
					|| type.equals(ContentModelEvent.CM_UPDATED)
					|| type.equals(ContentModelEvent.CM_DELETED)) {
				//监听结点变更事件来更新缓存
				ContentModelCache.clear();
			}
		}
		
	}
}
