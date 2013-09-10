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
package org.openuap.cms.repo.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openuap.base.dao.hibernate.DynamicDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.repo.dao.DynamicContentDao;
import org.openuap.cms.util.PageInfo;

/**
 * <p>
 * 动态内容模型DAO实现.
 * </p>
 * 
 * <p>
 * $Id: DynamicContentDaoImpl.java 4010 2011-01-14 11:32:51Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DynamicContentDaoImpl extends DynamicDaoHibernate implements
		DynamicContentDao {

	public DynamicContentDaoImpl() {
	}

	/**
	 * 
	 */
	public List getContentList(String nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		String contentTable = "Content_" + tableId;

		return this.getContentList(nodeId, contentTable, where, order, args,
				start, limit, pageInfo);
	}

	public List getRecycleContentList(Long nodeId, Long tableId) {
		String contentTable = "Content_" + tableId;
		return this.getRecycleContentList(nodeId, contentTable);

	}

	/**
	 * 
	 * 
	 * @param indexId
	 *            Integer
	 * @param tableId
	 *            Integer
	 * @return Object -it is array,the frist is ContentIndex map second is
	 *         Content_? map
	 */
	public Object getContent(Long indexId, Long tableId) {
		String contentTable = "Content_" + tableId;
		System.out.println("tableId" + tableId);
		return this.getContent(indexId, contentTable);

	}

	public Long addContentIndex(Map contentIndex) {
		return (Long) this.getHibernateTemplate().save("ContentIndex",
				contentIndex);
	}

	public Long addContent(Long tableId, Map content) {
		String contentTable = "Content_" + tableId;
		return (Long) this.getHibernateTemplate().save(contentTable, content);
	}

	public void saveContentIndex(Map contentIndex) {
		//
		getHibernateTemplate().saveOrUpdate("ContentIndex", contentIndex);
		getHibernateTemplate().flush();

	}

	public void saveContent(Long tableId, Map content) {
		String contentTable = "Content_" + tableId;
		this.saveContent(contentTable, content);
	}

	public Map getContentIndexMapById(Long indexId) {
		return (Map) this.findUniqueResult("from ContentIndex where indexId=?",
				new Object[] { indexId });
	}

	public void deleteContentIndex(Long indexId, Long tableId, boolean cascade) {
		if (cascade) {
			String contentTable = "Content_" + tableId;
			String publishTable = "Publish_" + tableId;
			Map contentIndex = this.getContentIndexMapById(indexId);
			Integer contentId = (Integer) contentIndex.get("contentId");
			this.executeUpdate("delete from " + contentTable
					+ " where contentId=?", new Object[] { contentId });
			//

			//
			this.executeUpdate("delete from " + publishTable
					+ " where indexId=?", new Object[] { indexId });

		}
		this.executeUpdate("delete from ContentIndex where indexId=?",
				new Object[] { indexId });
	}

	public long getLongHql(String hql, Object[] args) {
		if (args != null) {
			return ((Number) this.getHibernateTemplate().iterate(hql, args)
					.next()).longValue();
		} else {
			return ((Number) this.getHibernateTemplate().iterate(hql).next())
					.longValue();
		}
	}

	public List getListHql(String hql, Object[] args, QueryInfo qi) {
		if (qi != null) {
			return this.executeFind(hql, qi, args);
		} else {
			return this.executeFind(hql, args);
		}
	}

	/**
	 * $Date: 2006/08/31 02:26:00 $
	 * 
	 * @param tableId
	 *            Integer
	 * @param publish
	 *            Map
	 */
	public void savePublish(Long tableId, Map publish) {
		String publishTable = "Publish_" + tableId;
		this.getHibernateTemplate().saveOrUpdate(publishTable, publish);
		getHibernateTemplate().flush();
	}

	public void deletePublish(Long tableId, Long indexId) {
		String publishTable = "Publish_" + tableId;
		this.executeUpdate("delete from " + publishTable + " where indexId=?",
				new Object[] { indexId });

	}

	public Object getObjectHql(String hql, Object[] args) {
		if (args != null) {
			return this.findUniqueResult(hql, args);
		} else {
			return this.findUniqueResult(hql);
		}

	}

	public List searchContentList(String keywords, String[] fields,
			String published, String[] nodeIds, String time, Long tableId,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		String contentTable = "Content_" + tableId;
		//
		return this.searchContentList(keywords, fields, published, nodeIds,
				time, contentTable, where, order, args, start, limit, pageInfo);
	}

	/**
	 * 
	 * 
	 * @param indexId
	 *            Long
	 * @param tableId
	 *            Long
	 * @param type
	 *            int
	 */
	public void deleteContentIndex(Long indexId, Long tableId, int type) {
		if (type == 0) {
			// it is a virtual link
			// delete the contentIndex and publish
			String publishTable = "Publish_" + tableId;

			this.executeUpdate("delete from " + publishTable
					+ " where indexId=?", new Object[] { indexId });
			this.executeUpdate("delete from ContentIndex where indexId=?",
					new Object[] { indexId });
		} else if (type == 1) {
			// it is a actually link
			// must delete all about the indexId's contentId
			String contentTable = "Content_" + tableId;
			String publishTable = "Publish_" + tableId;
			Map contentIndex = this.getContentIndexMapById(indexId);
			Long contentId = (Long) contentIndex.get("contentId");
			//
			this.executeUpdate("delete from " + publishTable
					+ " where contentId=?", new Object[] { contentId });
			this.executeUpdate("delete from " + contentTable
					+ " where contentId=?", new Object[] { contentId });
			this.executeUpdate(
					"delete from ContentIndex where contentId=? and tableId=?",
					new Object[] { contentId, tableId });

		} else if (type == 2) {
			// it is a index link
			// only delete the contentIndex
			this.executeUpdate("delete from ContentIndex where indexId=?",
					new Object[] { indexId });

		}
	}

	public void deleteContent(Long tableId, Long contentId) {
		String contentTable = "Content_" + tableId;
		this.executeUpdate(
				"delete from " + contentTable + " where contentId=?",
				new Object[] { contentId });
	}

	public List getLinkList(Long contentId, Long tableId, Integer type) {
		String contentTable = "Content_" + tableId;
		String hql = "select ci,ct,node from ContentIndex as ci,Node as node,"
				+ contentTable
				+ " as ct where ci.contentId=ct.contentId and "
				+ " ci.nodeId=node.nodeId and ci.state<>-1 and ci.contentId=? and ci.tableId=? and ci.type=?";
		return this.executeFind(hql, new Object[] { contentId, tableId, type });
	}

	public void saveContentTable(Map contentTable) {
		this.getHibernateTemplate().saveOrUpdate("ContentTable", contentTable);
		getHibernateTemplate().flush();
		//
	}

	public void savePublish(String tableName, Map publish) {
		this.getHibernateTemplate().saveOrUpdate(tableName, publish);
		getHibernateTemplate().flush();
	}

	public void deletePublish(String tableName, Long indexId) {
		this.executeUpdate("delete from " + tableName + " where indexId=?",
				new Object[] { indexId });
	}

	public List getLinkList(Long contentId, Long tableId, String tableName,
			Integer type) {
		String hql = "select ci,ct,node from ContentIndex as ci,Node as node,"
				+ tableName
				+ " as ct where ci.contentId=ct.contentId and "
				+ " ci.nodeId=node.nodeId and ci.state<>-1 and ci.contentId=? and ci.tableId=? and ci.type=?";
		return this.executeFind(hql, new Object[] { contentId, tableId, type });

	}

	public void deleteContent(String tableName, Long contentId) {
		this.executeUpdate("delete from " + tableName + " where contentId=?",
				new Object[] { contentId });
	}
	
	public void deleteContentIndex(Long indexId, Long tableId,
			String tableName, boolean cascade) {
		if (cascade) {
			String contentTable = tableName;
			String publishTable = tableName + "Publish";
			Map contentIndex = this.getContentIndexMapById(indexId);
			Integer contentId = (Integer) contentIndex.get("contentId");
			this.executeUpdate("delete from " + contentTable
					+ " where contentId=?", new Object[] { contentId });
			//

			//
			this.executeUpdate("delete from " + publishTable
					+ " where indexId=?", new Object[] { indexId });

		}
		this.executeUpdate("delete from ContentIndex where indexId=?",
				new Object[] { indexId });

	}

	public void deleteContentIndex(Long indexId, Long tableId,
			String tableName, int type) {
		if (type == 0) {
			// it is a virtual link
			// delete the contentIndex and publish
			String publishTable = tableName + "Publish";

			this.executeUpdate("delete from " + publishTable
					+ " where indexId=?", new Object[] { indexId });
			this.executeUpdate("delete from ContentIndex where indexId=?",
					new Object[] { indexId });
		} else if (type == 1) {
			// it is a actually link
			// must delete all about the indexId's contentId
			String contentTable = tableName;
			String publishTable = tableName + "Publish";
			Map contentIndex = this.getContentIndexMapById(indexId);
			Long contentId = (Long) contentIndex.get("contentId");
			//
			this.executeUpdate("delete from " + publishTable
					+ " where contentId=?", new Object[] { contentId });
			this.executeUpdate("delete from " + contentTable
					+ " where contentId=?", new Object[] { contentId });
			this.executeUpdate(
					"delete from ContentIndex where contentId=? and tableId=?",
					new Object[] { contentId, tableId });

		} else if (type == 2) {
			// it is a index link
			// only delete the contentIndex
			this.executeUpdate("delete from ContentIndex where indexId=?",
					new Object[] { indexId });

		}

	}

	public Long addContent(String tableName, Map content) {
		return (Long) this.getHibernateTemplate().save(tableName, content);
	}

	/**
	 * TODO 索引的公共属性存储会降低性能，但是带来了一定的灵活性
	 */
	public Object getContent(Long indexId, String tableName) {

		String hql = "select ci,c from " + " ContentIndex as ci," + tableName
				+ " as c where " + " "
				+ " ci.contentId=c.contentId and ci.indexId=?";
		return this.findUniqueResult(hql, new Object[] { indexId });

	}

	/**
	 * 返回Map形式的动态内容列表 返回结果格式 排序方式，缺省按照置顶，发布日期，状态，排序进行倒排 List<Object[]>
	 * Ojbect[]={ContentIndex Map,Content Map}
	 */
	public List<?> getContentList(String nodeId, String tableName, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		Object[] destAtgs = null;
		if (args != null) {
			destAtgs =args;
			
		} else {
			destAtgs = new Object[] { };
		}
		//
		// TODO 这里优化，关联查询，非常浪费时间，变关联为平坦？
		String hql = "select ci from ContentIndex as ci," + "" + tableName
				+ " as c where ci.contentId=c.contentId and ci.nodeId in("+nodeId+")";
		String hql2 = "select count(ci.indexId) from ContentIndex as ci," + " "
				+ tableName
				+ " as c where ci.contentId=c.contentId and ci.nodeId in("+nodeId+")";
		// process the where condition
		if (where != null && !where.equals("")) {
			hql += " and " + where;
			hql2 += " and " + where;
		} else {
			hql += " and ci.state<>-1";
			hql2 += " and ci.state<>-1";
		}
		//
		long totalNum = this.getLongHql(hql2, destAtgs);
		pageInfo.items((int) totalNum);
		// 处理排序条件
		if (order != null && !order.equals("")) {
			hql += " order by " + order;
		} else {
			hql += " order by ci.top DESC,ci.publishDate DESC,ci.state DESC,ci.sort DESC";
		}

		// process the pager
		QueryInfo qi = new QueryInfo();
		if (start != null) {
			qi.setOffset(new Integer(start.intValue()));
		}
		if (limit != null) {
			qi.setLimit(new Integer(limit.intValue()));
		}
		
		//System.out.println("hql="+hql);
		return this.executeFind(hql, qi, destAtgs);
	}

	/**
	 * 获得所有指定结点的内容
	 * 
	 * @param nodeId
	 * @param tableName
	 * @param where
	 * @param order
	 * @param args
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getAllContentList(Long nodeId, String tableName, String where,
			String order, Object[] args, Long start, Long limit) {
		Object[] destAtgs = null;
		if (args != null) {
			destAtgs = new Object[args.length + 1];
			destAtgs[0] = nodeId;
			for (int i = 0; i < args.length; i++) {
				destAtgs[i + 1] = args[i];
			}
		} else {
			destAtgs = new Object[] { nodeId };
		}
		//
		// TODO 这里优化，关联查询，非常浪费时间，变关联为平坦？
		String hql = "select ci,c from ContentIndex as ci," + "" + tableName
				+ " as c where ci.contentId=c.contentId and ci.nodeId=?";

		// process the where condition
		if (where != null && !where.equals("")) {
			hql += " and " + where;
		}
		//

		// process the pager
		QueryInfo qi = new QueryInfo();
		if (start != null) {
			qi.setOffset(new Integer(start.intValue()));
		}
		if (limit != null) {
			qi.setLimit(new Integer(limit.intValue()));
		}
		return this.executeFind(hql, qi, destAtgs);
	}

	public List getQuickContentList(String nodeId, Long tableId, String where,
			String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		String contentTable = "Content_" + tableId;

		return this.getQuickContentList(nodeId, contentTable, where, order,
				args, start, limit, pageInfo);
	}

	/**
	 * 快速获得内容列表 使用新的内容索引对象（冗余设计）为提高性能
	 * 
	 * @param nodeId
	 * @param tableName
	 * @param where
	 * @param order
	 * @param args
	 * @param start
	 * @param limit
	 * @param pageInfo
	 * @return
	 */
	public List getQuickContentList(String nodeId, String tableName,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		Object[] destAtgs = null;
		if (args != null) {
			destAtgs = args;

		} else {
			destAtgs = new Object[] { };
		}
		//
		// TODO 这里优化，关联查询，非常浪费时间，变关联为平坦？
		String hql = "select ci from ContentIndex as ci where ci.nodeId in("+nodeId+")";
		String hql2 = "select count(ci.indexId) from ContentIndex as ci where ci.nodeId in("+nodeId+")";

		// process the where condition
		if (where != null && !where.equals("")) {
			hql += " and " + where;
			hql2 += " and " + where;
		} else {
			hql += " and ci.state<>-1";
			hql2 += " and ci.state<>-1";
		}
		//
		long totalNum = this.getLongHql(hql2, destAtgs);
		pageInfo.items((int) totalNum);
		// 处理排序条件
		if (order != null && !order.equals("")) {
			hql += " order by " + order;
		} else {
			hql += " order by ci.top DESC,ci.publishDate DESC,ci.state desc,ci.sort DESC";
		}

		// process the pager
		QueryInfo qi = new QueryInfo();
		if (start != null) {
			qi.setOffset(new Integer(start.intValue()));
		}
		if (limit != null) {
			qi.setLimit(new Integer(limit.intValue()));
		}
		// and ci.state<>-1 order by ci.state desc,ci.top DESC,ci.sort
		// DESC,ci.publishDate DESC";

		return this.executeFind(hql, qi, destAtgs);
	}

	public List searchContentList(String keywords, String[] fields,
			String published, String[] nodeIds, String time, String tableName,
			String where, String order, Object[] args, Long start, Long limit,
			PageInfo pageInfo) {
		String contentTable = tableName;
		//

		//
		String hql = "select ci,c from ContentIndex as ci," + contentTable
				+ " as c where ci.contentId=c.contentId ";
		String hql2 = "select count(ci.indexId) from ContentIndex as ci,"
				+ contentTable + " as c where ci.contentId=c.contentId ";
		String nodeId_condition = "";
		if (nodeIds != null) {
			nodeId_condition = "ci.nodeId in(";
			for (int i = 0; i < nodeIds.length - 1; i++) {
				nodeId_condition += nodeIds[i] + ",";
			}
			nodeId_condition += nodeIds[nodeIds.length - 1] + ") ";
		}
		//
		if (!nodeId_condition.equals("")) {
			hql += " and " + nodeId_condition;
			hql2 += " and " + nodeId_condition;
		}
		// //
		String keywords_condition = "";
		if (keywords != null && !keywords.equals("")) {
			if (fields != null) {
				keywords_condition = " (";
				for (int i = 0; i < fields.length - 1; i++) {
					keywords_condition += " c." + fields[i] + " like '%"
							+ keywords + "%' or ";
				}
				keywords_condition += " c." + fields[fields.length - 1]
						+ " like '%" + keywords + "%')";
			}
		}
		if (!keywords_condition.equals("")) {
			hql += " and " + keywords_condition;
			hql2 += " and " + keywords_condition;
		}
		// //
		String state_condition = "";
		if (published != null && !published.equals("")) {
			if (published.equals("0")) {
				state_condition = " ci.state=0 ";
			} else if (published.equals("1")) {
				state_condition = " ci.state=1 ";
			}
		}
		if (!state_condition.equals("")) {
			hql += " and " + state_condition;
			hql2 += " and " + state_condition;
		}
		// //
		String time_condition = "";
		if (time != null && !time.equals("") && !time.equals("0")) {
			long now = System.currentTimeMillis();
			now = now / 1000;
			long low = now - Integer.parseInt(time) * 24 * 60 * 60;
			time_condition = "(ci.publishDate>=" + low
					+ " and ci.publishDate<=" + now + ")";
		}
		if (!time_condition.equals("")) {
			hql += " and " + time_condition;
			hql2 += " and " + time_condition;
		}
		// process the where condition
		if (where != null && !where.equals("")) {
			hql += " and " + where;
			hql2 += " and " + where;
		}
		//
		long totalNum = this.getLongHql(hql2, args);
		pageInfo.items((int) totalNum);
		//
		if (order != null && !order.equals("")) {
			hql += " order by " + order;
		} else {
			hql += " order by ci.state desc,ci.top DESC,ci.sort DESC,ci.publishDate DESC";
		}

		// process the pager
		QueryInfo qi = new QueryInfo();
		if (start != null) {
			qi.setOffset(new Integer(start.intValue()));
		}
		if (limit != null) {
			qi.setLimit(new Integer(limit.intValue()));
		}
		// and ci.state<>-1 order by ci.state desc,ci.top DESC,ci.sort
		// DESC,ci.publishDate DESC";
		return this.executeFind(hql, qi, args);

	}

	public List getRecycleContentList(Long nodeId, Long tableId, QueryInfo qi,
			PageBuilder pb) {
		String contentTable = "Content_" + tableId;
		return this.getRecycleContentList(nodeId, contentTable, qi, pb);
	}

	public List getRecycleContentList(Long nodeId, String tableName,
			QueryInfo qi, PageBuilder pb) {
		String hql = "select ci,c from ContentIndex as ci," + tableName
				+ " as c where ci.contentId=c.contentId and ci.nodeId="
				+ nodeId + " and ci.state=-1";
		String hql_count = "select count(ci.indexId) from ContentIndex as ci,"
				+ tableName
				+ " as c where ci.contentId=c.contentId and ci.nodeId="
				+ nodeId + " and ci.state=-1";
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public List getRecycleContentList(Long nodeId, String tableName) {
		String hql = "select ci,c from ContentIndex as ci,"
				+ tableName
				+ " as c where ci.contentId=c.contentId and ci.nodeId=? and ci.state=-1 order by ci.top DESC,ci.sort DESC,ci.publishDate DESC";
		return this.executeFind(hql, new Object[] { nodeId });

	}

	public void saveContent(String tableName, Map content) {
		this.getHibernateTemplate().saveOrUpdate(tableName, content);
		getHibernateTemplate().flush();
	}

	public List getListContent(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb) {
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public Map<String, ?> getDynamicContent(Long contentId, Long tableId) {
		String contentTable = "Content_" + tableId;
		return getDynamicContent(contentId, contentTable);
	}

	public Map<String, ?> getDynamicContent(Long contentId, String tableName) {
		String hql = "select c from " + tableName + " as c where "
				+ " c.contentId=?";
		return (Map<String, ?>) this.findUniqueResult(hql,
				new Object[] { contentId });
	}

	/**
	 * @see
	 */
	public List getDynamicPublish(String tableName, int start, int limit) {
		String hql = "from " + tableName
				+ " p,ContentIndex as ci where p.indexId=ci.indexId";
		QueryInfo queryInfo = new QueryInfo(null, null, limit, start);
		List pcList = this.executeFind(hql, queryInfo);

		List mergeList = new ArrayList();
		if (pcList != null) {
			int size = pcList.size();
			for (int i = 0; i < size; i++) {
				// merge the two map
				Map p = (Map) ((Object[]) pcList.get(i))[0];
				Map ci = (Map) ((Object[]) pcList.get(i))[1];
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
		return mergeList;
	}

	public int getDynamicPublishCount(String tableName) {
		String hql = "select count(*) from " + tableName + "";
		return this.getIntFieldValue(hql);
	}

	public List getDynamicPublish(String tableName, String indexIds) {
		//
		String hql = "from " + tableName
				+ " p,ContentIndex as ci where p.indexId=ci.indexId";

		String[] ids = indexIds.split(",");
		hql += " and (";
		int size = ids.length;
		for (int i = 0; i < size; i++) {
			String indexId = ids[i];
			hql += " p.indexId=" + indexId;
			if (i != size - 1) {
				hql += " or ";
			}
		}
		hql += ")";
		QueryInfo queryInfo = new QueryInfo(null, null, null, null);
		List pcList = this.executeFind(hql, queryInfo);

		List mergeList = new ArrayList();
		if (pcList != null) {
			int size2 = pcList.size();
			for (int i = 0; i < size2; i++) {
				// merge the two map
				Map p = (Map) ((Object[]) pcList.get(i))[0];
				Map ci = (Map) ((Object[]) pcList.get(i))[1];
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

		return mergeList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openuap.cms.dao.DynamicContentDao#getDynamicPublish(java.lang.Long,
	 *      java.lang.String)
	 */
	public Map getDynamicPublish(Long indexId, String publishName) {
		String hql = "from " + publishName + " where indexId=" + indexId;
		return (Map) this.findUniqueResult(hql);
	}
}
