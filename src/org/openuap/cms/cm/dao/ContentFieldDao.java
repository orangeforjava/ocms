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
package org.openuap.cms.cm.dao;

import java.util.List;

import org.openuap.cms.cm.model.ContentField;

/**
 * <p>
 * 内容属性DAO.
 * </p>
 * 
 * 
 * <p>
 * $Id: ContentFieldDao.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ContentFieldDao {

	public Long addContentField(ContentField contentField);

	public void saveContentField(ContentField contentField);

	public void deleteContentField(Long id);

	public void deleteContentFieldOfTable(Long tableId);

	public List<ContentField> getAllContentField(Long tableId);

	public List<ContentField> getContentFieldOfTable(Long tableId, String orderby);

	public ContentField getContentFieldById(Long fieldId);

	public ContentField getContentFieldByName(Long tableId, String fieldName);

	public long getContentFieldCount(Long tableId);

	public ContentField getTitleField(Long tableId);

	public ContentField getPhotoField(Long tableId);

	public ContentField getMainField(Long tableId);

	public ContentField getKeywordsField(Long tableId);

	public void updateStatus(Long fieldId, String field, Object value);

	public void updateStatusOfTable(Long tableId, String field, Object value);

	public List<ContentField> getSearchFields(Long tableId);
}
