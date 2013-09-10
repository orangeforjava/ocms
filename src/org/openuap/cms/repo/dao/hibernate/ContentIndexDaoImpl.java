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

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.cms.repo.dao.ContentIndexDao;
import org.openuap.cms.repo.model.ContentIndex;

/**
 * <p>
 * 内容索引Dao实现.
 * </p>
 * 
 * <p>
 * $Id: ContentIndexDaoImpl.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentIndexDaoImpl extends BaseDaoHibernate implements ContentIndexDao {
	public ContentIndexDaoImpl() {
	}

	public ContentIndex getContentIndexById(Long contentIndexId) {
		return (ContentIndex) this.findUniqueResult("from ContentIndex where indexId=?",
				new Object[] { contentIndexId });
	}

	public void saveContentIndex(ContentIndex contentIndex) {
		this.saveObject(contentIndex);

	}

	public void deleteContentIndex(Long contentIndexId) {
		ContentIndex contentIndex = this.getContentIndexById(contentIndexId);
		this.deleteObject(contentIndex);
	}

	public long getNodePublishContentCount(Long nodeId) {
		return this.getLongFieldValue("select count(*) from ContentIndex where state=1 and nodeId="+nodeId);
//		return ((Number) this.getHibernateTemplate().iterate(
//				"select count(*) from ContentIndex where state=1 and nodeId=?", new Object[] { nodeId }).next())
//				.longValue();

	}

	public long getNodeUnPubllishContentCount(Long nodeId) {
		String hql="select count(*) from ContentIndex where state=0 and nodeId="+nodeId;
		return this.getLongFieldValue(hql);
//		return ((Number) this.getHibernateTemplate().iterate(
//				"select count(*) from ContentIndex where state=0 and nodeId=?", new Object[] { nodeId }).next())
//				.longValue();

	}

	public List getNodePublishContents(Long nodeId, Long start, Long length) {
		QueryInfo qi = new QueryInfo();
		qi.setOffset(new Integer(start.intValue()));
		qi.setLimit(new Integer(length.intValue()));
		return this.executeFind("from ContentIndex where state=1 and nodeId=? order by indexId", qi,
				new Object[] { nodeId });
	}

	public List getNodeUnPublishContents(Long nodeId, Long start, Long length) {

		QueryInfo qi = new QueryInfo();
		qi.setOffset(new Integer(start.intValue()));
		qi.setLimit(new Integer(length.intValue()));
		return this.executeFind("from ContentIndex where state=0 and nodeId=? order by indexId", qi,
				new Object[] { nodeId });

	}

	public Long addContentIndex(ContentIndex contentIndex) {
		return (Long) this.getHibernateTemplate().save(contentIndex);
	}

	public ContentIndex getContentIndexByContentId(Long contentId, Long nodeId) {
		return (ContentIndex) this.findUniqueResult("from ContentIndex where contentId=? and nodeId=?", new Object[] {
				contentId, nodeId });

	}

	public ContentIndex getContentIndexByOldId(Long nodeId, Long oldId, String table) {
		String hql = "from ContentIndex where nodeId=? and oldId=? and oldTable=?";
		Object obj = this.findUniqueResult(hql, new Object[] { nodeId, oldId, table });
		if (obj != null) {
			return (ContentIndex) obj;
		}
		return null;
	}

	public ContentIndex getContentIndexByOldId(Long oldId, String table) {
		String hql = "from ContentIndex where oldId=? and oldTable=?";
		Object obj = this.findUniqueResult(hql, new Object[] { oldId, table });
		if (obj != null) {
			return (ContentIndex) obj;
		}
		return null;

	}

	public List getRecycleContents(Long nodeId) {
		String hql = "from ContentIndex where nodeId=" + nodeId + " and state=-1";
		return executeFind(hql);
	}
	/**
	 * @see org.openuap.cms.repo.dao.ContentIndexDao#getNodeContentCount(Long)
	 */
	public long getNodeContentCount(Long nodeId) {
		String hql = "select count(*) from ContentIndex where nodeId=? and state<>-1";
		return ((Number) this.getHibernateTemplate().iterate(hql, new Object[] { nodeId }).next()).longValue();
	}
	public long getAllContentCount(Long nodeId) {
		String hql = "select count(*) from ContentIndex where nodeId=?";
		return ((Number) this.getHibernateTemplate().iterate(hql, new Object[] { nodeId }).next()).longValue();
	}
}
