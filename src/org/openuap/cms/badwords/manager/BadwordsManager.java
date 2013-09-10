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
package org.openuap.cms.badwords.manager;

import java.util.List;

import org.openuap.cms.badwords.model.Badwords;

/**
 * <p>
 * 敏感词管理接口.
 * </p>
 * 
 * <p>
 * $Id: BadwordsManager.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface BadwordsManager {

	public List findAll();

	public List getBadwordsByScope(String scope);

	/**
	 * 根据作用范围获得敏感词对象
	 * 
	 * @param scope
	 *            作用范围
	 * @param status
	 *            可用状态
	 * @return List
	 */
	public List getBadwordsByScope(String scope, int status);

	public Badwords getBadwordsById(int id);

	public void deleteBadwordsById(int id);

	public int addBadwords(Badwords badwords);

	public void saveBadwords(Badwords badwords);

	public void deleteBadwords(Badwords badwords);

}
