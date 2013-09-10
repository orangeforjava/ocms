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
package org.openuap.cms.comment.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.manager.CommentManager;
import org.openuap.cms.user.security.IUserSession;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 评论编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: CommentEditAction.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class CommentEditAction extends CommentBaseAction {

	private CommentManager commentManager;

	private String rsViewName;
	private String defaultScreensPath;

	public CommentEditAction() {

		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/comment/screens/";
		
		rsViewName = defaultScreensPath + "comment_op_result.html";
	}

	/**
	 * 显示评论内容
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String id = request.getParameter("id");

		return null;
	}

	/**
	 * 添加评论
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doAdd(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//支持通过参数传递模板
		String viewName = helper.getString("tpl", rsViewName);
		ModelAndView mv=new ModelAndView(viewName);
		//
		model.put("op", "add");
		//
		try {
			String objectId = request.getParameter("id");
			String author = request.getParameter("author");
			String comment = request.getParameter("comment");
			// 对象类型，缺省是来自cms
			String objectType = helper.getString("type", "org.openuap.cms");
			// 父帖子id
			String pid = helper.getString("pid", "0");
			int hip = helper.getInt("hip", 0);
			String ip = helper.getRemoteAddr();
			String realIp = helper.getRealIP();
			//
			model.put("id", objectId);
			//
			IUserSession userSession = this.getUserSession();
			//
			if (userSession.isAnonymous()) {
				// 匿名用户
			}
			//
			if (objectId != null) {
				// 添加评论
				ICommentPost commentPost = commentManager
						.addCommentPost(null, objectId, objectType, author,
								comment, pid, ip, realIp, hip);
				if (commentPost != null) {
					// 成功
					model.put("rs", "success");
				} else {
					// 失败
					model.put("rs", "success");
				}

			}
		} catch (Exception e) {
			model.put("rs", "failed");
			model.put("msgs", e.getMessage());
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 删除评论内容
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doDelete(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//支持通过参数传递模板
		String viewName = helper.getString("tpl", rsViewName);
		ModelAndView mv=new ModelAndView(viewName);
		//
		model.put("op", "delete");
		if(this.getUserSession()!=null&&(this.getUserSession().isSysUser()||this.getUserSession().isAdmin())){
			try {
				String id = helper.getString("id");
				Long cid = helper.getLong("cid");
				commentManager.removeCommentById(cid);
				model.put("id", id);
				model.put("rs", "success");
			} catch (Exception e) {
				model.put("rs", "failed");
				model.put("msgs", e.getMessage());
				//e.printStackTrace();
			}
		}else{
			model.put("rs", "failed");
			model.put("msgs","您没有权限执行此操作！");
		}
		
		return mv;
	}
	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

}
