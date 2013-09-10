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
package org.openuap.cms.search.index;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.runtime.setup.compass.CompassFactoryChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

/**
 * <p>
 * RSEM帮助工具类.
 * </p>
 * 
 * <p>
 * $Id: RSEMHelper.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class RSEMHelper implements ApplicationContextAware {

	private String rsemTpl;

	private String dynamicRSEMPath;

	private ApplicationContext applicationContext;

	private FreeMarkerConfigurer freemarkerConfigurer;

	private DynamicContentManager dynamicContentManager;

	public RSEMHelper() {
		initDefaultTplName();
	}

	protected void initDefaultTplName() {
		this.rsemTpl = "/plugin/cms/base/system/index/content_rsem_tpl.xml";
		dynamicRSEMPath = CMSConfig.getInstance().getDynamicRSEMPath();
	}

	/**
	 * 更新compass RSEM映射文件
	 * 
	 * @param ct
	 * @throws Exception
	 */
	public void updateRSEM(ContentTable ct) throws Exception {
		generateMappingXmlFiles(ct, dynamicRSEMPath);
		publishCompassFactoryChangeEvent();

	}

	/**
	 * 
	 * 
	 */
	private void publishCompassFactoryChangeEvent() {
		CompassFactoryChangeEvent ce = new CompassFactoryChangeEvent(this);
		applicationContext.publishEvent(ce);
	}

	/**
	 * 
	 * @param ct
	 * @throws Exception
	 */
	public void updateIndex(ContentTable ct) throws Exception {
		//
		List pList = dynamicContentManager.getDynamicPublish(ct
				.getContentTableName(), -1, -1);
		Set fields = ct.getContentFieldsSet();
		for (int i = 0; i < pList.size(); i++) {
			//
			Map publish = (Map) pList.get(i);

		}
	}

	/**
	 * 
	 * @param tpl
	 * @param ct
	 * 
	 * @return
	 * @throws
	 */
	public String generateMappingXml(String tpl, ContentTable ct)
			throws Exception {
		freemarker.template.Configuration fcfg = freemarkerConfigurer
				.getConfiguration();
		Template tp = fcfg.getTemplate(tpl);
		Map model = new HashMap(1);
		model.put("ct", ct);
		String mapping_xml = FreeMarkerTemplateUtils.processTemplateIntoString(
				tp, model);
		return mapping_xml;
	}

	public void generateMappingXmlFile(String tpl, ContentTable ct, File xmlFile)
			throws Exception {
		String mappingXml = this.generateMappingXml(tpl, ct);
		FileOutputStream out = new FileOutputStream(xmlFile);
		out.write(mappingXml.getBytes());
		out.close();
	}

	public void generateMappingXmlFiles(ContentTable ct, String xmlFilePath)
			throws Exception {
		String path = xmlFilePath + "/rsem_" + ct.getTableId() + ".cpm.xml";
		path = StringUtil.normalizePath(path);
		File f1 = new File(path);
		//
		generateMappingXmlFile(this.rsemTpl, ct, f1);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setDynamicRSEMPath(String dynamicRSEMPath) {
		this.dynamicRSEMPath = dynamicRSEMPath;
	}

	public void setFreemarkerConfigurer(
			FreeMarkerConfigurer freemarkerConfigurer) {
		this.freemarkerConfigurer = freemarkerConfigurer;
	}

	public void setRsemTpl(String rsemTpl) {
		this.rsemTpl = rsemTpl;
	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public static Object getNotNullObject(Object value,Object defaultValue){
		
    	if(value==null||!StringUtils.hasText(value.toString())){
    		return defaultValue;
    	}
    	return value;
    }
}
