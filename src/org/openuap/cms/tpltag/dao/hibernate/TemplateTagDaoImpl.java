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
package org.openuap.cms.tpltag.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.tpltag.dao.TemplateTagDao;
import org.openuap.cms.tpltag.model.TemplateTag;

/**
 * <p>
 * 模板Tag Hibernate DAO实现.
 * </p>
 * 
 * <p>
 * $Id: TemplateTagDaoImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateTagDaoImpl extends BaseDaoHibernate implements
		TemplateTagDao {

	public Long addTag(TemplateTag tag) {
		return (Long) this.getHibernateTemplate().save(tag);
	}

	public void deleteTag(Long id) {
		TemplateTag tag = this.getTagById(id);
		if (tag != null) {
			this.getHibernateTemplate().delete(tag);
		}
	}

	public TemplateTag getTagById(Long id) {
		String hql = "from TemplateTag where id=?";
		return (TemplateTag) this.findUniqueResult(hql, new Object[] { id });
	}

	public List<TemplateTag> getTagsByNode(Long nodeId) {
		String hql = "from TemplateTag where nodeId=? order by pos DESC";
		return this.executeFind(hql, new Object[] { nodeId });
	}

	public void saveTag(TemplateTag tag) {
		getHibernateTemplate().saveOrUpdate(tag);
		getHibernateTemplate().flush();
	}

	public List<TemplateTag> getTagsByModel(Long modelId) {
		String hql = "from TemplateTag where modelId=? order by pos DESC";
		return this.executeFind(hql, new Object[] { modelId });
	}

	public TemplateTag getTagByName(String name) {
		String hql = "from TemplateTag where name=?";
		return (TemplateTag) this.findUniqueResult(hql, new Object[] { name });
	}

	public List<TemplateTag> getAllTags() {
		String hql="from TemplateTag";
		return this.executeFind(hql);
	}

}
