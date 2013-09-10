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
package org.openuap.cms.tpl.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.tpl.dao.TemplateDao;
import org.openuap.cms.tpl.model.Template;

/**
 * <p>
 * 模板DAO实现.
 * </p>
 * 
 * <p>
 * $Id: TemplateDaoImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateDaoImpl extends BaseDaoHibernate implements TemplateDao {
	public TemplateDaoImpl() {
	}

	public Long addTemplate(Template template) {
		return (Long) this.getHibernateTemplate().save(template);
	}

	public void saveTemplate(Template template) {
		this.getHibernateTemplate().saveOrUpdate(template);
	}

	public void deleteTemplate(Template template) {
		this.getHibernateTemplate().delete(template);
	}

	public void deleteTemplateById(Long id) {
		Template tpl = this.getTemplateById(id);
		this.getHibernateTemplate().delete(tpl);
	}

	public Template getTemplateById(Long id) {
		String hql = "from Template where id=?";
		return (Template) this.findUniqueResult(hql, new Object[] { id });

	}

	public Template getTemplateByName(String name) {
		String hql = "from Template where tplName=?";
		return (Template) this.findUniqueResult(hql, new Object[] { name });

	}

	public List getTemplates(Long tcid) {
		String hql = "from Template where tcid=?";
		return this.executeFind(hql, new Object[] { tcid });
	}

	public List getTemplates(Long tcid, QueryInfo qi, PageBuilder pb) {
		String hql = "select tp from Template as tp where tcid=" + tcid + " ";
		String hql_count = "select count(tp.id) from Template as tp where tcid="
				+ tcid + " ";
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public List getTemplates(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb) {
		return this.getObjects(hql, hql_count, qi, pb);
	}
}
