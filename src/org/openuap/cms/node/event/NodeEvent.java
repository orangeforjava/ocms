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
package org.openuap.cms.node.event;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.openuap.cms.node.model.Node;
import org.openuap.event.Event;

/**
 * <p>
 * $Id: NodeEvent.java 4011 2011-01-17 16:39:25Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeEvent extends Event {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1999296433242395062L;

	public static final String NODE_CREATED = "org.openuap.cms.node.created";

	public static final String NODE_UPDATED = "org.openuap.cms.node.updated";

	public static final String NODE_DELETED = "org.openuap.cms.node.deleted";
	
	public static final String NODE_DESTROY = "org.openuap.cms.node.destroy";

	private String eventType;

	private Map params;

	private Node node;

	private Date created;

	private Exception failureException;

	public NodeEvent(Object source) {
		super(source);
	}

	public NodeEvent(String eventType, Node node, Map params, Object source) {
		super(source);
		this.eventType = eventType;
		this.node = node;
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

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
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
}
