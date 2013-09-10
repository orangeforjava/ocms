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
package org.openuap.cms.schedule.manager.impl;

import java.util.List;

import org.openuap.cms.schedule.dao.ScheduleDao;
import org.openuap.cms.schedule.manager.ScheduleManager;
import org.openuap.cms.schedule.model.Schedule;

/**
 * <p>
 * Title: ScheduleManagerDBImpl
 * </p>
 * 
 * <p>
 * Description: 计划任务管理数据库实现.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * $Id: ScheduleManagerDBImpl.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class ScheduleManagerDBImpl implements ScheduleManager {

	private ScheduleDao scheduleDao;

	public ScheduleManagerDBImpl() {
	}

	public int addSchedule(Schedule schedule) {
		return scheduleDao.addSchedule(schedule);
	}

	public void saveSchedule(Schedule schedule) {
		scheduleDao.saveSchedule(schedule);
	}

	public void deleteSchedule(Schedule schedule) {
		scheduleDao.deleteSchedule(schedule);
	}

	public void deleteScheduleById(int id) {
		scheduleDao.deleteScheduleById(id);
	}

	public List findAll() {
		return scheduleDao.findAll();
	}

	public Schedule findById(int id) {
		return scheduleDao.findById(id);
	}

	public List getAllTasks() {
		return scheduleDao.getAllTasks();
	}

	public List getTasksByHost(String hostid) {
		return scheduleDao.getTasksByHost(hostid);
	}

	public List getAllJobEntries(String hostid) {
		return scheduleDao.getAllJobEntries(hostid);
	}

	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}
}
