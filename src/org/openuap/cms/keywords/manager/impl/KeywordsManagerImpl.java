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
package org.openuap.cms.keywords.manager.impl;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.keywords.dao.KeywordsDao;
import org.openuap.cms.keywords.manager.KeywordsManager;
import org.openuap.cms.keywords.model.Keywords;

/**
 * <p>
 * 关键词管理实现.
 * </p>
 * 
 * <p>
 * $Id: KeywordsManagerImpl.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class KeywordsManagerImpl implements KeywordsManager {
	private KeywordsDao dao;

	
	public int addKeywords(Keywords keywords) {
		return dao.addKeywords(keywords);
	}

	public void deleteKeywords(Keywords keywords) {
		dao.deleteKeywords(keywords);
	}

	public void deleteKeywordsById(int id) {
		dao.deleteKeywordsById(id);
	}

	public List findAll() {
		return dao.findAll();
	}

	public List getKeywords(QueryInfo qi, PageBuilder pb) {
		return dao.getKeywords(qi, pb);
	}

	public Keywords getKeywordsById(int id) {
		return dao.getKeywordsById(id);
	}

	public List getKeywordsByScope(String scope) {
		return dao.getKeywordsByScope(scope);
	}

	public List getKeywordsByScope(String scope, int status) {
		return dao.getKeywordsByScope(scope, status);
	}

	public void saveKeywords(Keywords keywords) {
		dao.saveKeywords(keywords);

	}

	public void setDao(KeywordsDao dao) {
		this.dao = dao;
	}
	
}
