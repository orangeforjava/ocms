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
package org.openuap.cms.cm.event;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.openuap.cms.cm.model.ContentTable;
import org.openuap.event.Event;

/**
 * 内容模型事件
 * $Id: ContentModelEvent.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * @author Joseph
 * 
 */
public class ContentModelEvent extends Event {
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1999296433242395062L;

	public static final String CM_CREATED = "org.openuap.cms.cm.created";

	public static final String CM_UPDATED = "org.openuap.cms.cm.updated";

	public static final String CM_DELETED = "org.openuap.cms.cm.deleted";

	private String eventType;

	private Map params;

	private ContentTable contentTable;

	private Date created;

	private Exception failureException;

	public ContentModelEvent(Object source) {
		super(source);
	}

	public ContentModelEvent(String eventType, ContentTable contentTable,
			Map params, Object source) {
		super(source);
		this.eventType = eventType;
		this.contentTable = contentTable;
		this.params = params != null ? Collections.unmodifiableMap(params)
				: null;
		this.created = new Date();
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Exception getFailureException() {
		return failureException;
	}

	public void setFailureException(Exception failureException) {
		this.failureException = failureException;
	}

	public ContentTable getContentTable() {
		return contentTable;
	}

	public void setContentTable(ContentTable contentTable) {
		this.contentTable = contentTable;
	}
}
