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
package org.openuap.cms.ds.dao;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDao;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.ds.model.DataSourceModel;

/**
 * 
 * <p>
 * 数据源DAO接口.
 * </p>
 * 
 * 
 * <p>
 * $Id: DataSourceDao.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface DataSourceDao extends BaseDao{

	public Long addDataSource(DataSourceModel ds);

	public void saveDataSource(DataSourceModel ds);

	public void deleteDataSource(Long id);

	public List<DataSourceModel> getAllDataSource();
	
	public List<DataSourceModel> getDataSourceModels(String hql, String hql_count,
			QueryInfo qi, PageBuilder pb);
	
	public List<DataSourceModel> getDataSourceModels(QueryInfo qi, PageBuilder pb);
	
	public DataSourceModel getDataSourceById(Long id);

	public DataSourceModel getDataSourceByName(String name);

	public long getDataSourceCount();
}
