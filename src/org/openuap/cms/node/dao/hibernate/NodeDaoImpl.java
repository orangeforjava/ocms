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
package org.openuap.cms.node.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.node.dao.NodeDao;
import org.openuap.cms.node.model.Node;

/**
 * <p>
 * 结点Dao Hibernate实现
 * </p>
 * 
 * <p>
 * $Id: NodeDaoImpl.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeDaoImpl extends BaseDaoHibernate implements NodeDao {

	public NodeDaoImpl() {
	}

	public Long addNode(Node node) {
		return (Long) this.addObject(node);
	}

	public void saveNode(Node node) {
		this.saveObject(node);
	}

	public void deleteNode(Long nodeId) {
		Node node = this.getNodeById(nodeId);
		this.deleteObject(node);
	}

	public Node getNodeById(Long nodeId) {
		return (Node) this.findUniqueResult("from Node where nodeId=?", new Object[] { nodeId });
	}

	public Node getNodeByGuid(String guid) {
		return (Node) this.findUniqueResult("from Node where nodeGuid=?", new Object[] { guid });
	}

	public List getNodes(Long parentId) {
		return this.executeFind("from Node where parentId=?", new Object[] { parentId });
	}

	public List getNodes(Long parentId, Long rootId) {
		return this.executeFind("from Node where parentId=? and rootId=? order by nodeSort desc",
				new Object[] { parentId, rootId });
	}

	public List getNodes(Long parentId, Long rootId, Integer disabled) {
		List nodes = this.executeFind(
				"from Node where parentId=? and rootId=? and disabled=? order by nodeSort desc",
				new Object[] { parentId, rootId, disabled });
		return nodes;
	}

	public List getNodes(String hql, Object[] args) {
		return this.executeFind(hql, args);
	}

	public long getAllNodeCount() {
		return ((Number) this.getHibernateTemplate().iterate("select count(*) from Node").next())
				.longValue();
	}

	public long getNodeCount(Long parentId) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Node where parentId=?", new Object[] { parentId }).next())
				.longValue();
	}

	public long getNodeCount(Long parentId, Long rootId) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Node where parentId=? and rootId=?",
				new Object[] { parentId, rootId }).next()).longValue();

	}

	public long getNodeCount(Long parentId, Integer disabled) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Node where parentId=? and disabled=?",
				new Object[] { parentId, disabled }).next()).longValue();
	}

	public long getNodeCount(Long parentId, Long rootId, Integer disabled) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Node where parentId=? and rootId=? and disabled=?",
				new Object[] { parentId, rootId, disabled }).next()).longValue();

	}

	public List getNodes(Long parentId, Long rootId, Integer disabled, Integer nodeType) {
		List nodes = this
				.executeFind(
						"from Node where parentId=? and rootId=? and disabled=? and nodeType=? order by nodeSort desc",
						new Object[] { parentId, rootId, disabled, nodeType });
		return nodes;
	}

	public long getNodeCount(Long parentId, Long rootId, Integer disabled, Integer nodeType) {
		long count = ((Number) this
				.getHibernateTemplate()
				.iterate(
						"select count(*) from Node where parentId=? and rootId=? and disabled=? and nodeType=?",
						new Object[] { parentId, rootId, disabled, nodeType }).next()).longValue();
		return count;
	}
	/**
	 * 获得所有子结点id,包括自身
	 */
	public List getAllChildNodeId(Long parentNodeId) {
		List<Long> idList = new ArrayList<Long>();
		idList.add(parentNodeId);
		getChildNodeIds(parentNodeId, idList);
		return idList;
	}

	private void getChildNodeIds(Long parentNodeId, List<Long> idList) {
		List ids = this.executeFind("select nodeId from Node where parentId=? "
				+ "and disabled=0 order by nodeSort desc", new Object[] { parentNodeId });
		//
		if (ids != null) {
			for (int i = 0; i < ids.size(); i++) {
				Long id = (Long) ids.get(i);
				idList.add(id);
				getChildNodeIds(id, idList);
			}
		}

	}

	public void recycleNode(Long nodeId, boolean containChild) {
		Node node = this.getNodeById(nodeId);
		if (node != null) {
			//设置为禁用状态
			node.setDisabled(new Integer("1"));
			this.saveNode(node);
		}
		if (containChild) {
			List childNodes = getNodes(nodeId, new Integer("0"));
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node child = (Node) childNodes.get(i);
					recycleNode(child.getNodeId(), containChild);
				}
			}
		}

	}

	public void unRecycleNode(Long nodeId, boolean containChild) {
		Node node = this.getNodeById(nodeId);
		if (node != null) {
			node.setDisabled(new Integer("0"));
			this.saveNode(node);
		}
		if (containChild) {
			List childNodes = getNodes(nodeId, new Integer("1"));
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node child = (Node) childNodes.get(i);
					unRecycleNode(child.getNodeId(), containChild);
				}
			}
		}

	}

	public void deleteNode(Long nodeId, boolean containChild) {
		this.deleteNode(nodeId);
		if (containChild) {
			List childNodes = getNodes(nodeId);
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node child = (Node) childNodes.get(i);
					deleteNode(child.getNodeId(), containChild);
				}
			}

		}
	}

	public boolean getContainTableIdNodes(Long nodeId, Long tableId, List nodeIds,
			List containNodeIds) {
		String hql = "from Node where parentId=? and tableId=? and disabled<>1 order by nodeSort";
		String hql2 = "from Node where parentId=? and disabled<>1 order by nodeSort";
		String hql3 = "from Node where nodeId=? and tableId=? and disabled<>1 ";
		boolean contain = false;
		boolean childContain = false;
		boolean selfContain = false;
		Object obj = this.findUniqueResult(hql3, new Object[] { nodeId, tableId });
		if (obj != null) {
			nodeIds.add(nodeId);
			containNodeIds.add(nodeId);
			selfContain = true;
		}
		List okNodes = this.executeFind(hql, new Object[] { nodeId, tableId });
		if (okNodes != null && okNodes.size() > 0) {
			for (int i = 0; i < okNodes.size(); i++) {
				Node okNode = (Node) okNodes.get(i);
				nodeIds.add(okNode.getNodeId());
				containNodeIds.add(nodeId);
			}
			contain = true;
		}
		//
		List childNodes = this.executeFind(hql2, new Object[] { nodeId });
		if (childNodes != null && childNodes.size() > 0) {
			for (int i = 0; i < childNodes.size(); i++) {
				Node childNode = (Node) childNodes.get(i);
				childContain = getContainTableIdNodes(childNode.getNodeId(), tableId, nodeIds,
						containNodeIds);
			}
		}
		if (selfContain || contain || childContain) {
			if (!containNodeIds.contains(nodeId)) {
				containNodeIds.add(nodeId);
			}
			return true;
		} else {
			return false;
		}
	}

	public List getRecycleBinNodes() {
		List nodes = this.executeFind("from Node where disabled=? order by nodeSort desc",
				new Object[] { new Integer("1") });
		return nodes;
	}

	public void deleteRecycleBinNodes() {
		this.executeUpdate("delete from Node where disabled=?", new Object[] { new Byte("1") });
	}

	public boolean getIsChildNode(Long parentId, Long nodeId) {

		List childNodes = this.getNodes(parentId);
		if (childNodes != null) {
			for (int i = 0; i < childNodes.size(); i++) {
				Node childNode = (Node) childNodes.get(i);
				if (childNode.getNodeId().equals(nodeId)) {
					return true;
				}
			}
			for (int i = 0; i < childNodes.size(); i++) {
				Node childNode = (Node) childNodes.get(i);
				return getIsChildNode(childNode.getNodeId(), nodeId);
			}
		}
		return false;
	}

	public List getNodes(Long parentId, Integer disabled, Integer system) {
		return this.executeFind("from Node where parentId=? and disabled=? and system=?",
				new Object[] { parentId, disabled, system });
	}

	public List getNodes(Long parentId, Long rootId, Integer disabled, Integer nodeType,
			Integer system) {
		List nodes = this
				.executeFind(
						"from Node where parentId=? and rootId=? and disabled=? and nodeType=? and system=? order by nodeSort desc",
						new Object[] { parentId, rootId, disabled, nodeType, system });
		return nodes;
	}

	public List getNodes(Long parentId, Integer disabled) {
		return this.executeFind("from Node where parentId=? and disabled=? order by nodeSort desc",
				new Object[] { parentId, disabled });
	}

	public List getNodes(Integer system, Long parentId) {
		return this.executeFind("from Node where parentId=? and system=? order by nodeSort desc",
				new Object[] { parentId, system });
	}

	public long getNodeCount(Integer system, Long parentId) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Node where parentId=? and system=?",
				new Object[] { parentId, system }).next()).longValue();

	}

	public long getNodeCount(Long parentId, Integer disabled, Integer system) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Node where parentId=? and disabled=? and system=?",
				new Object[] { parentId, disabled, system }).next()).longValue();

	}

	public long getNodeCount(Long parentId, Long rootId, Integer disabled, Integer nodeType,
			Integer system) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Node where parentId=? and rootId=? and disabled=?"
						+ " and nodeType=? and system=?",
				new Object[] { parentId, rootId, disabled, nodeType, system }).next()).longValue();

	}

	public List getAllNodes() {
		String hql = "from Node order by nodeId";
		return this.executeFind(hql);
	}

	public void updateNodeContentCount(Long nodeId, String type, long count) {
		StringBuffer hql=new StringBuffer("update "+ENTITY_NAME+" set contentCount=");
		if("+".equals(type)){
			hql.append("contentCount+"+count);			
		}else if("-".equals(type)){
			hql.append("contentCount-"+count);	
		}else if("=".equals(type)){
			hql.append(count);
		}else{
			return;
		}
		//
		hql.append(" where nodeId=?");
		//GO!
		this.executeUpdate(hql.toString(),new Object[]{nodeId});
	}

}
