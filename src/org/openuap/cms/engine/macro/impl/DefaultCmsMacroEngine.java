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
package org.openuap.cms.engine.macro.impl;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 缺省CMS 宏引擎实现.
 * </p>
 * 
 * 
 * <p>
 * $Id: DefaultCmsMacroEngine.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultCmsMacroEngine extends AbstractCmsMacroEngine implements
		CmsMacroEngine {

	private CmsMacroEngine repoCmsMacroEngine;

	/**
	 * 
	 * 
	 */
	public DefaultCmsMacroEngine() {
	}

	/**
	 * 内容列表
	 */
	public List getCmsList(String nodeId, String num, String nodeGUID,
			String orderBy, String where, String TableID, String ignore,
			String page, String url) {
		//

		String repoType = "db";
		// CMSConfig.getInstance().getStringProperty("cms.repo.type", "db");
		//
		if (repoType.equals("index")) {
			CmsMacroEngine indexedCmsMacroEngine = (CmsMacroEngine) ObjectLocator
					.lookup("indexedCmsMacroEngine", CmsPlugin.PLUGIN_ID);
			if (indexedCmsMacroEngine != null) {
				return indexedCmsMacroEngine.getCmsList(nodeId, num, nodeGUID,
						orderBy, where, TableID, ignore, page, url);
			}
		} else {

			return repoCmsMacroEngine.getCmsList(nodeId, num, nodeGUID,
					orderBy, where, TableID, ignore, page, url);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * 获得内容列表分页信息
	 */
	public PageBuilder getCmsListPageInfo(String nodeId, String num,
			String nodeGUID, String orderBy, String where, String TableID,
			String ignore, String page, String url) {
		String repoType = "db";
		// CMSConfig.getInstance().getStringProperty("cms.repo.type", "db");
		if (repoType.equals("index")) {
			CmsMacroEngine indexedCmsMacroEngine = (CmsMacroEngine) ObjectLocator
					.lookup("indexedCmsMacroEngine", CmsPlugin.PLUGIN_ID);
			return indexedCmsMacroEngine.getCmsListPageInfo(nodeId, num,
					nodeGUID, orderBy, where, TableID, ignore, page, url);

		} else {
			return repoCmsMacroEngine.getCmsListPageInfo(nodeId, num, nodeGUID,
					orderBy, where, TableID, ignore, page, url);
		}
	}

	public List getCmsContent(String indexId) {
		// 缺省使用索引模式
		String repoType = "db";
		// CMSConfig.getInstance().getStringProperty("cms.repo.type", "db");
		if (repoType.equals("index")) {
			CmsMacroEngine indexedCmsMacroEngine = (CmsMacroEngine) ObjectLocator
					.lookup("indexedCmsMacroEngine", CmsPlugin.PLUGIN_ID);
			return indexedCmsMacroEngine.getCmsContent(indexId);
		} else {
			return repoCmsMacroEngine.getCmsContent(indexId);
		}

	}

	public List getCmsContent(String indexId, String orderby) {
		// 缺省使用索引模式
		String repoType = "db";
		// CMSConfig.getInstance().getStringProperty("cms.repo.type", "index");
		if (repoType.equals("index")) {
			CmsMacroEngine indexedCmsMacroEngine = (CmsMacroEngine) ObjectLocator
					.lookup("indexedCmsMacroEngine", CmsPlugin.PLUGIN_ID);
			return indexedCmsMacroEngine.getCmsContent(indexId, orderby);
		} else {
			return repoCmsMacroEngine.getCmsContent(indexId, orderby);
		}

	}

	public List getCmsNodeList(String Type, String NodeID, String ignore) {
		return repoCmsMacroEngine.getCmsNodeList(Type, NodeID, ignore);
	}

	public Object getCmsNode(String type, String NodeID) {
		return repoCmsMacroEngine.getCmsNode(type, NodeID);
	}

	public static void main(String[] args) {
		DefaultCmsMacroEngine engine = new DefaultCmsMacroEngine();
		// String test = "<img
		// src=\"http://www.google.com\"/>&nbsp;&nbsp;&nbsp;&nbsp;<P>&nbsp;&nbsp;&nbsp;
		// <font
		// color=blue>始建于1949年，</font>最初为中国人民解放军第一军医大学护训班（地址在天津），1949年10月招收第一批学员。1951年，由于抗美援朝的需要，又开设了“中级医务训练...";
		// String keyword = "天津";
		// String rs = engine.getExtractHtmlContent(test, keyword, 200);
		// System.out.println("rs=" + rs);
		String testUrl = "http://localhost.org.cn:8080/web";
		String pattern = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)*(:[a-zA-Z0-9]*)?([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
		//
		String path = null;
		Pattern p = Pattern.compile(pattern);
		// System.out.println("psnUrlInfo=" + testUrl);
		Matcher m = p.matcher(testUrl);
		boolean found = m.find();
		if (found) {
			path = m.group(4);
			// System.out.println("path="+path);
			//
		}
		if (path == null) {
			path = "";
		}
		// path = "/" + path + "/" + fileName;
		path = path.replaceAll("\\/\\/", "/");
		// System.out.println("path="+path);
	}

	/**
	 * 搜索，可以配置采用索引或者数据库方式
	 */
	public List getCmsSearchList(String nodeID, String num, String nodeGUID,
			String orderBy, String where, String TableID, String ignore,
			String page, String url, String ignoreIndex, String keywords,
			String fields) {
		String repoType = CMSConfig.getInstance().getStringProperty(
				"cms.repo.type", "index");
		//
		if (repoType.equals("index")) {
			CmsMacroEngine indexedCmsMacroEngine = (CmsMacroEngine) ObjectLocator
					.lookup("indexedCmsMacroEngine", CmsPlugin.PLUGIN_ID);
			if (indexedCmsMacroEngine != null) {
				return indexedCmsMacroEngine.getCmsSearchList(nodeID, num,
						nodeGUID, orderBy, where, TableID, ignore, page, url,
						ignoreIndex, keywords, fields);
			}
		} else {
			return repoCmsMacroEngine.getCmsSearchList(nodeID, num, nodeGUID,
					orderBy, where, TableID, ignore, page, url, ignoreIndex,
					keywords, fields);
		}
		return Collections.EMPTY_LIST;

	}

	public String getExtraPublishContent(String id) {
		// 调用Repo实现
		return repoCmsMacroEngine.getExtraPublishContent(id);
	}

	public void setRepoCmsMacroEngine(CmsMacroEngine repoCmsMacroEngine) {
		this.repoCmsMacroEngine = repoCmsMacroEngine;
	}

	public String getExtraPublishPath(String id) {
		return this.repoCmsMacroEngine.getExtraPublishPath(id);
	}

	public String getExtraPublishUrl(String id) {
		return this.repoCmsMacroEngine.getExtraPublishUrl(id);
	}
	
	public List getCmsContent(String indexId, String orderby, boolean preview) {
		//
		return repoCmsMacroEngine.getCmsContent(indexId, orderby,preview);
	}

}
