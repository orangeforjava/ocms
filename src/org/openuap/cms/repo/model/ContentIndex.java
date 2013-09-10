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
package org.openuap.cms.repo.model;

import java.io.Serializable;
import java.util.Set;

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * 
 * <p>
 * 内容索引实体
 * </p>
 * 
 * <p>
 * $Id: ContentIndex.java 3999 2011-01-06 15:58:59Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class ContentIndex extends BaseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 424288773421501949L;
	public static final int STATE_UNPUBLISHED = 0;
	public static final int STATE_PUBLISHED = 1;
	public static final int STATE_DELETED = -1;

	public static final int TYPE_SOLID = 1;
	public static final int TYPE_INDEX = 2;
	private int hashValue = 0;

	/** 索引id. */
	private Long indexId;

	/** 内容id. */
	private Long contentId;

	/** 结点id. */
	private Long nodeId;

	/** 内容表id. */
	private Long tableId;

	/** 父索引id. */
	private Long parentIndexId;

	/** 索引类型，实、虚、索引. */
	private Integer type;

	/** 发布日期. */
	private Long publishDate;

	/** 自定义模板. */
	private String selfTemplate;

	/** 自定义发布psn. */
	private String selfPsn;

	/** 自定义发布文件名. */
	private String selfPublishFileName;

	/** 自定义发布点URL. */
	private String selfPsnUrl;

	/** 自定义外部URL. */
	private String selfUrl;

	/** 索引状态，0-未发布，1-发布,-1回收站. */
	private Integer state;

	/** 发布url. */
	private String url;

	/** 置顶权重. */
	private Integer top;

	/** 精华权重. */
	private Integer pink;

	/** 排序权重. */
	private Integer sort;

	/** 权限，用来动态发布时指定权限.可以是简单的用户id组合或者是区别匿名还是登陆. */
	private String permission;
	/** 内容引用的资源集合. */
	private Set resourceSet;

	/** 用来保留导入的老数据信息. */
	private Long oldId;

	private String oldTable;

	private Long oldHits;

	/** 发布模式,不,静态,动态. */
	private Integer publishMode;

	/** 内容页Portal URL. */
	private String contentPortalUrl;

	// ～关联内容属性,冗余信息，用来提高效率
	/** 内容标题. */
	private String contentTitle;
	/** 内容首图. */
	private String contentPhoto;
	/** 作者名. */
	private String creationUserName;
	// ～统计属性
	private Long hitsTotal;

	private Long hitsToday;

	private Long hitsWeek;

	private Long hitsMonth;
	/** 评论数目. */
	private Long commentNum;

	private Long hitsDate;
	/** 顶数. */
	private Long dits;
	/** 踩数. */
	private Long downs;

	// ~工作流状态
	/** 默认的通过审核状态为0，未审核状态为小于0. */
	private Integer workflowState;

	// ~为增量索引准备，所有对内容的修改都要更新此属性，以便让索引有机会更新
	private Long lastModifiedDate;

	/**
	 * 
	 */
	public ContentIndex() {
	}

	/**
	 * 
	 * @param indexid
	 */
	public ContentIndex(Long indexid) {
		this.setIndexId(indexid);
	}

	public Long getDowns() {
		return downs;
	}

	public void setDowns(Long downs) {
		this.downs = downs;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getIndexId() {

		return indexId;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param indexId
	 *            Integer
	 */
	public void setIndexId(Long indexId) {
		this.hashValue = 0;

		this.indexId = indexId;
	}

	/**
	 * Return the value of the ContentID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getContentId() {

		return contentId;
	}

	/**
	 * Set the value of the ContentID column.
	 * 
	 * @param contentId
	 *            Integer
	 */
	public void setContentId(Long contentId) {

		this.contentId = contentId;
	}

	/**
	 * Return the value of the NodeID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getNodeId() {

		return nodeId;
	}

	/**
	 * Set the value of the NodeID column.
	 * 
	 * @param nodeId
	 *            Integer
	 */
	public void setNodeId(Long nodeId) {

		this.nodeId = nodeId;
	}

	/**
	 * Return the value of the ParentIndexID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getParentIndexId() {

		return parentIndexId;
	}

	/**
	 * Set the value of the ParentIndexID column.
	 * 
	 * @param parentIndexId
	 *            Integer
	 */
	public void setParentIndexId(Long parentIndexId) {

		this.parentIndexId = parentIndexId;
	}

	/**
	 * Return the value of the Type column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 * Set the value of the Type column.
	 * 
	 * @param type
	 *            Byte
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * Return the value of the PublishDate column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getPublishDate() {

		return publishDate;
	}

	/**
	 * Set the value of the PublishDate column.
	 * 
	 * @param publishDate
	 *            Integer
	 */
	public void setPublishDate(Long publishDate) {

		this.publishDate = publishDate;
	}

	/**
	 * Return the value of the SelfTemplate column.
	 * 
	 * @return java.lang.String
	 */
	public String getSelfTemplate() {

		return selfTemplate;
	}

	/**
	 * Set the value of the SelfTemplate column.
	 * 
	 * @param selfTemplate
	 *            String
	 */
	public void setSelfTemplate(String selfTemplate) {

		this.selfTemplate = selfTemplate;
	}

	/**
	 * Return the value of the SelfPSN column.
	 * 
	 * @return java.lang.String
	 */
	public String getSelfPsn() {

		return selfPsn;
	}

	/**
	 * Set the value of the SelfPSN column.
	 * 
	 * @param selfPsn
	 *            String
	 */
	public void setSelfPsn(String selfPsn) {

		this.selfPsn = selfPsn;
	}

	/**
	 * Return the value of the SelfPublishFileName column.
	 * 
	 * @return java.lang.String
	 */
	public String getSelfPublishFileName() {

		return selfPublishFileName;
	}

	/**
	 * Set the value of the SelfPublishFileName column.
	 * 
	 * @param selfPublishFileName
	 *            String
	 */
	public void setSelfPublishFileName(String selfPublishFileName) {

		this.selfPublishFileName = selfPublishFileName;
	}

	/**
	 * Return the value of the SelfPSNURL column.
	 * 
	 * @return java.lang.String
	 */
	public String getSelfPsnUrl() {

		return selfPsnUrl;
	}

	/**
	 * Set the value of the SelfPSNURL column.
	 * 
	 * @param selfPsnUrl
	 *            String
	 */
	public void setSelfPsnUrl(String selfPsnUrl) {

		this.selfPsnUrl = selfPsnUrl;
	}

	/**
	 * Return the value of the SelfURL column.
	 * 
	 * @return java.lang.String
	 */
	public String getSelfUrl() {

		return selfUrl;
	}

	/**
	 * Set the value of the SelfURL column.
	 * 
	 * @param selfUrl
	 *            String
	 */
	public void setSelfUrl(String selfUrl) {

		this.selfUrl = selfUrl;
	}

	/**
	 * Return the value of the State column.
	 * 
	 * @return java.lang.Short
	 */
	public Integer getState() {
		return this.state;
	}

	/**
	 * Set the value of the State column.
	 * 
	 * @param state
	 *            Short
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * Return the value of the URL column.
	 * 
	 * @return java.lang.String
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Set the value of the URL column.
	 * 
	 * @param url
	 *            String
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Return the value of the Top column.
	 * 
	 * @return java.lang.Short
	 */
	public Integer getTop() {
		return this.top;
	}

	/**
	 * Set the value of the Top column.
	 * 
	 * @param top
	 *            Short
	 */
	public void setTop(Integer top) {
		this.top = top;
	}

	/**
	 * Return the value of the Pink column.
	 * 
	 * @return java.lang.Short
	 */
	public Integer getPink() {
		return this.pink;
	}

	/**
	 * Set the value of the Pink column.
	 * 
	 * @param pink
	 *            Short
	 */
	public void setPink(Integer pink) {
		this.pink = pink;
	}

	/**
	 * Return the value of the Sort column.
	 * 
	 * @return java.lang.Short
	 */
	public Integer getSort() {
		return this.sort;
	}

	public Set getResourceSet() {
		return resourceSet;
	}

	public Long getTableId() {
		return tableId;
	}

	public Long getOldHits() {
		return oldHits;
	}

	public Long getOldId() {
		return oldId;
	}

	public String getOldTable() {
		return oldTable;
	}

	public String getPermission() {
		return permission;
	}

	public String getContentPortalUrl() {
		return contentPortalUrl;
	}

	public Integer getPublishMode() {
		return publishMode;
	}

	/**
	 * Set the value of the Sort column.
	 * 
	 * @param sort
	 *            Short
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setResourceSet(Set resourceSet) {
		this.resourceSet = resourceSet;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public void setOldHits(Long oldHits) {
		this.oldHits = oldHits;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public void setOldTable(String oldTable) {
		this.oldTable = oldTable;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setContentPortalUrl(String contentPortalUrl) {
		this.contentPortalUrl = contentPortalUrl;
	}

	public void setPublishMode(Integer publishMode) {
		this.publishMode = publishMode;
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof ContentIndex)) {
			return false;
		}
		ContentIndex that = (ContentIndex) rhs;
		if (this.getIndexId() == null || that.getIndexId() == null) {
			return false;
		}
		return (this.getIndexId().equals(that.getIndexId()));
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int indexidValue = this.getIndexId() == null ? 0 : this
					.getIndexId().hashCode();
			result = result * 37 + indexidValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	/* Add customized code below */
	public void init() {
		this.setParentIndexId(new Long(-1));
		this.setPink(new Integer("0"));
		this.setSelfPsn("");
		this.setSelfPsnUrl("");
		this.setSelfPublishFileName("");
		this.setSelfTemplate("");
		this.setSelfUrl("");
		this.setSort(new Integer("0"));
		this.setTop(new Integer("0"));
		this.setUrl("");
	}

	public int getHashValue() {
		return hashValue;
	}

	public void setHashValue(int hashValue) {
		this.hashValue = hashValue;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getContentPhoto() {
		return contentPhoto;
	}

	public void setContentPhoto(String contentPhoto) {
		this.contentPhoto = contentPhoto;
	}

	public Long getHitsTotal() {
		return hitsTotal;
	}

	public void setHitsTotal(Long hitsTotal) {
		this.hitsTotal = hitsTotal;
	}

	public Long getHitsToday() {
		return hitsToday;
	}

	public void setHitsToday(Long hitsToday) {
		this.hitsToday = hitsToday;
	}

	public Long getHitsWeek() {
		return hitsWeek;
	}

	public void setHitsWeek(Long hitsWeek) {
		this.hitsWeek = hitsWeek;
	}

	public Long getHitsMonth() {
		return hitsMonth;
	}

	public void setHitsMonth(Long hitsMonth) {
		this.hitsMonth = hitsMonth;
	}

	public Long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Long commentNum) {
		this.commentNum = commentNum;
	}

	public Long getHitsDate() {
		return hitsDate;
	}

	public void setHitsDate(Long hitsDate) {
		this.hitsDate = hitsDate;
	}

	public String getCreationUserName() {
		return creationUserName;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public Integer getWorkflowState() {
		return workflowState;
	}

	public void setWorkflowState(Integer workflowState) {
		this.workflowState = workflowState;
	}

	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getDits() {
		return dits;
	}

	public void setDits(Long dits) {
		this.dits = dits;
	}

	public boolean isPublished() {
		return this.state.equals(ContentIndex.STATE_PUBLISHED);
	}

	public boolean isDeleted() {
		return this.state.equals(ContentIndex.STATE_DELETED);
	}

	public boolean isUnPublished() {
		return this.state.equals(ContentIndex.STATE_UNPUBLISHED);
	}
}
