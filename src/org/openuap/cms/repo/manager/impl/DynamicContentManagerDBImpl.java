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
package org.openuap.cms.repo.manager.impl;

import java.util.List;
import java.util.Map;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.cache.ContentModelCache;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.node.cache.NodeCache;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.repo.cache.RepoCache;
import org.openuap.cms.repo.dao.ContentIndexDao;
import org.openuap.cms.repo.dao.DynamicContentDao;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.resource.dao.ResourceRefDao;
import org.openuap.cms.resource.model.ResourceRef;
import org.openuap.cms.util.PageInfo;

/**
 * <p>
 * 动态内容管理数据库实现.
 * </p>
 * 
 * <p>
 * $Id: DynamicContentManagerDBImpl.java 3963 2010-12-06 14:56:49Z orangeforjava
 * $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DynamicContentManagerDBImpl implements DynamicContentManager {

	/** 动态内容管理Dao. */
	private DynamicContentDao dynamicContentDao;

	/** 内容索引Dao. */
	private ContentIndexDao contentIndexDao;

	/** 资源引用Dao. */
	private ResourceRefDao resourceRefDao;

	/** 结点 Manager. */
	private NodeManager nodeManager;

	/** 内容模型Manager. */
	private ContentTableManager contentTableManager;

	/**
	 * 
	 * 
	 */
	public DynamicContentManagerDBImpl() {
	}

	public void setDynamicContentDao(DynamicContentDao dao) {
		this.dynamicContentDao = dao;
	}

	/**
	 * 获得内容列表
	 * 
	 * @param nodeId
	 * 
	 * @param tableId
	 * 
	 * @param where
	 * 
	 * @param order
	 * 
	 * @param args
	 * 
	 * @param start
	 * 
	 * @param limit
	 * 
	 * @param pageInfo
	 * 
	 * @return
	 */
	public List getContentList(Long nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		return this.getContentList(String.valueOf(nodeId), tableId, where,
				order, args, start, limit, pageInfo);
	}

	public List getRecycleContentList(Long nodeId, Long tableId) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				return dynamicContentDao.getRecycleContentList(nodeId,
						tableName);
			} else {
				return dynamicContentDao.getRecycleContentList(nodeId, tableId);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param indexId
	 *            内容索引
	 * @param tableId
	 *            内容模型id
	 * @return 内容的Map对象
	 */
	public Object getContent(Long indexId, Long tableId) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			// System.out.println("tableName=" + tableName);
			if (tableName != null && !tableName.trim().equals("")) {
				// System.out.println("tableName=" + tableName);
				return dynamicContentDao.getContent(indexId, tableName);

			} else {
				// System.out.println("tableName2=" + tableName);
				return dynamicContentDao.getContent(indexId, tableId);

			}
		}
		return null;

	}

	public Long addContentIndex(Map contentIndex) {
		return dynamicContentDao.addContentIndex(contentIndex);
	}

	public Long addContent(Long tableId, Map content) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				return dynamicContentDao.addContent(tableName, content);
			} else {
				return dynamicContentDao.addContent(tableId, content);
			}
		}
		return null;
	}

	public void savePublish(Long tableId, Map publish) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				dynamicContentDao.savePublish(tableName + "Publish", publish);
			} else {
				dynamicContentDao.savePublish(tableId, publish);
			}
		}
	}

	public void deletePublish(Long tableId, Long indexId) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				this.dynamicContentDao.deletePublish(tableName + "Publish",
						indexId);
			} else {
				this.dynamicContentDao.deletePublish(tableId, indexId);
			}
		}

	}

	public void setContentIndexDao(ContentIndexDao dao) {
		this.contentIndexDao = dao;
	}

	public ContentIndex getContentIndexById(Long contentIndexId) {
		return this.contentIndexDao.getContentIndexById(contentIndexId);
	}

	public void saveContentIndex(ContentIndex contentIndex) {
		this.contentIndexDao.saveContentIndex(contentIndex);
	}

	public void deleteContentIndex(Long contentIndexId) {
		contentIndexDao.deleteContentIndex(contentIndexId);
	}

	public void setResourceRefDao(ResourceRefDao dao) {
		this.resourceRefDao = dao;
	}

	public void addResourceRef(ResourceRef resourceRef) {
		this.resourceRefDao.addResourceRef(resourceRef);
	}

	public void saveContentIndex(Map contentIndex) {
		this.dynamicContentDao.saveContentIndex(contentIndex);
	}

	public void saveContent(Long tableId, Map content) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				this.dynamicContentDao.saveContent(tableName, content);
			} else {
				this.dynamicContentDao.saveContent(tableId, content);
			}
		}

	}

	public ResourceRef getResourceRefById(Long nodeId, Long indexId,
			Long resourceId) {
		return resourceRefDao.getResourceRefById(nodeId, indexId, resourceId);
	}

	public List getResourceRefByNodeIndexId(Long nodeId, Long indexId) {
		return resourceRefDao.getResourceRefByNodeIndexId(nodeId, indexId);
	}

	public void deleteResourceRefByNodeIndexId(Long nodeId, Long indexId) {
		resourceRefDao.deleteResourceRefByNodeIndexId(nodeId, indexId);
	}

	public void saveResourceRef(ResourceRef resourceRef) {
		resourceRefDao.saveResourceRef(resourceRef);
	}

	public Map getContentIndexMapById(Long indexId) {
		return dynamicContentDao.getContentIndexMapById(indexId);
	}

	public void deleteContentIndex(Long indexId, Long tableId, boolean cascade) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				dynamicContentDao.deleteContentIndex(indexId, tableId,
						tableName, cascade);
			} else {
				dynamicContentDao.deleteContentIndex(indexId, tableId, cascade);
			}
		}

	}

	public long getLongHql(String hql, Object[] args) {
		return dynamicContentDao.getLongHql(hql, args);
	}

	public List getListHql(String hql, Object[] args, QueryInfo qi) {
		return dynamicContentDao.getListHql(hql, args, qi);
	}

	public Object getObjectHql(String hql, Object[] args) {
		return dynamicContentDao.getObjectHql(hql, args);
	}

	public long getNodePublishContentCount(Long nodeId) {
		return contentIndexDao.getNodePublishContentCount(nodeId);
	}

	public long getNodeUnPubllishContentCount(Long nodeId) {
		return contentIndexDao.getNodeUnPubllishContentCount(nodeId);
	}

	public List getNodePublishContents(Long nodeId, Long start, Long length) {
		return contentIndexDao.getNodePublishContents(nodeId, start, length);
	}

	public List getNodeUnPublishContents(Long nodeId, Long start, Long length) {
		return contentIndexDao.getNodeUnPublishContents(nodeId, start, length);
	}

	public List searchContentList(String keywords, String[] fields,
			String published, String[] nodeIds, String time, Long tableId,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				return dynamicContentDao.searchContentList(keywords, fields,
						published, nodeIds, time, tableName, where, order,
						args, start, limit, pageInfo);

			} else {
				return dynamicContentDao.searchContentList(keywords, fields,
						published, nodeIds, time, tableId, where, order, args,
						start, limit, pageInfo);

			}
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @param contentIndex
	 *            ContentIndex
	 * @return Integer
	 */
	public Long addContentIndex(ContentIndex contentIndex) {
		return contentIndexDao.addContentIndex(contentIndex);
	}

	public void deleteContentIndex(Long indexId, Long tableId, int type) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				dynamicContentDao.deleteContentIndex(indexId, tableId,
						tableName, type);
			} else {
				dynamicContentDao.deleteContentIndex(indexId, tableId, type);
			}
		}

	}

	public void deleteContent(Long tableId, Long contentId) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				dynamicContentDao.deleteContent(tableName, contentId);
			} else {
				dynamicContentDao.deleteContent(tableId, contentId);
			}
		}

	}

	public List getLinkList(Long contentId, Long tableId, Integer type) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				return dynamicContentDao.getLinkList(contentId, tableId,
						tableName, type);
			} else {
				return dynamicContentDao.getLinkList(contentId, tableId, type);
			}
		}
		return null;
	}

	public void saveContentTable(Map contentTable) {
		dynamicContentDao.saveContentTable(contentTable);
	}

	public ContentIndex getContentIndexByContentId(Long contentId, Long nodeId) {
		return contentIndexDao.getContentIndexByContentId(contentId, nodeId);
	}

	/**
	 * 
	 */
	public ContentIndex getContentIndexByOldId(Long nodeId, Long oldId,
			String table) {
		return contentIndexDao.getContentIndexByOldId(nodeId, oldId, table);
	}

	/**
	 * 
	 */
	public ContentIndex getContentIndexByOldId(Long oldId, String table) {
		return contentIndexDao.getContentIndexByOldId(oldId, table);
	}

	public List getRecycleContentList(Long nodeId, Long tableId, QueryInfo qi,
			PageBuilder pb) {
		return dynamicContentDao.getRecycleContentList(nodeId, tableId, qi, pb);
	}

	public List getRecycleContentList(Long nodeId, String tableName,
			QueryInfo qi, PageBuilder pb) {
		return dynamicContentDao.getRecycleContentList(nodeId, tableName, qi,
				pb);
	}

	public List getRecycleContents(Long nodeId) {
		return contentIndexDao.getRecycleContents(nodeId);
	}

	public List getListContent(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb) {
		return dynamicContentDao.getListContent(hql, hql_count, qi, pb);
	}

	public Map getDynamicContent(Long indexId, Long tableId) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				return dynamicContentDao.getDynamicContent(indexId, tableName);
			} else {
				return dynamicContentDao.getDynamicContent(indexId, tableId);
			}
		}
		return null;
	}

	public List getDynamicPublish(String tableName, int start, int limit) {
		// TODO Auto-generated method stub
		return dynamicContentDao.getDynamicPublish(tableName, start, limit);
	}

	public Map getDynamicPublish(Long indexId) {
		ContentIndex ci = contentIndexDao.getContentIndexById(indexId);
		String publishName = null;
		if (ci != null) {
			Long nid = ci.getNodeId();
			Node n = nodeManager.getNode(nid);

			if (n != null) {
				Long tid = n.getTableId();
				ContentTable ct = ContentModelCache.getContentTable(tid);
				String tableName = ct.getEntityPublishName();
				if (tableName != null) {
					publishName = tableName;
				} else {
					publishName = "Publish_" + tid;
				}
			}
		}
		if (publishName != null) {
			return this.dynamicContentDao.getDynamicPublish(indexId,
					publishName);
		}
		return null;
	}

	/**
	 * 结点所有内容数目
	 */
	public long getNodeAllContentCount(Long parentId) {
		// 递归子结点
		long count = 0;
		List childNodeIds = getNodeManager().getAllChildNodeId(parentId);
		int size = childNodeIds.size();
		Node node = null;
		for (int i = 0; i < size; i++) {
			Long nid = (Long) childNodeIds.get(i);
			// TODO 每次计算的成本是比较高的，不应该每次计算，而应该是缓存在结点之中
			// NodePublishStat nps = RepoCache.getNodePublishStat(nid);
			node = NodeCache.getNode(nid);
			if (node != null) {
				count += node.getContentCount();
			}
		}
		return count;
	}

	/**
	 * 结点发布内容数目
	 * 
	 * @param parentId
	 * @return
	 */
	public long getNodeAllPublishContentCount(Long parentId) {
		long count = 0;
		List childNodeIds = getNodeManager().getAllChildNodeId(parentId);
		int size = childNodeIds.size();
		Node node = null;
		for (int i = 0; i < size; i++) {
			Long nid = (Long) childNodeIds.get(i);
			// NodePublishStat nps = RepoCache.getNodePublishStat(nid);
			//
			node = NodeCache.getNode(nid);
			if (node != null) {

				count += node.getPublishedConentCount();
			}
		}
		return count;
	}

	public NodeManager getNodeManager() {
		// NodeManager nodeManager = (NodeManager) ObjectLocator.lookup(
		// "nodeManager", CmsPlugin.PLUGIN_ID);
		return nodeManager;
	}

	public Long getObjectCount(String hql, QueryInfo qi, PageBuilder pb) {
		//
		return dynamicContentDao.getObjectCount(hql, qi, pb);
	}

	public ContentIndex getContentIndexFromCache(Long contentIndexId) {
		//
		return RepoCache.getContentIndex(contentIndexId);
	}

	public List getQuickContentList(Long nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {

		return this.getQuickContentList(String.valueOf(nodeId), tableId, where,
				order, args, start, limit, pageInfo);
	}

	public long getAllContentCount(Long nodeId) {
		return contentIndexDao.getAllContentCount(nodeId);
	}

	/**
	 * 获得所有内容列表（包括回收站之中的内容）
	 */
	public List getAllContentList(Long nodeId, String tableName, String where,
			String order, Object[] args, Long start, Long limit) {
		return dynamicContentDao.getAllContentList(nodeId, tableName, where,
				order, args, start, limit);
	}

	public List getAllContentList(Long nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String tableName = ct.getEntityName();
			if (tableName != null && !tableName.trim().equals("")) {
				return dynamicContentDao.getAllContentList(nodeId, tableName,
						where, order, args, start, limit);
			} else {
				return dynamicContentDao.getAllContentList(nodeId, "Content_"
						+ tableId, where, order, args, start, limit);
			}
		}
		return null;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	/**
	 * 更新内容索引内容
	 */
	public void updateContentIndex(Long id, String fieldName, Object fieldValue) {
		this.updateContentIndex(id, fieldName, fieldValue, null);
	}

	public void updateContentIndex(Long id, String fieldName,
			Object fieldValue, String where) {
		String hql = "update ContentIndex set " + fieldName + "=" + fieldValue
				+ ",lastModifiedDate=" + System.currentTimeMillis()
				+ " where indexId=" + id;
		if (StringUtil.hasText(where)) {
			hql += " and " + where;
		}
		this.contentIndexDao.executeUpdate(hql);
	}

	public Long addContent(String tableName, Map content) {
		return this.dynamicContentDao.addContent(tableName, content);
	}

	public void saveContent(String tableName, Map content) {
		this.dynamicContentDao.saveContent(tableName, content);

	}

	public int executeUpdate(String hql) {
		return dynamicContentDao.executeUpdate(hql);
	}

	public List executeFind(String hql) {
		return dynamicContentDao.executeFind(hql);
	}

	public void verifyNodeContentStat(Long nodeId) {
		long publishedCount = getNodePublishContentCount(nodeId);
		long unpublishedCount = getNodeUnPubllishContentCount(nodeId);
		//
		String hql = "update Node set contentCount="
				+ (publishedCount + unpublishedCount)
				+ " , publishedConentCount=" + publishedCount
				+ " where nodeId=" + nodeId;
		this.executeUpdate(hql);
		// 递归调用
		List<Long> ids = NodeCache.getChildNodeIds(nodeId.longValue());
		if (ids != null) {
			for (Long id : ids) {
				verifyNodeContentStat(id);
			}
		}
	}

	public void verifyAllNodeContentStat() {
		List<Long> ids = NodeCache.getChildNodeIds(0);
		for (Long id : ids) {
			verifyNodeContentStat(id);
		}
		// 自动清除缓存
		NodeCache.clear();
	}

	public List getQuickContentList(String nodeIds, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			if (ct.getEntityName() != null) {
				return dynamicContentDao.getQuickContentList(nodeIds, ct
						.getEntityName(), where, order, args, start, limit,
						pageInfo);
			} else {
				return dynamicContentDao.getQuickContentList(nodeIds, tableId,
						where, order, args, start, limit, pageInfo);

			}
		}
		return null;
	}

	public List getContentList(String nodeIds, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		// 获得内容模型信息
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			if (ct.getEntityName() != null) {
				return dynamicContentDao.getContentList(nodeIds, ct
						.getEntityName(), where, order, args, start, limit,
						pageInfo);
			} else {
				return dynamicContentDao.getContentList(nodeIds, tableId,
						where, order, args, start, limit, pageInfo);

			}
		}
		return null;
	}

	public long getDynamictContentCount(long tableId) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		if (ct != null) {
			String entityName="Content_"+tableId;
			if (ct.getEntityName() != null) {
				entityName=ct.getEntityName();
			}
			return this.dynamicContentDao.getLongHql("select count(*) from "+entityName+" as c", new Object[]{});
		}
		return -1;
	}
}
