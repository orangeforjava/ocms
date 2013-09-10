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
package org.openuap.cms.config.cache;

import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.cache.JCSCacheFactory;

import freemarker.template.Configuration;

/**
 * <p>
 * 配置缓存
 * </p>
 * 
 * <p>
 * $Id: ConfigurationCache.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public class ConfigurationCache extends CmsCache {
	public ConfigurationCache() {
	}

	public static Configuration getConfiguration(String tplEncoding) {
		Object obj = JCSCacheFactory.nodeCache.get(tplEncoding);
		try {
			return (Configuration) obj;
		} catch (Exception ex) {
			log.debug(ex);
			return null;
		}
	}

	public static void put(String tplEncoding, Configuration configuration) {
		try {
			JCSCacheFactory.configurationCache.put(tplEncoding, configuration);
		} catch (Exception ex) {
			log.error("Can't put object to cache", ex);
		}
	}

	public static void clear() {
		try {
			JCSCacheFactory.configurationCache.clear();
		} catch (CacheException ex) {
			log.error(ex);
		}

	}

}
