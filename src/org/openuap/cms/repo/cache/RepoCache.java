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
package org.openuap.cms.repo.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.repo.dao.ContentIndexDao;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.repo.stat.NodePublishStat;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 内容存储缓存
 * </p>
 * 
 * <p>
 * $Id: RepoCache.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 2.0
 */
public class RepoCache extends CmsCache {
	private static JCS repoCache = null;
	static {
		try {
			repoCache = JCS.getInstance("repo");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从缓存中获得结点发布信息统计
	 *  
	 * @param nodeId 结点名
	 * @return
	 */
	public static NodePublishStat getNodePublishStat(long nodeId) {
		Object obj = repoCache.get("publish-stat-" + nodeId);
		if (obj == null) {
			ContentIndexDao contentIndexDao = (ContentIndexDao) ObjectLocator
					.lookup("contentIndexDao", CmsPlugin.PLUGIN_ID);
			if (contentIndexDao != null) {
				long contentCount = contentIndexDao.getNodeContentCount(nodeId);
				long publishedConentCount = contentIndexDao
						.getNodePublishContentCount(nodeId);
				//
				NodePublishStat nodePublishStat = new NodePublishStat();
				nodePublishStat.setContentCount(contentCount);
				nodePublishStat.setPublishedConentCount(publishedConentCount);
				nodePublishStat.setNodeId(nodeId);
				//
				try {
					repoCache.put("publish-stat-" + nodeId, nodePublishStat);
					obj = nodePublishStat;
				} catch (CacheException ex) {
					log.error(ex);
				}
			}
		}
		return (NodePublishStat) obj;
	}
	/**
	 * 获得内容索引缓存
	 * @param indexId
	 * @return
	 */
	public static ContentIndex getContentIndex(Long indexId) {
		Object obj = repoCache.get("contentIndex-" + indexId);
		if (obj == null) {
			ContentIndexDao contentIndexDao = (ContentIndexDao) ObjectLocator
					.lookup("contentIndexDao", CmsPlugin.PLUGIN_ID);
			if (contentIndexDao != null) {
				obj = contentIndexDao.getContentIndexById(indexId);
				//
				if (obj != null) {
					//
					try {
						repoCache.put("contentIndex-" + indexId, obj);
					} catch (CacheException ex) {
						log.error(ex);
					}
				}
			}
		}
		return (ContentIndex) obj;
	}
	public static void clearNodePublishStat(long nodeId) {
		try {
			repoCache.remove("publish-stat-" + nodeId);
		} catch (CacheException e) {
			log.error(e);
		}
	}
}
