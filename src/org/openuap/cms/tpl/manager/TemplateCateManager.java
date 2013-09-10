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
package org.openuap.cms.tpl.manager;

import java.util.List;

import org.openuap.cms.tpl.dao.TemplateCategoryDao;
import org.openuap.cms.tpl.model.TemplateCategory;

/**
 * <p>
 * 模板分类管理接口.
 * </p>
 * 
 * <p>
 * $Id: TemplateCateManager.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public interface TemplateCateManager {

	public void setTemplateCategoryDao(TemplateCategoryDao templateCategoryDao);

	public Long addTemplateCategory(TemplateCategory templateCategory);

	public void saveTemplateCategory(TemplateCategory templateCategory);

	public void deleteTemplateCategory(TemplateCategory templateCategory);

	public void deleteTemplateCategoryById(Long id);

	public TemplateCategory getTemplateCategoryById(Long id);

	public TemplateCategory getTemplateCategoryByName(String name);

	public List getTemplateCategories(Long parentId);

	public long getChildCategoryCount(Long parentId);
}
