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
package org.openuap.cms.schedule.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.schedule.dao.ScheduleDao;
import org.openuap.cms.schedule.model.Schedule;

/**
 * <p>
 * Title:ScheduleDaoImpl
 * </p>
 * 
 * <p>
 * Description:计划任务DAO实现.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * $Id: ScheduleDaoImpl.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class ScheduleDaoImpl extends BaseDaoHibernate implements ScheduleDao {

	public ScheduleDaoImpl() {
	}

	public int addSchedule(Schedule schedule) {
		return ((Integer) this.addObject(schedule)).intValue();
	}

	public void saveSchedule(Schedule schedule) {
		this.saveObject(schedule);
	}

	public void deleteSchedule(Schedule schedule) {
		this.deleteObject(schedule);
	}

	public void deleteScheduleById(int id) {
		Schedule schedule = this.findById(id);
		if (schedule != null) {
			this.deleteSchedule(schedule);
		}
	}

	public List findAll() {
		String hql = "from Schedule order by host,id";
		return executeFind(hql);
	}

	public Schedule findById(int id) {
		String hql = "from Schedule where id=" + id;
		return (Schedule) this.findUniqueResult(hql);
	}

	public List getAllTasks() {
		return findAll();
	}

	public List getTasksByHost(String hostid) {
		String hql = "from Schedule where host like '" + hostid + "'";
		return executeFind(hql);
	}

	public List getAllJobEntries(String hostid) {
		List items = getTasksByHost(hostid);
		List results = new ArrayList();
		for (int i = 0; i < items.size(); i++) {
			try {
				Schedule sche = (Schedule) items.get(i);
				if (sche.getStatus() == 1) {
					results.add(sche.toJobEntry());
					this.getLogger().info("Schedule service: Add scheduled job:" + sche.getTask());
				}
				} catch (Exception e) {
				this.getLogger().error(e);
			}
		}

		return results;

	}

}
