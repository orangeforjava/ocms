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
package org.openuap.cms.node.model;

import org.openuap.cms.node.manager.NodeManager;

/**
 * <p>
 * 自定义结点描述.
 * </p>
 * 
 * <p>
 * $Id: CustomNodeDescriptor.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CustomNodeDescriptor implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5362321160710531006L;

	//
	private NodeManager nodeManager;

	private String iconPath;

	/**
	 * @return the nodeManager
	 */
	public NodeManager getNodeManager() {
		return nodeManager;
	}

	/**
	 * @param nodeManager
	 *            the nodeManager to set
	 */
	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	/**
	 * @return the iconPath
	 */
	public String getIconPath() {
		return iconPath;
	}

	/**
	 * @param iconPath
	 * 
	 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

}
