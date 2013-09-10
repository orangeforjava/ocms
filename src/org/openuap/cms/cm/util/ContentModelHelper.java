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
package org.openuap.cms.cm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.cfg.Environment;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.config.CMSConfig;
import org.openuap.runtime.setup.hibernate.MappingResourceChangeEvent;
import org.openuap.runtime.setup.persistence.hibernate.HibernateConfig;
import org.openuap.runtime.setup.persistence.hibernate.schema.SchemaHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

/**
 * <p>
 * 内容模型帮助者.
 * </p>
 * 
 * <p>
 * $Id: ContentModelHelper.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentModelHelper implements ApplicationContextAware,
		InitializingBean {

	private FreeMarkerConfigurer freemarkerConfigurer;

	private SchemaHelper schemaHelper;

	private String contentTableTpl;

	private String publishTableTpl;

	private String contributionTableTpl;

	private String collectionTableTpl;

	private String defaultScreensPath;

	//
	private ApplicationContext applicationContext;

	private HibernateConfig hibernateConfig;

	/**
	 * 
	 */
	public ContentModelHelper() {

	}

	protected void initDefaultTplName() {
		this.defaultScreensPath = "/plugin/cms/base/system/hibernate/";
		// 获得当前的数据库方言,Oracle用Oracle模板，其它用common模板
		String nowDialect = getHibernateConfig().getHibernateProperties()
				.getProperty("hibernate.dialect");
		if (nowDialect.equalsIgnoreCase("org.hibernate.dialect.Oracle9Dialect")) {
			defaultScreensPath += "oracle/";
		} else {
			defaultScreensPath += "common/";
		}
		this.contentTableTpl = defaultScreensPath + "cms_content.xml";
		this.publishTableTpl = defaultScreensPath + "cms_publish.xml";
		this.contributionTableTpl = defaultScreensPath + "cms_contribution.xml";
		this.collectionTableTpl = defaultScreensPath + "cms_collection.xml";

	}
	/**
	 * 判断Hibernate映射文件是否存在
	 * @param tableId
	 * @return
	 */
	public boolean isMappingXmlExists(long tableId) {
		String dynamicMappingPath = CMSConfig.getInstance()
				.getDynamicMappingPath();
		String finalXmlFilePath = dynamicMappingPath;
		String nowDialect = getHibernateConfig().getHibernateProperties()
				.getProperty("hibernate.dialect");
		if (nowDialect.equalsIgnoreCase("org.hibernate.dialect.Oracle9Dialect")) {
			finalXmlFilePath += "/oracle";
		} else {
			finalXmlFilePath += "/common";
		}
		String path = finalXmlFilePath + "/Content_" + tableId + ".hbm.xml";
		path = StringUtil.normalizePath(path);
		File f1 = new File(path);
		if (f1.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * 更新内容模型
	 * </p>
	 * 
	 * @param ct
	 * 
	 * @param script
	 * 
	 * @param doUpdate
	 * 
	 * @throws
	 */
	public void updateContentModel(ContentTable ct, boolean script,
			boolean doUpdate) throws Exception {
		freemarker.template.Configuration fcfg = freemarkerConfigurer
				.getConfiguration();
		// 1）更新数据库Schema
		updateContentTableModel(fcfg, ct, script, doUpdate);
		updatePublishTableModel(fcfg, ct, script, doUpdate);
		updateContributionTableModel(fcfg, ct, script, doUpdate);
		updateCollectionTableModel(fcfg, ct, script, doUpdate);
		// 2）发布准备更新映射文件事件
		publishModelChangeEvent(MappingResourceChangeEvent.TYPE_CREATE_MAPPING,
				ct);
	}

	/**
	 * 删除内容模型生成的表
	 * 
	 * @param ct
	 * @param script
	 * @param doUpdate
	 * @throws Exception
	 */
	public void dropContentModel(ContentTable ct, boolean script,
			boolean doUpdate) throws Exception {
		freemarker.template.Configuration fcfg = freemarkerConfigurer
				.getConfiguration();
		//
		dropContentTableModel(fcfg, ct, script, doUpdate);
		dropPublishTableModel(fcfg, ct, script, doUpdate);
		dropContributionTableModel(fcfg, ct, script, doUpdate);
		dropCollectionTableModel(fcfg, ct, script, doUpdate);
		//
		publishModelChangeEvent(MappingResourceChangeEvent.TYPE_DELETE_MAPPING,
				ct);
	}

	public void publishModelChangeEvent(int type, ContentTable ct) {
		MappingResourceChangeEvent mrce = new MappingResourceChangeEvent(this);
		mrce.setType(type);
		mrce.setTargetObject(ct);
		applicationContext.publishEvent(mrce);
	}

	/**
	 * 更新内容表
	 * 
	 * @param fcfg
	 * @param ct
	 * @param script
	 * @param doUpdate
	 * @throws Exception
	 */
	protected void updateContentTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		updateTableModel(contentTableTpl, fcfg, ct, script, doUpdate);
	}

	protected void dropContentTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		dropTableModel(contentTableTpl, fcfg, ct, script, doUpdate);
	}

	/**
	 * 
	 * @param fcfg
	 * @param ct
	 * @param script
	 * @param doUpdate
	 * @throws Exception
	 */
	protected void updatePublishTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		updateTableModel(publishTableTpl, fcfg, ct, script, doUpdate);
	}

	protected void dropPublishTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		dropTableModel(publishTableTpl, fcfg, ct, script, doUpdate);
	}

	/**
	 * 
	 * @param fcfg
	 * @param ct
	 * @param script
	 * @param doUpdate
	 * @throws Exception
	 */
	protected void updateContributionTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		updateTableModel(contributionTableTpl, fcfg, ct, script, doUpdate);
	}

	protected void dropContributionTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		dropTableModel(contributionTableTpl, fcfg, ct, script, doUpdate);
	}

	/**
	 * 
	 * @param fcfg
	 * @param ct
	 * @param script
	 * @param doUpdate
	 * @throws Exception
	 */
	protected void updateCollectionTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		updateTableModel(collectionTableTpl, fcfg, ct, script, doUpdate);
	}

	protected void dropCollectionTableModel(
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		dropTableModel(collectionTableTpl, fcfg, ct, script, doUpdate);
	}

	/**
	 * 更新Hibernate ORM表模型
	 * 
	 * @param tpl
	 * @param fcfg
	 * @param ct
	 * @param script
	 * @param doUpdate
	 * @throws Exception
	 */
	protected void updateTableModel(String tpl,
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {

		// System.out.println(mapping_xml);
		//
		org.hibernate.cfg.Configuration cfg = schemaHelper
				.makeBaseHibernateConfiguration();
		//
		// Dialect dialect = Dialect.getDialect(cfg.getProperties());
		// String newTpl = null;
		// if (dialect instanceof MySQLDialect) {
		// newTpl = dialectTplName(tpl, "mysql");
		// }
		// else if (dialect instanceof SQLServerDialect) {
		// newTpl = dialectTplName(tpl, "mssql");
		// }
		// // else if (dialect instanceof HSQLDialect) {
		// // newTpl = dialectTplName(tpl, "hsql");
		// // }
		// else {
		// newTpl = tpl;
		// }
		String newTpl = tpl;
		//
		Template tp = fcfg.getTemplate(newTpl);
		Map model = new HashMap(1);
		model.put("ct", ct);

		String mapping_xml = FreeMarkerTemplateUtils.processTemplateIntoString(
				tp, model);
		//
		// System.out.println("mapping_xml="+mapping_xml);
		//
		cfg.setProperty(Environment.DEFAULT_ENTITY_MODE, EntityMode.MAP
				.toString());
		cfg.addXML(mapping_xml);
		// System.out.println(cfg.buildSettings().getDialect().toString());
		schemaHelper.createTables(cfg, script, doUpdate);
	}

	/**
	 * 创建Hibernate映射文件
	 * 
	 * @param tpl
	 * @param ct
	 * @return
	 * @throws Exception
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

	/**
	 * 产生Hibernate内容模型映射文件
	 * 
	 * @param tpl
	 * @param ct
	 * @param xmlFile
	 * @throws Exception
	 */
	public void generateMappingXmlFile(String tpl, ContentTable ct, File xmlFile)
			throws Exception {
		String mappingXml = this.generateMappingXml(tpl, ct);
		FileOutputStream out = new FileOutputStream(xmlFile);
		out.write(mappingXml.getBytes());
		out.close();
	}

	/**
	 * 产生动态内容模型的映射文件
	 * 
	 * @param ct
	 * @param xmlFilePath
	 * @throws Exception
	 */
	public void generateMappingXmlFiles(ContentTable ct, String xmlFilePath)
			throws Exception {
		// 根据数据库方言调整最终需要放置的路径
		String finalXmlFilePath = xmlFilePath;
		String nowDialect = getHibernateConfig().getHibernateProperties()
				.getProperty("hibernate.dialect");
		if (nowDialect.equalsIgnoreCase("org.hibernate.dialect.Oracle9Dialect")) {
			finalXmlFilePath += "/oracle";
		} else {
			finalXmlFilePath += "/common";
		}
		String path = finalXmlFilePath + "/Content_" + ct.getTableId()
				+ ".hbm.xml";
		path = StringUtil.normalizePath(path);
		File f1 = new File(path);
		//
		generateMappingXmlFile(this.contentTableTpl, ct, f1);
		// //
		String path2 = finalXmlFilePath + "/Publish_" + ct.getTableId()
				+ ".hbm.xml";
		path2 = StringUtil.normalizePath(path2);
		File f2 = new File(path2);
		generateMappingXmlFile(this.publishTableTpl, ct, f2);
		// //
		String path3 = finalXmlFilePath + "/Contribution_" + ct.getTableId()
				+ ".hbm.xml";
		path3 = StringUtil.normalizePath(path3);
		File f3 = new File(path3);
		generateMappingXmlFile(this.contributionTableTpl, ct, f3);
		// //
		String path4 = finalXmlFilePath + "/Collection_" + ct.getTableId()
				+ ".hbm.xml";
		path4 = StringUtil.normalizePath(path4);
		File f4 = new File(path4);
		generateMappingXmlFile(this.collectionTableTpl, ct, f4);

	}

	/**
	 * 删除动态产生的Hibenate映射文件
	 * 
	 * @param ct
	 * @param xmlFilePath
	 * @throws Exception
	 */
	public void deleteMappingXmlFiles(ContentTable ct, String xmlFilePath)
			throws Exception {
		// 确定最终的存放目录
		String nowDialect = getHibernateConfig().getHibernateProperties()
				.getProperty("hibernate.dialect");
		String finalXmlFilePath = xmlFilePath;
		if (nowDialect.equalsIgnoreCase("org.hibernate.dialect.Oracle9Dialect")) {
			finalXmlFilePath += "/oracle";
		} else {
			finalXmlFilePath += "/common";
		}
		String path = finalXmlFilePath + "/Content_" + ct.getTableId()
				+ ".hbm.xml";
		path = StringUtil.normalizePath(path);
		File f1 = new File(path);
		if (f1.exists()) {
			f1.delete();
		}
		//
		String path2 = finalXmlFilePath + "/Publish_" + ct.getTableId()
				+ ".hbm.xml";
		path2 = StringUtil.normalizePath(path2);
		File f2 = new File(path2);
		if (f2.exists()) {
			f2.delete();
		}

		//
		String path3 = finalXmlFilePath + "/Contribution_" + ct.getTableId()
				+ ".hbm.xml";
		path3 = StringUtil.normalizePath(path3);
		File f3 = new File(path3);
		if (f3.exists()) {
			f3.delete();
		}
		//
		String path4 = finalXmlFilePath + "/Collection_" + ct.getTableId()
				+ ".hbm.xml";
		path4 = StringUtil.normalizePath(path4);
		File f4 = new File(path4);
		if (f4.exists()) {
			f4.delete();
		}
	}

	protected void dropTableModel(String tpl,
			freemarker.template.Configuration fcfg, ContentTable ct,
			boolean script, boolean doUpdate) throws Exception {
		Template tp = fcfg.getTemplate(tpl);
		Map model = new HashMap(1);
		model.put("ct", ct);
		String mapping_xml = FreeMarkerTemplateUtils.processTemplateIntoString(
				tp, model);
		// System.out.println(mapping_xml);
		//
		org.hibernate.cfg.Configuration cfg = schemaHelper
				.makeBaseHibernateConfiguration();
		cfg.addXML(mapping_xml);
		// System.out.println(cfg.buildSettings().getDialect().toString());
		//
		// drop table
		schemaHelper.dropTables(cfg, script, doUpdate);

	}

	public void setFreemarkerConfigurer(
			FreeMarkerConfigurer freemarkerConfigurer) {
		this.freemarkerConfigurer = freemarkerConfigurer;
	}

	public void setSchemaHelper(SchemaHelper schemaHelper) {
		this.schemaHelper = schemaHelper;
	}

	public void setPublishTableTpl(String publishTableTpl) {
		this.publishTableTpl = publishTableTpl;
	}

	public void setContentTableTpl(String contentTableTpl) {
		this.contentTableTpl = contentTableTpl;
	}

	public void setContributionTableTpl(String contributionTableTpl) {
		this.contributionTableTpl = contributionTableTpl;
	}

	public void setCollectionTableTpl(String collectionTableTpl) {
		this.collectionTableTpl = collectionTableTpl;
	}

	public String getCollectionTableTpl() {
		return collectionTableTpl;
	}

	public String getContentTableTpl() {
		return contentTableTpl;
	}

	public String getContributionTableTpl() {
		return contributionTableTpl;
	}

	public String getPublishTableTpl() {
		return publishTableTpl;
	}

	public SchemaHelper getSchemaHelper() {
		return schemaHelper;
	}

	public FreeMarkerConfigurer getFreemarkerConfigurer() {
		return freemarkerConfigurer;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected String dialectTplName(String tpl, String dialect) {
		String newTplName = "";
		int pos = tpl.lastIndexOf(".");
		if (pos > -1) {
			String name = tpl.substring(0, pos);
			String ext = tpl.substring(pos + 1);
			newTplName = name + "_" + dialect + "." + ext;
		} else {
			newTplName = tpl + "_" + dialect;
		}
		return newTplName;
	}

	public HibernateConfig getHibernateConfig() {
		return hibernateConfig;
	}

	public void setHibernateConfig(HibernateConfig hibernateConfig) {
		this.hibernateConfig = hibernateConfig;
	}

	public void afterPropertiesSet() throws Exception {
		initDefaultTplName();
	}

}
