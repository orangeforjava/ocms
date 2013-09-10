/*
 * Copyright 2005-2008 the original author or authors.
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
package org.openuap.cms.publish.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.util.PageInfo;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 发布内容选择控制器.
 * </p>
 * 
 * <p>
 * $Id: ContentSelectAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentSelectAction extends AdminAction {
	//
	private NodeManager nodeManager;

	private ContentFieldManager contentFieldManager;

	private DynamicContentManager dynamicContentManager;

	//
	private String defaultViewName;

	private String defaultScreensPath;

	private String headerViewName;

	private String mainViewName;

	/**
	 * 
	 */
	public ContentSelectAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/publish/select/";
		defaultViewName = defaultScreensPath
				+ "publish_content_link_frameset.html";
		headerViewName = defaultScreensPath
				+ "publish_content_link_header.html";
		mainViewName = defaultScreensPath + "publish_content_link.html";
	}

	/**
	 * show the content link frameset.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String fieldName = request.getParameter("fieldName");
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		model.put("fieldName", fieldName);
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		return mv;
	}

	/**
	 * show the content link header.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return ModelAndView
	 */
	public ModelAndView doEditContentLinkHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String fieldName = request.getParameter("fieldName");
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		//
		ModelAndView mv = new ModelAndView(headerViewName, model);
		model.put("fieldName", fieldName);
		model.put("nodeId", new Integer(nodeId));
		model.put("indexId", indexId);
		model.put("nodeManager", nodeManager);
		return mv;
	}

	public ModelAndView doEditContentLink(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		String column_condition = "";
		String fieldName = request.getParameter("fieldName");
		String indexId = request.getParameter("indexId");
		//
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		String order_mode = request.getParameter("order_mode");
		String order_name = request.getParameter("order_name");
		String where = helper.getString("where", "");
		String pubDate = request.getParameter("pubDate");
		String pubDate2 = request.getParameter("pubDate2");
		String nodeId = request.getParameter("nodeId");
		//
		String orderBy = "";
		Long start = new Long(0);
		Long limit = new Long(15);
		Object[] args = null;
		//
		// state = "1";
		where = " ci.state=1 ";
		// argList.add(new Integer(state));
		System.out.println("pageNum="+pageNum);
		if (pageNum != null) {
			limit = new Long(pageNum);
		} else {
			pageNum = "15";
		}
		if (page != null) {
			start = new Long((Integer.parseInt(page) - 1) * limit.intValue());
		} else {
			page = "1";
		}
		String tmp = request.getParameter("keyword");
		if (tmp == null) {
			tmp = "";
		}
		//
		String keyword = tmp.trim();
		//
		String fields = helper.getString("fields", "");
		//

		//
		if (where == null) {
			where = "";
		}
		if (order == null) {
			order = "";
		}
		if (order_mode == null) {
			order_mode = "";
		}
		if (order_name == null) {
			order_name = "";
		}
		order_name = order_name.replaceAll("\\^", "");
		//
		String final_order = "";
		if (!order.equals("") && !order_mode.equals("")) {
			final_order = order + " " + order_mode;
		}
		if (keyword != null && !keyword.equals("")) {
			if (fields != null && !fields.equals("")) {
				String columns[] = fields.split(",");
				if (columns != null) {
					for (int i = 0; i < columns.length; i++) {
						column_condition += " or ci." + columns[i] + " like '%"
								+ keyword + "%'";

					}
					if (!column_condition.equals("")) {
						column_condition = column_condition.substring(4);
						column_condition = " and (" + column_condition + ")";
					}
				}
			} else {
				// 获取内容属性信息
				column_condition += " or ci.contentTitle like '%" + keyword
						+ "%'";

				if (!column_condition.equals("")) {
					column_condition = column_condition.substring(4);
					column_condition = " and (" + column_condition + ")";
				}

			}
		}

		//
		where += column_condition;
		//
		// pubDate
		if (pubDate != null && !pubDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(pubDate);
			where += " and ci.publishDate>=" + dd.getTime() + "";
		} else {
			pubDate = "";
		}
		// pubDate2
		if (pubDate2 != null && !pubDate2.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(pubDate2);
			where += " and ci.publishDate<=" + dd.getTime() + "";
		} else {
			pubDate2 = "";
		}
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(mainViewName, model);

			Long id = new Long(nodeId);
			Node node = nodeManager.getNode(id);

			//
			Long tableId = node.getTableId();
			ContentField cf = contentFieldManager.getTitleField(tableId);
			//
			ContentField photoField = contentFieldManager
					.getPhotoFieldFromCache(tableId);
			if (cf != null) {
				String titleFieldName = cf.getFieldName();
				PageInfo pageInfo = new PageInfo();
				pageInfo.itemsPerPage(limit.intValue());
				pageInfo.page(Integer.parseInt(page));
				// 获取动态内容列表
				List contents = dynamicContentManager.getQuickContentList(id,
						tableId, where, orderBy, args, start, limit, pageInfo);
				// @todo maybe only PageBuilder
				PageBuilder pb = new PageBuilder(limit.intValue());
				pb.items((int) pageInfo.getTotalNum());
				pb.page(Integer.parseInt(page));
				//
				model.put("contents", contents);
				model.put("titleFieldName", titleFieldName);
				model.put("pb", pb);
			}
			if (photoField != null) {
				model.put("photoFieldName", photoField.getFieldName());
			}
			model.put("fieldName", fieldName);
			model.put("nodeId", nodeId);
			model.put("indexId", indexId);
			//
			model.put("page", page);
			model.put("pageNum", pageNum);
			model.put("order", order);
			model.put("order_mode", order_mode);
			model.put("order_name", order_name);
			model.put("where", where);
			model.put("keyword", keyword);
			//
			model.put("pubDate", pubDate);
			model.put("pubDate2", pubDate2);
			return mv;
		}
		return null;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setHeaderViewName(String headerViewName) {
		this.headerViewName = headerViewName;
	}

	public void setMainViewName(String mainViewName) {
		this.mainViewName = mainViewName;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}
}
