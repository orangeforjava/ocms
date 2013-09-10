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
package org.openuap.cms.comment;

/**
 * <p>
 * 评论主题接口.
 * </p>
 * 
 * <p>
 * $Id: ICommentThread.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ICommentThread {

	public Long getId();

	public void setId(Long id);

	public String getObjectId();

	public void setObjectId(String objectId);

	public String getObjectType();

	public void setObjectType(String objectType);

	public Long getCatalogId();

	public void setCatalogId(Long catalogId);

	public String getTitle();

	public void setTitle(String title);

	public String getContent();

	public void setContent(String content);

	public String getSource();

	public void setSource(String source);

	public String getUrl();

	public void setUrl(String url);

	public int getReply();

	public void setReply(int reply);

	public Long getLastPostDate();

	public void setLastPostDate(Long lastPostDate);

	public String getLastPostName();

	public void setLastPostName(String lastPostName);

	public Long getCreationDate();

	public void setCreationDate(Long creationDate);

	public String getUserName();

	public void setUserName(String userName);

	/**
	 * 设置评论状态
	 * 
	 * @param status
	 *            评论状态
	 */
	public void setStatus(int status);

	/**
	 * 获得评论的状态
	 * 
	 * @return 评论的状态
	 */
	public int getStatus();
}
