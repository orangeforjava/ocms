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

import java.util.List;
import java.util.Map;

import org.openuap.cms.cm.cache.ContentModelCache;
import org.openuap.cms.cm.cache.ContentModelQuickQuery;
import org.openuap.cms.cm.dao.ContentFieldDao;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.model.ContentField;

/**
 * <p>
 * 内容域管理DB实现.
 * </p>
 * 
 * <p>
 * $Id: ContentFieldManagerDBImpl.java 3963 2010-12-06 14:56:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentFieldManagerDBImpl implements ContentFieldManager {

	private ContentFieldDao contentFieldDao;

	public ContentFieldManagerDBImpl() {
	}

	public void setContentFieldDao(ContentFieldDao dao) {
		this.contentFieldDao = dao;
	}

	public Long addContentField(ContentField contentField) {
		return contentFieldDao.addContentField(contentField);
	}

	public void saveContentField(ContentField contentField) {
		contentFieldDao.saveContentField(contentField);
	}

	public void deleteContentField(Long id) {
		contentFieldDao.deleteContentField(id);
	}

	public void deleteContentFieldOfTable(Long tableId) {
		contentFieldDao.deleteContentFieldOfTable(tableId);
	}

	public List<ContentField> getAllContentField(Long tableId) {
		return contentFieldDao.getAllContentField(tableId);
	}

	public ContentField getContentFieldById(Long fieldId) {
		return contentFieldDao.getContentFieldById(fieldId);
	}

	public ContentField getContentFieldByName(Long tableId, String fieldName) {
		return contentFieldDao.getContentFieldByName(tableId, fieldName);
	}

	public long getContentFieldCount(Long tableId) {
		return contentFieldDao.getContentFieldCount(tableId);
	}

	public void updateStatus(Long fieldId, String field, Object value) {
		contentFieldDao.updateStatus(fieldId, field, value);
	}

	public void updateStatusOfTable(Long tableId, String field, Object value) {
		contentFieldDao.updateStatusOfTable(tableId, field, value);
	}

	public ContentField getTitleField(Long tableId) {
		return contentFieldDao.getTitleField(tableId);
	}

	public List<ContentField> getContentFieldOfTable(Long tableId,
			String orderby) {
		return contentFieldDao.getContentFieldOfTable(tableId, orderby);
	}

	public ContentField getPhotoField(Long tableId) {
		return contentFieldDao.getPhotoField(tableId);
	}

	public ContentField getMainField(Long tableId) {
		return contentFieldDao.getMainField(tableId);
	}

	public List<ContentField> getSearchFields(Long tableId) {
		return contentFieldDao.getSearchFields(tableId);
	}

	public ContentField getKeywordsField(Long tableId) {
		return contentFieldDao.getKeywordsField(tableId);
	}

	public List<ContentField> getContentFieldsFromCache(Long tableId) {
		List<ContentField> fields = ContentModelCache.getContentFields(tableId);
		return fields;
	}

	public ContentField getKeywordsFieldFromCache(Long tableId) {
		List fields = getContentFieldsFromCache(tableId);
		return ContentModelQuickQuery.getKeywordsField(fields);
	}

	public ContentField getMainFieldFromCache(Long tableId) {
		List fields = getContentFieldsFromCache(tableId);
		return ContentModelQuickQuery.getMainField(fields);
	}

	public ContentField getPhotoFieldFromCache(Long tableId) {
		List fields = getContentFieldsFromCache(tableId);
		return ContentModelQuickQuery.getPhotoField(fields);
	}

	public List<ContentField> getSearchFieldsFromCache(Long tableId) {
		List fields = getContentFieldsFromCache(tableId);
		return ContentModelQuickQuery.getSearchFields(fields);
	}

	public ContentField getTitleFieldFromCache(Long tableId) {
		List fields = getContentFieldsFromCache(tableId);
		return ContentModelQuickQuery.getTitleField(fields);
	}

	public Map<String, ContentField> getFieldsMap(String alias) {
		// TODO Auto-generated method stub
		return ContentModelCache.getFieldsMap(alias);
	}
}
