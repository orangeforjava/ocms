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
package org.openuap.cms.cm.cache;

import java.util.ArrayList;
import java.util.List;

import org.josql.Query;
import org.josql.QueryResults;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.runtime.plugin.WebPluginManagerUtils;

/**
 * <p>
 * $Id: ContentModelQuickQuery.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * 
 */
public class ContentModelQuickQuery {

	
	public final static String CONTENTFIELD_CLASSNAME = "org.openuap.cms.cm.model.ContentField";

	

	/**
	 * 以内存查询方式获得指定的内容域
	 * 
	 * @param fieldId
	 * @return
	 */
	public static ContentField getField(List fields,Long fieldId) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM " + CONTENTFIELD_CLASSNAME
				+ " WHERE contentFieldId=" + fieldId;
		try {
			q.parse(osql);
			results = q.execute(fields);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return (ContentField) rs.get(0);
		}
		return null;
	}

	/**
	 * 以内存查询方式获得搜索域
	 * 
	 * @return
	 */
	public static List getSearchFields(List fields) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID));

		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM " + CONTENTFIELD_CLASSNAME
				+ " WHERE fieldSearchable=1";
		try {
			q.parse(osql);
			results = q.execute(fields);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return rs;
		}
		return new ArrayList();
	}

	/**
	 * 以内存查询方式获得标题域
	 * 
	 * @return
	 */
	public static ContentField getTitleField(List fields) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM " + CONTENTFIELD_CLASSNAME
				+ " WHERE titleField=1";
		try {
			q.parse(osql);
			results = q.execute(fields);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return (ContentField) rs.get(0);
		}
		return null;
	}

	public static ContentField getPhotoField(List fields) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM " + CONTENTFIELD_CLASSNAME
				+ " WHERE photoField=1";
		try {
			q.parse(osql);
			results = q.execute(fields);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return (ContentField) rs.get(0);
		}
		return null;
	}

	public static ContentField getKeywordsField(List fields) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM " + CONTENTFIELD_CLASSNAME
				+ " WHERE keywordsField=1";
		try {
			q.parse(osql);
			results = q.execute(fields);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return (ContentField) rs.get(0);
		}
		return null;
	}

	public static ContentField getMainField(List fields) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM " + CONTENTFIELD_CLASSNAME
				+ " WHERE mainField=1";
		try {
			q.parse(osql);
			results = q.execute(fields);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null && rs.size() > 0) {
			return (ContentField) rs.get(0);
		}
		return null;
	}
}
