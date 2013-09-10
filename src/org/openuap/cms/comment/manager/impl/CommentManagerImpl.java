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
package org.openuap.cms.comment.manager.impl;

import java.util.List;
import java.util.Map;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.util.DynamicContentHelper;
import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.ICommentThread;
import org.openuap.cms.comment.dao.CommentDao;
import org.openuap.cms.comment.filter.CommentFilter;
import org.openuap.cms.comment.manager.CommentManager;
import org.openuap.cms.comment.model.CommentPost;
import org.openuap.cms.comment.model.CommentThread;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * MS评论管理服务实现.
 * </p>
 * 
 * <p>
 * $Id: CommentManagerImpl.java 3999 2011-01-06 15:58:59Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CommentManagerImpl implements CommentManager {

	private CommentDao commentDao;
	private DynamicContentManager dynamicContentManager;

	public DynamicContentManager getDynamicContentManager() {
		if (dynamicContentManager == null) {
			dynamicContentManager = (DynamicContentManager) ObjectLocator
					.lookup("dynamicContentManager", CmsPlugin.PLUGIN_ID);
		}
		return dynamicContentManager;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	/**
	 * 
	 */
	public CommentManagerImpl() {
	}

	public void setCommentDao(CommentDao dao) {
		this.commentDao = dao;
	}

	public Long addComment(ICommentPost commentPost) {
		return this.commentDao.addComment(commentPost);
	}

	public ICommentPost getCommentById(Long commentId) {
		return this.commentDao.getCommentById(commentId);
	}

	public ICommentThread getCommentThread(String objectId, String objectType) {
		return this.commentDao.getCommentThread(objectId, objectType);
	}

	public List<ICommentPost> getCommentsByObjectId(String objectId,
			String objectType) {
		return this.commentDao.getCommentsByObjectId(objectId, objectType);
	}

	public List<ICommentPost> getFlatComments(String objectId,
			String objectType, QueryInfo qi, PageBuilder pb) {
		return this.commentDao.getFlatComments(objectId, objectType, qi, pb);
	}

	public void removeComment(ICommentPost commentPost) {
		this.commentDao.removeComment(commentPost);

	}

	public void removeCommentById(Long commentId) {
		//完善：同时更新评论数目
		ICommentPost post = getCommentById(commentId);
		if (post != null) {
			this.commentDao.removeComment(post);
			getDynamicContentManager().updateContentIndex(
					new Long(post.getObjectId()), "commentNum", "commentNum-1",
					"commentNum<>0");
		}
	}

	public void removeCommentByObjectId(String objectId, String objectType) {
		this.commentDao.removeCommentByObjectId(objectId, objectType);

	}

	public void saveComment(ICommentPost commentPost) {
		this.commentDao.saveComment(commentPost);
	}

	public Long addCommentThread(ICommentThread commentThread) {
		return commentDao.addCommentThread(commentThread);
	}

	public ICommentThread addCommentThread(Long indexId) {

		ICommentThread commentThread = null;
		String objectType = "org.openuap.cms";
		// 获取主题帖
		commentThread = this.getCommentThread(indexId.toString(), objectType);

		if (commentThread == null) {
			ContentIndex ci = getDynamicContentManager().getContentIndexById(
					indexId);
			if (ci != null) {
				String url = ci.getUrl();
				Long nodeId = ci.getNodeId();
				String title = ci.getContentTitle();
				// 判断是否发布，若没有发布则应该从内容中获取

				// 获得发布对象
				Map publish = getDynamicContentManager().getDynamicPublish(
						ci.getIndexId());
				if (publish == null || publish.size() == 0) {
					publish = getDynamicContentManager().getDynamicContent(
							indexId, ci.getTableId());
				}
				long now = System.currentTimeMillis();
				//
				commentThread = new CommentThread();
				commentThread.setObjectId(indexId.toString());
				commentThread.setObjectType(objectType);
				commentThread.setCatalogId(nodeId);// nodeId
				commentThread.setTitle(title);
				// 文章内容
				String mainContent = DynamicContentHelper
						.getContentMainContent(publish, ci.getTableId());
				commentThread.setContent(mainContent);
				//
				commentThread.setUrl(url);
				commentThread.setLastPostName(ci.getCreationUserName());
				commentThread.setLastPostDate(now);
				commentThread.setCreationDate(now);
				commentThread.setReply(0);
				commentThread.setStatus(0);
				commentThread.setUserName(ci.getCreationUserName());
				Long tid = this.addCommentThread(commentThread);
				commentThread.setId(tid);
			}
		}
		return commentThread;
	}

	public ICommentPost addCommentPost(ICommentThread commentThread,
			String objectId, String objectType, String author, String comment,
			String pid, String ip, String realIp, Integer hiddenIpStatus) {
		if (commentThread == null) {
			commentThread = this.addCommentThread(new Long(objectId));
		}
		// FIX:status=0
		if (commentThread != null && commentThread.getStatus() == 0) {

			long now = System.currentTimeMillis();
			String oid = commentThread.getObjectId();
			String otype = commentThread.getObjectType();
			Long nid = commentThread.getCatalogId();
			CommentPost commentPost = new CommentPost();
			commentPost.setUserName(author);
			commentPost.setContent(comment);
			commentPost.setIp(ip);
			commentPost.setObjectType(otype);
			commentPost.setObjectId(oid);
			commentPost.setCatalogId(nid);
			commentPost.setRealIp(realIp);
			commentPost.setCreationDate(now);
			commentPost.setLastModifyDate(now);
			commentPost.setRootId(commentThread.getId());
			commentPost.setHiddenIpStatus(hiddenIpStatus);
			commentPost.setStatus(0);
			if (pid != null && !pid.equalsIgnoreCase("")) {
				Long parentId = new Long(pid);
				commentPost.setParentId(parentId);
			} else {
				commentPost.setParentId(0L);
			}
			// 调用存储过滤器过滤非法字符
			CommentFilter.doStoreFilter(commentPost);
			Long postId = this.addComment(commentPost);
			commentPost.setId(postId);
			// 更新内容索引评论数目
			getDynamicContentManager().updateContentIndex(new Long(oid),
					"commentNum", "commentNum+1");
			//
			return commentPost;
		}
		return null;
	}

	public List<ICommentPost> getFlatComments(String objectId,
			String objectType, int offset, int nums) {
		return commentDao.getFlatComments(objectId, objectType, offset, nums);
	}
}
