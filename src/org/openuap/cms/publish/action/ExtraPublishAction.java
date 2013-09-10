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
package org.openuap.cms.publish.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.CMSBaseFormAction;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.security.NodeSecurityUtil;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.publish.manager.ExtraPublishManager;
import org.openuap.cms.publish.model.ExtraPublish;
import org.openuap.cms.publish.security.PublishPermissionConstant;
import org.openuap.cms.util.ui.AutoRefreshMode;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 附加发布控制器.
 * </p>
 * 
 * <p>
 * $Id: ExtraPublishAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ExtraPublishAction extends CMSBaseFormAction {

	private String defaultViewName;

	private String defaultScreensPath;

	//
	private String operationViewName;

	//
	private ExtraPublishManager extraPublishManager;

	private PublishEngine publishEngine;

	private PsnManager psnManager;

	private NodeManager nodeManager;

	/**
	 * 
	 */
	public ExtraPublishAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/publish/extrapublish/";
		defaultViewName = defaultScreensPath + "extrapublish.html";
		operationViewName = defaultScreensPath
				+ "extrapublish_operation_result.html";
		//
		this.setFormView(defaultScreensPath + "extrapublish_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(ExtraPublish.class);
		this.setCommandName("publish");

	}

	/**
	 * 附件发布内容列表
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		// 所属结点
		String nodeId = request.getParameter("nodeId");
		String nids = helper.getString("nids", "");
		String ids = helper.getString("ids", "");
		String tplName = helper.getString("tplName", "");
		String pageName = helper.getString("pageName", "");
		
		String name = helper.getString("name", "");
		int page = helper.getInt("page", 1);
		int pageNum = helper.getInt("pageNum", 20);
		//

		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.ViewExtraPublish)) {
			throw new UnauthorizedException();
		}
		if (nodeId != null) {
			String where = "";
			if (StringUtil.hasText(nids)) {
				if(!nids.equals("-1")){
					where += " and nodeId in(" + nids + ")";
				}
				
			} else {

				where += " and nodeId in(" + nodeId + ")";

			}

			if (StringUtil.hasText(ids)) {
				where += " and publishId in(" + ids + ")";
			}
			if (StringUtil.hasText(tplName)) {
				where += " and tpl like'%" + tplName + "%'";
			}
			if (StringUtil.hasText(pageName)) {
				where += " and publishFileName like'%" + pageName + "%'";
			}
			if (StringUtil.hasText(name)) {
				where += " and publishName like'%" + name + "%'";
			}
			if (StringUtil.hasText(where)) {
				where = where.substring(4);
			}
			QueryInfo qi = new QueryInfo();
			qi.setWhereClause(where);
			qi.setLimit(pageNum);
			qi.setOffset((page - 1) * pageNum);
			qi.setOrderByClause(" creationDate DESC");
			//
			PageBuilder pb = new PageBuilder();
			pb.setPage(page);
			pb.setItemsPerPage(pageNum);
			//
			ModelAndView mv = new ModelAndView(defaultViewName, model);
			Long nid = new Long(nodeId);
			//

			Node node = nodeManager.getNode(nid);
			// TODO 修改成支持分页形式
			List publishes = extraPublishManager.getPublishes(qi,pb);
			model.put("publishes", publishes);
			model.put("nodeId", nodeId);
			model.put("node", node);
			model.put("pb", pb);
			//
			model.put("nids",nids);
			model.put("ids",ids);
			model.put("tplName",tplName);
			model.put("pageName",pageName);
			model.put("name",name);
			model.put("page",String.valueOf(page));
			model.put("pageNum",String.valueOf(pageNum));
			return mv;
		}
		return null;
	}

	/**
	 * 删除附件发布
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doDel(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(operationViewName, model);
		model.put("operation", "del");
		String publishId = request.getParameter("publishId");
		//
		String nodeId = request.getParameter("nodeId");
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.DeleteExtraPublish)) {
			throw new UnauthorizedException();
		}
		String result = "success";
		String msg = "";
		if (publishId != null) {
			try {
				Long pid = new Long(publishId);
				extraPublishManager.deletePublish(pid);

			} catch (Exception ex) {
				result = "failed";
				msg = ex.getMessage();
			}
		}
		model.put("nodeId", nodeId);
		model.put("result", result);
		model.put("msg", msg);
		return mv;
	}

	/**
	 * 提交附加发布
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		ModelAndView mv = new ModelAndView(operationViewName, model);
		ExtraPublish extraPublish = (ExtraPublish) command;
		String mode = request.getParameter("mode");
		String nodeId = request.getParameter("nodeId");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.EditExtraPublish)) {
			throw new UnauthorizedException();
		}
		//
		String result = "success";
		String msg = "";
		model.put("operation", "add");
		model.put("nodeId", nodeId);

		try {
			if (mode == null || mode.equals("add")) {
				long createDate = System.currentTimeMillis();
				extraPublish.setCreationDate(new Long(createDate));

				extraPublish.setCreationUserId(this.getUser().getUserId());
				extraPublish.setCreationUserName(this.getUser().getName());
				//
				extraPublish.setModifiedDate(new Long(createDate));
				extraPublish.setLastModifiedUserId(this.getUser().getUserId());
				extraPublish.setLastModifiedUserName(this.getUser().getName());

				//
				Long pid = extraPublishManager.addPublish(extraPublish);

				model.put("pid", pid);
				model.put("mode", "edit");
			} else {
				long modifyDate = System.currentTimeMillis();
				extraPublish.setModifiedDate(new Long(modifyDate));
				extraPublish.setLastModifiedUserId(this.getUser().getUserId());
				extraPublish.setLastModifiedUserName(this.getUser().getName());
				//
				extraPublishManager.savePublish(extraPublish);
				model.put("operation", "edit");
				model.put("pid", extraPublish.getPublishId());
				model.put("mode", "edit");
			}

		} catch (Exception ex) {
			result = "failed";
			msg = ex.getMessage();
		}
		model.put("result", result);
		model.put("msg", msg);
		return mv;
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		// @todo
	}

	protected Object formBackingObject(HttpServletRequest request) {
		String mode = request.getParameter("mode");
		String nodeId = request.getParameter("nodeId");
		String publishId = request.getParameter("publishId");
		Long nid = new Long(nodeId);
		ExtraPublish extraPublish = null;
		if (mode == null || mode.equals("add")) {
			extraPublish = new ExtraPublish();
			String defaultFileName = "" + System.currentTimeMillis() + ".html";
			extraPublish.setPublishFileName(defaultFileName);
			extraPublish.setNodeId(nid);
		} else {
			Long pid = new Long(publishId);
			extraPublish = extraPublishManager.getPublishById(pid);
		}
		return extraPublish;
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		String mode = request.getParameter("mode");
		String nodeId = request.getParameter("nodeId");
		Node node = nodeManager.getNode(new Long(nodeId));
		Map model = new HashMap();
		model.put("mode", mode);
		model.put("nodeId", nodeId);
		model.put("node", node);
		model.put("publishModes", PublishMode.DEFAULT_MODES);
		//
		model.put("autoRefreshModes", AutoRefreshMode.ALL_REFRESH_MODES);
		return model;
	}

	/**
	 * 刷新指定附加发布内容
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRefresh(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(operationViewName, model);
		model.put("operation", "refreshExtra");

		String publishId = request.getParameter("pid");
		String nodeId = request.getParameter("nid");
		String multi = request.getParameter("multi");
		String[] pData = request.getParameterValues("pData");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.RefreshExtraPublish)) {
			throw new UnauthorizedException();
		}
		model.put("nodeId", nodeId);
		if (multi != null && multi.equals("1")) {
			List errors = new ArrayList();
			try {
				model.put("operation", "batch_refreshExtra");
				if (pData != null) {
					for (int i = 0; i < pData.length; i++) {
						String p_id = pData[i];
						Long batch_pid = new Long(p_id);
						Long nid = new Long(nodeId);
						//
						publishEngine.refreshNodeExtraIndex(nid, batch_pid,
								errors);

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
			// 单条模式
			try {
				if (publishId != null && nodeId != null) {
					Long pid = new Long(publishId);
					Long nid = new Long(nodeId);

					List errors = new ArrayList();
					// 调用结点附加发布刷新引擎
					publishEngine.refreshNodeExtraIndex(nid, pid, errors);

					if (errors.size() > 0) {
						model.put("result", "failed");
						model.put("msgs", errors);
					} else {
						model.put("result", "success");
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
	 * 刷新指定结点的所有附加发布
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRefreshAll(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(operationViewName, model);
		model.put("operation", "refreshExtraAll");
		String nodeId = request.getParameter("nid");
		model.put("nodeId", nodeId);
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.RefreshExtraPublish)) {
			throw new UnauthorizedException();
		}
		if (nodeId != null) {
			try {
				List errors = new ArrayList();
				Long nid = new Long(nodeId);
				publishEngine.refreshNodeAllExtraIndex(nid, errors);
				if (errors.size() > 0) {
					model.put("result", "failed");
					model.put("msgs", errors);
				} else {
					model.put("result", "success");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				model.put("result", "failed");
				model.put("msgs", ex);
			}

		}
		return mv;
	}

	/**
	 * 查看附加发布
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws UnauthorizedException
	 */
	public ModelAndView doView(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String publishId = request.getParameter("pid");
		if (publishId != null) {
			Long pid = new Long(publishId);
			ExtraPublish p = extraPublishManager.getPublishById(pid);
			if (p != null) {
				Long nid = p.getNodeId();
				// 鉴权
				if (!NodeSecurityUtil.hasPermission(
						PublishPermissionConstant.OBJECT_TYPE.toString(), nid
								.toString(),
						PublishPermissionConstant.ViewExtraPublish)) {
					throw new UnauthorizedException();
				}

				Node node = nodeManager.getNodeById(nid);
				Integer publishMode = node.getPublishMode();
				Integer selfPublishMode = p.getPublishMode();
				if (selfPublishMode != null
						&& !selfPublishMode.equals(new Integer(-1))) {
					publishMode = selfPublishMode;
				}
				if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
					String psnUrl = p.getSelfPsnUrl();
					String url = psnManager.getPsnUrlInfo(psnUrl);
					String fileName = p.getPublishFileName();
					url = url + "/" + fileName;
					response.sendRedirect(url);
				} else {
					// 动态发布
					String url = node.getExtraPortalUrl();
					String selfUrl = p.getExtraPortalUrl();
					if (selfUrl != null && !selfUrl.equals("")) {
						url = selfUrl;
					}
					url = url.replaceAll("\\{PublishID\\}", pid.toString());
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

	/**
	 * 附加发布剪切
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws
	 */
	public ModelAndView doCut(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(operationViewName, model);
		model.put("operation", "cut");
		String id = request.getParameter("id");
		String nodeId = request.getParameter("nodeId");
		String targetNodeId = request.getParameter("targetNodeId");
		//
		String multi = request.getParameter("multi");
		String[] pData = request.getParameterValues("pData");
		//
		if (!NodeSecurityUtil.hasPermission(
				PublishPermissionConstant.OBJECT_TYPE.toString(), nodeId,
				PublishPermissionConstant.EditExtraPublish)) {
			throw new UnauthorizedException();
		}

		model.put("nodeId", nodeId);
		model.put("id", id);
		//
		List errors = new ArrayList();
		//
		if (multi != null && multi.equals("1")) {
			// batch
			model.put("operation", "batch_cut");
			try {
				Long nid = new Long(nodeId);
				Long tnid = new Long(targetNodeId);
				Node srcNode = nodeManager.getNode(nid);
				Node destNode = nodeManager.getNode(tnid);
				if (srcNode != null && destNode != null) {
					if (nid.equals(tnid)) {
						model.put("result", "failed");
						model.put("msgs", "您不能在同一结点内剪切内容!");
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
						String p_id = pData[i];
						Long batch_iid = new Long(p_id);
						ExtraPublish publish = this.extraPublishManager
								.getPublishById(batch_iid);
						publish.setNodeId(tnid);
						extraPublishManager.savePublish(publish);
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
			if (nodeId != null && targetNodeId != null && id != null) {
				try {
					Long nid = new Long(nodeId);
					Long tnid = new Long(targetNodeId);
					Long iid = new Long(id);
					Node srcNode = nodeManager.getNode(nid);
					Node destNode = nodeManager.getNode(tnid);
					if (srcNode != null && destNode != null) {
						if (nid.equals(tnid)) {
							model.put("result", "failed");
							model.put("msgs", "您不能在同一结点下剪切内容!");
							return mv;
						}
					} else {
						model.put("result", "failed");
						model.put("msgs", "您选择正确的源结点与目标结点!");
						return mv;
					}
					// 2)unpublish the index content
					ExtraPublish publish = this.extraPublishManager
							.getPublishById(iid);
					publish.setNodeId(tnid);
					extraPublishManager.savePublish(publish);
					// 3)update the nodeId
					if (errors.size() == 0) {

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

	public String getDefaultScreensPath() {
		return defaultScreensPath;
	}

	public String getDefaultViewName() {
		return defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setExtraPublishManager(ExtraPublishManager extraPublishManager) {
		this.extraPublishManager = extraPublishManager;
	}

	public void setOperationViewName(String operationViewName) {
		this.operationViewName = operationViewName;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setPublishEngine(PublishEngine publishEngine) {
		this.publishEngine = publishEngine;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

}
