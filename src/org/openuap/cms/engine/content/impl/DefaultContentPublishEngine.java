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
package org.openuap.cms.engine.content.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.util.MultiResField;
import org.openuap.cms.cm.util.ResRefBean;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.event.PublishEvent;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.engine.content.ContentPublishEngine;
import org.openuap.cms.engine.generate.CmsStaticFileGenerateEngine;
import org.openuap.cms.engine.resource.ResourcePublishEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 缺省内容发布引擎实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultContentPublishEngine.java 3966 2010-12-16 12:10:02Z orangeforjava
 * $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultContentPublishEngine implements ContentPublishEngine,
		ApplicationContextAware {

	private DynamicContentManager dynamicContentManager;

	private NodeManager nodeManager;

	private PsnManager psnManager;

	private ContentFieldManager contentFieldManager;

	private ResourcePublishEngine resourcePublishEngine;

	private CmsStaticFileGenerateEngine cmsStaticFileGenerateEngine;

	private ApplicationContext applicationContext;

	/**
	 * 
	 */
	public DefaultContentPublishEngine() {
	}

	/**
	 * content publish<br/>
	 * <p>
	 * 1)if the node is static publish<br/>
	 * 2)if the node is dynamic publish<br/>
	 * a)update the url,it should be <br/>
	 * the http://xxxx.xxxx/xx.jhtml?nodeId=xx&contentId=?<br/>
	 * and the conent will be dynamic from database.<br/>
	 * b)set the state to "1"<br/>
	 * 3)if the node is non-publish<br/>
	 * should not be process.
	 * </p>
	 * 
	 * @param nodeId
	 * 
	 * @param indexId
	 * 
	 * @param errors
	 * 
	 * @return boolean
	 */
	public boolean contentPublish(Long nodeId, Long indexId,
			boolean refreshContent, List errors) {
		// 获取结点信息
		Node node = nodeManager.getNode(nodeId);
		if (node == null) {
			return false;
		}
		// 获得内容索引(TODO 是否有必要？)
		ContentIndex ci = dynamicContentManager.getContentIndexById(indexId);
		if (ci == null) {
			return false;
		}
		return contentPublish(node, ci, refreshContent, errors);
	}

	public boolean contentPublish(Long nodeId, ContentIndex ci,
			boolean refreshContent, List errors) {
		Node node = nodeManager.getNode(nodeId);
		return contentPublish(node, ci, refreshContent, errors);
	}

	/**
	 * 
	 * @param node
	 *            结点
	 * @param ci
	 *            内容索引
	 * @param refreshContent
	 *            是否生成静态文件
	 * @param errors
	 *            发布过程中的错误
	 * @return
	 */
	protected boolean contentPublish(Node node, ContentIndex ci,
			boolean refreshContent, List errors) {
		// 获得结点发布模式
		Integer publishMode = node.getPublishMode();
		// 获得内容自身的发布模式
		Integer selfPublishMode = ci.getPublishMode();
		if (selfPublishMode != null && !selfPublishMode.equals(new Integer(-1))) {
			// 内容自身设置了发布模式，采用内容自身的发布模式处理
			publishMode = selfPublishMode;
		}

		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			// 静态发布模式
			return staticContentPublish(node, ci, refreshContent, errors);
		} else if (publishMode.equals(PublishMode.DYNAMIC_MODE.getMode())) {
			// 动态发布模式
			return dynamicContentPublish(node, ci, errors);
		} else if (publishMode.equals(PublishMode.NO_MODE.getMode())) {
			// 不发布模式
			return false;
		}
		return false;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setResourcePublishEngine(
			ResourcePublishEngine resourcePublishEngine) {
		this.resourcePublishEngine = resourcePublishEngine;
	}

	/**
	 * 静态内容发布
	 * 
	 * @param node
	 *            结点
	 * @param contentIndex
	 *            内容索引
	 * @param errors
	 *            错误列表
	 * @return boolean
	 */
	protected boolean staticContentPublish(Node node,
			ContentIndex contentIndex, boolean refreshContent, List errors) {
		try {
			Long tableId = node.getTableId();
			//
			// (1)get the url info
			String url = "";
			url = getContentUrl(contentIndex, node);
			//

			// 2)insert or update the publish_?
			// 获得内容模型属性信息
			List cfs = contentFieldManager.getContentFieldsFromCache(tableId);
			// 获得具体内容信息
			Object cobj = dynamicContentManager.getDynamicContent(contentIndex
					.getContentId(), tableId);

			Map ci = null;
			Map content = null;
			//
			if (cobj instanceof Map) {
				content = (Map) cobj;
			}
			Map publish = new HashMap();
			// 固定字段
			publish.put("indexId", contentIndex.getIndexId());
			publish.put("contentId", contentIndex.getContentId());
			publish.put("nodeId", contentIndex.getNodeId());
			// 内容模型id
			publish.put("tableId", contentIndex.getTableId());
			publish.put("publishDate", contentIndex.getPublishDate());
			publish.put("url", url);
			// the top/sort/pink
			publish.put("top", contentIndex.getTop());
			publish.put("sort", contentIndex.getSort());
			publish.put("pink", contentIndex.getPink());
			// the node name and node url
			publish.put("nodeName", node.getName());
			publish.put("nodeUrl", node.getNodeUrl());
			// added fixed field
			publish.put("creationDate", content.get("creationDate"));
			publish.put("modifiedDate", content.get("modifiedDate"));
			publish.put("creationUserId", content.get("creationUserId"));
			publish.put("creationUserName", content.get("creationUserName"));
			publish
					.put("lastModifiedUserId", content
							.get("lastModifiedUserId"));
			publish.put("lastModifiedUserName", content
					.get("lastModifiedUserName"));
			publish
					.put("contributionUserId", content
							.get("contributionUserId"));
			publish.put("contributionUserName", content
					.get("contributionUserName"));
			publish.put("contributionId", content.get("contributionId"));
			// the dynamic field
			// get the resouce url to used replace ../resource/?
			String destResourceUrl = "";
			String rsUrl = node.getResourceUrl();
			// 资源url
			destResourceUrl = psnManager.getPsnUrlInfo(rsUrl);

			for (int i = 0; i < cfs.size(); i++) {
				ContentField cf = (ContentField) cfs.get(i);
				String type = cf.getFieldType();
				String input = cf.getFieldInput();
				String inputPicker = cf.getFieldInputPicker();
				if (inputPicker == null) {
					inputPicker = "";
				}
				String name = cf.getFieldName();
				Object raw_value = content.get(name);
				Object value = raw_value;
				if (type.equals("varchar")) {
					// text
					if (inputPicker.equals("upload")
							|| inputPicker.equals("upload_attach")
							|| inputPicker.equals("flash")) {
						//
						String svalue = (String) raw_value;
						if (svalue != null) {
							svalue = svalue.replaceAll("\\.\\.\\/resource",
									destResourceUrl);
							value = svalue;
						}

					}
				} else if (type.equals("integer")) {
					// integer
					// value = new Integer(raw_value);
				} else if (type.equals("float")) {
					// float
					// value = new Float(raw_value);
				} else if (type.equals("text")) {
					// text,as string
					if (input.equals("RichEditor")) {
						String svalue = (String) raw_value;
						if (svalue != null) {
							svalue = svalue.replaceAll("\\.\\.\\/resource",
									destResourceUrl);
							value = svalue;
						}
					}
					if (input.equals("MultiImg")) {
						String svalue = (String) raw_value;
						if (svalue != null) {
							MultiResField multiField = MultiResField
									.fieldFromString(svalue);
							if (multiField.getNums() > 0) {
								List<ResRefBean> beans = multiField.getReses();
								for (ResRefBean bean : beans) {
									String resurl = bean.getUrl();
									resurl = resurl.replaceAll(
											"\\.\\.\\/resource",
											destResourceUrl);
									bean.setUrl(resurl);
								}
							}
							value = multiField.toString();
						}
						
					}
				}
				publish.put(name, value);
			}
			// 保存发布表信息(*_publish)
			dynamicContentManager.savePublish(tableId, publish);
			// 同步模式发布
			contentIndex.setState(ContentIndex.STATE_PUBLISHED);
			contentIndex.setUrl(url);
			dynamicContentManager.saveContentIndex(contentIndex);
			// 发送内容发布事件，同步方式
			PublishEvent event = new PublishEvent(
					PublishEvent.CONTENT_PUBLISHED, publish, null, this);
			event.setAsynchronous(false);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			// this.applicationContext.publishEvent(event);
			//
			if (refreshContent) {
				// 静态文件生成
				cmsStaticFileGenerateEngine.generateContentStaticFile(node,
						contentIndex, errors);
				// 资源发布
				resourcePublishEngine.publishContentResource(node.getNodeId(),
						contentIndex.getIndexId(), errors);
			}
			// 完成发布,分发完成发布事件,异步方式
			PublishEvent event2 = new PublishEvent(
					PublishEvent.CONTENT_FINISH_PUBLISH, publish, null, this);
			WebPluginManagerUtils.dispatcherEvent(true, "base", event2);
		}

		catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			// 出现异常，取消发布
			this.staticContentUnPublish(node, contentIndex, errors);
			return false;
		}
		return true;
	}

	/**
	 * 静态内容取消发布
	 * 
	 * @param node
	 * @param contentIndex
	 * @param errors
	 * @return
	 */
	protected boolean staticContentUnPublish(Node node,
			ContentIndex contentIndex, List errors) {
		try {

			// 设置为未发布状态
			contentIndex.setState(ContentIndex.STATE_UNPUBLISHED);
			dynamicContentManager.saveContentIndex(contentIndex);
			//
			Long tableId = node.getTableId();
			// 删除发布内容（publish表）
			dynamicContentManager.deletePublish(tableId, contentIndex
					.getIndexId());
			// 分发内容取消发布事件，同步方式
			Map p = new HashMap();
			p.put("indexId", contentIndex.getIndexId());
			p.put("nodeId", node.getNodeId());
			PublishEvent event = new PublishEvent(
					PublishEvent.CONTENT_UNPUBLISHED, p, null, this);
			event.setAsynchronous(false);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			// this.applicationContext.publishEvent(event);
			// 完成发布,分发完成发布事件,异步方式
			PublishEvent event2 = new PublishEvent(
					PublishEvent.CONTENT_FINISH_PUBLISH, p, null, this);
			WebPluginManagerUtils.dispatcherEvent(true, "base", event2);
		}

		catch (Exception ex) {
			errors.add(ex);
			return false;
		}
		return true;
	}

	/**
	 * 动态内容发布
	 * 
	 * @param node
	 *            结点
	 * @param contentIndex
	 *            内容索引
	 * @param errors
	 *            错误
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	protected boolean dynamicContentPublish(Node node,
			ContentIndex contentIndex, List errors) {
		//
		try {
			// 获得结点定义的内容查看用的Portal
			String contentUrl = node.getContentPortalUrl();
			// 获得内容自身的内容查看用Portal
			String selfContentIndex = contentIndex.getContentPortalUrl();
			if (selfContentIndex != null && !selfContentIndex.equals("")) {
				contentUrl = selfContentIndex;
			}
			//
			contentUrl = contentUrl.replaceAll("\\{ContentID\\}", contentIndex
					.getIndexId().toString());
			contentUrl = contentUrl.replaceAll("\\{NodeID\\}", contentIndex
					.getNodeId().toString());
			// 校准内容Portal URL
			String baseUrl = CMSConfig.getInstance().getBaseUrl();
			if (baseUrl.endsWith("/")) {
				baseUrl.substring(0, baseUrl.length() - 1);
			}
			//
			if (!contentUrl.startsWith("http")) {
				contentUrl = baseUrl + "/" + contentUrl;
			}
			// 获得内容模型信息
			Long tableId = node.getTableId();
			// 获得内容属性信息
			// 从缓存中获取内容属性信息
			List cfs = contentFieldManager.getContentFieldsFromCache(tableId);
			// 获得动态内容信息
			Object cobj = dynamicContentManager.getDynamicContent(contentIndex
					.getContentId(), tableId);
			// get the content
			Map ci = null;
			Map content = null;
			//
			if (cobj instanceof Map) {
				// Object[] ct = (Object[]) cobj;
				// ci = (Map) ct[0];
				content = (Map) cobj;
			}
			Map publish = new HashMap();
			// the fixed field
			publish.put("indexId", contentIndex.getIndexId());
			publish.put("contentId", contentIndex.getContentId());
			publish.put("nodeId", contentIndex.getNodeId());
			publish.put("publishDate", contentIndex.getPublishDate());
			publish.put("url", contentUrl);
			// the top/sort/pink
			publish.put("top", contentIndex.getTop());
			publish.put("sort", contentIndex.getSort());
			publish.put("pink", contentIndex.getPink());
			// the node name and node url
			publish.put("nodeName", node.getName());
			publish.put("nodeUrl", node.getNodeUrl());
			//
			publish.put("creationDate", content.get("creationDate"));
			publish.put("modifiedDate", content.get("modifiedDate"));
			publish.put("creationUserId", content.get("creationUserId"));
			publish.put("creationUserName", content.get("creationUserName"));
			publish
					.put("lastModifiedUserId", content
							.get("lastModifiedUserId"));
			publish.put("lastModifiedUserName", content
					.get("lastModifiedUserName"));
			publish
					.put("contributionUserId", content
							.get("contributionUserId"));
			publish.put("contributionUserName", content
					.get("contributionUserName"));
			publish.put("contributionId", content.get("contributionId"));
			//

			for (int i = 0; i < cfs.size(); i++) {
				ContentField cf = (ContentField) cfs.get(i);
				String type = cf.getFieldType();
				String input = cf.getFieldInput();
				String inputPicker = cf.getFieldInputPicker();
				if (inputPicker == null) {
					inputPicker = "";
				}
				String name = cf.getFieldName();
				Object raw_value = content.get(name);
				Object value = raw_value;
				if (type.equals("varchar")) {
					// text
					if (inputPicker.equals("upload")
							|| inputPicker.equals("upload_attach")
							|| inputPicker.equals("flash")) {
						//
						String svalue = (String) raw_value;
						if (svalue != null) {
							// svalue = svalue.replaceAll("\\.\\.\\/resource",
							// destResourceUrl);
							value = svalue;
						}

					}
				} else if (type.equals("integer")) {
					// integer
					// value = new Integer(raw_value);
				} else if (type.equals("float")) {
					// float
					// value = new Float(raw_value);
				} else if (type.equals("text")) {
					// text,as string
					if (input.equals("RichEditor")) {
						String svalue = (String) raw_value;
						if (svalue != null) {
							// svalue = svalue.replaceAll("\\.\\.\\/resource",
							// destResourceUrl);
							value = svalue;
						}
					}

				}
				publish.put(name, value);
			}
			// 保存发布对象publish
			dynamicContentManager.savePublish(tableId, publish);
			//
			// 设置内容为发布状态
			contentIndex.setState(ContentIndex.STATE_PUBLISHED);
			contentIndex.setUrl(contentUrl);
			dynamicContentManager.saveContentIndex(contentIndex);
			//
			// 发送内容发布事件，同步方式
			PublishEvent event = new PublishEvent(
					PublishEvent.CONTENT_PUBLISHED, publish, null, this);
			event.setAsynchronous(false);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			//
			// 完成发布,分发完成发布事件,异步方式
			PublishEvent event2 = new PublishEvent(
					PublishEvent.CONTENT_FINISH_PUBLISH, publish, null, this);
			WebPluginManagerUtils.dispatcherEvent(true, "base", event2);
			//
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			return false;
		}
	}

	protected boolean dynamicContentUnPublish(Node node,
			ContentIndex contentIndex, List errors) {
		try {
			Long tableId = node.getTableId();
			dynamicContentManager.deletePublish(tableId, contentIndex
					.getIndexId());
			//
			contentIndex.setState(ContentIndex.STATE_UNPUBLISHED);
			dynamicContentManager.saveContentIndex(contentIndex);
			//
			Map publish = new HashMap();
			publish.put("indexId", contentIndex.getIndexId());
			publish.put("nodeId", node.getNodeId());
			// 发送内容发布事件，同步方式
			PublishEvent event = new PublishEvent(
					PublishEvent.CONTENT_PUBLISHED, publish, null, this);
			event.setAsynchronous(false);
			WebPluginManagerUtils.dispatcherEvent(false, "base", event);
			//
			// 完成发布,分发完成发布事件,异步方式
			PublishEvent event2 = new PublishEvent(
					PublishEvent.CONTENT_FINISH_PUBLISH, publish, null, this);
			WebPluginManagerUtils.dispatcherEvent(true, "base", event2);
		} catch (Exception ex) {
			errors.add(ex);
			return false;
		}
		return true;

	}

	// ////////////////////////////////////////////////////////////////////////////
	public String getSubDirName(String subDir, long timeStamp) {
		String destDir = "";
		if (subDir != null && !subDir.equals("") && !subDir.equals("none")) {
			String ft = "yyyy-MM-dd";
			if (!subDir.equals("auto")) {
				subDir = subDir.replaceAll("Y", "yyyy");
				subDir = subDir.replaceAll("m", "MM");
				subDir = subDir.replaceAll("d", "dd");
				ft = subDir;
			}
			SimpleDateFormat sf = new SimpleDateFormat(ft);
			// destDir = sf.format(new Date(timeStamp * 1000l));
			destDir = sf.format(new Date(timeStamp));
		}
		return destDir;
	}

	public String getPublishFileName(String fileName, long timeStamp,
			ContentIndex ci) {
		String destFileName = fileName;
		destFileName = destFileName.replaceAll("\\{TimeStamp\\}", ""
				+ timeStamp);
		destFileName = destFileName.replaceAll("\\{ContentID\\}", ci
				.getContentId().toString());

		destFileName = destFileName.replaceAll("\\{IndexID\\}", ci.getIndexId()
				.toString());
		destFileName = destFileName.replaceAll("\\{NodeID\\}", ci.getNodeId()
				.toString());
		return destFileName;
	}

	/**
	 * modified by Weiping Ju to solve the bug.
	 * 
	 * @param nodeId
	 * 
	 * @param mode
	 * 
	 * @param errors
	 * 
	 * @return boolean
	 */
	public boolean nodeAllContentPublish(Long nodeId, PublishEngineMode mode,
			List errors) {
		boolean success = true;
		// 每次处理数目
		int pageSize = mode.getProcessContentNums();
		long publishNums = dynamicContentManager
				.getNodeUnPubllishContentCount(nodeId);
		boolean refreshContent = mode.isRefreshContent();
		while (publishNums > 0) {
			List ciList = dynamicContentManager.getNodeUnPublishContents(
					nodeId, new Long(0), new Long(pageSize));
			if (ciList != null) {
				for (int j = 0; j < ciList.size(); j++) {
					ContentIndex ci = (ContentIndex) ciList.get(j);
					// publish content
					contentPublish(nodeId, ci, refreshContent, errors);
				}
			}
			publishNums = dynamicContentManager
					.getNodeUnPubllishContentCount(nodeId);
		}
		// int pageSize = mode.getProcessContentNums();
		// int totalPage = (int) (publishNums / pageSize);
		// if (publishNums % pageSize > 0) {
		// totalPage++;
		// }

		// System.out.println("publishNums="+publishNums);
		// System.out.println("pageSize="+pageSize);
		// System.out.println("totalPage="+totalPage);
		// for (int i = 0; i < totalPage; i++) {
		//
		// }
		if (mode.isContainChildNode()) {

			List childNodes = nodeManager.getChildNodes(nodeId);
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node childNode = (Node) childNodes.get(i);
					// recursive invoke
					nodeAllContentPublish(childNode.getNodeId(), mode, errors);
				}
			}
		}
		return success;
	}

	/**
	 * 
	 * 
	 * @param nodeId
	 *            Integer
	 * @param mode
	 *            PublishEngineMode
	 * @param errors
	 *            List
	 * @return boolean
	 */
	public boolean nodeAllContentUnPublish(Long nodeId, PublishEngineMode mode,
			List errors) {
		boolean success = true;
		int pageSize = mode.getProcessContentNums();
		long publishNums = dynamicContentManager
				.getNodePublishContentCount(nodeId);
		while (publishNums > 0) {
			List ciList = dynamicContentManager.getNodePublishContents(nodeId,
					new Long(0), new Long(pageSize));
			if (ciList != null) {
				for (int j = 0; j < ciList.size(); j++) {
					ContentIndex ci = (ContentIndex) ciList.get(j);
					// publish content
					success = contentUnPublish(nodeId, ci.getIndexId(), errors);
				}
			}
			publishNums = dynamicContentManager
					.getNodePublishContentCount(nodeId);
		}
		// int totalPage = (int) (publishNums / pageSize);
		// if (publishNums % pageSize > 0) {
		// totalPage++;
		// }
		// for (int i = 0; i < totalPage; i++) {
		//
		// }
		if (mode.isContainChildNode()) {
			List childNodes = nodeManager.getNodes(nodeId, new Long(0),
					new Integer("0"));
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node childNode = (Node) childNodes.get(i);
					// recursive invoke
					success = nodeAllContentUnPublish(childNode.getNodeId(),
							mode, errors);
				}
			}
		}
		return success;
	}

	/**
	 * 内容反发布
	 */
	public boolean contentUnPublish(Long nodeId, Long indexId, List errors) {
		Node node = nodeManager.getNode(nodeId);
		Integer publishMode = node.getPublishMode();
		ContentIndex ci = dynamicContentManager.getContentIndexById(indexId);
		// the static publish mode
		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			return staticContentUnPublish(node, ci, errors);
		} else if (publishMode.equals(PublishMode.DYNAMIC_MODE.getMode())) {
			// the dynamic publish mode
			return dynamicContentUnPublish(node, ci, errors);
		} else if (publishMode.equals(PublishMode.NO_MODE.getMode())) {
			// the none-publish mode
			return false;
		}
		return false;
	}

	/**
	 * 获得内容URL信息 首先获取自身的url定义，若自身没有定义则使用结点的设置
	 * 
	 * @param contentIndex
	 * @param node
	 * @return
	 */
	private String getContentUrl(ContentIndex contentIndex, Node node) {
		String url = "";
		// first look up the self url,if exist,it is,otherwise
		// from the node info,get the url info
		// after get the url,need get the file name
		// first get the self filename,if not exist,need get from the node info
		// from node info,include the path name and file name
		// cat the url and the file name is the finla url
		String selfUrl = contentIndex.getSelfUrl();
		String selfFileName = contentIndex.getSelfPublishFileName();
		// 发布日期
		long timeStamp = contentIndex.getPublishDate().longValue();
		if (StringUtils.hasText(selfUrl)) {
			// get from self url
			url = selfUrl;
		} else {
			// get from node info,psn url defintion.
			String nodeUrl = node.getContentUrl();
			url = psnManager.getPsnUrlInfo(nodeUrl);
		}
		if (StringUtils.hasText(selfFileName)) {
			// 若自定义发布文件名
			url += "/" + selfFileName;
		} else {
			// 根据规则生成URL
			String subDir = node.getSubDir();
			String destDir = this.getSubDirName(subDir, timeStamp);
			if (!destDir.equals("")) {
				url += "/" + destDir;
			}
			//
			// String stimeStamp = "" + timeStamp;
			String fileName = node.getPublishFileFormat();
			//
			fileName = getPublishFileName(fileName, timeStamp, contentIndex);
			if (!fileName.equals("")) {
				url += "/" + fileName;
			}
		}
		return url;
	}

	public boolean nodeAllContentRePublish(Long nodeId, PublishEngineMode mode,
			List errors) {
		boolean success = this.nodeAllContentUnPublish(nodeId, mode, errors);
		success = this.nodeAllContentPublish(nodeId, mode, errors);
		return success;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}

	public void setCmsStaticFileGenerateEngine(
			CmsStaticFileGenerateEngine cmsStaticFileGenerateEngine) {
		this.cmsStaticFileGenerateEngine = cmsStaticFileGenerateEngine;
	}
}
