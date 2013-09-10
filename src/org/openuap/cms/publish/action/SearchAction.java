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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.DateUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.base.util.resource.DirectoryListDataLoader;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.cache.NodeCache;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.util.PageInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 内容搜索控制器
 * </p>
 * 
 * <p>
 * $Id: SearchAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 * @deprecated
 */
public class SearchAction extends AdminAction {

	//
	private NodeManager nodeManager;

	private ContentFieldManager contentFieldManager;

	private DynamicContentManager dynamicContentManager;

	//
	private String defaultViewName;

	private String defaultScreensPath;

	private String searchResultViewName;

	/**
	 * 
	 */
	public SearchAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/publish/";
		defaultViewName = defaultScreensPath + "publish_search.html";
		searchResultViewName = defaultScreensPath
				+ "publish_search_result.html";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		ModelAndView mv = super.beforePerform(request, response, helper, model);
		if (mv != null) {
			return mv;
		}
		List<IUser> authors = this.getUserManager().getUsers(
				IUser.SYS_USER_TYPE | IUser.ADMIN_TYPE, IUser.NORMAL_STATUS);
		model.put("layout", "/plugin/cms/base/layouts/admin.html");
		model.put("dynamicContentManager", dynamicContentManager);
		model.put("authors", authors);
		String currentUserName = this.getUser().getName();
		model.put("currentUserName", currentUserName);
		model.put("nodeManager", nodeManager);
		//
		List dateNears=getDateNearConstants();
		model.put("dateNears", dateNears);
		return null;
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		// String tableId = request.getParameter("tableId");
		String nodeId = request.getParameter("nodeId");
		if (nodeId != null) {
			// Long tid = new Long(tableId);
			Long nid = new Long(nodeId);
			model.put("nodeId", nid);
		}
		// if (tableId != null) {
		ModelAndView mv = new ModelAndView(defaultViewName, model);

		// List contentFields =
		// contentFieldManager.getContentFieldsFromCache(tid);
		// List containNodeIds = new ArrayList();
		// List nodeIds = new ArrayList();
		// nodeManager.getContainTableIdNodes(new Long(0), tid, nodeIds,
		// containNodeIds);
		// model.put("contentFields", contentFields);
		// model.put("tableId", tableId);

		// model.put("nodeIds", nodeIds);
		// model.put("containNodeIds", containNodeIds);

		return mv;
		// }
		// return null;
	}

	/**
	 * 执行综合查询
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doSearch(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		ModelAndView mv = new ModelAndView(defaultViewName, model);

		String nodeId = helper.getString("nodeId", "");

		// 结点ID必须合法
		if (!StringUtil.isNumber(nodeId)) {
			return this.errorPage(request, response, helper, "node_must_set",
					model);
		}

		Long id = new Long(nodeId);
		Node node = nodeManager.getNode(id);
		if (node != null) {
			//
			Long tid = node.getTableId();
			// 1)结点包含过滤
			String ids = nodeId;
			int sub = helper.getInt("sub", 1);
			if (sub == 1) {
				List<Long> childIds =new ArrayList<Long>();
				NodeCache.getAllChildNodeIds(id,childIds);

				if (childIds.size() > 0) {
					for (Long cid : childIds) {
						ids += "," + cid;
					}
				}
			}
			model.put("node", node);
			model.put("nodeId", id);
			// 2)发布状态过滤
			String where = "";
			List argList = new ArrayList();
			Object[] args = null;

			String state = helper.getString("state", "-1");
			if (state == null || state.equals("-1")) {
				where = " ci.state<>-1 ";
			} else {
				where = " ci.state=? ";
				argList.add(new Integer(state));
			}
			model.put("state", state);
			// 分页
			String page = request.getParameter("page");
			String pageNum = request.getParameter("pageNum");
			if (page == null) {
				page = "1";
			}
			if (pageNum == null) {
				pageNum = "15";
			}

			Long ipage = new Long(page);
			Long limit = new Long(pageNum);
			Long start = new Long((ipage.intValue() - 1) * limit.intValue());
			if (pageNum != null) {
				limit = new Long(pageNum);
			} else {
				pageNum = "15";
			}
			if (page != null) {
				start = new Long((Integer.parseInt(page) - 1)
						* limit.intValue());
			} else {
				page = "1";
			}

			if (argList.size() > 0) {
				args = argList.toArray();
			}
			//
			String column_condition = "";
			//3)关键字过滤
			String fields = helper.getString("fields", "all");
			String tmp = request.getParameter("keyword");
			if (tmp == null) {
				tmp = "";
			}
			//
			String keyword = tmp.trim();
			ContentField titleField = contentFieldManager
					.getTitleFieldFromCache(tid);
			if (keyword != null && !keyword.equals("")) {
				if (!fields.equals("all")) {
					// 只检索标题

					column_condition += " and ci.contentTitle like '%"
							+ keyword + "%'";
				} else {
					// 使用所有可检索域
					List<ContentField> contentFields = contentFieldManager
							.getSearchFieldsFromCache(tid);
					if (contentFields != null) {
						for (ContentField field : contentFields) {
							column_condition += " or c." + field.getFieldName()
									+ " like '%" + keyword + "%'";
						}
					}
					if (!column_condition.equals("")) {
						column_condition = column_condition.substring(4);
						column_condition = " and (" + column_condition + ")";
					}

				}
			}
			model.put("keyword", keyword);
			model.put("fields", fields);
			where += column_condition;
			// 3)日期过滤
			String dateType = helper.getString("dateType", "dateNear");
			String pubDate = request.getParameter("pubDate");
			String pubDate2 = request.getParameter("pubDate2");
			if (dateType.equals("dateNear")) {
				// 最近日期
				long dateNum = helper.getInt("dateNum", 0);

				if (dateNum != 0) {
					model.put("dateNum", String.valueOf(dateNum));
					long dateStart = DateUtil.now().getTime() - dateNum * 24*3600
							* 1000;
					where += " and ci.publishDate>=" + dateStart + "";
				}

			} else {
				// 精确时间段
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
			}

			model.put("dateType", dateType);
			model.put("pubDate", pubDate);
			model.put("pubDate2", pubDate2);
			// 4)作者过滤
			String author = helper.getString("author", "");
			String[] authors = author.split(",");
			String author_condition = "";
			if (StringUtil.hasText(author)) {
				for (String auth : authors) {
					author_condition += " or  ci.creationUserName='" + auth
							+ "'";
				}
			}
			if (!author_condition.equals("")) {
				author_condition = author_condition.substring(4);
				author_condition = " and (" + author_condition + ")";
			}
			model.put("author", author);
			where += author_condition;
			// 5)排序模式
			String order = helper.getString("order", "");
			String order_mode = helper.getString("order_mode", "");
			String order_name = helper.getString("order_name", "");

			order_name = order_name.replaceAll("\\^", "");
			String final_order = "";
			if (!order.equals("") && !order_mode.equals("")) {
				final_order = order + " " + order_mode;
			}
			String orderBy = "";
			if (order != null) {
				orderBy = final_order;
			}
			//
			PageInfo pageInfo = new PageInfo();
			pageInfo.itemsPerPage(limit.intValue());
			pageInfo.page(Integer.parseInt(page));
			// 获取动态内容列表
			//System.out.println("where=" + where);
			List contents = null;
			if (StringUtil.hasText(keyword)) {
				contents = dynamicContentManager.getContentList(ids, tid,
						where, orderBy, args, start, limit, pageInfo);
			} else {
				contents = dynamicContentManager.getQuickContentList(ids, tid,
						where, orderBy, args, start, limit, pageInfo);
			}
			//
			PageBuilder pb = new PageBuilder(limit.intValue());
			pb.items((int) pageInfo.getTotalNum());
			pb.page(Integer.parseInt(page));
			model.put("contents", contents);
			model.put("titleFieldName", titleField.getFieldName());
			model.put("pb", pb);
			model.put("page", page);
			model.put("pageNum", pageNum);
			model.put("order", order);
			model.put("order_mode", order_mode);
			model.put("order_name", order_name);
			model.put("where", where);
			model.put("ac", "search");
			

			return mv;
		} else {
			return this.errorPage(request, response, helper, "node_not_exist",
					model);
		}
	}

	public ModelAndView doDateSearch(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		ModelAndView mv = new ModelAndView(searchResultViewName, model);
		String Keywords = request.getParameter("Keywords");
		String[] Fields = request.getParameterValues("Fields");
		String Published = request.getParameter("Published");
		// String[] NodeIDs = request.getParameterValues("NodeIDs");
		// String Sub = request.getParameter("Sub");
		String Time = request.getParameter("Time");
		String tableId = request.getParameter("tableId");
		String nodeId = request.getParameter("nodeId");
		String date = request.getParameter("date");
		//
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		if (page == null) {
			page = "1";
		}
		if (pageNum == null) {
			pageNum = "15";
		}
		Long ipage = new Long(page);
		Long limit = new Long(pageNum);
		Long start = new Long((ipage.intValue() - 1) * limit.intValue());
		//

		Long tid = new Long(tableId);
		String[] snids = new String[] { nodeId };
		//
		String nodeIds = StringUtil.arr2str(snids);
		//
		String where = "";
		//
		if (date != null) {
			int date_start = DateUtil.getDateStartSeconds(date);
			int date_end = DateUtil.getDateEndSeconds(date);
			where = " (ci.publishDate>=" + date_start + " and ci.publishDate<"
					+ date_end + ")";
		}
		//
		PageInfo pageInfo = new PageInfo();
		List contents = dynamicContentManager.searchContentList(Keywords,
				Fields, Published, snids, Time, tid, where, "", null, start,
				limit, pageInfo);
		ContentField cf = contentFieldManager.getTitleField(tid);
		model.put("contents", contents);
		model.put("titleFieldName", cf.getFieldName());
		model.put("pageInfo", pageInfo);
		model.put("action", this);
		model.put("tableId", tableId);
		model.put("nodeIds", nodeIds);
		return mv;

	}

	public ModelAndView doKeyWordSearch(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(searchResultViewName, model);
		String Keywords = helper.getString("Keywords");
		String[] Fields = request.getParameterValues("Fields");
		String Published = request.getParameter("Published");
		String[] NodeIDs = request.getParameterValues("NodeIDs");
		String Sub = request.getParameter("Sub");
		String Time = request.getParameter("Time");
		String tableId = request.getParameter("tableId");
		String nodeId = request.getParameter("nodeId");
		String date = request.getParameter("date");
		//
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		if (page == null) {
			page = "1";
		}
		if (pageNum == null) {
			pageNum = "15";
		}
		Long ipage = new Long(page);
		Long limit = new Long(pageNum);
		Long start = new Long((ipage.intValue() - 1) * limit.intValue());
		//

		Long tid = new Long(tableId);
		String[] snids = new String[] { nodeId };
		//
		// if keywords is not empty,and fields not select,it will all field
		// search.
		if (StringUtils.hasText(Keywords) && Fields == null) {
			List contentFields = contentFieldManager.getContentFieldOfTable(
					tid, "cf.fieldOrder");

			if (contentFields != null) {
				Fields = new String[contentFields.size()];
				for (int i = 0; i < contentFields.size(); i++) {
					Fields[i] = ((ContentField) contentFields.get(i))
							.getFieldName();
				}
			}
		}
		//
		String where = "";
		//
		//
		//
		PageInfo pageInfo = new PageInfo();
		List contents = dynamicContentManager.searchContentList(Keywords,
				Fields, Published, snids, Time, tid, where, "", null, start,
				limit, pageInfo);
		ContentField cf = contentFieldManager.getTitleField(tid);
		model.put("contents", contents);
		model.put("titleFieldName", cf.getFieldName());
		model.put("pageInfo", pageInfo);
		model.put("action", this);
		return mv;

	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setSearchResultViewName(String searchResultViewName) {
		this.searchResultViewName = searchResultViewName;
	}

	// ////////////////////////////////////////////////////////////////////////////
	private void getNodeIdList(String[] sids, List containNodeIds,
			List nodeIds, List result, Long tableId, boolean sub) {
		for (int i = 0; i < sids.length; i++) {
			String sid = sids[i];
			Long id = new Long(sid);
			if (nodeIds.contains(id)) {
				if (!result.contains(id.toString())) {
					result.add(id.toString());
				}

			} else {
				if (containNodeIds.contains(id) && sub) {
					List myNodeIds = new ArrayList();
					List myContainNodeIds = new ArrayList();
					nodeManager.getContainTableIdNodes(id, tableId, myNodeIds,
							containNodeIds);
					for (int j = 0; j < myNodeIds.size(); j++) {
						Integer myid = (Integer) myNodeIds.get(j);
						if (!result.contains(myid.toString())) {
							result.add(myid.toString());
						}
					}
				}
			}
		}
	}
	public List getDateNearConstants() {
		List dd = DirectoryListDataLoader
				.load("/plugin/cms/datenear_constant.xml");
		return dd;
	} 
}
