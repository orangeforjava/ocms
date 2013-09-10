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
import org.openuap.cms.tpl.dao.TemplateRefDao;
import org.openuap.cms.tpl.model.TemplateRef;

/**
 * <p>
 * 模板引用DAO实现.
 * </p>
 * 
 * <p>
 * $Id: TemplateRefDaoImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateRefDaoImpl extends BaseDaoHibernate implements
		TemplateRefDao {
	public TemplateRefDaoImpl() {
	}

	public void addTemplateRef(TemplateRef templateRef) {
		this.getHibernateTemplate().save(templateRef);
	}

	public void saveTemplateRef(TemplateRef templateRef) {
		this.getHibernateTemplate().saveOrUpdate(templateRef);
	}

	public TemplateRef getTemplateRefById(Long indexId, Long templateId) {
		String hql = "from TemplateRef where indexId=? and templateId=?";

		return (TemplateRef) this.findUniqueResult(hql, new Object[] { indexId,
				templateId });
	}

	public List getTemplateRefByTemplateId(Long templateId) {
		String hql = "from TemplateRef where templateId=?";
		return this.executeFind(hql, new Object[] { templateId });
	}

	public List getTemplateRefByIndexId(Long indexId) {
		String hql = "from TemplateRef where indexId=?";
		return this.executeFind(hql, new Object[] { indexId });
	}

	public void deleteTemplateRef(Long indexId, Long templateId) {
		String hql = "delete from TemplateRef where indexId=? and templateId=?";
		this.executeUpdate(hql, new Object[] { indexId, templateId });
	}

	public void deleteTemplateRefByTemplate(Long templateId) {
		String hql = "delete from TemplateRef where templateId=?";
		this.executeUpdate(hql, new Object[] { templateId });
	}

	public void deleteTempalteRefByIndex(Long indexId) {
		String hql = "delete from TemplateRef where indexId=?";
		this.executeUpdate(hql, new Object[] { indexId });
	}
}
