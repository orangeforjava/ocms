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
package org.openuap.cms.comment.service;

import java.util.List;

import org.openuap.cms.comment.ICommentPost;

/**
 * 
 * <p>
 * 评论服务接口
 * </p>
 * 
 * <p>
 * $Id: CommentService.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface CommentService {
	/**
	 * 
	 * @param indexId
	 * @param author
	 * @param ip
	 * @param comment
	 * @return
	 */
	public Long addComment(Long indexId, String author, String ip,
			String comment);
	
	public List getCommentsByIndex(Long indexId);

	/**
	 * 添加评论
	 * 
	 * @param iCommentPost
	 *            评论
	 * @return 新增评论的id
	 */
	public Long addComment(ICommentPost iCommentPost);

	/**
	 * 更新评论
	 * 
	 * @param iCommentPost
	 *            评论
	 */
	public void updateComment(ICommentPost iCommentPost);

	/**
	 * 删除评论
	 * 
	 * @param iCommentPost
	 *            评论
	 */
	public void deleteComment(ICommentPost iCommentPost);

	/**
	 * 删除评论
	 * 
	 * @param id
	 *            评论id
	 */
	public void deleteCommentById(long id);

	/**
	 * 根据id获得评论
	 * 
	 * @param id
	 *            评论id
	 * @return 评论
	 */
	public ICommentPost getCommentById(long id);

	/**
	 * 得到平板模式的评论集合
	 * 
	 * @param indexId
	 *            内容索引id
	 * @param order
	 *            评论排序
	 * @return List 评论集合
	 */
	public List getFlatComments(long indexId, String order);

	/**
	 * 得到树型模式的评论集合
	 * 
	 * @param indexId
	 *            内容索引id
	 * @return List 评论集合
	 */
	public List getThreadComments(long indexId);

	/**
	 * 得到评论数目
	 * 
	 * @param indexId
	 *            内容索引id
	 * @return int 评论数目
	 */
	public int getCommentsCount(long indexId);
}
