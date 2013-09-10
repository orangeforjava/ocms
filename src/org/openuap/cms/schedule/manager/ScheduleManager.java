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
package org.openuap.cms.schedule.manager;

import java.util.List;

import org.openuap.cms.schedule.model.Schedule;

/**
 * <p>
 * Title:ScheduleManager
 * </p>
 * 
 * <p>
 * Description:计划任务管理器.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * $Id: ScheduleManager.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public interface ScheduleManager {

	public int addSchedule(Schedule schedule);

	public void saveSchedule(Schedule schedule);

	public void deleteSchedule(Schedule schedule);

	public void deleteScheduleById(int id);

	public List findAll();

	public Schedule findById(int id);

	public List getAllTasks();

	public List getTasksByHost(String hostid);

	public List getAllJobEntries(String hostid);

}
