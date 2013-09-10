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
package org.openuap.cms.comment.model;

import java.util.Date;

import org.openuap.cms.comment.ICommentPost;

/**
 * 
 * <p>
 * CMS评论实体.
 * </p>
 * 
 * <p>
 * $Id: CommentPost.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class CommentPost implements ICommentPost, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -250933656289004716L;

	/**
	 * 缺省构造函数
	 */
	public CommentPost() {
	}

	/** 评论Id. */
	private Long id;

	/** 根帖子id. */
	private Long rootId;

	/** 父帖子id. */
	private Long parentId;

	/** 用户id. */
	private Long userId;

	/** 用户名. */
	private String userName;

	/** 内容索引Id. */
	private String objectId;
	/** 内容索引类型. */
	private String objectType;

	private Long catalogId;

	/** 评论日期. */
	private Long creationDate;

	/** 最后修改日期. */
	private Long lastModifyDate;

	/** 帖子标题. */
	private String title;

	/** 评论内容. */
	private String content;

	/** 评论IP. */
	private String ip;

	/** 发贴者真实Ip */
	private String realIp;

	/** 此帖状态. */
	private int status;

	/** 支持者数量. */
	private int agreeCount;

	/** 反对者数量. */
	private int opposeCount;
	/** 隐藏Ip状态. */
	private Integer hiddenIpStatus;

	public int getAgreeCount() {
		return agreeCount;
	}

	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
	}

	public int getOpposeCount() {
		return opposeCount;
	}

	public void setOpposeCount(int opposeCount) {
		this.opposeCount = opposeCount;
	}

	/**
	 * constructor with id
	 * 
	 * @param commentId
	 */
	public CommentPost(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof CommentPost) {
			CommentPost that = (CommentPost) obj;
			if (this.id.equals(that.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public Date getDisplayCreationDate() {
		return new Date(creationDate);
	}

	public Long getId() {
		return this.id;
	}

	public String getIp() {
		return this.ip;
	}

	public Long getLastModifyDate() {
		return this.lastModifyDate;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public String getRealIp() {
		return this.realIp;
	}

	public Long getRootId() {
		return this.rootId;
	}

	public int getStatus() {
		return this.status;
	}

	public String getTitle() {
		return this.title;
	}

	public Long getUserId() {
		return this.userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;

	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setLastModifyDate(Long lastModifyDate) {
		this.lastModifyDate = lastModifyDate;

	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;

	}

	public void setRealIp(String realIp) {
		this.realIp = realIp;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public String getDisplayIp() {
		if (this.getHiddenIpStatus() == 1) {
			// 用户想隐藏IP地址
			return "*";
		} else {
			if (this.realIp != null) {
				// 屏蔽掉最后一位的IP地址
				int pos = realIp.lastIndexOf(".");
				if (pos > 0) {
					String displayIp = realIp.substring(0, pos);
					return displayIp + ".*";
				}
				return realIp;
			}
			if (this.ip != null) {
				// 屏蔽掉最后一位的IP地址
				int pos = ip.lastIndexOf(".");
				if (pos > 0) {
					String displayIp = ip.substring(0, pos);
					return displayIp + ".*";
				}
				return ip;
			}
		}
		return "*";
	}

	public Integer getHiddenIpStatus() {
		return this.hiddenIpStatus;
	}

	public void setHiddenIpStatus(Integer hiddenIp) {
		this.hiddenIpStatus = hiddenIp;

	}

}
