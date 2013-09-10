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

import org.openuap.base.dao.hibernate.BaseDao;
import org.openuap.cms.repo.model.ContentIndex;

/**
 * <p>
 * 内容索引Dao接口.
 * </p>
 * 
 * 
 * <p>
 * $Id: ContentIndexDao.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ContentIndexDao extends BaseDao{
	/**
	 * 添加内容索引对象
	 * 
	 * @param contentIndex
	 *            内容索引对象
	 * @return 内容索引对象id
	 */
	public Long addContentIndex(ContentIndex contentIndex);

	/**
	 * 根据Id获得内容索引对象
	 * 
	 * @param contentIndexId
	 *            内容索引id
	 * @return
	 */
	public ContentIndex getContentIndexById(Long contentIndexId);

	/**
	 * 获得内容索引
	 * 
	 * @param contentId
	 *            内容id
	 * @param nodeId
	 *            结点id
	 * @return 内容索引对象
	 */
	public ContentIndex getContentIndexByContentId(Long contentId, Long nodeId);

	/**
	 * 保存内容
	 * 
	 * @param contentIndex
	 *            内容索引对象
	 */
	public void saveContentIndex(ContentIndex contentIndex);

	/**
	 * 删除内容
	 * 
	 * @param contentIndexId
	 *            内容id
	 */
	public void deleteContentIndex(Long contentIndexId);

	/**
	 * 获得结点发布内容数
	 * 
	 * @param nodeId
	 *            结点id
	 * @return 该结点发布内容数
	 */
	public long getNodePublishContentCount(Long nodeId);

	/**
	 * 获得结点未发布内容数目
	 * 
	 * @param nodeId
	 *            结点Id
	 * @return 该结点未发布内容数目
	 */
	public long getNodeUnPubllishContentCount(Long nodeId);

	/**
	 * 获得结点内容总数（不包括回收站之内的内容）
	 * 
	 * @param nodeId
	 *            结点id
	 * @return 内容总数
	 */
	public long getNodeContentCount(Long nodeId);

	/**
	 * 获得结点发布的内容列表
	 * 
	 * @param nodeId
	 *            结点id
	 * @param start
	 *            偏移
	 * @param length
	 *            记录数
	 * @return 内容列表
	 */
	public List getNodePublishContents(Long nodeId, Long start, Long length);

	/**
	 * 获得结点未发布的内容列表
	 * 
	 * @param nodeId
	 *            结点id
	 * @param start
	 *            偏移
	 * @param length
	 *            记录数
	 * @return 内容列表
	 */
	public List getNodeUnPublishContents(Long nodeId, Long start, Long length);

	/**
	 * 获得回收站内容列表
	 * 
	 * @param nodeId
	 *            结点id
	 * @return 内容列表
	 */
	public List getRecycleContents(Long nodeId);

	/**
	 * 根据原来id获得内容索引
	 * 
	 * @param nodeId
	 *            结点id
	 * @param oldId
	 *            原来内容id
	 * @param table
	 *            目标内容实体名
	 * @return 内容索引对象
	 */
	public ContentIndex getContentIndexByOldId(Long nodeId, Long oldId, String table);

	/**
	 * 根据原来的id获得内容索引
	 * 
	 * @param oldId
	 *            原来内容id
	 * @param table
	 *            目标内容实体名
	 * @return 内容索引对象
	 */
	public ContentIndex getContentIndexByOldId(Long oldId, String table);
	/**
	 * 获得所有内容数量（包括回收站之内的内容）
	 * @param nodeId
	 * @return
	 */
	public long getAllContentCount(Long nodeId);
	
}
