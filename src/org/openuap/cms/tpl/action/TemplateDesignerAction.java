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
package org.openuap.cms.tpl.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 模板设计器控制器.
 * </p>
 * 
 * <p>
 * $Id: TemplateDesignerAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateDesignerAction extends AdminAction {

	private NodeManager nodeManager;

	//

	/**
	 * 
	 */
	public TemplateDesignerAction() {
	}

	public ModelAndView doCmsListNodeId(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		/*String nodeId = request.getParameter("nodeId");
		String viewName = request.getParameter("viewName");
		String nodeGUID = request.getParameter("nodeGUID");
		List nodes = new ArrayList();
		if (nodeId != null && !nodeId.trim().equals("")) {
			if (!nodeId.equals("self")) {
				int pos = nodeId.lastIndexOf("all-");
				if (pos == -1) {
					String[] snidAry = nodeId.split(",");
					for (int i = 0; i < snidAry.length; i++) {
						Long nid = new Long(snidAry[i]);
						Node node = nodeManager.getNodeById(nid);
						if (node == null) {
							node = new Node(nid);
							node.setName("[警告]不存在的结点");
							node.setTableId(new Long(-1000));
						}
						nodes.add(node);
					}

				} else {
					String snid = nodeId.substring(4);
					Long nid = new Long(snid);
					Node node = nodeManager.getNodeById(nid);
					if (node == null) {
						node = new Node(nid);
						node.setName("[警告]不存在的结点");
						node.setTableId(new Long(-1000));
					}
					nodes.add(node);
				}
			}
		} else {

			if (nodeGUID != null && !nodeGUID.trim().equals("")) {
				Node node = nodeManager.getNodeByGuid(nodeGUID);
				if (node == null) {
					node = new Node(new Long(-1));
					node.setNodeGuid(nodeGUID);
					node.setName("[警告]不存在的结点");
					node.setTableId(new Long(-1000));
				}
				nodes.add(node);
			}
		}
		//
		model.put("nodes", nodes);
		ModelAndView mv = new ModelAndView(viewName, model);
		return mv;*/
//    	add
		System.out.println("zhangchunli----2007-6-11------>modify ");
		return null;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

}
