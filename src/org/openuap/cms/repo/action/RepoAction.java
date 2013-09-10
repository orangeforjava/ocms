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
package org.openuap.cms.repo.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 存储管理控制器
 * </p>
 * 
 * <p>
 * 目前主要实现了从1.0版本的数据到2.0版本的数据迁移功能
 * <p>
 * 
 * <p>
 * $Id: RepoAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 2.0
 * @since 2.0
 * 
 */
public class RepoAction extends AdminAction {

	private DynamicContentManager dynamicContentManager;
	private NodeManager nodeManager;
	private ContentFieldManager contentFieldManager;
	
	private String defaultScreensPath;
	private String repoSettingViewName;

	/** 操作结果视图定义.*/
	private String rsViewName;

	public RepoAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/repo/";
		repoSettingViewName = defaultScreensPath + "node_repo_dialog.html";
		rsViewName = defaultScreensPath + "repo_operation_result.html";
	}

	/**
	 * 迁移数据,适合于原来为ContentIndex没有添加ContentTitle等属性的应用
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doMigration(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rsViewName);
		// 是否包含子结点
		boolean includeSub = helper.getBoolean("includeSub", false);
		// 每次处理内容数
		int contentNum = helper.getInt("contentNum", 100);

		String nodeId = helper.getString("nodeId");

		model.put("op", "migration");
		model.put("nodeId", nodeId);
		if (nodeId != null) {
			Long nid = new Long(nodeId);
			//
			migrateRepoContent(nid, includeSub, contentNum);
		}
		model.put("rs", "success");
		return mv;
	}
	/**
	 * 手工校正结点统计信息
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doVerifyNodeStat(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rsViewName);
		try {
			dynamicContentManager.verifyAllNodeContentStat();
			model.put("rs", "success");
			
		} catch (Exception e) {
			e.printStackTrace();
			model.put("rs", "failed");
		}
		return mv;
	}
	/**
	 * 具体执行数据迁移操作
	 * 
	 * @param nid
	 * @param includeSub
	 * @param contentNum
	 */
	protected void migrateRepoContent(Long nid, boolean includeSub,
			int contentNum) {
		//

		Node node = nodeManager.getNode(nid);
		Long tableId = node.getTableId();
		//
		ContentField titleField = contentFieldManager.getTitleField(tableId);
		ContentField photoField = contentFieldManager
				.getPhotoFieldFromCache(tableId);
		Long totalCount = dynamicContentManager.getAllContentCount(nid);
		int pageSize = contentNum;
		int totalPage = (int) (totalCount / pageSize);
		if (totalCount % pageSize > 0) {
			totalPage++;
		}
		//
		for (int i = 0; i < totalPage; i++) {
			// 获得通过关联查询的内容数据
			List ciList = dynamicContentManager.getAllContentList(nid, tableId,
					"", "", null, new Long(i * pageSize), new Long(pageSize));
			//
			int size = ciList.size();
			for (int j = 0; j < size; j++) {
				Object rs = ciList.get(j);
				if (rs instanceof Object[]) {
					Object[] contents = (Object[]) rs;
					Map ci = (Map) contents[0];
					Map c = (Map) contents[1];
					Map ci2 = (Map) contents[2];
					Map co = (Map) contents[3];
					Long id = (Long) ci.get("indexId");
					ContentIndex contentIndex = dynamicContentManager
							.getContentIndexById(id);
					Long hitsTotal = (Long) co.get("hitsTotal");
					Long hitsToday = (Long) co.get("hitsToday");
					Long hitsWeek = (Long) co.get("hitsWeek");
					Long hitsMonth = (Long) co.get("hitsMonth");
					Long commentNum = (Long) co.get("commentNum");
					Long hitsDate = (Long) co.get("hitsDate");
					contentIndex.setHitsToday(hitsToday);
					contentIndex.setHitsTotal(hitsTotal);
					contentIndex.setHitsWeek(hitsWeek);
					contentIndex.setHitsMonth(hitsMonth);
					contentIndex.setHitsDate(hitsDate);
					contentIndex.setCommentNum(commentNum);
					if (titleField != null) {
						String titleFieldName = titleField.getFieldName();
						String contentTitle = (String) c.get(titleFieldName);
						contentIndex.setContentTitle(contentTitle);
					}
					if (photoField != null) {
						String photoFieldName = photoField.getFieldName();
						String contentPhoto = (String) c.get(photoFieldName);
						contentIndex.setContentPhoto(contentPhoto);
					}
					contentIndex.setCreationUserName((String) c
							.get("creationUserName"));
					dynamicContentManager.saveContentIndex(contentIndex);
				}
			}
		}
		if (includeSub) {
			// 处理子结点
			List childNodes = nodeManager.getChildNodes(nid);
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node childNode = (Node) childNodes.get(i);
					Long cnid = childNode.getNodeId();
					//
					try {
						migrateRepoContent(cnid, includeSub, contentNum);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 内容发布设置对话框
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doMigrationSettingDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		ModelAndView mv = new ModelAndView(repoSettingViewName, model);
		return mv;
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

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setRepoSettingViewName(String repoSettingViewName) {
		this.repoSettingViewName = repoSettingViewName;
	}

	public void setRsViewName(String rsViewName) {
		this.rsViewName = rsViewName;
	}
}
