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
package org.openuap.cms.repo.dao;

import java.util.List;
import java.util.Map;

import org.openuap.base.dao.hibernate.BaseDao;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.util.PageInfo;

/**
 * <p>
 * 动态内容DAO接口.
 * </p>
 * 
 * <p>
 * $Id: DynamicContentDao.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public interface DynamicContentDao extends BaseDao {
	/**
	 * 
	 * 返回Map形式的动态内容列表
	 * <p>
	 * 返回结果格式 List<Object[2]>
	 * <p>
	 * Ojbect[]={ContentIndex Map,Content Map}
	 * <p>
	 * 排序方式:缺省按照置顶，发布日期，状态，排序进行倒排
	 * 
	 * @param nodeId
	 *            内容结点id
	 * @param tableId
	 *            模型Id
	 * @param where
	 *            条件语句
	 * @param order
	 *            排序条件
	 * @param args
	 *            参数值
	 * @param start
	 *            开始记录
	 * @param limit
	 *            记录数
	 * @param pageInfo
	 *            分页信息
	 * @return List
	 */
	public List<?> getContentList(String nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	/**
	 * 返回Map形式的动态内容列表
	 * <p>
	 * 返回结果格式 List<Object[2]>
	 * <p>
	 * Ojbect[]={ContentIndex Map,Content Map}
	 * <p>
	 * 排序方式:缺省按照置顶，发布日期，状态，排序进行倒排
	 * 
	 * @param nodeId
	 *            内容结点id
	 * @param tableName
	 *            模型实体名
	 * @param where
	 *            条件语句
	 * @param order
	 *            排序条件
	 * @param args
	 *            参数值
	 * @param start
	 *            开始记录
	 * @param limit
	 *            记录数
	 * @param pageInfo
	 *            分页信息
	 * @return 返回Map形式的动态内容列表
	 */
	public List<?> getContentList(String nodeId, String tableName, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	/**
	 * 返回Map形式的回收站动态内容列表
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * List:Object[2]
	 * <p>
	 * Ojbect[]={ContentIndex Map,Content Map}
	 * <p>
	 * 排序方式:缺省按照置顶，发布日期，状态，排序进行倒排
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param tableId
	 *            内容模型Id
	 * @return Map形式的回收站动态内容列表
	 */
	public List<Object[]> getRecycleContentList(Long nodeId, Long tableId);

	/**
	 * 返回Map形式的回收站动态内容列表
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * List:Object[2]
	 * <p>
	 * Ojbect[]:{ContentIndex:Map,Content:Map}
	 * <p>
	 * 排序方式:缺省按照置顶，发布日期，状态，排序进行倒排
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param tableName
	 *            内容模型实体名
	 * @return Map形式的回收站动态内容列表
	 */
	public List<Object[]> getRecycleContentList(Long nodeId, String tableName);

	/**
	 * 返回分页Map形式的回收站动态内容列表
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * List:Object[2]
	 * <p>
	 * Ojbect[]:{ContentIndex:Map,Content:Map}
	 * <p>
	 * 排序方式:缺省按照置顶，发布日期，状态，排序进行倒排
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param tableId
	 *            内容模型Id
	 * @param qi
	 *            查询信息
	 * @param pb
	 *            分页信息
	 * @return 分页Map形式的回收站动态内容列表
	 */
	public List<Object[]> getRecycleContentList(Long nodeId, Long tableId,
			QueryInfo qi, PageBuilder pb);

	/**
	 * 返回分页Map形式的回收站动态内容列表
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * List:Object[2]
	 * <p>
	 * Ojbect[]:{ContentIndex:Map,Content:Map}
	 * <p>
	 * 排序方式:缺省按照置顶，发布日期，状态，排序进行倒排
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param tableName
	 *            内容模型实体名
	 * @param qi
	 *            查询信息
	 * @param pb
	 *            分页信息
	 * @return 分页Map形式的回收站动态内容列表
	 */
	public List<Object[]> getRecycleContentList(Long nodeId, String tableName,
			QueryInfo qi, PageBuilder pb);

	/**
	 * 返回Map形式的指定内容对象数组
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * Object[2]:{ContentIndex:Map,Content:Map}
	 * 
	 * @param indexId
	 *            索引Id
	 * @param tableId
	 *            内容模型Id
	 * @return 返回Map形式的指定内容对象数组
	 */
	public Object getContent(Long indexId, Long tableId);

	/**
	 * 返回Map形式的指定内容对象数组
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * Object[2]:{ContentIndex:Map,Content:Map}
	 * 
	 * @param indexId
	 *            内容索引Id
	 * @param tableName
	 *            内容表名
	 * @return 返回Map形式的指定内容对象数组
	 */
	public Object getContent(Long indexId, String tableName);

	/**
	 * 返回Map形式的指定内容本身，不包含索引信息
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * Map:属性名,属性值
	 * <p>
	 * 
	 * @param indexId
	 *            索引Id
	 * @param tableId
	 *            内容表Id
	 * @return Map形式的指定内容本身，不包含索引信息
	 */
	public Map<String, ?> getDynamicContent(Long indexId, Long tableId);

	/**
	 * 返回Map形式的指定内容本身，不包含索引信息
	 * <p>
	 * 返回结果格式 ：
	 * <p>
	 * Map:属性名,属性值
	 * <p>
	 * 
	 * @param indexId
	 *            索引Id
	 * @param tableName
	 *            内容表实体名
	 * @return Map形式的指定内容本身，不包含索引信息
	 */
	public Map<String, ?> getDynamicContent(Long indexId, String tableName);

	/**
	 * 添加Map形式的内容索引
	 * 
	 * @param contentIndex
	 *            Map形式的内容索引对象，格式是属性名->属性值
	 * @return id 新插入对象的Id
	 */
	public Long addContentIndex(Map<String, ?> contentIndex);

	/**
	 * 保存Map形式的内容索引
	 * 
	 * @param contentIndex
	 *            Map形式的内容索引对象，格式是属性名->属性值
	 */
	public void saveContentIndex(Map<String, ?> contentIndex);

	/**
	 * 根据Id获得对应的内容索引对象
	 * 
	 * @param indexId
	 *            内容索引Id
	 * @return Map Map形式的内容索引对象，格式是属性名->属性值
	 */
	public Map<String, ?> getContentIndexMapById(Long indexId);

	/**
	 * 
	 * 删除指定Id的内容索引对象，同时可以选择是否级联删除内容实体相关对象
	 * 
	 * @param indexId
	 *            索引Id
	 * @param tableId
	 *            内容模型Id
	 * @param cascade
	 *            是否级联删除content_?,pubish_?,contribution_?,collection_?
	 */
	public void deleteContentIndex(Long indexId, Long tableId, boolean cascade);

	/**
	 * 
	 * 
	 * @param indexId
	 *            Integer
	 * @param tableId
	 *            Integer
	 * @param tableName
	 *            String
	 * @param cascade
	 *            boolean
	 */
	public void deleteContentIndex(Long indexId, Long tableId,
			String tableName, boolean cascade);

	public void deleteContentIndex(Long indexId, Long tableId, int type);

	/**
	 * 
	 * 
	 * @param indexId
	 * 
	 * @param tableId
	 * 
	 * @param tableName
	 * 
	 * @param type
	 * 
	 */
	public void deleteContentIndex(Long indexId, Long tableId,
			String tableName, int type);

	/**
	 * add the content by the dynamic model entity will be Content_?(the table
	 * id)
	 * 
	 * @param tableId
	 * 
	 * @param content
	 *            Map
	 * @return Integer
	 */
	public Long addContent(Long tableId, Map content);

	/**
	 * 
	 * 
	 * @param tableName
	 *            String
	 * @param content
	 *            Map
	 * @return Integer
	 */
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

	/**
	 * 
	 * 
	 * @param tableName
	 *            String
	 * @param content
	 *            Map
	 */
	public void saveContent(String tableName, Map content);

	/**
	 * save the publish content by dynamic model
	 * 
	 * @param tableId
	 *            Integer
	 * @param publish
	 *            Map
	 */
	public void savePublish(Long tableId, Map publish);

	/**
	 * $Date: 2006/08/31 02:25:58 $ save the publish content by dynamic model
	 * but the publish table name is assigned
	 * 
	 * @param tableName
	 *            String
	 * @param publish
	 *            Map
	 */
	public void savePublish(String tableName, Map publish);

	/**
	 * delete the publish by indexId
	 * 
	 * @param tableId
	 *            Integer
	 * @param indexId
	 *            Integer
	 */
	public void deletePublish(Long tableId, Long indexId);

	/**
	 * Date:2006/01/17 19:49$ delete the publish by indexId but the publish
	 * table name is assigned
	 * 
	 * @param tableName
	 *            String
	 * @param indexId
	 *            Integer
	 */
	public void deletePublish(String tableName, Long indexId);

	/**
	 * delete the content by contentId
	 * 
	 * @param tableId
	 *            Integer
	 * @param contentId
	 *            Integer
	 */
	public void deleteContent(Long tableId, Long contentId);

	/**
	 * $Date: 2006/08/31 02:25:58 $
	 * 
	 * @param tableName
	 *            String
	 * @param contentId
	 *            Integer
	 */
	public void deleteContent(String tableName, Long contentId);

	/**
	 * return the hql result as long
	 * 
	 * @param hql
	 *            String
	 * @param args
	 *            Object[]
	 * @return Integer
	 */
	public long getLongHql(String hql, Object[] args);

	public List getListHql(String hql, Object[] args, QueryInfo qi);

	public Object getObjectHql(String hql, Object[] args);

	public List searchContentList(String keywords, String[] fields,
			String published, String[] nodeIds, String time, Long tableId,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	public List searchContentList(String keywords, String[] fields,
			String published, String[] nodeIds, String time, String tableName,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	/**
	 * 
	 * 
	 * @param contentId
	 *            Integer
	 * @param tableId
	 *            Integer
	 * @param type
	 *            0-virtual,1-actual,2-index
	 * @return List
	 */
	public List getLinkList(Long contentId, Long tableId, Integer type);

	/**
	 * 
	 * 
	 * @param contentId
	 *            内容id
	 * @param tableId
	 *            模型id
	 * @param tableName
	 *            模型名
	 * @param type
	 *            类型
	 * @return List
	 */
	public List getLinkList(Long contentId, Long tableId, String tableName,
			Integer type);

	/**
	 * 
	 * 保存内容模型对象
	 * 
	 * @param contentTable
	 *            内容模型
	 */
	public void saveContentTable(Map contentTable);

	public List getListContent(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb);

	/**
	 * 获得动态发布
	 * 
	 * @param tableName
	 *            内容模型名
	 * @param start
	 *            偏移
	 * @param limit
	 *            记录数
	 * @return 发布动态对象列表
	 */
	public List getDynamicPublish(String tableName, int start, int limit);

	/**
	 * 获得动态发布数量
	 * 
	 * @param tableName
	 *            内容模型名
	 * @return 动态发布数量
	 */
	public int getDynamicPublishCount(String tableName);

	/**
	 * 获得动态发布列表
	 * 
	 * @param tableName
	 *            内容模型名
	 * @param indexIds
	 *            索引id字符串，多个id之间用逗号分割
	 * @return 发布动态对象列表
	 */
	public List getDynamicPublish(String tableName, String indexIds);

	/**
	 * 根据索引Id以及发布内容发布名获得动态发布内容对象
	 * 
	 * @param indexId
	 *            索引id
	 * @param tableName
	 * @return
	 */
	public Map getDynamicPublish(Long indexId, String publishName);

	/**
	 * 快速获得内容列表 使用新的内容索引对象（冗余设计）为提高性能
	 * 
	 * @param nodeId
	 * @param tableName
	 * @param where
	 * @param order
	 * @param args
	 * @param start
	 * @param limit
	 * @param pageInfo
	 * @return
	 */
	public List getQuickContentList(String nodeId, String tableName,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	public List getQuickContentList(String nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo);

	public List getAllContentList(Long nodeId, String tableName, String where,
			String order, Object[] args, Long start, Long limit);

}
