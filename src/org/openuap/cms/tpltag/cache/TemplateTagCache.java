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
package org.openuap.cms.tpltag.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.tpltag.manager.TemplateTagManager;
import org.openuap.cms.tpltag.model.TemplateTag;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 模板标记Cache
 * </p>
 * 
 * <p>
 * $Id: TemplateTagCache.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateTagCache extends CmsCache {
	private static JCS tplTagCache = null;
	static {
		try {
			tplTagCache = JCS.getInstance("tpltag");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以缓存方式获得指定内容模型下的Tag
	 * 
	 * @param modelId
	 *            内容模型id
	 * @return Tag列表
	 */
	public static List getTagByModel(Long modelId) {
		List <TemplateTag>tts=getAllTags();
		List rs=new ArrayList();
		for(TemplateTag tt:tts){
			if(tt.getModelId()!=null&&tt.getModelId().equals(modelId)){
				rs.add(tt);
			}
		}
		return rs;
	}
	/**
	 * 以缓存方式获得指定结点下的Tag
	 * @param nodeId 结点Id
	 * @return
	 */
	public static List getTagByNode(Long nodeId) {
		List <TemplateTag>tts=getAllTags();
		List rs=new ArrayList();
		for(TemplateTag tt:tts){
			if(tt.getNodeId()!=null&&tt.getNodeId().equals(nodeId)){
				rs.add(tt);
			}
		}
		return rs;
	}
	public static TemplateTag getTag(Long tid) {
		List <TemplateTag>tts=getAllTags();
		for(TemplateTag tt:tts){
			if(tt.getId().equals(tid)){
				return tt;
			}
		}
		return null;
	}
	public static TemplateTag getTagByName(String name) {
		List <TemplateTag>tts=getAllTags();
		for(TemplateTag tt:tts){
			if(tt.getName().equals(name)){
				return tt;
			}
		}
		return null;
	}
	public static List getAllTags() {
		Object obj = tplTagCache.get("all");
		if (obj == null) {
			TemplateTagManager tplTagManager = (TemplateTagManager) ObjectLocator
					.lookup("templateTagManager", CmsPlugin.PLUGIN_ID);
			if (tplTagManager != null) {
				obj=tplTagManager.getAllTags();
				if(obj!=null){
					try {
						tplTagCache.put("all", obj);
					} catch (CacheException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (List) obj;
	}
	
	public static void clear(){
		try {
			tplTagCache.remove("all");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
}
