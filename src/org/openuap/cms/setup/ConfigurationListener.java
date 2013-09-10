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
package org.openuap.cms.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.jcs.engine.control.CompositeCacheManager;
import org.openuap.runtime.config.ApplicationConfiguration;
import org.openuap.runtime.setup.BootstrapUtils;
import org.openuap.runtime.setup.cms.CmsConfigurationListener;

/**
 * <p>
 * 添加缓存配置.
 * </p>
 * 
 * <p>
 * $Id: ConfigurationListener.java 3920 2010-10-26 11:41:54Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ConfigurationListener extends CmsConfigurationListener {
	public ConfigurationListener() {
	}

	protected void postContextInitialized(ServletContextEvent event) {
		CompositeCacheManager ccm = CompositeCacheManager
				.getUnconfiguredInstance();
		Properties props = new Properties();
		try {
			String configPath = BootstrapUtils.getBootstrapManager("base")
					.getApplicationConfig().getString("sys.cache.config.path");
			File cacheConfigFile = new File(configPath);
			InputStream inputStream = new FileInputStream(cacheConfigFile);
			props.load(inputStream);
			parseProperties(props);
			ccm.configure(props);
			inputStream.close();
		} catch (Exception ex) {
			this.log.error(ex);
		}
	}

	protected void postContextDestroyed(ServletContextEvent event) {
		CompositeCacheManager.getInstance().shutDown();
	}

	protected void parseProperties(Properties props) {
		if (props != null) {
			//
			java.util.Enumeration keyEnum = props.propertyNames();
			while (keyEnum.hasMoreElements()) {

				String key = (String) keyEnum.nextElement();
				String value = props.getProperty(key);
				value = evaluate(value);
				props.setProperty(key, value);
			}
		}
	}

	/**
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	public String evaluate(String value) {
		int pos = value.indexOf("${");
		if (pos < 0) {
			return value;
		}
		int end = value.indexOf("}");
		if (end < pos + 2) {
			return value;
		}
		String alias = value.substring(pos + 2, end).trim();
		System.err.println(alias);
		String alias_value = null;
		if (alias
				.equalsIgnoreCase(ApplicationConfiguration.DEFAULT_HOME_PROPERTY)) {

			alias_value = BootstrapUtils.getBootstrapManager().getUAPHome();
		} else {
			alias_value = BootstrapUtils.getBootstrapManager()
					.getApplicationConfig().getString(alias);
		}
		if (alias_value == null) {
			return value;
		} else {
			value = value.substring(0, pos) + alias_value
					+ value.substring(end + 1);
			return value;
		}
	}

}
