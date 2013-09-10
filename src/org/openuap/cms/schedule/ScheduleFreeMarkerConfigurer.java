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
package org.openuap.cms.schedule;

import java.io.IOException;
import java.util.Locale;

import org.openuap.runtime.plugin.view.freemarker.PluginFreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 
 * <p>
 * 计划任务管理插件FreeMarker配置器
 * </p>
 * $Id: ScheduleFreeMarkerConfigurer.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * 
 * @author Joseph
 * 
 */
public class ScheduleFreeMarkerConfigurer extends PluginFreeMarkerConfigurer {
	@Override
	protected String getPluginId() {
		return SchedulePlugin.PLUGIN_ID;
	}

	@Override
	protected void postProcessConfiguration(Configuration config)
			throws IOException, TemplateException {

		// 系统模版都用UTF-8
		String encoding = "utf-8";//
		//
		config.setDefaultEncoding(encoding);
		config.setEncoding(Locale.SIMPLIFIED_CHINESE, encoding);
		config.setEncoding(Locale.TRADITIONAL_CHINESE, encoding);
		config.setEncoding(Locale.getDefault(), encoding);
		// 自动设置模板语法
		config.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
	}
}
