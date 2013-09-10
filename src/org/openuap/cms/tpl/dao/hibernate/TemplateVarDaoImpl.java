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
import org.openuap.cms.tpl.dao.TemplateVarDao;
import org.openuap.cms.tpl.model.TemplateVar;

/**
 * <p>
 * 模版变量DAO实现.
 * </p>
 * 
 * <p>
 * $Id: TemplateVarDaoImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateVarDaoImpl extends BaseDaoHibernate implements
		TemplateVarDao {
	public TemplateVarDaoImpl() {
	}

	public Long addTemplateVar(TemplateVar templateVar) {
		return (Long) this.getHibernateTemplate().save(templateVar);
	}

	public void saveTemplateVar(TemplateVar template) {
		this.getHibernateTemplate().saveOrUpdate(template);
	}

	public void deleteTemplateVar(TemplateVar template) {
		this.getHibernateTemplate().delete(template);
	}

	public void deleteTemplateVarById(long id) {
		TemplateVar tpl = this.getTemplateVarById(id);
		this.getHibernateTemplate().delete(tpl);
	}

	public TemplateVar getTemplateVarById(long id) {
		String hql = "from TemplateVar where id=?";
		return (TemplateVar) this.findUniqueResult(hql,
				new Object[] { new Long(id) });

	}

	public TemplateVar getTemplateVarByName(String name) {
		String hql = "from TemplateVar where name=?";
		return (TemplateVar) this.findUniqueResult(hql, new Object[] { name });

	}

	public List getAllTemplateVars() {
		String hql = "from TemplateVar";
		return this.executeFind(hql);
	}
}
