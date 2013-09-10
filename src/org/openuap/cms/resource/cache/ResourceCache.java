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
package org.openuap.cms.resource.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.resource.manager.ResourceManager;
import org.openuap.cms.resource.model.Resource;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 资源缓存
 * </p>
 * 
 * <p>
 * $Id: ResourceCache.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ResourceCache extends CmsCache {
	
	private static JCS resCache = null;
	static {
		try {
			resCache = JCS.getInstance("res");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 以缓存方式获得资源
	 * @param id
	 * @return
	 */
	public static Resource getResource(Long id) {
		Object obj = resCache.get(id);
		if (obj == null) {
			ResourceManager resManager = (ResourceManager) ObjectLocator
					.lookup("cmsResourceManager", CmsPlugin.PLUGIN_ID);
			if (resManager != null) {
				obj = resManager.getResourceById(id);
				if (obj != null) {
					try {
						resCache.put(id, obj);
					} catch (CacheException ex) {
						log.error(ex);
					}
				}
			}
		}
		return (Resource) obj;
	}
}
