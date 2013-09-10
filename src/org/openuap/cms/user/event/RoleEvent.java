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
package org.openuap.cms.user.event;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.openuap.cms.user.model.IRole;
import org.openuap.event.Event;

/**
 * <p>
 * 角色操作事件.
 * </p>
 * 
 * <p>
 * $Id: RoleEvent.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class RoleEvent extends Event {

	private static final long serialVersionUID = -4345856261516188478L;

	public static final int ROLE_CREATED = 200;

	public static final int ROLE_DELETED = 201;

	public static final int ROLE_MODIFIED = 202;

	private int eventType;

	private Map params;

	private IRole role;

	private Date created;

	private Exception failureException;

	/**
	 * 
	 * @param source
	 *            Object
	 */
	public RoleEvent(Object source) {
		super(source);
	}

	public RoleEvent(int eventType, IRole role, Map params, Object source) {
		super(source);
		this.eventType = -1;
		this.eventType = eventType;
		this.role = role;
		this.params = params != null ? Collections.unmodifiableMap(params) : null;
		this.created = new Date();

	}

	public int getEventType() {
		return this.eventType;
	}

	public Map getParams() {
		return this.params;
	}

	public Exception getFailureException() {
		return failureException;
	}

	public IRole getRole() {
		return role;
	}

	public Date getDate() {
		return this.created;
	}

	public void setFailureException(Exception failureException) {
		this.failureException = failureException;
	}
}
