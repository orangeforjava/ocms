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
package org.openuap.cms.publish.model;

import java.io.Serializable;

import org.openuap.base.dao.hibernate.BaseObject;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 附加发布对象.
 * </p>
 * 
 * <p>
 * $Id: ExtraPublish.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class ExtraPublish extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7168512485863213829L;

	private int hashValue = 0;

	/** 发布id. */
	private Long publishId;

	/** 所属结点id. */
	private Long nodeId;

	/** 附加发布名. */
	private String publishName;

	/** 自定义发布点. */
	private String selfPsn;

	/** 自定义发布点url. */
	private String selfPsnUrl;

	/** 发布文件名. */
	private String publishFileName;

	/** 模板路径. */
	private String tpl;

	/** 描述. */
	private String intro;

	/** T创建人id. */
	private Long creationUserId;

	/** 创建人. */
	private String creationUserName;

	/** 最后修改用户id. */
	private Long lastModifiedUserId;

	/** 最后修改用户. */
	private String lastModifiedUserName;

	/** 创建日期. */
	private Long creationDate;

	/** 最后修改日期. */
	private Long modifiedDate;

	/** 附加发布的全局id. */
	private String publishGuid;
	/** 发布模式. */
	private Integer publishMode;
	/** portal url. */
	private String extraPortalUrl;
	/** 自动刷新模式，0-不刷新，1-同级刷新，2-父级刷新，3-全局刷新. */
	private Integer autoRefreshMode;

	/**
	 * Simple constructor of AbstractCmsExtraPublish instances.
	 */
	public ExtraPublish() {
	}

	/**
	 * Constructor of AbstractCmsExtraPublish instances given a simple primary
	 * key.
	 * 
	 * @param publishid
	 *            Integer
	 */
	public ExtraPublish(Long publishid) {
		this.setPublishId(publishid);
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getPublishId() {

		return publishId;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param publishId
	 *            Integer
	 */
	public void setPublishId(Long publishId) {
		this.hashValue = 0;

		this.publishId = publishId;
	}

	/**
	 * Return the value of the NodeID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getNodeId() {

		return nodeId;
	}

	/**
	 * Set the value of the NodeID column.
	 * 
	 * @param nodeId
	 *            Integer
	 */
	public void setNodeId(Long nodeId) {

		this.nodeId = nodeId;
	}

	/**
	 * Return the value of the PublishName column.
	 * 
	 * @return java.lang.String
	 */
	public String getPublishName() {

		return publishName;
	}

	/**
	 * Set the value of the PublishName column.
	 * 
	 * @param publishName
	 *            String
	 */
	public void setPublishName(String publishName) {

		this.publishName = publishName;
	}

	/**
	 * Return the value of the SelfPSN column.
	 * 
	 * @return java.lang.String
	 */
	public String getSelfPsn() {

		return selfPsn;
	}

	/**
	 * Set the value of the SelfPSN column.
	 * 
	 * @param selfPsn
	 *            String
	 */
	public void setSelfPsn(String selfPsn) {

		this.selfPsn = selfPsn;
	}

	/**
	 * Return the value of the SelfPSNURL column.
	 * 
	 * @return java.lang.String
	 */
	public String getSelfPsnUrl() {

		return selfPsnUrl;
	}

	/**
	 * Set the value of the SelfPSNURL column.
	 * 
	 * @param selfPsnUrl
	 *            String
	 */
	public void setSelfPsnUrl(String selfPsnUrl) {

		this.selfPsnUrl = selfPsnUrl;
	}

	/**
	 * Return the value of the PublishFileName column.
	 * 
	 * @return java.lang.String
	 */
	public String getPublishFileName() {

		return publishFileName;
	}

	/**
	 * Set the value of the PublishFileName column.
	 * 
	 * @param publishFileName
	 *            String
	 */
	public void setPublishFileName(String publishFileName) {

		this.publishFileName = publishFileName;
	}

	/**
	 * Return the value of the Tpl column.
	 * 
	 * @return java.lang.String
	 */
	public String getTpl() {
		return this.tpl;
	}

	/**
	 * Set the value of the Tpl column.
	 * 
	 * @param tpl
	 *            String
	 */
	public void setTpl(String tpl) {
		this.tpl = tpl;
	}

	/**
	 * Return the value of the Intro column.
	 * 
	 * @return java.lang.String
	 */
	public String getIntro() {
		return this.intro;
	}

	/**
	 * Set the value of the Intro column.
	 * 
	 * @param intro
	 *            String
	 */
	public void setIntro(String intro) {
		this.intro = intro;
	}

	/**
	 * Return the value of the CreationUserID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getCreationUserId() {

		return creationUserId;
	}

	/**
	 * Set the value of the CreationUserID column.
	 * 
	 * @param creationUserId
	 *            Integer
	 */
	public void setCreationUserId(Long creationUserId) {

		this.creationUserId = creationUserId;
	}

	/**
	 * Return the value of the LastModifiedUserID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getLastModifiedUserId() {

		return lastModifiedUserId;
	}

	/**
	 * Set the value of the LastModifiedUserID column.
	 * 
	 * @param lastModifiedUserId
	 *            Integer
	 */
	public void setLastModifiedUserId(Long lastModifiedUserId) {

		this.lastModifiedUserId = lastModifiedUserId;
	}

	/**
	 * Return the value of the CreationDate column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getCreationDate() {

		return creationDate;
	}

	/**
	 * Set the value of the CreationDate column.
	 * 
	 * @param creationDate
	 *            Integer
	 */
	public void setCreationDate(Long creationDate) {

		this.creationDate = creationDate;
	}

	/**
	 * Return the value of the ModifiedDate column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getModifiedDate() {

		return modifiedDate;
	}

	public String getLastModifiedUserName() {
		return lastModifiedUserName;
	}

	public String getCreationUserName() {
		return creationUserName;
	}

	public String getPublishGuid() {
		return publishGuid;
	}

	public Integer getPublishMode() {
		return publishMode;
	}

	public String getExtraPortalUrl() {
		return extraPortalUrl;
	}

	/**
	 * Set the value of the ModifiedDate column.
	 * 
	 * @param modifiedDate
	 *            Integer
	 */
	public void setModifiedDate(Long modifiedDate) {

		this.modifiedDate = modifiedDate;
	}

	public void setLastModifiedUserName(String lastModifiedUserName) {
		this.lastModifiedUserName = lastModifiedUserName;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public void setPublishGuid(String publishGuid) {
		this.publishGuid = publishGuid;
	}

	public void setPublishMode(Integer publishMode) {
		this.publishMode = publishMode;
	}

	public void setExtraPortalUrl(String extraPortalUrl) {
		this.extraPortalUrl = extraPortalUrl;
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof ExtraPublish)) {
			return false;
		}
		ExtraPublish that = (ExtraPublish) rhs;
		if (this.getPublishId() == null || that.getPublishId() == null) {
			return false;
		}
		return (this.getPublishId().equals(that.getPublishId()));
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int publishidValue = this.getPublishId() == null ? 0 : this
					.getPublishId().hashCode();
			result = result * 37 + publishidValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public Integer getAutoRefreshMode() {
		return autoRefreshMode;
	}

	public void setAutoRefreshMode(Integer autoRefreshMode) {
		this.autoRefreshMode = autoRefreshMode;
	}
	/**
	 * 获得附加发布的url
	 * @return
	 */
	public String getUrl() {
		Node node = getNodeManager().getNode(this.getNodeId());
		Integer publishMode = node.getPublishMode();
		Integer selfPublishMode = this.getPublishMode();
		if (selfPublishMode != null && !selfPublishMode.equals(new Integer(-1))) {
			publishMode = selfPublishMode;
		}
		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			String psnUrl = this.getSelfPsnUrl();
			String url = getPsnManager().getPsnUrlInfo(psnUrl);
			String fileName = this.getPublishFileName();
			url = url + "/" + fileName;
			return url;
		} else {
			// 动态发布
			String url = node.getExtraPortalUrl();
			String selfUrl = this.getExtraPortalUrl();
			if (selfUrl != null && !selfUrl.equals("")) {
				url = selfUrl;
			}
			url = url.replaceAll("\\{PublishID\\}", this.publishId.toString());
			url = url.replaceAll("\\{NodeID\\}", node.getNodeId().toString());
			//
			String baseUrl = CMSConfig.getInstance().getBaseUrl();
			if (baseUrl.endsWith("/")) {
				baseUrl.substring(0, baseUrl.length() - 1);
			}
			//
			if (!url.startsWith("http")) {
				url = baseUrl + "/" + url;
			}
			return url;
		}
	}

	public PsnManager getPsnManager() {
		PsnManager psnManager = (PsnManager) ObjectLocator.lookup("psnManager",
				CmsPlugin.PLUGIN_ID);
		return psnManager;
	}

	public NodeManager getNodeManager() {
		NodeManager nodeManager = (NodeManager) ObjectLocator.lookup(
				"nodeManager", CmsPlugin.PLUGIN_ID);
		return nodeManager;
	}
}
