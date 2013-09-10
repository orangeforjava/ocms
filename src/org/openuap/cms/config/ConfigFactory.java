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
import org.apache.commons.configuration.SystemConfiguration;
import org.openuap.cms.CmsPlugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.openuap.runtime.setup.BootstrapUtils;

/**
 * CMS 配置工厂
 * 
 * $Id: ConfigFactory.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * @author Joseph
 * 
 */
public class ConfigFactory {

	private static ConfigFactory _instance;

	private static Object lock = new Object();

	public static String CMS_CONFIG_FILENAME = "cms.cfg.xml";

	private Configuration configuration;

	private static ICmsConfig config = null;

	/**
	 * 
	 * @param config
	 * 
	 */
	private ConfigFactory(Configuration config) {
		this.configuration = config;
	}

	/**
	 * 获得配置工厂实例
	 * 
	 * @return
	 * @param config
	 * 
	 */
	public static ConfigFactory getInstance(Configuration config) {
		synchronized (lock) {
			if (_instance == null) {
				_instance = new ConfigFactory(config);
			}
		}
		return _instance;
	}

	public static ConfigFactory getInstance() {
		synchronized (lock) {
			if (_instance == null) {
				SystemConfiguration config = new SystemConfiguration();
				_instance = new ConfigFactory(config);
			}
		}
		return _instance;

	}

	/**
	 * 获得系统基础配置
	 * 
	 * @return BaseConfig
	 */
	public static BaseApplicationConfiguration getBaseConfig() {
		return BootstrapUtils.getBootstrapManager().getApplicationConfig();
	}
	/**
	 * 获得CMS配置，从插件自己的目录中取配置信息
	 * TODO 目前只支持了XML，对于DB方式的配置支持待加入
	 * @return
	 */
	public static ICmsConfig getCmsConfig() {
		if (config == null) {
			CmsPlugin plugin = (CmsPlugin) WebPluginManagerUtils.getPlugin(
					"base", CmsPlugin.PLUGIN_ID);
			if (plugin != null) {
				config = new XmlCmsConfig();
				((XmlCmsConfig)config).setConfiguration(plugin.getPluginConfig());
				;
				config.reloadConfig();
			}
		} 
		return config;
	}
}
