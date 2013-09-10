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
package org.openuap.cms.schedule;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.openuap.base.util.StringUtil;
import org.openuap.cms.schedule.manager.ScheduleManager;
import org.openuap.cms.schedule.model.Schedule;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * Title: JobEntry
 * </p>
 * 
 * <p>
 * Description:工作任务实体.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:http://www.openuap.org
 * </p>
 * $Id: JobEntry.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class JobEntry {

	private static final int SECOND = 0;

	private static final int MINUTE = 1;

	private static final int WEEK_DAY = 2;

	private static final int DAY_OF_MONTH = 3;

	private static final int DAILY = 4;

	private int second;

	private int minute;

	private int hour;

	private int weekday;

	private int day_of_month;

	private String task;

	private long runtime;

	private String email;
	/** 任务是否被激活. */
	private boolean jobIsActive;

	private boolean modified;
	/** 任务属性集. */
	private Hashtable jobProp;

	/**
	 * 计算运行时间
	 * 
	 * @throws Exception
	 */
	public void calcRunTime() throws Exception {
		Calendar schedrun = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		switch (evaluateJobType()) {
		default:
			break;

		case SECOND:
			schedrun.add(Calendar.SECOND, second);
			runtime = schedrun.getTime().getTime();
			break;

		case MINUTE:
			schedrun.add(Calendar.SECOND, second);
			schedrun.add(Calendar.MINUTE, minute);
			runtime = schedrun.getTime().getTime();
			break;

		case WEEK_DAY:
			schedrun.add(Calendar.SECOND, second);
			schedrun.set(Calendar.MINUTE, minute);
			schedrun.set(Calendar.HOUR, hour);
			schedrun.set(Calendar.DAY_OF_WEEK, weekday);
			if (now.before(schedrun)) {
				// 现在在运行日之前
				runtime = schedrun.getTime().getTime();
			} else {
				// 在运行日之后，放到下一周处理
				schedrun.add(Calendar.DAY_OF_WEEK, 7);
				runtime = schedrun.getTime().getTime();
			}
			break;

		case DAY_OF_MONTH:
			schedrun.add(Calendar.SECOND, second);
			schedrun.set(Calendar.MINUTE, minute);
			schedrun.set(Calendar.HOUR, hour);
			schedrun.set(Calendar.DAY_OF_MONTH, day_of_month);
			if (now.before(schedrun)) {
				runtime = schedrun.getTime().getTime();
			} else {
				schedrun.add(Calendar.MONTH, 1);
				runtime = schedrun.getTime().getTime();
			}
			break;

		case DAILY:
			schedrun.add(Calendar.SECOND, second);
			schedrun.set(Calendar.MINUTE, minute);
			schedrun.set(Calendar.HOUR, hour);
			if (now.before(schedrun)) {
				runtime = schedrun.getTime().getTime();
			} else {
				schedrun.add(Calendar.HOUR_OF_DAY, 24);
				runtime = schedrun.getTime().getTime();
			}
			break;
		}
	}

	public long getNextRuntime() {
		return runtime;
	}

	/**
	 * 得到下次运行时间的字符串表示
	 * 
	 * @return
	 */
	public String getNextRunAsString() {
		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG);
		return formatter.format(new Date(runtime));
	}

	public Date getNextRunDate() {
		return new Date(runtime);
	}

	public void setEmail(String mail) {
		email = mail;
		setModified(true);
	}

	public String getEmail() {
		if (email == null || email.length() == 0) {
			return "not set";
		} else {
			return email;
		}
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
		setModified(true);
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int v) {
		second = v;
		setModified(true);
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int v) {
		minute = v;
		setModified(true);
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int v) {
		hour = v;
		setModified(true);
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int v) {
		weekday = v;
		setModified(true);
	}

	public int getDay_of_month() {
		return day_of_month;
	}

	public void setDay_of_month(int v) {
		day_of_month = v;
		setModified(true);
	}

	/**
	 * 保存计划
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		Schedule schedule = new Schedule();
		schedule.setSecond(getSecond());
		schedule.setMinute(getMinute());
		schedule.setHour(getHour());
		schedule.setDayOfMonth(getDay_of_month());
		schedule.setWeekDay(getWeekday());
		schedule.setTask(getTask());
		schedule.setEmail(getEmail());
		schedule.setProperty(StringUtil.hash2str(getProperty()));
		//
		getScheduleManager().addSchedule(schedule);
	}

	public ScheduleManager getScheduleManager() {
		ScheduleManager scheduleManager = (ScheduleManager) ObjectLocator
				.lookup("scheduleManager", SchedulePlugin.PLUGIN_ID);
		return scheduleManager;
	}

	/**
	 * 计算任务类型
	 * 
	 * @return
	 * @throws Exception
	 */
	private int evaluateJobType() throws Exception {
		if (day_of_month < 0) {
			if (weekday < 0) {
				if (hour < 0) {
					if (minute < 0) {
						if (second < 0) {
							throw new Exception(
									"Error in JobEntry. Bad Job parameter.");
						} else {
							return SECOND;
						}
					}
					if (minute < 0 || second < 0) {
						throw new Exception(
								"Error in JobEntry. Bad Job parameter.");
					} else {
						return MINUTE;
					}
				}
				if (minute < 0 || hour < 0 || second < 0) {
					throw new Exception("Error in JobEntry. Bad Job parameter.");
				} else {
					return DAILY;
				}
			}
			if (minute < 0 || hour < 0 || second < 0) {
				throw new Exception("Error in JobEntry. Bad Job parameter.");
			} else {
				return WEEK_DAY;
			}
		}
		if (minute < 0 || hour < 0) {
			throw new Exception("Error in JobEntry. Bad Job parameter.");
		} else {
			return DAY_OF_MONTH;
		}
	}

	public void setActive(boolean isActive) {
		jobIsActive = isActive;
	}

	public boolean isActive() {
		return jobIsActive;
	}

	public void setProperty(Hashtable prop) {
		jobProp = prop;
		setModified(true);
	}

	public Hashtable getProperty() {
		if (jobProp == null) {
			return new Hashtable(89);
		} else {
			return jobProp;
		}
	}

	public void setModified(boolean m) {
		modified = m;
	}

	public boolean isModified() {
		return modified;
	}

	/**
	 * 
	 * @param sec
	 *            　秒数
	 * @param min
	 *            　分数
	 * @param hour
	 *            　小时数
	 * @param wd
	 *            　周几
	 * @param day_mo
	 *            　几号
	 * @param task
	 *            　任务
	 * @throws Exception
	 */
	public JobEntry(int sec, int min, int hour, int wd, int day_mo, String task)
			throws Exception {
		second = -1;
		minute = -1;
		this.hour = -1;
		weekday = -1;
		day_of_month = -1;
		this.task = null;
		runtime = 0L;
		email = "";
		jobIsActive = false;
		modified = false;
		jobProp = null;
		if (task == null || task.length() == 0) {
			throw new Exception(
					"Error in JobEntry. Bad Job parameter. Task not set.");
		} else {
			second = sec;
			minute = min;
			this.hour = hour;
			weekday = wd;
			day_of_month = day_mo;
			this.task = task;
			calcRunTime();
			return;
		}
	}
}
