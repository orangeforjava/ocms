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
package org.openuap.cms.comment.event;

import java.util.Map;

import org.openuap.cms.comment.manager.CommentManager;
import org.openuap.cms.core.event.PublishEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * CMS内容发布事件监听器
 * </p>
 * 
 * <p>
 * $Id: ContentPublishListener.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentPublishListener implements ApplicationListener {
	private CommentManager commentManager;

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof PublishEvent) {
			PublishEvent pEvent = (PublishEvent) event;
			Map publish = pEvent.getPublish();
			int eventType = pEvent.getEventType();
			Long indexId = (Long) publish.get("indexId");
			if (eventType == PublishEvent.CONTENT_PUBLISHED) {
				// 发布事件
				commentManager.addCommentThread(indexId);

			} else if (eventType == PublishEvent.CONTENT_UNPUBLISHED) {
				// 取消发布事件

			}
		}

	}

	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

}
