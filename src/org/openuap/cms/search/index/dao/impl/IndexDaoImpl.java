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
package org.openuap.cms.search.index.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.Resource;
import org.openuap.base.dao.search.DynamicCompassDaoSupport;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.cm.model.ContentTable;
import org.openuap.cms.node.cache.NodeCache;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.repo.dao.DynamicContentDao;
import org.openuap.cms.search.index.RSEMHelper;
import org.openuap.cms.search.index.dao.IndexDao;
import org.openuap.util.extractor.HtmlExtractor;

/**
 * <p>
 * 索引DAO实现.
 * </p>
 * 
 * <p>
 * $Id: IndexDaoImpl.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class IndexDaoImpl extends DynamicCompassDaoSupport implements IndexDao {

	//

	private ContentTableManager contentTableManager;

	private DynamicContentDao dynamicContentDao;

	public void indexContent(Long indexId) {
		//
		Map contentIndex = dynamicContentDao.getContentIndexMapById(indexId);
		Long tid = (Long) contentIndex.get("tableId");
		ContentTable ct = contentTableManager.getContentTableFromCache(tid);
		if (ct != null && ct.getAllowIndex() > 0) {
			// 此模型允许建立索引
			String pName1 = ct.getEntityPublishName();
			if (pName1 == null || pName1.equals("")) {
				pName1 = "Publish_" + tid;
			}
			Set sf = ct.getContentFieldsSet();
			final ContentField[] fields = (ContentField[]) sf
					.toArray(new ContentField[sf.size()]);
			final List pList = dynamicContentDao.getDynamicPublish(pName1,
					indexId.toString());
			final int size = pList.size();
			final String pName = pName1;
			this.getCompassTemplate().execute(
					new MyCompassCallback(pList, pName, fields));

		}
	}

	/**
	 * 批量索引内容
	 * 
	 * @param indexIds
	 */
	public void indexContents(String indexIds) {
		if (indexIds != null) {
			String[] ids = indexIds.split(",");
			int size = ids.length;
			for (int i = 0; i < size; i++) {
				String id = ids[i];
				this.indexContent(new Long(id));
			}
		}
	}

	/**
	 * 按照内容表进行索引
	 */
	public void indexContentByTable(Long tid) {
		ContentTable ct = contentTableManager.getContentTableFromCache(tid);
		// List cfs = contentFieldDao.getAllContentField(tid);
		if (ct != null && ct.getAllowIndex() > 0) {
			String pName1 = ct.getEntityPublishName();
			if (pName1 == null || pName1.equals("")) {
				pName1 = "Publish_" + tid;
			}
			final String pName = pName1;
			//
			Set sf = ct.getContentFieldsSet();
			final ContentField[] fields = (ContentField[]) sf
					.toArray(new ContentField[sf.size()]);
			//首先删除旧数据			
			this.getCompassTemplate().execute(new CompassCallback(){
				public Object doInCompass(CompassSession session)
						throws CompassException {
					CompassQueryBuilder queryBuilder = session.queryBuilder();
					CompassQuery query=queryBuilder.alias(pName);
					session.delete(query);
					return null;
				}});
			//
			int totalNum = dynamicContentDao.getDynamicPublishCount(pName);
			PageBuilder pb = new PageBuilder(500);
			pb.items(totalNum);
			int totalPages = pb.pages();
			System.out.println("totalNum=" + totalNum + ";totalPages="
					+ totalPages);
			for (int p = 1; p <= totalPages; p++) {
				pb.page(p);
				int start = pb.beginIndex() - 1;
				int limit = 500;
				//
				final List pList = dynamicContentDao.getDynamicPublish(pName,
						start, limit);
				final int size = pList.size();
				//
				this.getCompassTemplate().execute(
						new MyCompassCallback(pList, pName, fields));
				//
			}

		}
	}

	public void setDynamicContentDao(DynamicContentDao dynamicContentDao) {
		this.dynamicContentDao = dynamicContentDao;
	}

	/**
	 * 删除指定的索引内容
	 */
	public void deleteIndexContent(Long indexId) {
		Map contentIndex = dynamicContentDao.getContentIndexMapById(indexId);
		Long tid = (Long) contentIndex.get("tableId");
		ContentTable ct = contentTableManager.getContentTableFromCache(tid);
		if (ct != null && ct.getAllowIndex() > 0) {
			// 此模型允许建立索引
			String pName1 = ct.getEntityPublishName();
			if (pName1 == null || pName1.equals("")) {
				pName1 = "Publish_" + tid;
			}
			Resource r = this.getCompassTemplate().getResource(pName1, indexId);
			if (r != null) {
				// 执行索引删除操作
				System.out.println("执行索引删除操作 indexId=" + indexId);
				this.getCompassTemplate().delete(r);
			}
		}

	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}

	private String getNotValue(Object input, String defaultValue) {
		String out = RSEMHelper.getNotNullObject(input, defaultValue)
				.toString();
		return out;
	}

	private String processText(Object input, String defaultValue) {
		String out = RSEMHelper.getNotNullObject(input, defaultValue)
				.toString();
		HtmlExtractor extractor = new HtmlExtractor();
		out = extractor.getText(out);
		// 替换掉分页标识
		String ppattern = "#p#([\\s\\p{Print}[^\\x00-\\xff]&&[^#]]*?)#e#";
		out = out.replaceAll(ppattern, "");
		return out;
	}
	
	private class MyCompassCallback implements CompassCallback {
		private List pList;
		private int size;

		private String pName;
		private ContentField[] fields;

		public MyCompassCallback(List pList, String pName, ContentField[] fields) {
			this.pName = pName;
			this.pList = pList;
			size = pList.size();
			this.fields = fields;
		}

		public Object doInCompass(CompassSession session)
				throws CompassException {
			//
			CompassTransaction tr = session.beginTransaction();
			long startIndexTime = System.currentTimeMillis();
			//
			
			//
			for (int i = 0; i < size; i++) {
				//
				Map pMap = (Map) pList.get(i);
				//
				//过滤掉所在结点已经被删除的情况
				Long nid = new Long(pMap.get("nodeId").toString());
				Node node = NodeCache.getNode(nid);
				if (node != null && node.getDisabled() == 0) {
					Resource r = session.resourceFactory()
							.createResource(pName);
					//				
					r.addProperty("indexId", pMap.get("indexId"));
					r.addProperty("contentId", pMap.get("contentId"));
					r.addProperty("nodeId", pMap.get("nodeId"));
					r.addProperty("publishDate", pMap.get("publishDate"));
					r.addProperty("creationDate", pMap.get("creationDate"));
					r.addProperty("modifiedDate", pMap.get("modifiedDate"));
					r.addProperty("creationUserId", pMap.get("creationUserId"));
					r.addProperty("creationUserName", getNotValue(pMap
							.get("creationUserName"), "null"));
					r.addProperty("lastModifiedUserId", pMap
							.get("lastModifiedUserId"));
					r.addProperty("lastModifiedUserName", getNotValue(pMap
							.get("lastModifiedUserName"), "null"));
					r.addProperty("contributionUserId", pMap
							.get("contributionUserId"));
					r.addProperty("contributionUserName", getNotValue(pMap
							.get("contributionUserName"), "null"));
					r.addProperty("contributionId", pMap.get("contributionId"));
					r.addProperty("url", getNotValue(pMap.get("url"), "null"));
					r.addProperty("top", getNotValue(pMap.get("top"), "0"));
					r.addProperty("pink", getNotValue(pMap.get("pink"), "0"));
					r.addProperty("sort", getNotValue(pMap.get("sort"), "0"));
					r.addProperty("nodeUrl", getNotValue(pMap.get("nodeUrl"),
							"null"));
					r.addProperty("nodeName", getNotValue(pMap.get("nodeName"),
							"null"));
					r.addProperty("hitsTotal", pMap.get("hitsTotal"));
					r.addProperty("hitsToday", pMap.get("hitsToday"));
					r.addProperty("hitsWeek", pMap.get("hitsWeek"));
					r.addProperty("hitsMonth", pMap.get("hitsMonth"));
					r.addProperty("commentNum", pMap.get("commentNum"));
					r.addProperty("hitsDate", pMap.get("hitsDate"));
					//
					for (int j = 0; j < fields.length; j++) {
						ContentField cf = fields[j];
						if (!cf.getIndexType().equalsIgnoreCase("NO")
								|| !cf.getStoreType().equalsIgnoreCase("NO")) {
							String cname = cf.getFieldName();
							// 处理文本，去掉html标记
							String extractorText = processText(pMap.get(cname),
									"null");
							r.addProperty(cname, extractorText);
						}
					}
					session.save(r);
				}else{
					Long indexId=new Long(pMap.get("indexId").toString());
					
					Resource r = session.getResource(pName, indexId);
					if (r != null) {
						// 执行索引删除操作
						System.out.println("执行索引删除操作 indexId=" + indexId);
						session.delete(r);
					}
				}

			}
			tr.commit();
			//
			long tookTime = System.currentTimeMillis() - startIndexTime;
			System.out.println("index data[" + size + "]条，耗时=" + tookTime
					+ "ms");
			return null;
		}
	}
	
	public boolean isIndexed(Long nodeId,Long indexId) {
		Node node=NodeCache.getNode(nodeId);
		if(node!=null){
			Long tid=node.getTableId();
			ContentTable ct = contentTableManager.getContentTableFromCache(tid);
			String pName1 = ct.getEntityPublishName();
			if (pName1 == null || pName1.equals("")) {
				pName1 = "Publish_" + tid;
			}
			Resource r = this.getCompassTemplate().getResource(pName1, indexId);
			if(r!=null){
				return true;
			}
		}
		return false;
	}
}
