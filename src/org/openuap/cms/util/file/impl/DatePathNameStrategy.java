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
package org.openuap.cms.util.file.impl;

import java.util.Calendar;

import org.openuap.cms.util.file.PathNameStrategy;

/**
 * <p>
 * 日期名称路径名策略.
 * </p>
 * 
 * <p>
 * $Id: DatePathNameStrategy.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DatePathNameStrategy implements PathNameStrategy {
	
	private String rootDir;

	private String userName;

	public DatePathNameStrategy() {
	}

	public String getStrategyName() {
		return DatePathNameStrategy.class.getName();
	}

	public String getPathName() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int second = cal.get(Calendar.SECOND);
		int minute = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR);
		int ms = cal.get(Calendar.MILLISECOND);
		String path = year + "/" + (month + 1) + "/" + day;
		if (userName != null) {
			path = userName + "/" + path;
		}
		return path;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isEqualStrategy(PathNameStrategy strategy) {
		return getStrategyName().equals(strategy.getClass().getName());
	}

	public String getFileName(String type) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int second = cal.get(Calendar.SECOND);
		int minute = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR);
		int ms = cal.get(Calendar.MILLISECOND);

		return type + year + (month + 1) + day + hour + minute + second + ms;
	}

	public String getUserName() {
		return userName;
	}
}
