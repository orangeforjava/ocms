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
package org.openuap.cms.publish.dao;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.publish.model.ExtraPublish;

/**
 * <p>
 * 附加发布DAO接口定义.
 * </p>
 * 
 * <p>
 * $Id: ExtraPublishDao.java 3966 2010-12-16 12:10:02Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ExtraPublishDao {

	public Long addPublish(ExtraPublish publish);

	public void savePublish(ExtraPublish publish);

	public void deletePublish(Long publishId);

	public ExtraPublish getPublishById(Long publishId);

	public List getPublishes(Long nodeId);

	/**
	 * 根据GUID获得附加发布对象
	 * 
	 * @param guid
	 *            String
	 * @return ExtraPublish
	 */
	public ExtraPublish getPublishByGuid(String guid);
	
	public List getPublishes(QueryInfo qi, PageBuilder pb);
	
	public List<ExtraPublish> getNodeAutoRefreshPublish(Long nodeId,int mode);
	
	public List<ExtraPublish> getGlobalAutoRefreshPublish();

}
