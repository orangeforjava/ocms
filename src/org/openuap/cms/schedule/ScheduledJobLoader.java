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

import java.util.Hashtable;

/**
 * <p>
 * Title: ScheduledJobLoader
 * </p>
 * 
 * <p>
 * Description:任务装载.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:<a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: ScheduledJobLoader.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class ScheduledJobLoader extends Hashtable {

	// private static ScheduledJobLoader instance = new ScheduledJobLoader(
	// CMSConfig.getInstance().getIntegerProperty(
	// "sys.schedule.cache_size", 10));
	private static ScheduledJobLoader instance = new ScheduledJobLoader(10);
	private boolean reload;

	private boolean CACHE;

	private void addInstance(String name, ScheduledJob job) {
		if (cache()) {
			put(name, job);
		}
	}

	public boolean cache() {
		return CACHE;
	}

	public void exec(JobEntry job, String name) throws Exception {
		getInstance(name).execute(job);
	}

	public ScheduledJob getInstance(String name) throws Exception {
		ScheduledJob job = null;
		if (cache() && containsKey(name)) {
			job = (ScheduledJob) get(name);
		} else {
			String classname = null;
			try {
				classname = name;
				ScheduleService.log.debug("Attempt load task:" + classname);
				Class servClass = Class.forName(classname);
				job = (ScheduledJob) servClass.newInstance();
			} catch (Exception ex) {
				
				job = null;
			}
			if (job == null) {
				throw new ClassNotFoundException(
						"Requested ScheduledJob not found:" + classname);
			}
			if (!(job instanceof SingleTaskMode) && cache()) {
				addInstance(name, job);
			}
		}
		return job;
	}

	public static ScheduledJobLoader getInstance() {
		return instance;
	}

	private ScheduledJobLoader() {
		reload = false;
		CACHE = true;
	}

	private ScheduledJobLoader(int i) {
		super(i);
		reload = false;
		CACHE = true;
	}

}
