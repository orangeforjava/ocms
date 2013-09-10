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

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.util.MultiResField;
import org.openuap.cms.cm.util.ResRefBean;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.security.NodeSecurityUtil;
import org.openuap.cms.publish.security.PublishPermissionConstant;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.resource.manager.ResourceManager;
import org.openuap.cms.resource.model.Resource;
import org.openuap.cms.resource.model.ResourceRef;
import org.openuap.cms.resource.model.ResourceRefKey;
import org.openuap.cms.stat.manager.CountManager;
import org.openuap.cms.stat.model.CmsCount;
import org.openuap.cms.util.file.PathNameStrategy;
import org.openuap.cms.util.file.impl.DatePathNameStrategy;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.util.ImageUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 发布内容编辑控制器
 * </p>
 * 
 * <p>
 * $Id: PublishEditAction.java 4039 2011-04-28 05:59:10Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishEditAction extends CMSBaseAction {

	/** 结点管理. */
	private NodeManager nodeManager;

	/** 内容模型域管理. */
	private ContentFieldManager contentFieldManager;

	/** 动态内容管理. */
	private DynamicContentManager dynamicContentManager;

	/** 资源管理. */
	private ResourceManager resourceManager;

	/** 内容点击统计管理. */
	private CountManager countManager;

	/** 内容发布引擎. */
	private PublishEngine publishEngine;

	/** 内容版本宏引擎. */
	private CmsMacroEngine cmsMacroEngine;

	/** 内容静态生成命名策略. */
	private PathNameStrategy pathNameStrategy;

	private String defaultScreensPath;

	private String contentEditorView;

	private String contentEditorFView;

	private String contentEditorHView;

	//
	private String contentEditorSaveView;

	//
	private String contentOperationView;

	public PublishEditAction() {
		initDefaultViewName();
	}

	public ModelAndView perform(HttpServletRequest arg0,
			HttpServletResponse arg1, ControllerHelper arg2, Map arg3)
			throws Exception {

		return null;
	}

	protected void initDefaultViewName() {
		this.defaultScreensPath = "/plugin/cms/base/screens/publish/";
		this.contentEditorView = defaultScreensPath
				+ "edit/publish_content_edit.html";
		this.contentEditorSaveView = defaultScreensPath
				+ "edit/publish_save.html";
		contentEditorFView = defaultScreensPath
				+ "edit/publish_content_edit_frameset.html";
		contentEditorHView = defaultScreensPath
				+ "edit/publish_content_edit_header.html";
		contentOperationView = defaultScreensPath
				+ "publish_operation_result.html";

	}

	/**
	 * 显示内容编辑页面
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
	 * @throws 若无权限
	 *             ，抛出未认证错误
	 */
	public ModelAndView doEdit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {

		ModelAndView mv = new ModelAndView(contentEditorView, model);
		//
		Map contentIndex = new HashMap();
		model.put("contentIndex", contentIndex);
		model.put("publishModes", PublishMode.DEFAULT_MODES);
		//
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		// 判断权限
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.EditContent)) {
			throw new UnauthorizedException();
		}
		String mode = request.getParameter("mode");
		if (mode != null && mode.equals("edit")) {
			// 编辑模式
			if (nodeId != null && indexId != null) {
				Long nid = new Long(nodeId);
				Long iid = new Long(indexId);
				//
				Node node = nodeManager.getNode(nid);
				Long tableId = node.getTableId();
				List cfs = contentFieldManager
						.getContentFieldsFromCache(tableId);
				Object cobj = dynamicContentManager.getContent(iid, tableId);
				//
				contentIndex = null;
				Map content = null;
				//
				if (cobj instanceof Object[]) {
					Object[] ct = (Object[]) cobj;
					contentIndex = (Map) ct[0];
					content = (Map) ct[1];
					//
					model.put("contentIndex", contentIndex);
					model.put("content", content);
				}
				//
				List imgRefList = resourceManager.getResourceByContentRef(nid,
						iid, "img");
				List flashRefList = resourceManager.getResourceByContentRef(
						nid, iid, "flash");
				List attachRefList = resourceManager.getResourceByContentRef(
						nid, iid, "attach");
				//
				model.put("mode", mode);
				model.put("indexId", indexId);
				model.put("cfs", cfs);
				model.put("node", node);
				model.put("nodeManager", nodeManager);
				long nows = 0l;
				Long ipd = (Long) contentIndex.get("publishDate");
				nows = ipd.longValue();
				model.put("now", new Date(nows));
				//
				model.put("imgRefList", imgRefList);
				model.put("flashRefList", flashRefList);
				model.put("attachRefList", attachRefList);
				model.put("cmsMacroEngine", cmsMacroEngine);
				//
				//
				return mv;
			}
		} else {
			// the "Add" mode
			if (nodeId != null) {

				Long id = new Long(nodeId);
				Node node = nodeManager.getNodeById(id);
				Long tableId = node.getTableId();
				List cfs = contentFieldManager
						.getContentFieldsFromCache(tableId);
				// 内容模型属性
				model.put("cfs", cfs);
				model.put("node", node);
				model.put("nodeManager", nodeManager);
				model.put("now", new Date(System.currentTimeMillis()));
				model.put("mode", "add");
				return mv;
			}
		}
		return null;
	}

	/**
	 * 保存内容
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
	public ModelAndView doSubmit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(contentEditorSaveView, model);
		// 结点Id
		String nodeId = request.getParameter("nodeId");
		// 索引Id
		String indexId = request.getParameter("indexId");
		String redirection = helper.getString("redirection", "edit");
		// 模式，新增或者保存
		String mode = request.getParameter("mode");
		// 权限验证
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.EditContent)) {
			throw new UnauthorizedException();
		}
		// 操作模式，编辑或添加
		if (mode == null) {
			mode = "add";
		}
		List errors = new ArrayList();
		if (nodeId != null) {
			//
			try {
				List resourceIds = new ArrayList();
				//
				Long nid = new Long(nodeId);
				Node node = nodeManager.getNode(nid);
				Long tableId = node.getTableId();
				List cfs = contentFieldManager
						.getContentFieldsFromCache(tableId);
				// 保存动态内容
				Map content = this.saveDynamicContent(request, response,
						helper, tableId, nid, cfs, resourceIds, mode);
				// 保存索引
				ContentIndex ci = this.saveContentIndex(request, response,
						helper, content, nid, tableId, mode);

				Long ciid = ci.getIndexId();
				if (mode.equals("add")) {
					ci.setParentIndexId(ciid);
					dynamicContentManager.saveContentIndex(ci);
				}
				//
				Long cid = (Long) content.get("contentId");
				saveCount(ciid, cid, nid, tableId, mode);
				//
				indexId = ciid.toString();
				// update the content_index,the
				// add the resource refrence
				// 1,first delete all resource ref
				dynamicContentManager.deleteResourceRefByNodeIndexId(nid, ciid);
				// 2,second add the ref
				if (resourceIds.size() > 0) {
					for (int i = 0; i < resourceIds.size(); i++) {
						Long rsid = (Long) resourceIds.get(i);
						ResourceRefKey key = new ResourceRefKey();
						key.setIndexId(ciid);
						key.setNodeId(nid);
						key.setResourceId(rsid);
						ResourceRef rsRef = new ResourceRef(key);
						dynamicContentManager.addResourceRef(rsRef);
					}
				}
				//
				if (mode.equals("add")) {
					Integer autoPublish = node.getAutoPublish();
					if (autoPublish.intValue() == 1) {
						publishEngine.publishContent(nid, ciid, true, errors);
						if (errors.size() > 0) {
							publishEngine.unPublishContent(nid, ciid, errors);
						}
					}
				} else {
					// if has published
					if (ci.getState().intValue() == 1) {
						publishEngine.unPublishContent(nid, ciid, errors);
						publishEngine.publishContent(nid, ciid, true, errors);
						if (errors.size() > 0) {
							model.put("save_result", "failed");
							model.put("msgs", errors);
						} else {
							model.put("save_result", "success");
						}
					}

				}

				model.put("save_result", "success");

				// submit
				// if publish automatic
				// insert the publish_? table
				// create the static file
			} catch (Exception ex) {
				ex.printStackTrace();
				errors.add(ex);
				model.put("save_result", "failed");
				model.put("msgs", errors);
			}
			model.put("redirection", redirection);
			model.put("nodeId", nodeId);
			model.put("indexId", indexId);
			model.put("mode", mode);
			return mv;
		}
		return null;
	}

	/**
	 * 内容编辑的Frameset窗口
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
	public ModelAndView doContentEditorFrameset(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {

		String nodeId = request.getParameter("nodeId");
		String mode = request.getParameter("mode");
		String indexId = request.getParameter("indexId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.EditContent)) {
			throw new UnauthorizedException();
		}
		//
		if (mode == null) {
			mode = "add";
		}
		if (mode.equals("edit")) {
			if (indexId != null) {
				if (nodeId == null) {
					ContentIndex ci = dynamicContentManager
							.getContentIndexById(new Long(indexId));
					if (ci != null) {
						nodeId = ci.getNodeId().toString();
					}
				}
			}
		}
		if (nodeId != null) {

			ModelAndView mv = new ModelAndView(contentEditorFView, model);
			model.put("nodeId", nodeId);
			model.put("mode", mode);
			model.put("indexId", indexId);
			return mv;
		}
		return null;
	}

	/**
	 * 内容编辑头部窗口
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
	public ModelAndView doContentEditorHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		// 权限验证
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.EditContent)) {
			throw new UnauthorizedException();
		}
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(contentEditorHView, model);
			Long id = new Long(nodeId);
			Node node = nodeManager.getNode(id);
			List cfs = contentFieldManager.getContentFieldsFromCache(node
					.getTableId());
			model.put("node", node);
			model.put("nodeManager", nodeManager);
			model.put("cfs", cfs);

			return mv;
		}
		return null;
	}

	/**
	 * the del operation should put it to recycle,not delete! delete content
	 * index 把内容放入回收站
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
	public ModelAndView doDel(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		//
		model.put("operation", "del");
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		//
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.CutContent)) {
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
			model.put("operation", "batch_del");
			try {
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						String index_id = pData[i];
						Long batch_iid = new Long(index_id);
						ContentIndex ci = dynamicContentManager
								.getContentIndexById(batch_iid);
						// -1 is put in recycle state
						if (ci.getState().intValue() == 1) {
							// 内容反发布
							publishEngine.unPublishContent(ci.getNodeId(),
									batch_iid, errors);
						}
						// 设置删除状态
						ci.setState(ContentIndex.STATE_DELETED);
						dynamicContentManager.saveContentIndex(ci);
					}
				}
				//
				if (errors.size() > 0) {
					model.put("result", "failed");
					model.put("msgs", errors);
				} else {
					model.put("result", "success");
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
				errors.add(ex1);
				model.put("result", "failed");
				model.put("msgs", errors);
			}
		} else {
			//
			if (nodeId != null && indexId != null) {
				try {
					Long iid = new Long(indexId);
					// 1)update the contentIndex,set state to -1
					ContentIndex ci = dynamicContentManager
							.getContentIndexById(iid);
					// -1 is put in recycle state
					if (ci.getState().intValue() == 1) {
						publishEngine.unPublishContent(ci.getNodeId(), iid,
								errors);

					}
					// 设置删除状态
					ci.setState(ContentIndex.STATE_DELETED);
					dynamicContentManager.saveContentIndex(ci);
					//
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
	 * 永久删除
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
	 * @throws IOException
	 * @throws UnauthorizedException
	 */
	public ModelAndView doDestroy(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		// ModelAndView mv = new ModelAndView(contentOperationView, model);
		//
		String nodeId = request.getParameter("nodeId");
		model.put("nodeId", nodeId);
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.EmptyRecycleBin)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		String indexId = request.getParameter("indexId");
		//
		String multi = request.getParameter("multi");
		String[] pData = request.getParameterValues("pData");

		boolean success = true;
		if (multi != null && multi.equals("1")) {
			List errors = new ArrayList();
			try {
				model.put("operation", "batch_destroy");
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						String index_id = pData[i];
						Long batch_iid = new Long(index_id);
						Long nid = new Long(nodeId);
						// Node node = nodeManager.getNodeById(nid);
						// Long tableId = node.getTableId();
						ContentIndex ci = dynamicContentManager
								.getContentIndexById(batch_iid);

						//
						if (ci != null) {
							// 1)delete the
							// contentIndex(contentIndex,content_?,publish_?<if
							// has pulished>)
							// 2)delete the publish html(if has pulished)
							// 3)delete the resource refrence
							dynamicContentManager
									.deleteResourceRefByNodeIndexId(nid,
											batch_iid);
							dynamicContentManager.deleteContentIndex(batch_iid,
									ci.getTableId(), ci.getType().intValue());
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
					// List errors = new ArrayList();
					ContentIndex ci = dynamicContentManager
							.getContentIndexById(iid);
					if (ci != null) {
						// 1)delete the
						// contentIndex(contentIndex,content_?,publish_?<if has
						// pulished>)
						// 2)delete the publish html(if has pulished)
						// 3)delete the resource refrence
						dynamicContentManager.deleteResourceRefByNodeIndexId(
								nid, iid);
						dynamicContentManager.deleteContentIndex(iid, ci
								.getTableId(), ci.getType().intValue());
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				//
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
		return mv;
	}

	/**
	 * 
	 * 从回收站中恢复
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
	 * @throws IOException
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRestore(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.Restore)) {
			throw new UnauthorizedException();
		}

		boolean success = true;
		if (nodeId != null && indexId != null) {
			try {
				Long iid = new Long(indexId);
				// 1)update the contentIndex,set state to 0
				ContentIndex ci = dynamicContentManager
						.getContentIndexById(iid);
				// 0 is put in recycle state
				ci.setState(new Integer("0"));
				dynamicContentManager.saveContentIndex(ci);

			} catch (Exception ex) {
				success = false;
			}
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
	 * @throws UnauthorizedException
	 */
	public ModelAndView doCreateLink(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		//
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		model.put("operation", "createLink");
		//
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		String targetNodeId = request.getParameter("targetNodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.CreateLink)) {
			throw new UnauthorizedException();
		}
		//
		model.put("indexId", indexId);
		model.put("nodeId", nodeId);
		model.put("targetNodeId", targetNodeId);
		//
		try {
			if (indexId != null && nodeId != null && targetNodeId != null) {
				// compare the nodeId and the tartgetNodeId
				// must have the same model
				Long iid = new Long(indexId);
				Long nid = new Long(nodeId);
				Long tnid = new Long(targetNodeId);
				//
				Node node = nodeManager.getNodeById(nid);
				Node tnode = nodeManager.getNodeById(tnid);
				//
				Long tableId = node.getTableId();
				Long targetTableId = tnode.getTableId();
				//
				if (!tableId.equals(targetTableId)) {
					model.put("result", "failed");
					model.put("msgs", "不能在不同的内容模型之间建立虚链接！");
					return mv;
				}
				//
				ContentIndex ci = this.dynamicContentManager
						.getContentIndexById(iid);
				//
				ContentIndex new_ci = new ContentIndex();
				new_ci.setContentId(ci.getContentId());
				new_ci.setNodeId(tnid);
				new_ci.setType(new Integer("0"));
				new_ci.setTableId(tableId);
				//
				long pd = System.currentTimeMillis();
				long publishDate = pd / 1000L;
				new_ci.setPublishDate(new Long(pd));
				//
				new_ci.setState(new Integer("0"));
				new_ci.init();
				Long new_iid = dynamicContentManager.addContentIndex(new_ci);
				new_ci.setIndexId(new_iid);
				new_ci.setParentIndexId(new_iid);
				dynamicContentManager.saveContentIndex(new_ci);
				//
				saveCount(new_iid, ci.getContentId(), nid, tableId, "add");
				// may be need publish!
				Integer autoPublish = tnode.getAutoPublish();
				if (autoPublish.equals(new Integer("1"))) {
					List errors = new ArrayList();
					publishEngine.publishContent(nid, new_iid, true, errors);
					if (errors.size() > 0) {
						model.put("msgs", "产生虚链接出现错误.");
					}
				}
				model.put("result", "success");
			}
		} catch (Exception ex) {
			model.put("result", "failed");
			model.put("msgs", "建立虚链接出现意外错误：" + ex.getMessage());
		}
		return mv;
	}

	/**
	 * 
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
	 * @throws UnauthorizedException
	 */
	public ModelAndView doCreateIndexLink(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		model.put("operation", "createIndexLink");
		//
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		String targetNodeId = request.getParameter("targetNodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.CreateIndexLink)) {
			throw new UnauthorizedException();
		}
		model.put("indexId", indexId);
		model.put("nodeId", nodeId);
		model.put("targetNodeId", targetNodeId);
		//
		try {
			if (indexId != null && nodeId != null && targetNodeId != null) {
				// compare the nodeId and the tartgetNodeId
				// must have the same model
				Long iid = new Long(indexId);
				Long nid = new Long(nodeId);
				Long tnid = new Long(targetNodeId);
				ContentIndex ci = this.dynamicContentManager
						.getContentIndexById(iid);
				//
				Node node = nodeManager.getNodeById(nid);
				Long tableId = node.getTableId();
				//
				ContentIndex new_ci = new ContentIndex();
				new_ci.setContentId(ci.getContentId());
				new_ci.setNodeId(tnid);
				new_ci.setType(new Integer("2"));
				new_ci.setTableId(tableId);
				//
				long pd = System.currentTimeMillis();
				long publishDate = pd / 1000L;
				new_ci.setPublishDate(new Long(pd));
				//
				//
				new_ci.setState(new Integer("0"));
				new_ci.init();
				new_ci.setParentIndexId(iid);
				Long new_iid = dynamicContentManager.addContentIndex(new_ci);
			}
			model.put("result", "success");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("result", "failed");
			model.put("msgs", "产生索引链接出现意外错误：" + ex.getMessage());
		}
		return mv;
	}

	/**
	 * 清空回收站
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
	public ModelAndView doEmptyRecycle(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		model.put("nodeId", nodeId);
		model.put("operation", "empty");
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		if (nodeId != null) {
			//
			if (!NodeSecurityUtil.hasPermission(
					PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
					PublishPermissionConstant.EmptyRecycleBin)) {
				throw new UnauthorizedException();
			}
			try {
				Long nid = new Long(nodeId);
				Node node = nodeManager.getNode(nid);
				if (node != null) {
					Long tid = node.getTableId();
					List ciList = dynamicContentManager.getRecycleContents(nid);
					for (int i = 0; i < ciList.size(); i++) {
						ContentIndex ci = (ContentIndex) ciList.get(i);
						//
						dynamicContentManager.deleteResourceRefByNodeIndexId(
								nid, ci.getIndexId());
						dynamicContentManager.deleteContentIndex(ci
								.getIndexId(), ci.getTableId(), ci.getType()
								.intValue());
					}
				}
				model.put("result", "success");
			} catch (Exception ex) {
				ex.printStackTrace();
				model.put("result", "failed");
				model.put("msgs", ex);
			}
		}
		return mv;
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
	public ModelAndView doCopy(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		model.put("operation", "copy");
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.CopyContent)) {
			throw new UnauthorizedException();
		}
		//
		String targetNodeId = request.getParameter("targetNodeId");
		//
		String multi = request.getParameter("multi");
		String[] pData = request.getParameterValues("pData");
		//
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		//
		List errors = new ArrayList();
		if (multi != null && multi.equals("1")) {
			// batch
			model.put("operation", "batch_copy");
			Long nid = new Long(nodeId);
			Long tnid = new Long(targetNodeId);
			Node srcNode = nodeManager.getNodeById(nid);
			Node destNode = nodeManager.getNodeById(tnid);
			if (srcNode != null && destNode != null) {
				if (!srcNode.getTableId().equals(destNode.getTableId())) {
					model.put("result", "failed");
					model.put("msgs", "不能在不同的内容模型之间复制内容!");
					return mv;
				}
			} else {
				model.put("result", "failed");
				model.put("msgs", "您选择正确的源结点与目标结点!");
				return mv;
			}
			//
			try {
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						long now = System.currentTimeMillis();
						String index_id = pData[i];
						Long batch_iid = new Long(index_id);
						ContentIndex old_ci = dynamicContentManager
								.getContentIndexById(batch_iid);
						ContentIndex ci = new ContentIndex();
						ci.setContentId(old_ci.getContentId());
						ci.setNodeId(tnid);
						ci.setParentIndexId(old_ci.getParentIndexId());
						ci.setPink(new Integer("0"));
						ci.setPublishDate(new Long(System.currentTimeMillis()));
						// ci.setResourceSet(old_ci.getResourceSet());
						ci.setSelfPsn(old_ci.getSelfPsn());
						ci.setSelfPsnUrl(old_ci.getSelfPsnUrl());
						ci.setSelfPublishFileName(old_ci
								.getSelfPublishFileName());
						ci.setSelfTemplate(old_ci.getSelfTemplate());
						ci.setSelfUrl(old_ci.getSelfUrl());
						ci.setUrl("");
						ci.setSort(new Integer("0"));
						ci.setState(new Integer("0"));
						ci.setTop(new Integer("0"));
						ci.setType(new Integer("1"));
						// FIX:SOME MUST NOT BE NULL PROPS
						ci.setHitsToday(old_ci.getHitsToday());
						ci.setHitsTotal(old_ci.getHitsTotal());
						ci.setHitsWeek(old_ci.getHitsWeek());
						ci.setHitsMonth(old_ci.getHitsMonth());
						ci.setHitsDate(old_ci.getHitsDate());
						ci.setCommentNum(old_ci.getCommentNum());
						ci.setDits(old_ci.getDits());
						ci.setDowns(old_ci.getDowns());
						ci.setLastModifiedDate(now);
						ci.setContentTitle(old_ci.getContentTitle());
						ci.setContentPhoto(old_ci.getContentPhoto());
						ci.setContentPortalUrl(old_ci.getContentPortalUrl());
						ci.setCreationUserName(old_ci.getCreationUserName());
						// END FIX
						Long new_index_id = dynamicContentManager
								.addContentIndex(ci);
						ContentIndex new_index = dynamicContentManager
								.getContentIndexById(new_index_id);
						// update the parentIndexId
						new_index.setParentIndexId(new_index_id);
						dynamicContentManager.saveContentIndex(new_index);
						// copy the resource ref
						List rsRefList = dynamicContentManager
								.getResourceRefByNodeIndexId(nid, batch_iid);
						if (rsRefList != null) {
							for (int j = 0; j < rsRefList.size(); j++) {
								ResourceRef rr = (ResourceRef) rsRefList.get(j);
								ResourceRefKey rkey = new ResourceRefKey();
								rkey.setIndexId(new_index_id);
								rkey.setNodeId(tnid);
								rkey.setResourceId(rr.getResourceId());
								ResourceRef rr_new = new ResourceRef(rkey);
								dynamicContentManager.addResourceRef(rr_new);
							}
						}

					}
				}
				if (errors.size() == 0) {
					model.put("result", "success");
					model.put("msgs", "成功复制内容！");
					return mv;
				} else {
					model.put("result", "failed");
					model.put("msgs", "复制内容失败!");
					return mv;
				}

			} catch (Exception ex1) {
				ex1.printStackTrace();
				model.put("result", "failed");
				model.put("msgs", "复制内容出现意外错误:" + ex1.getMessage());
				return mv;
			}
		} else {
			// 1)check the node table is same,must same!
			if (nodeId != null && targetNodeId != null && indexId != null) {
				try {
					long now = System.currentTimeMillis();
					Long nid = new Long(nodeId);
					Long tnid = new Long(targetNodeId);
					Long iid = new Long(indexId);
					Node srcNode = nodeManager.getNodeById(nid);
					Node destNode = nodeManager.getNodeById(tnid);
					if (srcNode != null && destNode != null) {
						if (!srcNode.getTableId().equals(destNode.getTableId())) {
							model.put("result", "failed");
							model.put("msgs", "不能在不同的内容模型之间复制内容!");
							return mv;
						}
					} else {
						model.put("result", "failed");
						model.put("msgs", "您选择正确的源结点与目标结点!");
						return mv;
					}

					// 2)new the contentIndex,set the nodeId
					ContentIndex old_ci = dynamicContentManager
							.getContentIndexById(iid);
					ContentIndex ci = new ContentIndex();
					ci.setContentId(old_ci.getContentId());
					ci.setNodeId(tnid);
					ci.setParentIndexId(old_ci.getParentIndexId());
					ci.setPink(new Integer("0"));
					ci.setPublishDate(new Long(System.currentTimeMillis()));
					// ci.setResourceSet(old_ci.getResourceSet());
					ci.setSelfPsn(old_ci.getSelfPsn());
					ci.setSelfPsnUrl(old_ci.getSelfPsnUrl());
					ci.setSelfPublishFileName(old_ci.getSelfPublishFileName());
					ci.setSelfTemplate(old_ci.getSelfTemplate());
					ci.setSelfUrl(old_ci.getSelfUrl());
					ci.setUrl("");
					ci.setSort(new Integer("0"));
					ci.setState(new Integer("0"));
					ci.setTop(new Integer("0"));
					ci.setType(new Integer("1"));
					// FIX:SOME MUST NOT BE NULL PROPS
					ci.setHitsToday(old_ci.getHitsToday());
					ci.setHitsTotal(old_ci.getHitsTotal());
					ci.setHitsWeek(old_ci.getHitsWeek());
					ci.setHitsMonth(old_ci.getHitsMonth());
					ci.setHitsDate(old_ci.getHitsDate());
					ci.setCommentNum(old_ci.getCommentNum());
					ci.setDits(old_ci.getDits());
					ci.setDowns(old_ci.getDowns());
					ci.setLastModifiedDate(now);
					//
					ci.setContentTitle(old_ci.getContentTitle());
					ci.setContentPhoto(old_ci.getContentPhoto());
					ci.setContentPortalUrl(old_ci.getContentPortalUrl());
					ci.setCreationUserName(old_ci.getCreationUserName());
					// END FIX
					Long new_index_id = dynamicContentManager
							.addContentIndex(ci);
					ContentIndex new_index = dynamicContentManager
							.getContentIndexById(new_index_id);
					// update the parentIndexId
					new_index.setParentIndexId(new_index_id);
					dynamicContentManager.saveContentIndex(new_index);
					// copy the resource ref
					List rsRefList = dynamicContentManager
							.getResourceRefByNodeIndexId(nid, iid);
					if (rsRefList != null) {
						for (int i = 0; i < rsRefList.size(); i++) {
							ResourceRef rr = (ResourceRef) rsRefList.get(i);
							ResourceRefKey rkey = new ResourceRefKey();
							rkey.setIndexId(new_index_id);
							rkey.setNodeId(tnid);
							rkey.setResourceId(rr.getResourceId());
							ResourceRef rr_new = new ResourceRef(rkey);
							dynamicContentManager.addResourceRef(rr_new);
						}
					}
					if (errors.size() == 0) {

						model.put("result", "success");
						model.put("msgs", "复制内容成功!");
					} else {
						model.put("result", "failed");
						model.put("msgs", "复制内容失败!");
						return mv;
					}
				} catch (Exception ex) {
					model.put("result", "failed");
					model.put("msgs", "复制内容出现意外错误:" + ex.getMessage());
					return mv;
				}
			}
		}
		return mv;
	}

	/**
	 * 内容剪切
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doCut(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(contentOperationView, model);
		model.put("operation", "cut");
		String indexId = request.getParameter("indexId");
		String nodeId = request.getParameter("nodeId");
		String targetNodeId = request.getParameter("targetNodeId");
		//
		String multi = request.getParameter("multi");
		String[] pData = request.getParameterValues("pData");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.CutContent)) {
			throw new UnauthorizedException();
		}

		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		//
		List errors = new ArrayList();
		//
		if (multi != null && multi.equals("1")) {
			// batch
			model.put("operation", "batch_cut");
			try {
				Long nid = new Long(nodeId);
				Long tnid = new Long(targetNodeId);
				Node srcNode = nodeManager.getNodeById(nid);
				Node destNode = nodeManager.getNodeById(tnid);
				if (srcNode != null && destNode != null) {
					if (!srcNode.getTableId().equals(destNode.getTableId())) {
						model.put("result", "failed");
						model.put("msgs", "您不能在不同的内容模型之间剪切内容!");
						return mv;
					}
				} else {
					model.put("result", "faled");
					model.put("msgs", "您选择正确的源结点与目标结点!");
					return mv;
				}
				//
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						String index_id = pData[i];
						Long batch_iid = new Long(index_id);
						// 2)unpublish the index content
						publishEngine.unPublishContent(nid, batch_iid, errors);
						// 3)update the nodeId
						//
						ContentIndex ci = dynamicContentManager
								.getContentIndexById(batch_iid);
						ci.setNodeId(tnid);
						dynamicContentManager.saveContentIndex(ci);
						// cut the resource ref
						List rsRefList = dynamicContentManager
								.getResourceRefByNodeIndexId(nid, batch_iid);
						if (rsRefList != null) {
							for (int j = 0; j < rsRefList.size(); j++) {
								ResourceRef rr = (ResourceRef) rsRefList.get(j);
								ResourceRefKey rkey = new ResourceRefKey();
								rkey.setIndexId(batch_iid);
								rkey.setNodeId(tnid);
								rkey.setResourceId(rr.getResourceId());
								ResourceRef rr_new = new ResourceRef(rkey);
								dynamicContentManager.saveResourceRef(rr_new);
							}
						}
					}
				}
				if (errors.size() == 0) {
					model.put("result", "success");
					model.put("msgs", "成功剪切内容！");
				} else {
					model.put("result", "failed");
					model.put("msgs", "剪切内容失败！");
				}

			} catch (Exception ex1) {
				model.put("result", "failed");
				model.put("msgs", "剪切内容出现意外错误:" + ex1.getMessage());
				return mv;
			}
		} else {
			// 1)check the node table is same,must same!
			if (nodeId != null && targetNodeId != null && indexId != null) {
				try {
					Long nid = new Long(nodeId);
					Long tnid = new Long(targetNodeId);
					Long iid = new Long(indexId);
					Node srcNode = nodeManager.getNodeById(nid);
					Node destNode = nodeManager.getNodeById(tnid);
					if (srcNode != null && destNode != null) {
						if (!srcNode.getTableId().equals(destNode.getTableId())) {
							model.put("result", "failed");
							model.put("msgs", "您不能在不同的内容模型之间剪切内容!");
							return mv;
						}
					} else {
						model.put("result", "failed");
						model.put("msgs", "您选择正确的源结点与目标结点!");
						return mv;
					}
					// 2)unpublish the index content
					publishEngine.unPublishContent(nid, iid, errors);
					// 3)update the nodeId
					if (errors.size() == 0) {
						ContentIndex ci = dynamicContentManager
								.getContentIndexById(iid);
						ci.setNodeId(tnid);
						dynamicContentManager.saveContentIndex(ci);
						// cut the resource ref
						List rsRefList = dynamicContentManager
								.getResourceRefByNodeIndexId(nid, iid);
						if (rsRefList != null) {
							for (int i = 0; i < rsRefList.size(); i++) {
								ResourceRef rr = (ResourceRef) rsRefList.get(i);
								ResourceRefKey rkey = new ResourceRefKey();
								rkey.setIndexId(iid);
								rkey.setNodeId(tnid);
								rkey.setResourceId(rr.getResourceId());
								ResourceRef rr_new = new ResourceRef(rkey);
								dynamicContentManager.saveResourceRef(rr_new);
							}
						}

						model.put("result", "success");
						model.put("msgs", "成功剪切内容！");
					} else {
						model.put("result", "failed");
						model.put("msgs", "剪切内容失败!");
						return mv;
					}
				} catch (Exception ex) {
					model.put("result", "failed");
					model.put("msgs", "剪切内容出现意外错误:" + ex.getMessage());
					return mv;
				}
			}
		}
		return mv;
	}

	// //
	/**
	 * save the dynamic content of content-model and return the created content
	 * primary-key
	 * <ul>
	 * <li>1)get every field,see the type</li>
	 * <li>2)if be WebEditor,will decide the remote image/flash/file auto local</li>
	 * <li>3)get the resource and save the resource,add the reference</li>
	 * <li>4)replace the WebEditor content,such as:http://-->../</li>
	 * </ul>
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param tableId
	 * 
	 * @param nodeId
	 * 
	 * @param cfs
	 * 
	 * @param resourceIds
	 * 
	 * @param mode
	 * 
	 * @return the content primary-key
	 * @throws Exception
	 */
	protected Map saveDynamicContent(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper,
			Long tableId, Long nodeId, List cfs, List resourceIds, String mode)
			throws Exception {

		//
		CMSConfig cfg = CMSConfig.getInstance();
		String imgExtensions = cfg.getUploadFileImageType();
		String flashExtensions = cfg.getUploadFileFlashType();
		String attachExtensions = cfg.getUploadFileAttachType();
		// 规定允许上传的附件类型
		String sAllowExt = imgExtensions + "|" + flashExtensions + "|"
				+ attachExtensions;

		// Map contentIndex = null;
		Map content = null;
		boolean edit = false;
		// 编辑状态返回原内容
		if (mode.equals("edit")) {
			edit = true;
			String indexId = request.getParameter("indexId");
			Long iid = new Long(indexId);
			Object cobj = dynamicContentManager.getContent(iid, tableId);
			if (cobj instanceof Object[]) {
				Object[] ct = (Object[]) cobj;
				// contentIndex = (Map) ct[0];
				content = (Map) ct[1];
			}
		}
		if (mode.equals("add")) {
			content = new HashMap();
		}
		// some fixed content field
		// 1,creationDate
		long now = System.currentTimeMillis();
		// now = now / 1000;
		Long creationDate = new Long(now);
		if (!edit) {
			content.put("creationDate", creationDate);
		}
		// 2,modifiedDate,as add,it equal creationDate
		content.put("modifiedDate", creationDate);
		// 3,creationUserId
		if (!edit) {
			content.put("creationUserId", this.getUser().getUserId());
			content.put("creationUserName", this.getUser().getName());
		}
		// 4,lastModifiedUserId,as add,it equal lastModifiedUserId
		content.put("lastModifiedUserId", this.getUser().getUserId());
		content.put("lastModifiedUserName", this.getUser().getName());
		// 保存投稿信息
		if (!edit) {
			// 5,contributionUserId,as default,it will be 0
			content.put("contributionUserId", new Long(0));
			// 6,contributionId,as default,it will be 0
			content.put("contributionId", new Long(0));
		}

		for (int i = 0; i < cfs.size(); i++) {
			ContentField cf = (ContentField) cfs.get(i);
			String type = cf.getFieldType();
			String input = cf.getFieldInput();
			String inputPicker = cf.getFieldInputPicker();
			Integer enableStatics = cf.getEnableStatics();
			// 如果是统计字段则不做处理
			if (enableStatics != null && enableStatics.equals(new Integer(1))) {
				continue;
			}
			//
			if (inputPicker == null) {
				inputPicker = "";
			}
			String name = cf.getFieldName();
			String raw_value = request.getParameter("data_" + name);
			Object value = raw_value;
			if (type.equals("varchar")) {
				// text
				if (inputPicker.equals("upload")
						|| inputPicker.equals("upload_attach")
						|| inputPicker.equals("flash")) {
					// 处理资源上传
					//
					String sp = "\\.\\.\\/resource\\/((img|flash|attach)(\\/\\w+)+\\.("
							+ sAllowExt + "))";
					Pattern p = Pattern.compile(sp);
					Matcher m = p.matcher(raw_value);
					boolean found = m.find();
					while (found) {
						String path = m.group(1);
						Resource rs = resourceManager.getResourceByPath(path);
						// if not find?
						if (rs != null) {
							Long rsid = rs.getResourceId();
							if (!resourceIds.contains(rsid)) {
								resourceIds.add(rsid);
							}
						}
						found = m.find();
					} // end while
				}

			} else if (type.equals("integer")) {
				// integer
				value = new Integer(raw_value);
			} else if (type.equals("float")) {
				// float
				value = new Float(raw_value);
			} else if (type.equals("text") || type.equals("contentlink")) {
				// text,as string
				if (input.equals("RichEditor")) {
					// List rsIds = new ArrayList();
					value = processWebEditorContent(request, raw_value, nodeId,
							resourceIds, edit);
				}
				if (inputPicker.equals("content")) {
					value = request.getParameter("data_" + name + "_value");
					if (value.equals("undefined")) {
						value = "";
					}
				}
				if (input.equals("MultiImg")) {
					// 多图字段
					value = raw_value;
					processMultiImg(raw_value, resourceIds, sAllowExt);
				}
			}
			//
			content.put(name, value);
		}
		//
		if (edit) {
			dynamicContentManager.saveContent(tableId, content);
			return content;
		} else {
			Long contentId = dynamicContentManager.addContent(tableId, content);
			content.put("contentId", contentId);
			return content;
		}
	}

	/**
	 * 保存内容索引
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param contentId
	 * 
	 * @param nodeId
	 * 
	 * @param tableId
	 * 
	 * @param mode
	 * 
	 * @return
	 */
	protected ContentIndex saveContentIndex(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map content,
			Long nodeId, Long tableId, String mode) {
		//
		String indexId = request.getParameter("indexId");

		//
		Long iid = null;
		ContentIndex ci = null;
		if (mode.equals("edit")) {
			// 编辑模式
			if (indexId != null) {
				iid = new Long(indexId);
				ci = dynamicContentManager.getContentIndexById(iid);
			}
		} else {
			ci = new ContentIndex();
		}
		Long contentId = (Long) content.get("contentId");
		// 1,contentId
		ci.setContentId(contentId);

		// 2,nodeId
		ci.setNodeId(nodeId);

		// 3,parentIndexId,now will be 0
		if (mode.equals("add")) {
			ci.setTableId(tableId);
			ci.setParentIndexId(new Long(0));

		}

		// 4,type,as a add,will be 1,
		if (mode.equals("edit")) {
			// ci.put("type", old_ci.getType());
		} else {
			// ci.put("type", new Integer("1"));
			ci.setType(new Integer(1));
		}

		// 5,publishDate,form year,hour,minute,second
		String date = request.getParameter("date");
		String hour = request.getParameter("hour");
		String minute = request.getParameter("minute");
		String second = request.getParameter("second");
		String[] ymd = date.split("\\-");
		Calendar cal = Calendar.getInstance();
		// 清除毫秒部分，修正每次编辑带来的发布时间变化Bug，JIRA#CMS-11
		cal.clear();
		cal.set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]) - 1, Integer
				.parseInt(ymd[2]), Integer.parseInt(hour), Integer
				.parseInt(minute), Integer.parseInt(second));
		//
		long pd = cal.getTimeInMillis();
		//
		ci.setPublishDate(new Long(pd));
		// 6,selfTemplate
		String selfTemplate = helper.getString("selfTemplate", "");
		ci.setSelfTemplate(selfTemplate);
		// 7,selfPsn
		String selfPsn = helper.getString("selfPsn", "");
		ci.setSelfPsn(selfPsn);
		// 8,selfPublishFileName
		String selfPublishFileName = helper
				.getString("selfPublishFileName", "");
		ci.setSelfPublishFileName(selfPublishFileName);
		// 9,selfPsnUrl
		String selfPsnUrl = helper.getString("selfPsnUrl", "");
		ci.setSelfPsnUrl(selfPsnUrl);
		// 10,selfUrl
		String selfUrl = helper.getString("selfUrl", "");
		ci.setSelfUrl(selfUrl);
		// 11,state,now,will be 0
		if (!mode.equals("edit")) {
			ci.setState(new Integer(0));
		} else {
		}

		// 12,url,now will be empty
		ci.setUrl("");
		Integer top = helper.getInt("top", 0);
		Integer pink = helper.getInt("pink", 0);
		Integer sort = helper.getInt("sort", 0);

		// 13,top,now will be 0
		ci.setTop(top);
		// 14,pink
		ci.setPink(pink);
		// 15,sort
		ci.setSort(sort);

		ci.setLastModifiedDate(System.currentTimeMillis());
		// 16,permission
		String permission = helper.getString("permission", "");
		ci.setPermission(permission);
		// 17,publishMode
		String publishMode = helper.getString("publishMode", "-1");

		Integer pm = new Integer(publishMode);
		ci.setPublishMode(pm);
		// 18,contentPortalUrl
		String contentPortalUrl = helper.getString("contentPortalUrl", "");
		ci.setContentPortalUrl(contentPortalUrl);
		// 保存提速冗余数据
		ContentField titleField = contentFieldManager.getTitleField(tableId);
		ContentField photoField = contentFieldManager
				.getPhotoFieldFromCache(tableId);

		if (titleField != null) {
			String titleFieldName = titleField.getFieldName();
			String contentTitle = (String) content.get(titleFieldName);
			ci.setContentTitle(contentTitle);
		}
		if (photoField != null) {
			String photoFieldName = photoField.getFieldName();
			String contentPhoto = (String) content.get(photoFieldName);
			ci.setContentPhoto(contentPhoto);
		}
		//
		String creationUserName = (String) content.get("creationUserName");
		ci.setCreationUserName(creationUserName);
		//
		ci.setHitsToday(0L);
		ci.setHitsTotal(0L);
		ci.setHitsWeek(0L);
		ci.setHitsMonth(0L);
		ci.setHitsDate(0L);
		ci.setCommentNum(0L);
		ci.setDits(0L);
		ci.setDowns(0L);
		//
		if (mode.equals("edit")) {
			dynamicContentManager.saveContentIndex(ci);
			return ci;
		} else {
			Long id = dynamicContentManager.addContentIndex(ci);
			ci.setIndexId(id);
			return ci;
		}
	}

	/**
	 * 保存内容计数
	 * 
	 * @param indexId
	 * 
	 * @param contentId
	 * 
	 * @param nodeId
	 * 
	 * @param tableId
	 * 
	 * @param mode
	 * 
	 */
	protected void saveCount(Long indexId, Long contentId, Long nodeId,
			Long tableId, String mode) {
		CmsCount count = countManager.getCountById(indexId);
		if (count == null) {
			count = new CmsCount(indexId);
			count.setNodeId(nodeId);
			count.setContentId(contentId);
			count.setTableId(tableId);
			//
			count.init();
			countManager.saveCount(count);
			//
		}
	}

	/**
	 * 处理在线Web编辑器内容
	 * 
	 * @param request
	 * @param editorContent
	 * @param nodeId
	 * @param rsIds
	 * @param edit
	 * @return
	 * @throws Exception
	 */
	protected String processWebEditorContent(HttpServletRequest request,
			String editorContent, Long nodeId, List rsIds, boolean edit)
			throws Exception {
		// 1,process the remote url
		String ct1 = processRemoteUrl(request, editorContent, nodeId, rsIds);
		// 2,process the local url

		String ct2 = processLocalUrl(request, ct1, nodeId, rsIds, edit);

		return ct2;
	}

	/**
	 * 处理远程资源引用
	 */
	protected String processRemoteUrl(HttpServletRequest request,
			String editorContent, Long nodeId, List rsIds) throws Exception {
		List resourceIdList = new ArrayList();
		// should not be duplicate
		Set remoteFileUrl = new HashSet();
		// @todo some filter process
		// get the image,flash extension name
		CMSConfig cfg = CMSConfig.getInstance();
		String imgExtensions = cfg.getUploadFileImageType();
		String flashExtensions = cfg.getUploadFileFlashType();
		String attachExtensions = cfg.getUploadFileAttachType();
		//
		String sAllowExt = imgExtensions + "|" + flashExtensions;
		//
		String remoteFileurl = "";
		String originalFileName = "";
		String saveFileType = "";
		String resourceType = "";
		String localFileUrl = "";
		StringBuffer sb = new StringBuffer();
		//
		Pattern pRemoteFileurl = Pattern
				.compile(
						"((http|https|ftp|rtsp|mms):(//|\\\\)"
								+ "{1}(([A-Za-z0-9_-])+[.]){1,}"
								+ "(net|com|cn|org|cc|tv|[0-9]{1,3})(\\S*/)((\\S)+[.]{1}("
								+ sAllowExt + ")))", Pattern.CASE_INSENSITIVE);
		Matcher mRemoteFileurl = pRemoteFileurl.matcher(editorContent);
		boolean result = mRemoteFileurl.find();
		// boolean duplicate = false;
		while (result) {
			remoteFileurl = mRemoteFileurl.group(0);
			// eject duplicate
			if (!remoteFileUrl.contains(remoteFileurl)) {
				remoteFileUrl.add(remoteFileurl); // add to the set
				// duplicate = false;
				originalFileName = remoteFileurl.substring(remoteFileurl
						.lastIndexOf("/"));
				Pattern pFileType = Pattern.compile(
						"[.]{1}(" + sAllowExt + ")", Pattern.CASE_INSENSITIVE); //
				Matcher mFileType = pFileType.matcher(remoteFileurl);
				while (mFileType.find()) {
					saveFileType = mFileType.group();
					//
				}
				saveFileType = saveFileType.substring(1);
				//
				resourceType = getResourceType(saveFileType.toLowerCase());
				if (pathNameStrategy == null) {
					pathNameStrategy = new DatePathNameStrategy();
				}
				//
				String pathName = pathNameStrategy.getPathName();
				String destFileName = pathNameStrategy
						.getFileName(resourceType);
				String rootDir = CMSConfig.getInstance().getResourceRootPath();
				String fullFileName = rootDir + File.separator + resourceType
						+ File.separator + pathName + File.separator
						+ destFileName + "." + saveFileType;
				fullFileName = StringUtil.normalizePath(fullFileName);
				if (FileUtil.downloadRemoteFile(remoteFileurl, fullFileName)) {
					// add to the database
					Resource rs = new Resource();
					rs.setCategory(resourceType);
					// get the now seconds
					long now = System.currentTimeMillis();
					// now = now / 1000;
					// Integer inow = new Integer(String.valueOf(now));
					rs.setCreationDate(new Long(now));
					rs.setModifiedDate(new Long(now));
					//
					rs.setNodeId(nodeId);
					rs.setCreationUserId(this.getUser().getUserId());
					//
					File destFile = new File(fullFileName);
					if (resourceType.equals("img")) {
						FileInputStream finput = new FileInputStream(destFile);
						Dimension dm = ImageUtil.getDimension(finput);
						//
						if (dm != null) {
							String info = dm.width + "*" + dm.height;
							rs.setInfo(info);
						} else {
							rs.setInfo("");
						}
					} else {
						rs.setInfo("");
					}

					rs.setName(destFileName + "." + saveFileType);
					rs.setParentId(new Long(0));
					//
					long size = destFile.length();
					size = size / 1024; // b->kb
					if (size == 0) {
						size = 1;
					}
					Integer kbsize = new Integer(String.valueOf(size));
					rs.setSize(kbsize);
					rs.setSrc("");
					rs.setType(new Integer("1"));

					rs.setTitle(FileUtil.getFileName(originalFileName));
					rs.setPath(resourceType + "/" + pathName + "/"
							+ destFileName + "." + saveFileType);

					Long resourceId = resourceManager.addResource(rs);
					//
					resourceIdList.add(resourceId);
					//
					localFileUrl = "../resource" + "/" + resourceType + "/"
							+ pathName + "/" + destFileName + "."
							+ saveFileType;

				}
			} else {
				// duplicate = true;
			}
			mRemoteFileurl.appendReplacement(sb, localFileUrl); // replace the
			// text
			//
			result = mRemoteFileurl.find();
		}
		mRemoteFileurl.appendTail(sb);
		// System.out.println(sb.toString());
		//
		String processedContent = sb.toString();
		for (int i = 0; i < resourceIdList.size(); i++) {
			if (!rsIds.contains(resourceIdList.get(i))) {
				rsIds.add(resourceIdList.get(i));
			}

		}
		return processedContent;
	}

	/**
	 * 处理多图中的资源引用
	 * 
	 * @param rsIds
	 */
	protected void processMultiImg(String content, List rsIds, String sAllowExt) {
		if (content != null) {
			MultiResField field = MultiResField.fieldFromString(content);
			if (field.getNums() > 0) {
				List<ResRefBean> beans = field.getReses();
				for (ResRefBean bean : beans) {
					long id = bean.getId();
					if (!rsIds.contains(id)) {
						rsIds.add(id);
					}
				}
			}
		}
	}

	/**
	 * 处理本地资源引用
	 * 
	 * @param request
	 * @param editorContent
	 * @param nodeId
	 * @param rsIds
	 * @param edit
	 * @return
	 * @throws Exception
	 */
	protected String processLocalUrl(HttpServletRequest request,
			String editorContent, Long nodeId, List rsIds, boolean edit)
			throws Exception {
		// List resourceIdList = new ArrayList();
		Set localFileUrl = new HashSet();
		// @todo some filter process
		// get the image,flash extension name
		CMSConfig cfg = CMSConfig.getInstance();
		String imgExtensions = cfg.getUploadFileImageType();
		String flashExtensions = cfg.getUploadFileFlashType();
		String attachExtensions = cfg.getUploadFileAttachType();
		//
		String sAllowExt = imgExtensions + "|" + flashExtensions + "|"
				+ attachExtensions;
		String sp = "\\.\\.\\/resource\\/((img|flash|attach)(\\/\\w+)+\\.("
				+ sAllowExt + "))";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(editorContent);
		boolean found = m.find();
		while (found) {
			String path = m.group(1);
			if (!localFileUrl.contains(path)) {
				localFileUrl.add(path);
				Resource rs = resourceManager.getResourceByPath(path);
				if(rs!=null){
					Long rsid = rs.getResourceId();
					if (!rsIds.contains(rsid)) {
						rsIds.add(rsid);
					}
				}
			}
			found = m.find();
		}
		// process another things
		StringBuffer sb = new StringBuffer();
		String contextPath = request.getContextPath();
		String sp2 = "";
		// System.out.println("contextPath=" + contextPath);
		if (!contextPath.equals("")) {
			contextPath = contextPath.substring(1);
			sp2 = "\\/" + contextPath
					+ "(\\/)+resource\\/((img|flash|attach)(\\/\\w+)+\\.("
					+ sAllowExt + "))";
		} else {
			sp2 = "(\\/)+resource\\/((img|flash|attach)(\\/\\w+)+\\.("
					+ sAllowExt + "))";
		}
		Pattern p2 = Pattern.compile(sp2);
		Matcher m2 = p2.matcher(editorContent);
		boolean found2 = m2.find();
		while (found2) {
			String fullPath = m2.group(0);
			String rsPath = m2.group(2);
			if (!localFileUrl.contains(rsPath)) {
				localFileUrl.add(rsPath);
				Resource rs = resourceManager.getResourceByPath(rsPath);
				if (rs != null) {
					Long rsid = rs.getResourceId();
					if (!rsIds.contains(rsid)) {
						rsIds.add(rsid);
					}
				}
				m2.appendReplacement(sb, "../resource/" + rsPath);
			}
			//

			//
			found2 = m2.find();
		}
		m2.appendTail(sb);
		String content = sb.toString();
		return content;
	}

	private String getResourceType(String contentType) {
		if (isValidFileType("img", contentType)) {
			return "img";
		} else if (isValidFileType("flash", contentType)) {
			return "flash";
		} else if (isValidFileType("attach", contentType)) {
			return "attach";
		}
		return null;
	}

	protected boolean isValidFileType(String type, String contentType) {

		String acceptTypes = getAcceptFileType(type);
		if (acceptTypes != null) {
			StringTokenizer tk = new StringTokenizer(acceptTypes, "|");
			while (tk.hasMoreTokens()) {
				String acType = tk.nextToken();
				if (contentType.indexOf(acType) > -1) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	private String getAcceptFileType(String type) {
		if (type != null) {
			if (type.equals("img")) {
				String acceptTypes = CMSConfig.getInstance()
						.getUploadFileImageType();
				return acceptTypes;
			} else if (type.equals("flash")) {
				String acceptTypes = CMSConfig.getInstance()
						.getUploadFileFlashType();
				return acceptTypes;
			} else if (type.equals("attach")) {
				String acceptTypes = CMSConfig.getInstance()
						.getUploadFileAttachType();
				return acceptTypes;
			}
		}
		return null;
	}

	public void setContentEditorView(String contentEditorView) {
		this.contentEditorView = contentEditorView;
	}

	public void setContentEditorSaveView(String contentEditorSaveView) {
		this.contentEditorSaveView = contentEditorSaveView;
	}

	public void setContentEditorHView(String contentEditorHView) {
		this.contentEditorHView = contentEditorHView;
	}

	public void setContentEditorFView(String contentEditorFView) {
		this.contentEditorFView = contentEditorFView;
	}

	public void setCmsMacroEngine(CmsMacroEngine cmsMacroEngine) {
		this.cmsMacroEngine = cmsMacroEngine;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	public void setContentOperationView(String contentOperationView) {
		this.contentOperationView = contentOperationView;
	}

	public void setCountManager(CountManager countManager) {
		this.countManager = countManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setPathNameStrategy(PathNameStrategy pathNameStrategy) {
		this.pathNameStrategy = pathNameStrategy;
	}

	public void setPublishEngine(PublishEngine publishEngine) {
		this.publishEngine = publishEngine;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
}
