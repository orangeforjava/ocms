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
package org.openuap.cms.stat.model;

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * 
 * <p>
 * Cms内容计数.
 * </p>
 * 
 * <p>
 * $Id: CmsCount.java 3920 2010-10-26 11:41:54Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class CmsCount extends BaseObject implements java.io.Serializable {

	// Constructors

	/** default constructor */
	private Long indexId;

	private Long contentId;

	private Long nodeId;

	private Long tableId;

	private Long hitsTotal;

	private Long hitsToday;

	private Long hitsWeek;

	private Long hitsMonth;

	private Long commentNum;

	private Long hitsDate;

	// Constructors

	/** default constructor */
	public CmsCount() {
	}

	/**
	 * constructor with id
	 * 
	 * @param indexId
	 *            Long
	 */
	public CmsCount(Long indexId) {
		this.indexId = indexId;
	}

	// Property accessors

	public Long getIndexId() {
		return this.indexId;
	}

	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}

	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Long getHitsTotal() {
		return this.hitsTotal;
	}

	public void setHitsTotal(Long hitsTotal) {
		this.hitsTotal = hitsTotal;
	}

	public Long getHitsToday() {
		return this.hitsToday;
	}

	public void setHitsToday(Long hitsToday) {
		this.hitsToday = hitsToday;
	}

	public Long getHitsWeek() {
		return this.hitsWeek;
	}

	public void setHitsWeek(Long hitsWeek) {
		this.hitsWeek = hitsWeek;
	}

	public Long getHitsMonth() {
		return this.hitsMonth;
	}

	public void setHitsMonth(Long hitsMonth) {
		this.hitsMonth = hitsMonth;
	}

	public Long getCommentNum() {
		return this.commentNum;
	}

	public void setCommentNum(Long commentNum) {
		this.commentNum = commentNum;
	}

	public Long getHitsDate() {
		return this.hitsDate;
	}

	public void setHitsDate(Long hitsDate) {
		this.hitsDate = hitsDate;
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof CmsCount) {
			CmsCount that = (CmsCount) o;
			if (this.getIndexId().equals(that.getIndexId())) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getIndexId() == null ? 0 : this.getIndexId().hashCode());
		return result;

	}

	public void init() {
		this.setCommentNum(new Long(0));
		this.setHitsDate(new Long(-1L));
		this.setHitsToday(new Long(0));
		this.setHitsWeek(new Long(0));
		this.setHitsMonth(new Long(0));
		this.setHitsTotal(new Long(0));
	}
}
