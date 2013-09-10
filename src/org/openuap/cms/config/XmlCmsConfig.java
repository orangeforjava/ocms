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
package org.openuap.cms.config;

/**
 * 基于Xml的CMS配置
 * <p>
 * $Id: XmlCmsConfig.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * 
 */
public class XmlCmsConfig extends AbstractXmlConfiguration implements
		ICmsConfig {

	private String pwdEncryptArithmetic = "md5";

	private String templatePath;

	private String tmpTemplatePath;

	private String systemTemplatePath;

	/** the user(to cms author)'s template path. */
	private String userTemplatePath;

	private String templateSkinPath;

	/** 动态Hibernate Mapping 定义路径. */
	private String dynamicMappingPath;

	/** 动态模型索引路径. */
	private String dynamicRSEMPath;

	/** property for cms upload file max size(bytes) */
	private String uploadFileMaxSize;

	/** proerty for cms upload file temp path */
	/** */
	private String encoding;

	/** */
	private String uploadFileImageType;

	private String uploadFileFlashType;

	private String uploadFileAttachType;

	private String uploadFileMediaType;
	/** */
	private String resourceRootPath;

	/** */
	private String sysRootPath;

	/** the cms application title. */
	private String title;

	private String baseUrl;

	/** */
	private String initDataPath;

	public String getPwdEncryptArithmetic() {
		return pwdEncryptArithmetic;
	}

	public void setPwdEncryptArithmetic(String pwdEncryptArithmetic) {
		this.pwdEncryptArithmetic = pwdEncryptArithmetic;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getTmpTemplatePath() {
		return tmpTemplatePath;
	}

	public void setTmpTemplatePath(String tmpTemplatePath) {
		this.tmpTemplatePath = tmpTemplatePath;
	}

	public String getSystemTemplatePath() {
		return systemTemplatePath;
	}

	public void setSystemTemplatePath(String systemTemplatePath) {
		this.systemTemplatePath = systemTemplatePath;
	}

	public String getUserTemplatePath() {
		return userTemplatePath;
	}

	public void setUserTemplatePath(String userTemplatePath) {
		this.userTemplatePath = userTemplatePath;
	}

	public String getTemplateSkinPath() {
		return templateSkinPath;
	}

	public void setTemplateSkinPath(String templateSkinPath) {
		this.templateSkinPath = templateSkinPath;
	}

	public String getDynamicMappingPath() {
		return dynamicMappingPath;
	}

	public void setDynamicMappingPath(String dynamicMappingPath) {
		this.dynamicMappingPath = dynamicMappingPath;
	}

	public String getDynamicRSEMPath() {
		return dynamicRSEMPath;
	}

	public void setDynamicRSEMPath(String dynamicRSEMPath) {
		this.dynamicRSEMPath = dynamicRSEMPath;
	}

	public String getUploadFileMaxSize() {
		return uploadFileMaxSize;
	}

	public void setUploadFileMaxSize(String uploadFileMaxSize) {
		this.uploadFileMaxSize = uploadFileMaxSize;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getUploadFileImageType() {
		return uploadFileImageType;
	}

	public void setUploadFileImageType(String uploadFileImageType) {
		this.uploadFileImageType = uploadFileImageType;
	}

	public String getUploadFileFlashType() {
		return uploadFileFlashType;
	}

	public void setUploadFileFlashType(String uploadFileFlashType) {
		this.uploadFileFlashType = uploadFileFlashType;
	}

	public String getUploadFileAttachType() {
		return uploadFileAttachType;
	}

	public void setUploadFileAttachType(String uploadFileAttachType) {
		this.uploadFileAttachType = uploadFileAttachType;
	}

	public String getUploadFileMediaType() {
		return uploadFileMediaType;
	}

	public void setUploadFileMediaType(String uploadFileMediaType) {
		this.uploadFileMediaType = uploadFileMediaType;
	}

	public String getResourceRootPath() {
		return resourceRootPath;
	}

	public void setResourceRootPath(String resourceRootPath) {
		this.resourceRootPath = resourceRootPath;
	}

	public String getSysRootPath() {
		return sysRootPath;
	}

	public void setSysRootPath(String sysRootPath) {
		this.sysRootPath = sysRootPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getInitDataPath() {
		return initDataPath;
	}

	public void setInitDataPath(String initDataPath) {
		this.initDataPath = initDataPath;
	}

	public void reloadConfig() {
		if (configuration != null) {
			super.reload();
			this.pwdEncryptArithmetic = this.getString("cms.pwd.encryption",
					"md5");
			this.userTemplatePath = this.getString("cms.user.tpl.path", "/");
			dynamicMappingPath = this
					.getString("base.hibernate.dynamic-mapping-dir");
			dynamicRSEMPath = this.getString("base.index.meta-dir");
			long maxSize = 1024 * 1024 * 10;
			uploadFileMaxSize = this.getString("cms.upload-file.max-size",
					String.valueOf(maxSize));
			uploadFileImageType = this.getString("cms.upload-file.image-type",
					"jpg|jpeg|gif|png|bmp");
			uploadFileFlashType = this.getString("cms.upload-file.flash-type",
					"swf");
			uploadFileAttachType = this
					.getString(
							"cms.upload-file.attach-type",
							"zip|rar|doc|xls|txt|pdf|ppt|wmv|mpeg|3gp|vox|mp4|mp3|wav|wma|midi|avi|mpg|dat|asf");

			resourceRootPath = this.getString("cms.resource.root-dir");
			templatePath = this.getString("cms.tpl.path");
			systemTemplatePath = this.getString("cms.sys.tpl.path");
			tmpTemplatePath = this.getString("cms.tmp.tpl.path");
			templateSkinPath = this.getString("cms.tpl.skin.path");
			title = this.getString("cms.title", "OpenUAP CMS");
			baseUrl = this.getString("cms.url.homepage");
			initDataPath = this.getString("cms.init.data.path");
		}
	}
}
