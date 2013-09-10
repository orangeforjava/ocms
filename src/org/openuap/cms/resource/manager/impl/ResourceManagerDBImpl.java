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
package org.openuap.cms.resource.manager.impl;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.resource.cache.ResourceCache;
import org.openuap.cms.resource.dao.ResourceDao;
import org.openuap.cms.resource.manager.ResourceManager;
import org.openuap.cms.resource.model.Resource;

/**
 * <p>
 * 资源管理实现.
 * </p>
 * 
 * <p>
 * $Id: ResourceManagerDBImpl.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ResourceManagerDBImpl implements ResourceManager {

	private ResourceDao resourceDao;

	public ResourceManagerDBImpl() {
	}

	public void setResourceDao(ResourceDao dao) {
		this.resourceDao = dao;
	}

	public Long addResource(Resource resource) {
		return resourceDao.addResource(resource);
	}

	public void saveResource(Resource resource) {
		resourceDao.saveResource(resource);
	}

	public void deleteResource(Long resourceId) {
		resourceDao.deleteResource(resourceId);
	}

	public Resource getResourceById(Long resourceId) {
		return resourceDao.getResourceById(resourceId);
	}

	public Resource getResourceByPath(String path) {
		return resourceDao.getResourceByPath(path);
	}

	public List getAllResource() {
		return resourceDao.getAllResource();
	}

	public List getAllResource(Long start, Long length) {
		return resourceDao.getAllResource(start, length);
	}

	public List getResourcesByNode(Long nodeId) {
		return resourceDao.getResourcesByNode(nodeId);
	}

	public List getResourcesByNode(Long nodeId, Long start, Long length) {
		return resourceDao.getResourcesByNode(nodeId, start, length);
	}

	public List getResourcesByNodeCata(Long nodeId, String category,
			Long start, Long length) {
		return resourceDao.getResourcesByNodeCata(nodeId, category, start,
				length);
	}

	public List getResourcesByCata(String category, Long start, Long length) {
		return resourceDao.getResourcesByCata(category, start, length);
	}

	public long getResourceCountByNodeCata(Long nodeId, String category) {
		return resourceDao.getResourceCountByNodeCata(nodeId, category);
	}

	public long getResourceCountByCata(String category) {
		return resourceDao.getResourceCountByCata(category);
	}

	public List getResourceByContentRef(Long nodeId, Long indexId,
			String category) {
		return resourceDao.getResourceByContentRef(nodeId, indexId, category);
	}

	public List getResourcesByUser(Long userId, QueryInfo qi, PageBuilder pb) {
		return resourceDao.getResourcesByUser(userId, qi, pb);
	}

	public long getTotalResourceSizeByUser(Long userId, QueryInfo qi) {
		return resourceDao.getTotalResourceSizeByUser(userId, qi);
	}

	public long getResourceCountByUser(Long userId, QueryInfo qi, PageBuilder pb) {
		return resourceDao.getResourceCountByUser(userId, qi, pb);
	}

	public List getResourcesByMember(Long memberId, QueryInfo qi, PageBuilder pb) {
		return resourceDao.getResourcesByMember(memberId, qi, pb);
	}

	public long getResourceCountByMember(Long memberId, QueryInfo qi,
			PageBuilder pb) {
		return resourceDao.getResourceCountByMember(memberId, qi, pb);
	}

	public long getTotalResourceSizeByMember(Long memberId, QueryInfo qi) {
		return resourceDao.getTotalResourceSizeByMember(memberId, qi);
	}

	public List getResourcesByNodeCata(Long nodeId, String category,
			String customCategory, Long start, Long length) {
		return resourceDao.getResourcesByNodeCata(nodeId, category,
				customCategory, start, length);
	}

	public List getResourcesByCata(String category, String customCategory,
			Long start, Long length) {
		return resourceDao.getResourcesByCata(category, customCategory, start,
				length);
	}

	public long getResourceCountByNodeCata(Long nodeId, String category,
			String customCategory) {
		return resourceDao.getResourceCountByNodeCata(nodeId, category,
				customCategory);
	}

	public long getResourceCountByCata(String category, String customCategory) {
		return resourceDao.getResourceCountByCata(category, customCategory);
	}

	public Resource getResourceFromCache(Long resourceId) {

		return ResourceCache.getResource(resourceId);
	}
}
