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
package org.openuap.cms.keywords.dao;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.keywords.model.Keywords;

/**
 * <p>
 * 关键字DAO接口.
 * </p>
 * 
 * <p>
 * $Id: KeywordsDao.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface KeywordsDao {

	public List findAll();

	public List getKeywordsByScope(String scope);

	/**
	 * 根据作用范围获得关键字列表
	 * 
	 * @param scope
	 *            作用范围
	 * @param status
	 *            可用状态
	 * @return List
	 */
	public List getKeywordsByScope(String scope, int status);

	public Keywords getKeywordsById(int id);

	public void deleteKeywordsById(int id);

	public int addKeywords(Keywords keywords);

	public void saveKeywords(Keywords keywords);

	public void deleteKeywords(Keywords keywords);

	/**
	 * 获取关键字列表
	 * 
	 * @param qi
	 *            查询信息
	 * @param pb
	 *            分页信息
	 * @return 关键字列表
	 */
	public List getKeywords(QueryInfo qi, PageBuilder pb);
}
