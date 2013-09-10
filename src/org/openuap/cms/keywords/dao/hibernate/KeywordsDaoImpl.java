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
package org.openuap.cms.keywords.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.keywords.dao.KeywordsDao;
import org.openuap.cms.keywords.model.Keywords;

/**
 * <p>
 * 关键词DAO实现
 * </p>
 * 
 * <p>
 * $Id: KeywordsDaoImpl.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class KeywordsDaoImpl extends BaseDaoHibernate implements KeywordsDao {
	protected static transient String TABLE = "Keywords";

	public int addKeywords(Keywords keywords) {
		return ((Number) this.addObject(keywords)).intValue();
	}

	public void deleteKeywords(Keywords keywords) {
		this.deleteObject(keywords);
	}

	public void deleteKeywordsById(int id) {
		String hql = "delete from " + TABLE + " where id=" + id;
		this.executeUpdate(hql);

	}

	public List findAll() {
		String hql = "from " + TABLE + " order by pos";
		return this.executeFind(hql);
	}

	public List getKeywords(QueryInfo qi, PageBuilder pb) {
		String hql = "from " + TABLE;
		String hql_count = "select count(*) from " + TABLE;

		return this.getObjects(hql, hql_count, qi, pb);
	}

	public Keywords getKeywordsById(int id) {
		String hql = "from " + TABLE + " where id=" + id;
		return (Keywords) this.findUniqueResult(hql);
	}

	public List getKeywordsByScope(String scope) {
		String hql = "from " + TABLE + " where scope in(" + scope
				+ ")  order by pos";
		return this.executeFind(hql);
	}

	public List getKeywordsByScope(String scope, int status) {
		String hql = "from " + TABLE + " where scope in(" + scope
				+ ") and status=" + status + " order by pos";
		return this.executeFind(hql);
	}

	public void saveKeywords(Keywords keywords) {
		this.saveObject(keywords);

	}

}
