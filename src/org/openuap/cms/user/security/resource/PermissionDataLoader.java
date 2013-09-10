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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.openuap.base.util.context.ECSTool;
import org.openuap.base.util.resource.ResourceLoader;
import org.openuap.cms.user.model.PermissionObjectType;
import org.openuap.util.XMLUtil;

/**
 * 
 * <p>
 * 权限数据装载器.
 * </p>
 * 
 * <p>
 * $Id: PermissionDataLoader.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PermissionDataLoader extends ResourceLoader {
	
	public class PermissionData extends LinkedHashMap {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8489772350244318993L;

		/**
		 * 
		 * @param doc
		 *            Document
		 */
		public void parse(Document doc) {
			//
			List list = doc
					.selectNodes("/child::node()/child::node()/child::node()");
			if (list == null || list.size() < 1) {
				return;
			}
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Node node = (Node) iter.next();
				//
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

	public class PermissionTypeData extends LinkedHashMap {
		/**
		 * 
		 */
		private static final long serialVersionUID = 85457761278208120L;

		/**
		 * 
		 * @param doc
		 *            Document
		 */
		public void parse(Document doc) {
			// xpath
			List list = doc
					.selectNodes("/child::node()/child::node()/child::node()");
			if (list == null || list.size() < 1) {
				return;
			}
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Node node = (Node) iter.next();
				//
				String key = node.valueOf("@key").trim();
				String type = node.valueOf("@type").trim();
				String name = node.valueOf("@name").trim();
				if (key != "") {
					String title = node.getText();

					int itype = Integer.parseInt(type);
					//
					PermissionObjectType pot = new PermissionObjectType(key,
							name, title, itype);

					put(key, pot);
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

		public PermissionTypeData() {
		}

	}

	public static PermissionData load(String file) {
		PermissionData map = null;
		if (ResourceLoader.CACHE_ENABLE) {
			map = (PermissionData) ResourceLoader.CACHE.get(file);
			if (map != null && !XMLUtil.isModified(file)) {
				return map;
			}
		}
		Document doc = XMLUtil.getDocument(ResourceLoader.RESOURCE_ROOT + file);
		map = (new PermissionDataLoader()).creat();
		map.parse(doc);
		if (ResourceLoader.CACHE_ENABLE) {
			ResourceLoader.CACHE.put(file, map);
		}
		return map;
	}

	public static PermissionTypeData loadType(String file) {
		PermissionTypeData map = null;
		if (ResourceLoader.CACHE_ENABLE) {
			map = (PermissionTypeData) ResourceLoader.CACHE.get(file);
			if (map != null && !XMLUtil.isModified(file)) {
				return map;
			}
		}
		Document doc = XMLUtil.getDocument(ResourceLoader.RESOURCE_ROOT + file);
		map = (new PermissionDataLoader()).creatType();
		map.parse(doc);
		if (ResourceLoader.CACHE_ENABLE) {
			ResourceLoader.CACHE.put(file, map);
		}
		return map;

	}

	public PermissionData creat() {
		return new PermissionData();
	}

	public PermissionTypeData creatType() {
		return new PermissionTypeData();
	}

	public PermissionDataLoader() {
	}

}
