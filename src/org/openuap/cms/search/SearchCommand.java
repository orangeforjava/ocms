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

/**
 * <p>
 * 查询命令，用来封装查询请求.
 * </p>
 * 
 * <p>
 * $Id: SearchCommand.java 4012 2011-01-24 11:05:06Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SearchCommand implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4439678904523415046L;

	private String keyword;

	private String fields;

	private String nodeId;

	private String nodeGUID;

	private String tableId;

	private String ignore;

	private String order;

	private String where;

	/** 需要高亮的属性. */
	private String highlights;

	private int page;

	private String pageNum;

	private String url;
	
	private String ignoreIndex;
	
	private boolean parseKeyword=true;
	
	
	
	public boolean isParseKeyword() {
		return parseKeyword;
	}

	public void setParseKeyword(boolean parseKeyword) {
		this.parseKeyword = parseKeyword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public SearchCommand(String keyword, String fields, String nodeId,
			String nodeGUID, String tableId, String ignore, String order,
			String where, String highlights, int page, String pageNum,
			String url) {
		this.keyword = keyword;
		this.fields = fields;
		this.nodeId = nodeId;
		this.nodeGUID = nodeGUID;
		this.tableId = tableId;
		this.ignore = ignore;
		this.order = order;
		this.where = where;
		this.highlights = highlights;
		this.page = page;
		this.pageNum = pageNum;
		this.url = url;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getNodeGUID() {
		return nodeGUID;
	}

	public void setNodeGUID(String nodeGUID) {
		this.nodeGUID = nodeGUID;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	/**
	 * @return the highlights
	 */
	public String getHighlights() {
		return highlights;
	}

	/**
	 * @param highlights
	 *            the highlights to set
	 */
	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}

	public String getIgnoreIndex() {
		return ignoreIndex;
	}

	public void setIgnoreIndex(String ignoreIndex) {
		this.ignoreIndex = ignoreIndex;
	}

}
