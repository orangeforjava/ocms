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
package org.openuap.cms.badwords.dao.jdbc;

import java.util.List;

import org.openuap.base.dao.jdbc.BaseDaoSupport;
import org.openuap.cms.badwords.dao.BadwordsDao;
import org.openuap.cms.badwords.model.Badwords;

/**
 * <p>
 * 敏感词DAO JDBC实现.
 * </p>
 * 
 * <p>
 * $Id: BadwordsDaoJdbcImpl.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0 
 */
public class BadwordsDaoJdbcImpl extends BaseDaoSupport implements BadwordsDao {

	protected static transient String TABLE = "common_badwords";

	public BadwordsDaoJdbcImpl() {
	}

	public List findAll() {
		return this.getDBObjectList("SELECT * FROM " + TABLE + " ORDER BY POS",
				Badwords.class);
	}

	public List getBadwordsByScope(String scope) {
		String sql = "";

		sql = "SELECT * FROM " + TABLE + " WHERE scope in(" + scope + ")"
				+ " ORDER BY POS";
		return this.getDBObjectList(sql, Badwords.class);

	}

	public Badwords getBadwordsById(int id) {
		String sql = "SELECT * FROM " + TABLE + " WHERE id=" + id;
		return (Badwords) this.getDBObject(sql, Badwords.class);

	}

	public void deleteBadwordsById(int id) {
		String sql = "DELETE FROM " + TABLE + " WHERE id=" + id;
		this.getJdbcTemplate().execute(sql);
	}

	public List getBadwordsByScope(String scope, int status) {
		String sql = "";

		sql = "SELECT * FROM " + TABLE + " WHERE scope in(" + scope
				+ ") AND status=" + status + " ORDER BY POS";
		;

		return this.getDBObjectList(sql, Badwords.class);
	}

	public int addBadwords(Badwords badwords) {
		this.doInsert(badwords);
		return badwords.getId();
	}

	public void deleteBadwords(Badwords badwords) {
		
		this.doDelete(badwords);
	}

	public void saveBadwords(Badwords badwords) {
		this.doUpdate(badwords);
		
	}
}
