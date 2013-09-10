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
package org.openuap.cms.psn.cache;

import java.util.List;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * PSN缓存
 * <p>
 * <p>
 * $Id: PsnCache.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public class PsnCache extends CmsCache {
	private static JCS psnCache = null;
	private static String ALL_KEY="all";
	static {
		try {
			psnCache = JCS.getInstance("psn");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	public static Psn getPsn(Long id) {
		List <Psn> psns=getAll();
		if(psns!=null){
			for(Psn psn:psns){
				if(psn.getId().equals(id)){
					return psn;
				}
			}
		}
		return null;
	}
	public static List<Psn> getAll(){
		Object obj = psnCache.get(ALL_KEY);
		if (obj == null) {
			PsnManager psnManager = (PsnManager) ObjectLocator.lookup(
					"psnManager", CmsPlugin.PLUGIN_ID);
			if (psnManager != null) {
				obj=psnManager.getAllPsn();
				if(obj!=null){
					try {
						psnCache.put(ALL_KEY, obj);
					} catch (CacheException ex) {
						log.error(ex);
					}
				}
			}
		}
		return (List<Psn>) obj;
	}
	public static void clearAll(){
		try {
			psnCache.remove(ALL_KEY);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
}
