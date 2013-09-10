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
package org.openuap.cms.ds.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.ds.dao.DataSourceDao;
import org.openuap.cms.ds.model.DataSourceModel;

/**
 * 
 * <p>
 * 据源DAO Hibernate实现类
 * </p>
 * 
 * <p>
 * $Id: DataSourceDaoImpl.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class DataSourceDaoImpl extends BaseDaoHibernate implements
		DataSourceDao {
	public DataSourceDaoImpl() {
	}

	public Long addDataSource(DataSourceModel ds) {
		//
		return (Long) this.getHibernateTemplate().save(ds);
	}

	public void saveDataSource(DataSourceModel ds) {
		getHibernateTemplate().saveOrUpdate(ds);

	}

	public void deleteDataSource(Long id) {
		DataSourceModel ds = this.getDataSourceById(id);
		if (ds != null) {
			this.getHibernateTemplate().delete(ds);
		}
	}

	public List getAllDataSource() {
		return this.executeFind("from DataSourceModel");
	}

	public DataSourceModel getDataSourceById(Long id) {
		return (DataSourceModel) this.findUniqueResult(
				"from DataSourceModel where id=?", new Object[] { id });
	}

	public DataSourceModel getDataSourceByName(String name) {
		return (DataSourceModel) this.findUniqueResult(
				"from DataSourceModel where name=?", new Object[] { name });
	}

	public long getDataSourceCount() {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from DataSourceModel").next()).longValue();
	}

	public List<DataSourceModel> getDataSourceModels(String hql,
			String hql_count, QueryInfo qi, PageBuilder pb) {
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public List<DataSourceModel> getDataSourceModels(QueryInfo qi,
			PageBuilder pb) {
		String hql = "from DataSourceModel";
		String hql_count = "select count(*) from DataSourceModel";
		return getDataSourceModels(hql, hql_count, qi, pb);
	}
}
