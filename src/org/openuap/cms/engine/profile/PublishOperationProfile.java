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
package org.openuap.cms.engine.profile;

/**
 * <p>
 * 发布操作侦测器.
 * </p>
 * 
 * <p>
 * $Id: PublishOperationProfile.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface PublishOperationProfile {

	public String getTemplateContent();

	public void setTemplateContent(String tpl);

	public int getRecords();

	public void setRecords(int records);

	public void setOperation(String op);

	public String getOperation();

	public void setStartTime(long startTime);

	public long getStartTime();

	public void setEndTime(long endTime);

	public long getEndTime();

	public long getProccessTimes();

	public void setException(Exception ex);

	public Exception getException();
}
