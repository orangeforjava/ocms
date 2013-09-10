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

import java.util.List;

import org.openuap.cms.schedule.manager.ScheduleManager;
import org.openuap.runtime.log.Log;
import org.openuap.runtime.setup.BootstrapUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * Title: ScheduleService
 * </p>
 * 
 * <p>
 * Description:计划任务服务.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:http://www.openuap.org
 * </p>
 * $Id: ScheduleService.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class ScheduleService implements InitializingBean, DisposableBean {
	//
	private ScheduleManager scheduleManager;

	protected class MainLoop implements Runnable {

		public void run() {
			if (ScheduleService.log.isDebugEnabled()) {
				ScheduleService.log.debug("Schedule Main Loop Running");
			}
			try {
				do {
					JobEntry je = nextJob();
					if (je == null) {
						break;
					}
					WorkerThread wt = new WorkerThread(je);
					Thread helper = new Thread(wt);
					helper.start();
				} while (true);
			} catch (Exception e) {
				ScheduleService.log.fatal("Error in Scheduled Job Main Loop: ", e);
			} finally {
				clearThread();
			}
		}

		MainLoop() {
		}
	}

	public static Log log = new Log("sys.schedule");

	// public static final String host = CMSConfig.getInstance()
	// .getStringProperty("sys.host.id", "default");
	public static final String host = BootstrapUtils.getBootstrapManager("base").getApplicationConfig().getString(
			"sys.host.id", "default");
	// public static final String host = "default";

	private static ScheduleService service = null;

	protected JobQueue scheduleQueue;

	protected MainLoop mainLoop;

	protected boolean isInitialized;

	protected Thread thread;

	protected void setInit(boolean value) {
		isInitialized = value;
	}

	public void shutdown() {
		setInit(false);
		if (getThread() != null) {
			getThread().interrupt();
			thread = null;
		}
		log.info("Scheduled Job Engine successly shutdown!");
	}

	public synchronized Thread getThread() {
		return thread;
	}

	public static synchronized ScheduleService getInstance() {
		if (service == null) {
			log.info("Get Schedule Service Instance");
			service = new ScheduleService();
		}
		return service;
	}

	public boolean getInit() {
		return isInitialized;
	}

	public void destroy() {
		shutdown();
		clearThread();
	}
	/**
	 * 初始化计划任务引擎
	 * @throws Exception
	 */
	public void init() throws Exception {
		log.info("Scheduled Job Service begin initialize");
		if (getInit()) {
			return;
		}
		try {
			scheduleQueue = new JobQueue();
			mainLoop = new MainLoop();
			//获得所有任务项
			List jobs = scheduleManager.getAllJobEntries(host);
			if (jobs != null && jobs.size() > 0) {
				scheduleQueue.batchLoad(jobs);
				restart();
			}
			setInit(true);
			log.info("Scheduled Job Service success initialize");
		} catch (Exception e) {
			log.fatal("Scheduled Job Engine failed to initialize", e);
		}
	}

	private synchronized void clearThread() {
		thread = null;
	}

	public synchronized void restart() {
		if (thread == null) {
			thread = new Thread(mainLoop, "SchedulerService");
			thread.setDaemon(true);
			thread.start();
		} else {
			notify();
		}
	}

	private synchronized JobEntry nextJob() throws Exception {
		try {
			while (!Thread.interrupted()) {
				JobEntry je = scheduleQueue.getNext();
				if (je == null) {
					wait();
				} else {
					long now = System.currentTimeMillis();
					long when = je.getNextRuntime();
					if (when > now) {
						wait(when - now);
					} else {
						scheduleQueue.updateQueue(je);
						return je;
					}
				}
			}
		} catch (InterruptedException interruptedexception) {
		}
		return null;
	}

	public void updateJob(JobEntry je) throws Exception {
		try {
			je.calcRunTime();
			je.save();
		} catch (Exception e) {
			log.error("Problem updating Scheduled Job: " + e);
		}
		scheduleQueue.modify(je);
		restart();
	}

	public JobQueue getScheduleQueue() {
		return scheduleQueue;
	}

	private ScheduleService() {
		scheduleQueue = null;
		isInitialized = false;
	}

	public void setScheduleManager(ScheduleManager scheduleManager) {
		this.scheduleManager = scheduleManager;
	}

	public void afterPropertiesSet() throws Exception {
		init();
	}
}
