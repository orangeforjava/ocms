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
package org.openuap.cms.stat.dao.hibernate;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.stat.dao.CountDao;
import org.openuap.cms.stat.model.CmsCount;

/**
 * <p>
 * 计数Dao实现.
 * </p>
 * 
 * <p>
 * $Id: CountDaoImpl.java 3920 2010-10-26 11:41:54Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class CountDaoImpl extends BaseDaoHibernate implements CountDao {

	public CountDaoImpl() {
	}

	public CmsCount getCountById(Long indexId) {
		return (CmsCount) this.findUniqueResult(
				"from CmsCount where indexId=?", new Object[] { indexId });
	}

	public void saveCount(CmsCount count) {
		getHibernateTemplate().saveOrUpdate(count);
		//
		getHibernateTemplate().flush();
	}

	public void removeCount(CmsCount count) {
		this.getHibernateTemplate().delete(count);
		getHibernateTemplate().flush();
	}

	public void removeCountById(Long indexId) {
		this.executeUpdate("delete from CmsCount where indexId=?",
				new Object[] { indexId });
	}
}
