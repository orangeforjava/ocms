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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cache.CmsCache;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.runtime.util.ObjectLocator;

/**
 * 内容模型Cache
 * $Id: ContentModelCache.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * @author Joseph
 * 
 */
public class ContentModelCache extends CmsCache {
	private static JCS cmCache = null;
	static {
		try {
			cmCache = JCS.getInstance("cm");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以缓存方式获得内容属性列表
	 * 
	 * @param tid
	 *            内容模型id
	 * @return 属性列表
	 */
	public static List<ContentField> getContentFields(Long tid) {
		List<ContentTable> cts = getAllContentTables();
		for (ContentTable ct : cts) {
			if (ct.getTableId().equals(tid)) {
				Set fields = ct.getContentFieldsSet();
				if (fields != null) {
					List rs = new ArrayList();
					Iterator fieldIt = fields.iterator();
					while (fieldIt.hasNext()) {
						rs.add(fieldIt.next());
					}
					return rs;
				}
			}
		}
		return null;
	}
	/**
	 * 获得内容模型的标题域
	 * @param tid
	 * @return
	 */
	public static ContentField getTitleContentField(Long tid) {
		List<ContentTable> cts = getAllContentTables();
		for (ContentTable ct : cts) {
			if (ct.getTableId().equals(tid)) {
				Set<ContentField> fields = ct.getContentFieldsSet();
				if (fields != null) {
					Iterator<ContentField> fieldIt = fields.iterator();
					ContentField field = null;
					while (fieldIt.hasNext()) {
						field = fieldIt.next();
						if (field.getTitleField() == 1) {
							return field;
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 获得内容模型的主内容域
	 * @param tid
	 * @return
	 */
	public static ContentField getMainContentField(Long tid) {
		List<ContentTable> cts = getAllContentTables();
		for (ContentTable ct : cts) {
			if (ct.getTableId().equals(tid)) {
				Set<ContentField> fields = ct.getContentFieldsSet();
				if (fields != null) {
					Iterator<ContentField> fieldIt = fields.iterator();
					ContentField field = null;
					while (fieldIt.hasNext()) {
						field = fieldIt.next();
						if (field.getMainField() == 1) {
							return field;
						}
					}
				}
			}
		}
		return null;
	}
	public static ContentTable getContentTable(Long tid) {
		List<ContentTable> cts = getAllContentTables();
		for (ContentTable ct : cts) {
			if (ct.getTableId().equals(tid)) {
				return ct;
			}
		}
		return null;
	}

	public static ContentTable getContentTable(String alias) {
		List<ContentTable> cts = getAllContentTables();
		for (ContentTable ct : cts) {
			if (ct.getEntityPublishName().equalsIgnoreCase(alias)) {
				return ct;
			}
		}
		return null;
	}

	public static List getAllContentTables() {
		Object obj = cmCache.get("all");
		if (obj == null) {
			ContentTableManager ctManager = (ContentTableManager) ObjectLocator
					.lookup("contentTableManager", CmsPlugin.PLUGIN_ID);
			if (ctManager != null) {
				obj = ctManager.getAllContentTable();
				if (obj != null) {
					try {
						cmCache.put("all", obj);
					} catch (CacheException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (List) obj;
	}

	public static List getUserContentTables() {
		List<ContentTable> cts = getAllContentTables();
		List rs = new ArrayList();
		if (cts != null) {
			for (ContentTable ct : cts) {
				if (ct.getSystem() == 0) {
					rs.add(ct);
				}
			}
		}
		return rs;
	}

	public static List getSystemContentTables() {
		List<ContentTable> cts = getAllContentTables();
		List rs = new ArrayList();
		if (cts != null) {
			for (ContentTable ct : cts) {
				if (ct.getSystem() == 1) {
					rs.add(ct);
				}
			}
		}
		return rs;
	}

	public static void clear() {
		try {
			cmCache.remove("all");
			cmCache.clear();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得内容域Map对象
	 * 
	 * @param alias
	 * @return
	 */
	public static Map<String, ContentField> getFieldsMap(String alias) {
		Object rs = cmCache.get(alias + "-map");
		if (rs == null) {
			ContentTable ct = getContentTable(alias);
			if (ct != null) {
				Map<String, ContentField> fieldMap = new HashMap<String, ContentField>();
				List<ContentField> fields = getContentFields(ct.getTableId());
				for (ContentField field : fields) {
					fieldMap.put(field.getFieldName(), field);
				}
				try {
					cmCache.put(alias + "-map", fieldMap);
				} catch (CacheException e) {
					e.printStackTrace();
				}
				rs = fieldMap;
			}
		}
		return (Map<String, ContentField>) rs;
	}
}
