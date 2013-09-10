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
import java.util.List;
import java.util.Vector;

import org.compass.core.CompassAnalyzerHelper;
import org.compass.core.CompassCallback;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassException;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder;
import org.compass.core.CompassSession;
import org.compass.core.CompassToken;
import org.compass.core.CompassQueryBuilder.CompassBooleanQueryBuilder;
import org.compass.core.CompassQueryBuilder.CompassMultiPropertyQueryStringBuilder;
import org.openuap.base.dao.search.DynamicCompassDaoSupport;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.search.SearchCommand;
import org.openuap.cms.search.SearchEngine;
import org.openuap.cms.search.SearchResults;
import org.openuap.cms.search.util.CompassHitUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 基于索引的搜索引擎实现.
 * </p>
 * 
 * <p>
 * $Id: LuceneSearchEngineImpl.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class LuceneSearchEngineImpl extends DynamicCompassDaoSupport implements
		SearchEngine {

	private NodeManager nodeManager;

	private ContentTableManager contentTableManager;

	private ContentFieldManager contentFieldManager;

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public SearchResults doSearch(final SearchCommand searchCommand)
			throws Exception {
		//
		return (SearchResults) getCompassTemplate()
				.execute(new CompassCallback() {
							public Object doInCompass(CompassSession session)
									throws CompassException {
								return performSearch(searchCommand, session);
							}
						});
	}

	/**
	 * 执行搜索
	 * 
	 * @param searchCommand
	 * @param session
	 * @return
	 */
	protected SearchResults performSearch(SearchCommand searchCommand,
			CompassSession session) {
		
		//
		long time = System.currentTimeMillis();
		String num = searchCommand.getPageNum();
		int page = searchCommand.getPage();
		String highlights = searchCommand.getHighlights();
		//
		String[] aHighlight = null;
		if (StringUtils.hasText(highlights)) {
			aHighlight = highlights.split(",");
		} else {
			aHighlight = new String[] { "Content", "Title" };
		}
		String url = searchCommand.getUrl();
		int hSize = aHighlight.length;
		int start = 0;
		// limit若为-1，代表返回所有结果
		int limit = 20;
		//
		if (!num.equals("")) {
			int pos = num.indexOf("page");
			if (pos == 0) {
				// such as page-15
				String pageNum = num.substring(5);
				int iPageNum = Integer.parseInt(pageNum);
				limit = iPageNum;
				start = new Integer((page - 1) * limit);
			} else {
				// such as limit 0,10
				//
				String[] se = num.split(",");
				if (se != null) {
					//

					if (se.length == 2) {
						start = Integer.parseInt(se[0]);
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
		// 构造查询
		CompassQuery query = buildQuery(searchCommand, session);		
		// 执行搜索
		
		CompassHits hits = query.hits();
		CompassDetachedHits detachedHits = null;
		//
//		System.out.println("Lucene hits:" + hits.getLength() + ";耗时："
//				+ (System.currentTimeMillis() - time) + "ms");
		//
		PageBuilder pb = new PageBuilder(limit);
		pb.setUrl(url);
		if (limit == -1) {
			detachedHits = hits.detach();

		} else {
			int hitsLength = hits.getLength();
			pb.items(hitsLength);
			pb.page(page);
			start = pb.beginIndex();
			int end = pb.endIndex();
			int size = end - start + 1;

			if (start > 0) {
				for (int i = start - 1; i < end; i++) {
					for (int j = 0; j < hSize; j++) {
						String h = aHighlight[j];
						try {
							hits.highlighter(i).fragment(h);
						} catch (CompassException e) {
							//
						}
					}
					// hits.highlighter(i).fragment("Content");
					// hits.highlighter(i).fragment("Title");
				}
				detachedHits = hits.detach(start - 1, size);
			} else {
				detachedHits = hits.detach(0, 0);
			}
		}
		time = System.currentTimeMillis() - time;
		//转换内容为正确的Map类型
		List rs = new ArrayList();
		CompassHitUtils.convertHitsToMap(detachedHits.getHits(), rs,
				aHighlight);
		//
		SearchResults sr = new SearchResults(rs, pb, time);
		return sr;
	}

	/**
	 * 构造查询对象
	 * 
	 * @param searchCommand
	 * @param session
	 * @return
	 */
	protected CompassQuery buildQuery(SearchCommand searchCommand,
			CompassSession session) {
		//
		CompassQuery query = null;
		// 充分的支撑标记
		String where = searchCommand.getWhere();
		String order = searchCommand.getOrder();
		String fields = searchCommand.getFields();
		String keyword = searchCommand.getKeyword();
		String nodeId = searchCommand.getNodeId();
		String nodeGUID = searchCommand.getNodeGUID();
		String sTableId = searchCommand.getTableId();
		String ignore = searchCommand.getIgnore();
		String ignoreIndex = searchCommand.getIgnoreIndex();
		boolean parseKeyword=searchCommand.isParseKeyword();
		//
		Long tableId = null;
		String guid = "";
		Node firstNode = null;
		//
		CompassQueryBuilder queryBuilder = session.queryBuilder();
		CompassBooleanQueryBuilder keywordQueryBuilder = queryBuilder.bool();
		CompassBooleanQueryBuilder nodeIdQueryBuilder = queryBuilder.bool();
		// CompassBooleanQueryBuilder ignoreQueryBuilder = queryBuilder.bool();
		CompassBooleanQueryBuilder whereQueryBuilder = queryBuilder.bool();
		CompassBooleanQueryBuilder aliasQueryBuilder = queryBuilder.bool();
		//
		CompassBooleanQueryBuilder allQueryBuilder = queryBuilder.bool();
		boolean hasNodeId = false;
		// boolean hasIgnore = false;
		boolean hasWhere = false;
		boolean hasAlias = false;
		boolean hasKeyword = false;
		String alias = null;
		String keywordQuery = "";
		//
		Vector<String> vIgnore = new Vector<String>();

		// nodeId过滤
		if (!ignore.equals("")) {
			//
			String[] inIds = ignore.split(",");
			if (inIds != null) {
				for (int i = 0; i < inIds.length; i++) {
					vIgnore.add(inIds[i]);
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
				firstNode = nodeManager.getNodeByGuidFromCache(guid);
				if (firstNode != null) {
					// 此时只有一个结点，也就是说不存在所谓的忽略问题，因为忽略的本意是为了减少范围，如果按照这个意思，减少没了条件反而是搜索范围扩大了
					// 而这个忽略的本意是一定要排除，而且是前提条件是必须存在给定的结点范围
					// 没有范围就相当于无从忽略，忽略也就是无从谈起
					tableId = firstNode.getTableId();
					//
					Long nid = firstNode.getNodeId();
					hasNodeId = true;
					nodeIdQueryBuilder = nodeIdQueryBuilder
							.addMust(queryBuilder.term("nodeId", nid));
				}
			}
		} else {
			// the nodeId condition

			//
			if (nodeId.indexOf(",") > -1) {
				// such as 1,2,3,5
				String[] nodeIds = nodeId.split(",");
				if (nodeIds != null) {
					String firstNodeId = nodeIds[0];
					firstNode = nodeManager.getNode(new Long(firstNodeId));
					tableId = firstNode.getTableId();
					for (int i = 0; i < nodeIds.length; i++) {
						if (!vIgnore.contains(nodeIds[i])) {
							hasNodeId = true;
							nodeIdQueryBuilder = nodeIdQueryBuilder
									.addShould(queryBuilder.term("nodeId",
											nodeIds[i]));
						}
					}

				}
			} else if (nodeId.indexOf("all-") == 0) {
				// such as all-10
				String allNodeId = nodeId.substring(4);
				firstNode = nodeManager.getNode(new Long(allNodeId));
				tableId = firstNode.getTableId();
				// get all child Node,include the self
				Long iallNodeId = new Long(allNodeId);
				List allNodeIdList = nodeManager.getAllChildNodeId(iallNodeId);
				int size = allNodeIdList.size();
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						Long nid = (Long) allNodeIdList.get(i);
						if (!vIgnore.contains(nid.toString())) {
							hasNodeId = true;
							nodeIdQueryBuilder = nodeIdQueryBuilder
									.addShould(queryBuilder.term("nodeId",
											allNodeIdList.get(i)));
						}
					}
				}
			} else {
				// such as 38,single node
				if (!vIgnore.contains(nodeId)) {
					hasNodeId = true;
					nodeIdQueryBuilder = nodeIdQueryBuilder
							.addShould(queryBuilder.term("nodeId", nodeId));
				}
				//
				Node n = nodeManager.getNode(new Long(nodeId));
				tableId = n.getTableId();
			}
		}
		//
		// 关键字匹配
		// TODO 改进，AND与OR应该允许从外部指定，否则影响结果完全是两个样子
		if (keyword != null && !keyword.equals("")) {
			//
			String[] keywordAry = keyword.split("[\\s　]+");
			
			//
			//System.out.println("newKeyword=" + newKeyword);
			//
			if(StringUtil.hasText(keyword)){
				hasKeyword = true;
			}
			//若不存在指定字段，则获取关键字字段作为默认字段
			if (!StringUtils.hasText(fields)) {
				if (tableId != null) {
					ContentField field = contentFieldManager
							.getKeywordsFieldFromCache(tableId);
					if (field != null) {
						fields = field.getFieldName();
					}
				}
			}
			if (StringUtils.hasText(fields)&&hasKeyword) {
				String columns[] = fields.split(",");
				if(parseKeyword){
					keyword=this.getTerms(keyword, session);
				}
				if (columns != null) {
					
					for (int i = 0; i < columns.length; i++) {
						//keywordQuery += "(";
						//
						
						CompassMultiPropertyQueryStringBuilder mpq=queryBuilder.multiPropertyQueryString(keyword);
						//
						mpq.forceAnalyzer();
						mpq.add(columns[i]);
						mpq.useOrDefaultOperator();
						CompassQuery kwordQuery=mpq.toQuery();
						keywordQueryBuilder=keywordQueryBuilder.addShould(kwordQuery);
//						for (String k : keywordAry) {
//							if(StringUtil.hasText(k)){
//								keywordQuery += columns[i] + ":" + k + " OR ";
//							}
//						}
//						
//						keywordQuery = keywordQuery.substring(0, keywordQuery
//								.length() - 4);
//						keywordQuery += ") OR ";
						// keywordQueryBuilder = keywordQueryBuilder
						// .addShould(queryBuilder.queryString(q)
						// .toQuery());
					}
//					keywordQuery = keywordQuery.substring(0, keywordQuery
//							.length() - 3);
//					System.out.println("keywordQuery=" + keywordQueryBuilder.toQuery().toString());

				}
			} else {

				// System.out.println("keyword="+keyword);
				keywordQueryBuilder = keywordQueryBuilder.addShould(queryBuilder.multiPropertyQueryString(keyword).toQuery());
			}
		}
		// 2)ignore process

		// alias
		if (tableId != null) {
			ContentTable ct = contentTableManager
					.getContentTableFromCache(tableId);
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
			}
			if (!publishTableName.equals("")) {
				//
				hasAlias = true;
				alias = publishTableName;

			}
		}
		// where
		boolean bWhereInject = false;
		// 处理忽略条件ignoreIndex
		if (StringUtils.hasText(ignoreIndex)) {
			if (where == null) {
				where = "";
			}
			String[] ignoreIndexArg = ignoreIndex.split(",");
			for (String ignoreIndexId : ignoreIndexArg) {
				where += " !indexId:" + ignoreIndexId + " AND";
			}
			where = where.substring(0, where.length() - 3);
		}
		// 需要特殊处理Where部分
		if (where != null && !where.equals("")) {
			//
			// 判断是否为单一的否定条件，若是单一的否定条件，则需要把此条件并入到其它条件之中
			hasWhere = true;
			where = where.trim();
			int andPos = where.indexOf("AND");
			int orPos = where.indexOf("OR");
			if (andPos < 0) {
				// 没有组合条件
				if (orPos < 0) {
					if (where.charAt(0) == '!') {
						// 此时为单一的否定条件，需要并入到其它条件之中，若没有其它条件，则不能使用此条件
						bWhereInject = true;
					}
				} else {
					// TODO 有OR条件此时要检查是不是都为否定条件

				}
			} else {
				if (orPos < 0) {
					// 有组合条件，但是此时要检查是不是都为否定条件
					String[] whereAry = where.split("AND");
					for (String w : whereAry) {
						w = w.trim();
						if (where.charAt(0) != '!') {
							bWhereInject = false;
							break;
						} else {
							bWhereInject = true;
						}
					}
				} else {
					// TODO
				}
			}

		}

		// alias
		if (hasAlias) {
			if (hasWhere && bWhereInject) {
				// where合并
				// String whereQuery =
				// queryBuilder.queryString(where).toQuery().;
				String aliasQuery = queryBuilder.alias(alias).toString()
						+ " AND " + where + "";

				allQueryBuilder = allQueryBuilder.addMust(queryBuilder
						.queryString(aliasQuery).toQuery());
			} else {
				allQueryBuilder = allQueryBuilder.addMust(queryBuilder
						.alias(alias));
			}
		}
		// where
		if (hasWhere && !bWhereInject) {
			allQueryBuilder = allQueryBuilder.addMust(queryBuilder.queryString(
					where).toQuery());
		}
		// keyword
		//System.out.println("keywordQuery=("+keywordQuery+")");
		if (hasKeyword) {
			allQueryBuilder = allQueryBuilder.addMust(keywordQueryBuilder.toQuery());
		}
		// nodeId
		if (hasNodeId) {
			allQueryBuilder = allQueryBuilder.addMust(nodeIdQueryBuilder
					.toQuery());
		}

		query = allQueryBuilder.toQuery();
//		System.out.println("Lucene query=" + query.toString());
		// 排序处理
		if (order != null && !order.equals("")) {
//			System.out.println("Lucene order=" + order);
			String[] oAry = order.split(",");
			for (int i = 0; i < oAry.length; i++) {
				String o = oAry[i];
				int pos = o.toUpperCase().lastIndexOf("DESC");
				if (pos > -1) {
					o = o.substring(0, pos);
					query.addSort(o.trim(), CompassQuery.SortDirection.REVERSE);
				} else {
					int ascPos = o.toUpperCase().lastIndexOf("ASC");
					if (ascPos > -1) {
						o = o.substring(0, pos);
					}
					query.addSort(o.trim(), CompassQuery.SortDirection.AUTO);
				}
			}
		} else {
			query.addSort(CompassQuery.SortImplicitType.SCORE);
		}
		//
		return query;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public PageBuilder doSearchCount(SearchCommand searchCommand)
			throws Exception {
		SearchResults sr = doSearch(searchCommand);
		if (sr != null) {
			return sr.getPageBuilder();
		}
		return null;
	}
	/**
	 * 进行分词
	 * @param query
	 * @param session
	 * @return
	 */
	protected String getTerms(String query,CompassSession session) {
		StringBuilder terms=new StringBuilder();
		CompassAnalyzerHelper helper=session.analyzerHelper();
		CompassToken []tokens=helper.analyze(query);
		for(CompassToken token:tokens){
			terms.append(token.getTermText());
			terms.append(" ");
		}
		return terms.toString();
	}
	public ContentFieldManager getContentFieldManager() {
		return contentFieldManager;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}
}
