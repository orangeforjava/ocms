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

import java.util.Locale;

import org.openuap.base.util.StringUtil;
import org.openuap.cms.Version;
import org.openuap.runtime.config.ApplicationConfigurationException;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.openuap.runtime.setup.BootstrapManager;
import org.openuap.runtime.setup.BootstrapUtils;
import org.openuap.util.Locales;

/**
 * <p>
 * CMS配置，已经停用，请使用ConfigFactory.getCmsConfig();
 * </p>
 * 
 * <p>
 * $Id: CMSConfig.java 4034 2011-03-22 17:58:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 * @deprecated
 */
public class CMSConfig {

	private static CMSConfig _instance = null;

	private BaseApplicationConfiguration config;

	private BootstrapManager bootstrapManager;

	private String propPrefix;

	//
	private String pwdEncryptArithmetic = "md5";

	private String templatePath;

	private String tmpTemplatePath;

	private String systemTemplatePath;

	/** the user(to cms author)'s template path. */
	private String userTemplatePath;

	private String templateSkinPath;

	/** property for hibernate dynamic mapping file path. */
	private String dynamicMappingPath;

	/** 动态模型索引路径. */
	private String dynamicRSEMPath;

	/** property for editor style file. */
	private String editorStyleFileName;

	/** property for editor button file. */
	private String editorButtonFileName;

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

	private String memberPresenceUrl;

	private String memberPresenceImagesUrl;

	private String imServerName;

	//
	private boolean enableChangeLanguage;
	/** 是否启用关键词分析.*/
	private boolean enableKeywordsParse;

	/**
	 * 
	 * @param bootstrapManager
	 *            
	 */
	protected CMSConfig(BootstrapManager bootstrapManager) {
		this.bootstrapManager = bootstrapManager;
		this.config = this.bootstrapManager.getApplicationConfig();
		propPrefix = "";
		loadConfig();
	}

	public static CMSConfig getInstance() {
		if (_instance == null) {
			synchronized (CMSConfig.class) {
				BootstrapManager bsm = BootstrapUtils.getBootstrapManager();
				_instance = new CMSConfig(bsm);
			}

		}
		return _instance;
	}

	protected void loadConfig() {
		this.pwdEncryptArithmetic = config.getString("cms.pwd.encryption",
				"md5");
		this.userTemplatePath = config.getString("cms.user.tpl.path", "/");
		dynamicMappingPath = config
				.getString("base.hibernate.dynamic-mapping-dir");
		dynamicRSEMPath = config.getString("base.index.meta-dir");
		editorStyleFileName = config.getString("cms.editor.style-file-name");
		editorButtonFileName = config.getString("cms.editor.button-file-name");
		long maxSize = 1024 * 1024 * 100;
		uploadFileMaxSize = config.getString("cms.upload-file.max-size", String
				.valueOf(maxSize));
		encoding = config.getString("sys.charset.encoding", "UTF-8");
		uploadFileImageType = config.getString("cms.upload-file.image-type",
				"jpg|jpeg|gif|png|bmp");
		uploadFileFlashType = config.getString("cms.upload-file.flash-type",
				"swf");
		uploadFileAttachType = config.getString("cms.upload-file.attach-type",
				"zip|rar|doc|xls|txt|pdf|ppt|wmv|mpeg|3gp|vox|mp4|mp3|wav|wma|midi|avi|mpg|dat|asf|flv");
		//uploadFileMediaType=config.getString("cms.upload-file.media-type","wmv|mpeg|3gp|vox|mp4|mp3|wav|wma|midi|avi|mpg|dat|asf");
		
		resourceRootPath = config.getString("cms.resource.root-dir");
		sysRootPath = config.getString("sys.path.root");
		templatePath = config.getString("cms.tpl.path");
		systemTemplatePath = config.getString("cms.sys.tpl.path");
		tmpTemplatePath = config.getString("cms.tmp.tpl.path");
		templateSkinPath = config.getString("cms.tpl.skin.path");
		title = config.getString("cms.title", "OpenUAP CMS");
		baseUrl = config.getString("sys.url.homepage");
		initDataPath = config.getString("cms.init.data.path");
		memberPresenceUrl = config.getString("im.member.presence.url", "");
		memberPresenceImagesUrl = config.getString(
				"im.member.presence.images.url", "");
		imServerName = config.getString("im.server.name", "");
		//默认启动关键词分析
		enableKeywordsParse=config.getBoolean("cms.enable-keywords-parse",true);
		//
		loadLocales();
	}

	private void loadLocales() {
		try {
			// CMS可用的语言
			String langs[] = StringUtil.str2arr(config
					.getString("cms.locale.language"));
			// CMS使用的缺省语言
			String defaultLang = config.getString(
					"cms.locale.language.[@default]", "");
			if (langs.length < 1) {
				Locales.setAvaibleLocale(null, null);
				enableChangeLanguage = false;
				return;
			}
			Locale locales[] = new Locale[langs.length];
			Locale defaultLocale = null;
			for (int i = 0; langs != null && i < langs.length; i++) {
				String lang = null;
				String country = null;
				int pos = langs[i].indexOf("_");
				if (pos > -1) {
					lang = langs[i].substring(0, pos);
					country = langs[i].substring(pos + 1);
					locales[i] = new Locale(lang, country);
				} else {
					locales[i] = new Locale(langs[i], "", "");
				}
				if (defaultLang.equals(langs[i])) {
					defaultLocale = locales[i];
				}
			}

			Locales.setAvaibleLocale(locales, defaultLocale);
			if (config.getBoolean("cms.user_change_language[@enable]")) {
				enableChangeLanguage = true;
			} else {
				enableChangeLanguage = false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Locale getDefaultLocale() {
		return Locales.getDefaultLocale();
	}

	public void setPropPrefix(String propPrefix) {
		this.propPrefix = propPrefix;
	}

	public void setPwdEncryptArithmetic(String pwdEncryptArithmetic) {
		this.pwdEncryptArithmetic = pwdEncryptArithmetic;
	}

	public void setUserTemplatePath(String userTemplatePath) {
		this.userTemplatePath = userTemplatePath;
	}

	public void setDynamicMappingPath(String dynamicMappingPath) {
		this.dynamicMappingPath = dynamicMappingPath;
	}

	public void setEditorButtonFileName(String editorButtonFileName) {
		this.editorButtonFileName = editorButtonFileName;
	}

	public void setEditorStyleFileName(String editorStyleFileName) {
		this.editorStyleFileName = editorStyleFileName;
	}

	public void setUploadFileMaxSize(String uploadFileMaxSize) {
		this.uploadFileMaxSize = uploadFileMaxSize;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setUploadFileAttachType(String uploadFileAttachType) {
		this.uploadFileAttachType = uploadFileAttachType;
	}

	public void setUploadFileFlashType(String uploadFileFlashType) {
		this.uploadFileFlashType = uploadFileFlashType;
	}

	public void setUploadFileImageType(String uploadFileImageType) {
		this.uploadFileImageType = uploadFileImageType;
	}

	public void setResourceRootPath(String resourceRootPath) {
		this.resourceRootPath = resourceRootPath;
	}

	public void setSysRootPath(String sysRootPath) {
		this.sysRootPath = sysRootPath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public void setSystemTemplatePath(String systemTemplatePath) {
		this.systemTemplatePath = systemTemplatePath;
	}

	public void setTmpTemplatePath(String tmpTemplatePath) {
		this.tmpTemplatePath = tmpTemplatePath;
	}

	public void setTemplateSkinPath(String templateSkinPath) {
		this.templateSkinPath = templateSkinPath;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setInitDataPath(String initDataPath) {
		this.initDataPath = initDataPath;
	}

	public void setMemberPresenceImagesUrl(String memberPresenceImagesUrl) {
		this.memberPresenceImagesUrl = memberPresenceImagesUrl;
	}

	public void setMemberPresenceUrl(String memberPresenceUrl) {
		this.memberPresenceUrl = memberPresenceUrl;
	}

	public void setImServerName(String imServerName) {
		this.imServerName = imServerName;
	}

	public String getPropPrefix() {
		return propPrefix;
	}

	public String getPwdEncryptArithmetic() {
		return pwdEncryptArithmetic;
	}

	public String getUserTemplatePath() {
		return userTemplatePath;
	}

	public String getDynamicMappingPath() {
		return dynamicMappingPath;
	}

	public String getEditorButtonFileName() {
		return editorButtonFileName;
	}

	public String getEditorStyleFileName() {
		return editorStyleFileName;
	}

	public String getUploadFileMaxSize() {
		return uploadFileMaxSize;
	}

	public String getEncoding() {
		return encoding;
	}

	public String getUploadFileAttachType() {
		return uploadFileAttachType;
	}

	public String getUploadFileFlashType() {
		return uploadFileFlashType;
	}

	public String getUploadFileImageType() {
		return uploadFileImageType;
	}

	public String getResourceRootPath() {
		return resourceRootPath;
	}

	public String getSysRootPath() {
		return sysRootPath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public String getSystemTemplatePath() {
		return systemTemplatePath;
	}

	public String getTmpTemplatePath() {
		return tmpTemplatePath;
	}

	public String getTemplateSkinPath() {
		return templateSkinPath;
	}

	public String getTitle() {
		return title;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getInitDataPath() {
		return initDataPath;
	}

	public String getMemberPresenceImagesUrl() {
		return memberPresenceImagesUrl;
	}

	public String getMemberPresenceUrl() {
		return memberPresenceUrl;
	}

	public String getImServerName() {
		return imServerName;
	}

	public Object getProperty(String key) {
		return config.getProperty(propPrefix + key);
	}

	public String getStringProperty(String key) {
		return config.getString(propPrefix + key);
	}

	public String getStringProperty(String key, String def) {
		return config.getString(propPrefix + key, def);
	}

	public int getIntegerProperty(String key) {
		return config.getInt(propPrefix + key);
	}

	public int getIntegerProperty(String key, int def) {
		return config.getInt(propPrefix + key, def);
	}

	public boolean getBooleanProperty(String key) {
		return config.getBoolean(key);
	}

	public boolean getBooleanProperty(String key, boolean def) {
		return config.getBoolean(key, def);
	}

	public void setProperty(String key, Object value) {
		this.config.setProperty(key, value);
		try {
			this.config.save();
		} catch (ApplicationConfigurationException ex) {
			ex.printStackTrace();
		}
	}

	public void refresh() throws ApplicationConfigurationException {
		this.config.load();
		loadConfig();
	}

	public String getVersion() {
		return Version.getVersionNumber();
	}

	public boolean enableChangeLanguage() {
		return this.enableChangeLanguage;
	}

	public String getDynamicRSEMPath() {
		return dynamicRSEMPath;
	}

	public void setDynamicRSEMPath(String dynamicRSEMPath) {
		this.dynamicRSEMPath = dynamicRSEMPath;
	}

	public String getUploadFileMediaType() {
		return uploadFileMediaType;
	}

	public void setUploadFileMediaType(String uploadFileMediaType) {
		this.uploadFileMediaType = uploadFileMediaType;
	}

	public boolean isEnableKeywordsParse() {
		return enableKeywordsParse;
	}

	public void setEnableKeywordsParse(boolean enableKeywordsParse) {
		this.enableKeywordsParse = enableKeywordsParse;
	}
	
}
