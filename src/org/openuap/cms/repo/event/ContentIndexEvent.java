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
package org.openuap.cms.repo.event;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.event.Event;

/**
 * <p>
 * 内容索引事件
 * </p>
 * <p>
 * $Id: ContentIndexEvent.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ContentIndexEvent extends Event {

	public static final String CONTENT_DELETE = "org.openuap.cms.contentindex.deleted";

	public static final String CONTENT_ADDED ="org.openuap.cms.contentindex.added";
	
	public static final String CONTENT_UPDATED = "org.openuap.cms.contentindex.updated";

	private String eventType;

	private Map params;

	private ContentIndex contentIndex;

	private Date created;

	private Exception failureException;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6315004333816648241L;

	/**
	 * 
	 * @param source
	 */
	public ContentIndexEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 * @param eventType
	 * @param publish
	 * @param params
	 * @param source
	 */
	public ContentIndexEvent(String eventType, ContentIndex ci, Map params, Object source) {
		super(source);
		this.eventType = "";
		this.eventType = eventType;
		this.contentIndex = ci;
		this.params = params != null ? Collections.unmodifiableMap(params) : null;
		this.created = new Date();
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the failureException
	 */
	public Exception getFailureException() {
		return failureException;
	}

	/**
	 * @param failureException
	 *            the failureException to set
	 */
	public void setFailureException(Exception failureException) {
		this.failureException = failureException;
	}

	/**
	 * @return the params
	 */
	public Map getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map params) {
		this.params = params;
	}

	public ContentIndex getContentIndex() {
		return contentIndex;
	}

	public void setContentIndex(ContentIndex contentIndex) {
		this.contentIndex = contentIndex;
	}

	

}
