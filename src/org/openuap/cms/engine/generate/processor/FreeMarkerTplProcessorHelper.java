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
package org.openuap.cms.engine.generate.processor;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openuap.base.util.StringUtil;
import org.openuap.base.web.mvc.view.BaseClassTemplateLoader;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.cms.engine.profile.impl.PublishOperationProfileImpl;
import org.openuap.runtime.plugin.view.freemarker.PluginFreeMarkerConfigurer;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.openuap.runtime.setup.BaseConfigUtil;
import org.openuap.tpl.engine.plugin.FreeMarkerEngineContentFilter;
import org.openuap.tpl.engine.plugin.TplProcessorFilterPluginManager;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <p>
 * FreeMarker模板处理帮助者
 * </p>
 * 
 * <p>
 * $Id: FreeMarkerTplProcessorHelper.java 3966 2010-12-16 12:10:02Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class FreeMarkerTplProcessorHelper {

	private PluginFreeMarkerConfigurer freemarkerConfigurer;

	private Configuration configuration;

	/**
	 * 处理模板
	 * 
	 * @param tplContent
	 *            模板内容
	 * @param tplName
	 *            模板名称
	 * @param model
	 *            模型
	 * @param errors
	 *            错误
	 * @return 模板处理后的结果
	 */
	public String processTemplate(String tplContent, String tplName, Map model,
			List errors) {
		try {
			Configuration newConfiguration = this.getConfiguration();
			//
			String rs = freeMarkerProcess(tplContent, tplName, model,
					newConfiguration, errors);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setFreemarkerConfigurer(
			PluginFreeMarkerConfigurer freemarkerConfigurer) {
		this.freemarkerConfigurer = freemarkerConfigurer;
	}

	/**
	 * 具体处理模板
	 * 
	 * @param context
	 * @param configuration
	 * @param errors
	 * @return
	 */
	protected synchronized String freeMarkerProcess(String tplContent,
			String tplName, Map model, Configuration configuration, List errors) {
		// 性能诊断-start
		PublishOperationProfileImpl op = null;
		Exception exception = null;
		if (PublishProfileInfoHolder.isEnableProfile()) {
			op = new PublishOperationProfileImpl();
			op.setOperation("FreeMarkerProcessor");
			op.setStartTime(System.currentTimeMillis());
		}

		try {
			if (tplContent == null) {
				// 模板内容为空，不予处理
				return null;
			}
			// 从字符串构建模板实例
			Template template = new Template(tplName, new StringReader(
					tplContent), configuration);
			//
			//
			Map<String, Object> inModel = new HashMap<String, Object>();
			addContentModel(inModel, model, tplContent, configuration);
			//
			long s1 = System.currentTimeMillis();
			// TODO 影响模板的处理速度因素？
			String fileContent = FreeMarkerTemplateUtils
					.processTemplateIntoString(template, inModel);
			//
			//System.out.println("单纯处理模板渲染耗时："
			//		+ (System.currentTimeMillis() - s1) + "毫秒");
			return fileContent;

		} catch (Exception ex) {
			exception = ex;
			ex.printStackTrace();
			errors.add(ex);
			return null;
		} finally {
			// 性能诊断-end
			if (PublishProfileInfoHolder.isEnableProfile()) {
				op.setEndTime(System.currentTimeMillis());
				op.setException(exception);
				if (exception != null) {
					// 若有异常，保存此时的模板内容
					op.setTemplateContent(tplContent);
				}
				PublishProfileInfoHolder.getProfile().addPublishOperation(op);
			}
		}
	}

	protected CMSConfig getConfig() {
		return CMSConfig.getInstance();
	}

	/**
	 * 配置FreeMarker引擎
	 * 
	 * @param newConfiguration
	 * @param userTplPath
	 * @throws IOException
	 */
	private void settingConfiguration(Configuration newConfiguration,
			String userTplPath) throws IOException {
		// 1,get the dest template file name.
		String rootPath = this.getConfig().getTemplatePath();
		String sys_prefix = "/";
		String tmp_prefix = "/tmp/";
		// 系统模板路径
		String sysTplPath = rootPath;
		sysTplPath += sys_prefix;
		sysTplPath = StringUtil.normalizePath(sysTplPath);
		File sysTplDir = new File(sysTplPath);
		if (!sysTplDir.exists()) {
			sysTplDir.mkdirs();
		}
		// 临时模板路径
		String tmpTplPath = rootPath + tmp_prefix;
		tmpTplPath = StringUtil.normalizePath(tmpTplPath);
		File tmpTplDir = new File(tmpTplPath);
		if (!tmpTplDir.exists()) {
			tmpTplDir.mkdirs();
		}
		// 用户模板路径
		File userTplDir = new File(userTplPath);
		if (!userTplDir.exists()) {
			userTplDir.mkdirs();
		}
		//
		FileTemplateLoader ftl1 = new FileTemplateLoader(sysTplDir);
		FileTemplateLoader ftl2 = new FileTemplateLoader(userTplDir);
		FileTemplateLoader ftl3 = new FileTemplateLoader(tmpTplDir);
		// 设定模板装载路径
		// FIX:调整模板装载路径的顺序
		TemplateLoader[] loaders = new TemplateLoader[] { ftl3, ftl2, ftl1,
				this.freemarkerConfigurer.getTemplateClassLoader(),
				new BaseClassTemplateLoader() };
		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
		newConfiguration.setTemplateLoader(mtl);

		// 设置模板编码
		BaseApplicationConfiguration baseConfig = BaseConfigUtil.getConfig();
		String encoding = baseConfig.getString("cms.tpl.encoding", "UTF-8");
		//
		// 设置系统输入模板编码与内容输出编码
		// 系统可以设置模版的编码，这样可以适用于不同的模版编码
		// 内容输出的编码与模板编码相同
		// 在在线模版编辑器内将采用编码转换统一使用UTF-8来编辑模版
		newConfiguration.setDefaultEncoding(encoding);
		newConfiguration.setEncoding(Locale.getDefault(), encoding);
		newConfiguration.setEncoding(Locale.SIMPLIFIED_CHINESE, encoding);
		newConfiguration.setOutputEncoding(encoding);
		// 添加模板宏自动输入
		addAutoImport(newConfiguration);
		//
	}

	/**
	 * <p>
	 * 添加宏输入
	 * </p>
	 * TODO 解决多个宏使用同一前缀的问题
	 * 
	 * @param configuration
	 */
	protected void addAutoImport(Configuration configuration) {
		List<FreeMarkerEngineContentFilter> filters = getContentFilters();
		for (FreeMarkerEngineContentFilter filter : filters) {
			Map macroMap = filter.getMacroDefinitions();
			Set<String> keySet = macroMap.keySet();
			for (String key : keySet) {
				String value = (String) macroMap.get(key);
				value = value.trim();
				configuration.addAutoImport(key, value);
			}
		}
	}

	/**
	 * 添加模板处理器的模型数据
	 * 
	 * @param inModel
	 * @param model
	 */
	protected void addContentModel(Map<String, Object> inModel, Map model,
			String tplContent, Configuration configuration) {

		List<FreeMarkerEngineContentFilter> filters = getContentFilters();
		for (FreeMarkerEngineContentFilter filter : filters) {
			Map sharedVariableMap = filter.getSharedVariables(tplContent,
					model, configuration);
			Set<String> keySet = sharedVariableMap.keySet();
			for (String key : keySet) {
				inModel.put(key, sharedVariableMap.get(key));
			}
		}
		//
		inModel.putAll(model);
	}

	/**
	 * 取得FreeMarker配置对象
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		if (configuration == null) {
			try {
				configuration = freemarkerConfigurer.createConfiguration();
				// 获得用户模板路径
				String userTplPath = this.getConfig().getUserTemplatePath();
				userTplPath = StringUtil.normalizePath(userTplPath);
				// 设置模板引擎配置属性
				settingConfiguration(configuration, userTplPath);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return configuration;
	}

	/**
	 * 
	 * @param configuration
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	protected List<FreeMarkerEngineContentFilter> getContentFilters() {
		List<FreeMarkerEngineContentFilter> filters = TplProcessorFilterPluginManager
				.getContentFilters();
		return filters;
	}
}
