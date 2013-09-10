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
package org.openuap.cms.engine.impl;

import java.io.File;
import java.util.List;

import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.engine.content.ContentPublishEngine;
import org.openuap.cms.engine.generate.CmsStaticFileGenerateEngine;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.cms.engine.resource.ResourcePublishEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;

/**
 * <p>
 * 缺省发布引擎实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultPublishEngine.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultPublishEngine implements PublishEngine {

	/** 内容发布引擎. */
	private ContentPublishEngine contentPublishEngine;

	/** 静态文件产生引擎. */
	// 
	/** 资源发布引擎. */
	private ResourcePublishEngine resourcePublishEngine;

	/** 结点管理服务. */
	private NodeManager nodeManager;
	/** 静态文件产生引擎. */
	private CmsStaticFileGenerateEngine cmsStaticFileGenerateEngine;

	public void setCmsStaticFileGenerateEngine(
			CmsStaticFileGenerateEngine cmsStaticFileGenerateEngine) {
		this.cmsStaticFileGenerateEngine = cmsStaticFileGenerateEngine;
	}

	/**
	 * 
	 */
	public DefaultPublishEngine() {
	}

	/**
	 * 发布内容页
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            索引Id
	 * @param errors
	 *            发布过程中的错误
	 * @return boolean 若无错误返回true,若有错误返回false
	 */
	public boolean publishContent(Long nodeId, Long indexId,
			boolean refreshContent, List errors) {
		boolean forward = true;
		// 发布内容，处理内容
		try {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				//
				PublishProfileInfoHolder.getProfile().setStartActionTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.getProfile().setActionName(
						"publishContent-[node=" + nodeId + ";indexId="
								+ indexId + "]");
			}
			//调用内容发布引擎发布内容
			forward = contentPublishEngine.contentPublish(nodeId, indexId,
					refreshContent, errors);
//			if (forward && refreshContent) {
//				// 产生静态文件
//				try {
//					//调用静态文件产生引擎产生静态文件或内容
//					cmsStaticFileGenerateEngine.generateContentStaticFile(
//							nodeId, indexId, errors);
//				} catch (Exception e) {
//					forward = false;
//					e.printStackTrace();
//				}
//			}
//			if (forward && refreshContent) {
//				// 调用资源发布引擎发布相关的资源
//				forward = resourcePublishEngine.publishContentResource(nodeId,
//						indexId, errors);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				PublishProfileInfoHolder.getProfile().setStartRenderTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.logProfile();
				PublishProfileInfoHolder.clearProfile();
			}
		}
		return forward;
	}

	/**
	 * by Weiping Ju history: 1)add some logic process the nodeId=0
	 * 
	 * @todo 这个需要改进，否则跟这个架构不一致，也就是说要首先取出所有的需要发布的内容Id<br/>
	 *       以后的操作都基于这些id,这样也能提高处理速度 发布结点内容
	 * @param parentId
	 *            结点Id
	 * @param mode
	 *            发布模式
	 * @param errors
	 *            发布过程中的错误.
	 * @return boolean 若没有错误，则发挥true,否则返回false
	 */
	public boolean publishAllNodeContent(Long parentId, PublishEngineMode mode,
			List errors) {
		boolean success = true;
		if (parentId.intValue() == 0) {
			// it is root node
			// 如果是根结点，则返回根结点下的所有一级子结点，分别调用相关的方法
			List firstLevelNodes = nodeManager.getNodes(parentId, new Long(0),
					new Integer("0"));
			if (firstLevelNodes != null) {
				for (int i = 0; i < firstLevelNodes.size(); i++) {
					Node childNode = (Node) firstLevelNodes.get(i);
					success = contentPublishEngine.nodeAllContentPublish(
							childNode.getNodeId(), mode, errors);
				}
			}
		} else {
			// 发布结点下的所有内容
			success = contentPublishEngine.nodeAllContentPublish(parentId,
					mode, errors);
		}
		// success=staticFileGenerateEngine.generateAllNodeStaticFile(parentId,mode,errors);
		return success;
	}

	public void setResourcePublishEngine(
			ResourcePublishEngine resourcePublishEngine) {
		this.resourcePublishEngine = resourcePublishEngine;
	}

	public void setContentPublishEngine(
			ContentPublishEngine contentPublishEngine) {
		this.contentPublishEngine = contentPublishEngine;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	/**
	 * 删除发布后的静态文件
	 * 
	 * @param nodeId
	 *            Long
	 * @param indexId
	 *            Long
	 * @param errors
	 *            List
	 * @return boolean
	 */
	public boolean deletePublishedFile(Long nodeId, Long indexId, List errors) {
		try {
			File destFile = cmsStaticFileGenerateEngine.getDestFile(nodeId,
					indexId);
			if (destFile!=null&&destFile.exists()) {
				destFile.delete();
			}
		} catch (Exception ex) {
			errors.add(ex);
			return false;
		}
		return true;
	}

	public boolean refreshNodeIndex(Long nodeId, List errors) {
		try {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				//
				PublishProfileInfoHolder.getProfile().setStartActionTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.getProfile().setActionName(
						"refreshNodeIndex-[" + nodeId + "]");
			}
			cmsStaticFileGenerateEngine.generateNodeIndexStaticFile(nodeId,
					errors);
		} catch (Exception e) {
			return false;
		} finally {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				PublishProfileInfoHolder.getProfile().setStartRenderTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.logProfile();
			}
		}
		return true;
	}

	public boolean refreshNodeExtraIndex(Long nodeId, Long publishId,
			List errors) {
		try {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				//
				PublishProfileInfoHolder.getProfile().setStartActionTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.getProfile().setActionName(
						"refreshNodeExtraIndex-{node=[" + nodeId + "]"+","+"publishId=["+publishId+"]}");
			}
			//静态渲染引擎
			cmsStaticFileGenerateEngine.generateNodeExtraIndexStaticFile(
					nodeId, publishId, errors);
		} catch (Exception e) {
			return false;
		}finally {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				PublishProfileInfoHolder.getProfile().setStartRenderTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.logProfile();
				PublishProfileInfoHolder.clearProfile();
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean refreshNodeAllExtraIndex(Long nodeId, List errors) {

		try {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				//
				PublishProfileInfoHolder.getProfile().setStartActionTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.getProfile().setActionName(
						"refreshNodeIndex-[" + nodeId + "]");
			}
			cmsStaticFileGenerateEngine.generateNodeAllExtraIndexStaticFile(
					nodeId, errors);
		} catch (Exception e) {
			return false;
		}finally {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				PublishProfileInfoHolder.getProfile().setStartRenderTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.logProfile();
			}
		}
		return true;
	}

	/**
	 * 更新内容
	 * 
	 * @param nodeId
	 * 
	 * @param indexId
	 * 
	 * @param errors
	 * 
	 * @return
	 */
	public boolean refreshContent(Long nodeId, Long indexId, List errors) {
		boolean success = true;
		try {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				//
				PublishProfileInfoHolder.getProfile().setStartActionTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.getProfile().setActionName(
						"refreshContent-[node=" + nodeId + ";indexId="
								+ indexId + "]");
			}
			if (success) {
				// 静态文件发布
				try {
					//
					cmsStaticFileGenerateEngine.generateContentStaticFile(
							nodeId, indexId, errors);
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
					e.printStackTrace();
					errors.add(e);
				}
			}
			if (success) {
				// 资源发布
				success = resourcePublishEngine.publishContentResource(nodeId,
						indexId, errors);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (PublishProfileInfoHolder.isEnableProfile()) {
				PublishProfileInfoHolder.getProfile().setStartRenderTime(
						System.currentTimeMillis());
				PublishProfileInfoHolder.logProfile();
			}
		}
		return success;

	}

	public boolean refreshAllNodeContent(Long parentId, PublishEngineMode mode,
			List errors) {
		boolean success = true;
		if (parentId.intValue() == 0) {
			// it is root node
			List firstLevelNodes = nodeManager.getNodes(parentId, new Long(0),
					new Integer("0"));
			if (firstLevelNodes != null) {
				for (int i = 0; i < firstLevelNodes.size(); i++) {
					Node childNode = (Node) firstLevelNodes.get(i);
					cmsStaticFileGenerateEngine.generateAllNodeStaticFile(
							childNode.getNodeId(), mode, errors);
					success = resourcePublishEngine.refreshAllNodeResource(
							childNode.getNodeId(), mode, errors);
				}
			}
		} else {
			cmsStaticFileGenerateEngine.generateAllNodeStaticFile(parentId,
					mode, errors);
			success = resourcePublishEngine.refreshAllNodeResource(parentId,
					mode, errors);
		}
		return success;
	}

	public boolean unPublishContent(Long nodeId, Long indexId, List errors) {
		boolean success = true;
		success = contentPublishEngine
				.contentUnPublish(nodeId, indexId, errors);
		//删除静态文件
		success = deletePublishedFile(nodeId, indexId, errors);
		return success;
	}

	/**
	 * 
	 * @param nodeId
	 *            Integer
	 * @param indexId
	 *            Integer
	 * @param extraPublishId
	 *            Integer
	 * @param type
	 *            int
	 * @param tplContent
	 *            String
	 * @param page
	 *            int
	 * @param errors
	 *            List
	 * @return String
	 */
	public String previewTemplate(Long nodeId, Long indexId,
			Long extraPublishId, int type, String tplContent, int page,
			List errors) {
		return cmsStaticFileGenerateEngine.previewTemplate(nodeId, indexId,
				tplContent, extraPublishId, type, page, errors);
	}

	public boolean republishAllNodeContent(Long parentId,
			PublishEngineMode mode, List errors) {
		boolean success = true;
		if (parentId.intValue() == 0) {
			// it is root node
			List firstLevelNodes = nodeManager.getNodes(parentId, new Long(0),
					new Integer("0"));
			if (firstLevelNodes != null) {
				for (int i = 0; i < firstLevelNodes.size(); i++) {
					Node childNode = (Node) firstLevelNodes.get(i);
					success = contentPublishEngine.nodeAllContentRePublish(
							childNode.getNodeId(), mode, errors);
				}
			}
		} else {
			success = contentPublishEngine.nodeAllContentRePublish(parentId,
					mode, errors);
		}
		return success;

	}

	public boolean unpublishAllNodeContent(Long parentId,
			PublishEngineMode mode, List errors) {
		boolean success = true;
		if (parentId.intValue() == 0) {
			// it is root node
			List firstLevelNodes = nodeManager.getNodes(parentId, new Long(0),
					new Integer("0"));
			if (firstLevelNodes != null) {
				for (int i = 0; i < firstLevelNodes.size(); i++) {
					Node childNode = (Node) firstLevelNodes.get(i);
					success = contentPublishEngine.nodeAllContentUnPublish(
							childNode.getNodeId(), mode, errors);
				}
			}
		} else {
			success = contentPublishEngine.nodeAllContentUnPublish(parentId,
					mode, errors);
		}
		return success;

	}

	public String getContent(Long nodeId, Long indexId, int page, List errors) {
		return cmsStaticFileGenerateEngine.getContent(nodeId, indexId, page,
				errors,false);
	}

	public String getNodeIndex(Long nodeId, int page, List errors) {
		return cmsStaticFileGenerateEngine.getNodeIndex(nodeId, page, errors);
	}

	public String getExtraContent(Long nodeId, Long publishId, int page,
			List errors) {
		return cmsStaticFileGenerateEngine.getExtraContent(nodeId, publishId,
				page, errors);
	}

	
	public String getPreviewContent(Long nodeId, Long indexId, int page,
			List errors) {
		return cmsStaticFileGenerateEngine.getContent(nodeId, indexId, page,
				errors,true);
	}
}
