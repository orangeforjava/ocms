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
package org.openuap.cms.search.index.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openuap.cms.core.event.PublishEvent;
import org.openuap.cms.repo.event.ContentIndexEvent;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.search.index.IndexEngine;
import org.openuap.cms.search.index.IndexParameter;
import org.openuap.cms.search.index.dao.IndexDao;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * 缺省索引引擎实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultIndexEngineImpl.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultIndexEngineImpl implements IndexEngine, ApplicationListener {

	private IndexDao indexDao;

	/**
	 * TODO 未进行错误处理以及增量支持
	 */
	public void addIndexContent(IndexParameter parameter, List errors) {
		//
		int mode = parameter.getMode();
		if (mode == parameter.SINGLE_MODE) {
			// 非按照模型模式
			String indexIds = parameter.getIndexIds();
			if (indexIds != null && !indexIds.equals("")) {
				String[] ids = indexIds.split(",");
				int size = ids.length;
				for (int i = 0; i < size; i++) {
					String id = ids[i];
					Long iid = new Long(id);
					indexDao.indexContent(iid);
				}
			}
		} else {
			//按照内容表方式
			String modelIds = parameter.getModelIds();
			if (modelIds != null && !modelIds.equals("")) {
				String[] mids = modelIds.split(",");
				int size = mids.length;
				for (int i = 0; i < size; i++) {
					String mid = mids[i];
					Long tid = new Long(mid);
					indexDao.indexContentByTable(tid);
				}
			}
		}
	}

	public void deleteIndexContent(IndexParameter parameter, List errors) {
		int mode = parameter.getMode();
		if (mode == parameter.SINGLE_MODE) {
			String indexIds = parameter.getIndexIds();
			if (indexIds != null && !indexIds.equals("")) {
				String[] ids = indexIds.split(",");
				int size = ids.length;
				for (int i = 0; i < size; i++) {
					String id = ids[i];
					Long iid = new Long(id);
					indexDao.deleteIndexContent(iid);
				}
			}
		}
	}

	public void setIndexDao(IndexDao indexDao) {
		this.indexDao = indexDao;
	}

	/**
	 * 处理事件
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof PublishEvent) {
			PublishEvent pEvent = (PublishEvent) event;
			Map publish = pEvent.getPublish();
			int eventType = pEvent.getEventType();
			Long indexId = (Long) publish.get("indexId");
			if (eventType == PublishEvent.CONTENT_PUBLISHED) {
				// 发布事件
				//System.out.println("-------------------------");
				//System.out.println("PublishEvent indexId="+indexId);
				//System.out.println("-------------------------");
				IndexParameter p = new IndexParameter();
				p.setMode(IndexParameter.SINGLE_MODE);
				p.setIndexIds(indexId.toString());
				List errors = new ArrayList();
				this.addIndexContent(p, errors);
			} else if (eventType == PublishEvent.CONTENT_UNPUBLISHED) {
				// 取消发布事件
				IndexParameter p = new IndexParameter();
				p.setMode(IndexParameter.SINGLE_MODE);
				p.setIndexIds(indexId.toString());
				List errors = new ArrayList();
				this.deleteIndexContent(p, errors);
			}
		}
		if (event instanceof ContentIndexEvent) {
			ContentIndexEvent ciEvent = (ContentIndexEvent) event;
			String eventType = ciEvent.getEventType();
			ContentIndex ci = ciEvent.getContentIndex();
			Long cid = ci.getIndexId();
			//System.out.println("ci top="+ci.getTop());
			if (eventType.equals(ContentIndexEvent.CONTENT_ADDED)
					|| eventType.equals(ContentIndexEvent.CONTENT_UPDATED)) {

				this.indexDao.indexContent(cid);
			} else if (eventType.equals(ContentIndexEvent.CONTENT_DELETE)) {
				this.indexDao.deleteIndexContent(cid);
			}
		}
	}

}
