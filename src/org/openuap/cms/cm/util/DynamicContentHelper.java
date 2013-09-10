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
package org.openuap.cms.cm.util;

import java.util.Map;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.cache.ContentModelCache;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 动态内容帮助类
 * </p>
 * 
 * <p>
 * $Id: DynamicContentHelper.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 3.0
 */
public class DynamicContentHelper {
	/**
	 * 获得内容标题
	 * 
	 * @param content
	 *            Map形式的内容
	 * @param tableId
	 *            内容模型id
	 * @return
	 */
	public static String getContentTitle(Map content, Long tableId) {
		ContentField cf = ContentModelCache.getTitleContentField(tableId);
		if (cf != null) {
			return (String) content.get(cf.getFieldName());
		}
		return null;
	}

	/**
	 * 获得内容主内容
	 * 
	 * @param content
	 *            Map形式的内容
	 * @param tableId
	 *            内容模型id
	 * @return
	 */
	public static String getContentMainContent(Map content, Long tableId) {
		ContentField cf = ContentModelCache.getMainContentField(tableId);
		if (cf != null) {
			Object rs = content.get(cf.getFieldName());
			if (rs != null) {
				return rs.toString();
			} else {
				return "";
			}
		}
		return "";
	}

	/**
	 * 返回内容表记录数目
	 * 
	 * @param tableId
	 * @return 若内容表还不存在，则返回-1
	 */
	public static long getContentCount(long tableId) {
		// 首先查看内容表定义是否存在
		ContentModelHelper helper = getContentModelHelper();

		if (helper != null) {
			if (!helper.isMappingXmlExists(tableId)) {
				return -1;
			} else {
				DynamicContentManager dcm = getDynamicContentManager();
				return dcm.getDynamictContentCount(tableId);
			}

		}
		return -2;
	}

	public static ContentModelHelper getContentModelHelper() {
		return (ContentModelHelper) ObjectLocator.lookup("contentModelHelper",
				CmsPlugin.PLUGIN_ID);
	}

	public static DynamicContentManager getDynamicContentManager() {
		return (DynamicContentManager) ObjectLocator.lookup(
				"dynamicContentManager", CmsPlugin.PLUGIN_ID);
	}
}
