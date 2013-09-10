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

import java.util.ArrayList;
import java.util.List;

import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.filter.Filter;
import org.openuap.cms.engine.macro.CmsMacroEngine;

import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.search.SearchCommand;
import org.openuap.cms.search.SearchEngine;
import org.openuap.cms.search.SearchResults;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 基于索引的宏引擎实现
 * </p>
 * 
 * <p>
 * $Id: IndexedCmsMacroEngineImpl.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class IndexedCmsMacroEngineImpl extends AbstractCmsMacroEngine implements
		CmsMacroEngine {

	private SearchEngine searchEngine;

	/**
	 * 从索引中获取内容列表
	 */
	public List getCmsList(String nodeId, String num, String nodeGUID,
			String orderBy, String where, String tableID, String ignore,
			String page, String url) {
		int ipage = 1;

		if (page == null || page.equals("")) {
			page = "1";
		}
		// 获得索引模式的where条件
		// System.out.println("where="+where);
		String newWhere = getIndexWhere(where);
		// System.out.println("newWhere="+newWhere);
		// 获得索引模式的orderBy条件
		String newOrder = getIndexOrderBy(orderBy);
		ipage = Integer.valueOf(page).intValue();
		SearchCommand searchCommand = new SearchCommand("", "", nodeId,
				nodeGUID, tableID, ignore, newOrder, newWhere, "", ipage, num,
				url);
		List<Object> result = new ArrayList<Object>();
		try {
			SearchResults rs = searchEngine.doSearch(searchCommand);
			PageBuilder pb = rs.getPageBuilder();
			//
			result.add(0, pb);
			result.add(1, rs.getHits());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List getCmsContent(String indexId) {
		//

		String firstNodeId = null;
		String indexId_condition = "";
		if (indexId != null) {

			if (indexId.indexOf(",") > -1) {
				// 多条内容
				String[] sids = indexId.split(",");
				String firstIndexId = sids[0];
				ContentIndex ci = getDynamicContentManager()
						.getContentIndexFromCache(new Long(firstIndexId));
				if (ci == null) {
					return null;
				}
				firstNodeId = ci.getNodeId().toString();
				//
				indexId_condition = "(";
				for (int i = 0; i < sids.length - 1; i++) {
					indexId_condition += " indexId:" + sids[i] + " OR ";
				}
				indexId_condition += " indexId:" + sids[sids.length - 1] + " )";
			} else {
				ContentIndex ci = getDynamicContentManager()
						.getContentIndexFromCache(new Long(indexId));
				if (ci == null) {
					return null;
				}
				firstNodeId = ci.getNodeId().toString();
				//
				indexId_condition = "(indexId:" + indexId + ")";

			}
		}
		//
		SearchCommand searchCommand = new SearchCommand("", "", firstNodeId,
				"", "", "", "", indexId_condition, "", 1, "-1", "");
		//
		try {
			SearchResults rs = searchEngine.doSearch(searchCommand);
			// PageBuilder pb = rs.getPageBuilder();
			//
			return (List) rs.getHits();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List getCmsContent(String indexId, String orderBy) {
		String firstNodeId = null;
		String indexId_condition = "";
		if (indexId != null) {

			if (indexId.indexOf(",") > -1) {
				// 多条内容
				String[] sids = indexId.split(",");
				String firstIndexId = sids[0];
				ContentIndex ci = getDynamicContentManager()
						.getContentIndexFromCache(new Long(firstIndexId));
				if (ci == null) {
					return null;
				}
				firstNodeId = ci.getNodeId().toString();
				//
				indexId_condition = "(";
				for (int i = 0; i < sids.length - 1; i++) {
					indexId_condition += " indexId:" + sids[i] + " OR ";
				}
				indexId_condition += " indexId:" + sids[sids.length - 1] + " )";
			} else {
				ContentIndex ci = getDynamicContentManager()
						.getContentIndexFromCache(new Long(indexId));
				if (ci == null) {
					return null;
				}
				firstNodeId = ci.getNodeId().toString();
				//
				indexId_condition = "(indexId:" + indexId + ")";

			}
		}
		//
		// 获得索引模式的orderBy条件
		String newOrder = getContentIndexOrderBy(orderBy);
		//
		SearchCommand searchCommand = new SearchCommand("", "", firstNodeId,
				"", "", "", newOrder, indexId_condition, "", 1, "-1", "");
		//
		try {
			SearchResults rs = searchEngine.doSearch(searchCommand);
			// PageBuilder pb = rs.getPageBuilder();
			//
			return (List) rs.getHits();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public PageBuilder getCmsListPageInfo(String nodeId, String num,
			String nodeGUID, String orderBy, String where, String TableID,
			String ignore, String page, String url) {
		int ipage = 1;

		if (page == null || page.equals("")) {
			page = "1";
		}
		// 获得索引模式的where条件
		// System.out.println("where="+where);
		String newWhere = getIndexWhere(where);
		// System.out.println("newWhere="+newWhere);
		// 获得索引模式的orderBy条件
		String newOrder = getIndexOrderBy(orderBy);
		ipage = Integer.valueOf(page).intValue();
		SearchCommand searchCommand = new SearchCommand("", "", nodeId,
				nodeGUID, TableID, ignore, newOrder, newWhere, "", ipage, num,
				url);
		PageBuilder pb = null;
		try {
			SearchResults rs = searchEngine.doSearch(searchCommand);
			if (rs != null) {
				pb = rs.getPageBuilder();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pb;
	}

	public Object getCmsNode(String type, String NodeID) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getCmsNodeList(String Type, String NodeID, String ignore) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getCmsSearchList(String nodeID, String num, String nodeGUID,
			String orderBy, String where, String TableID, String ignore,
			String page, String url, String ignoreIndex, String keywords,
			String fields) {
		int ipage = 1;

		if (page == null || page.equals("")) {
			page = "1";
		}
		// 获得索引模式的where条件

		// System.out.println("where="+where);
		String newWhere = getIndexWhere(where);
		// System.out.println("newWhere="+newWhere);
		// 获得索引模式的orderBy条件
		String newOrder = getIndexOrderBy(orderBy);
		ipage = Integer.valueOf(page).intValue();
		SearchCommand searchCommand = new SearchCommand(keywords, fields,
				nodeID, nodeGUID, TableID, ignore, newOrder, newWhere, "",
				ipage, num, url);
		searchCommand.setIgnoreIndex(ignoreIndex);
		List<Object> result = new ArrayList<Object>();
		try {
			SearchResults rs = searchEngine.doSearch(searchCommand);
			PageBuilder pb = rs.getPageBuilder();
			//
			result.add(0, pb);
			result.add(1, rs.getHits());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	/**
	 * 获得索引兼容的SQL语法 TODO 完美的实现应该是采用ANTLR等语法分析器处理
	 * 
	 * @param where
	 * @return
	 */
	protected String getIndexWhere(String where) {
		String newWhere = where;
		if (StringUtils.hasText(where)) {
			int pos = where.indexOf("where");

			if (pos != -1) {
				// 去掉where关键字
				newWhere = where.substring(pos + 5);
			}
			newWhere = newWhere.replaceAll("and", "AND");
			newWhere = newWhere.replaceAll("or", "OR");
			newWhere = newWhere.replaceAll("not", "NOT");
			// 用正则表达式替换
			// 替换掉!=''为!='null'
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\s]*)([\\S]*?)([\\s]*)!=''([\\s\\S]*?)#$1 $3!='null'$6#ig",
							newWhere);
			// p.
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\S]*?)[.]([\\S]*?)([\\s]*)([\\s\\S]*?)#$1$3#ig",
							newWhere);
			// not like
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\s]*)([\\S]*?)([\\s]*)not like([\\s]*)'%([^%]+)%'([\\s\\S]*?)#$1 !$3:($6)$7#ig",
							newWhere);
			// like
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\s]+)like([\\s]*)'%([^%]+)%'([\\s\\S]*?)#$1:($4)$5#ig",
							newWhere);
			// !=''
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\s]*)([\\S]*?)([\\s]*)!='([^']+)'([\\s\\S]*?)#$1 !$3:($5)$6#ig",
							newWhere);
			// =''
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\s]*)='([^']+)'([\\s\\S]*?)#$1:($3)$4#ig",
							newWhere);
			// !=
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\s]*)([\\S]*?)([\\s]*)!=([^\\s]+)([\\s\\S]*?)#$1 !$3:($5)$6#ig",
							newWhere);
			// =
			newWhere = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\s]*)=([^\\s]+)([\\s\\S]*?)#$1:($3)$4#ig",
							newWhere);

			//
		}
		return newWhere;
	}

	/**
	 * 获得适用于索引的排序条件
	 * 
	 * @param order
	 * @return
	 */
	protected String getIndexOrderBy(String order) {
		String newOrder = "";
		if (StringUtils.hasText(order)) {
			// 需要特殊处理SCORE这个排序条件
			if (order.trim().equals("SCORE")) {
				newOrder = "";
			} else {
				// 去掉前缀
				newOrder = Filter.perl
						.substitute(
								"s#([\\s\\S]*?)([\\S]*?)[.]([\\S]*?)([\\s]*)([\\s\\S]*?)#$1$3#ig",
								newOrder);
				String[] conditions = order.split(",");
				for (String con : conditions) {
					String[] words = con.split("\\s");
					for (String w : words) {

						String[] ds = w.split("\\.");

						if (ds.length == 2) {
							// 去掉前面的别名前缀，由于索引不需要这个前缀
							newOrder += " " + ds[1];
						} else {
							newOrder += " " + w;
						}

					}
					newOrder += ",";
				}
				if (newOrder.charAt(newOrder.length() - 1) == ',') {
					newOrder = newOrder.substring(0, newOrder.length() - 1);
				}
			}
		} else {
			// 缺省按照置顶,排序，发布日期倒序排列
			//newOrder = " top DESC,sort DESC,publishDate DESC";
			//全文检索里去掉缺省排序
			newOrder="";
		}
		return newOrder;
	}

	protected String getContentIndexOrderBy(String order) {
		String newOrder = "";
		if (StringUtils.hasText(order)) {
			// 去掉前缀
			newOrder = Filter.perl
					.substitute(
							"s#([\\s\\S]*?)([\\S]*?)[.]([\\S]*?)([\\s]*)([\\s\\S]*?)#$1$3#ig",
							newOrder);
			String[] conditions = order.split(",");
			for (String con : conditions) {
				String[] words = con.split("\\s");
				for (String w : words) {

					String[] ds = w.split("\\.");

					if (ds.length == 2) {
						// 去掉前面的别名前缀，由于索引不需要这个前缀
						newOrder += " " + ds[1];
					} else {
						newOrder += " " + w;
					}

				}
				newOrder += ",";
			}
			if (newOrder.charAt(newOrder.length() - 1) == ',') {
				newOrder = newOrder.substring(0, newOrder.length() - 1);
			}
		}
		return newOrder;
	}

	public DynamicContentManager getDynamicContentManager() {
		DynamicContentManager dynamicContentManager = (DynamicContentManager) ObjectLocator
				.lookup("dynamicContentManager", CmsPlugin.PLUGIN_ID);
		return dynamicContentManager;
	}

	public static void main(String[] args) {
		IndexedCmsMacroEngineImpl m = new IndexedCmsMacroEngineImpl();
		String order = " co.hitsTotal DESC,p.publishDate DESC";
		String newOrder = m.getIndexOrderBy(order);
		System.out.println("newOrder=" + newOrder);
		//
		String where = " p.infoType='供应' and p.photo!=''";
		String newWhere = m.getIndexWhere(where);
		System.out.println("newWhere=" + newWhere);
	}

	public List getCmsContent(String indexId, String orderBy, boolean preview) {
		// 
		return this.getCmsContent(indexId, orderBy);
	}
}
