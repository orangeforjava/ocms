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
package org.openuap.cms.tpl.dao;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.tpl.model.Template;

/**
 * <p>
 * 模板DAO.
 * </p>
 * 
 * <p>
 * $Id: TemplateDao.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface TemplateDao {

	public Long addTemplate(Template template);

	public void saveTemplate(Template template);

	public void deleteTemplate(Template template);

	public void deleteTemplateById(Long id);

	public Template getTemplateById(Long id);

	public Template getTemplateByName(String name);

	public List getTemplates(Long tcid);

	public List getTemplates(Long tcid, QueryInfo qi, PageBuilder pb);

	public List getTemplates(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb);
}
