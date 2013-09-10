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
package org.openuap.cms.search;

import org.openuap.base.util.context.PageBuilder;

/**
 * <p>
 * 搜索引擎接口.
 * </p>
 * 
 * <p>
 * $Id: SearchEngine.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface SearchEngine {
	/**
	 * 执行搜索
	 * @param searchCommand 搜索命令对象
	 * @return 搜索结果对象
	 * @throws Exception
	 */
	public SearchResults doSearch(SearchCommand searchCommand) throws Exception;
	/**
	 * 执行搜索，只返回数量
	 * @param searchCommand
	 * @return
	 * @throws Exception
	 */
	public PageBuilder doSearchCount(SearchCommand searchCommand) throws Exception;
}
