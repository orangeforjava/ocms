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
package org.openuap.cms.node.permission.event;

import org.openuap.cms.node.event.NodeEvent;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.node.permission.manager.NodePermissionManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * 结点事件监听器
 * </p>
 * 
 * <p>
 * $Id: PermissionNodeEventListener.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PermissionNodeEventListener implements ApplicationListener{
	
	private NodePermissionManager nodePermissionManager;
	
	public NodePermissionManager getNodePermissionManager() {
		return nodePermissionManager;
	}

	public void setNodePermissionManager(NodePermissionManager nodePermissionManager) {
		this.nodePermissionManager = nodePermissionManager;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof NodeEvent) {
			NodeEvent nodeEvent = (NodeEvent) event;
			String type = nodeEvent.getEventType();
			Node node=nodeEvent.getNode();
			
			if (type.equals(NodeEvent.NODE_CREATED)) {
				//监听结点变更来进行权限初始化操作
				getNodePermissionManager().initNodeRole(node.getNodeId());
			}
		}
		
	}

}
