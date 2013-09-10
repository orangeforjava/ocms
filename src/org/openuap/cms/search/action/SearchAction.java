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
package org.openuap.cms.search.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.web.mvc.BaseController;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.cms.search.SearchCommand;
import org.openuap.cms.search.SearchEngine;
import org.openuap.cms.search.SearchResults;
import org.openuap.runtime.util.ObjectLocator;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 通用内容搜索控制器，可以选择基于数据库搜索或者搜索引擎.
 * </p>
 * 
 * <p>
 * $Id: SearchAction.java 4012 2011-01-24 11:05:06Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class SearchAction extends BaseController implements InitializingBean {

	private String jsonViewName;

	private String defaultScreensPath;
	/** 搜索引擎. */
	private SearchEngine searchEngine;
	/** 模板引擎. */
	private TemplateEngine templateEngine;

	/**
	 * 
	 */
	public SearchAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/search/";
		jsonViewName = defaultScreensPath + "search_json.htm";
	}

	/**
	 * 执行搜索
	 */
	public ModelAndView doSearchJson(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(jsonViewName);
		// 附加参数编码
		String encoding = helper.getString("encoding", "UTF-8");
		model.put("responseType", "text/javaScript");
		model.put("noLayout", true);
		// 模板参数
		try {
			// 返回页码
			String page = helper.getString("page", "1");
			// 每页记录数目
			String pageNum = helper.getString("pageNum", "10");
			//默认对这种方式的查询请求为false
			boolean parseKeyword=helper.getBoolean("parseKeyword",false);
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
			// 模型Id
			String sTableId = helper.getString("tableId", "");
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
					nodeGUID, sTableId, ignore, order, where2, highlights,
					ipage, pageNum, "");
			command.setParseKeyword(parseKeyword);
			//
			SearchResults sr = searchEngine.doSearch(command);
			//
			String callback=request.getParameter("jsoncallback");
			model.put("jsoncallback", callback);
			// 搜索结果
			model.put("rs", sr.getHits());
			model.put("pb", sr.getPageBuilder());
			model.put("searchTime", sr.getSearchTime());
			//
			model.put("keyword", URLEncoder.encode(keyword, "UTF-8"));
			model.put("keyword2", keyword);
			model.put("page", page);
			model.put("pageNum", pageNum);
			model.put("encoding", encoding);
			model.put("fields", fields);
			model.put("nodeId", nodeId);
			model.put("nodeGUID", nodeGUID);
			model.put("tableId", sTableId);
			model.put("ignore", ignore);
			model.put("order", order);
			model.put("where", where);
			model.put("extra", extra);
			model.put("extraHash", extraHash);
			//
			model.put("result", "success");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			model.put("result", "failed");
			model.put("msg",e.getMessage());
			return mv;
		} 
	}
	/**
	 * 支持灵活订制的全文检索
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		// 判断URL编码
		String uriEncoding = CMSConfig.getInstance().getStringProperty(
				"cms.uriEncoding", "UTF-8");
		// 附加参数编码
		String encoding = helper.getString("encoding", "UTF-8");
		// 结果模板编码
		String tplEncoding = helper.getString("tpl.encoding", "UTF-8");
		// 设定模板编码
		request.setAttribute("tpl.encoding", tplEncoding);
		// TODO 输出编码控制
		model.put("responseType", "text/html;charset=" + tplEncoding);
		model.put("serverParse", "true");
		// 模板参数
		String tpl = helper.getString("tpl");
		if (tpl != null) {
			// ModelAndView mv = new ModelAndView(tpl, model);
			//
			String url = "";
			//
			// 返回页码
			String page = helper.getString("page", "1");
			// 每页记录数目
			String pageNum = helper.getString("pageNum", "10");
			// 关键字
			String tmp = request.getParameter("keyword");
			if (tmp == null) {
				tmp = "";
			} else {
				tmp = URLDecoder.decode(tmp, uriEncoding);
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
			// 模型Id
			String sTableId = helper.getString("tableId", "");
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
				tmp = URLDecoder.decode(tmp, uriEncoding);
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
					nodeGUID, sTableId, ignore, order, where2, highlights,
					ipage, pageNum, "");
			SearchResults sr = searchEngine.doSearch(command);
			//
			//
			model.put("action", "News");
			model.put("tpl", tpl);
			// 搜索结果
			model.put("rs", sr.getHits());
			model.put("pb", sr.getPageBuilder());
			model.put("searchTime", sr.getSearchTime());
			//
			model.put("keyword", URLEncoder.encode(keyword, "UTF-8"));
			model.put("keyword2", keyword);
			model.put("page", page);
			model.put("pageNum", pageNum);
			model.put("encoding", encoding);
			model.put("tplEncoding", tplEncoding);
			model.put("fields", fields);
			model.put("nodeId", nodeId);
			model.put("nodeGUID", nodeGUID);
			model.put("tableId", sTableId);
			model.put("ignore", ignore);
			model.put("order", order);
			model.put("where", where);
			model.put("extra", extra);
			model.put("extraHash", extraHash);
			//
			try {
				if (PublishProfileInfoHolder.isEnableProfile()) {
					//
					PublishProfileInfoHolder.getProfile().setStartActionTime(
							System.currentTimeMillis());
					PublishProfileInfoHolder.getProfile().setActionName(
							"search");
				}
				TemplateContext context = new TemplateContext();
				// System.out.println("tpl="+tpl);
				context.setTplName(tpl);
				Map finalmodel = Collections.synchronizedMap(new HashMap());
				//
				finalmodel.put("__direct_out__", "yes");
				finalmodel.putAll(model);
				context.setModel(finalmodel);
				List errors = new ArrayList();
				getTemplateEngine().renderTemplate(context, errors);
				String content = context.getTplContent();
				response.setContentType("text/html;charset=" + tplEncoding);
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
		return null;
	}

	protected void setRequestCharacterEncoding(HttpServletRequest request) {
		// no set chracter encoding
	}

	public void afterPropertiesSet() throws Exception {

	}

	public static void main(String[] args) {
		String test = "%BB%A4%C0%ED";
		try {
			test = URLDecoder.decode(test, "GBK");
			System.out.println("test=" + test);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
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
