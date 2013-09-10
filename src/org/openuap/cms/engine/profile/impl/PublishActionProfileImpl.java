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
package org.openuap.cms.engine.profile.impl;

import java.util.ArrayList;
import java.util.List;

import org.openuap.cms.engine.profile.PublishActionProfile;
import org.openuap.cms.engine.profile.PublishOperationProfile;

/**
 * <p>
 * 缺省请求处理分析实现
 * </p>
 * 
 * <p>
 * $Id: PublishActionProfileImpl.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishActionProfileImpl implements PublishActionProfile {

	private List publishOperations = new ArrayList();

	private Exception unCatchedException;

	private String actionName;

	private long startActionTime;

	private long startRenderTime;

	private long startResponseTime;

	private long startTime;

	private int userId;

	private String username;

	private String actionURI;

	private Exception actionException;
	
	private Exception renderException;

	public void addPublishOperation(PublishOperationProfile pubOperation) {
		publishOperations.add(pubOperation);
	}

	public void addUnCatchedException(Exception ex) {
		unCatchedException = ex;
	}

	public String getActionName() {
		return actionName;
	}

	public List getAllPublishOperations() {
		return publishOperations;
	}

	public long getStartActionTime() {
		return startActionTime;
	}

	public long getStartRenderTime() {
		return startRenderTime;
	}

	public long getStartResponseTime() {
		return startResponseTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public Exception getUnCatchedExcetion() {
		return unCatchedException;
	}

	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;

	}

	public void setStartActionTime(long time) {
		this.startActionTime = time;
	}

	public void setStartRenderTime(long time) {
		this.startRenderTime = time;
	}

	public void setStartResponseTime(long time) {
		this.startResponseTime = time;

	}

	public void setStartTime(long time) {
		this.startTime = time;

	}

	public void setUserId(int id) {
		this.userId = id;

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getActionURI() {
		return actionURI;
	}

	public void setActionURI(String actionURI) {
		this.actionURI = actionURI;
	}

	public Exception getActionException() {		
		return actionException;
	}

	public void setActionException(Exception ex) {
		this.actionException=ex;		
	}

	public Exception getRenderException() {
		return this.renderException;
	}

	public void setRenderException(Exception ex) {
		this.renderException=ex;
		
	}

	public long getLocatorActionTimes() {
		
		return startActionTime-startTime;
	}

	public long getProcessActionTimes() {
		
		return startRenderTime-startActionTime;
	}

	public long getRenderTimes() {		
		return startResponseTime-startRenderTime;
	}

}
