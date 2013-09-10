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
package org.openuap.cms.comment.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SystemConfiguration;
import org.openuap.cms.CmsPlugin;
import org.openuap.runtime.config.PluginConfig;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.openuap.runtime.setup.BootstrapUtils;

/**
 * <p>
 * 评论配置工厂
 * </p>
 * 
 * <p>
 * $Id: CommentConfigFactory.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class CommentConfigFactory {

	private static CommentConfigFactory _instance;

	private static Object lock = new Object();

	public static String COMMENT_CONFIG_FILENAME = "comment.xml";
	
	

	private Map configs = new HashMap();
	
	private Configuration config;

	/**
	 * 
	 * @param config
	 * 
	 */
	private CommentConfigFactory(Configuration config) {
		this.config = config;
	}

	/**
	 * 获得配置工厂实例
	 * 
	 * @return
	 * @param config
	 * 
	 */
	public static CommentConfigFactory getInstance(Configuration config) {
		synchronized (lock) {
			if (_instance == null) {
				_instance = new CommentConfigFactory(config);
			}
		}
		return _instance;
	}

	public static CommentConfigFactory getInstance() {
		synchronized (lock) {
			if (_instance == null) {
				SystemConfiguration config = new SystemConfiguration();
				_instance = new CommentConfigFactory(config);
			}
		}
		return _instance;

	}
	/**
	 * 获得系统基础配置
	 * 
	 * @return BaseConfig
	 */
	public BaseApplicationConfiguration getBaseConfig() {
		return BootstrapUtils.getBootstrapManager("base")
				.getApplicationConfig();
	}
	/**
	 * 
	 * @return
	 */
	public ICommentConfig getXmlCommentConfig() {
		Object obj = configs.get(COMMENT_CONFIG_FILENAME);
		if (obj == null) {
			XmlCommentConfig config = new XmlCommentConfig();
			//
			PluginConfig pluginConfig=WebPluginManagerUtils.getPluginConfig(CmsPlugin.PLUGIN_ID);
			//
			if (pluginConfig!=null) {
				//
				pluginConfig.setConfigurationFileName("comment-cfg.xml");
				config.setConfiguration(pluginConfig);
				config.reloadConfig();
				configs.put(COMMENT_CONFIG_FILENAME, config);				
				return config;
			} else {
				return null;
			}
		} else {
			return (ICommentConfig) obj;
		}
	}
	
}
