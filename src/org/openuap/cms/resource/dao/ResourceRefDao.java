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
package org.openuap.cms.resource.dao;

import java.util.List;

import org.openuap.cms.resource.model.ResourceRef;

/**
 * <p>
 * 资源引用DAO接口.
 * </p>
 *
 * 
 * <p>
 * $Id: ResourceRefDao.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ResourceRefDao {
	
	public void addResourceRef(ResourceRef resourceRef);

	public void saveResourceRef(ResourceRef resourceRef);

	public ResourceRef getResourceRefById(Long nodeId, Long indexId,
			Long resourceId);

	public List getResourceRefByNodeIndexId(Long nodeId, Long indexId);

	public void deleteResourceRefByNodeIndexId(Long nodeId, Long indexId);
}
