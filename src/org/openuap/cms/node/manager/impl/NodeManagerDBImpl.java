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

package org.openuap.cms.node.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.dao.ContentTableDao;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.node.cache.NodeCache;
import org.openuap.cms.node.dao.NodeDao;
import org.openuap.cms.node.event.NodeEvent;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.manager.NodeQuickQuery;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.model.Nodes;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * 结点管理实现.
 * </p>
 * 
 * 
 * <p>
 * $Id: NodeManagerDBImpl.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 *
 * @author Joseph
 * @version 1.0
 */
public class NodeManagerDBImpl implements NodeManager, ApplicationListener {

	private static final String INIT_DATA_FILE_NAME = "nodeData.xml";

	private NodeDao nodeDao;

	private ContentTableDao contentTableDao;

	public NodeManagerDBImpl() {
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

	public void setContentTableDao(ContentTableDao contentTableDao) {
		this.contentTableDao = contentTableDao;
	}

	public Long addNode(Node node) {
		return nodeDao.addNode(node);
	}

	public void saveNode(Node node) {
		nodeDao.saveNode(node);
	}

	public void deleteNode(Long nodeId) {
		nodeDao.deleteNode(nodeId);
	}

	public Node getNodeById(Long nodeId) {
		return nodeDao.getNodeById(nodeId);
	}

	public Node getNodeByGuid(String guid) {
		return nodeDao.getNodeByGuid(guid);
	}

	public List<Node> getNodes(Long parentId) {
		return nodeDao.getNodes(parentId);
	}

	public List<Node> getNodes(Long parentId, Long rootId) {
		return nodeDao.getNodes(parentId, rootId);
	}

	public List<Node> getNodes(String hql, Object[] args) {
		return nodeDao.getNodes(hql, args);
	}

	public long getAllNodeCount() {
		return nodeDao.getAllNodeCount();
	}

	public long getNodeCount(Long parentId) {
		return nodeDao.getNodeCount(parentId);
	}
	
	public List<Node> getNodes(Long parentId, Long rootId, Integer disabled) {
		return nodeDao.getNodes(parentId, rootId, disabled);
	}

	public long getNodeCount(Long parentId, Long rootId) {
		return nodeDao.getNodeCount(parentId, rootId);
	}

	public long getNodeCount(Long parentId, Long rootId, Integer disabled) {
		return nodeDao.getNodeCount(parentId, rootId, disabled);
	}

	public List<Node> getNodes(Long parentId, Long rootId, Integer disabled,
			Integer nodeType) {
		return nodeDao.getNodes(parentId, rootId, disabled, nodeType);
	}

	public long getNodeCount(Long parentId, Long rootId, Integer disabled,
			Integer nodeType) {
		return nodeDao.getNodeCount(parentId, rootId, disabled, nodeType);
	}

	public String getNodeFullPath(Long nodeId, String delimiter) {
		Node node = this.getNode(nodeId);
		if (node != null) {
			Long pid = node.getParentId();
			if (pid == Long.valueOf("0")) {
				return node.getName();
			} else {
				return getNodeFullPath(pid, delimiter) + delimiter
						+ node.getName();
			}
		}
		if (nodeId.intValue() == 0) {
			return "站点根";
		}
		return "";
	}

	public void recycleNode(Long nodeId, boolean containChild) {
		nodeDao.recycleNode(nodeId, containChild);
	}

	public void unRecycleNode(Long nodeId, boolean containChild) {
		nodeDao.unRecycleNode(nodeId, containChild);
	}

	public void deleteNode(Long nodeId, boolean containChild) {
		nodeDao.deleteNode(nodeId, containChild);
	}

	public boolean getContainTableIdNodes(Long nodeId, Long tableId,
			List nodeIds, List containNodeIds) {
		NodeQuickQuery query=new NodeQuickQuery(getAllNodesFromCache());
		return query.getContainTableIdNodes(nodeId, tableId, nodeIds,
				containNodeIds);
	}

	public List<Node> getRecycleBinNodes() {
		return nodeDao.getRecycleBinNodes();
	}

	public void deleteRecycleBinNodes() {
		nodeDao.deleteRecycleBinNodes();
	}

	public boolean getIsChildNode(Long parentId, Long nodeId) {
		return nodeDao.getIsChildNode(parentId, nodeId);
	}

	public boolean initData() {
		XStream xstream = new XStream();
		xstream.alias("node", Node.class);
		xstream.alias("data", Nodes.class);
		//
		try {
			String dataPath = CMSConfig.getInstance().getInitDataPath();
			String fileName = dataPath + "/" + INIT_DATA_FILE_NAME;
			fileName = StringUtil.normalizePath(fileName);
			File file = new File(fileName);
			InputStreamReader in = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			Nodes nodes = (Nodes) xstream.fromXML(in);
			if (nodes != null) {
				List nodeList = nodes.getNodes();
				if (nodeList != null) {
					for (int i = 0; i < nodeList.size(); i++) {
						Node node = (Node) nodeList.get(i);
						Node node_old = this.getNodeByGuid(node.getNodeGuid());
						String tableName = node.getTableName();
						Long tid = null;
						Long nid = null;
						if (tableName != null) {
							ContentTable ct = contentTableDao
									.getCTByTableName(tableName);
							if (ct != null) {
								tid = ct.getTableId();
							}
						}
						if (tid == null) {
							tid = new Long(0);
						}
						if (node_old != null) {
							nid = node_old.getNodeId();
							node.setTableId(tid);
							this.saveNode(node);
						} else {
							node.setTableId(tid);
							nid = this.addNode(node);
							node.setNodeId(nid);
						}
					}
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public List getAllChildNodeId(Long parentNodeId) {
		// 从缓存中获取必要的信息
		List ids = new ArrayList();
		ids.add(parentNodeId);
		getChildNodeIds(parentNodeId, ids);
		return ids;
	}

	private void getChildNodeIds(Long parentNodeId, List<Long> idList) {
		List ids = NodeCache.getChildNodeIds(parentNodeId);
		//
		if (ids != null) {
			for (int i = 0; i < ids.size(); i++) {
				Long id = (Long) ids.get(i);
				idList.add(id);
				getChildNodeIds(id, idList);
			}
		}

	}

	public List<Node> getNavNodes(Long leafNodeId, Long topNodeId) {
		Stack<Node> navNodes = new Stack<Node>();
		getNavNodes(leafNodeId, topNodeId, navNodes);
		List<Node> rsNodes = new ArrayList<Node>();
		Node node = null;
		while (!navNodes.empty() && ((node = navNodes.pop()) != null)) {
			rsNodes.add(node);
		}
		return rsNodes;
	}

	public List<Node> getNavNodes(Node leafNode, Long topNodeId) {
		//
		if (leafNode != null) {
			long pid = leafNode.getParentId();
			Stack<Node> navNodes = new Stack<Node>();
			if (pid > 0) {
				getNavNodes(pid, topNodeId, navNodes);
			}
			List<Node> rsNodes = new ArrayList<Node>();
			Node node = null;
			while (!navNodes.empty() && ((node = navNodes.pop()) != null)) {
				rsNodes.add(node);
			}
			rsNodes.add(leafNode);
			return rsNodes;
		}
		return null;
	}

	private void getNavNodes(Long leafNodeId, Long topNodeId,
			Stack<Node> navNodes) {
		Node node = getNode(leafNodeId);
		if (node != null) {
			Long pid = node.getParentId();
			navNodes.push(node);
			if (pid > 0 && !pid.equals(topNodeId)) {
				getNavNodes(pid, topNodeId, navNodes);
			}
		}
	}

	/**
	 * 从缓存中获得Node对象
	 */
	public Node getNode(Long nodeId) {
		Node node = NodeCache.getNode(nodeId);
		return node;
	}

	public List getAllNodes() {
		return nodeDao.getAllNodes();
	}

	public List<Node> getRootNodes() {
		return null;
	}

	public List<Node> getAllNodesFromCache() {
		return NodeCache.getAllNodes();
	}

	public List getChildNodes(long parentId) {
		return NodeCache.getChildNodes(parentId);
	}

	public int getChildNodesCount(long parentId) {
		return getChildNodes(parentId).size();
	}

	public Node getNodeByGuidFromCache(String guid) {
		return NodeCache.getNodeByGuid(guid);
	}

	public void onApplicationEvent(ApplicationEvent event) {

		if (event instanceof NodeEvent) {
			NodeEvent nodeEvent = (NodeEvent) event;
			String type = nodeEvent.getEventType();
			if (type.equals(NodeEvent.NODE_CREATED)
					|| type.equals(NodeEvent.NODE_UPDATED)
					|| type.equals(NodeEvent.NODE_DELETED)) {
				//监听结点变更事件来更新缓存
				NodeCache.clear();
			}
		}
	}

	public void updateNodeContentCount(Long nodeId, String type, long count) {
		nodeDao.updateNodeContentCount(nodeId, type, count);
	}

	
}
