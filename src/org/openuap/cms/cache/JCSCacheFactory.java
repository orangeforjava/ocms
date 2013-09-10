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
package org.openuap.cms.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * <p>
 * CMS缓存管理工厂
 * </p>
 * <p>
 * $Id: JCSCacheFactory.java 3961 2010-11-11 03:06:16Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class JCSCacheFactory {
	public static JCS nodeCache;
	public static JCS configurationCache;
	public static JCS advertiseCache;
	
	public JCSCacheFactory() {
	}

	static {
		try {
			nodeCache = JCS.getInstance("node");
			configurationCache = JCS.getInstance("configuration");
			advertiseCache = JCS.getInstance("advertise");
		} catch (CacheException ex) {
			ex.printStackTrace();
		}
	}

}
