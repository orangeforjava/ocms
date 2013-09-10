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
import java.util.Map;

import org.openuap.cms.cm.dao.ContentFieldDao;
import org.openuap.cms.cm.model.ContentField;

/**
 * <p>
 * 内容属性管理.
 * </p>
 * 
 * <p>
 * $Id: ContentFieldManager.java 3963 2010-12-06 14:56:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ContentFieldManager {

	public void setContentFieldDao(ContentFieldDao dao);

	/**
	 * 添加属性
	 * 
	 * @param 内容属性
	 * @return
	 */
	public Long addContentField(ContentField contentField);

	/**
	 * 保存属性
	 * 
	 * @param 内容属性
	 */
	public void saveContentField(ContentField contentField);

	/**
	 * 删除属性
	 * 
	 * @param 属性id
	 */
	public void deleteContentField(Long id);

	/**
	 * 删除指定内容模型的属性
	 * 
	 * @param 内容id
	 */
	public void deleteContentFieldOfTable(Long tableId);

	/**
	 * 获得指定内容模型的所有属性
	 * 
	 * @param tableId
	 *            内容id
	 * @return
	 */
	public List<ContentField> getAllContentField(Long tableId);

	/**
	 * 获得指定内容模型的属性
	 * 
	 * @param 内容id
	 * @param 排序条件
	 * @return 内容属性列表
	 */
	public List<ContentField> getContentFieldOfTable(Long tableId,
			String orderby);

	/**
	 * 根据id获得内容属性
	 * 
	 * @param 属性id
	 * @return 内容属性
	 */
	public ContentField getContentFieldById(Long fieldId);

	/**
	 * 根据指定内容id与属性名获得内容属性
	 * 
	 * @param tableId
	 *            内容id
	 * @param fieldName
	 *            属性名
	 * @return 内容属性
	 */
	public ContentField getContentFieldByName(Long tableId, String fieldName);

	/**
	 * 获得指定内容包含的属性数目
	 * 
	 * @param tableId
	 *            内容id
	 * @return 属性数目
	 */
	public long getContentFieldCount(Long tableId);

	/**
	 * 
	 * @param fieldId
	 * @param field
	 * @param value
	 */
	public void updateStatus(Long fieldId, String field, Object value);

	public void updateStatusOfTable(Long tableId, String field, Object value);

	/**
	 * 获得标题属性
	 * 
	 * @param 内容id
	 * @return 内容属性
	 */
	public ContentField getTitleField(Long tableId);

	/**
	 * 获得首图属性
	 * 
	 * @param 内容id
	 * @return 内容属性
	 */
	public ContentField getPhotoField(Long tableId);

	/**
	 * 获得主内容属性
	 * 
	 * @param 内容id
	 * @return
	 */
	public ContentField getMainField(Long tableId);

	/**
	 * 获得可搜索的字段
	 * 
	 * @param tableId
	 * @return
	 */
	public List<ContentField> getSearchFields(Long tableId);

	/**
	 * 获得关键字字段
	 * 
	 * @param tableId
	 * @return
	 */
	public ContentField getKeywordsField(Long tableId);

	public List<ContentField> getContentFieldsFromCache(Long tableId);

	/**
	 * 从缓存中获得标题属性
	 * 
	 * @param 内容id
	 * @return 内容属性
	 */
	public ContentField getTitleFieldFromCache(Long tableId);

	/**
	 * 从缓存中获得首图属性
	 * 
	 * @param 内容id
	 * @return 内容属性
	 */
	public ContentField getPhotoFieldFromCache(Long tableId);

	/**
	 * 从缓存中获得主内容属性
	 * 
	 * @param 内容id
	 * @return
	 */
	public ContentField getMainFieldFromCache(Long tableId);

	/**
	 * 从缓存中获得可搜索的字段
	 * 
	 * @param tableId
	 * @return
	 */
	public List<ContentField> getSearchFieldsFromCache(Long tableId);

	/**
	 * 从缓存中获得关键字字段
	 * 
	 * @param tableId
	 * @return
	 */
	public ContentField getKeywordsFieldFromCache(Long tableId);
	/**
	 * 获得内容属性域Map
	 * @param alias 别名
	 * @return
	 */
	public Map<String,ContentField> getFieldsMap(String alias);
}
