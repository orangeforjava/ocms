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
package org.openuap.cms.node.security;

import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.user.security.manager.IResourceReflection;

/**
 * <p>
 * 用户资源反射，提供用户资源的权限信息
 * </p>
 * <p>
 * $Id: NodeResourceReflection.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeResourceReflection implements IResourceReflection {

	private NodeManager nodeManager;
	private String resourceSelectionUrl;
	private String caption;
	
	public NodeResourceReflection() {

		this.resourceSelectionUrl = "admin/node.jhtml?action=TargetNodeWindow";
		caption="站点选择";
	}

	public String getResourceDescription(String id) {
		if (nodeManager != null && id != null) {
			Node node = nodeManager.getNodeById(new Long(id));
			return node.getName();
		}
		return id;
	}

	public String getResourceSelectionUrl() {
		return resourceSelectionUrl;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setResourceSelectionUrl(String resourceSelectionUrl) {
		this.resourceSelectionUrl = resourceSelectionUrl;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

}
