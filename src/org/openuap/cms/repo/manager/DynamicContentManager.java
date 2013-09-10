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
package org.openuap.cms.repo.manager;

import java.util.List;
import java.util.Map;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.repo.dao.ContentIndexDao;
import org.openuap.cms.repo.dao.DynamicContentDao;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.resource.dao.ResourceRefDao;
import org.openuap.cms.resource.model.ResourceRef;
import org.openuap.cms.util.PageInfo;

/**
 * <p>
 * 动态内容管理器.
 * </p>
 * 
 * <p>
 * $Id: DynamicContentManager.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface DynamicContentManager {

	public void setDynamicContentDao(DynamicContentDao dao);

	public void setContentIndexDao(ContentIndexDao dao);

	public void setResourceRefDao(ResourceRefDao dao);

	/**
	 * add where,order parameter.
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param tableId
	 *            内容表Id
	 * @param where
	 *            查询条件
	 * @param order
	 *            排序方式
	 * @param args
	 *            查询参数
	 * @param start
	 *            开始偏移
	 * @param limit
	 *            记录限制
	 * @param pageInfo
	 *            分页信息
	 * @return 内容列表
	 */
	public List getContentList(Long nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	public List getContentList(String nodeIds, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	/**
	 * get the link list
	 * 
	 * @param contentId
	 * 
	 * @param tableId
	 * 
	 * @param type
	 *            0-virtual,1-actual,2-index
	 * @return List
	 */
	public List getLinkList(Long contentId, Long tableId, Integer type);

	/**
	 * get the content list in recycle bin
	 * 
	 * @param nodeId
	 * 
	 * @param tableId
	 * 
	 * @return List
	 */
	public List getRecycleContentList(Long nodeId, Long tableId);

	/**
	 * 
	 * @param indexId
	 *            内容索引id
	 * 
	 * @param tableId
	 *            内容模型id
	 * 
	 * @return Object
	 */
	public Object getContent(Long indexId, Long tableId);

	/**
	 * 返回动态内容
	 * 
	 * @param indexId
	 *            内容索引ID
	 * @param tableId
	 *            内容模型ID
	 * @return
	 */
	public Map getDynamicContent(Long indexId, Long tableId);

	/**
	 * add the content_index entity will be ContentIndex
	 * 
	 * @param contentIndex
	 * 
	 * @return id
	 */
	public Long addContentIndex(Map contentIndex);

	/**
	 * 
	 * 
	 * @param contentIndex
	 * 
	 * @return Integer
	 */
	public Long addContentIndex(ContentIndex contentIndex);

	/**
	 * save the contentIndex
	 * 
	 * @param contentIndex
	 *            Map
	 */
	public void saveContentIndex(Map contentIndex);

	/**
	 * get the contentIndex by indexId
	 * 
	 * @param indexId
	 *            Integer
	 * @return Map
	 */
	public Map getContentIndexMapById(Long indexId);

	/**
	 * delete the contentIndex,if cascade,delete the content_?,publish_?,?...
	 * 
	 * @param indexId
	 *            Integer
	 * @param tableId
	 *            Integer
	 * @param cascade
	 *            is cascade?
	 */
	public void deleteContentIndex(Long indexId, Long tableId, boolean cascade);

	public void deleteContentIndex(Long indexId, Long tableId, int type);

	/**
	 * add the content by the dynamic model entity will be Content_?(the table
	 * id)
	 * 
	 * @param tableId
	 *            Integer the table id
	 * @param content
	 *            Map
	 * @return Integer
	 */
	public Long addContent(Long tableId, Map content);

	public Long addContent(String tableName, Map content);

	/**
	 * save the content by the dynamic model entity will be Content_?(the table
	 * id)
	 * 
	 * @param tableId
	 *            Integer
	 * @param content
	 *            Map
	 */
	public void saveContent(Long tableId, Map content);

	public void saveContent(String tableName, Map content);

	/**
	 * save the publish content by the dynamic model entity will be
	 * Publish_?(the table id)
	 * 
	 * @param tableId
	 *            Integer
	 * @param publish
	 *            Map
	 */
	public void savePublish(Long tableId, Map publish);

	/**
	 * delete the publish by indexId
	 * 
	 * @param tableId
	 *            Integer
	 * @param indexId
	 *            Integer
	 */
	public void deletePublish(Long tableId, Long indexId);

	public void deleteContent(Long tableId, Long contentId);

	public ContentIndex getContentIndexById(Long contentIndexId);

	public ContentIndex getContentIndexByContentId(Long contentId, Long nodeId);

	public void saveContentIndex(ContentIndex contentIndex);

	public void deleteContentIndex(Long contentIndexId);

	public long getNodePublishContentCount(Long nodeId);

	public long getNodeUnPubllishContentCount(Long nodeId);

	public List getNodePublishContents(Long nodeId, Long start, Long length);

	public List getNodeUnPublishContents(Long nodeId, Long start, Long length);

	// ////////////////////////////////////////////////////////////////////////////
	public long getLongHql(String hql, Object[] args);

	public List getListHql(String hql, Object[] args, QueryInfo qi);

	public Object getObjectHql(String hql, Object[] args);

	// ///////////////////////////////////////////////////////////////////////////
	public List searchContentList(String keywords, String[] fields,
			String published, String[] nodeIds, String time, Long tableId,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	// ////////////////////////////////////////////////////////////////////////////
	public void saveContentTable(Map contentTable);

	// ////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * 
	 * @param nodeId
	 *            Integer
	 * @param oldId
	 *            Integer
	 * @param table
	 *            String
	 * @return ContentIndex
	 */
	public ContentIndex getContentIndexByOldId(Long nodeId, Long oldId,
			String table);

	/**
	 * 
	 * 
	 * @param oldId
	 *            Integer
	 * @param table
	 *            String
	 * @return ContentIndex
	 */
	public ContentIndex getContentIndexByOldId(Long oldId, String table);

	public List getRecycleContentList(Long nodeId, Long tableId, QueryInfo qi,
			PageBuilder pb);

	public List getRecycleContentList(Long nodeId, String tableName,
			QueryInfo qi, PageBuilder pb);

	public List getRecycleContents(Long nodeId);

	public List getListContent(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb);

	/**
	 * 
	 * @param tableName
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getDynamicPublish(String tableName, int start, int limit);

	/**
	 * 获得动态发布对象
	 * 
	 * @param indexId
	 *            内容索引Id
	 * @return 动态发布对象
	 */
	public Map getDynamicPublish(Long indexId);

	public Long getObjectCount(String hql, QueryInfo qi, PageBuilder pb);

	public ContentIndex getContentIndexFromCache(Long contentIndexId);

	/**
	 * 更新内容索引内容
	 * 
	 * @param id
	 * @param fieldName
	 * @param fieldValue
	 */
	public void updateContentIndex(Long id, String fieldName, Object fieldValue);

	/**
	 * <p>
	 * 更新内容索引内容 <br/>
	 * 会同时更新内容的最后修改日期.
	 * </p>
	 * 
	 * @param id
	 * @param fieldName
	 * @param fieldValue
	 * @param where
	 *            附加条件
	 */
	public void updateContentIndex(Long id, String fieldName,
			Object fieldValue, String where);

	/**
	 * 快速获得内容列表 使用新的内容索引对象（冗余设计）为提高性能
	 * 
	 * @param nodeId
	 *            结点id
	 * @param tableId
	 *            内容模型id
	 * @param where
	 *            WHERE条件
	 * @param order
	 *            排序条件
	 * @param args
	 *            查询参数
	 * @param start
	 *            起始行号
	 * @param limit
	 *            限制返回数目
	 * @param pageInfo
	 *            分页信息
	 * @return
	 */
	public List getQuickContentList(Long nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	/**
	 * 快速获得内容列表 使用新的内容索引对象（冗余设计）为提高性能
	 * 
	 * @param nodeIds
	 *            结点id，多个id之间使用","分割
	 * @param tableId
	 * @param where
	 * @param order
	 * @param args
	 * @param start
	 * @param limit
	 * @param pageInfo
	 * @return
	 */
	public List getQuickContentList(String nodeIds, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	public long getAllContentCount(Long nodeId);

	public List getAllContentList(Long nodeId, String tableName, String where,
			String order, Object[] args, Long start, Long limit);

	public List getAllContentList(Long nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit);

	public int executeUpdate(String hql);

	public List<?> executeFind(String hql);

	// ~资源引用相关功能
	/**
	 * 添加资源引用
	 * 
	 * @param resourceRef
	 */
	public void addResourceRef(ResourceRef resourceRef);

	/**
	 * 保存资源引用
	 * 
	 * @param resourceRef
	 */
	public void saveResourceRef(ResourceRef resourceRef);

	/**
	 * 获得资源引用
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            索引Id
	 * @param resourceId
	 *            资源Id
	 * @return 资源引用对象
	 */
	public ResourceRef getResourceRefById(Long nodeId, Long indexId,
			Long resourceId);

	/**
	 * 获得指定内容关联的资源引用列表
	 * 
	 * @param nodeId
	 *            结点id
	 * @param indexId
	 *            内容索引id
	 * @return 对应内容索引的资源列表
	 */
	public List<ResourceRef> getResourceRefByNodeIndexId(Long nodeId,
			Long indexId);

	/**
	 * 删除指定内容关联的所有资源引用
	 * 
	 * @param nodeId
	 *            结点id
	 * @param indexId
	 *            内容索引id
	 */
	public void deleteResourceRefByNodeIndexId(Long nodeId, Long indexId);

	/**
	 * 校正结点统计信息(发布内容数/内容总数)
	 * 
	 * @param nodeId
	 *            结点id
	 */
	public void verifyNodeContentStat(Long nodeId);

	/**
	 * 校正所有结点统计信息(发布内容数/内容总数)，同时自动更新NodeCache
	 */
	public void verifyAllNodeContentStat();

	/**
	 * 获取内容表数据量
	 * 
	 * @param tableId
	 *            模型id
	 * @return
	 */
	public long getDynamictContentCount(long tableId);
}
