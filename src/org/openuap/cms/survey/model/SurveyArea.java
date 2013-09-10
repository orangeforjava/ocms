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
package org.openuap.cms.survey.model;

import java.io.Serializable;

/**
 * <p>
 * 投票位对象(类似于广告位概念).
 * </p>
 * 
 * <p>
 * $Id: SurveyArea.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SurveyArea implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3615281780113091664L;

	/** 投票区id */
	private Long id;

	/** 投票区名称. */
	private String areaName;

	/** 投票区缺省组. */
	private Long defaultSurvey;

	/** 区域产生日期. */
	private Long creationDate;

	/** 区域描述. */
	private String description;

	/** 投票区状态，0停用，1. */
	private Integer status;
	private Long creationUserId;
	private String creationUserName;
	private Long lastModifiedUserId;
	private String lastModifiedUserName;
	private Long lastModifiedDate;
	/** 发布的全局id. */
	private String guid;
	// 发布相关属性
	/** 所属结点Id，0为全局. */
	private Long nodeId;
	/** 发布模式. */
	private Integer publishMode;
	/** 发布状态. */
	private Integer publishState;
	
	private Long publishDate;
	/** 管理排序属性.*/
	private Integer pos;
	/** 自定义发布点. */
	private String selfPsn;

	/** 自定义发布点PSN. */
	private String selfPsnUrl;

	/** 自定义发布文件名. */
	private String publishFileName;
	/** 模板类型,0-文件，1-数据库模板内容. */
	private Integer tplType;
	/** 模板内容. */
	private String tplContent;
	/** 模板路径. */
	private String tplPath;

	/** portal访问的URL. */
	private String portalUrl;

	public SurveyArea() {
		status = 0;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SurveyArea other = (SurveyArea) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Long getCreationUserId() {
		return creationUserId;
	}

	public void setCreationUserId(Long creationUserId) {
		this.creationUserId = creationUserId;
	}

	public String getCreationUserName() {
		return creationUserName;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getSelfPsn() {
		return selfPsn;
	}

	public void setSelfPsn(String selfPsn) {
		this.selfPsn = selfPsn;
	}

	public String getSelfPsnUrl() {
		return selfPsnUrl;
	}

	public void setSelfPsnUrl(String selfPsnUrl) {
		this.selfPsnUrl = selfPsnUrl;
	}

	public String getPublishFileName() {
		return publishFileName;
	}

	public void setPublishFileName(String publishFileName) {
		this.publishFileName = publishFileName;
	}

	public Integer getTplType() {
		return tplType;
	}

	public void setTplType(Integer tplType) {
		this.tplType = tplType;
	}

	public String getTplContent() {
		return tplContent;
	}

	public void setTplContent(String tplContent) {
		this.tplContent = tplContent;
	}

	public String getTplPath() {
		return tplPath;
	}

	public void setTplPath(String tplPath) {
		this.tplPath = tplPath;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Integer getPublishMode() {
		return publishMode;
	}

	public void setPublishMode(Integer publishMode) {
		this.publishMode = publishMode;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDefaultSurvey() {
		return defaultSurvey;
	}

	public void setDefaultSurvey(Long defaultSurvey) {
		this.defaultSurvey = defaultSurvey;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPublishState() {
		return publishState;
	}

	public void setPublishState(Integer publishState) {
		this.publishState = publishState;
	}

	public Long getLastModifiedUserId() {
		return lastModifiedUserId;
	}

	public void setLastModifiedUserId(Long lastModifiedUserId) {
		this.lastModifiedUserId = lastModifiedUserId;
	}

	public String getLastModifiedUserName() {
		return lastModifiedUserName;
	}

	public void setLastModifiedUserName(String lastModifiedUserName) {
		this.lastModifiedUserName = lastModifiedUserName;
	}

	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public Long getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Long publishDate) {
		this.publishDate = publishDate;
	}

}
