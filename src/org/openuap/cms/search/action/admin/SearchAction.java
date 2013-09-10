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
package org.openuap.cms.search.action.admin;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.search.SearchCommand;
import org.openuap.cms.search.SearchEngine;
import org.openuap.cms.search.SearchResults;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 后台全文检索控制器. <br/>
 * 仅供后台管理内容使用，不提供给前台调用.
 * </p>
 * <p>
 * $Id: SearchAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class SearchAction extends AdminAction {

	private String defaultViewName;

	private String defaultScreensPath;
	/** 内容模型管理. */
	private ContentTableManager contentTableManager;
	/** 搜索引擎. */
	private SearchEngine searchEngine;
	/** */
	private NodeManager nodeManager;

	public SearchAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/search/";
		defaultViewName = defaultScreensPath + "fulltext_search.html";
	}

	@SuppressWarnings("unchecked")
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String tid = helper.getString("tid", "");
		String nodeId = helper.getString("nodeId", "");
		ModelAndView mv = new ModelAndView(defaultViewName);
		if (StringUtil.hasText(tid)) {
			ContentTable ct = contentTableManager
					.getContentTableFromCache(new Long(tid));
			model.put("nodeManager", nodeManager);
			
			if (ct != null) {
				model.put("ct", ct);
				model.put("tid", tid);
				if (StringUtil.hasText(nodeId)) {
					model.put("nodeId", new Long(nodeId));
				} else {
					model.put("nodeId", -1);
				}
				return mv;
			} else {
				this.errorPage(request, response, helper,
						"contentTable_not_exists", model);
			}
		} else {
			this.errorPage(request, response, helper, "tid_not_exists", model);
		}
		return null;
	}

	/**
	 * 执行检索
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doSearch(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(defaultViewName);
		try {
			// 模型Id
			String tid = helper.getString("tid", "");
			ContentTable ct = contentTableManager
					.getContentTableFromCache(new Long(tid));
			model.put("nodeManager", nodeManager);
			model.put("ct", ct);
			String encoding = helper.getString("encoding", "UTF-8");
			// 返回页码
			String page = helper.getString("page", "1");
			// 每页记录数目
			String pageNum = helper.getString("pageNum", "10");
			// 关键字
			String tmp = request.getParameter("keyword");
			if (tmp == null) {
				tmp = "";
			} else {
				tmp = URLDecoder.decode(tmp, encoding);
			}
			// System.out.println("tmp="+tmp);
			//
			String keyword = tmp; //
			//
			// 传输的附加参数
			String tmp2 = request.getParameter("extra");
			if (tmp2 == null) {
				tmp2 = "";
			}
			tmp2 = new String(tmp2.getBytes(), encoding);
			String extra = tmp2;
			if (extra == null) {
				extra = "";
			}
			Map extraHash = new HashMap();
			String[] extras = extra.split(",");
			for (int i = 0; i < extras.length; i++) {
				String extra_tmp = extras[i];
				String[] extra_tmps = extra_tmp.split(":");
				if (extra_tmps.length == 2) {
					extraHash.put(extra_tmps[0], extra_tmps[1]);
				}
			}
			// 搜索域
			String fields = helper.getString("fields", "");
			// 结点id
			String nodeId = helper.getString("nodeId", "");
			// 结点GUID
			String nodeGUID = helper.getString("nodeGUID", "");

			// 忽略结点
			String ignore = helper.getString("ignore", "");
			// 排序
			String order = helper.getString("order", "");
			// 条件
			tmp = request.getParameter("where");
			// 高亮
			String highlights = helper.getString("highlights", "");
			if (tmp == null) {
				tmp = "";
			} else {
				tmp = URLDecoder.decode(tmp, encoding);
			}
			// 条件中过滤@(=)以及~(')
			String where = tmp; //
			String where2 = where.replaceAll("@", "=");
			where2 = where2.replaceAll("~", "'");
			//
			int ipage = Integer.parseInt(page);
			int ipageNum = Integer.parseInt(pageNum);
			//
			SearchCommand command = new SearchCommand(keyword, fields, nodeId,
					nodeGUID, tid, ignore, order, where2, highlights, ipage,
					pageNum, "");
			SearchResults sr = searchEngine.doSearch(command);
			model.put("ac", "search");
			model.put("rs", sr.getHits());
			model.put("pb", sr.getPageBuilder());
			model.put("searchTime", sr.getSearchTime());
			//
			model.put("keyword", URLDecoder.decode(keyword, "UTF-8"));
			model.put("keyword2", keyword);
			model.put("page", page);
			model.put("pageNum", pageNum);
			model.put("encoding", encoding);
			model.put("fields", fields);
			if (StringUtil.hasText(nodeId)) {
				model.put("nodeId", new Long(nodeId));
			} else {
				model.put("nodeId", -1);
			}
			model.put("nodeGUID", nodeGUID);
			model.put("tid", tid);
			model.put("ignore", ignore);
			model.put("order", order);
			model.put("where", where);
			model.put("extra", extra);
			model.put("extraHash", extraHash);
			model.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("result", "failed");
			model.put("msg", e.getMessage());
		}
		return mv;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

}
