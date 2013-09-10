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
package org.openuap.cms.publish.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.node.cache.NodeCache;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.publish.dao.ExtraPublishDao;
import org.openuap.cms.publish.manager.ExtraPublishManager;
import org.openuap.cms.publish.model.ExtraPublish;
import org.openuap.cms.util.ui.AutoRefreshMode;

/**
 * <p>
 * 附加发布管理实现.
 * </p>
 * 
 * <p>
 * $Id: ExtraPublishManagerDBImpl.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ExtraPublishManagerDBImpl implements ExtraPublishManager {
	
	private ExtraPublishDao extraPublishDao;
	
	private PsnManager psnManager;
	
	

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public ExtraPublishManagerDBImpl() {
	}

	public void setExtraPublishDao(ExtraPublishDao publishDao) {
		extraPublishDao = publishDao;
	}

	public Long addPublish(ExtraPublish publish) {
		return extraPublishDao.addPublish(publish);
	}

	public void savePublish(ExtraPublish publish) {
		extraPublishDao.savePublish(publish);
	}

	public void deletePublish(Long publishId) {
		extraPublishDao.deletePublish(publishId);
	}

	public ExtraPublish getPublishById(Long publishId) {
		return extraPublishDao.getPublishById(publishId);
	}

	public List getPublishes(Long nodeId) {
		return extraPublishDao.getPublishes(nodeId);
	}

	public ExtraPublish getPublishByGuid(String guid) {
		return extraPublishDao.getPublishByGuid(guid);
	}

	public String getExtraPublishPath(String id) {
		if (id != null) {
			Long publishId = new Long(id);
			ExtraPublish publish = this.getPublishById(publishId);
			if (publish != null) {
				String path = null;
				String psnUrl = publish.getSelfPsnUrl();
				String fileName = publish.getPublishFileName();
				String pattern = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)*(:[a-zA-Z0-9]*)?([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
				String psnUrlInfo = getPsnManager().getPsnUrlInfo(psnUrl);
				Pattern p = Pattern.compile(pattern);
				// System.out.println("psnUrlInfo=" + psnUrlInfo);
				Matcher m = p.matcher(psnUrlInfo);
				boolean found = m.find();
				if (found) {
					path = m.group(4);
					// System.out.println("path="+path);
					//
				}
				if (path == null) {
					path = "";
				}
				path = "/" + path + "/" + fileName;
				path = path.replaceAll("\\/\\/", "/");
				//
				return path;
			}
		}
		return "";
	}
	/**
	 * TODO 这种方式会导致无法把两个同样的CMS部署在一起
	 * @return
	 */
	public PsnManager getPsnManager(){
//		PsnManager psnManager = (PsnManager) ObjectLocator.lookup(
//				"psnManager", CmsPlugin.PLUGIN_ID);
		return psnManager;
	}

	public List<ExtraPublish> getAutoRefreshPublish(Long nodeId) {
		
		return getNodeAutoReRefreshPublish(nodeId);
	}
	/**
	 * TODO 附加发布需要加入缓存机制
	 * @param nodeId
	 * @return
	 */
	protected List<ExtraPublish> getNodeAutoReRefreshPublish(Long nodeId) {
		//
		List<ExtraPublish> rs=new ArrayList<ExtraPublish>();
		//
		List<ExtraPublish> publishes=this.extraPublishDao.getNodeAutoRefreshPublish(nodeId,AutoRefreshMode.SELF_REFRESH_MODE.getMode());
		for(ExtraPublish p:publishes){
			if(!rs.contains(p)){
				rs.add(p);
			}
		}
		Node parentNode=NodeCache.getParentNode(nodeId);
		
		while(parentNode!=null){
			Long pid=parentNode.getNodeId();
			List<ExtraPublish> publishes2=this.extraPublishDao.getNodeAutoRefreshPublish(pid,AutoRefreshMode.PARENT_REFRESH_MODE.getMode());
			for(ExtraPublish p:publishes2){
				if(!rs.contains(p)){
					rs.add(p);
				}
			}
			parentNode=NodeCache.getParentNode(pid);
		}
		//
		List<ExtraPublish> publishes3=this.extraPublishDao.getGlobalAutoRefreshPublish();
		for(ExtraPublish p:publishes3){
			if(!rs.contains(p)){
				rs.add(p);
			}
		}
		return rs;		
	}

	
	public List getPublishes(QueryInfo qi, PageBuilder pb) {
		
		return extraPublishDao.getPublishes(qi, pb);
	}
	
}
