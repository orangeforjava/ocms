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
package org.openuap.cms.user.security.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.Node;
import org.openuap.base.util.context.ECSTool;
import org.openuap.cms.user.model.PermissionObjectType;
import org.openuap.util.XMLUtil;

/**
 * <p>
 * 插件权限数据装载器.
 * </p>
 * 
 * <p>
 * $Id: PluginPermissionDataLoader.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PluginPermissionDataLoader {

	protected static final HashMap CACHE = new HashMap();
	protected static boolean CACHE_ENABLE = true;

	/**
	 * abc.xml->abc_zh_CN.xml
	 * 
	 * @param file
	 *            String
	 * @param locale
	 *            Locale
	 * @return String
	 */
	protected static String getLocaleName(String file, Locale locale) {
		if (locale == null) {
			return file;
		}
		char chars[] = file.toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean append = false;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '.' && !append) {
				sb.append("_" + locale.toString());
			}
			sb.append(chars[i]);
		}

		return sb.toString();
	}

	/**
	 * 权限数据类
	 * 
	 * @author Joseph
	 * 
	 */
	public class PermissionData extends LinkedHashMap {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8489772350244318993L;

		/**
		 * 
		 * @param doc
		 *            输入文档
		 */
		public void parse(Node doc) {
			//
			
			List list = doc
					.selectNodes("/child::node()/child::node()/child::node()");
			
			if (list == null || list.size() < 1) {
				return;
			}
			//System.out.println("list="+list);
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Node node = (Node) iter.next();
				//这里只使用了key与显示名称
				String key = node.valueOf("@key").trim();
				if (key != "") {
					long ikey = Long.parseLong(key);
					ikey = 0x1L << ikey;
					Long type = new Long(ikey);
					put(type, node.getText());
				}
			}

		}

		public String asHtml() {
			return asHtml(null);
		}

		public String asHtml(String s) {
			try {
				String keys[] = new String[size()];
				String values[] = new String[size()];
				keys = (String[]) keySet().toArray(keys);
				values = (String[]) values().toArray(values);
				return ECSTool.option(values, keys, s);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return "";
		}

		public PermissionData() {
		}
	}
	/**
	 * 权限类型数据
	 * @author Joseph
	 *
	 */
	public class PermissionTypeData extends LinkedHashMap {
		/**
		 * 
		 */
		private static final long serialVersionUID = 85457761278208120L;
		private LinkedHashMap permissionDataMap=new LinkedHashMap();
		/**
		 * 
		 * @param doc
		 *            输入文档
		 */
		public void parse(Node doc) {
			// xpath
			List permissionTypes = doc.selectNodes("//object");
			if (permissionTypes == null || permissionTypes.size() < 1) {
				return;
			}
			for (Iterator iter = permissionTypes.iterator(); iter.hasNext();) {
				Node node = (Node) iter.next();
				//
				String name = node.valueOf("@name");
				String key = node.valueOf("@key");
				String title = node.valueOf("@title");
				String type = node.valueOf("@type");
				//类型数据
				if (key != "") {
					int itype = Integer.parseInt(type);
					//构建权限对象类型
					PermissionObjectType pot = new PermissionObjectType(key,
							name, title, itype);
					put(key, pot);
				}
				//
				PermissionData permissionData=new PermissionData();
				permissionData.parse(node);
				permissionDataMap.put(key, permissionData);
			}
		}

		public String asHtml() {
			return asHtml(null);
		}

		public String asHtml(String s) {
			try {
				String keys[] = new String[size()];
				String values[] = new String[size()];
				keys = (String[]) keySet().toArray(keys);
				values = (String[]) values().toArray(values);
				return ECSTool.option(values, keys, s);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return "";
		}

		public PermissionTypeData() {
		}

		public LinkedHashMap getPermissionDataMap() {
			return permissionDataMap;
		}
	}

	public static PermissionData load(String file) {
		PermissionData map = null;
		if (CACHE_ENABLE) {
			map = (PermissionData) CACHE.get(file);
			if (map != null && !XMLUtil.isModified(file)) {
				return map;
			}
		}
		Document doc = XMLUtil.getDocument(file);
		map = (new PluginPermissionDataLoader()).creat();
		map.parse(doc);
		if (CACHE_ENABLE) {
			CACHE.put(file, map);
		}
		return map;
	}

	public static PermissionTypeData loadType(String file) {
		PermissionTypeData map = null;
		if (CACHE_ENABLE) {
			map = (PermissionTypeData) CACHE.get(file);
			if (map != null && !XMLUtil.isModified(file)) {
				return map;
			}
		}
		Document doc = XMLUtil.getDocument(file);
		map = (new PluginPermissionDataLoader()).creatType();
		map.parse(doc);
		if (CACHE_ENABLE) {
			CACHE.put(file, map);
		}
		return map;

	}

	public PermissionData creat() {
		return new PermissionData();
	}

	public PermissionTypeData creatType() {
		return new PermissionTypeData();
	}
}
