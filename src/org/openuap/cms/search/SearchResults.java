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
 * 搜索结果.
 * </p>
 * 
 * <p>
 * $Id: SearchResults.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SearchResults implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2081536326309407874L;

	/**
	 * 分页对象
	 */
	private PageBuilder pageBuilder;

	/**
	 * 搜索耗时(微秒)
	 */
	private long searchTime;

	/**
	 * 返回命中的结果,可能是List对象从数据库，也可能是CompassHit[]数组对象从Lucence
	 */
	private Object hits;

	public SearchResults(Object hits, PageBuilder pageBuilder, long searchTime) {
		this.hits = hits;
		this.pageBuilder = pageBuilder;
		this.searchTime = searchTime;
	}

	public Object getHits() {
		return hits;
	}

	public void setHits(Object hits) {
		this.hits = hits;
	}

	public PageBuilder getPageBuilder() {
		return pageBuilder;
	}

	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}

	public long getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(long searchTime) {
		this.searchTime = searchTime;
	}
}
