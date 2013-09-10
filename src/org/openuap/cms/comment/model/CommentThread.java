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

import org.openuap.cms.comment.ICommentThread;

/**
 * <p>
 * 评论主题实体.
 * </p>
 * 
 * <p>
 * $Id: CommentThread.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CommentThread implements ICommentThread, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8177194674348697520L;

	private Long id;
	/** 评论的对象id.*/
	private String objectId;
	/** 评论的对象类型.*/
	private String objectType;
	private Long catalogId;
	/** 标题.*/
	private String title;
	/** 内容摘要.*/
	private String content;
	/** 内容来源.*/
	private String source;
	private String url;
	private int reply;
	private Long creationDate;
	private String userName;
	private Long lastPostDate;
	private String lastPostName;
	/** 此帖状态. */
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getReply() {
		return reply;
	}

	public void setReply(int reply) {
		this.reply = reply;
	}

	public Long getLastPostDate() {
		return lastPostDate;
	}

	public void setLastPostDate(Long lastPostDate) {
		this.lastPostDate = lastPostDate;
	}

	public String getLastPostName() {
		return lastPostName;
	}

	public void setLastPostName(String lastPostName) {
		this.lastPostName = lastPostName;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
