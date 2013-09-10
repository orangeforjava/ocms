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
package org.openuap.cms.vfs.cache;

import java.util.List;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.vfs.manager.FtpSettingManager;
import org.openuap.cms.vfs.model.FtpSetting;
import org.openuap.runtime.util.ObjectLocator;


/**
 * <p>
 * VFS缓存.
 * </p>
 * 
 * <p>
 * $Id: VfsCache.java 3916 2010-10-26 09:35:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class VfsCache extends CmsCache {
	private static JCS vfsCache = null;
	private static String FTP_SETTING_CACHE="all-ftpsetting";
	static {
		try {
			vfsCache = JCS.getInstance("vfsCache");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	public static List<FtpSetting> getAllFtpSetting() {
		Object obj = vfsCache.get(FTP_SETTING_CACHE);
		if (obj == null) {
			FtpSettingManager tplTagManager = (FtpSettingManager) ObjectLocator
					.lookup("ftpSettingManager", CmsPlugin.PLUGIN_ID);
			if (tplTagManager != null) {
				obj=tplTagManager.getAllFtpSettings();
				if(obj!=null){
					try {
						vfsCache.put(FTP_SETTING_CACHE, obj);
					} catch (CacheException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (List) obj;
	}
	public static void clearFtpSetting(){
		 try {
			vfsCache.remove(FTP_SETTING_CACHE);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	public static FtpSetting getFtpSetting(Long id){
		
		List <FtpSetting>ftpSettingList=getAllFtpSetting();
		if(ftpSettingList!=null){
			for(FtpSetting ftpSetting:ftpSettingList){
				if(ftpSetting.getId().equals(id)){
					return ftpSetting;
				}
			}
		}
		return null;
	}
	public static FtpSetting getFtpSettingByRefresh(Long id){
		clearFtpSetting();
		return getFtpSetting(id);
	}
}
