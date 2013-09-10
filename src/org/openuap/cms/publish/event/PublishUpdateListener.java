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
package org.openuap.cms.publish.event;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.openuap.cms.core.event.PublishEvent;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.node.cache.NodeCache;
import org.openuap.cms.node.event.NodeEvent;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.publish.manager.ExtraPublishManager;
import org.openuap.cms.publish.model.ExtraPublish;
import org.openuap.cms.repo.cache.RepoCache;
import org.openuap.cms.schedule.JobEntry;
import org.openuap.cms.schedule.WorkerHelper;
import org.openuap.cms.util.ui.AutoRefreshMode;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * 发布更新监听器.
 * </p>
 * 
 * <p>
 * $Id: PublishUpdateListener.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishUpdateListener implements ApplicationListener {
	private ExtraPublishManager extraPublishManager;
	private PublishEngine publishEngine;

	public PublishEngine getPublishEngine() {
		return publishEngine;
	}

	public void setPublishEngine(PublishEngine publishEngine) {
		this.publishEngine = publishEngine;
	}

	public void setExtraPublishManager(ExtraPublishManager extraPublishManager) {
		this.extraPublishManager = extraPublishManager;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof PublishEvent) {
			PublishEvent pEvent = (PublishEvent) event;
			Map publish = pEvent.getPublish();
			int eventType = pEvent.getEventType();

			if (eventType == PublishEvent.CONTENT_FINISH_PUBLISH) {
				//
				updateExtraPublish(publish);
				updateNodeIndex(publish);
				//清除结点缓存
				updateNodeStat(publish);
			}
		}
		if(event instanceof NodeEvent){
			//若是结点事件
			NodeEvent nEvent = (NodeEvent) event;
			String eventType=nEvent.getEventType();
			Long nid=nEvent.getNode().getNodeId();
			if(NodeEvent.NODE_DELETED.equals(eventType)){
				unpublishNode(nid);
			}
		}

	}
	/**
	 * 取消发布结点
	 * @param nid
	 */
	@SuppressWarnings("unchecked")
	protected void unpublishNode(Long nid){
		// 准备计划任务属性
		Hashtable prop = new Hashtable();
		//
		prop.put("containChildNode","true");
		prop.put("containExtraPublish","true");
		prop.put("containContent", "true");
		prop.put("containIndex", "true");
		prop.put("processContentNums", "100");
		prop.put("nodeId", String.valueOf(nid));
		prop.put("mode", new Integer(PublishEngineMode.UNPUBLISH_MODE));
		//prop.put("triggerEvent", value);
		//
		try {
			JobEntry job = new JobEntry(0, 0, 0, 0, 0,
					"org.openuap.cms.engine.task.PublishNodeTask");
			job.setProperty(prop);
			// 启动任务
			WorkerHelper.startTask(job);
			//
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	protected void updateExtraPublish(Map publish) {
		Long indexId = (Long) publish.get("indexId");
		Long nodeId = (Long) publish.get("nodeId");
//		System.out.println("接收到需要自动刷新事件:indexId=" + indexId + ";nodeId="
//				+ nodeId);
		List<ExtraPublish> extraPublishes = extraPublishManager
				.getAutoRefreshPublish(nodeId);
		List errors = new ArrayList();
		for (ExtraPublish p : extraPublishes) {
			//
//			System.out.println("执行自动刷新:publishId=" + p.getPublishId()
//					+ ";autoRefreshMode=" + p.getAutoRefreshMode() + ";name="
//					+ p.getPublishName());
			getPublishEngine().refreshNodeExtraIndex(p.getNodeId(),
					p.getPublishId(), errors);
		}
	}
	protected void updateNodeIndex(Map publish) {
		Long nodeId = (Long) publish.get("nodeId");
		//
		Node node=NodeCache.getNode(nodeId);
		List <Node> nodes=new ArrayList<Node>();
		if(node!=null&&node.getAutoRefreshMode()>=AutoRefreshMode.SELF_REFRESH_MODE.getMode()){
			if(!nodes.contains(node)){
				nodes.add(node);
			}
			Node parentNode=NodeCache.getParentNode(nodeId);
			while(parentNode!=null){
				if(parentNode.getAutoRefreshMode()>=AutoRefreshMode.PARENT_REFRESH_MODE.getMode()){
					if(!nodes.contains(parentNode)){
						nodes.add(parentNode);
					}
				}
				parentNode=NodeCache.getParentNode(parentNode.getNodeId());
			}
		}
		List<Node> globalNodes=NodeCache.getGlobalRefreshNodes();
		for(Node gn:globalNodes){
			if(!nodes.contains(gn)){
				nodes.add(gn);
			}
		}
		//
		List errors = new ArrayList();
		for(Node n:nodes){
//			System.out.println("执行结点自动刷新:nodeId=" + n.getNodeId()
//					+ ";autoRefreshMode=" + n.getAutoRefreshMode() + ";name="
//					+ n.getName());
			getPublishEngine().refreshNodeIndex(n.getNodeId(), errors);
		}
		
	}
	protected  void updateNodeStat(Map publish) {
		Long nodeId = (Long) publish.get("nodeId");
		Node node=NodeCache.getNode(nodeId);
		if(node!=null){
			RepoCache.clearNodePublishStat(nodeId);
			//
			Node parentNode=NodeCache.getParentNode(nodeId);
			while(parentNode!=null){
				RepoCache.clearNodePublishStat(parentNode.getNodeId());
				parentNode=NodeCache.getParentNode(parentNode.getNodeId());
			}
		}
	}

}
