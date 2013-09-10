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
package org.openuap.cms.tpltag.manager.impl;

import java.util.List;

import org.openuap.cms.tpltag.dao.TemplateTagDao;
import org.openuap.cms.tpltag.manager.TemplateTagManager;
import org.openuap.cms.tpltag.model.TemplateTag;

/**
 * <p>
 * 模板DAO 管理者实现
 * </p>
 * 
 * <p>
 * $Id: TemplateTagManagerImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateTagManagerImpl implements TemplateTagManager {

	private TemplateTagDao dao;

	public Long addTag(TemplateTag tag) {
		return dao.addTag(tag);
	}

	public void deleteTag(Long id) {
		this.dao.deleteTag(id);
	}

	public TemplateTag getTagById(Long id) {
		return this.dao.getTagById(id);
	}

	public List<TemplateTag> getTagsByNode(Long nodeId) {
		return this.dao.getTagsByNode(nodeId);
	}

	public void saveTag(TemplateTag tag) {
		this.dao.saveTag(tag);
	}

	public void setDao(TemplateTagDao dao) {
		this.dao = dao;
	}

	public List<TemplateTag> getTagsByModel(Long modelId) {
		return this.dao.getTagsByModel(modelId);
	}

	public TemplateTag getTagByName(String name) {
		return this.dao.getTagByName(name);
	}

	public List<TemplateTag> getAllTags() {
		return this.dao.getAllTags();
	}

}
