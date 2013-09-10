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

import java.util.Date;

import org.openuap.runtime.log.Log;

/**
 * <p>
 * Title: ExecuteThread
 * </p>
 * 
 * <p>
 * Description:执行线程.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * $Id: ExecuteThread.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class ExecuteThread implements Runnable {
	public static Log log = new Log("sys.schedule.executethread");
	private JobEntry je;
	private ScheduledJob scheduleJob;

	public ExecuteThread(JobEntry job) {
		this.je = job;
		scheduleJob = scheduleJob;
	}

	public void run() {
		if (scheduleJob == null || je == null || je.isActive()) {
			return;
		}
		try {
			if (!je.isActive()) {
				je.setActive(true);
				scheduleJob.execute(je);
			}
		} catch (Exception e) {
			log.error("Error in ExecuteThread for Task: " + je.getTask(), e);
		} finally {
			if (je.isActive()) {
				je.setActive(false);
				logJobEntryStateChange("completed");
			}
		}

	}

	private final void logJobEntryStateChange(String state) {
		if (log.isDebugEnabled()) {
			log.debug("Scheduled job:" + state + " task " + je.getTask()
					+ " on " + new Date());
		}
	}

}
