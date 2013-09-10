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
package org.openuap.cms.comment.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.ICommentThread;
import org.openuap.cms.comment.dao.CommentDao;
import org.openuap.cms.comment.model.CommentPost;
import org.openuap.cms.comment.model.CommentThread;

/**
 * <p>
 * CMS评论DAO Hibernate实现.
 * </p>
 * 
 * <p>
 * $Id: CommentDaoImpl.java 3999 2011-01-06 15:58:59Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class CommentDaoImpl extends BaseDaoHibernate implements CommentDao {

	public CommentDaoImpl() {
	}

	public ICommentPost getCommentById(Long commentId) {
		return (CommentPost) this.findUniqueResult(
				"from CommentPost where id=?", new Object[] { commentId });
	}

	public void saveComment(ICommentPost comment) {
		this.saveObject(comment);
	}

	public Long addComment(ICommentPost comment) {
		return (Long) this.addObject(comment);
	}

	public void removeComment(ICommentPost comment) {
		this.deleteObject(comment);
	}
	
	public void removeCommentById(Long commentId) {
		this.executeUpdate("delete from CommentPost where id=?",
				new Object[] { commentId });
	}

	public List<ICommentPost> getCommentsByObjectId(String objectId,
			String objectType) {
		return this.executeFind(
				"from CommentPost where objectId=? and objectType=? ",
				new Object[] { objectId, objectType });
	}

	public void removeCommentByObjectId(String objectId, String objectType) {
		this.executeUpdate(
				"delete from CommentPost where objectId=? and objectType=? ",
				new Object[] { objectId, objectType });
	}

	public ICommentThread getCommentThread(String objectId, String objectType) {
		String hql = "from CommentThread where objectId='" + objectId + "'"
				+ " and objectType='" + objectType + "'";
		return (CommentThread) this.findUniqueResult(hql);
	}

	/**
	 * FIX:add status=0 condition
	 */
	public List<ICommentPost> getFlatComments(String objectId,
			String objectType, QueryInfo qi, PageBuilder pb) {
		String hql = "from CommentPost where objectId='" + objectId
				+ "' and objectType='" + objectType + "' and status=0"
				+ " order by creationDate desc";
		String hql_count = "select count(*) from CommentPost where objectId='"
				+ objectId + "' and objectType='" + objectType
				+ "' and status=0";
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public Long addCommentThread(ICommentThread commentThread) {
		return (Long) this.addObject(commentThread);
	}
	
	public List<ICommentPost> getFlatComments(String objectId,
			String objectType, int offset, int nums) {
		String hql = "from CommentPost where objectId='" + objectId
				+ "' and objectType='" + objectType + "' and status=0 "
				+ " order by creationDate desc";
		return this.executeFind(hql, new QueryInfo(null,null,nums,offset));
	}

}
