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
package org.openuap.cms.comment.manager;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.ICommentThread;

/**
 * <p>
 * 内容管理接口.
 * </p>
 * 
 * <p>
 * $Id: CommentManager.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface CommentManager {

	/**
	 * 根据id获得评论
	 * 
	 * @param commentId
	 * @return
	 */
	public ICommentPost getCommentById(Long commentId);

	/**
	 * 保存评论
	 * 
	 * @param commentPost
	 */
	public void saveComment(ICommentPost commentPost);

	/**
	 * 添加评论
	 * 
	 * @param commentPost
	 * @return
	 */
	public Long addComment(ICommentPost commentPost);

	public Long addCommentThread(ICommentThread commentThread);

	/**
	 * 删除评论
	 * 
	 * @param commentPost
	 */
	public void removeComment(ICommentPost commentPost);

	/**
	 * 根据Id删除评论
	 * 
	 * @param commentId
	 */
	public void removeCommentById(Long commentId);

	/**
	 * 根据内容索引获得评论
	 * 
	 * @param indexId
	 * @return
	 */
	public List getCommentsByObjectId(String objectId, String objectType);

	/**
	 * 根据内容索引删除评论
	 * 
	 * @param indexId
	 */
	public void removeCommentByObjectId(String objectId, String objectType);

	/**
	 * 获得根贴对象
	 * 
	 * @param indexId
	 * @return
	 */
	public ICommentThread getCommentThread(String objectId, String objectType);

	/**
	 * 获得平铺的指定话题评论
	 * 
	 * @param indexId
	 *            内容索引id
	 * @param start
	 *            开始偏移
	 * @param len
	 *            每页长度
	 * @return
	 */
	public List<ICommentPost> getFlatComments(String objectId, String objectType,
			QueryInfo qi, PageBuilder pb);

	/**
	 * 根据CMS内容生成评论主帖
	 * 
	 * @param indexId
	 *            内容索引id
	 * @return
	 */
	public ICommentThread addCommentThread(Long indexId);
	
	public ICommentPost addCommentPost(ICommentThread thread, String objectId,
			String objectType, String author, String comment, String pid,
			String ip, String realIp,Integer hiddenIpStatus);
	/**
	 * 获得平铺的指定话题评论
	 * @param objectId 
	 * @param objectType
	 * @param offset 偏移	
	 * @param nums 数目
	 * @return
	 */
	public List<ICommentPost> getFlatComments(String objectId,String objectType,int offset,int nums);
}
