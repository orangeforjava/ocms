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

import org.apache.commons.configuration.Configuration;

/**
 * Cms配置接口
 * <p>
 * $Id: ICmsConfig.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * <p>
 * @author Joseph
 *
 */
public interface ICmsConfig extends Configuration{
	/**
	 * 重新装载配置
	 */
	public void reloadConfig();
	
	public void setPwdEncryptArithmetic(String pwdEncryptArithmetic);

	public void setUserTemplatePath(String userTemplatePath);

	public void setDynamicMappingPath(String dynamicMappingPath);

	

	public void setUploadFileMaxSize(String uploadFileMaxSize);

	public void setEncoding(String encoding);

	public void setUploadFileAttachType(String uploadFileAttachType);

	public void setUploadFileFlashType(String uploadFileFlashType);

	public void setUploadFileImageType(String uploadFileImageType);

	public void setResourceRootPath(String resourceRootPath);

	public void setTemplatePath(String templatePath);

	public void setSystemTemplatePath(String systemTemplatePath);

	public void setTmpTemplatePath(String tmpTemplatePath);

	public void setTemplateSkinPath(String templateSkinPath);

	public void setTitle(String title);

	public void setBaseUrl(String baseUrl);

	public void setInitDataPath(String initDataPath);


	public String getPwdEncryptArithmetic();

	public String getUserTemplatePath();

	public String getDynamicMappingPath();



	public String getUploadFileMaxSize();

	public String getEncoding();

	public String getUploadFileAttachType() ;

	public String getUploadFileFlashType();

	public String getUploadFileImageType();

	public String getResourceRootPath();

	public String getSysRootPath();

	public String getTemplatePath();

	public String getSystemTemplatePath();

	public String getTmpTemplatePath();

	public String getTemplateSkinPath();

	public String getTitle() ;

	public String getBaseUrl();

	public String getInitDataPath() ;
}
