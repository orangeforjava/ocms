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
package org.openuap.cms.node.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.node.dao.NodeDao;
import org.openuap.cms.node.model.Node;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 结点缓存.
 * </p>
 * 
 * <p>
 * $Id: NodeCache.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeCache extends CmsCache {

	private static JCS nodeCache = null;
	static {
		try {
			nodeCache = JCS.getInstance("node");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从缓存中获取所有结点
	 * 
	 * @return
	 */
	public static List<Node> getAllNodes() {
		Object obj = nodeCache.get("all");
		if (obj == null) {
			NodeDao nodeDao = (NodeDao) ObjectLocator.lookup("nodeDao",
					CmsPlugin.PLUGIN_ID);
			if (nodeDao != null) {
				obj = nodeDao.getAllNodes();
				if (obj != null) {
					try {
						nodeCache.put("all", obj);
					} catch (CacheException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (List) obj;
	}

	/**
	 * 从缓存中获取所有儿子结点ID列表
	 * 
	 * @param parentId 父节点ID
	 * @return
	 */
	public static List<Long> getChildNodeIds(long parentId) {
		List<Node> nodes = getAllNodes();
		List ids = new ArrayList();
		if (nodes != null) {
			for (Node node : nodes) {
				// FIX:确保结点没有被删除
				if (!new Integer("1").equals(node.getDisabled())
						&& node.getParentId().longValue() == parentId) {
					ids.add(node.getNodeId());
				}
			}
		}
		return ids;
	}

	public static void getAllChildNodeIds(long parentId, List<Long> nodeIds) {
		List<Long> nids = getChildNodeIds(parentId);
		if (nids.size() == 0) {
			return;
		} else {
			for (Long nid : nids) {
				nodeIds.add(nid);
				getAllChildNodeIds(nid, nodeIds);
			}
		}
	}

	public static List<Node> getChildNodes(long parentId) {
		List<Node> nodes = getAllNodes();
		List ids = NodeQuickQuery.getChildNodes(nodes, parentId, 0);
		
		return ids;
	}

	public static List<Node> getGlobalRefreshNodes() {
		List<Node> nodes = getAllNodes();
		List rs = new ArrayList();
		if (nodes != null) {
			for (Node node : nodes) {
				if (node.getAutoRefreshMode() == 3) {
					rs.add(node);
				}
			}
		}
		return rs;
	}

	public static Node getNode(long nodeId) {
		List<Node> nodes = getAllNodes();
		if (nodes != null) {
			for (Node node : nodes) {
				if (node.getNodeId().longValue() == nodeId) {
					return node;
				}
			}
		}
		return null;
	}

	public static Node getParentNode(long nodeId) {
		List<Node> nodes = getAllNodes();
		if (nodes != null) {
			for (Node node : nodes) {
				if (node.getNodeId().longValue() == nodeId) {
					// 找到子结点
					long parentId = node.getParentId();
					if (parentId != 0) {
						return getNode(parentId);
					}
				}
			}
		}
		return null;
	}

	public static Node getNodeByGuid(String guid) {
		List<Node> nodes = getAllNodes();
		if (nodes != null) {
			for (Node node : nodes) {
				if (node.getNodeGuid() != null
						&& node.getNodeGuid().equals(guid)) {
					return node;
				}
			}
		}
		return null;
	}

	/**
	 * 移出缓存
	 * 
	 * @param id
	 * 
	 */
	public static void remove(Long id) {
		try {
			nodeCache.remove(id);
		} catch (CacheException ex) {
			log.error(ex);
		}
	}

	/**
	 * 清空缓存
	 */
	public static void clear() {
		try {
			nodeCache.clear();
		} catch (CacheException ex) {
			log.error(ex);
		}
	}

	/**
	 * 放入缓存
	 * 
	 * @param node
	 * 
	 */
	public static void put(Node node) {
		try {
			nodeCache.put(node.getNodeId(), node);
		} catch (Exception ex) {
			log.error("Can't put object to cache", ex);
		}
	}

}
