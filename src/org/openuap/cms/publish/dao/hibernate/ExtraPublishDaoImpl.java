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
package org.openuap.cms.publish.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.publish.dao.ExtraPublishDao;
import org.openuap.cms.publish.model.ExtraPublish;

/**
 * <p>
 * 附加发布Dao实现.
 * </p>
 * 
 * <p>
 * $Id: ExtraPublishDaoImpl.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ExtraPublishDaoImpl extends BaseDaoHibernate implements
		ExtraPublishDao {
	public ExtraPublishDaoImpl() {
	}

	public Long addPublish(ExtraPublish publish) {
		return (Long) this.getHibernateTemplate().save(publish);
	}

	public void savePublish(ExtraPublish publish) {
		getHibernateTemplate().saveOrUpdate(publish);
		// necessary to throw a DataIntegrityViolation and catch it in
		// PsnManager
		getHibernateTemplate().flush();

	}

	public void deletePublish(Long publishId) {
		ExtraPublish publish = this.getPublishById(publishId);
		this.getHibernateTemplate().delete(publish);
	}

	public ExtraPublish getPublishById(Long publishId) {
		return (ExtraPublish) this.findUniqueResult(
				"from ExtraPublish where publishId=?",
				new Object[] { publishId });
	}

	public List getPublishes(Long nodeId) {
		//FIX:ADD Order
		return this.executeFind("from ExtraPublish where nodeId=? order by publishId desc",
				new Object[] { nodeId });
	}

	/**
	 * 
	 * @param guid
	 *            String
	 * @return ExtraPublish
	 */
	public ExtraPublish getPublishByGuid(String guid) {
		String hql = "from ExtraPublish where publishGuid=" + guid;
		return (ExtraPublish) this.findUniqueResult(hql);

	}

	public List<ExtraPublish> getNodeAutoRefreshPublish(Long nodeId,int mode) {
		String hql="from ExtraPublish where nodeId=? and autoRefreshMode>="+mode;
		return this.executeFind(hql, new Object[]{nodeId});
	}

	public List<ExtraPublish> getGlobalAutoRefreshPublish() {
		String hql="from ExtraPublish where autoRefreshMode=3";
		return this.executeFind(hql);
	}

	
	public List getPublishes(QueryInfo qi, PageBuilder pb) {
		String hql="from ExtraPublish ";
		String hql_count="select count(*) from ExtraPublish ";
		return this.getObjects(hql, hql_count, qi, pb);
	}
}
