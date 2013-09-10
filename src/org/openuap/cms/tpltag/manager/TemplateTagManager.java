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
package org.openuap.cms.tpltag.manager;

import java.util.List;

import org.openuap.cms.tpltag.model.TemplateTag;

/**
 * <p>
 * 模板DAO 管理者定义
 * </p>
 * 
 * <p>
 * $Id: TemplateTagManager.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface TemplateTagManager {
	
	public Long addTag(TemplateTag tag);

	public void saveTag(TemplateTag tag);

	public void deleteTag(Long id);

	public TemplateTag getTagById(Long id);

	public List<TemplateTag> getTagsByNode(Long nodeId);
	
	public List<TemplateTag> getTagsByModel(Long modelId);
	
	public TemplateTag getTagByName(String name);
	
	public List<TemplateTag> getAllTags();
}
