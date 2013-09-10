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
package org.openuap.cms.tpltag.model;

import java.util.Map;

/**
 * <p>
 * 模板标记实体.
 * </p>
 * 
 * <p>
 * $Id: TemplateTag.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateTag implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1477001640350579981L;

	public static final Integer TPL_TYPE_FILE = 0;
	public static final Integer TPL_TYPE_DB = 1;

	private Long id;
	private Long userId;
	private String userName;
	private Long creationDate;
	private Long editorId;
	private String editorName;
	private Long editedDate;
	/** tag 对应的结点id. */
	private Long nodeId;
	/** tag 名称，可以是中文. */
	private String name;
	/** tag 描述. */
	private String description;
	/** tag 对应的模型id. */
	private Long modelId;

	/** tag 对应的模板类型. */
	private Integer tplType;
	/** tag 对应的模板路径. */
	private String templatePath;
	/** tag 对应的模板内容. */
	private String templateContent;

	/** tag 状态. */
	private Integer status;
	/** 内容区块缓存类型，-1,无缓存,0-文件形式缓存,1-内存形式缓存. */
	private Integer cacheType;
	/** 内容区块输出形式,0，嵌入方式,1,ssi静态包含形式. */
	private Integer outType;
	/** 辅助tag管理解析的帮助类. */
	private String tagHelperClassName;
	/** 以xml存储的可变化配置参数. */
	private String paramsXML;
	/** 模板可变化参数的Map形式. */
	private Map params;
	/** 设置模板路径. */
	private String settingTplPath;

	private Integer pos;

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public Long getEditorId() {
		return editorId;
	}

	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public Long getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(Long editedDate) {
		this.editedDate = editedDate;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public Integer getTplType() {
		return tplType;
	}

	public void setTplType(Integer tplType) {
		this.tplType = tplType;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCacheType() {
		return cacheType;
	}

	public void setCacheType(Integer cacheType) {
		this.cacheType = cacheType;
	}

	public Integer getOutType() {
		return outType;
	}

	public void setOutType(Integer outType) {
		this.outType = outType;
	}

	public String getTagHelperClassName() {
		return tagHelperClassName;
	}

	public void setTagHelperClassName(String tagHelperClassName) {
		this.tagHelperClassName = tagHelperClassName;
	}

	public String getParamsXML() {
		return paramsXML;
	}

	public void setParamsXML(String paramsXML) {
		this.paramsXML = paramsXML;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public String getSettingTplPath() {
		return settingTplPath;
	}

	public void setSettingTplPath(String settingTplPath) {
		this.settingTplPath = settingTplPath;
	}

}
