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
package org.openuap.cms.badwords.manager.impl;

import java.util.List;

import org.openuap.cms.badwords.cache.BadwordsCache;
import org.openuap.cms.badwords.dao.BadwordsDao;
import org.openuap.cms.badwords.manager.BadwordsManager;
import org.openuap.cms.badwords.model.Badwords;

/**
 * <p>
 * 敏感词管理实现.
 * </p>
 * 
 * <p>
 * $Id: BadwordsManagerImpl.java 3966 2010-12-16 12:10:02Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0 
 */
public class BadwordsManagerImpl implements BadwordsManager {

	private BadwordsDao badwordsDao;

	public BadwordsManagerImpl() {
	}

	public List findAll() {
		return badwordsDao.findAll();
	}

	public List getBadwordsByScope(String scope) {
		return badwordsDao.getBadwordsByScope(scope);
	}

	public Badwords getBadwordsById(int id) {
		return badwordsDao.getBadwordsById(id);
	}

	public void deleteBadwordsById(int id) {
		Badwords badwords=getBadwordsById(id);
		this.deleteBadwords(badwords);
	}

	public List getBadwordsByScope(String scope, int status) {
		return badwordsDao.getBadwordsByScope(scope, status);
	}

	public void setBadwordsDao(BadwordsDao badwordsDao) {
		this.badwordsDao = badwordsDao;
	}
	public int addBadwords(Badwords badwords) {
		BadwordsCache.remove();
		return this.badwordsDao.addBadwords(badwords);
	}

	public void saveBadwords(Badwords badwords) {
		BadwordsCache.remove();
		this.badwordsDao.saveBadwords(badwords);
	}
	public void deleteBadwords(Badwords badwords){
		BadwordsCache.remove();
		this.badwordsDao.deleteBadwords(badwords);
	}
}
