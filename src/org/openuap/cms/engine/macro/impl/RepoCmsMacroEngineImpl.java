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

package org.openuap.cms.engine.macro.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.FileUtil;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.publish.manager.ExtraPublishManager;
import org.openuap.cms.publish.model.ExtraPublish;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.util.PageInfo;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 基于存储引擎的宏引擎实现.
 * </p>
 * 
 * <p>
 * $Id: RepoCmsMacroEngineImpl.java 3966 2010-12-16 12:10:02Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class RepoCmsMacroEngineImpl extends AbstractCmsMacroEngine implements
		CmsMacroEngine {

	/** 结点 Manager. */
	private NodeManager nodeManager;

	/** 内容模型Manager. */
	private ContentTableManager contentTableManager;

	/** 内容属性Manager. */
	private ContentFieldManager contentFieldManager;

	private DynamicContentManager dynamicContentManager;

	private ExtraPublishManager extraPublishManager;

	private PsnManager psnManager;

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;

	}

	/**
	 * 
	 */
	public List getCmsContent(String indexId) {
		return this.getCmsContent(indexId, "");
	}

	public List getCmsContent(String indexId, String orderBy) {
		return this.getCmsContent(indexId, orderBy, false);
	}

	public List getCmsContent(String indexId, String orderBy, boolean preview) {
		// System.out.println("cms content indexId="+indexId);
		if (indexId != null) {
			Long tableId = null;
			Node firstNode = null;
			String indexId_condition = "";
			if (indexId.indexOf(",") > -1) {
				String[] sids = indexId.split(",");
				String firstIndexId = sids[0];
				// 获得此内容的ContentIndex对象
				Map ci = dynamicContentManager.getContentIndexMapById(new Long(
						firstIndexId));
				// 获得结点对象
				Long nodeId = (Long) ci.get("nodeId");
				firstNode = nodeManager.getNode(nodeId);
				tableId = firstNode.getTableId();
				// 拼写查询条件
				indexId_condition = "(";
				for (int i = 0; i < sids.length - 1; i++) {
					indexId_condition += " ci.indexId=" + sids[i] + " or ";
				}
				indexId_condition += " ci.indexId=" + sids[sids.length - 1]
						+ " )";
			} else {
				Map ci = dynamicContentManager.getContentIndexMapById(new Long(
						indexId));
				if (ci == null) {
					return Collections.EMPTY_LIST;
				}
				Long nodeId = (Long) ci.get("nodeId");
				firstNode = nodeManager.getNode(nodeId);
				tableId = firstNode.getTableId();
				indexId_condition = " ci.indexId=" + indexId;

			}
			// 获得内容模型信息
			ContentTable ct = contentTableManager
					.getContentTableFromCache(tableId);
			//
			String tblSuffix=preview?"":"Publish";
			String tblPrefix=preview?"Content_":"Publish_";
			//
			String publishTableName = "";
			if (ct != null) {
				String tableName = ct.getEntityName();

				if (tableName != null && !tableName.trim().equals("")) {
					publishTableName = tableName + tblSuffix;
				} else {
					publishTableName = tblPrefix+ tableId;
				}
			} else {
				return null;
			}

			String hql = "select ci,p from ContentIndex as ci,"
					+ publishTableName + " as p ";
			if(preview){
				hql += " where ci.contentId=p.contentId " + "";
			}else{
				hql += " where ci.indexId=p.indexId " + "";
			}
			hql += " and " + indexId_condition;
			if (orderBy != null && !orderBy.equals("")) {
				// FIX
				orderBy = parseCondition(orderBy);
				orderBy = parseCondition2(orderBy);
				hql += " order by " + orderBy;
			} else {

			}
			List pcList = this.dynamicContentManager
					.getListHql(hql, null, null);
			List mergeList = new ArrayList();
			if (pcList != null) {
				for (int i = 0; i < pcList.size(); i++) {
					// merge the two map
					Map ci = (Map) ((Object[]) pcList.get(i))[0];
					Map p = (Map) ((Object[]) pcList.get(i))[1];
					//
					p.put("hitsTotal", ci.get("hitsTotal"));
					p.put("hitsToday", ci.get("hitsToday"));
					p.put("hitsWeek", ci.get("hitsWeek"));
					p.put("hitsMonth", ci.get("hitsMonth"));
					p.put("commentNum", ci.get("commentNum"));
					p.put("hitsDate", ci.get("hitsDate"));
					//
					p.put("top", ci.get("top"));
					p.put("sort", ci.get("sort"));
					p.put("pink", ci.get("pink"));
					p.put("nodeId", ci.get("nodeId"));
					p.put("indexId", ci.get("indexId"));
					//
					mergeList.add(p);
				}
			}
			return mergeList;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * 获得分页信息
	 */
	public PageBuilder getCmsListPageInfo(String nodeID, String num,
			String nodeGUID, String orderBy, String where, String TableID,
			String ignore, String page, String url) {
		if (num == null || num.equals("")) {
			return null;
		}
		if (num.indexOf("page") != 0) {
			return null;
		}
		PageBuilder pageInfo = null;
		//
		Long tableId = null;
		String guid = null;
		String nodeId_condition = "";
		Node firstNode = null;
		// 1)nodeID process
		if (nodeID.equals("")) {
			// the nodeId is empty,you can assign the nodeGUID or TableID
			if (nodeGUID.equals("")) {
				if (!TableID.equals("")) {
					tableId = new Long(nodeGUID);
				}
			} else {
				guid = nodeGUID.trim();
				firstNode = nodeManager.getNodeByGuidFromCache(guid);
				tableId = firstNode.getTableId();
				nodeId_condition = "( ci.nodeId=" + firstNode.getNodeId()
						+ " )";
			}
		} else {
			// the nodeId condition
			if (nodeID.indexOf(",") > -1) {
				// such as 1,2,3,5
				String[] nodeIds = nodeID.split(",");
				if (nodeIds != null) {
					String firstNodeId = nodeIds[0];
					firstNode = nodeManager.getNode(new Long(firstNodeId));
					tableId = firstNode.getTableId();
					nodeId_condition = "(";
					for (int i = 0; i < nodeIds.length - 1; i++) {
						nodeId_condition += " ci.nodeId=" + nodeIds[i] + " or ";
					}
					nodeId_condition += " ci.nodeId="
							+ nodeIds[nodeIds.length - 1] + " )";
				}
			} else if (nodeID.indexOf("all-") == 0) {
				// such as all-10
				String allNodeId = nodeID.substring(4);
				firstNode = nodeManager.getNode(new Long(allNodeId));
				tableId = firstNode.getTableId();
				// get all child Node,include the self
				Long iallNodeId = new Long(allNodeId);
				List allNodeIdList = nodeManager.getAllChildNodeId(iallNodeId);
				int size = allNodeIdList.size();
				if (size > 0) {
					nodeId_condition = "(";
					for (int i = 0; i < size - 1; i++) {
						nodeId_condition += " ci.nodeId="
								+ allNodeIdList.get(i) + " or ";
					}
					nodeId_condition += " ci.nodeId="
							+ allNodeIdList.get(size - 1) + " )";
				}
			} else {
				// such as 38,single node
				nodeId_condition += " (ci.nodeId=" + nodeID + " )";
				Node n = nodeManager.getNode(new Long(nodeID));
				tableId = n.getTableId();
			}

		}
		//
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		//
		String contentTableName = "";
		String publishTableName = "";
		if (ct != null) {
			String tableName = ct.getEntityName();

			if (tableName != null && !tableName.trim().equals("")) {
				contentTableName = tableName;
				publishTableName = tableName + "Publish";
			} else {
				publishTableName = "Publish_" + tableId;
			}
		} else {
			return null;
		}

		String hql2 = "select count(ci) from ContentIndex as ci,"
				+ publishTableName + " as p";

		hql2 += " where ci.indexId=p.indexId ";
		if (!nodeId_condition.equals("")) {

			hql2 += " and " + nodeId_condition + " ";
		}
		// 2)ignore process
		String ignore_condition = "";
		if (!ignore.equals("")) {
			String[] inIds = ignore.split(",");
			if (inIds != null) {
				ignore_condition = "(";
				for (int i = 0; i < inIds.length - 1; i++) {
					ignore_condition += " ci.nodeId<>" + inIds[i] + " and ";
				}
				ignore_condition += " ci.nodeId<>" + inIds[inIds.length - 1]
						+ " )";
			}
		}
		if (!ignore_condition.equals("")) {

			hql2 += " and " + ignore_condition + " ";
		}
		// 4)where process
		if (!where.equals("")) {
			// FIX:兼容性处理，c.*要兼容成p.*
			where = parseCondition(where);
			where = parseCondition2(where);
			hql2 += " and (" + where + ") ";
		}

		// 3)limit process
		QueryInfo qi = null; // new QueryInfo();
		if (!num.equals("")) {
			int pos = num.indexOf("page");
			if (pos == 0) {
				// such as page-15
				String pageNum = num.substring(5);
				int iPageNum = Integer.parseInt(pageNum);
				if (page.equals("")) {
					page = "1";
				}
				int ipage = Integer.parseInt(page);
				qi = new QueryInfo();
				qi.setOffset(new Integer((ipage - 1) * iPageNum));
				qi.setLimit(new Integer(iPageNum));
				// create the pageInfo object
				long totalNum = this.dynamicContentManager.getLongHql(hql2,
						null);
				long totalPage = (totalNum / iPageNum);
				if (totalNum % iPageNum != 0) {
					totalPage++;
				}

				pageInfo = new PageBuilder();
				pageInfo.setUrl(url);
				pageInfo.page(ipage);
				pageInfo.items((int) totalNum);
				// pageInfo.setTotalPage(totalPage);
				pageInfo.itemsPerPage(iPageNum);
			}
		}
		return pageInfo;
	}

	/**
	 * 
	 * @param Type
	 *            String
	 * @param NodeID
	 *            String
	 * @param ignore
	 *            String
	 * @return List
	 */
	public List getCmsNodeList(String Type, String NodeID, String ignore) {
		List nodeList = null;
		if (Type.equals("sub") || Type.equals("son")) {
			// when type is "sub" or "son",NodeID only to be such as "1"
			// and not such as "1,2",the check should at macro
			// at here,think is a valid
			//
			//
			String hql = "select n from Node as n where n.parentId=" + NodeID
					+ " ";
			String ignore_condition = null;
			if (ignore != null && !ignore.equals("")) {
				String[] ignoreIds = ignore.split(",");

				if (ignoreIds != null) {
					ignore_condition = "(";
					for (int i = 0; i < ignoreIds.length - 1; i++) {
						ignore_condition += " n.nodeId <> " + ignoreIds[i]
								+ " and ";
					}
					ignore_condition += " n.nodeId <> "
							+ ignoreIds[ignoreIds.length - 1] + " )";
				}
			}
			//
			if (ignore_condition != null) {
				hql += " and " + ignore_condition;
			}
			hql += " order by nodeSort ";
			nodeList = this.dynamicContentManager.getListHql(hql, null, null);

			// return nodeList;
		} else if (Type.equals("set")) {
			String hql = "select n from Node as n "; // n.parentId=" + NodeID
			// + " ";
			String node_condition = null;
			String[] nodeIds = NodeID.split(",");

			if (nodeIds != null) {
				node_condition = "(";
				for (int i = 0; i < nodeIds.length - 1; i++) {
					node_condition += " n.nodeId= " + nodeIds[i] + " or ";
				}
				node_condition += " n.nodeId= " + nodeIds[nodeIds.length - 1]
						+ " )";
			}
			if (node_condition != null) {
				hql += " where " + node_condition;
			}
			//
			nodeList = this.dynamicContentManager.getListHql(hql, null, null);

			// return nodeList;

		} else if (Type.equals("parent")) {
			Long nid = new Long(NodeID);
			Node node = nodeManager.getNode(nid);
			if (node != null) {
				Long parentId = node.getParentId();
				if (parentId.intValue() == 0) {
					return null;
				} else {
					Node parentNode = nodeManager.getNode(parentId);
					if (parentNode != null) {
						Long ppid = parentNode.getParentId();
						String hql = "select n from Node as n where n.parentId="
								+ ppid + " ";
						String ignore_condition = null;
						if (ignore != null && !ignore.equals("")) {
							String[] ignoreIds = ignore.split(",");

							if (ignoreIds != null) {
								ignore_condition = "(";
								for (int i = 0; i < ignoreIds.length - 1; i++) {
									ignore_condition += " n.nodeId <> "
											+ ignoreIds[i] + " and ";
								}
								ignore_condition += " n.nodeId <> "
										+ ignoreIds[ignoreIds.length - 1]
										+ " )";
							}
						}
						//
						if (ignore_condition != null) {
							hql += " and " + ignore_condition;
						}
						hql += " order by nodeSort ";
						nodeList = this.dynamicContentManager.getListHql(hql,
								null, null);

					}
				}
			}
		} else if (Type.equals("brother")) {
			Long nid = new Long(NodeID);
			Node node = nodeManager.getNode(nid);
			if (node != null) {
				Long parentId = node.getParentId();
				String hql = "select n from Node as n where n.parentId="
						+ parentId + " ";
				String ignore_condition = null;
				if (ignore != null && !ignore.equals("")) {
					String[] ignoreIds = ignore.split(",");

					if (ignoreIds != null) {
						ignore_condition = "(";
						for (int i = 0; i < ignoreIds.length - 1; i++) {
							ignore_condition += " n.nodeId <> " + ignoreIds[i]
									+ " and ";
						}
						ignore_condition += " n.nodeId <> "
								+ ignoreIds[ignoreIds.length - 1] + " )";
					}
				}
				//
				if (ignore_condition != null) {
					hql += " and " + ignore_condition;
				}
				hql += " order by nodeSort ";
				nodeList = this.dynamicContentManager.getListHql(hql, null,
						null);

			}
		}

		if (nodeList != null) {
			// List rtList=new ArrayList();
			for (int i = 0; i < nodeList.size(); i++) {
				Map tmpnode = (Map) nodeList.get(i);
				Long tmpnid = (Long) tmpnode.get("nodeId");
				List navigation = new ArrayList();
				this.getNodeNavigation(tmpnid, navigation);
				tmpnode.put("navigation", navigation);
				// rtList.add(tmpnode);
			}
			// return rtList;
		}

		return nodeList;
	}

	/**
	 * 获得结点信息
	 */
	public Object getCmsNode(String type, String NodeID) {

		// String hql = "select n from Node as n where n.nodeId=?";
		Node node = null;
		Long nid = new Long(NodeID);
		List<Node> navigation = new ArrayList<Node>();
		//
		if (type.equals("parent")) {
			node = nodeManager.getNode(nid);
			if (node != null) {
				Long pid = node.getParentId();
				getNodeNavigation(pid, navigation);
				node.setNavigation(navigation);
			}
		} else {
			node = nodeManager.getNode(nid);
			if (node != null) {
				getNodeNavigation(nid, navigation);
				node.setNavigation(navigation);
			}
		}
		//
		return node;
	}

	/**
	 * 
	 * cms.list 宏逻辑实现
	 * 
	 * @param nodeID
	 * 
	 * @param num
	 * 
	 * @param nodeGUID
	 * 
	 * @param orderBy
	 * 
	 * @param where
	 * 
	 * @param TableID
	 * 
	 * @param ignore
	 * 
	 * @param page
	 * 
	 * @param url
	 * 
	 * @return the List,first element is pageInfo,second is the cms publish
	 *         content list
	 */
	public List getCmsList(String nodeID, String num, String nodeGUID,
			String orderBy, String where, String TableID, String ignore,
			String page, String url) {

		//
		List<Object> result = new ArrayList<Object>();
		PageInfo pageInfo = new PageInfo();
		//
		Long tableId = null;
		String guid = null;
		String nodeId_condition = "";
		Node firstNode = null;
		// 1)nodeID process
		if (nodeID.equals("")) {
			// the nodeId is empty,you can assign the nodeGUID or TableID
			if (nodeGUID.equals("")) {
				if (!TableID.equals("")) {
					//
					tableId = new Long(TableID);
				}
			} else {
				guid = nodeGUID.trim();
				firstNode = nodeManager.getNodeByGuidFromCache(guid);
				if (firstNode == null) {
					return new ArrayList();
				}
				tableId = firstNode.getTableId();
				nodeId_condition = "( ci.nodeId=" + firstNode.getNodeId()
						+ " )";
			}
		} else {
			// the nodeId condition
			if (nodeID.indexOf(",") > -1) {
				// such as 1,2,3,5
				String[] nodeIds = nodeID.split(",");
				if (nodeIds != null) {
					String firstNodeId = nodeIds[0];
					firstNode = nodeManager.getNode(new Long(firstNodeId));
					if (firstNode == null) {
						return new ArrayList();
					}
					tableId = firstNode.getTableId();
					nodeId_condition = "(";
					for (int i = 0; i < nodeIds.length - 1; i++) {
						nodeId_condition += " ci.nodeId=" + nodeIds[i] + " or ";
					}
					nodeId_condition += " ci.nodeId="
							+ nodeIds[nodeIds.length - 1] + " )";
				}
			} else if (nodeID.indexOf("all-") == 0) {
				// such as all-10
				String allNodeId = nodeID.substring(4);
				firstNode = nodeManager.getNode(new Long(allNodeId));
				if (firstNode == null) {
					return new ArrayList();
				}
				tableId = firstNode.getTableId();
				// get all child Node,include the self
				Long iallNodeId = new Long(allNodeId);
				List allNodeIdList = nodeManager.getAllChildNodeId(iallNodeId);
				int size = allNodeIdList.size();
				if (size > 0) {
					nodeId_condition = "(";
					for (int i = 0; i < size - 1; i++) {
						nodeId_condition += " ci.nodeId="
								+ allNodeIdList.get(i) + " or ";
					}
					nodeId_condition += " ci.nodeId="
							+ allNodeIdList.get(size - 1) + " )";
				}
			} else {
				// such as 38,single node
				nodeId_condition += " (ci.nodeId=" + nodeID + " )";
				Node n = nodeManager.getNode(new Long(nodeID));
				if (n == null) {
					return new ArrayList();
				}
				tableId = n.getTableId();
			}
		}
		//
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		//
		// String contentTableName = "";
		String publishTableName = "";
		if (ct != null) {
			String tableName = ct.getEntityName();

			if (tableName != null && !tableName.trim().equals("")) {
				// contentTableName = tableName;
				publishTableName = tableName + "Publish";
			} else {
				publishTableName = "Publish_" + tableId;
			}
		} else {
			return new ArrayList();
		}

		//
		String hql = "select ci,p from ContentIndex as ci," + publishTableName
				+ " as p ";
		String hql2 = "select count(ci) from ContentIndex as ci,"
				+ publishTableName + " as p ";
		hql += " where ci.indexId=p.indexId ";
		hql2 += " where ci.indexId=p.indexId ";
		if (!nodeId_condition.equals("")) {
			hql += " and " + nodeId_condition + " ";
			hql2 += " and " + nodeId_condition + " ";
		}
		// 2)ignore process
		String ignore_condition = "";
		if (!ignore.equals("")) {
			String[] inIds = ignore.split(",");
			if (inIds != null) {
				ignore_condition = "(";
				for (int i = 0; i < inIds.length - 1; i++) {
					ignore_condition += " ci.nodeId<>" + inIds[i] + " and ";
				}
				ignore_condition += " ci.nodeId<>" + inIds[inIds.length - 1]
						+ " )";
			}
		}
		if (!ignore_condition.equals("")) {
			hql += " and " + ignore_condition + " ";
			hql2 += " and " + ignore_condition + " ";
		}
		//
		// 4)where process
		if (!where.equals("")) {
			// FIX:兼容性处理，c.*要兼容成p.*
			where = parseCondition(where);
			where = parseCondition2(where);
			hql += " and (" + where + ") ";
			hql2 += " and (" + where + ") ";
		}

		// 3)limit process
		QueryInfo qi = null; // new QueryInfo();
		if (!num.equals("")) {
			int pos = num.indexOf("page");
			if (pos == 0) {
				// such as page-15
				String pageNum = num.substring(5);
				int iPageNum = Integer.parseInt(pageNum);
				if (page.equals("")) {
					page = "1";
				}
				int ipage = Integer.parseInt(page);
				qi = new QueryInfo();
				qi.setOffset(new Integer((ipage - 1) * iPageNum));
				qi.setLimit(new Integer(iPageNum));
				// create the pageInfo object
				long totalNum = this.dynamicContentManager.getLongHql(hql2,
						null);
				long totalPage = (totalNum / iPageNum);
				if (totalNum % iPageNum != 0) {
					totalPage++;
				}

				pageInfo = new PageInfo();
				//
				pageInfo.setUrl(url);
				pageInfo.page(ipage);
				pageInfo.items((int) totalNum);
				// pageInfo.setTotalPage(totalPage);
				pageInfo.itemsPerPage(iPageNum);
			} else {
				// such as limit 0,10
				//
				String[] se = num.split(",");
				if (se != null) {
					//
					qi = new QueryInfo();

					if (se.length == 2) {
						int offset = Integer.parseInt(se[0]);
						qi.setOffset(new Integer(offset));
						int end = Integer.parseInt(se[1]);
						qi.setLimit(new Integer(end - offset));
					} else {
						qi.setOffset(new Integer(0));
						int end = Integer.parseInt(se[0]);
						qi.setLimit(new Integer(end));
					}
				}
			}
		}
		// 5)order by process
		if (orderBy != null && !orderBy.equals("")) {
			orderBy = parseCondition(orderBy);
			orderBy = parseCondition2(orderBy);
			hql += " order by " + orderBy;
		} else {
			hql += " order by ci.top DESC,ci.sort DESC,ci.publishDate DESC";
		}
		result.add(0, pageInfo);
		List pcList = this.dynamicContentManager.getListHql(hql, null, qi);
		List mergeList = new ArrayList();
		if (pcList != null) {
			for (int i = 0; i < pcList.size(); i++) {
				// merge the two map
				Map ci = (Map) ((Object[]) pcList.get(i))[0];
				Map p = (Map) ((Object[]) pcList.get(i))[1];
				//
				p.put("hitsTotal", ci.get("hitsTotal"));
				p.put("hitsToday", ci.get("hitsToday"));
				p.put("hitsWeek", ci.get("hitsWeek"));
				p.put("hitsMonth", ci.get("hitsMonth"));
				p.put("commentNum", ci.get("commentNum"));
				p.put("hitsDate", ci.get("hitsDate"));
				//
				p.put("top", ci.get("top"));
				p.put("sort", ci.get("sort"));
				p.put("pink", ci.get("pink"));
				//
				mergeList.add(p);
			}
		}
		result.add(1, mergeList);
		return result;

	}

	/**
	 * 搜索标识调用
	 * 
	 * @param nodeID
	 *            String
	 * @param num
	 *            String
	 * @param nodeGUID
	 *            String
	 * @param orderBy
	 *            String
	 * @param where
	 *            String
	 * @param TableID
	 *            String
	 * @param ignore
	 *            String
	 * @param page
	 *            String
	 * @param url
	 *            String
	 * @param ignoreIndex
	 *            String
	 * @param keywords
	 *            String
	 * @param fields
	 *            String
	 * @return List
	 */
	public List getCmsSearchList(String nodeID, String num, String nodeGUID,
			String orderBy, String where, String TableID, String ignore,
			String page, String url, String ignoreIndex, String keywords,
			String fields) {

		//
		List result = new ArrayList();
		PageInfo pageInfo = new PageInfo();
		//
		Long tableId = null;
		String guid = null;
		String nodeId_condition = "";
		Node firstNode = null;
		// 1)nodeID process
		if (nodeID.equals("")) {
			// the nodeId is empty,you can assign the nodeGUID or TableID
			if (nodeGUID.equals("")) {
				if (!TableID.equals("")) {
					//
					tableId = new Long(TableID);
				}
			} else {
				guid = nodeGUID.trim();
				firstNode = nodeManager.getNodeByGuidFromCache(guid);
				if (firstNode == null) {
					return new ArrayList();
				}
				tableId = firstNode.getTableId();
				nodeId_condition = "( ci.nodeId=" + firstNode.getNodeId()
						+ " )";
			}
		} else {
			// the nodeId condition
			if (nodeID.indexOf(",") > -1) {
				// such as 1,2,3,5
				String[] nodeIds = nodeID.split(",");
				if (nodeIds != null) {
					String firstNodeId = nodeIds[0];
					firstNode = nodeManager.getNode(new Long(firstNodeId));
					if (firstNode == null) {
						return new ArrayList();
					}
					tableId = firstNode.getTableId();
					nodeId_condition = "(";
					for (int i = 0; i < nodeIds.length - 1; i++) {
						nodeId_condition += " ci.nodeId=" + nodeIds[i] + " or ";
					}
					nodeId_condition += " ci.nodeId="
							+ nodeIds[nodeIds.length - 1] + " )";
				}
			} else if (nodeID.indexOf("all-") == 0) {
				// such as all-10
				String allNodeId = nodeID.substring(4);
				firstNode = nodeManager.getNode(new Long(allNodeId));
				if (firstNode == null) {
					return new ArrayList();
				}
				tableId = firstNode.getTableId();
				// get all child Node,include the self
				Long iallNodeId = new Long(allNodeId);
				List allNodeIdList = nodeManager.getAllChildNodeId(iallNodeId);
				int size = allNodeIdList.size();
				if (size > 0) {
					nodeId_condition = "(";
					for (int i = 0; i < size - 1; i++) {
						nodeId_condition += " ci.nodeId="
								+ allNodeIdList.get(i) + " or ";
					}
					nodeId_condition += " ci.nodeId="
							+ allNodeIdList.get(size - 1) + " )";
				}
			} else {
				// such as 38,single node
				nodeId_condition += " (ci.nodeId=" + nodeID + " )";
				Node n = nodeManager.getNode(new Long(nodeID));
				if (n == null) {
					return new ArrayList();
				}
				tableId = n.getTableId();
			}
		}
		//
		ContentTable ct = contentTableManager.getContentTableFromCache(tableId);
		//
		String contentTableName = "";
		String publishTableName = "";
		if (ct != null) {
			String tableName = ct.getEntityName();

			if (tableName != null && !tableName.trim().equals("")) {
				contentTableName = tableName;
				publishTableName = tableName + "Publish";
			} else {
				publishTableName = "Publish_" + tableId;
			}
		} else {
			return new ArrayList();
		}
		//
		String keyword_condition = "";
		if (keywords != null && !keywords.equals("")) {
			//
			if (fields == null || fields.equals("")) {
				//
				ContentField cf = contentFieldManager
						.getKeywordsFieldFromCache(tableId);
				fields = cf.getFieldName();
			}
			String fieldAry[] = fields.split("[\\,]");
			int fsize = fieldAry.length;

			// keyword_condition = "(";
			String[] keywordArg = keywords.split("[\\s　]+");
			int size = keywordArg.length;
			for (int j = 0; j < fsize; j++) {
				String field = fieldAry[j];
				for (int i = 0; i < size; i++) {
					String kword = keywordArg[i];
					keyword_condition += " p." + field + " like '%" + kword
							+ "%' or";
				}
			}
			if (keyword_condition != null) {
				keyword_condition = keyword_condition.substring(0,
						keyword_condition.length() - 2);
				keyword_condition = "(" + keyword_condition + ")";
			}
		}
		//
		String hql = "select ci,p from ContentIndex as ci," + publishTableName
				+ " as p ";
		String hql2 = "select count(ci) from ContentIndex as ci,"
				+ publishTableName + " as p ";
		hql += " where ci.indexId=p.indexId ";
		hql2 += " where ci.indexId=p.indexId ";
		if (!nodeId_condition.equals("")) {
			hql += " and " + nodeId_condition + " ";
			hql2 += " and " + nodeId_condition + " ";
		}
		if (keyword_condition != null && !keyword_condition.equals("")) {
			//
			// System.out.println("keyword_condition="+keyword_condition);
			hql += " and " + keyword_condition + " ";
			hql2 += " and " + keyword_condition + " ";
		}
		// 2)ignore process
		String ignore_condition = "";
		if (!ignore.equals("")) {
			String[] inIds = ignore.split(",");
			if (inIds != null) {
				ignore_condition = "(";
				for (int i = 0; i < inIds.length - 1; i++) {
					ignore_condition += " ci.nodeId<>" + inIds[i] + " and ";
				}
				ignore_condition += " ci.nodeId<>" + inIds[inIds.length - 1]
						+ " )";
			}
		}
		String ignoreIndex_condition = null;
		if (ignoreIndex != null && !ignoreIndex.equals("")) {
			String[] inIds = ignoreIndex.split(",");
			if (inIds != null) {
				ignoreIndex_condition = "(";
				for (int i = 0; i < inIds.length - 1; i++) {
					ignoreIndex_condition += " ci.indexId<>" + inIds[i]
							+ " and ";
				}
				ignoreIndex_condition += " ci.indexId<>"
						+ inIds[inIds.length - 1] + " )";
			}
		}

		if (!ignore_condition.equals("")) {
			hql += " and " + ignore_condition + " ";
			hql2 += " and " + ignore_condition + " ";
		}
		//
		if (ignoreIndex_condition != null && !ignoreIndex_condition.equals("")) {
			hql += " and " + ignoreIndex_condition + " ";
			hql2 += " and " + ignoreIndex_condition + " ";
		}
		//
		// 4)where process
		if (!where.equals("")) {
			// FIX:兼容性处理，c.*要兼容成p.*
			where = parseCondition(where);
			where = parseCondition2(where);
			hql += " and (" + where + ") ";
			hql2 += " and (" + where + ") ";
		}

		// 3)limit process
		QueryInfo qi = null; // new QueryInfo();
		if (!num.equals("")) {
			int pos = num.indexOf("page");
			if (pos == 0) {
				// such as page-15
				String pageNum = num.substring(5);
				int iPageNum = Integer.parseInt(pageNum);
				if (page.equals("")) {
					page = "1";
				}
				int ipage = Integer.parseInt(page);
				qi = new QueryInfo();
				qi.setOffset(new Integer((ipage - 1) * iPageNum));
				qi.setLimit(new Integer(iPageNum));
				// create the pageInfo object
				long totalNum = this.dynamicContentManager.getLongHql(hql2,
						null);
				long totalPage = (totalNum / iPageNum);
				if (totalNum % iPageNum != 0) {
					totalPage++;
				}

				pageInfo = new PageInfo();
				//
				pageInfo.setUrl(url);
				pageInfo.page(ipage);
				pageInfo.items((int) totalNum);
				// pageInfo.setTotalPage(totalPage);
				pageInfo.itemsPerPage(iPageNum);
			} else {
				// such as limit 0,10
				//
				String[] se = num.split(",");
				if (se != null) {
					// @todo $Date: 2006/08/31 02:26:05 $
					qi = new QueryInfo();

					if (se.length == 2) {
						int offset = Integer.parseInt(se[0]);
						qi.setOffset(new Integer(offset));
						int end = Integer.parseInt(se[1]);
						qi.setLimit(new Integer(end - offset));
					} else {
						qi.setOffset(new Integer(0));
						int end = Integer.parseInt(se[0]);
						qi.setLimit(new Integer(end));
					}
				}
			}
		}
		// 5)order by process
		if (!orderBy.equals("")) {
			orderBy = parseCondition(orderBy);
			orderBy = parseCondition2(orderBy);
			hql += " order by " + orderBy;
		} else {
			hql += " order by ci.top DESC,ci.sort DESC,ci.publishDate DESC";
		}
		result.add(0, pageInfo);
		List pcList = this.dynamicContentManager.getListHql(hql, null, qi);
		List mergeList = new ArrayList();
		if (pcList != null) {
			for (int i = 0; i < pcList.size(); i++) {
				Map ci = (Map) ((Object[]) pcList.get(i))[0];
				Map p = (Map) ((Object[]) pcList.get(i))[1];
				//
				p.put("hitsTotal", ci.get("hitsTotal"));
				p.put("hitsToday", ci.get("hitsToday"));
				p.put("hitsWeek", ci.get("hitsWeek"));
				p.put("hitsMonth", ci.get("hitsMonth"));
				p.put("commentNum", ci.get("commentNum"));
				p.put("hitsDate", ci.get("hitsDate"));
				//
				p.put("top", ci.get("top"));
				p.put("sort", ci.get("sort"));
				p.put("pink", ci.get("pink"));
				//
				mergeList.add(p);
			}
		}
		result.add(1, mergeList);
		return result;
	}

	public void getNodeNavigation(Long nodeId, List<Node> navigation) {
		// String hql = "select n from Node as n where nodeId=? ";
		Node node = this.nodeManager.getNode(nodeId);
		if (node != null) {
			//
			Long parentId = (Long) node.getParentId();
			if (parentId != 0) {
				getNodeNavigation(parentId, navigation);
			}
			navigation.add(node);
		}

	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	/**
	 * 
	 */
	public String getExtraPublishPath(String id) {
		if (id != null) {
			Long publishId = new Long(id);
			ExtraPublish publish = this.extraPublishManager
					.getPublishById(publishId);
			if (publish != null) {
				String path = null;
				String psnUrl = publish.getSelfPsnUrl();
				String fileName = publish.getPublishFileName();
				String pattern = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)*(:[a-zA-Z0-9]*)?([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
				String psnUrlInfo = psnManager.getPsnUrlInfo(psnUrl);
				Pattern p = Pattern.compile(pattern);
				// System.out.println("psnUrlInfo=" + psnUrlInfo);
				Matcher m = p.matcher(psnUrlInfo);
				boolean found = m.find();
				if (found) {
					path = m.group(4);
					// System.out.println("path="+path);
					//
				}
				if (path == null) {
					path = "";
				}
				path = "/" + path + "/" + fileName;
				path = path.replaceAll("\\/\\/", "/");
				//
				return path;
			}
		}
		return "";
	}

	private String parseCondition(String where) {
		//
		int pos = where.indexOf("c.");
		if (pos >= 0) {
			String temp = where.substring(0, pos) + "p."
					+ where.substring(pos + 2, where.length());
			return parseCondition(temp);
		}
		return where;
	}

	private String parseCondition2(String where) {
		int pos = where.indexOf("co.");
		if (pos >= 0) {
			String temp = where.substring(0, pos) + "ci."
					+ where.substring(pos + 3, where.length());
			return parseCondition2(temp);
		}
		return where;
	}

	public void setExtraPublishManager(ExtraPublishManager extraPublishManager) {
		this.extraPublishManager = extraPublishManager;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public String getExtraPublishUrl(String id) {
		ExtraPublish publish = this.extraPublishManager
				.getPublishById(new Long(id));
		if (publish != null) {
			return publish.getUrl();
		}
		return null;
	}

	public static void main(String[] args) {
		// System.out.println(RepoCmsMacroEngineImpl.parseWhere(" c.Photo!=''
		// and ci.Top=1 and c.Photo!=''"));
	}

	public String getExtraPublishContent(String id) {
		ExtraPublish publish = this.extraPublishManager
				.getPublishById(new Long(id));
		String result = "";
		if (publish != null) {
			Long nid = publish.getNodeId();
			Node node = nodeManager.getNode(nid);

			if (publish == null || node == null) {
				return null;
			}
			// 不需要验证是否是静态发布？？
			//
			String encoding = this.getTemplateOutEncoding(node);
			File file = this.getExtraPublishFile(publish, node);
			if (!file.exists()) {
				// 文件不存在,立即生成
				List errors = new ArrayList();
				getPublishEngine().refreshNodeExtraIndex(node.getNodeId(),
						publish.getPublishId(), errors);
			}
			if (file.exists()) {
				try {
					result = FileUtil.readTextFile(file, encoding);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}

	/**
	 * 获得指定结点下的模板渲染后的输出编码
	 * 
	 * @param node
	 * @return
	 */
	private String getTemplateOutEncoding(Node node) {
		// 缺省采用UTF-8编码
		CMSConfig config = CMSConfig.getInstance();
		String encoding = config.getStringProperty("cms.tpl.out_encoding",
				"UTF-8");

		// 每个结点可以自己覆盖缺省的编码
		String tplEncoding = node.getTplEncoding();
		if (StringUtils.hasText(tplEncoding)) {
			encoding = tplEncoding;
		}
		return encoding;
	}

	/**
	 * 获得附加发布所对应的文件
	 * 
	 * @param publish
	 * @param node
	 * @return
	 */
	private File getExtraPublishFile(ExtraPublish publish, Node node) {
		CMSConfig config = CMSConfig.getInstance();
		String sysRootPath = config.getSysRootPath();
		//
		String fullPath = "";
		String relativePath = "";
		String publishFileName = "";
		//
		String pblPsn = publish.getSelfPsn();
		if (StringUtils.hasText(pblPsn)) {
			relativePath = getRelativePath(pblPsn);
		}
		//
		if (relativePath.equals("")) {
			pblPsn = node.getContentPsn();
			relativePath = getRelativePath(pblPsn);
		}
		//
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		//

		if (publish != null) {
			publishFileName = publish.getPublishFileName();
		}
		//
		File destFile = new File(fullPath, publishFileName);
		return destFile;
	}

	private String getRelativePath(String selfPsn) throws NumberFormatException {
		String relativePath = "";
		String sp = "\\{PSN:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(selfPsn);
		boolean result = m.find();
		while (result) {
			String path = m.group(2);
			String psnId = m.group(1);
			Psn psn = psnManager.getPsnFromCache(new Long(psnId));
			// String psnUrl = psn.getPsn();
			if (psn.getType() == Psn.LOCAL_PSN_TYPE) {
				// now,only process the local
				// remote will be do later.
				relativePath = psn.getLocalPath();
				relativePath += "/" + path;
			}
			result = m.find();
		} // end while result
		return relativePath;
	}

	public PublishEngine getPublishEngine() {
		PublishEngine publishEngine = (PublishEngine) ObjectLocator.lookup(
				"publishEngine", "org.openuap.cms");
		return publishEngine;
	}
}
