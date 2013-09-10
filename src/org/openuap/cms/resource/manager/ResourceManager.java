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
package org.openuap.cms.resource.manager;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.resource.dao.ResourceDao;
import org.openuap.cms.resource.model.Resource;

/**
 * <p>
 * 资源管理接口定义
 * </p>
 * 
 * <p>
 * $Id: ResourceManager.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ResourceManager {

	public void setResourceDao(ResourceDao dao);

	public Long addResource(Resource resource);

	public void saveResource(Resource resource);

	public void deleteResource(Long resourceId);

	public Resource getResourceById(Long resourceId);

	public Resource getResourceByPath(String path);

	public List getAllResource();

	public List getAllResource(Long start, Long length);

	public List getResourcesByNode(Long nodeId);

	public List getResourcesByNode(Long nodeId, Long start, Long length);

	public List getResourcesByNodeCata(Long nodeId, String category,
			Long start, Long length);

	public List getResourcesByCata(String category, Long start, Long length);

	public long getResourceCountByNodeCata(Long nodeId, String category);

	public long getResourceCountByNodeCata(Long nodeId, String category,
			String customCategory);

	public long getResourceCountByCata(String category);

	public long getResourceCountByCata(String category, String customCategory);

	public List getResourceByContentRef(Long nodeId, Long indexId,
			String category);

	public List getResourcesByUser(Long userId, QueryInfo qi, PageBuilder pb);

	public long getTotalResourceSizeByUser(Long userId, QueryInfo qi);

	public long getResourceCountByUser(Long userId, QueryInfo qi, PageBuilder pb);

	/**
	 * 根据会员id获得资源列表
	 * 
	 * @param memberId
	 *            Long
	 * @param qi
	 *            QueryInfo
	 * @param pb
	 *            PageBuilder
	 * @return List
	 */
	public List getResourcesByMember(Long memberId, QueryInfo qi, PageBuilder pb);

	/**
	 * 
	 * @param memberId
	 *            Long
	 * @param qi
	 *            QueryInfo
	 * @param pb
	 *            PageBuilder
	 * @return long
	 */
	public long getResourceCountByMember(Long memberId, QueryInfo qi,
			PageBuilder pb);

	/**
	 * 
	 * @param memberId
	 *            Long
	 * @param qi
	 *            QueryInfo
	 * @return long
	 */
	public long getTotalResourceSizeByMember(Long memberId, QueryInfo qi);

	public List getResourcesByNodeCata(Long nodeId, String category,
			String customCategory, Long start, Long length);

	public List getResourcesByCata(String category, String customCategory,
			Long start, Long length);
	/**
	 * 从缓存中获得资源对象
	 * @param resourceId
	 * @return
	 */
	public Resource getResourceFromCache(Long resourceId);
}
