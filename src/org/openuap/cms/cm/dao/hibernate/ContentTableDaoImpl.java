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
package org.openuap.cms.cm.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.cm.dao.ContentTableDao;
import org.openuap.cms.cm.model.ContentTable;

/**
 * <p>
 * Title: ContentTableDaoImpl
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * $Id: ContentTableDaoImpl.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentTableDaoImpl extends BaseDaoHibernate implements
		ContentTableDao {
	public ContentTableDaoImpl() {
	}

	public Long addContentTable(ContentTable contentTable) {
		return (Long) this.addObject(contentTable);
	}

	public void saveContentTable(ContentTable contentTable) {
		// getHibernateTemplate().evict(contentTable);
		saveObject(contentTable);
	}

	public void deleteContentTable(Long tableId) {
		ContentTable contentTable = this.getContentTableById(tableId);
		this.deleteObject(contentTable);
	}

	public List<ContentTable> getAllContentTable() {
		return this
				.executeFind("from ContentTable order by system desc,tableId");
	}

	public ContentTable getContentTableById(Long tableId) {
		return (ContentTable) this.findUniqueResult(
				"from ContentTable where tableId=?", new Object[] { tableId });
	}

	public ContentTable getContentTableByName(String name) {
		return (ContentTable) this.findUniqueResult(
				"from ContentTable where name=?", new Object[] { name });
	}

	public long getContentTableCount() {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from ContentTable").next()).longValue();
	}

	public ContentTable getCTByTableName(String tableName) {
		return (ContentTable) this.findUniqueResult(
				"from ContentTable where tableName=?",
				new Object[] { tableName });
	}

	/**
	 * 
	 * @return List
	 */
	public List<ContentTable> getUserContentTables() {
		return this
				.executeFind("from ContentTable where system=0 order by tableId");
	}

	public List<ContentTable> getSysContentTables() {
		return this
				.executeFind("from ContentTable where system=1 order by tableId");
	}

}
