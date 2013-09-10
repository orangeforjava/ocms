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
import org.openuap.cms.cm.dao.ContentFieldDao;
import org.openuap.cms.cm.model.ContentField;

/**
 * <p>
 * Title: ContentFieldDaoImpl
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
 *  $Id: ContentFieldDaoImpl.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 *
 * @author Joseph
 * @version 1.0
 */
public class ContentFieldDaoImpl extends BaseDaoHibernate implements
		ContentFieldDao {
	public ContentFieldDaoImpl() {
	}

	public Long addContentField(ContentField contentField) {
		return (Long) this.getHibernateTemplate().save(contentField);
	}

	public void saveContentField(ContentField contentField) {
		getHibernateTemplate().saveOrUpdate(contentField);
		getHibernateTemplate().flush();

	}

	public void deleteContentField(Long id) {
		ContentField cf = this.getContentFieldById(id);
		this.getHibernateTemplate().delete(cf);
	}

	public void deleteContentFieldOfTable(Long tableId) {
		List<ContentField> contentFieldList = getAllContentField(tableId);
		this.getHibernateTemplate().deleteAll(contentFieldList);
	}

	public List<ContentField> getAllContentField(Long tableId) {
		return this.executeFind(
				"from ContentField cf where cf.contentTable.tableId=? order by cf.fieldOrder",
				new Object[] { tableId });
	}

	public ContentField getContentFieldById(Long fieldId) {
		return (ContentField) this.findUniqueResult(
				"from ContentField where contentFieldId=?",
				new Object[] { fieldId });
	}

	public ContentField getContentFieldByName(Long tableId, String fieldName) {
		return (ContentField) this.findUniqueResult(
				"from ContentField where tableId=? and fieldName=?",
				new Object[] { tableId, fieldName });
	}

	public long getContentFieldCount(Long tableId) {
		return ((Number) this
				.getHibernateTemplate()
				.iterate(
						"select count(*) from ContentField cf where cf.contentTable.tableId=?",
						new Object[] { tableId }).next()).longValue();
	}

	public void updateStatus(Long fieldId, String field, Object value) {
		this.executeUpdate("update ContentField cf set cf." + field
				+ "=? where cf.contentFieldId=?",
				new Object[] { value, fieldId });
	}

	public void updateStatusOfTable(Long tableId, String field, Object value) {
		this.executeUpdate("update ContentField cf set cf." + field
				+ "=? where cf.contentTable.tableId=?", new Object[] { value,
				tableId });
	}

	public ContentField getTitleField(Long tableId) {
		return (ContentField) this.findUniqueResult(
				"from ContentField where tableId=? and titleField=1",
				new Object[] { tableId });

	}

	public List<ContentField> getContentFieldOfTable(Long tableId, String orderby) {
		return this.executeFind(
				"from ContentField cf where cf.contentTable.tableId=? order by "
						+ orderby, new Object[] { tableId });
	}

	public ContentField getPhotoField(Long tableId) {
		return (ContentField) this.findUniqueResult(
				"from ContentField where tableId=? and photoField=1",
				new Object[] { tableId });

	}

	public ContentField getMainField(Long tableId) {
		return (ContentField) this.findUniqueResult(
				"from ContentField where tableId=? and mainField=1",
				new Object[] { tableId });

	}

	public List<ContentField> getSearchFields(Long tableId) {
		String hql = "from ContentField cf where cf.fieldSearchable=1 and cf.contentTable.tableId=? order by cf.fieldOrder";
		return this.executeFind(hql, new Object[] { tableId });
	}

	public ContentField getKeywordsField(Long tableId) {
		return (ContentField) this.findUniqueResult(
				"from ContentField where tableId=? and keywordsField=1",
				new Object[] { tableId });
	}

}
