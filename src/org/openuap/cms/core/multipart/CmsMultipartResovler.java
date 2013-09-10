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
package org.openuap.cms.core.multipart;

import org.openuap.cms.config.CMSConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 
 * <p>
 * CMS附件上传解析器，可以配置附件上传限制
 * </p>
 * 
 * <p>
 * $Id: CmsMultipartResovler.java 4021 2011-03-22 14:48:53Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class CmsMultipartResovler extends CommonsMultipartResolver implements
		InitializingBean {
	public CmsMultipartResovler() {

	}

	public void afterPropertiesSet() throws Exception {
		String uploadFileMaxSize = CMSConfig.getInstance()
				.getUploadFileMaxSize();
		this.setMaxUploadSize(Long.parseLong(uploadFileMaxSize));
		this.setDefaultEncoding(CMSConfig.getInstance().getEncoding());
	}
}
