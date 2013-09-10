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
package org.openuap.cms.engine.resource;

import java.util.List;

import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.repo.model.ContentIndex;

/**
 * <p>
 * 资源发布引擎.
 * </p>
 * 
 * <p>
 * $Id: ResourcePublishEngine.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ResourcePublishEngine {

	public boolean publishContentResource(Long nodeId, Long indexId, List errors);
	
	public boolean publishContentResource(Node node, ContentIndex ci, List errors);

	public boolean refreshAllNodeResource(Long parentId,
			PublishEngineMode mode, List errors);
}
