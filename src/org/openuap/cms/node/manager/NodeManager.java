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
package org.openuap.cms.node.manager;

import java.util.List;

import org.openuap.cms.node.dao.NodeDao;
import org.openuap.cms.node.model.Node;

/**
 * <p>
 * Title:NodeManager
 * </p>
 * 
 * <p>
 * Description:结点管理接口.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: <a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: NodeManager.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public interface NodeManager {

	public void setNodeDao(NodeDao nodeDao);

	public Long addNode(Node node);

	public void saveNode(Node node);

	public void deleteNode(Long nodeId);

	public Node getNodeById(Long nodeId);

	/**
	 * 从缓存中获取结点
	 * 
	 * @param nodeId
	 *            结点id
	 * @return
	 */
	public Node getNode(Long nodeId);

	public Node getNodeByGuid(String guid);

	public List<Node> getNodes(Long parentId);

	public List<Node> getNodes(Long parentId, Long rootId);

	/**
	 * 
	 * @param parentId
	 * @param rootId
	 * @param disabled
	 * @return
	 */
	public List<Node> getNodes(Long parentId, Long rootId, Integer disabled);

	public List<Node> getNodes(Long parentId, Long rootId, Integer disabled,
			Integer nodeType);

	public List<Node> getRecycleBinNodes();

	public List<Node> getNodes(String hql, Object[] args);

	public List<Node> getAllNodes();

	/**
	 * 从缓存中获得所有结点
	 * 
	 * @return
	 */
	public List<Node> getAllNodesFromCache();

	public long getAllNodeCount();

	public long getNodeCount(Long parentId);

	public long getNodeCount(Long parentId, Long rootId);

	public long getNodeCount(Long parentId, Long rootId, Integer disabled);

	public long getNodeCount(Long parentId, Long rootId, Integer disabled,
			Integer nodeType);

	/**
	 * return the full node name such as root->child1->child2
	 * 
	 * @param nodeId
	 *            the node id
	 * @param delimiter
	 *            such as ->
	 * @return String
	 */
	public String getNodeFullPath(Long nodeId, String delimiter);

	/**
	 * recycle the node
	 * 
	 * @param nodeId
	 * 
	 * @param containChild
	 *            recycle child node?
	 */
	public void recycleNode(Long nodeId, boolean containChild);

	/**
	 * unRecycle the node
	 * 
	 * @param nodeId
	 *            Integer
	 * @param containChild
	 *            unRecycle child node?
	 */
	public void unRecycleNode(Long nodeId, boolean containChild);

	public void deleteRecycleBinNodes();

	/**
	 * delete the node in fact
	 * 
	 * @param nodeId
	 *            Integer
	 * @param containChild
	 *            delete the child node?
	 */
	public void deleteNode(Long nodeId, boolean containChild);

	/**
	 * 获取包含指定内容模型的子结点集合
	 * 
	 * @param nodeId
	 * @param tableId
	 * @param nodeIds
	 * @param containNodeIds
	 * @return
	 */
	public boolean getContainTableIdNodes(Long nodeId, Long tableId,
			List nodeIds, List containNodeIds);

	/**
	 * decide the nodeId is child of parentId may be child's child...
	 * 
	 * @param parentId
	 *            Integer
	 * @param nodeId
	 *            Integer
	 * @return boolean
	 */
	public boolean getIsChildNode(Long parentId, Long nodeId);

	/**
	 * init some system data
	 * 
	 * @return boolean
	 */
	public boolean initData();

	/**
	 * 
	 * @param parentNodeId
	 * @return
	 */
	public List getAllChildNodeId(Long parentNodeId);

	/**
	 * 获得导航Node结点.
	 * 
	 * @param leafNodeid
	 *            叶子结点Id
	 * @param topNodeId
	 *            导航顶级结点
	 * @return 按照导航顺序的结点列表
	 */
	public List<Node> getNavNodes(Long leafNodeid, Long topNodeId);

	/**
	 * 获得导航Node结点.
	 * 
	 * @param leafNode
	 *            叶子结点
	 * @param topNodeId
	 *            导航顶级结点
	 * @return 按照导航顺序的结点列表
	 */
	public List<Node> getNavNodes(Node leafNode, Long topNodeId);
	/**
	 * 获得所有根结点
	 * @return
	 */
	public List<Node> getRootNodes();

	/**
	 * 获取下一级子结点集合
	 * 
	 * @param parentId
	 * @return
	 */
	public List getChildNodes(long parentId);

	public int getChildNodesCount(long parentId);

	public Node getNodeByGuidFromCache(String guid);

	/**
	 * 更新结点内容数目
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param type
	 *            +,-,=
	 * @param count
	 *            改变的内容数目
	 */
	public void updateNodeContentCount(Long nodeId, String type, long count);

}
