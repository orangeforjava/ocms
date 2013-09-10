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

import java.util.Date;

/**
 * <p>
 * 评论发帖接口.
 * </p>
 * 
 * 
 * <p>
 * $Id: ICommentPost.java,v 1.1 2006/08/31 02:26:10 Administrator Exp$
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ICommentPost {

	/**
	 * 设置评论的Id
	 * 
	 * @param id
	 *            评论Id
	 */
	public void setId(Long id);

	/**
	 * 获得评论的Id
	 * 
	 * @return 评论Id
	 */
	public Long getId();

	/**
	 * 设置评论的主题Id
	 * 
	 * @param rootId
	 *            评论主题Id
	 */
	public void setRootId(Long rootId);

	/**
	 * 获取评论主题的Id
	 * 
	 * @return 评论主题Id
	 */
	public Long getRootId();

	/**
	 * 设置评论针对的内容索引Id
	 * 
	 * @param id
	 *            内容索引Id
	 */
	public void setObjectId(String id);

	/**
	 * 获得评论针对的内容索引Id
	 * 
	 * @return 内容索引Id
	 */
	public String getObjectId();
	
	/**
	 * 设置评论针对的内容索引Id
	 * 
	 * @param id
	 *            内容索引Id
	 */
	public void setObjectType(String type);

	/**
	 * 获得评论针对的内容索引Id
	 * 
	 * @return 内容索引Id
	 */
	public String getObjectType();

	/**
	 * 设置评论的父贴Id
	 * 
	 * @param parentId
	 *            评论的父贴Id
	 */
	public void setParentId(Long parentId);

	/**
	 * 获得评论的父帖Id
	 * 
	 * @return 评论的父贴Id
	 */
	public Long getParentId();

	/**
	 * 设置评论主题
	 * 
	 * @param title
	 *            评论的主题
	 */
	public void setTitle(String title);

	/**
	 * 获得评论的主题
	 * 
	 * @return 评论的主题
	 */
	public String getTitle();

	/**
	 * 设置评论的用户名
	 * 
	 * @param userName
	 *            评论的用户名
	 */
	public void setUserName(String userName);

	/**
	 * 获得评论的用户名
	 * 
	 * @return String
	 */
	public String getUserName();

	/**
	 * 设置评论的用户Id
	 * 
	 * @param uid
	 *            用户Id
	 */
	public void setUserId(Long uid);

	/**
	 * 获得评论用户的用户id
	 * 
	 * @return 用户Id
	 */
	public Long getUserId();

	/**
	 * 设置评论内容
	 * 
	 * @param content
	 *            评论内容
	 */
	public void setContent(String content);

	/**
	 * 获得评论内容
	 * 
	 * @return 评论内容
	 */
	public String getContent();

	/**
	 * 设置发表评论的IP
	 * 
	 * @param ip
	 *            发表评论的Ip
	 */
	public void setIp(String ip);

	/**
	 * 获得发表评论的Ip
	 * 
	 * @return 发表评论的Ip
	 */
	public String getIp();

	/**
	 * 设置发表评论的真实Ip
	 * 
	 * @param realIp
	 *            发表评论的真实Ip
	 */
	public void setRealIp(String realIp);

	/**
	 * 获得发表评论的真实Ip
	 * 
	 * @return 发表评论的真实Ip
	 */
	public String getRealIp();

	/**
	 * 设置评论的添加日期
	 * 
	 * @param creationDate
	 *            评论的添加日期
	 */
	public void setCreationDate(Long creationDate);

	/**
	 * 获得评论的添加日期
	 * 
	 * @return 评论的添加日期
	 */
	public Long getCreationDate();

	/**
	 * 设置评论的修改日期
	 * 
	 * @param creationDate
	 *            评论的修改日期
	 */
	public void setLastModifyDate(Long creationDate);

	/**
	 * 获得评论的修改日期
	 * 
	 * @return 评论的修改日期
	 */
	public Long getLastModifyDate();

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

	public int getAgreeCount();

	public void setAgreeCount(int agreeCount);

	public int getOpposeCount();

	public void setOpposeCount(int opposeCount);
	
	public Long getCatalogId();

	public void setCatalogId(Long catalogId);
	/**
	 * 返回创建日期的日期格式
	 * @return
	 */
	public Date getDisplayCreationDate();
	
	public String getDisplayIp();
	
	public Integer getHiddenIpStatus();
	
	public void setHiddenIpStatus(Integer hiddenIp);
}
