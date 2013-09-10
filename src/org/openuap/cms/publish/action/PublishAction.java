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

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.core.event.PublishEvent;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.manager.NodeQuickQuery;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.security.NodeSecurityUtil;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.publish.security.PublishPermissionConstant;
import org.openuap.cms.repo.event.ContentIndexEvent;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.schedule.JobEntry;
import org.openuap.cms.schedule.WorkerHelper;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.util.PageInfo;
import org.openuap.cms.util.ui.NodeType;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 内容发布控制器.
 * </p>
 * 
 * <p>
 * $Id: PublishAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishAction extends AdminAction {
	/** 结点管理. */
	private NodeManager nodeManager;

	/** 内容模型域管理. */
	private ContentFieldManager contentFieldManager;

	/** 动态内容管理. */
	private DynamicContentManager dynamicContentManager;

	/** 发布点管理. */
	private PsnManager psnManager;

	/** 内容发布引擎. */
	private PublishEngine publishEngine;

	/** 内容版本宏引擎. */
	private CmsMacroEngine cmsMacroEngine;

	//
	private String defaultViewName;

	private String defaultScreensPath;

	private String publishXmlViewName;

	private String contentFramesetViewName;

	private String contentHeaderViewName;

	private String contentListViewName;

	private String contentListViewName2;

	private String externContentListViewName;

	private String recycleListViewName;

	private String contentOperationView;

	//
	private String refreshSettingViewName;

	private String publishSettingViewName;

	private String unpublishSettingViewName;

	private String republishSettingViewName;

	//
	private String topSettingViewName;

	private String pinkSettingViewName;

	private String sortSettingViewName;

	private String linkStateViewName;

	//
	private String contentViewName;

	//
	private String publishFileFormatViewName;

	/** JS视图名. */
	private String jsViewName;

	//
	private String nodeFsViewName;
	//
	private String nodeHeaderViewName;

	private String nodeTreeTableViewName;
	private String publishTreeViewName;

	/**
	 * 
	 */
	public PublishAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/publish/";
		defaultViewName = defaultScreensPath + "publish.html";
		publishTreeViewName = defaultScreensPath + "publish-site.htm";
		jsViewName = defaultScreensPath + "publish.js";
		publishXmlViewName = defaultScreensPath + "publish_tree.xml";
		contentFramesetViewName = defaultScreensPath
				+ "list/publish_frameset.html";
		contentHeaderViewName = defaultScreensPath
				+ "publish_content_header.html";
		contentListViewName = defaultScreensPath
				+ "list/publish_content_list.html";
		contentListViewName2 = defaultScreensPath
				+ "list/publish_content_list2.html";
		externContentListViewName = defaultScreensPath
				+ "list/publish_content_list_extern.html";
		contentOperationView = defaultScreensPath
				+ "publish_operation_result.html";
		recycleListViewName = defaultScreensPath
				+ "recycle-bin/publish_recycle_bin_list.html";
		refreshSettingViewName = defaultScreensPath
				+ "node/node_refresh_dialog.html";
		publishSettingViewName = defaultScreensPath
				+ "node/node_publish_dialog.html";
		unpublishSettingViewName = defaultScreensPath
				+ "node/node_unpublish_dialog.html";
		republishSettingViewName = defaultScreensPath
				+ "node/node_republish_dialog.html";
		topSettingViewName = defaultScreensPath + "list/top_dialog.html";
		pinkSettingViewName = defaultScreensPath + "list/pink_dialog.html";
		sortSettingViewName = defaultScreensPath + "list/sort_dialog.html";
		contentViewName = defaultScreensPath + "list/publish_view.html";
		linkStateViewName = defaultScreensPath + "list/publish_link_state.html";
		//
		publishFileFormatViewName = defaultScreensPath
				+ "publish_file_format.html";
		nodeFsViewName = defaultScreensPath + "node/publish_node_fs.html";
		nodeHeaderViewName = defaultScreensPath
				+ "node/publish_node_header.html";
		nodeTreeTableViewName = defaultScreensPath
				+ "node/publish_node_treetable.html";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		ModelAndView mv = super.beforePerform(request, response, helper, model);
		if (mv != null) {
			return mv;
		}
		// model.put("layout", "/plugin/cms/base/layouts/admin.html");
		return null;
	}

	/**
	 * 内容预览功能
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doPreview(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		String page = request.getParameter("page");
		// String contentId=req.getParameter("contentId");
		Integer p = new Integer(0);
		if (nodeId != null && indexId != null) {
			Long nid = new Long(nodeId);
			Long iid = new Long(indexId);
			if (page != null) {
				p = new Integer(page);
			}
			// 通过发布引擎获取内容，保证动静态效果一致
			List errors = new ArrayList();
			String result = publishEngine.getPreviewContent(nid, iid, p
					.intValue(), errors);

			response.setContentType("text/html;charset="
					+ getTemplateEncoding());
			//
			PrintWriter pw = response.getWriter();
			pw.print(result);
			pw.flush();
			pw.close();
			return null;
		}
		return null;

	}

	/**
	 * the default action will show the init publish site tree.
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
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		// get the root nodes
		int f = 0;
		List rootNodes = nodeManager.getNodes(new Long(0), new Long(0),
				new Integer(f));
		model.put("rootNodes", rootNodes);
		model.put("nodeManager", nodeManager);
		model.put("dynamicContentManager", dynamicContentManager);
		// 结点树不设置缓存
		setNoCacheHeader(response);
		return mv;
	}

	/**
	 * 发布管理TreeTable框架视图
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doNodeFS(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		// ModelAndView mv = new ModelAndView(nodeFsViewName, model);
		return doNodeTreeTable(request, response, helper, model);
	}

	/**
	 * 发布管理的TreeTable框架视图头部
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doNodeHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(nodeHeaderViewName, model);
		return mv;
	}

	/**
	 * 发布管理的TreeTable视图
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doNodeTreeTable(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(nodeTreeTableViewName, model);
		//
		List rootNodes = nodeManager.getChildNodes(new Long(0));
		model.put("rootNodes", rootNodes);
		model.put("nodeManager", nodeManager);
		List nodes = nodeManager.getAllNodesFromCache();
		NodeQuickQuery quickQuery = new NodeQuickQuery(nodes);
		model.put("quickQuery", quickQuery);
		model.put("dynamicContentManager", dynamicContentManager);
		return mv;
	}

	/**
	 * 内容刷新设置对话框
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRefreshSettingDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String nodeId = helper.getString("nodeId", "0");
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.RefreshNode)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(refreshSettingViewName, model);
		return mv;
	}

	/**
	 * 内容发布设置对话框
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
	 * @throws UnauthorizedException
	 */
	public ModelAndView doPublishSettingDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String nodeId = helper.getString("nodeId", "0");
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.PublishNode)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(publishSettingViewName, model);
		return mv;
	}

	/**
	 * 取消发布设置对话框
	 * 
	 * @param request
	 * @param response
	 * 
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doUnPublishSettingDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String nodeId = helper.getString("nodeId", "0");
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.UnpublishNode)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(unpublishSettingViewName, model);
		return mv;
	}

	/**
	 * 重新发布设置对话框
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
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRePublishSettingDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String nodeId = helper.getString("nodeId", "0");
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.RepublishNode)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(republishSettingViewName, model);
		return mv;
	}

	/**
	 * 结点更新 TODO １）改造前台Ａｊａｘ调用，使用ｐｒｏｔｏｔｙｐｅ ２）发布日志 ３）显示错误
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRefreshNode(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		//
		boolean success;
		// 结点id
		String nodeId = request.getParameter("nodeId");
		// 是否刷新首页
		int refreshIndex = helper.getInt("refreshIndex", 0);
		// 是否刷新内容页
		int refreshContent = helper.getInt("refreshContent", 0);
		// 是否刷新附加发布页
		int refreshExtra = helper.getInt("refreshExtra", 0);
		// 是否包含子结点
		int includeSub = helper.getInt("includeSub", 0);
		// 每次处理内容数
		String contentNum = helper.getString("contentNum", "20");
		//
		if (nodeId == null) {
			success = false;
		} else {
			// 判断是否具有权限
			if (!NodeSecurityUtil.hasPermission(
					PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
					PublishPermissionConstant.RefreshNode)) {
				throw new UnauthorizedException();
			}

			// 准备计划任务属性
			Hashtable prop = new Hashtable();
			//
			prop.put("containChildNode", includeSub != 0 ? "true" : "false");
			prop.put("containExtraPublish", refreshExtra != 0 ? "true"
					: "false");
			prop.put("containContent", refreshContent != 0 ? "true" : "false");
			prop.put("containIndex", refreshIndex != 0 ? "true" : "false");
			prop.put("processContentNums", contentNum);
			prop.put("nodeId", nodeId);
			prop.put("mode", new Integer(PublishEngineMode.REFRESH_MODE));

			//
			try {
				JobEntry job = new JobEntry(0, 0, 0, 0, 0,
						"org.openuap.cms.engine.task.PublishNodeTask");
				job.setProperty(prop);
				// 启动任务
				WorkerHelper.startTask(job);
				//
				success = true;
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("name");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	/**
	 * 结点发布
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws UnauthorizedException
	 */
	public ModelAndView doPublishNode(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		//
		boolean success;
		// 结点id
		String nodeId = request.getParameter("nodeId");
		// 是否刷新首页
		int refreshIndex = helper.getInt("refreshIndex", 0);
		// 是否刷新内容页
		int refreshContent = helper.getInt("refreshContent", 0);
		// 是否刷新附加发布页
		int refreshExtra = helper.getInt("refreshExtra", 0);
		// 是否包含子结点
		int includeSub = helper.getInt("includeSub", 0);
		// 每次处理内容数
		String contentNum = helper.getString("contentNum", "20");
		//
		if (nodeId == null) {
			success = false;
		} else {
			// 判断是否具有权限
			if (!NodeSecurityUtil.hasPermission(
					PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
					PublishPermissionConstant.PublishNode)) {
				throw new UnauthorizedException();
			}

			// 准备计划任务属性
			Hashtable prop = new Hashtable();
			//
			prop.put("containChildNode", includeSub != 0 ? "true" : "false");
			prop.put("containExtraPublish", refreshExtra != 0 ? "true"
					: "false");
			prop.put("containContent", refreshContent != 0 ? "true" : "false");
			prop.put("containIndex", refreshIndex != 0 ? "true" : "false");
			prop.put("processContentNums", contentNum);
			prop.put("nodeId", nodeId);
			prop.put("mode", new Integer(PublishEngineMode.PUBLISH_MODE));

			//
			try {
				JobEntry job = new JobEntry(0, 0, 0, 0, 0,
						"org.openuap.cms.engine.task.PublishNodeTask");
				job.setProperty(prop);
				// 启动任务
				WorkerHelper.startTask(job);
				//
				success = true;
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("name");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	/**
	 * 取消发布结点
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doUnPublishNode(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		boolean success;
		// 结点id
		String nodeId = request.getParameter("nodeId");
		// 是否刷新首页
		int refreshIndex = helper.getInt("refreshIndex", 0);
		// 是否刷新内容页
		int refreshContent = helper.getInt("refreshContent", 0);
		// 是否刷新附加发布页
		int refreshExtra = helper.getInt("refreshExtra", 0);
		// 是否包含子结点
		int includeSub = helper.getInt("includeSub", 0);
		// 每次处理内容数
		String contentNum = helper.getString("contentNum", "50");
		//
		if (nodeId == null) {
			success = false;
		} else {
			// 判断是否具有权限
			if (!NodeSecurityUtil.hasPermission(
					PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
					PublishPermissionConstant.UnpublishNode)) {
				throw new UnauthorizedException();
			}

			// 准备计划任务属性
			Hashtable prop = new Hashtable();
			//
			prop.put("containChildNode", includeSub != 0 ? "true" : "false");
			prop.put("containExtraPublish", refreshExtra != 0 ? "true"
					: "false");
			prop.put("containContent", refreshContent != 0 ? "true" : "false");
			prop.put("containIndex", refreshIndex != 0 ? "true" : "false");
			prop.put("processContentNums", contentNum);
			prop.put("nodeId", nodeId);
			prop.put("mode", new Integer(PublishEngineMode.UNPUBLISH_MODE));

			//
			try {
				JobEntry job = new JobEntry(0, 0, 0, 0, 0,
						"org.openuap.cms.engine.task.PublishNodeTask");
				job.setProperty(prop);
				// 启动任务
				WorkerHelper.startTask(job);
				//
				success = true;
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("name");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	/**
	 * 结点重新发布
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRePublishNode(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		//
		boolean success;
		// 结点id
		String nodeId = request.getParameter("nodeId");
		// 是否刷新首页
		int refreshIndex = helper.getInt("refreshIndex", 0);
		// 是否刷新内容页
		int refreshContent = helper.getInt("refreshContent", 0);
		// 是否刷新附加发布页
		int refreshExtra = helper.getInt("refreshExtra", 0);
		// 是否包含子结点
		int includeSub = helper.getInt("includeSub", 0);
		// 每次处理内容数
		String contentNum = helper.getString("contentNum", "50");
		// 是否刷新文件
		int refreshfile = helper.getInt("refreshfile", 0);

		if (nodeId == null) {
			success = false;
		} else {
			// 判断是否具有权限
			if (!NodeSecurityUtil.hasPermission(
					PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
					PublishPermissionConstant.RepublishNode)) {
				throw new UnauthorizedException();
			}

			// 准备计划任务属性
			Hashtable prop = new Hashtable();
			//
			prop.put("containChildNode", includeSub != 0 ? "true" : "false");
			prop.put("containExtraPublish", refreshExtra != 0 ? "true"
					: "false");
			prop.put("containContent", refreshContent != 0 ? "true" : "false");
			prop.put("containIndex", refreshIndex != 0 ? "true" : "false");
			prop.put("processContentNums", contentNum);
			prop.put("nodeId", nodeId);
			prop.put("mode", new Integer(PublishEngineMode.REPUBLISH_MODE));
			prop.put("refreshfile", refreshfile != 0 ? "true" : "false");
			//
			try {
				JobEntry job = new JobEntry(0, 0, 0, 0, 0,
						"org.openuap.cms.engine.task.PublishNodeTask");
				job.setProperty(prop);
				// 启动任务
				WorkerHelper.startTask(job);
				//
				success = true;
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("name");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	/**
	 * 刷新结点首页
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
	 * @throws
	 * @throws
	 */
	public ModelAndView doRefreshNodeIndex(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.RefreshIndex)) {
			throw new UnauthorizedException();
		}
		boolean success;
		// 准备计划任务属性
		Hashtable prop = new Hashtable();
		//
		prop.put("containChildNode", "false");
		prop.put("containExtraPublish", "false");
		prop.put("containContent", "false");
		prop.put("containIndex", "true");
		prop.put("processContentNums", "50");
		prop.put("nodeId", nodeId);
		prop.put("mode", new Integer(PublishEngineMode.REFRESH_MODE));
		prop.put("refreshfile", "true");
		//
		try {
			JobEntry job = new JobEntry(0, 0, 0, 0, 0,
					"org.openuap.cms.engine.task.PublishNodeTask");
			job.setProperty(prop);
			// 启动任务
			WorkerHelper.startTask(job);
			//
			success = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("name");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	/**
	 * 查看首页
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
	 * @throws IOException
	 * @throws
	 */
	public ModelAndView doViewIndex(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.ViewContent)) {
			throw new UnauthorizedException();
		}
		//
		if (nodeId != null) {
			Long nid = new Long(nodeId);
			Node node = nodeManager.getNodeById(nid);
			if (node != null) {
				// $Date: 2006/08/31 02:26:23 $ by Weiping Ju
				if (node.getPublishMode().equals(
						PublishMode.STATIC_MODE.getMode())) {
					String url = psnManager.getPsnUrlInfo(node.getContentUrl());
					String indexFileName = node.getIndexName();
					indexFileName = indexFileName.replaceAll("\\{NodeID\\}",
							node.getNodeId().toString());
					url = url + "/" + indexFileName;
					//
					// add function,if the url has change,should update the
					// nodeUrl value
					String nodeUrl = node.getNodeUrl();
					if (nodeUrl == null || !nodeUrl.equals(url)) {
						node.setNodeUrl(url);
						nodeManager.saveNode(node);
					}
					response.sendRedirect(url);
				} else {
					String url = node.getIndexPortalUrl();

					url = url.replaceAll("\\{NodeID\\}", node.getNodeId()
							.toString());

					//
					String baseUrl = CMSConfig.getInstance().getBaseUrl();
					if (baseUrl.endsWith("/")) {
						baseUrl.substring(0, baseUrl.length() - 1);
					}
					//
					if (!url.startsWith("http")) {
						url = baseUrl + "/" + url;
					}
					response.sendRedirect(url);
				}
			}
		}
		return null;
	}

	public ModelAndView doViewPublish(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException {
		String indexId = request.getParameter("indexId");
		if (indexId != null) {
			Long iid = new Long(indexId);
			ContentIndex ci = dynamicContentManager.getContentIndexById(iid);
			if (ci != null) {
				String url = ci.getUrl();
				response.sendRedirect(url);
			}
		}
		return null;
	}

	/**
	 * 查看某条内容的具体内容
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
	 * @throws IOException
	 * @throws UnauthorizedException
	 */
	public ModelAndView doView(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		String keyword = helper.getString("keyword", "");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.ViewContent)) {
			throw new UnauthorizedException();
		}

		if (indexId != null && nodeId != null) {

			ModelAndView mv = new ModelAndView(contentViewName, model);
			Long nid = new Long(nodeId);
			Long iid = new Long(indexId);
			Node node = nodeManager.getNodeById(nid);
			Long tableId = node.getTableId();
			List cfs = contentFieldManager.getContentFieldsFromCache(tableId);
			Object cobj = dynamicContentManager.getContent(iid, tableId);
			//
			Map contentIndex = null;
			Map content = null;
			//
			if (cobj instanceof Object[]) {
				Object[] ct = (Object[]) cobj;
				contentIndex = (Map) ct[0];
				content = (Map) ct[1];
				content.putAll(contentIndex);
				//
				ContentField mainField = contentFieldManager
						.getMainField(tableId);
				if (mainField != null) {
					String mainContent = (String) content.get(mainField
							.getFieldName());
					if (mainContent != null) {
						// mainContent = mainContent.replaceAll(
						// "\\.\\.\\/resource",
						// "./resource");
						// content.put(mainField.getFieldName(), mainContent);
					}
				}
				model.put("content", content);
			}
			model.put("indexId", indexId);
			model.put("nodeId", nodeId);
			model.put("cfs", cfs);
			model.put("cmsMacroEngine", cmsMacroEngine);
			model.put("keyword", keyword);
			return mv;
		}
		return null;
	}

	/**
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
	 * @throws
	 */
	public ModelAndView doPublishXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {

		String nodeId = request.getParameter("nodeId");
		//
		if (!SecurityUtil.hasPermission(PublishPermissionConstant.OBJECT_TYPE
				.toString(), "0", PublishPermissionConstant.ViewContent)) {
			throw new UnauthorizedException();
		}
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(publishXmlViewName, model);
			Long id = new Long(nodeId);
			int f = 0;
			List nodes = nodeManager.getNodes(id, new Long(0), new Integer(f));
			//
			setNoCacheHeader(response);

			model.put("responseType", "text/xml");
			model.put("nodes", nodes);
			model.put("nodeManager", nodeManager);
			model.put("dynamicContentManager", dynamicContentManager);

			return mv;
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doSiteJS(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		if (nodeId == null) {
			nodeId = "0";
		}
		//
		ModelAndView mv;
		try {
			mv = new ModelAndView(jsViewName, model);
			Long id = new Long(nodeId);
			int f = 0;
			List nodes = nodeManager.getNodes(id, new Long(0), new Integer(f));
			//
			setNoCacheHeader(response);

			model.put("responseType", "text/javaScript");
			model.put("nodes", nodes);
			model.put("nodeManager", nodeManager);
			model.put("dynamicContentManager", dynamicContentManager);

			return mv;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doListContent(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//

		// String nodeId = request.getParameter("nodeId");
		// if (!SecurityUtil.hasPermission(PublishPermissionConstant.OBJECT_TYPE
		// .toString(), nodeId, PublishPermissionConstant.ViewContent)) {
		// throw new UnauthorizedException();
		// }
		//
		// if (nodeId != null) {
		// ModelAndView mv = new ModelAndView(contentFramesetViewName, model);
		// model.put("nodeId", nodeId);
		// return mv;
		// }
		return doContentList(request, response, helper, model);
	}

	public ModelAndView doContentHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String nodeId = request.getParameter("nodeId");
		//

		//
		if (nodeId != null) {
			String indexTplPath = "";
			String indexTplName = "";

			String contentTplPath = "";
			String contentTplName = "";

			String imageTplPath = "";
			String imageTplName = "";

			Long id = new Long(nodeId);
			Node node = nodeManager.getNodeById(id);
			//
			String indexTpl = node.getIndexTpl();
			if (indexTpl != null) {
				int pos = indexTpl.lastIndexOf("/");
				if (pos > -1) {
					indexTplPath = indexTpl.substring(0, pos);
					indexTplName = indexTpl.substring(pos + 1);
				} else {
					indexTplPath = "/";
					indexTplName = indexTpl;
				}
			}

			String contentTpl = node.getContentTpl();
			if (contentTpl != null) {
				int pos = contentTpl.lastIndexOf("/");
				if (pos > -1) {
					contentTplPath = contentTpl.substring(0, pos);
					contentTplName = contentTpl.substring(pos + 1);
				} else {
					contentTplPath = "/";
					contentTplName = contentTpl;
				}
			}
			String imageTpl = node.getImageTpl();
			if (imageTpl != null) {
				int pos = imageTpl.lastIndexOf("/");
				if (pos > -1) {
					imageTplPath = imageTpl.substring(0, pos);
					imageTplName = imageTpl.substring(pos + 1);
				} else {
					imageTplPath = "/";
					imageTplName = imageTpl;
				}
			}
			ModelAndView mv = new ModelAndView(contentHeaderViewName, model);
			model.put("indexTplPath", indexTplPath);
			model.put("indexTplName", indexTplName);
			model.put("contentTplPath", contentTplPath);
			model.put("contentTplName", contentTplName);
			model.put("imageTplPath", imageTplPath);
			model.put("imageTplName", imageTplName);
			model.put("node", node);
			return mv;
		}
		return null;
	}

	/**
	 * 显示内容列表
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doContentList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		/***********************************************************************
		 * String column_condition = ""; // String nodeId =
		 * request.getParameter("nodeId"); String state =
		 * request.getParameter("state"); String page =
		 * request.getParameter("page"); String pageNum =
		 * request.getParameter("pageNum"); String order =
		 * request.getParameter("order"); String order_mode =
		 * request.getParameter("order_mode"); String order_name =
		 * request.getParameter("order_name"); String where =
		 * request.getParameter("where"); String pubDate =
		 * request.getParameter("pubDate"); String pubDate2 =
		 * request.getParameter("pubDate2"); try { if (nodeId != null) { Long id
		 * = new Long(nodeId); // 从缓存中获取结点 Node node = nodeManager.getNode(id);
		 * if (node != null) { // Long tableId = node.getTableId(); // 权限校验 if
		 * (!SecurityUtil.hasPermission(
		 * PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
		 * PublishPermissionConstant.ViewContent)) { throw new
		 * UnauthorizedException(); } // // get the keyword String tmp =
		 * request.getParameter("keyword"); if (tmp == null) { tmp = ""; } // //
		 * String keyword = tmp.trim(); // String fields =
		 * helper.getString("fields", ""); // // if (where == null) { where =
		 * ""; } if (order == null) { order = ""; } if (order_mode == null) {
		 * order_mode = ""; } if (order_name == null) { order_name = ""; }
		 * order_name = order_name.replaceAll("\\^", ""); // String final_order
		 * = ""; if (!order.equals("") && !order_mode.equals("")) { final_order
		 * = order + " " + order_mode; }
		 * 
		 * String orderBy = ""; List argList = new ArrayList(); Long start = new
		 * Long(0); Long limit = new Long(15); Object[] args = null; if (state
		 * == null || state.equals("")) { where = " ci.state<>-1 "; } else {
		 * where = " ci.state=? "; argList.add(new Integer(state)); } if
		 * (pageNum != null) { limit = new Long(pageNum); } else { pageNum =
		 * "15"; } if (page != null) { start = new Long((Integer.parseInt(page)
		 * - 1) limit.intValue()); } else { page = "1"; } if (order != null) {
		 * orderBy = final_order; } if (argList.size() > 0) { args =
		 * argList.toArray(); } // if (keyword != null && !keyword.equals("")) {
		 * if (fields != null && !fields.equals("")) { String columns[] =
		 * fields.split(","); if (columns != null) { for (int i = 0; i <
		 * columns.length; i++) { column_condition += " or c." + columns[i] + "
		 * like '%" + keyword + "%'"; } if (!column_condition.equals("")) {
		 * column_condition = column_condition .substring(4); column_condition =
		 * " and (" + column_condition + ")"; } } } else { // 获取内容属性信息 List
		 * searchFields = contentFieldManager
		 * .getSearchFieldsFromCache(tableId); for (int i = 0; i <
		 * searchFields.size(); i++) { ContentField cf = (ContentField)
		 * searchFields .get(i); String columName = cf.getFieldName();
		 * column_condition += " or c." + columName + " like '%" + keyword +
		 * "%'"; } // end for if (!column_condition.equals("")) {
		 * column_condition = column_condition .substring(4); column_condition =
		 * " and (" + column_condition + ")"; } } } // where +=
		 * column_condition; // // pubDate if (pubDate != null &&
		 * !pubDate.equals("")) { SimpleDateFormat sdf = new SimpleDateFormat(
		 * "yyyy-MM-dd"); Date dd = sdf.parse(pubDate); where += " and
		 * ci.publishDate>=" + dd.getTime() + ""; } else { pubDate = ""; } //
		 * pubDate2 if (pubDate2 != null && !pubDate2.equals("")) {
		 * SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd"); Date dd =
		 * sdf.parse(pubDate2); where += " and ci.publishDate<=" + dd.getTime()
		 * + ""; } else { pubDate2 = ""; }
		 * 
		 * ModelAndView mv = new ModelAndView(contentListViewName, model); //
		 * ContentField cf = contentFieldManager
		 * .getTitleFieldFromCache(tableId); ContentField photoField =
		 * contentFieldManager .getPhotoFieldFromCache(tableId); if (cf != null)
		 * { String fieldName = cf.getFieldName(); PageInfo pageInfo = new
		 * PageInfo(); pageInfo.itemsPerPage(limit.intValue());
		 * pageInfo.page(Integer.parseInt(page)); // 获取动态内容列表 List contents =
		 * dynamicContentManager.getContentList( id, tableId, where, orderBy,
		 * args, start, limit, pageInfo); //
		 * 
		 * @todo maybe only PageBuilder PageBuilder pb = new
		 *       PageBuilder(limit.intValue()); pb.items((int)
		 *       pageInfo.getTotalNum()); pb.page(Integer.parseInt(page)); //
		 *       model.put("contents", contents); model.put("titleFieldName",
		 *       fieldName); model.put("pb", pb); } if (photoField != null) {
		 *       model.put("photoFieldName", photoField.getFieldName()); }
		 *       model.put("page", page); model.put("pageNum", pageNum);
		 *       model.put("order", order); model.put("order_mode", order_mode);
		 *       model.put("order_name", order_name); model.put("where", where);
		 *       model.put("keyword", keyword); model.put("node", node);
		 *       model.put("nodeId", nodeId); model.put("action", this);
		 *       model.put("state", state); model.put("pubDate", pubDate);
		 *       model.put("pubDate2", pubDate2);
		 *       model.put("dynamicContentManager", dynamicContentManager);
		 *       return mv; } } } catch (Exception ex) { ex.printStackTrace();
		 *       log.error(ex); } return null;
		 **********************************************************************/
		return doContentList2(request, response, helper, model);
	}

	public ModelAndView doContentList2(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		String column_condition = "";
		//
		String nodeId = request.getParameter("nodeId");
		String state = request.getParameter("state");
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		String order_mode = request.getParameter("order_mode");
		String order_name = request.getParameter("order_name");
		String where = request.getParameter("where");
		String pubDate = request.getParameter("pubDate");
		String pubDate2 = request.getParameter("pubDate2");
		try {
			if (nodeId != null) {
				Long id = new Long(nodeId);
				//
				List rootNodes = nodeManager.getChildNodes(id);
				model.put("rootNodes", rootNodes);
				model.put("nodeManager", nodeManager);
				model.put("dynamicContentManager", dynamicContentManager);
				List nodes = nodeManager.getAllNodesFromCache();
				NodeQuickQuery quickQuery = new NodeQuickQuery(nodes);
				model.put("quickQuery", quickQuery);
				// 从缓存中获取结点
				Node node = nodeManager.getNode(id);

				if (node != null) {
					//
					Long tid = node.getTableId();
					model.put("node", node);
					model.put("nodeId", nodeId);
					model.put("tid", tid);
					//

					// 权限校验
					if (!NodeSecurityUtil.hasPermission(
							PublishPermissionConstant.OBJECT_TYPE.toString(),
							nodeId, PublishPermissionConstant.ViewContent)) {
						throw new UnauthorizedException();
					}
					//
					Long tableId = node.getTableId();
					ContentField cf = contentFieldManager
							.getTitleFieldFromCache(tableId);
					//
					ContentField photoField = contentFieldManager
							.getPhotoFieldFromCache(tableId);

					if (node.getNodeType().equals(
							NodeType.EXTERN_NODE_TYPE.getType())) {
						ModelAndView mv = new ModelAndView(
								externContentListViewName, model);
						return mv;
					}
					//
					// get the keyword
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

					String orderBy = "";
					List argList = new ArrayList();
					Long start = new Long(0);
					Long limit = new Long(15);
					Object[] args = null;
					if (state == null || state.equals("")) {
						where = " ci.state<>-1 ";
					} else {
						where = " ci.state=? ";
						argList.add(new Integer(state));
					}
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
					if (order != null) {
						orderBy = final_order;
					}
					if (argList.size() > 0) {
						args = argList.toArray();
					}
					//
					if (keyword != null && !keyword.equals("")) {
						if (fields != null && !fields.equals("")) {
							String columns[] = fields.split(",");
							if (columns != null) {
								for (int i = 0; i < columns.length; i++) {
									column_condition += " or ci." + columns[i]
											+ " like '%" + keyword + "%'";

								}
								if (!column_condition.equals("")) {
									column_condition = column_condition
											.substring(4);
									column_condition = " and ("
											+ column_condition + ")";
								}
							}
						} else {
							// 使用所有可检索域
							List<ContentField> contentFields = contentFieldManager
									.getSearchFieldsFromCache(tableId);
							if (contentFields != null) {
								for (ContentField field : contentFields) {
									column_condition += " or c."
											+ field.getFieldName() + " like '%"
											+ keyword + "%'";
								}
							}
							// 获取内容属性信息
							// column_condition += " or ci.contentTitle like '%"
							// + keyword + "%'";

							if (!column_condition.equals("")) {
								column_condition = column_condition
										.substring(4);
								column_condition = " and (" + column_condition
										+ ")";
							}

						}
					}

					//
					where += column_condition;
					//
					// pubDate
					if (pubDate != null && !pubDate.equals("")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = sdf.parse(pubDate);
						where += " and ci.publishDate>=" + dd.getTime() + "";
					} else {
						pubDate = "";
					}
					// pubDate2
					if (pubDate2 != null && !pubDate2.equals("")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = sdf.parse(pubDate2);
						where += " and ci.publishDate<=" + dd.getTime() + "";
					} else {
						pubDate2 = "";
					}
					// 作者过滤
					String author = helper.getString("author", "");
					if (author.equals("me")) {
						where += " and ci.creationUserName='"
								+ this.getUser().getName() + "'";
					} else if (author.equals("others")) {
						where += " and ci.creationUserName<>'"
								+ this.getUser().getName() + "'";
					}
					model.put("author", author);
					//
					ModelAndView mv = new ModelAndView(contentListViewName2,
							model);
					// 处理外部结点，其不存在内容模型

					if (cf != null) {
						String fieldName = cf.getFieldName();
						PageInfo pageInfo = new PageInfo();
						pageInfo.itemsPerPage(limit.intValue());
						pageInfo.page(Integer.parseInt(page));
						// 获取动态内容列表
						List contents = null;
						if (StringUtil.hasText(keyword)) {
							contents = dynamicContentManager.getContentList(id,
									tableId, where, orderBy, args, start,
									limit, pageInfo);
						} else {
							contents = dynamicContentManager
									.getQuickContentList(id, tableId, where,
											orderBy, args, start, limit,
											pageInfo);
						}
						//
						PageBuilder pb = new PageBuilder(limit.intValue());
						pb.items((int) pageInfo.getTotalNum());
						pb.page(Integer.parseInt(page));
						//
						model.put("contents", contents);
						model.put("titleFieldName", fieldName);
						model.put("pb", pb);
					}
					if (photoField != null) {
						model.put("photoFieldName", photoField.getFieldName());
					}

					model.put("page", page);
					model.put("pageNum", pageNum);
					model.put("order", order);
					model.put("order_mode", order_mode);
					model.put("order_name", order_name);
					model.put("where", where);
					model.put("keyword", keyword);

					model.put("state", state);
					model.put("pubDate", pubDate);
					model.put("pubDate2", pubDate2);

					return mv;
				}
			}
		} catch (Exception ex) {
			if (ex instanceof UnauthorizedException) {
				throw ex;
			}
			log.error(ex);
		}
		return null;
	}

	/**
	 * 显示回收站内容列表
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
	 * @throws
	 */
	public ModelAndView doRecycleContentListForBak(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		//
		if (!SecurityUtil.hasPermission(PublishPermissionConstant.OBJECT_TYPE
				.toString(), nodeId, PublishPermissionConstant.ViewRecycleBin)) {
			throw new UnauthorizedException();
		}
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(recycleListViewName, model);
			//
			Long id = new Long(nodeId);
			Node node = nodeManager.getNode(id);
			Long tableId = node.getTableId();
			ContentField cf = contentFieldManager
					.getTitleFieldFromCache(tableId);
			if (cf != null) {
				String fieldName = cf.getFieldName();
				List contents = dynamicContentManager.getRecycleContentList(id,
						tableId);
				model.put("contents", contents);
				model.put("titleFieldName", fieldName);
			}
			model.put("node", node);
			model.put("action", this);
			return mv;
		}
		return null;
	}

	/**
	 * 内容发布
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doPublish(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		model.put("operation", "publish");
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		// 权限验证
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.Publish)) {
			throw new UnauthorizedException();
		}
		// 批量模式
		String multi = helper.getString("multi", "0");
		String[] pData = request.getParameterValues("pData");
		//
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		//
		if (multi.equals("1")) {
			// 错误
			List errors = new ArrayList();
			try {
				model.put("operation", "batch_publish");
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						String index_id = pData[i];
						Long batch_iid = new Long(index_id);
						// 获得内容索引对象
						ContentIndex ci = dynamicContentManager
								.getContentIndexById(batch_iid);
						if (ci == null) {
							errors.add("{" + i + "}content_is_not_exists");
						}
						if (ci.getState().intValue() == ContentIndex.STATE_UNPUBLISHED) {
							// need publish
							if (ci.getType().intValue() == ContentIndex.TYPE_INDEX) {
								// index link,only a state
								ci.setState(new Integer(
										ContentIndex.STATE_PUBLISHED));
								dynamicContentManager.saveContentIndex(ci);
							} else {
								// 发布内容
								Long nid = new Long(nodeId);
								publishEngine.publishContent(nid, batch_iid,
										true, errors);
							}
						}
					}
				}
				if (errors.size() > 0) {
					model.put("result", "failed");
					model.put("msgs", errors);
				} else {
					model.put("result", "success");
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
				model.put("result", "failed");
				model.put("msgs", ex1);
			}
		} else {
			//
			try {
				if (indexId != null && nodeId != null) {
					Long iid = new Long(indexId);
					Long nid = new Long(nodeId);

					// String publishUrl = publishContent(iid, nid);
					List errors = new ArrayList();
					ContentIndex ci = dynamicContentManager
							.getContentIndexFromCache(iid);
					if (ci != null) {
						if (ci.getType().intValue() == 2) {
							// index link,only a state
							ci.setState(new Integer("1"));
							dynamicContentManager.saveContentIndex(ci);
						} else {
							publishEngine
									.publishContent(nid, iid, true, errors);
						}
					}

					if (errors.size() > 0) {
						model.put("result", "failed");
						model.put("msgs", errors);
					} else {
						ci = dynamicContentManager.getContentIndexById(iid);
						model.put("result", "success");
						if (ci != null) {
							model.put("publishUrl", ci.getUrl());
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				model.put("result", "failed");
			}
		}
		return mv;
	}

	/**
	 * refresh the content index,recreate the file.
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
	 * @throws
	 */
	public ModelAndView doRefresh(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		model.put("operation", "refresh");
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");

		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.Refresh)) {
			throw new UnauthorizedException();
		}
		//
		String multi = request.getParameter("multi");
		String[] pData = request.getParameterValues("pData");
		//
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		List errors = new ArrayList();
		//
		if (multi != null && multi.equals("1")) {
			// batch
			model.put("operation", "batch_refresh");
			try {
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						String index_id = pData[i];
						Long batch_iid = new Long(index_id);
						ContentIndex ci = dynamicContentManager
								.getContentIndexById(batch_iid);

						if (ci.getState().equals(new Integer("1"))) {
							// need publish
							Long nid = new Long(nodeId);
							publishEngine
									.refreshContent(nid, batch_iid, errors);
						}
					}
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
				model.put("result", "failed");
				model.put("msgs", ex1);
				return mv;
			}
			//
			if (errors.size() > 0) {
				model.put("result", "failed");
				model.put("msgs", errors);
			} else {
				model.put("result", "success");
			}
		} else {
			//
			try {
				if (indexId != null && nodeId != null) {
					Long iid = new Long(indexId);
					Long nid = new Long(nodeId);

					this.publishEngine.refreshContent(nid, iid, errors);
					if (errors.size() > 0) {
						model.put("result", "failed");
						model.put("msgs", errors);
						//
						for (int ie = 0; ie < errors.size(); ie++) {
							Exception e = (Exception) errors.get(ie);
							e.printStackTrace();
						}
					} else {
						ContentIndex ci = dynamicContentManager
								.getContentIndexFromCache(iid);
						String publishUrl = ci.getUrl();
						// 4)show the result message
						model.put("result", "success");
						model.put("publishUrl", publishUrl);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				model.put("result", "failed");
				errors.add(ex);
				model.put("msgs", errors);
			}
		}
		return mv;
	}

	protected String publishContent(Long indexId, Long nodeId) throws Exception {
		try {
			// 1)update the contentIndex
			ContentIndex ci = dynamicContentManager
					.getContentIndexById(indexId);
			Long nid = ci.getNodeId();
			Node node = nodeManager.getNodeById(nid);
			Long tableId = node.getTableId();
			//
			// (1)get the url info
			String url = "";
			// first look up the self url,if exist,it is,otherwise
			// from the node info,get the url info
			// after get the url,need get the file name
			// first get the self filename,if not exist,need get from the node
			// info
			// from node info,include the path name and file name
			// cat the url and the file name is the finla url
			String selfUrl = ci.getSelfUrl();
			String selfFileName = ci.getSelfPublishFileName();
			long timeStamp = ci.getPublishDate().longValue();
			if (StringUtils.hasText(selfUrl)) {
				// get from self url
				url = selfUrl;
			} else {
				// get from node info,psn url defintion.
				String nodeUrl = node.getContentUrl();
				String sp = "\\{PSN-URL:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
				Pattern p = Pattern.compile(sp);
				Matcher m = p.matcher(nodeUrl);
				boolean result = m.find();
				StringBuffer sb = new StringBuffer();
				while (result) {
					String path = m.group(2);
					String psnId = m.group(1);
					Psn psn = psnManager.getPsnById(new Long(psnId));
					String psnUrl = psn.getUrl();
					m.appendReplacement(sb, psnUrl + path);
					result = m.find();
				} // end while result
				m.appendTail(sb);
				url = sb.toString();
			}
			if (StringUtils.hasText(selfFileName)) {
				url += "/" + selfFileName;
			} else {
				String subDir = node.getSubDir();
				String destDir = "";
				if (!subDir.equals("")) {
					String ft = "yyyy-MM-dd";
					if (!subDir.equals("auto")) {
						subDir = subDir.replaceAll("Y", "yyyy");
						subDir = subDir.replaceAll("m", "MM");
						subDir = subDir.replaceAll("d", "dd");
						ft = subDir;
					}
					SimpleDateFormat sf = new SimpleDateFormat(ft);
					destDir = sf.format(new Date(timeStamp * 1000l));
				}
				if (!destDir.equals("")) {
					url += "/" + destDir;
				}
				//

				String stimeStamp = "" + timeStamp;
				String fileName = node.getPublishFileFormat();
				//
				fileName = fileName.replaceAll("\\{TimeStamp\\}", stimeStamp);
				fileName = fileName.replaceAll("\\{ContentID\\}", ci
						.getContentId().toString());
				fileName = fileName.replaceAll("\\{IndexID\\}", indexId
						.toString());
				fileName = fileName.replaceAll("\\{NodeID\\}", ci.getNodeId()
						.toString());
				if (!fileName.equals("")) {
					url += "/" + fileName;
				}
			}
			ci.setState(new Integer("1"));
			ci.setUrl(url);
			dynamicContentManager.saveContentIndex(ci);
			// 2)insert or update the publish_?
			// get the content field list
			List cfs = contentFieldManager.getContentFieldOfTable(tableId,
					" fieldOrder");
			Object cobj = dynamicContentManager.getContent(indexId, tableId);
			// get the content
			Map contentIndex = null;
			Map content = null;
			//
			if (cobj instanceof Object[]) {
				Object[] ct = (Object[]) cobj;
				contentIndex = (Map) ct[0];
				content = (Map) ct[1];
			}
			Map publish = new HashMap();
			// the fixed field
			publish.put("indexId", contentIndex.get("indexId"));
			publish.put("contentId", contentIndex.get("contentId"));
			publish.put("nodeId", contentIndex.get("nodeId"));
			publish.put("publishDate", contentIndex.get("publishDate"));
			publish.put("url", contentIndex.get("url"));
			// the dynamic field
			// get the resouce url to used replace ../resource/?
			String destResourceUrl = "";
			String rsUrl = node.getResourceUrl();
			String sp = "\\{PSN-URL:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
			Pattern p = Pattern.compile(sp);
			Matcher m = p.matcher(rsUrl);
			boolean result = m.find();
			StringBuffer sb = new StringBuffer();
			while (result) {
				String path = m.group(2);
				String psnId = m.group(1);
				Psn psn = psnManager.getPsnById(new Long(psnId));
				String psnUrl = psn.getUrl();
				m.appendReplacement(sb, psnUrl + path);
				result = m.find();
			} // end while result
			m.appendTail(sb);
			destResourceUrl = sb.toString();

			for (int i = 0; i < cfs.size(); i++) {
				ContentField cf = (ContentField) cfs.get(i);
				String type = cf.getFieldType();
				String input = cf.getFieldInput();
				String inputPicker = cf.getFieldInputPicker();
				String name = cf.getFieldName();
				Object raw_value = content.get(name);
				Object value = raw_value;
				if (type.equals("varchar")) {
					// text
					if (inputPicker.equals("upload")
							|| inputPicker.equals("upload_attach")
							|| inputPicker.equals("flash")) {
						//
						String svalue = (String) raw_value;
						svalue = svalue.replaceAll("\\.\\.\\/resource",
								destResourceUrl);
						value = svalue;
					}
				} else if (type.equals("integer")) {
					// integer
					// value = new Integer(raw_value);
				} else if (type.equals("float")) {
					// float
					// value = new Float(raw_value);
				} else if (type.equals("text")) {
					// text,as string
					if (input.equals("RichEditor")) {
						String svalue = (String) raw_value;
						svalue = svalue.replaceAll("\\.\\.\\/resource",
								destResourceUrl);
						value = svalue;
					}
				}
				publish.put(name, value);
			}
			dynamicContentManager.savePublish(tableId, publish);
			// 3)create the static file
			List errors = new ArrayList();
			this.publishEngine.publishContent(nodeId, indexId, true, errors);
			return url;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public ModelAndView doRecycleContentList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		String column_condition = "";
		//
		String nodeId = request.getParameter("nodeId");
		String state = request.getParameter("state");
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		String order_mode = request.getParameter("order_mode");
		String order_name = request.getParameter("order_name");
		String where = request.getParameter("where");
		String pubDate = request.getParameter("pubDate");
		String pubDate2 = request.getParameter("pubDate2");
		try {
			if (nodeId != null) {
				Long id = new Long(nodeId);
				//
				List rootNodes = nodeManager.getChildNodes(id);
				model.put("rootNodes", rootNodes);
				model.put("nodeManager", nodeManager);
				List nodes = nodeManager.getAllNodesFromCache();
				NodeQuickQuery quickQuery = new NodeQuickQuery(nodes);
				model.put("quickQuery", quickQuery);
				// 从缓存中获取结点
				Node node = nodeManager.getNode(id);
				if (node != null) {
					//
					Long tableId = node.getTableId();
					// 权限校验
					if (!NodeSecurityUtil.hasPermission(
							PublishPermissionConstant.OBJECT_TYPE.toString(),
							nodeId, PublishPermissionConstant.ViewRecycleBin)) {
						throw new UnauthorizedException();
					}
					//
					// get the keyword
					String tmp = request.getParameter("keyword");
					if (tmp == null) {
						tmp = "";
					}
					//
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

					String orderBy = "";
					List argList = new ArrayList();
					Long start = new Long(0);
					Long limit = new Long(15);
					Object[] args = null;
					state = "-1";
					if (state == null || state.equals("")) {
						where = " ci.state=-1 ";
					} else {
						where = " ci.state=" + state;
					}
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
					if (order != null) {
						orderBy = final_order;
					}
					if (argList.size() > 0) {
						args = argList.toArray();
					}
					//
					if (keyword != null && !keyword.equals("")) {
						if (fields != null && !fields.equals("")) {
							String columns[] = fields.split(",");
							if (columns != null) {
								for (int i = 0; i < columns.length; i++) {
									column_condition += " or ci." + columns[i]
											+ " like '%" + keyword + "%'";

								}
								if (!column_condition.equals("")) {
									column_condition = column_condition
											.substring(4);
									column_condition = " and ("
											+ column_condition + ")";
								}
							}
						} else {
							// 获取内容属性信息
							column_condition += " or ci.contentTitle like '%"
									+ keyword + "%'";

							if (!column_condition.equals("")) {
								column_condition = column_condition
										.substring(4);
								column_condition = " and (" + column_condition
										+ ")";
							}

						}
					}

					//
					where += column_condition;
					//
					// pubDate
					if (pubDate != null && !pubDate.equals("")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = sdf.parse(pubDate);
						where += " and ci.publishDate>=" + dd.getTime() + "";
					} else {
						pubDate = "";
					}
					// pubDate2
					if (pubDate2 != null && !pubDate2.equals("")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date dd = sdf.parse(pubDate2);
						where += " and ci.publishDate<=" + dd.getTime() + "";
					} else {
						pubDate2 = "";
					}

					ModelAndView mv = new ModelAndView(recycleListViewName,
							model);
					//
					ContentField cf = contentFieldManager
							.getTitleFieldFromCache(tableId);
					ContentField photoField = contentFieldManager
							.getPhotoFieldFromCache(tableId);
					if (cf != null) {
						String fieldName = cf.getFieldName();
						PageInfo pageInfo = new PageInfo();
						pageInfo.itemsPerPage(limit.intValue());
						pageInfo.page(Integer.parseInt(page));
						// 获取动态内容列表
						List contents = dynamicContentManager
								.getQuickContentList(id, tableId, where,
										orderBy, args, start, limit, pageInfo);
						// @todo maybe only PageBuilder
						PageBuilder pb = new PageBuilder(limit.intValue());
						pb.items((int) pageInfo.getTotalNum());
						pb.page(Integer.parseInt(page));
						//
						model.put("contents", contents);
						model.put("titleFieldName", fieldName);
						model.put("pb", pb);
					}
					if (photoField != null) {
						model.put("photoFieldName", photoField.getFieldName());
					}
					model.put("page", page);
					model.put("pageNum", pageNum);
					model.put("order", order);
					model.put("order_mode", order_mode);
					model.put("order_name", order_name);
					model.put("where", where);
					model.put("keyword", keyword);
					model.put("node", node);
					model.put("nodeId", nodeId);
					model.put("action", this);
					model.put("state", state);
					model.put("pubDate", pubDate);
					model.put("pubDate2", pubDate2);
					model.put("dynamicContentManager", dynamicContentManager);
					return mv;
				}
			}
		} catch (Exception ex) {
			if (ex instanceof UnauthorizedException) {
				throw ex;
			}
			log.error(ex);
		}
		return null;
	}

	/**
	 * 取消发布内容
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doUnPublish(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {

		ModelAndView mv = new ModelAndView(contentOperationView, model);
		model.put("operation", "unpublish");
		//
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.UnPublish)) {
			throw new UnauthorizedException();
		}
		String multi = request.getParameter("multi");
		String[] pData = request.getParameterValues("pData");
		//
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);

		List errors = new ArrayList();
		if (multi != null && multi.equals("1")) {
			// batch
			model.put("operation", "batch_unpublish");
			try {
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						String index_id = pData[i];
						Long batch_iid = new Long(index_id);
						ContentIndex ci = dynamicContentManager
								.getContentIndexById(batch_iid);
						if (ci.getState().intValue() == 1) {
							// need unpublish
							if (ci.getType().intValue() == 2) {
								// index link,only a state
								ci.setState(new Integer("0"));
								dynamicContentManager.saveContentIndex(ci);
							} else {
								Long nid = new Long(nodeId);
								publishEngine.unPublishContent(nid, batch_iid,
										errors);
							}
						}
					}
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
				model.put("result", "failed");
				model.put("msgs", ex1);
				return mv;
			}
			//
			if (errors.size() > 0) {
				model.put("result", "failed");
				model.put("msgs", errors);
			} else {
				model.put("result", "success");
			}
		} else {
			if (indexId != null) {
				try {
					Long iid = new Long(indexId);

					ContentIndex ci = dynamicContentManager
							.getContentIndexById(iid);
					if (ci != null) {
						Long nid = ci.getNodeId();
						if (ci.getType().intValue() == 2) {
							// index link,only a state
							ci.setState(new Integer("0"));
							dynamicContentManager.saveContentIndex(ci);
						} else {
							publishEngine.unPublishContent(nid, iid, errors);
						}
					}

					if (errors.size() > 0) {
						model.put("result", "failed");
						model.put("msgs", errors);
					} else {
						model.put("result", "success");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					errors.add(ex);
					model.put("result", "failed");
					model.put("msgs", errors);
				}
			}
		}
		return mv;
	}

	/**
	 * show the top setting dialog
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doTopDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		// helper.setLayout("layouts/none.html");
		model.put("noLayout", true);
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.SetTop)) {
			throw new UnauthorizedException();
		}
		//
		ModelAndView mv = new ModelAndView(topSettingViewName, model);
		if (indexId != null) {
			Long iid = new Long(indexId);
			ContentIndex ci = dynamicContentManager.getContentIndexById(iid);
			Integer top = ci.getTop();
			model.put("top", top);
			return mv;
		}
		return null;
	}

	public ModelAndView doTop(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		String weight = request.getParameter("weight");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.SetTop)) {
			throw new UnauthorizedException();
		}
		boolean success = false;
		try {
			if (indexId != null) {
				Long iid = new Long(indexId);
				ContentIndex ci = dynamicContentManager
						.getContentIndexById(iid);
				ci.setTop(new Integer(weight));
				dynamicContentManager.saveContentIndex(ci);
				// 分发内容更新事件
				ContentIndexEvent event = new ContentIndexEvent(
						ContentIndexEvent.CONTENT_UPDATED, ci, null, this);
				event.setAsynchronous(false);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
				// 内容已经发布，可能对调用内容区块有影响
				if (ci.getState().equals(ContentIndex.STATE_PUBLISHED)) {
					Map publish = new HashMap();
					publish.put("indexId", ci.getIndexId());
					publish.put("nodeId", new Long(nodeId));
					PublishEvent event2 = new PublishEvent(
							PublishEvent.CONTENT_FINISH_PUBLISH, publish, null,
							this);
					WebPluginManagerUtils.dispatcherEvent(true, "base", event2);
				}
				success = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doPinkDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		model.put("noLayout", true);
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		ModelAndView mv = new ModelAndView(pinkSettingViewName, model);
		if (indexId != null) {
			Long iid = new Long(indexId);
			ContentIndex ci = dynamicContentManager.getContentIndexById(iid);
			Integer pink = ci.getPink();
			model.put("pink", pink);
			return mv;
		}
		return null;
	}

	public ModelAndView doPink(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		String weight = request.getParameter("weight");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.ViewContent)) {
			throw new UnauthorizedException();
		}

		//
		boolean success = false;
		try {
			if (indexId != null) {
				Long iid = new Long(indexId);
				ContentIndex ci = dynamicContentManager
						.getContentIndexById(iid);
				ci.setPink(new Integer(weight));
				dynamicContentManager.saveContentIndex(ci);
				//
				// 分发内容更新事件
				ContentIndexEvent event = new ContentIndexEvent(
						ContentIndexEvent.CONTENT_UPDATED, ci, null, this);
				event.setAsynchronous(false);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
				//
				// 内容已经发布，可能对调用内容区块有影响
				if (ci.getState().equals(ContentIndex.STATE_PUBLISHED)) {
					Map publish = new HashMap();
					publish.put("indexId", ci.getIndexId());
					publish.put("nodeId", new Long(nodeId));
					PublishEvent event2 = new PublishEvent(
							PublishEvent.CONTENT_FINISH_PUBLISH, publish, null,
							this);
					WebPluginManagerUtils.dispatcherEvent(true, "base", event2);
				}
				success = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doSortDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String indexId = request.getParameter("indexId");
		String nodeId = helper.getString("nodeId", "0");
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.SetSort)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(sortSettingViewName, model);
		if (indexId != null) {
			Long iid = new Long(indexId);
			ContentIndex ci = dynamicContentManager.getContentIndexById(iid);
			Integer sort = ci.getSort();
			model.put("sort", sort);
			return mv;
		}
		return null;
	}

	public ModelAndView doSort(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		String weight = request.getParameter("weight");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.SetSort)) {
			throw new UnauthorizedException();
		}
		boolean success = false;
		try {
			if (indexId != null) {
				Long iid = new Long(indexId);
				ContentIndex ci = dynamicContentManager
						.getContentIndexById(iid);
				ci.setSort(new Integer(weight));
				dynamicContentManager.saveContentIndex(ci);
				//
				// 分发内容更新事件,用来更新索引
				ContentIndexEvent event = new ContentIndexEvent(
						ContentIndexEvent.CONTENT_UPDATED, ci, null, this);
				event.setAsynchronous(false);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
				//
				// 内容已经发布，可能对调用内容区块有影响
				if (ci.getState().equals(ContentIndex.STATE_PUBLISHED)) {
					Map publish = new HashMap();
					publish.put("indexId", ci.getIndexId());
					publish.put("nodeId", new Long(nodeId));
					PublishEvent event2 = new PublishEvent(
							PublishEvent.CONTENT_FINISH_PUBLISH, publish, null,
							this);
					WebPluginManagerUtils.dispatcherEvent(true, "base", event2);
				}
				//
				success = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doViewLinkState(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		//
		ModelAndView mv = new ModelAndView(linkStateViewName, model);
		//
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.ViewLink)) {
			throw new UnauthorizedException();
		}
		//

		if (indexId != null && nodeId != null) {
			Long iid = new Long(indexId);
			Long nid = new Long(nodeId);
			Node node = nodeManager.getNodeById(nid);
			if (node != null) {
				Long tid = node.getTableId();
				ContentField cf = contentFieldManager.getTitleField(tid);
				String titleFieldName = cf.getFieldName();
				ContentIndex ci = dynamicContentManager
						.getContentIndexById(iid);
				Object curretnLink = dynamicContentManager.getContent(iid, tid);
				List actualLinks = dynamicContentManager.getLinkList(ci
						.getContentId(), tid, new Integer("1"));
				List virtualLinks = dynamicContentManager.getLinkList(ci
						.getContentId(), tid, new Integer("0"));
				List indexLinks = dynamicContentManager.getLinkList(ci
						.getContentId(), tid, new Integer("2"));
				model.put("curretnLink", curretnLink);
				model.put("actualLinks", actualLinks);
				model.put("virtualLinks", virtualLinks);
				model.put("indexLinks", indexLinks);
				model.put("titleFieldName", titleFieldName);
				return mv;
			}
			//
		}
		return null;
	}

	public ModelAndView doPublishFileFormat(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(publishFileFormatViewName, model);
		return mv;
	}

	protected String getTemplateEncoding() {
		return getConfig().getString("sys.tpl.encoding", "UTF-8");
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setPublishXmlViewName(String publishXmlViewName) {
		this.publishXmlViewName = publishXmlViewName;
	}

	public void setContentHeaderViewName(String contentHeaderViewName) {
		this.contentHeaderViewName = contentHeaderViewName;
	}

	public void setContentFramesetViewName(String contentFramesetViewName) {
		this.contentFramesetViewName = contentFramesetViewName;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setContentOperationView(String contentOperationView) {
		this.contentOperationView = contentOperationView;
	}

	public void setContentListViewName(String contentListViewName) {
		this.contentListViewName = contentListViewName;
	}

	public void setPublishEngine(PublishEngine publishEngine) {
		this.publishEngine = publishEngine;
	}

	public void setRefreshSettingViewName(String refreshSettingViewName) {
		this.refreshSettingViewName = refreshSettingViewName;
	}

	public void setRecycleListViewName(String recycleListViewName) {
		this.recycleListViewName = recycleListViewName;
	}

	public void setSortSettingViewName(String sortSettingViewName) {
		this.sortSettingViewName = sortSettingViewName;
	}

	public void setTopSettingViewName(String topSettingViewName) {
		this.topSettingViewName = topSettingViewName;
	}

	public void setPublishSettingViewName(String publishSettingViewName) {
		this.publishSettingViewName = publishSettingViewName;
	}

	public void setPinkSettingViewName(String pinkSettingViewName) {
		this.pinkSettingViewName = pinkSettingViewName;
	}

	public void setCmsMacroEngine(CmsMacroEngine cmsMacroEngine) {
		this.cmsMacroEngine = cmsMacroEngine;
	}

	public void setContentViewName(String contentViewName) {
		this.contentViewName = contentViewName;
	}

	public void setLinkStateViewName(String linkStateViewName) {
		this.linkStateViewName = linkStateViewName;
	}

	public void setRepublishSettingViewName(String republishSettingViewName) {
		this.republishSettingViewName = republishSettingViewName;
	}

	public void setUnpublishSettingViewName(String unpublishSettingViewName) {
		this.unpublishSettingViewName = unpublishSettingViewName;
	}

	private void setStringValue(HttpServletRequest request, String fieldName,
			Map map) {
		String value = request.getParameter(fieldName);
		if (value == null) {
			value = "";
		}
		map.put(fieldName, value);
	}

	/**
	 * 设置整形的值
	 * 
	 * @param request
	 * 
	 * @param fieldName
	 *            String
	 * @param map
	 * 
	 * @param defaultValue
	 * 
	 */
	private void setIntegerValue(HttpServletRequest request, String fieldName,
			Map map, Integer defaultValue) {
		String value = request.getParameter(fieldName);
		if (value != null) {
			Integer ivalue = new Integer(value);
			map.put(fieldName, ivalue);
		} else {
			map.put(fieldName, defaultValue);
		}

	}

	public String getPhotoUrl(String photoUrl) {
		String destPhotoUrl = photoUrl;
		if (photoUrl.startsWith("../resource/")) {
			destPhotoUrl = photoUrl.substring(2);
		}
		return destPhotoUrl;
	}

	public void setContentListViewName2(String contentListViewName2) {
		this.contentListViewName2 = contentListViewName2;
	}
}
