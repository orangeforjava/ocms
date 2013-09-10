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
package org.openuap.cms.engine.task;

import java.util.ArrayList;
import java.util.List;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.schedule.JobEntry;
import org.openuap.cms.schedule.ScheduledJob;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 结点发布任务.
 * </p>
 * 
 * <p>
 * $Id: PublishNodeTask.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishNodeTask extends ScheduledJob {

	public static final String CONTAIN_CHILD_NODE = "containChildNode";

	/** 是否包含附加发布页面. */
	public static final String CONTAIN_EXTRA_PUBLISH = "containExtraPublish";

	/** 是否包含内容页. */
	public static final String CONTAIN_CONTENT = "containContent";

	/** 是否包含首页. */
	public static final String CONTAIN_INDEX = "containIndex";

	/** 每次处理内容页面数量. */
	public static final String PROCESS_CONTENT_NUMS = "processContentNums";

	/** 是否在发生错误时终止. */
	public static final String TERMINATE_ON_ERROR = "terminateOnError";
	
	public  static final String REFRESH_FILE = "refreshfile";
	/** 发布模式变量. */
	public static final String MODE = "mode";

	public static final String NODE_ID = "nodeId";

	/** 发布模式常量. */
	public static final int PUBLISH_MODE = 0;

	/** 更新模式常量. */
	public static final int REFRESH_MODE = 1;

	/** 取消发布模式. */
	public static final int UNPUBLISH_MODE = 2;

	/** 重新发布模式. */
	public static final int REPUBLISH_MODE = 3;

	public PublishNodeTask() {
	}

	public void run(JobEntry job) throws Exception {
		//
		Integer mode = (Integer) job.getProperty().get(MODE);
		String nodeId = (String) job.getProperty().get(NODE_ID);
		String processContentNums = (String) job.getProperty().get(PROCESS_CONTENT_NUMS);
		String containChildNode = (String) job.getProperty().get(CONTAIN_CHILD_NODE);
		String containExtraPublish = (String) job.getProperty().get(CONTAIN_EXTRA_PUBLISH);
		String containContent = (String) job.getProperty().get(CONTAIN_CONTENT);
		String containIndex = (String) job.getProperty().get(CONTAIN_INDEX);
		String refreshContent=(String) job.getProperty().get(REFRESH_FILE);
		// 设置缺省值
		if (processContentNums == null) {
			processContentNums = "10";
		}
		if (containChildNode == null) {
			containChildNode = "true";
		}
		if (containExtraPublish == null) {
			containExtraPublish = "true";
		}
		if (containContent == null) {
			containContent = "true";
		}
		if (containIndex == null) {
			containIndex = "true";
		}
		if(refreshContent==null||refreshContent.equals("")){
			refreshContent="true";
		}
		if (mode != null && nodeId != null) {
			// 获得发布引擎
			PublishEngine publishEngine = (PublishEngine) ObjectLocator.lookup("publishEngine",CmsPlugin.PLUGIN_ID);
			PublishEngineMode publishMode = new PublishEngineMode();
			publishMode.setMode(mode.intValue());
			publishMode.setContainChildNode(new Boolean(containChildNode).booleanValue());
			publishMode.setContainContent(new Boolean(containContent).booleanValue());
			publishMode.setContainExtraPublish(new Boolean(containExtraPublish).booleanValue());
			publishMode.setContainIndex(new Boolean(containIndex).booleanValue());
			publishMode.setProcessContentNums(new Integer(processContentNums).intValue());
			publishMode.setRefreshContent(new Boolean(refreshContent).booleanValue());
			List errors = new ArrayList();
			if (publishMode.getMode() == PublishEngineMode.REFRESH_MODE) {
				// 刷新模式
				publishEngine.refreshAllNodeContent(new Long(nodeId), publishMode, errors);
			} else if (publishMode.getMode() == PublishEngineMode.PUBLISH_MODE) {
				// 发布模式
				publishEngine.publishAllNodeContent(new Long(nodeId), publishMode, errors);
			} else if (publishMode.getMode() == PublishEngineMode.UNPUBLISH_MODE) {
				// 取消发布模式
				publishEngine.unpublishAllNodeContent(new Long(nodeId), publishMode, errors);
			} else if (publishMode.getMode() == PublishEngineMode.REPUBLISH_MODE) {
				// 重新发布模式
				publishEngine.republishAllNodeContent(new Long(nodeId), publishMode, errors);
			}
		}
	}
}
