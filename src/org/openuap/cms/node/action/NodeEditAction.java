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
package org.openuap.cms.node.action;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.core.action.CMSBaseFormAction;
import org.openuap.cms.node.event.NodeEvent;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.security.NodePermissionConstant;
import org.openuap.cms.node.security.NodeSecurityUtil;
import org.openuap.cms.node.ui.NodeKind;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.util.ui.AutoRefreshMode;
import org.openuap.cms.util.ui.ColorType;
import org.openuap.cms.util.ui.EditorType;
import org.openuap.cms.util.ui.NodeType;
import org.openuap.cms.util.ui.PagerType;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.cms.util.ui.SubDirType;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 结点编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: NodeEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeEditAction extends CMSBaseFormAction {

	private String defaultScreensPath;

	private NodeManager nodeManager;

	private ContentTableManager contentTableManager;

	private PsnManager psnManager;

	/**
	 * 
	 */
	public NodeEditAction() {
		initDefaultProperty();
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		// 如果通过请求参数指定了语言
		String parentId = request.getParameter("parentId");
		if (parentId != null) {
			if (!NodeSecurityUtil.hasPermission(
					NodePermissionConstant.OBJECT_TYPE.toString(), parentId,
					NodePermissionConstant.EditNode)) {
				throw new UnauthorizedException();
			}
		}
		String mode = helper.getString("mode","add");
		if(mode.equals("edit")){
			String nodeId=helper.getString("nodeId","");
			if(StringUtil.isNumber(nodeId)){
				if (!NodeSecurityUtil.hasPermission(
						NodePermissionConstant.OBJECT_TYPE.toString(), nodeId,
						NodePermissionConstant.EditNode)) {
					throw new UnauthorizedException();
				}
			}else{
				return this.errorPage(request, response, helper, "node_must_set", model);
			}
		}
		//
		return super.beforePerform(request, response, helper, model);
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/node/edit/";
		this.setFormView(defaultScreensPath + "node_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(Node.class);
		this.setCommandName("node");
	}

	/**
	 * save the node
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		Node node = (Node) command;
		if (node.getNodeId() == null) {
			if (node.getParentId() == null) {
				node.setParentId(0L);
			}
			if (!NodeSecurityUtil.hasPermission(
					NodePermissionConstant.OBJECT_TYPE.toString(), node
							.getParentId().toString(),
					NodePermissionConstant.EditNode)) {
				throw new UnauthorizedException();
			}
		} else {
			if (!NodeSecurityUtil.hasPermission(
					NodePermissionConstant.OBJECT_TYPE.toString(), node
							.getNodeId().toString(),
					NodePermissionConstant.EditNode)) {
				throw new UnauthorizedException();
			}
		}
		Long nodeId;
		String mode = request.getParameter("mode");
		String extra = request.getParameter("extra");
		if (extra == null) {
			extra = "";
		}
		if (mode != null && mode.equalsIgnoreCase("edit")) {
			// edit mode
			if (node.getPublishMode().equals(PublishMode.STATIC_MODE.getMode())
					&& !node.getNodeType().equals(
							NodeType.EXTERN_NODE_TYPE.getType())) {
				preSaveNode(node);
			}
			nodeManager.saveNode(node);
			nodeId = node.getNodeId();
			NodeEvent event = new NodeEvent(NodeEvent.NODE_UPDATED, node,
					new HashMap(), this);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
		} else {
			// add mode
			IUser user = this.getUser();
			node.setCreationUserId(user.getUserId());
			node.setDisabled(new Integer("0"));
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			int date = (int) ts.getTime();
			Long idate = new Long(date);
			node.setCreationDate(idate);
			node.setNodeSort(new Long(0));
			//

			nodeId = nodeManager.addNode(node);
			Node node2 = nodeManager.getNodeById(nodeId);
			if (node2.getPublishMode()
					.equals(PublishMode.STATIC_MODE.getMode())
					&& !node2.getNodeType().equals(
							NodeType.EXTERN_NODE_TYPE.getType())) {
				// 只处理正常、静态发布结点
				preSaveNode(node2);

			}
			nodeManager.saveNode(node2);
			NodeEvent event = new NodeEvent(NodeEvent.NODE_CREATED, node,
					new HashMap(), this);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
		}
		String messageCode = StringUtil.encodeURL("node_modify_success",
				"UTF-8");
		helper.sendRedirect(helper.getBaseURL()
				+ "admin/nodeEdit.jhtml?mode=edit&extra=" + extra + "&nodeId="
				+ nodeId + "&messageCode=" + messageCode);

		return null;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name_empty",
				"the name shouldn't be empty.");
		// 设定结点缺省属性
		Node node = (Node) command;
		if (node.getNodeType().equals(new Integer(3))) {
			node.setIndexName("");
			node.setPager("default");
			node.setEditor("default");
			node.setAutoPublish(0);
			node.setIndexPortalUrl("");
			node.setContentPortalUrl("");
			node.setExtraPortalUrl("");
			node.setWorkflow(0L);

		}
		//

	}

	protected Object formBackingObject(HttpServletRequest request) {
		String mode = request.getParameter("mode");
		String parentId = request.getParameter("parentId");
		String nodeId = request.getParameter("nodeId");
		String basedNodeId = request.getParameter("basedNodeId");
		String extra = request.getParameter("extra");
		if (mode != null && mode.equals("edit")) {
			if (nodeId != null) {
				Long id = new Long(nodeId);
				Node node = nodeManager.getNodeById(id);
				return node;
			}
		} else {
			if (parentId != null) {
				Long pid = new Long(parentId);
				if (basedNodeId != null) {
					Long basednid = new Long(basedNodeId);
					Node basedNode = nodeManager.getNodeById(basednid);
					basedNode.setNodeId(null);
					basedNode.setName("");
					basedNode.setParentId(pid);
					basedNode.setRootId(new Long(0));
					basedNode.setNodeSort(new Long("0"));
					return basedNode;
				} else {

					Node node = new Node();
					if (extra != null && extra.equals("system")) {
						node.setSystem(new Integer("1"));
					} else {
						node.setSystem(new Integer("0"));
					}
					node.setParentId(pid);
					node.setAutoPublish(new Integer("0"));
					node.setWorkflow(new Long(0));
					node.setPublishMode(new Integer("1"));
					node.setRootId(new Long(0));
					node.setInheritNodeId(new Long(0));
					node.setNodeSort(new Long("0"));
					node
							.setIndexPortalUrl("publish/index.jhtml?nodeId={NodeID}");
					node
							.setContentPortalUrl("publish/content.jhtml?nodeId={NodeID}&contentId={ContentID}");
					node
							.setExtraPortalUrl("publish/extra.jhtml?nodeId={NodeID}&publishId={ContentID}");
					return node;
				}
			}
		}
		return null;
	}

	/**
	 * 处理引用模型
	 * 
	 * @param request
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 * @return
	 * @throws
	 */
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map model = new HashMap();
		Node node = (Node) command;
		String mode = request.getParameter("mode");
		String nodeType = request.getParameter("nodeType");
		String extra = request.getParameter("extra");
		String nodeId=request.getParameter("nodeId");
		//
		if (node.getNodeId() != null) {
			nodeType = node.getNodeType().toString();
		}
		model.put("extra", extra);
		// remember the edit mode
		if (mode != null && mode.equals("edit")) {
			model.put("mode", mode);

		} else {
			model.put("mode", "new");
		}
		// set the nodeType
		if (nodeType != null && nodeType.equals("2")) {
			// 虚结点
			model.put("nodeType", 2);
			node.setNodeType(2);
		} else if (nodeType != null && nodeType.equals("3")) {
			// 外部结点
			model.put("nodeType", 3);
			node.setNodeType(3);
		} else {
			// 实结点
			model.put("nodeType", 1);
			node.setNodeType(1);
		}
		//
		if (node.getNodeId() == null) {
			model.put("nodePath", nodeManager.getNodeFullPath(node
					.getParentId(), "->"));
		} else {
			model.put("nodePath", nodeManager.getNodeFullPath(node.getNodeId(),
					"->"));
		}
		// add the contentTable
		List cts = null;
		if (extra != null && extra.equals("system")) {
			cts = contentTableManager.getSysContentTablesFromCache();
		} else {
			cts = contentTableManager.getUserContentTablesFromCache();
		}
		// List cts = contentTableManager.getAllContentTable();
		model.put("cts", cts);
		// add the node types
		model.put("nodeTypes", NodeType.DEFAULT_NODE_TYPES);
		// add the publish mode
		model.put("publishModes", PublishMode.DEFAULT_MODES);
		// 子目录类型
		model.put("subDirTypes", SubDirType.DEFAULT_SUBDIR_TYPES);
		// add for the nodes,no disabled,actual node type
		model.put("nodeManager", nodeManager);
		// add the editor type
		model.put("editorTypes", EditorType.DEFAULT_EDITOR_TYPES);
		model.put("pagerTypes", PagerType.DEFAULT_PAGERS);
		// 颜色类型
		model.put("colorTypes", ColorType.DEFAULT_COLOR_TYPES);
		// 站点种类
		model.put("nodeKinds", getSelNodeKinds(node));
		//关联刷新模式
		model.put("autoRefreshModes", AutoRefreshMode.ALL_REFRESH_MODES);
		//
		model.put("nodeId", nodeId);
		return model;

	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	/**
	 * it a method to edit node before save.
	 * 
	 * @param node
	 * 
	 */
	protected void preSaveNode(Node node) {

		String indexFileName = node.getIndexName();
		String contentUrl = node.getContentUrl();

		String url = contentUrl;
		String sp = "\\{PSN-URL:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(contentUrl);
		boolean result = m.find();
		if (result) {
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
		indexFileName = indexFileName.replaceAll("\\{NodeID\\}", node
				.getNodeId().toString());
		url = url + "/" + indexFileName;
		node.setNodeUrl(url);
	}

	protected NodeKind[] getSelNodeKinds(Node node) {
		if(node==null){
			return NodeKind.DEFAULT_NODE_KINDS;
		}
		Long pid = node.getParentId();
		if (pid == 0L) {
			return NodeKind.DEFAULT_NODE_KINDS;
		}
		Node parentNode = nodeManager.getNode(pid);
		if(parentNode==null){
			return NodeKind.DEFAULT_NODE_KINDS;
		}
		Integer pkind = parentNode.getNodeKind();
		if (pkind == null) {
			pkind = 0;
		}
		if (pkind.equals(NodeKind.NODE_KIND_SITE.getKind())) {
			// 父结点是站点
			return NodeKind.DEFAULT_NODE_KINDS;
		} else if (pkind.equals(NodeKind.NODE_KIND_CHANNEL.getKind())) {
			// 父结点是频道
			return new NodeKind[] { NodeKind.NODE_KIND_CHANNEL,
					NodeKind.NODE_KIND_COLUMN };
		} else if (pkind.equals(NodeKind.NODE_KIND_COLUMN.getKind())) {
			// 父结点是栏目
			return new NodeKind[] { NodeKind.NODE_KIND_COLUMN };
		}
		return new NodeKind[] {};
	}
}
