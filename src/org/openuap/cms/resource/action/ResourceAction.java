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
package org.openuap.cms.resource.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.resource.manager.ResourceManager;
import org.openuap.cms.resource.security.ResourcePermissionConstant;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 资源控制器.
 * </p>
 * 
 * <p>
 * $Id: ResourceAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ResourceAction extends AdminAction {

	private NodeManager nodeManager;

	private ResourceManager resourceManager;

	private IUserManager baseUserManager;

	//
	private String defaultViewName;

	private String defaultScreensPath;

	private String resourceXmlViewName;

	private String resourceHViewName;

	private String resourceFViewName;

	private String resourceLViewName;

	private String jsViewName;

	public ResourceAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/resource/";
		defaultViewName = defaultScreensPath + "resource.html";
		jsViewName = defaultScreensPath + "resource.js";
		resourceXmlViewName = defaultScreensPath + "resource_tree.xml";
		resourceHViewName = defaultScreensPath + "resource_header.html";
		resourceFViewName = defaultScreensPath + "resource_frameset.html";
		resourceLViewName = defaultScreensPath + "resource_list.html";
	}

	/**
	 * the default action will show the init resource site tree.
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
	public ModelAndView perform(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws UnauthorizedException {
		//
		if (!SecurityUtil.hasPermission(ResourcePermissionConstant.OBJECT_TYPE.toString(), "-1",
				ResourcePermissionConstant.ViewResource)) {
			throw new UnauthorizedException();
		}

		ModelAndView mv = new ModelAndView(defaultViewName, model);
		// get the root nodes
		byte f = 0;
		List rootNodes = nodeManager.getNodes(new Long(0), new Long(0), new Integer(f));
		model.put("rootNodes", rootNodes);
		model.put("nodeManager", nodeManager);
		setNoCacheHeader(response);
		return mv;

	}

	public ModelAndView doResourceXml(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {

		String nodeId = request.getParameter("nodeId");
		if (nodeId != null) {
			ModelAndView mv = new ModelAndView(resourceXmlViewName, model);
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
	 * 资源树JS
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doResourceJS(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {

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
	public ModelAndView doResourceListHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {

		String nodeId = request.getParameter("nodeId");
		//
		if (!SecurityUtil.hasPermission(ResourcePermissionConstant.OBJECT_TYPE.toString(),nodeId,
				ResourcePermissionConstant.ViewResource)) {
			throw new UnauthorizedException();
		}
		//
		ModelAndView mv = new ModelAndView(resourceHViewName, model);
		if (nodeId != null) {
			Long nid = new Long(nodeId);
			Node node = nodeManager.getNodeById(nid);
			model.put("node", node);
			model.put("nodeManager", nodeManager);
			return mv;
		}
		return mv;
	}

	public ModelAndView doListResource(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws UnauthorizedException {
		String nodeId = request.getParameter("nodeId");

		if (!SecurityUtil.hasPermission(ResourcePermissionConstant.OBJECT_TYPE.toString(),nodeId,
				ResourcePermissionConstant.DeleteResource)) {
			throw new UnauthorizedException();
		}

		ModelAndView mv = new ModelAndView(resourceFViewName, model);

		model.put("nodeId", nodeId);
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
	public ModelAndView doResourceList(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws UnauthorizedException {

		String nodeId = request.getParameter("nodeId");
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String category = request.getParameter("category");
		String haveLinks = request.getParameter("haveLinks");
		//
		if (!SecurityUtil.hasPermission(ResourcePermissionConstant.OBJECT_TYPE.toString(), nodeId,
				ResourcePermissionConstant.DeleteResource)) {
			throw new UnauthorizedException();
		}
		ModelAndView mv = new ModelAndView(resourceLViewName, model);
		//
		if (nodeId == null) {
			nodeId = "0";
		}
		if (page == null) {
			page = "1";
		}
		if (pageNum == null) {
			pageNum = "18";
		}
		if (category == null) {
			category = "img";
		}
		if (haveLinks == null) {
			haveLinks = "2";
		}
		Long nid = new Long(nodeId);
		Long ipage = new Long(page);
		Long ipageNum = new Long(pageNum);
		List resources = null;
		int start = (ipage.intValue() - 1) * ipageNum.intValue();
		long totalCount = 0;
		if (nodeId.equals("0")) {
			resources = resourceManager.getResourcesByCata(category, new Long(start), ipageNum);
			totalCount = resourceManager.getResourceCountByCata(category);
		} else {
			resources = resourceManager.getResourcesByNodeCata(nid, category, new Long(start),
					ipageNum);
			totalCount = resourceManager.getResourceCountByNodeCata(nid, category);
		}
		PageBuilder pb = new PageBuilder();
		pb.items((int) totalCount);
		pb.itemsPerPage(ipageNum.intValue());
		pb.page(ipage.intValue());
		//
		Node node = nodeManager.getNodeById(nid);
		model.put("resources", resources);
		model.put("node", node);
		model.put("action", this);
		model.put("page", ipage);
		model.put("category", category);
		model.put("totalCount", new Long(totalCount));
		model.put("haveLinks", haveLinks);
		//
		model.put("pageNum", ipageNum);
		//
		model.put("pb", pb);
		//
		return mv;
	}

	/**
	 * delete the resource
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
	public ModelAndView doDel(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws UnauthorizedException {
		ModelAndView mv = new ModelAndView(resourceLViewName, model);
		String nodeId = request.getParameter("nodeId");
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String category = request.getParameter("category");
		String haveLinks = request.getParameter("haveLinks");
		//
		if (!SecurityUtil.hasPermission(ResourcePermissionConstant.OBJECT_TYPE.toString(), nodeId,
				ResourcePermissionConstant.DeleteResource)) {
			throw new UnauthorizedException();
		}
		String[] resourceIds = request.getParameterValues("resourceId");
		if (resourceIds != null) {
			for (int i = 0; i < resourceIds.length; i++) {
				String resourceId = resourceIds[i];
				Long rsId = new Long(resourceId);
				resourceManager.deleteResource(rsId);
			}
		}
		if (nodeId == null) {
			nodeId = "0";
		}
		if (page == null) {
			page = "1";
		}
		if (pageNum == null) {
			pageNum = "18";
		}
		if (category == null) {
			category = "img";
		}
		if (haveLinks == null) {
			haveLinks = "2";
		}
		Long nid = new Long(nodeId);
		Long ipage = new Long(page);
		Long ipageNum = new Long(pageNum);
		List resources = null;
		int start = (ipage.intValue() - 1) * ipageNum.intValue();
		long totalCount = 0;
		if (nodeId.equals("0")) {
			resources = resourceManager.getResourcesByCata(category, new Long(start), ipageNum);
			totalCount = resourceManager.getResourceCountByCata(category);
		} else {
			resources = resourceManager.getResourcesByNodeCata(nid, category, new Long(start),
					ipageNum);
			totalCount = resourceManager.getResourceCountByNodeCata(nid, category);
		}
		PageBuilder pb = new PageBuilder();
		pb.items((int) totalCount);
		pb.itemsPerPage(ipageNum.intValue());
		pb.page(ipage.intValue());

		Node node = nodeManager.getNodeById(nid);
		model.put("resources", resources);
		model.put("node", node);
		model.put("action", this);
		model.put("page", ipage);
		model.put("category", category);
		model.put("totalCount", new Long(totalCount));
		model.put("haveLinks", haveLinks);
		//
		model.put("pageNum", ipageNum);
		//
		model.put("pb", pb);
		return mv;
	}

	public String getUserName(Long userId) {
		IUser bu = baseUserManager.getUserById(userId);
		if (bu != null) {
			return bu.getName();
		}
		return "";
	}

	//

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setBaseUserManager(IUserManager baseUserManager) {
		this.baseUserManager = baseUserManager;
	}

	public void setResourceFViewName(String resourceFViewName) {
		this.resourceFViewName = resourceFViewName;
	}

	public void setResourceHViewName(String resourceHViewName) {
		this.resourceHViewName = resourceHViewName;
	}

	public void setResourceLViewName(String resourceLViewName) {
		this.resourceLViewName = resourceLViewName;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setResourceXmlViewName(String resourceXmlViewName) {
		this.resourceXmlViewName = resourceXmlViewName;
	}

}
