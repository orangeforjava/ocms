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
package org.openuap.cms.cm.event;

import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.cm.util.ContentModelHelper;
import org.openuap.cms.config.CMSConfig;
import org.openuap.runtime.setup.hibernate.MappingResourceChangeEvent;
import org.openuap.runtime.setup.hibernate.SessionFactoryChangeEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * Title: ContentModelMappingListener
 * </p>
 * 
 * <p>
 * Description: 动态内容模型映射.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: <a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: ContentModelMappingListener.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class ContentModelMappingListener implements ApplicationContextAware,
		ApplicationListener {
	/** 内容模型帮助者. */
	private ContentModelHelper contentModelHelper;
	/** 动态内容模型映射文件存放路径. */
	private String dynamicMappingPath;
	/** 应用上下文. */
	private ApplicationContext applicationContext;

	/**
	 * 
	 */
	public ContentModelMappingListener() {
		// 获取动态内容映射文件存放的路径
		dynamicMappingPath = CMSConfig.getInstance().getDynamicMappingPath();

	}

	/**
	 * 处理映射资源改变事件
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof MappingResourceChangeEvent) {
			MappingResourceChangeEvent mcEvent = (MappingResourceChangeEvent) event;
			if (mcEvent.getType() == MappingResourceChangeEvent.TYPE_CREATE_MAPPING) {
				ContentTable ct = (ContentTable) mcEvent.getTargetObject();
				try {
					// 新增了影射资源
					updateContentModelMapping(ct);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (mcEvent.getType() == MappingResourceChangeEvent.TYPE_DELETE_MAPPING) {
				ContentTable ct = (ContentTable) mcEvent.getTargetObject();
				try {
					// 删除了映射资源
					deleteContentModelMapping(ct);
				} catch (Exception ex1) {
					ex1.printStackTrace();
				}
			}

		}

	}

	/**
	 * update the dynamic mapping file.
	 * 
	 * @throws Exception
	 */
	public void updateContentModelMapping(ContentTable ct) throws Exception {
		//
		contentModelHelper.generateMappingXmlFiles(ct, dynamicMappingPath);
		//
		publishSessionFactoryChangeEvent();
	}

	/**
	 * delete the content model mapping xml file
	 * 
	 * @param ct
	 * @throws
	 */
	public void deleteContentModelMapping(ContentTable ct) throws Exception {
		//
		contentModelHelper.deleteMappingXmlFiles(ct, dynamicMappingPath);
		//
		publishSessionFactoryChangeEvent();
	}

	private void publishSessionFactoryChangeEvent() {
		SessionFactoryChangeEvent ce = new SessionFactoryChangeEvent(this);
		applicationContext.publishEvent(ce);
	}

	public void setContentModelHelper(ContentModelHelper contentModelHelper) {
		this.contentModelHelper = contentModelHelper;
	}

	public void setDynamicMappingPath(String dynamicMappingPath) {
		this.dynamicMappingPath = dynamicMappingPath;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
