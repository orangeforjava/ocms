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
package org.openuap.cms.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.search.SearchCommand;
import org.openuap.cms.search.SearchEngine;
import org.openuap.cms.search.SearchResults;

/**
 * <p>
 * 数据库搜索实现.
 * </p>
 * 
 * <p>
 * $Id: DBSearchEngineImpl.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DBSearchEngineImpl implements SearchEngine {

	private DynamicContentManager dynamicContentManager;

	private NodeManager nodeManager;

	private ContentTableManager contentTableManager;

	/**
	 * 
	 */
	public SearchResults doSearch(SearchCommand searchCommand) throws Exception {
		//
		long startTime = System.currentTimeMillis();
		String num = searchCommand.getPageNum();
		int page = searchCommand.getPage();
		String where = searchCommand.getWhere();
		String order = searchCommand.getOrder();
		String fields = searchCommand.getFields();
		String keyword = searchCommand.getKeyword();
		String nodeId = searchCommand.getNodeId();
		String nodeGUID = searchCommand.getNodeGUID();
		String sTableId = searchCommand.getTableId();
		String ignore = searchCommand.getIgnore();
		//
		Integer start = new Integer(0);
		Integer limit = new Integer(20);

		//
		if (!num.equals("")) {
			int pos = num.indexOf("page");
			if (pos == 0) {
				// such as page-15
				String pageNum = num.substring(5);
				int iPageNum = Integer.parseInt(pageNum);
				limit = iPageNum;
				start = new Integer((page - 1) * limit.intValue());
			} else {
				// such as limit 0,10
				//
				String[] se = num.split(",");
				if (se != null) {
					//

					if (se.length == 2) {
						start = Integer.parseInt(se[0]);
						;
						int end = Integer.parseInt(se[1]);
						limit = end - start;
					} else {
						start = 0;
						int end = Integer.parseInt(se[0]);
						limit = end;
					}
				}
			}
		}
		//
		QueryInfo qi = new QueryInfo(where, order, limit, start);
		PageBuilder pb = new PageBuilder(limit);
		//
		Map sc=getSearchCondition(fields, keyword,
				nodeId, nodeGUID, sTableId, ignore);
		if(sc==null){
			return null;
		}
		String publishTableName=(String) sc.get("publishTableName");
		String condition=(String) sc.get("condition");
		String hql = "select p,co from " + publishTableName
				+ " as p,CmsCount as co";
		String hql2 = "select count(p) from " + publishTableName
				+ " as p,CmsCount as co";
		hql += " where " + " p.indexId=co.indexId ";
		hql2 += " where " + " p.indexId=co.indexId ";
		if(condition!=null){
			hql+=condition;
			hql2+=condition;
		}
		// 5)order by process
		if (!order.equals("")) {
			hql += " order by " + order;
		} else {
			hql += " order by p.top DESC,p.sort DESC,p.publishDate DESC";
		}
		//
		List pcList = this.dynamicContentManager.getListContent(hql, hql2, qi,
				pb);
		List mergeList = new ArrayList();
		if (pcList != null) {
			for (int i = 0; i < pcList.size(); i++) {
				// merge the two map
				Map p = (Map) ((Object[]) pcList.get(i))[0];
				Map co = (Map) ((Object[]) pcList.get(i))[1];
				//
				p.put("hitsTotal", co.get("hitsTotal"));
				p.put("hitsToday", co.get("hitsToday"));
				p.put("hitsWeek", co.get("hitsWeek"));
				p.put("hitsMonth", co.get("hitsMonth"));
				p.put("commentNum", co.get("commentNum"));
				p.put("hitsDate", co.get("hitsDate"));
				//
				mergeList.add(p);
			}
		}
		pb.page(page);
		long searchTime = System.currentTimeMillis() - startTime;
		SearchResults rs = new SearchResults(mergeList, pb, searchTime);
		return rs;
	}

	/**
	 * @param dynamicContentManager
	 */
	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public PageBuilder doSearchCount(SearchCommand searchCommand)
			throws Exception {
		long startTime = System.currentTimeMillis();
		String num = searchCommand.getPageNum();
		int page = searchCommand.getPage();
		String where = searchCommand.getWhere();
		String order = searchCommand.getOrder();
		String fields = searchCommand.getFields();
		String keyword = searchCommand.getKeyword();
		String nodeId = searchCommand.getNodeId();
		String nodeGUID = searchCommand.getNodeGUID();
		String sTableId = searchCommand.getTableId();
		String ignore = searchCommand.getIgnore();
		//
		Integer start = new Integer(0);
		Integer limit = new Integer(20);
		//
		QueryInfo qi = new QueryInfo(where, order, limit, start);
		PageBuilder pb = new PageBuilder(limit);
		//
		Map sc=getSearchCondition(fields, keyword,
				nodeId, nodeGUID, sTableId, ignore);
		if(sc==null){
			return null;
		}
		String publishTableName=(String) sc.get("publishTableName");
		String condition=(String) sc.get("condition");
		String hql2 = "select count(p) from " + publishTableName
				+ " as p,CmsCount as co";
		hql2 += " where " + " p.indexId=co.indexId ";
		if(condition!=null){
			hql2+=condition;
		}
		dynamicContentManager.getObjectCount(hql2, qi,pb);
		return pb;
	}

	public Map getSearchCondition(String fields, String keyword,
			String nodeId, String nodeGUID, String sTableId, String ignore) {

		String column_condition = "";
		Long tableId = null;
		String guid = "";
		Node firstNode = null;
		String nodeId_condition = "";
		//
		if (fields != null && !fields.equals("")) {
			String columns[] = fields.split(",");
			if (columns != null) {
				for (int i = 0; i < columns.length; i++) {
					column_condition += " or p." + columns[i] + " like '%"
							+ keyword + "%'";

				}
				if (!column_condition.equals("")) {
					column_condition = column_condition.substring(4);
					column_condition = " (" + column_condition + ")";
				}
			}
		}
		if (nodeId.equals("")) {
			// the nodeId is empty,you can assign the nodeGUID or TableID
			if (nodeGUID.equals("")) {
				if (!sTableId.equals("")) {
					tableId = new Long(sTableId);
				}
			} else {
				guid = nodeGUID.trim();
				firstNode = nodeManager.getNodeByGuid(guid);
				tableId = firstNode.getTableId();
				nodeId_condition = "( p.nodeId=" + firstNode.getNodeId() + " )";
			}
		} else {
			// the nodeId condition
			if (nodeId.indexOf(",") > -1) {
				// such as 1,2,3,5
				String[] nodeIds = nodeId.split(",");
				if (nodeIds != null) {
					String firstNodeId = nodeIds[0];
					firstNode = nodeManager.getNodeById(new Long(firstNodeId));
					tableId = firstNode.getTableId();
					nodeId_condition = "(";
					for (int i = 0; i < nodeIds.length - 1; i++) {
						nodeId_condition += " p.nodeId=" + nodeIds[i] + " or ";
					}
					nodeId_condition += " p.nodeId="
							+ nodeIds[nodeIds.length - 1] + " )";
				}
			} else if (nodeId.indexOf("all-") == 0) {
				// such as all-10
				String allNodeId = nodeId.substring(4);
				firstNode = nodeManager.getNodeById(new Long(allNodeId));
				tableId = firstNode.getTableId();
				// get all child Node,include the self
				Long iallNodeId = new Long(allNodeId);
				List allNodeIdList = nodeManager.getAllChildNodeId(iallNodeId);
				int size = allNodeIdList.size();
				if (size > 0) {
					nodeId_condition = "(";
					for (int i = 0; i < size - 1; i++) {
						nodeId_condition += " p.nodeId=" + allNodeIdList.get(i)
								+ " or ";
					}
					nodeId_condition += " p.nodeId="
							+ allNodeIdList.get(size - 1) + " )";
				}
			} else {
				// such as 38,single node
				nodeId_condition += " (p.nodeId=" + nodeId + " )";
				Node n = nodeManager.getNodeById(new Long(nodeId));
				tableId = n.getTableId();
			}
		}
		// 2)ignore process
		String ignore_condition = "";
		if (!ignore.equals("")) {
			String[] inIds = ignore.split(",");
			if (inIds != null) {
				ignore_condition = "(";
				for (int i = 0; i < inIds.length - 1; i++) {
					ignore_condition += " p.nodeId<>" + inIds[i] + " and ";
				}
				ignore_condition += " p.nodeId<>" + inIds[inIds.length - 1]
						+ " )";
			}
		}
		String condition = "";
		if (!nodeId_condition.equals("")) {
			condition += " and " + nodeId_condition + " ";
		}
		if (!column_condition.equals("")) {
			condition += " and " + column_condition + " ";
		}
		if (!ignore_condition.equals("")) {
			condition += " and " + ignore_condition + " ";
		}
		ContentTable ct = contentTableManager.getContentTableById(tableId);
		//
		//
		String publishTableName = "";
		if (ct != null) {
			String tableName = ct.getEntityName();
			//
			if (tableName != null && !tableName.trim().equals("")) {
				// contentTableName = tableName;
				publishTableName = tableName + "Publish";
			} else {
				publishTableName = "Publish_" + tableId;
			}
		} else {
			return null;
		}
		Map rs=new HashMap(2);
		rs.put("publishTableName", publishTableName);
		rs.put("condition", condition);
		return rs;
	}

}
