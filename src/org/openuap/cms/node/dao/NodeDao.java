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
package org.openuap.cms.node.dao;

import java.util.List;

import org.openuap.cms.node.model.Node;

/**
 * <p>
 * 结点DAO接口.
 * </p>
 * 
 * <p>
 * $Id: NodeDao.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface NodeDao {
	
	public static String ENTITY_NAME="Node";
	/**
	 * 添加结点
	 * 
	 * @param node
	 *            结点对象
	 * @return 结点Id
	 */
	public Long addNode(Node node);

	/**
	 * 保存结点
	 * 
	 * @param node
	 *            结点对象
	 */
	public void saveNode(Node node);

	/**
	 * 删除结点
	 * 
	 * @param nodeId
	 *            结点id
	 */
	public void deleteNode(Long nodeId);

	/**
	 * 根据id获得结点对象
	 * 
	 * @param nodeId
	 *            结点id
	 * @return 结点对象
	 */
	public Node getNodeById(Long nodeId);

	/**
	 * 根据GUID获得结点对象
	 * 
	 * @param guid
	 *            结点GUID
	 * @return 结点对象
	 */
	public Node getNodeByGuid(String guid);

	/**
	 * 获得子结点列表
	 * 
	 * @param parentId
	 *            父结点Id
	 * @return 子结点列表
	 */
	public List getNodes(Long parentId);

	/**
	 * 获得子结点列表
	 * 
	 * @param system
	 *            是否是系统结点
	 * @param parentId
	 *            父结点Id
	 * @return 子结点列表
	 */
	public List getNodes(Integer system, Long parentId);

	/**
	 * 获得子结点列表
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param disabled
	 *            是否可用
	 * @return 子结点列表
	 */
	public List getNodes(Long parentId, Integer disabled);

	/**
	 * 获得子结点列表
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @return 子结点列表
	 */
	public List getNodes(Long parentId, Long rootId);

	/**
	 * 获得子结点列表
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param disabled
	 *            是否可用
	 * @param system
	 *            是否是系统结点
	 * @return 子结点列表
	 */
	public List getNodes(Long parentId, Integer disabled, Integer system);

	/**
	 * 获得子结点列表
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @param disabled
	 *            是否可用
	 * @return 子结点列表
	 */
	public List getNodes(Long parentId, Long rootId, Integer disabled);

	/**
	 * 获得子结点列表
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @param disabled
	 *            是否可用
	 * @param nodeType
	 *            结点类型
	 * @return 子结点列表
	 */
	public List getNodes(Long parentId, Long rootId, Integer disabled, Integer nodeType);

	/**
	 * 获得子结点列表
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @param disabled
	 *            是否可用
	 * @param nodeType
	 *            结点类型
	 * @param system
	 *            是否系统结点
	 * @return 子结点列表
	 */
	public List getNodes(Long parentId, Long rootId, Integer disabled, Integer nodeType,
			Integer system);

	/**
	 * 获得结点列表
	 * 
	 * @param hql
	 *            查询语句
	 * @param args
	 *            参数
	 * @return 子结点列表
	 */
	public List getNodes(String hql, Object[] args);

	/**
	 * 获得回收站结点列表
	 * 
	 * @return 回收站结点列表
	 */
	public List getRecycleBinNodes();

	/**
	 * 获得所有结点对象的列表
	 * 
	 * @return 所有结点对象的列表
	 */
	public List getAllNodes();

	/**
	 * 获得所有结点数量
	 * 
	 * @return 结点数量
	 */
	public long getAllNodeCount();

	/**
	 * 获得子结点数量
	 * 
	 * @param parentId
	 *            父结点Id
	 * @return 子结点数量
	 */
	public long getNodeCount(Long parentId);

	/**
	 * 获得结点数量
	 * 
	 * @param system
	 *            是否是系统结点
	 * @param parentId
	 *            父结点Id
	 * @return 子结点数量
	 */
	public long getNodeCount(Integer system, Long parentId);

	/**
	 * 获得结点数量
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param disabled
	 *            是否可用
	 * @return 子结点数量
	 */
	public long getNodeCount(Long parentId, Integer disabled);

	/**
	 * 获得结点数量
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @return 子结点数量
	 */
	public long getNodeCount(Long parentId, Long rootId);

	/**
	 * 获得结点数量
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param disabled
	 *            是否可用
	 * @param system
	 *            是否系统结点
	 * @return 子结点数量
	 */
	public long getNodeCount(Long parentId, Integer disabled, Integer system);

	/**
	 * 获得结点数量
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @param disabled
	 *            是否可用
	 * @return 子结点数量
	 */
	public long getNodeCount(Long parentId, Long rootId, Integer disabled);

	/**
	 * 获得结点数量
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @param disabled
	 *            是否可用
	 * @param nodeType
	 *            结点类型
	 * @return 子结点数量
	 */
	public long getNodeCount(Long parentId, Long rootId, Integer disabled, Integer nodeType);

	/**
	 * 获得结点数量
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param rootId
	 *            根结点Id
	 * @param disabled
	 *            是否可用
	 * @param nodeType
	 *            结点类型
	 * @param system
	 *            是否系统结点
	 * @return 子结点数量
	 */
	public long getNodeCount(Long parentId, Long rootId, Integer disabled, Integer nodeType,
			Integer system);

	/**
	 * 获得所有子结点Id
	 * 
	 * @param parentNodeId
	 *            父结点Id
	 * @return 子结点Id列表
	 */
	public List getAllChildNodeId(Long parentNodeId);

	/**
	 * 结点放入回收站
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param containChild
	 *            是否包含子结点
	 */
	public void recycleNode(Long nodeId, boolean containChild);

	/**
	 * 清空回收站
	 * 
	 */
	public void deleteRecycleBinNodes();

	/**
	 * 从回收站恢复结点
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param containChild
	 *            是否包含子结点
	 */
	public void unRecycleNode(Long nodeId, boolean containChild);

	/**
	 * 删除结点
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param containChild
	 *            是否包含子结点
	 */
	public void deleteNode(Long nodeId, boolean containChild);

	/**
	 * 判断是否包含指定的结点id
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param tableId
	 *            内容模型Id
	 * @param nodeIds
	 *            结点集合
	 * @param containNodeIds
	 *            包含的结点Id集合
	 * @return boolean 是否包含
	 */
	public boolean getContainTableIdNodes(Long nodeId, Long tableId, List nodeIds,
			List containNodeIds);

	/**
	 * 是否是子结点
	 * 
	 * @param parentId
	 *            父结点Id
	 * @param nodeId
	 *            子结点Id
	 * @return 是否是子结点
	 */
	public boolean getIsChildNode(Long parentId, Long nodeId);
	/**
	 * 更新结点内容数目
	 * @param nodeId 结点Id
	 * @param type +,-,=
	 * @param count 改变的内容数目
	 */
	public void updateNodeContentCount(Long nodeId,String type,long count);

}
