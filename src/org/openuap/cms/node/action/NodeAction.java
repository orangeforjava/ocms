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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.cache.NodeCache;
import org.openuap.cms.node.event.NodeEvent;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.manager.NodePluginManager;
import org.openuap.cms.node.manager.NodeQuickQuery;
import org.openuap.cms.node.model.CustomNodeDescriptor;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.security.NodePermissionConstant;
import org.openuap.cms.node.security.NodeSecurityUtil;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 结点控制器.
 * </p>
 * 
 * <p>
 * $Id: NodeAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeAction extends AdminAction {

	private NodeManager nodeManager;

	private NodePluginManager nodePluginManager;

	//
	private String defaultViewName;

	private String defaultScreensPath;

	private String siteXmlViewName;

	private String recyclebinXmlViewName;

	//
	private String siteSystemViewName;

	//
	private String sortDialogViewName;

	private String operationViewName;
	//
	private String nodeSelDialogViewName;

	private String nodeSelDialogViewName2;

	private String nodeSelViewName;

	private String nodeSelXmlViewName;

	private String jsViewName;

	private String fsViewName;

	private String headerViewName;

	private String listViewName;

	private String treeViewName;

	/**
	 * 
	 */
	public NodeAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/node/";
		defaultViewName = defaultScreensPath + "node.html";
		jsViewName = defaultScreensPath + "node.js";
		siteXmlViewName = defaultScreensPath + "site_tree.xml";
		operationViewName = defaultScreensPath + "node_operation_result.html";
		recyclebinXmlViewName = defaultScreensPath + "recyclebin_tree.xml";
		sortDialogViewName = defaultScreensPath + "node_sort_dialog.html";
		siteSystemViewName = defaultScreensPath + "site_sys_tree.xml";
		//
		nodeSelDialogViewName = defaultScreensPath + "node_select_dialog.html";
		nodeSelViewName = defaultScreensPath + "node_select.html";
		nodeSelXmlViewName = defaultScreensPath + "node_select_tree.xml";
		//
		fsViewName = defaultScreensPath + "node_fs.html";
		headerViewName = defaultScreensPath + "node_header.html";
		listViewName = defaultScreensPath + "node_treetable.html";
		//
		treeViewName = defaultScreensPath + "node_tree.html";
		//

	}

	/**
	 * the default action will show the init node tree.
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
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), "-1", NodePermissionConstant.ViewNode)) {
			throw new UnauthorizedException();
		}

		ModelAndView mv = new ModelAndView(defaultViewName, model);
		// get the root nodes
		int f = 0;
		List rootNodes = nodeManager.getChildNodes(new Long(0));
		model.put("rootNodes", rootNodes);
		model.put("nodeManager", nodeManager);
		// 获取自定义结点
		if (nodePluginManager != null) {
			List<CustomNodeDescriptor> customNodeDescriptors = nodePluginManager
					.getCustomNodeDescriptors();
			model.put("customNodeDescriptors", customNodeDescriptors);
		}
		setNoCacheHeader(response);
		return mv;
	}

	/**
	 * 返回TreeTable方式的站点视图
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doFS(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(fsViewName, model);
		return mv;
	}

	public ModelAndView doHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(headerViewName, model);
		return mv;
	}

	public ModelAndView doTreeTable(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(listViewName, model);
		//
		List nodes = nodeManager.getAllNodes();
		NodeQuickQuery quickQuery = new NodeQuickQuery(nodes);
		model.put("quickQuery", quickQuery);
		return mv;
	}
	public ModelAndView doNavStr(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Long nid=helper.getLong("nid",0);
		StringBuffer navStr=new StringBuffer();
		if(nid!=0){
			Node node=nodeManager.getNode(nid);
			List<Node> navNodes=nodeManager.getNavNodes(node, 0L);
			
			for(Node nd:navNodes){
				navStr.append(nd.getName());
				navStr.append("&gt;");
			}
			if(navNodes.size()>0){
				navStr.delete(navStr.length()-"&gt;".length(), navStr.length());
			}
		}
		return this.sendContent(request, response, helper, model, navStr.toString());
	}
	public ModelAndView doNodeName(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
	throws Exception {
		Long nid=helper.getLong("nid",0);
		String nodeName="";
		if(nid!=0){
			Node node=nodeManager.getNode(nid);
			nodeName=node.getName();
		}
		return this.sendContent(request, response, helper, model, nodeName);
	}
	public ModelAndView doTree(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), "-1", NodePermissionConstant.ViewNode)) {
			throw new UnauthorizedException();
		}

		ModelAndView mv = new ModelAndView(treeViewName, model);
		// get the root nodes
		int f = 0;
		List rootNodes = nodeManager.getChildNodes(new Long(0));
		model.put("rootNodes", rootNodes);
		model.put("nodeManager", nodeManager);
		// 获取自定义结点
		if (nodePluginManager != null) {
			List<CustomNodeDescriptor> customNodeDescriptors = nodePluginManager
					.getCustomNodeDescriptors();
			model.put("customNodeDescriptors", customNodeDescriptors);
		}
		setNoCacheHeader(response);
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
	public ModelAndView doSiteXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), "-1", NodePermissionConstant.ViewNode)) {
			throw new UnauthorizedException();
		}

		String nodeId = request.getParameter("nodeId");
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(siteXmlViewName, model);
			Long id = new Long(nodeId);
			byte f = 0;
			List nodes = nodeManager.getChildNodes(id);
			//
			setNoCacheHeader(response);

			model.put("responseType", "text/xml");
			model.put("nodes", nodes);
			model.put("nodeManager", nodeManager);
			return mv;
		}
		return null;
	}

	/**
	 * 显示站点JS树
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
	public ModelAndView doSiteJS(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), "-1", NodePermissionConstant.ViewNode)) {
			throw new UnauthorizedException();
		}
		String nodeId = request.getParameter("nodeId");
		if (nodeId == null) {
			nodeId = "0";
		}
		try {
			ModelAndView mv = new ModelAndView(jsViewName, model);
			Long id = new Long(nodeId);
			byte f = 0;
			List nodes = nodeManager.getNodes(id, new Long(0), new Integer(f));
			//
			setNoCacheHeader(response);

			model.put("responseType", "text/javaScript");
			model.put("nodes", nodes);
			model.put("nodeManager", nodeManager);
			return mv;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 显示回收站站点XML
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doSiteRecycleBinXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), "-1", NodePermissionConstant.ViewNode)) {
			throw new UnauthorizedException();
		}

		ModelAndView mv = new ModelAndView(recyclebinXmlViewName, model);
		List nodes = nodeManager.getRecycleBinNodes();
		setNoCacheHeader(response);
		model.put("responseType", "text/xml");
		model.put("nodes", nodes);
		model.put("nodeManager", nodeManager);
		return mv;
	}

	public ModelAndView doSiteSystemXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		if (!SecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), "-1", NodePermissionConstant.ViewNode)) {
			throw new UnauthorizedException();
		}

		String nodeId = request.getParameter("nodeId");
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(siteSystemViewName, model);
			Long id = new Long(nodeId);
			byte f = 0;
			List nodes = nodeManager.getNodes(id, new Long(0), new Integer(f));
			//
			setNoCacheHeader(response);

			model.put("responseType", "text/xml");
			model.put("nodes", nodes);
			model.put("nodeManager", nodeManager);
			return mv;
		}
		return null;

	}

	/**
	 * <p>
	 * Delete the node,in fact,set the disabled to 1 <br/>
	 * 需要同时取消发布结点所包含的内容
	 * </p>
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

		ModelAndView mv = new ModelAndView(jsonOpViewName, model);
		model.put("operation", "del");
		String nodeId = request.getParameter("nodeId");
		String confirmDel = request.getParameter("confirmDel");
		//
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE,
				nodeId, NodePermissionConstant.DeleteNode)) {
			throw new UnauthorizedException();
		}
		//
		model.put("nodeId", nodeId);
		//
		if (nodeId != null) {
			try {
				Long nid = new Long(nodeId);
				long child_count = nodeManager.getNodeCount(nid, new Long(0),
						new Integer("0"));
				if (child_count > 0l) {
					model.put("extra", "containChild");
					if (confirmDel != null && confirmDel.equals("1")) {
						nodeManager.recycleNode(nid, true);

					} else {
						model.put("operation", "del_confirm");
					}
				} else {
					nodeManager.recycleNode(nid, true);

				}
				// 分发结点删除事件,在事件响应中会清除结点缓存
				Node node = new Node();
				node.setNodeId(nid);
				NodeEvent event = new NodeEvent(NodeEvent.NODE_DELETED, node,
						new HashMap(), this);
				WebPluginManagerUtils.dispatcherEvent(false, "base", event);
				model.put("result", "success");
			} catch (Exception ex) {
				model.put("result", "failed");
			}
		}
		return mv;
	}

	/**
	 * <p>
	 * 彻底删除结点 <br/>
	 * 同时也需要删除结点下的内容.
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doDestroy(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(jsonOpViewName, model);
		model.put("operation", "destroy");
		String nodeId = request.getParameter("nodeId");
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), nodeId, NodePermissionConstant.EmptyNode)) {
			throw new UnauthorizedException();
		}

		try {
			if (nodeId != null) {
				Long nid = new Long(nodeId);
				nodeManager.deleteNode(nid);
				NodeCache.clear();
				model.put("result", "success");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("result", "failed");
		}
		return mv;
	}

	/**
	 * 恢复结点
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doRestore(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(jsonOpViewName, model);
		model.put("operation", "restore");
		String nodeId = request.getParameter("nodeId");
		String targetNodeId = request.getParameter("targetNodeId");
		//
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), nodeId, NodePermissionConstant.RestoreNode)) {
			throw new UnauthorizedException();
		}
		try {
			if (nodeId != null && targetNodeId != null) {
				Long nid = new Long(nodeId);
				Long tnid = new Long(targetNodeId);
				Node node = nodeManager.getNodeById(nid);
				node.setDisabled(new Integer("0"));
				node.setParentId(tnid);
				nodeManager.saveNode(node);
				NodeCache.clear();
				//
				model.put("result", "success");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("result", "failed");
		}
		return mv;
	}

	/**
	 * Empty the recycle bin
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
	public ModelAndView doEmptyRecycleBin(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(jsonOpViewName, model);
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), "-1", NodePermissionConstant.EmptyRecycleBin)) {
			throw new UnauthorizedException();
		}

		model.put("operation", "empty");
		try {
			nodeManager.deleteRecycleBinNodes();
			NodeCache.clear();
			model.put("result", "success");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("result", "failed");
		}
		return mv;
	}

	/**
	 * Move the node,the target node should not be the node child
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doMove(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(jsonOpViewName, model);
		model.put("operation", "move");
		String nodeId = request.getParameter("nodeId");
		String targetNodeId = request.getParameter("targetNodeId");
		//
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), nodeId, NodePermissionConstant.MoveNode)) {
			throw new UnauthorizedException();
		}
		//
		try {
			Long nid = new Long(nodeId);
			Long tnid = new Long(targetNodeId);
			boolean failed = nodeManager.getIsChildNode(nid, tnid);
			if (failed) {
				model.put("result", "failed");
				model.put("msg", "移动结点失败！");
				return mv;
			}
			Node node = nodeManager.getNodeById(nid);
			node.setParentId(tnid);
			nodeManager.saveNode(node);
			NodeCache.clear();
			model.put("result", "success");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("result", "failed");
		}
		return mv;
	}

	/**
	 * 显示排序对话框
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 */
	public ModelAndView doSort(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(sortDialogViewName, model);
		String nodeId = request.getParameter("nodeId");
		//
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), nodeId, NodePermissionConstant.SortNode)) {
			throw new UnauthorizedException();
		}
		//
		if (nodeId != null) {
			Long nid = new Long(nodeId);
			Node node = nodeManager.getNodeById(nid);
			model.put("node", node);
			return mv;
		}
		return null;
	}

	/**
	 * sort the node
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
	public ModelAndView doSortSubmit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		String nodeId = request.getParameter("nodeId");
		String weight = request.getParameter("weight");
		//
		if (!NodeSecurityUtil.hasPermission(NodePermissionConstant.OBJECT_TYPE
				.toString(), nodeId, NodePermissionConstant.SortNode)) {
			throw new UnauthorizedException();
		}
		//

		boolean success = false;
		try {
			if (nodeId != null && weight != null) {
				Long nid = new Long(nodeId);
				Node node = nodeManager.getNodeById(nid);
				Long sort = new Long(weight);
				node.setNodeSort(sort);
				nodeManager.saveNode(node);
				NodeCache.clear();
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
	 * 结点选择窗口
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doTargetNodeWindow(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		String extra = request.getParameter("extra");
		Long nid=helper.getLong("nid",0);
		if(nid!=0){
			Node node=nodeManager.getNode(nid);
			List<Node> navNodes=nodeManager.getNavNodes(node, 0L);
			model.put("node", node);
			model.put("navNodes",navNodes);
		}
		ModelAndView mv = new ModelAndView(nodeSelDialogViewName, model);
		model.put("extra", extra);
		return mv;
	}
	
	/**
	 * IE模式对话框适用
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doTargetNodeWindow2(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		String extra = request.getParameter("extra");
		ModelAndView mv = new ModelAndView(nodeSelDialogViewName2, model);
		model.put("extra", extra);
		return mv;
	}

	/**
	 * 选择结点对话框
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doNodeSelect(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(nodeSelViewName, model);
		List rootNodes = nodeManager.getChildNodes(new Long(0));
		model.put("rootNodes", rootNodes);
		model.put("nodeManager", nodeManager);
		setNoCacheHeader(response);
		return mv;
	}

	public ModelAndView doNodeSelXml(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		String nodeId = request.getParameter("nodeId");
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(nodeSelXmlViewName, model);
			Long id = new Long(nodeId);
			List nodes = nodeManager.getChildNodes(id);
			//
			setNoCacheHeader(response);
			model.put("responseType", "text/xml");
			model.put("nodes", nodes);
			model.put("nodeManager", nodeManager);
			return mv;
		}
		return null;

	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	/**
	 * 
	 * @param nodePluginManager
	 */
	public void setNodePluginManager(NodePluginManager nodePluginManager) {
		this.nodePluginManager = nodePluginManager;
	}

}
