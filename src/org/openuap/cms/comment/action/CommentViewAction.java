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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.ICommentThread;
import org.openuap.cms.comment.manager.CommentManager;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.runtime.util.ObjectLocator;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateEngine;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 评论显示控制器
 * </p>
 * 
 * <p>
 * $Id: CommentViewAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class CommentViewAction extends CommentBaseAction {

	private CommentManager commentManager;
	private TemplateEngine templateEngine;

	private String defaultViewName;

	private String defaultScreensPath;
	private String rsViewName;

	public CommentViewAction() {

		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/comment/screens/";
		defaultViewName = defaultScreensPath + "comment_view.html";
		rsViewName = defaultScreensPath + "comment_op_result.html";
	}

	/**
	 * 显示评论内容 调用引擎渲染内容
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		// 增加使用参数传递过来的模板
		String viewName = helper.getString("tpl", defaultViewName);
		ModelAndView mv = new ModelAndView(viewName);
		// 新闻id
		String id = request.getParameter("id");
		int p = helper.getInt("p", 1);
		int pp = helper.getInt("pp", 20);
		String flag = helper.getString("flag", "");
		String order = helper.getString("order", "");
		//
		int offset = (p - 1) * pp;
		String objectType = "org.openuap.cms";
		QueryInfo qi = new QueryInfo("", order, pp, offset);
		PageBuilder pb = new PageBuilder(pp);
		
		// 决定是否发布主题帖
		ICommentThread thread = commentManager.addCommentThread(new Long(id));
		//
		List<ICommentPost> posts = commentManager.getFlatComments(id,
				objectType, qi, pb);
		pb.page(p);
		// 放入用户Session到模板中
		model.put("userSession", this.getUserSession());
		model.put("thread", thread);
		model.put("posts", posts);
		//
		model.put("p", p);
		model.put("pp", pp);
		model.put("pb", pb);
		model.put("order", order);
		// 采用模板引擎处理输出
		try {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				//
				PublishProfileInfoHolder.getProfile().setStartActionTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.getProfile().setActionName("comment");
			}
			TemplateContext context = new TemplateContext();
			// System.out.println("tpl="+tpl);
			context.setTplName(viewName);
			Map finalmodel = Collections.synchronizedMap(new HashMap());
			//
			finalmodel.put("__direct_out__", "yes");
			finalmodel.putAll(model);
			context.setModel(finalmodel);
			List errors = new ArrayList();
			getTemplateEngine().renderTemplate(context, errors);
			String content = context.getTplContent();
			response.setContentType("text/html;charset=UTF-8");
			//
			PrintWriter pw = response.getWriter();
			pw.print(content);
			pw.flush();
			pw.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				PublishProfileInfoHolder.getProfile().setStartRenderTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.logProfile();
			}
		}
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doJson(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String callback=request.getParameter("jsoncallback");
		model.put("jsoncallback", callback);
		return null;
	}
	public CommentManager getCommentManager() {
		return commentManager;
	}

	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

	public TemplateEngine getTemplateEngine() {
		if (templateEngine == null) {
			templateEngine = (TemplateEngine) ObjectLocator.lookup(
					"templateEngine", "org.openuap.tpl.engine");
		}
		return templateEngine;
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}
}
