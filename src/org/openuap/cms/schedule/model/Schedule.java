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
package org.openuap.cms.schedule.model;

import java.io.Serializable;

import org.openuap.base.dao.hibernate.BaseObject;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.schedule.JobEntry;

/**
 * <p>
 * Title:Schedule
 * </p>
 * 
 * <p>
 * Description: 计划任务对象.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * $Id: Schedule.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @preserve private
 * @author Weiping Ju
 * @version 1.0
 */
public class Schedule extends BaseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9048279499678371438L;

	//
	private int hashValue = 0;

	//
	private int id;

	private int second;

	private int minute;

	private int hour;

	private int weekDay;

	private int dayOfMonth;

	private String task;

	private String email;

	private String property;
	/** 主机信息，用来处理分布式计划任务. */
	private String host;
	/** 任务状态，用来控制任务是否可用. */
	private int status;

	public Schedule() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public JobEntry toJobEntry() {
		try {
			int sec = second;
			int min = minute;
			int hr = hour;
			int wd = weekDay;
			int d_m = dayOfMonth;
			String task = getTask();
			JobEntry je = new JobEntry(sec, min, hr, wd, d_m, task);
			je.setEmail(getEmail());
			je.setProperty(StringUtil.str2hash(getProperty()));
			je.setModified(false);
			return je;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof Schedule)) {
			return false;
		}
		Schedule that = (Schedule) rhs;
		if (this.getId() == 0 || that.getId() == 0) {
			return false;
		}
		return (this.getId() == that.getId());
	}

	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int idValue = this.getId() == 0 ? 0 : new Integer(this.getId()).hashCode();
			result = result * 37 + idValue;
			this.hashValue = result;
		}
		return this.hashValue;

	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
