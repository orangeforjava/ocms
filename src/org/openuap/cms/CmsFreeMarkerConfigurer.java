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
package org.openuap.cms;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openuap.base.util.StringUtil;
import org.openuap.runtime.plugin.view.freemarker.PluginFreeMarkerConfigurer;
import org.openuap.tpl.engine.plugin.FreeMarkerEngineContentFilter;
import org.openuap.tpl.engine.plugin.TplProcessorFilterPluginManager;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * <p>
 * CMS后台FreeMarker配置器
 * </p>
 * 
 * <p>
 * $Id: CmsFreeMarkerConfigurer.java 3963 2010-12-06 14:56:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CmsFreeMarkerConfigurer extends PluginFreeMarkerConfigurer {

	@Override
	protected String getPluginId() {
		return CmsPlugin.PLUGIN_ID;
	}

	@Override
	protected void postProcessConfiguration(Configuration config)
			throws IOException, TemplateException {
		String securityMacro = "/plugin/cms/base/macros/security-macros.html";
		// BaseApplicationConfiguration baseConfig=BaseConfigUtil.getConfig();
		config.addAutoImport("security", securityMacro);
		config.addAutoImport("ui", "/plugin/cms/workbench/macros/uiMacro.html");
		config.addAutoImport("mypager","/plugin/cms/base/macros/pager-macros.html");
		config.addAutoImport("tabler","/plugin/cms/base/macros/tabler-macros.html");
		
		// 系统模版都用UTF-8
		String encoding = "utf-8";// baseConfig.getString("sys.charset.encoding","utf-8");
		// System.out.println("encoding="+encoding);
		config.setDefaultEncoding(encoding);
		config.setEncoding(Locale.SIMPLIFIED_CHINESE, encoding);
		config.setEncoding(Locale.TRADITIONAL_CHINESE, encoding);
		config.setEncoding(Locale.getDefault(), encoding);
		// 自动设置模板语法
		config.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
		//进行自动导入
		addAutoImport(config);
		addContentModel(config);
		//
		config.setSharedVariable("version", new Version());
		config.setSharedVariable("StringUtil", new StringUtil());
	}

	/**
	 * 添加宏输入 TODO 解决多个宏使用同一前缀的问题
	 * 
	 * @param configuration
	 */
	protected void addAutoImport(Configuration configuration) {
		List<FreeMarkerEngineContentFilter> filters = getContentFilters();
		for (FreeMarkerEngineContentFilter filter : filters) {
			//
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
	protected void addContentModel(Configuration configuration) {

		List<FreeMarkerEngineContentFilter> filters = getContentFilters();
		for (FreeMarkerEngineContentFilter filter : filters) {
			Map sharedVariableMap = filter.getSharedVariables("",
					new HashMap(), configuration);
			Set<String> keySet = sharedVariableMap.keySet();
			for (String key : keySet) {
				try {
					configuration.setSharedVariable(key, sharedVariableMap
							.get(key));
				} catch (TemplateModelException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 从插件中获取内容模板引擎过滤器
	 * @return
	 */
	protected List<FreeMarkerEngineContentFilter> getContentFilters() {
		List<FreeMarkerEngineContentFilter> filters = TplProcessorFilterPluginManager
				.getContentFilters(CmsPlugin.PLUGIN_ID, true);

		return filters;
	}
}
