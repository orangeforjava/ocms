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
package org.openuap.cms.psn.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.psn.dao.PsnDao;
import org.openuap.cms.psn.model.Psn;

/**
 * 
 * <p>
 * PSN DAO Hibernate实现.
 * </p>
 *
 * 
 * <p>
 * $Id: PsnDaoImpl.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PsnDaoImpl extends BaseDaoHibernate implements PsnDao {
	public PsnDaoImpl() {
	}

	public Long addPsn(Psn psn) {
		//
		return (Long) this.getHibernateTemplate().save(psn);
	}

	public void savePsn(Psn psn) {
		getHibernateTemplate().saveOrUpdate(psn);
		// necessary to throw a DataIntegrityViolation and catch it in
		// PsnManager
		getHibernateTemplate().flush();

	}

	public void deletePsn(Long id) {
		Psn psn = this.getPsnById(id);
		this.getHibernateTemplate().delete(psn);
	}

	public List getAllPsn() {
		return this.executeFind("from Psn");
	}

	public Psn getPsnById(Long id) {
		return (Psn) this.findUniqueResult("from Psn where id=?",
				new Object[] { id });
	}

	public Psn getPsnByName(String name) {
		return (Psn) this.findUniqueResult("from Psn where name=?",
				new Object[] { name });
	}

	public long getPsnCount() {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from Psn").next()).longValue();
	}
}
