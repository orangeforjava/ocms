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
package org.openuap.cms.engine.generate.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.engine.generate.CmsStaticFileGenerateEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.publish.manager.ExtraPublishManager;
import org.openuap.cms.publish.model.ExtraPublish;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.runtime.util.ObjectLocator;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateEngine;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 缺省CMS静态内容生成
 * </p>
 * 
 * <p>
 * $Id: DefaultCmsStaticFileGenerateEngine.java 3924 2010-10-26 11:53:36Z
 * orangeforjava $
 * </p>
 * 
 * @author Weiping Ju
 * 
 */
public class DefaultCmsStaticFileGenerateEngine implements
		CmsStaticFileGenerateEngine {

	private DynamicContentManager dynamicContentManager;

	private NodeManager nodeManager;

	private TemplateEngine templateEngine;

	private PsnManager psnManager;

	private ExtraPublishManager extraPublishManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * generateAllNodeStaticFile(java.lang.Long,
	 * org.openuap.cms.publish.engine.PublishEngineMode, java.util.List)
	 */
	public void generateAllNodeStaticFile(Long parentId,
			PublishEngineMode mode, List errors) {

		// 处理首页
		if (mode.isContainIndex()) {
			generateNodeIndexStaticFile(parentId, errors);
		}
		// 处理附加发布
		if (mode.isContainExtraPublish()) {
			generateNodeAllExtraIndexStaticFile(parentId, errors);
		}
		// 处理内容页
		if (mode.isContainContent()) {
			//
			long publishNums = dynamicContentManager
					.getNodePublishContentCount(parentId);
			int pageSize = mode.getProcessContentNums();
			int totalPage = (int) (publishNums / pageSize);
			if (publishNums % pageSize > 0) {
				totalPage++;
			}
			//
			for (int i = 0; i < totalPage; i++) {
				List ciList = dynamicContentManager.getNodePublishContents(
						parentId, new Long(i * pageSize), new Long(pageSize));
				if (ciList != null) {
					for (int j = 0; j < ciList.size(); j++) {
						ContentIndex ci = (ContentIndex) ciList.get(j);
						//
						generateContentStaticFile(parentId, ci.getIndexId(),
								errors);
					}
				}
			}
		}
		// 递归处理子结点
		if (mode.isContainChildNode()) {
			List childNodes = nodeManager.getChildNodes(parentId);
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node childNode = (Node) childNodes.get(i);
					//
					generateAllNodeStaticFile(childNode.getNodeId(), mode,
							errors);
				}
			}
		}
	}

	/*
	 *
	 */
	public void generateContentStaticFile(Long nodeId, Long indexId, List errors) {
		try {
			// 获得内容索引对象
			ContentIndex ci = dynamicContentManager
					.getContentIndexById(indexId);
			if (ci == null) {
				errors.add("the indexId=" + indexId
						+ " ContentIndex is not exits.");
				return;
			}
			//
			// 获得内容所在的结点
			Node node = nodeManager.getNode(nodeId);
			if (node == null) {
				errors.add("the nodeId=" + nodeId + " Node is not exits.");

				return;
			}
			generateContentStaticFile(node, ci, errors);
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * generateNodeAllExtraIndexStaticFile(java.lang.Long, java.util.List)
	 */
	public void generateNodeAllExtraIndexStaticFile(Long nodeId, List errors) {
		List publishes = extraPublishManager.getPublishes(nodeId);
		if (publishes != null) {
			for (int i = 0; i < publishes.size(); i++) {
				generateNodeExtraIndexStaticFile(nodeId,
						(ExtraPublish) publishes.get(i), errors);
			}
		}
	}

	/*
	 * 附加发布
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * generateNodeExtraIndexStaticFile(java.lang.Long, java.lang.Long,
	 * java.util.List)
	 */
	public void generateNodeExtraIndexStaticFile(Long nodeId, Long publishId,
			List errors) {
		// TODO 附加发布缓存添加
		ExtraPublish publish = extraPublishManager.getPublishById(publishId);
		if (publish != null) {
			generateNodeExtraIndexStaticFile(nodeId, publish, errors);
		}
	}

	protected synchronized void generateNodeExtraIndexStaticFile(Long nodeId,
			ExtraPublish publish, List errors) {
		Node node = nodeManager.getNode(nodeId);
		Integer publishMode = node.getPublishMode();
		Integer selfPublishMode = publish.getPublishMode();
		if (selfPublishMode != null && !selfPublishMode.equals(new Integer(-1))) {
			publishMode = selfPublishMode;
		}
		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			// 只处理静态发布模式
			String tpl = publish.getTpl();
			//
			String psnUrl = publish.getSelfPsnUrl();
			//
			if (psnUrl == null) {
				// 如果自定义psnUrl
				psnUrl = node.getContentPsn();
			}
			//
			String url = psnManager.getPsnUrlInfo(psnUrl);
			String publishFileName = publish.getPublishFileName();
			publishFileName = publishFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());

			publishFileName = publishFileName.replaceAll("\\{PublishID\\}",
					publish.getPublishId().toString());
			url = url + "/" + publishFileName;
			//
			TemplateContext context = new TemplateContext();
			context.setTplName(tpl);
			Map model = Collections.synchronizedMap(new HashMap());
			model.put("node", node);
			model.put("nodeId", nodeId);
			model.put("url", url);
			model.put("publish", publish);
			// 得到输出文件
			File outFile = getDestNodeExtraFile(publish, node);
			model.put("outFile", outFile);
			context.setModel(model);
			//
			getTemplateEngine().renderTemplate(context, errors);
		}
	}

	/*
	 * 更新结点首页
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * generateNodeIndexStaticFile(java.lang.Long, java.util.List)
	 */
	public void generateNodeIndexStaticFile(Long nodeId, List errors) {
		Node node = nodeManager.getNode(nodeId);
		Integer publishMode = node.getPublishMode();
		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			// 如果是静态发布模式
			String indexTpl = node.getIndexTpl();
			//
			//
			String url = psnManager.getPsnUrlInfo(node.getContentUrl());
			String indexFileName = node.getIndexName();
			indexFileName = indexFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());
			url = url + "/" + indexFileName;
			// add function,if the url has change,should update the nodeUrl
			// value
			String nodeUrl = node.getNodeUrl();
			if (nodeUrl == null || !nodeUrl.equals(url)) {
				node.setNodeUrl(url);
				nodeManager.saveNode(node);
			}
			//

			TemplateContext context = new TemplateContext();
			context.setTplName(indexTpl);
			Map model = Collections.synchronizedMap(new HashMap());
			model.put("node", node);
			model.put("nodeId", nodeId);
			model.put("url", url);
			context.setModel(model);
			getTemplateEngine().renderTemplate(context, errors);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * getContent(java.lang.Long, java.lang.Long, int, java.util.List)
	 */
	public String getContent(Long nodeId, Long indexId, int page, List errors,
			boolean preview) {
		try {
			// 获得内容索引对象
			ContentIndex ci = dynamicContentManager
					.getContentIndexById(indexId);
			if (ci == null) {
				return null;
			}
			//
			// 获得内容所在的结点
			Node node = nodeManager.getNode(nodeId);
			if (node == null) {
				return null;
			} else {
				Integer publishMode = node.getPublishMode();
				Integer selfPublishMode = ci.getPublishMode();
				//
				if (selfPublishMode != null
						&& !selfPublishMode.equals(new Integer(-1))) {
					publishMode = selfPublishMode;
				}
			}
			String tpl = null;
			String url = null;
			// 获得内容自定义模板
			String selfTpl = ci.getSelfTemplate();
			if (StringUtils.hasText(selfTpl)) {
				tpl = selfTpl;
			} else {
				// get from the node info
				String conentTpl = node.getContentTpl();
				tpl = conentTpl;
			}
			// get the psn url info,it will be used to some url link
			url = ci.getUrl();
			// 纠正url
			// String okUrl = getContentUrl(ci, node);
			// if (okUrl != null && !okUrl.equals(url)) {
			// url = okUrl;
			// ci.setUrl(okUrl);
			// dynamicContentManager.saveContentIndex(ci);
			// }
			// 产生内容
			TemplateContext context = new TemplateContext();
			context.setTplName(tpl);
			Map model = Collections.synchronizedMap(new HashMap());
			model.put("node", node);
			model.put("page", page);
			model.put("nodeId", nodeId);
			if (preview) {
				url=CMSConfig.getInstance().getBaseUrl()+"admin/publish.jhtml?action=Preview&nodeId="+nodeId+"&indexId="+indexId;
				model.put("url", url);
			} else {
				model.put("url", url);
			}
			model.put("ci", ci);
			if (preview) {
				model.put("__preview__", "true");
			}
			//
			model.put("__direct_out__", "yes");
			//
			context.setModel(model);
			getTemplateEngine().renderTemplate(context, errors);
			// 获取返回内容
			String content = context.getTplContent();
			return content;
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * getExtraContent(java.lang.Long, java.lang.Long, int, java.util.List)
	 */
	public String getExtraContent(Long nodeId, Long publishId, int page,
			List errors) {
		ExtraPublish publish = extraPublishManager.getPublishById(publishId);
		if (publish != null) {
			Node node = nodeManager.getNodeById(nodeId);

			String tpl = publish.getTpl();
			//
			String psnUrl = publish.getSelfPsnUrl();
			//
			if (psnUrl == null) {
				// 如果自定义psnUrl
				psnUrl = node.getContentPsn();
			}
			//
			String url = psnManager.getPsnUrlInfo(psnUrl);
			String publishFileName = publish.getPublishFileName();
			publishFileName = publishFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());

			publishFileName = publishFileName.replaceAll("\\{PublishID\\}",
					publish.getPublishId().toString());
			url = url + "/" + publishFileName;
			//
			TemplateContext context = new TemplateContext();
			context.setTplName(tpl);
			Map model = Collections.synchronizedMap(new HashMap());
			model.put("node", node);
			model.put("nodeId", nodeId);
			model.put("url", url);
			model.put("publish", publish);
			model.put("__direct_out__", "yes");
			context.setModel(model);
			getTemplateEngine().renderTemplate(context, errors);
			String content = context.getTplContent();
			return content;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * getNodeIndex(java.lang.Long, int, java.util.List)
	 */
	public String getNodeIndex(Long nodeId, int page, List errors) {
		Node node = nodeManager.getNodeById(nodeId);
		if (node != null) {

			String indexTpl = node.getIndexTpl();
			//
			// fix bug:node.getNodeUrl() should be node.getIndexPortalUrl();
			String url = psnManager.getPsnUrlInfo(node.getIndexPortalUrl());
			String indexFileName = node.getIndexName();
			indexFileName = indexFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());
			url = url + "/" + indexFileName;
			// add function,if the url has change,should update the nodeUrl
			// value
			String nodeUrl = node.getNodeUrl();
			if (nodeUrl == null || !nodeUrl.equals(url)) {
				node.setNodeUrl(url);
				nodeManager.saveNode(node);
			}
			//
			TemplateContext context = new TemplateContext();
			context.setTplName(indexTpl);
			Map model = Collections.synchronizedMap(new HashMap());
			model.put("node", node);
			model.put("nodeId", nodeId);
			model.put("url", url);
			model.put("__direct_out__", "yes");
			context.setModel(model);
			getTemplateEngine().renderTemplate(context, errors);
			String content = context.getTplContent();
			return content;
		}
		return null;
	}

	/*
	 * 模板预览
	 * 
	 * @seeorg.openuap.cms.publish.engine.generate.CmsStaticFileGenerateEngine#
	 * previewTemplate(java.lang.Long, java.lang.Long, java.lang.String,
	 * java.lang.Long, int, int, java.util.List)
	 */
	public String previewTemplate(Long nodeId, Long indexId, String tplContent,
			Long extraPublishId, int type, int page, List errors) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	// //
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
		long timeStamp = contentIndex.getPublishDate().longValue();
		if (StringUtils.hasText(selfUrl)) {
			// get from self url
			url = selfUrl;
			return url;
		} else {
			// get from node info,psn url defintion.
			String nodeUrl = node.getContentUrl();
			url = psnManager.getPsnUrlInfo(nodeUrl);
		}
		if (StringUtils.hasText(selfFileName)) {
			url += "/" + selfFileName;
		} else {
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

	private String getSubDirName(String subDir, long timeStamp) {
		String destDir = "";
		if (StringUtils.hasText(subDir) && !subDir.equals("none")) {
			String ft = "yyyy-MM-dd";
			if (!subDir.equals("auto")) {
				subDir = subDir.replaceAll("Y", "yyyy");
				subDir = subDir.replaceAll("m", "MM");
				subDir = subDir.replaceAll("d", "dd");
				ft = subDir;
			}
			SimpleDateFormat sf = new SimpleDateFormat(ft);
			destDir = sf.format(new Date(timeStamp));
		}
		return destDir;
	}

	private String getPublishFileName(String fileName, long timeStamp,
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

	public File getDestDir(Long nodeId, Long indexId) {
		Node node = nodeManager.getNodeById(nodeId);
		ContentIndex ci = dynamicContentManager.getContentIndexById(indexId);

		CMSConfig config = CMSConfig.getInstance();
		String fullPath = "";
		String sysRootPath = config.getSysRootPath();
		String relativePath = "";
		String selfPsn = ci.getSelfPsn();
		if (StringUtils.hasText(selfPsn)) {
			relativePath = getRelativePath(selfPsn);
		} else {
			String pblPsn = node.getContentPsn();
			relativePath = getRelativePath(pblPsn);
		}
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		// sub dir
		String fileName = "";
		String selfFileName = ci.getSelfPublishFileName();
		//
		if (StringUtils.hasText(selfFileName)) {
			//
			int pos = selfFileName.lastIndexOf("/");
			String subDir = "";

			if (pos > -1) {
				subDir = selfFileName.substring(0, pos);
				fullPath += "/" + subDir;
				fileName = selfFileName.substring(pos + 1);
			} else {
				fileName = selfFileName;
			}

		} else {
			String subDir = node.getSubDir();
			String destDir = "";
			destDir = getDestDirName(subDir, ci);
			if (!destDir.equals("")) {
				fullPath += "/" + destDir;
			}
			fileName = getDestFileName(indexId, node, ci);
		}
		//
		fullPath = StringUtil.normalizePath(fullPath);
		//
		File psnDir = new File(fullPath);
		return psnDir;
	}

	/**
	 * 获得指定内容对应的文件
	 */
	public File getDestFile(Long nodeId, Long indexId) {
		try {
			Node node = nodeManager.getNode(nodeId);
			ContentIndex ci = dynamicContentManager
					.getContentIndexById(indexId);

			CMSConfig config = CMSConfig.getInstance();
			String fullPath = "";
			String sysRootPath = config.getSysRootPath();
			String relativePath = "";
			String selfPsn = ci.getSelfPsn();
			if (StringUtils.hasText(selfPsn)) {
				relativePath = getRelativePath(selfPsn);
			} else {
				String pblPsn = node.getContentPsn();
				relativePath = getRelativePath(pblPsn);
			}
			if (!relativePath.equals("")) {
				fullPath = sysRootPath + "/" + relativePath;
			} else {
				fullPath = sysRootPath;
			}
			// sub dir
			String fileName = "";
			String selfFileName = ci.getSelfPublishFileName();
			//
			if (StringUtils.hasText(selfFileName)) {
				//
				int pos = selfFileName.lastIndexOf("/");
				String subDir = "";

				if (pos > -1) {
					subDir = selfFileName.substring(0, pos);
					fullPath += "/" + subDir;
					fileName = selfFileName.substring(pos + 1);
				} else {
					fileName = selfFileName;
				}

			} else {
				String subDir = node.getSubDir();
				String destDir = "";
				destDir = getDestDirName(subDir, ci);
				if (!destDir.equals("")) {
					fullPath += "/" + destDir;
				}
				fileName = getDestFileName(indexId, node, ci);
			}
			//
			fullPath = StringUtil.normalizePath(fullPath);
			File psnDir = new File(fullPath);
			if (!psnDir.exists()) {
				psnDir.mkdirs();
			}
			File destFile = new File(psnDir, fileName);
			return destFile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private String getDestFileName(Long indexId, Node node, ContentIndex ci) {
		String fileName = "";
		ci.getPublishDate().longValue();
		long timeStamp = ci.getPublishDate().longValue();
		String stimeStamp = "" + timeStamp;
		fileName = node.getPublishFileFormat();
		//
		fileName = fileName.replaceAll("\\{TimeStamp\\}", stimeStamp);
		fileName = fileName.replaceAll("\\{ContentID\\}", ci.getContentId()
				.toString());
		fileName = fileName.replaceAll("\\{IndexID\\}", indexId.toString());
		fileName = fileName.replaceAll("\\{NodeID\\}", ci.getNodeId()
				.toString());
		return fileName;
	}

	private String getDestDirName(String subDir, ContentIndex ci) {
		String destDir = "";
		if (subDir != null && !subDir.equals("") && subDir.equals("none")) {
			String ft = "yyyy-MM-dd";
			if (!subDir.equals("auto")) {
				subDir = subDir.replaceAll("Y", "yyyy");
				subDir = subDir.replaceAll("m", "MM");
				subDir = subDir.replaceAll("d", "dd");
				ft = subDir;
			}
			SimpleDateFormat sf = new SimpleDateFormat(ft);
			long publishDate = ci.getPublishDate().longValue(); // * 1000l;
			destDir = sf.format(new Date(publishDate));
		}
		return destDir;
	}

	private String getRelativePath(String selfPsn) throws NumberFormatException {
		String relativePath = "";
		String sp = "\\{PSN:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(selfPsn);
		boolean result = m.find();
		while (result) {
			String path = m.group(2);
			String psnId = m.group(1);
			Psn psn = psnManager.getPsnFromCache(new Long(psnId));
			// String psnUrl = psn.getPsn();
			if (psn.getType() == Psn.LOCAL_PSN_TYPE) {
				// now,only process the local
				// remote will be do later.
				relativePath = psn.getLocalPath();
				relativePath += "/" + path;
			}
			result = m.find();
		} // end while result
		return relativePath;
	}

	public ExtraPublishManager getExtraPublishManager() {
		return extraPublishManager;
	}

	public void setExtraPublishManager(ExtraPublishManager extraPublishManager) {
		this.extraPublishManager = extraPublishManager;
	}

	public PsnManager getPsnManager() {
		return psnManager;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public DynamicContentManager getDynamicContentManager() {
		return dynamicContentManager;
	}

	public NodeManager getNodeManager() {
		return nodeManager;
	}

	/**
	 * 获取模板引擎插件
	 * 
	 * @return
	 */
	public TemplateEngine getTemplateEngine() {
		if (templateEngine == null) {
			templateEngine = (TemplateEngine) ObjectLocator.lookup(
					"templateEngine", "org.openuap.tpl.engine");
		}
		return templateEngine;
	}

	/**
	 * 获得目标节点附加发布文件
	 * 
	 * @param context
	 * @return
	 */
	protected File getDestNodeExtraFile(ExtraPublish publish, Node node) {
		String fullPath = "";
		CMSConfig config = CMSConfig.getInstance();
		String sysRootPath = config.getSysRootPath();
		String relativePath = "";
		String publishFileName = "";
		//
		if (publish == null || node == null) {
			return null;
		}

		String pblPsn = publish.getSelfPsn();
		if (StringUtils.hasText(pblPsn)) {
			relativePath = getRelativePath(pblPsn);
		}
		//
		if (relativePath.equals("")) {
			pblPsn = node.getContentPsn();
			relativePath = getRelativePath(pblPsn);
		}
		//
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		//

		if (publish != null) {
			publishFileName = publish.getPublishFileName();
		}
		//
		File destFile = new File(fullPath, publishFileName);
		return destFile;
	}

	public void generateContentStaticFile(Node node, ContentIndex ci,
			List errors) {
		try {

			if (ci == null) {
				return;
			}
			//
			// 获得内容所在的结点
			if (node == null) {
				return;
			} else {
				Integer publishMode = node.getPublishMode();
				Integer selfPublishMode = ci.getPublishMode();
				//
				if (selfPublishMode != null
						&& !selfPublishMode.equals(new Integer(-1))) {
					publishMode = selfPublishMode;
				}
				// 检查结点的发布模式，非静态发布结点不需要处理
				if (!publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
					return;
				}
			}
			String tpl = null;
			String url = null;
			// 获得内容自定义模板
			String selfTpl = ci.getSelfTemplate();
			if (StringUtils.hasText(selfTpl)) {
				tpl = selfTpl;
			} else {
				// get from the node info
				String conentTpl = node.getContentTpl();
				tpl = conentTpl;
			}
			// get the psn url info,it will be used to some url link
			url = ci.getUrl();
			// 纠正url
			String okUrl = getContentUrl(ci, node);
			if (okUrl != null && !okUrl.equals(url)) {
				url = okUrl;
				ci.setUrl(okUrl);
				dynamicContentManager.saveContentIndex(ci);
			}
			// 产生静态文件
			TemplateContext context = new TemplateContext();
			context.setTplName(tpl);
			Map model = Collections.synchronizedMap(new HashMap());
			model.put("node", node);
			model.put("nodeId", node.getNodeId());
			model.put("url", url);
			model.put("ci", ci);
			context.setModel(model);
			getTemplateEngine().renderTemplate(context, errors);
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
		}

	}
}
