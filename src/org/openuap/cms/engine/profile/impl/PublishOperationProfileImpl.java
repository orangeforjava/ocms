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

import org.openuap.cms.engine.profile.PublishOperationProfile;

/**
 * <p>
 * 缺省DB操作分析实现
 * </p>
 * 
 * <p>
 * $Id: PublishOperationProfileImpl.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishOperationProfileImpl implements PublishOperationProfile {

	private long endTime;

	private Exception exception;

	private long startTime;

	private int type;

	private String operation;
	
	private String templateContent;
	
	private int records;

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public long getEndTime() {
		return endTime;
	}

	public Exception getException() {
		return exception;
	}

	public long getStartTime() {
		return startTime;
	}

	public int getType() {
		return type;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void setException(Exception ex) {
		this.exception = ex;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getProccessTimes() {
		return endTime - startTime;
	}

}
