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
package org.openuap.cms.engine.macro;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.util.ResRefBean;

/**
 * <p>
 * CMS 模板宏引擎.
 * </p>
 * 
 * <p>
 * $Id: CmsMacroEngine.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface CmsMacroEngine {

	public List getCmsList(String nodeId, String num, String nodeGUID,
			String orderBy, String where, String TableID, String ignore,
			String page, String url);

	public PageBuilder getCmsListPageInfo(String nodeId, String num,
			String nodeGUID, String orderBy, String where, String TableID,
			String ignore, String page, String url);

	/**
	 * 从自定义编码中解析多资源列表
	 * 
	 * @param input
	 * @return
	 */
	public List<ResRefBean> getMultiRes(String input);

	/**
	 * 
	 * @param indexId
	 *            String
	 * @return List
	 */
	public List getCmsContent(String indexId);

	/**
	 * 获得内容，并指定排序条件
	 * 
	 * @param indexId
	 * @param orderby
	 *            排序条件
	 * @return
	 */
	public List getCmsContent(String indexId, String orderby);

	/**
	 * 获得内容
	 * 
	 * @param indexId
	 *            内容索引id
	 * @param orderby
	 *            内容排序方式
	 * @param preview
	 *            是否预览（预览的时候不需要发布即可查看最终效果）
	 * @return
	 */
	public List getCmsContent(String indexId, String orderby, boolean preview);

	/**
	 * get the node list
	 * 
	 * @param Type
	 *            String
	 * @param NodeID
	 *            String
	 * @param ignore
	 *            String
	 * @return List
	 */
	public List getCmsNodeList(String Type, String NodeID, String ignore);

	/**
	 * 
	 * 
	 * @param type
	 *            String
	 * @param NodeID
	 *            String
	 * @return Object
	 */
	public Object getCmsNode(String type, String NodeID);

	/**
	 * 返回JDBC SQL查询返回的结果，结果格式为List<Map>其中Map为字段名:值对
	 * 
	 * @param db
	 * @param sql
	 * @return
	 */
	public List<Map> getSqlSearchResult(String db, String sql);

	/**
	 * 返回秒数代表的日期值
	 * 
	 * @param seconds
	 *            秒数
	 * @return 日期值
	 */
	public Date getDate(long seconds);

	/**
	 * 返回毫秒数代表的日期值
	 * 
	 * @param millisseconds
	 *            毫秒
	 * @return 日期值
	 */
	public Date getDate2(long millisseconds);

	/**
	 * 获得现在的秒数
	 * 
	 * @return
	 */
	public Long getNow();

	/**
	 * 获得今天的秒数
	 * 
	 * @return
	 */
	public Long getToday();

	/**
	 * 获取指定内容的HTML标记剔出后的内容
	 * 
	 * @param content
	 *            输入内容
	 * @param keyword
	 *            查询关键字
	 * @param length
	 *            返回长度
	 * @return String 返回经过标记剔出的内容，关键字变红
	 */
	public String getExtractHtmlContent(String content, String keyword,
			int length);

	/**
	 * 
	 * @param nodeID
	 *            String
	 * @param num
	 *            String
	 * @param nodeGUID
	 *            String
	 * @param orderBy
	 *            String
	 * @param where
	 *            String
	 * @param TableID
	 *            String
	 * @param ignore
	 *            String
	 * @param page
	 *            String
	 * @param url
	 *            String
	 * @param keywords
	 *            String
	 * @param fields
	 *            String
	 * @return List
	 */
	public List getCmsSearchList(String nodeID, String num, String nodeGUID,
			String orderBy, String where, String TableID, String ignore,
			String page, String url, String ignoreIndex, String keywords,
			String fields);

	/**
	 * 
	 * @param content
	 * @param size
	 * @param type
	 * @return
	 */
	public PageBuilder getConentPagerInfo(String content, String size,
			String type);

	public String getExtraPublishPath(String id);

	public String getExtraPublishUrl(String id);

	/**
	 * 获得附加发布的内容
	 * 
	 * @param id
	 * @return
	 */
	public String getExtraPublishContent(String id);

	/**
	 * 高亮指定的关键字
	 * 
	 * @param input
	 * @param keywords
	 *            多个关键字以","分割
	 * @return
	 */
	public String highlightWords(String input, String keywords);
}
