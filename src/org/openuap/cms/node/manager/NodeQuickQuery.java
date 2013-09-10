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
package org.openuap.cms.node.manager;

import java.util.ArrayList;
import java.util.List;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.node.model.Node;
import org.openuap.runtime.plugin.WebPluginManagerUtils;

/**
 * <p>
 * 结点快速查询帮助类
 * </p>
 * 
 * <p>
 * $Id: NodeQuickQuery.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeQuickQuery {
	
	private List<Node> nodes;

	public NodeQuickQuery(List nodes) {
		this.nodes = nodes;
	}

	public Node getNode(Long nodeId) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM org.openuap.cms.node.model.Node WHERE disabled=0 AND nodeId="
				+ nodeId;
		try {
			q.parse(osql);
			results = q.execute(nodes);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return (Node) rs.get(0);
		}
		return null;
	}

	public List getNodeChildren(Long parentId) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM org.openuap.cms.node.model.Node WHERE disabled=0 AND parentId="
				+ parentId+" ORDER BY nodeSort DESC";
		try {
			q.parse(osql);
			results = q.execute(nodes);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return rs;
		}
		
		return  new ArrayList();
	}
	public int getNodeChildrenCount(Long parentId) {
		
		Query q = new Query();
		QueryResults results = null;
		q.setClassLoader(WebPluginManagerUtils.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		
		List rs = null;
		String osql = "SELECT * FROM org.openuap.cms.node.model.Node WHERE disabled=0 AND parentId="
				+ parentId;
		try {
			q.parse(osql);
			results = q.execute(nodes);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return rs.size();
		}
		return 0;
	}
	/**
	 * 获得回收站内的结点
	 * @return
	 */
	public List getRecycleBinNodes() {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM org.openuap.cms.node.model.Node WHERE disabled=1";
		try {
			q.parse(osql);
			results = q.execute(nodes);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return rs;
		}
		
		return  new ArrayList();
	}
	public List getNodes() {
		return nodes;
	}

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}
	public boolean getContainTableIdNodes(Long nodeId, Long tableId, List nodeIds,
			List containNodeIds){
		String osql = "SELECT * from org.openuap.cms.node.model.Node where parentId=:nid and tableId=:tid and disabled<>1 order by nodeSort";
		String osql2 = "SELECT * from org.openuap.cms.node.model.Node where parentId=:nid and disabled<>1 order by nodeSort";
		String osql3 = "SELECT * from org.openuap.cms.node.model.Node where nodeId=:nid and tableId=:tid and disabled<>1 ";
		boolean contain = false;
		boolean childContain = false;
		boolean selfContain = false;
		//
		try {
			Query q3 = new Query();
			q3.parse(osql3);
			q3.setVariable("nid", nodeId);
			q3.setVariable("tid", tableId);
			QueryResults qr=null;
			qr=q3.execute(nodes);
			//Object obj = this.findUniqueResult(hql3, new Object[] { nodeId, tableId });
			if (qr!= null) {
				nodeIds.add(nodeId);
				containNodeIds.add(nodeId);
				selfContain = true;
			}
			Query q1 = new Query();
			q1.parse(osql);
			q1.setVariable("nid", nodeId);
			q1.setVariable("tid", tableId);
			qr=q1.execute(nodes);
			//List okNodes = this.executeFind(hql, new Object[] { nodeId, tableId });
			if (qr != null) {
				List<Node> okNodes=qr.getResults();
				for (int i = 0; i < okNodes.size(); i++) {
					Node okNode = (Node) okNodes.get(i);
					nodeIds.add(okNode.getNodeId());
					containNodeIds.add(nodeId);
				}
				contain = true;
			}
			//
			Query q2= new Query();
			q2.parse(osql2);
			q2.setVariable("nid", nodeId);
			//
			qr=q2.execute(nodes);
			//List childNodes = this.executeFind(hql2, new Object[] { nodeId });
			if (qr != null) {
				List<Node> childNodes=qr.getResults();
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
		} catch (QueryParseException e) {
			e.printStackTrace();
			return false;
		} catch (QueryExecutionException e) {
			
			e.printStackTrace();
			return false;
		}
	}
}
