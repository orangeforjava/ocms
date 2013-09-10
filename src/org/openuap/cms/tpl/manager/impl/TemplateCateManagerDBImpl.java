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
package org.openuap.cms.tpl.manager.impl;

import java.util.List;

import org.openuap.cms.tpl.dao.TemplateCategoryDao;
import org.openuap.cms.tpl.manager.TemplateCateManager;
import org.openuap.cms.tpl.model.TemplateCategory;

/**
 * <p>
 * 模板分类管理实现.
 * </p>
 * 
 * <p>
 * $Id: TemplateCateManagerDBImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateCateManagerDBImpl implements TemplateCateManager {

	private TemplateCategoryDao templateCategoryDao;

	/**
	 * 
	 */
	public TemplateCateManagerDBImpl() {
	}

	public void setTemplateCategoryDao(TemplateCategoryDao templateCategoryDao) {
		this.templateCategoryDao = templateCategoryDao;
	}

	public Long addTemplateCategory(TemplateCategory templateCategory) {
		return templateCategoryDao.addTemplateCategory(templateCategory);
	}

	public void saveTemplateCategory(TemplateCategory templateCategory) {
		templateCategoryDao.saveTemplateCategory(templateCategory);
	}

	public void deleteTemplateCategory(TemplateCategory templateCategory) {
		templateCategoryDao.deleteTemplateCategory(templateCategory);
	}

	public void deleteTemplateCategoryById(Long id) {
		templateCategoryDao.deleteTemplateCategoryById(id);
	}

	public TemplateCategory getTemplateCategoryById(Long id) {
		return templateCategoryDao.getTemplateCategoryById(id);
	}

	public TemplateCategory getTemplateCategoryByName(String name) {
		return templateCategoryDao.getTemplateCategoryByName(name);
	}

	public List getTemplateCategories(Long parentId) {
		return templateCategoryDao.getTemplateCategories(parentId);
	}

	public long getChildCategoryCount(Long parentId) {
		return templateCategoryDao.getChildCategoryCount(parentId);
	}
}
