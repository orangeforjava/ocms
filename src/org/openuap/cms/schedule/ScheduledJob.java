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

import org.openuap.runtime.log.Log;

/**
 * <p>
 * Title:ScheduledJob
 * </p>
 * 
 * <p>
 * Description:计划任务
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:<a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public abstract class ScheduledJob {

	public Log log;

	public abstract void run(JobEntry jobentry) throws Exception;

	public void execute(JobEntry job) throws Exception {
		run(job);
	}

	public ScheduledJob() {
		log = WorkerThread.log;
	}
}
