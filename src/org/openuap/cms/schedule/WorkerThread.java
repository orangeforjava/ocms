/**
 * 
 */
package org.openuap.cms.schedule;

import java.util.Date;

import org.openuap.runtime.log.Log;

/**
 * <p>
 * Title: WorkerThread
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:http://www.openuap.org
 * </p>
 * $Id: WorkerThread.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class WorkerThread implements Runnable {

	public static Log log = new Log("sys.schedule.workerthread");

	private JobEntry je;

	public void run() {
		if (je == null || je.isActive()) {
			return;
		}
		try {
			if (!je.isActive()) {
				je.setActive(true);
				logJobEntryStateChange("started");
				ScheduledJobLoader.getInstance().exec(je, je.getTask());
			}
		} catch (Exception e) {
			log.error("Error in WorkerThread for Task: " + je.getTask(), e);
		} finally {
			if (je.isActive()) {
				je.setActive(false);
				logJobEntryStateChange("completed");
			}
		}
	}

	private final void logJobEntryStateChange(String state) {
		System.out.println("Scheduled job:" + state + " task " + je.getTask()
				+ " on " + new Date());
		if (log.isDebugEnabled()) {
			
			log.debug("Scheduled job:" + state + " task " + je.getTask()
					+ " on " + new Date());
		}
	}

	public WorkerThread(JobEntry je) {
		this.je = null;
		this.je = je;
	}

}
