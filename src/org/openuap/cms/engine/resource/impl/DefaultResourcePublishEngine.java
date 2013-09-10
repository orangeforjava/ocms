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
package org.openuap.cms.engine.resource.impl;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.engine.resource.ResourcePublishEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.resource.manager.ResourceManager;
import org.openuap.cms.resource.model.Resource;
import org.openuap.cms.resource.model.ResourceRef;

/**
 * <p>
 * 缺省资源引擎实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultResourcePublishEngine.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultResourcePublishEngine implements ResourcePublishEngine {

	private PsnManager psnManager;

	private ResourceManager resourceManager;

	private DynamicContentManager dynamicContentManager;

	private NodeManager nodeManager;

	public DefaultResourcePublishEngine() {
	}
	
	public boolean publishContentResource(Long nodeId, Long indexId, List errors) {
		try {
			//获得此内容的资源引用
			List resourceRefList = dynamicContentManager.getResourceRefByNodeIndexId(nodeId,
					indexId);
			//
			CMSConfig config = CMSConfig.getInstance();
			String rsRootDir = config.getResourceRootPath();
			String sysRootDir = config.getSysRootPath();
			
			if (resourceRefList != null) {
				for (int i = 0; i < resourceRefList.size(); i++) {
					//
					String fullPath = rsRootDir;
					ResourceRef ref = (ResourceRef) resourceRefList.get(i);
					Long resourceId = ref.getResourceId();
					Resource resource = resourceManager.getResourceFromCache(resourceId);
					if (resource != null) {
						String path = resource.getPath();
						fullPath += File.separator + path;
						fullPath = StringUtil.normalizePath(fullPath);
						File srcFile = new File(fullPath);
						File destFile = getResourceDestFile(sysRootDir, path, nodeId, errors);
						
						try {
							FileUtil.copy(srcFile, destFile);
						} catch (Exception e) {}
					}
				}
			}
		} catch (Exception ex) {
			errors.add(ex);
			return false;
		}
		return true;
	}

	public boolean refreshAllNodeResource(Long parentId, PublishEngineMode mode, List errors) {
		boolean success = true;
		if (mode.isContainContent()) {
			//
			long publishNums = dynamicContentManager.getNodePublishContentCount(parentId);
			int pageSize = mode.getProcessContentNums();
			int totalPage = (int) (publishNums / pageSize);
			if (publishNums % pageSize > 0) {
				totalPage++;
			}
			//
			for (int i = 0; i < totalPage; i++) {
				List ciList = dynamicContentManager.getNodePublishContents(parentId, new Long(i
						* pageSize), new Long(pageSize));
				if (ciList != null) {
					for (int j = 0; j < ciList.size(); j++) {
						ContentIndex ci = (ContentIndex) ciList.get(j);
						success = publishContentResource(parentId, ci.getIndexId(), errors);
					}
				}
			}
		}
		if (mode.isContainChildNode()) {
			List childNodes = nodeManager.getNodes(parentId, new Long(0), new Integer("0"));
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node childNode = (Node) childNodes.get(i);
					//
					success = refreshAllNodeResource(childNode.getNodeId(), mode, errors);
				}
			}
		}
		return success;

	}

	public void setDynamicContentManager(DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	// ///////////////////////////////////////////////////////////////////////////
	private File getResourceDestFile(String sysRootDir, String destFileName, Long nodeId,
			List errors) {
		String fullPath = sysRootDir;
		Node node = nodeManager.getNode(nodeId);
		String rsPsn = node.getResourcePsn();
		String relativePath = getRelativePath(rsPsn);
		if (!relativePath.equals("")) {
			fullPath += "/" + relativePath;
		}
		int pos = destFileName.lastIndexOf("/");
		String destPath = "";
		String destFile = "";
		if (pos > -1) {
			destPath = destFileName.substring(0, pos);
			destFile = destFileName.substring(pos + 1);
		} else {
			destFile = destFileName;
		}
		if (!destPath.equals("")) {
			fullPath += "/" + destPath;
		}
		fullPath = StringUtil.normalizePath(fullPath);
		File destDir = new File(fullPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		File dest = new File(destDir, destFile);
		return dest;
	}

	private String getRelativePath(String spsn) throws NumberFormatException {
		String relativePath = "";
		String sp = "\\{PSN:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(spsn);
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

	public boolean publishContentResource(Node node, ContentIndex ci,
			List errors) {
		// TODO Auto-generated method stub
		return false;
	}

}
