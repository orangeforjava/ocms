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
package org.openuap.cms.core.event;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.openuap.event.Event;

/**
 * <p>
 * 内容发布事件.
 * </p>
 * 
 * 
 * <p>
 * $Id: PublishEvent.java 4025 2011-03-22 14:57:57Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishEvent extends Event {

	public static final int CONTENT_PUBLISHED = 10000;

	public static final int CONTENT_UNPUBLISHED = 10001;
	
	public static final int CONTENT_UPDATED = 10002;
	
	public static final int CONTENT_FINISH_PUBLISH=10003;
	
	public static final int CONTENT_ADDED=10004;
	
	public static final int CONTENT_DELETE=10005;

	private int eventType;

	private Map params;

	private Map publish;

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
	public PublishEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 * @param eventType
	 * @param publish
	 * @param params
	 * @param source
	 */
	public PublishEvent(int eventType, Map publish, Map params, Object source) {
		super(source);
		this.eventType = -1;
		this.eventType = eventType;
		this.publish = publish;
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
	public int getEventType() {
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(int eventType) {
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

	/**
	 * @return the publish
	 */
	public Map getPublish() {
		return publish;
	}

	/**
	 * @param publish
	 *            the publish to set
	 */
	public void setPublish(Map publish) {
		this.publish = publish;
	}

}
