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
package org.openuap.cms.badwords.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.badwords.dao.BadwordsDao;
import org.openuap.cms.badwords.model.Badwords;

/**
 * <p>
 * 敏感词Hibernate DAO实现
 * </p>
 * 
 * <p>
 * $Id: BadwordsDaoImpl.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class BadwordsDaoImpl extends BaseDaoHibernate implements BadwordsDao {
	
	protected static transient String TABLE = "Badwords";

	public void deleteBadwordsById(int id) {
		String hql = "delete from " + TABLE + " where id=" + id;
		this.executeUpdate(hql);
	}

	public List findAll() {
		String hql = "from " + TABLE + " order by pos";
		return this.executeFind(hql);
	}

	public Badwords getBadwordsById(int id) {
		String hql = "from " + TABLE + " where id=" + id;
		return (Badwords) this.findUniqueResult(hql);
	}

	public List getBadwordsByScope(String scope) {
		String hql = "from " + TABLE + " where scope in(" + scope + ")" + " order by pos";
		return this.executeFind(hql);
	}

	public List getBadwordsByScope(String scope, int status) {
		String hql = "from " + TABLE + " where scope in(" + scope + ") and status=" + status + " order by pos";
		return this.executeFind(hql);
	}

	public int addBadwords(Badwords badwords) {
		return ((Number) this.addObject(badwords)).intValue();
	}

	public void saveBadwords(Badwords badwords) {
		this.saveObject(badwords);
	}
	public void deleteBadwords(Badwords badwords){
		this.deleteObject(badwords);
	}
}
