/*
 * Copyright 2005-2008 the original author or authors.
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
package org.openuap.cms.search.task;

import java.util.ArrayList;
import java.util.List;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.schedule.JobEntry;
import org.openuap.cms.schedule.ScheduledJob;
import org.openuap.cms.search.index.IndexEngine;
import org.openuap.cms.search.index.IndexParameter;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 构建索引任务.
 * </p>
 * 
 * 
 * <p>
 * $Id: BuildIndexTask.java 4012 2011-01-24 11:05:06Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class BuildIndexTask extends ScheduledJob{
	
	public static final String MODE = "mode";

	public static final String MODEL_ID = "modelIds";
	/**
	 * 
	 */
	public void run(JobEntry job) throws Exception {
		//
		String mode = (String) job.getProperty().get(MODE);
		String modelIds = (String) job.getProperty().get(MODEL_ID);
		IndexParameter indexParameter=new IndexParameter(Integer.parseInt(mode),modelIds);
		//
		IndexEngine indexEngine = (IndexEngine) ObjectLocator.lookup("indexEngine",CmsPlugin.PLUGIN_ID);
		List errors=new ArrayList();
		indexEngine.addIndexContent(indexParameter, errors);
	}

}
