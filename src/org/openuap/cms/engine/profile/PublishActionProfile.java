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
package org.openuap.cms.engine.profile;

import java.util.List;

/**
 * <p>
 * 动作请求处理分析接口定义
 * </p>
 * 
 * <p>
 * $Id: PublishActionProfile.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface PublishActionProfile {

	public void setStartTime(long time);

	public long getStartTime();

	public void setActionName(String actionName);

	public String getActionName();

	public void setUserId(int id);

	public int getUserId();

	public void setUsername(String username);

	public String getUsername();

	public void setStartActionTime(long time);

	public long getStartActionTime();

	public void setStartRenderTime(long time);

	public long getStartRenderTime();

	public void setStartResponseTime(long time);

	public long getStartResponseTime();
	
	public void addUnCatchedException(Exception ex);
	
	public Exception getUnCatchedExcetion();

	public void addPublishOperation(PublishOperationProfile dbOperation);
	
	public List getAllPublishOperations();
	
	
	public void setActionURI(String actionURI);
	
	public String getActionURI();
	/**
	 * 设置控制器逻辑处理阶段的异常
	 * @param ex
	 */
	public void setActionException(Exception ex);
	/**
	 * 获得控制器逻辑处理阶段的异常
	 * @return
	 */
	public Exception getActionException();
	/**
	 * 设置模板渲染的异常
	 * @param ex
	 */
	public void setRenderException(Exception ex);
	/**
	 * 获得渲染过程中的异常
	 * @return
	 */
	public Exception getRenderException();
	
	public long getLocatorActionTimes();
	
	public long getProcessActionTimes();
	
	public long getRenderTimes();

	
}
