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
package org.openuap.cms.comment.engine.support;

import java.util.List;

import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.engine.CommentMacroEngine;
import org.openuap.cms.comment.manager.CommentManager;

/**
 * <p>
 * 缺省评论引擎实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultCommentMacroEngine.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author joseph
 * @version 1.0
 */
public class DefaultCommentMacroEngine implements CommentMacroEngine{
	
	private CommentManager commentManager;
	
	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

	public List<ICommentPost> getFlatComments(String objectId,
			String objectType, int offset, int nums) {
		return commentManager.getFlatComments(objectId, objectType, offset, nums);
	}

	public List<ICommentPost> getFlatComments(long objectId, String objectType,
			int offset, int nums) {
		return commentManager.getFlatComments(String.valueOf(objectId), objectType, offset, nums);
	}

}
