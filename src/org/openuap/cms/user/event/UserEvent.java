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

import org.openuap.cms.user.model.IUser;
import org.openuap.event.Event;

/**
 * <p>
 * 用户操作事件.
 * </p>
 * 
 * <p>
 * $Id: UserEvent.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserEvent extends Event {

	private static final long serialVersionUID = 4274237783950249274L;

	/** 用户产生事件. */
	public static final int USER_CREATED = 100;

	/** 用户删除事件. */
	public static final int USER_DELETED = 101;

	/** 用户修改事件. */
	public static final int USER_MODIFIED = 102;

	private int eventType;

	private Map params;

	private IUser user;

	private Date created;

	private Exception failureException;

	/**
	 * 
	 * @param source
	 *            
	 */
	public UserEvent(Object source) {
		super(source);
	}

	public UserEvent(int eventType, IUser user, Map params, Object source) {
		super(source);
		this.eventType = -1;
		this.eventType = eventType;
		this.user = user;
		this.params = params != null ? Collections.unmodifiableMap(params) : null;
		this.created = new Date();

	}

	public int getEventType() {
		return this.eventType;
	}

	public Map getParams() {
		return this.params;
	}

	public IUser getUser() {
		return user;
	}

	public Exception getFailureException() {
		return failureException;
	}

	public Date getDate() {
		return this.created;
	}

	public void setFailureException(Exception failureException) {
		this.failureException = failureException;
	}
}
