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
package org.openuap.cms.comment.service.impl;

import java.util.List;

import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.manager.CommentManager;
import org.openuap.cms.comment.model.CommentPost;
import org.openuap.cms.comment.service.CommentService;

/**
 * <p>
 * 基本的评论功能实现.
 * </p>
 * 
 * <p>
 * $Id: CommentServiceImpl.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CommentServiceImpl implements CommentService {
	/** */
	private CommentManager commentManager;

	/**
	 * 
	 */
	public CommentServiceImpl() {
	}

	public Long addComment(ICommentPost iCommentPost) {
		CommentPost ccomment = new CommentPost();
		// ccomment.
		return null;
	}

	public void updateComment(ICommentPost iCommentPost) {
	}

	public void deleteComment(ICommentPost iCommentPost) {
	}

	public void deleteCommentById(long id) {
	}

	public ICommentPost getCommentById(long id) {
		return null;
	}

	public List getFlatComments(long indexId, String order) {
		return null;
	}

	public List getThreadComments(long indexId) {
		return null;
	}

	public int getCommentsCount(long indexId) {
		return 0;
	}

	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

	public Long addComment(Long indexId, String author, String ip,
			String comment) {
		return null;
	}

	public List getCommentsByIndex(Long indexId) {
		return null;
	}
}
