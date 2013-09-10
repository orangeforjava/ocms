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
package org.openuap.cms.resource.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.resource.dao.ResourceRefDao;
import org.openuap.cms.resource.model.ResourceRef;

/**
 * <p>
 * 资源引用DAO实现.
 * </p>
 *
 * <p>
 * $Id: ResourceRefDaoImpl.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 *
 * @author Joseph
 * @version 1.0 
 */
public class ResourceRefDaoImpl
    extends BaseDaoHibernate implements ResourceRefDao {
  public ResourceRefDaoImpl() {
  }

  public void addResourceRef(ResourceRef resourceRef) {
    this.getHibernateTemplate().save(resourceRef);
  }

  public void saveResourceRef(ResourceRef resourceRef) {
    getHibernateTemplate().saveOrUpdate(resourceRef);
    // necessary to throw a DataIntegrityViolation and catch it in PsnManager
    getHibernateTemplate().flush();
  }

  public ResourceRef getResourceRefById(Long nodeId, Long indexId,
                                        Long resourceId) {
    return (ResourceRef)this.findUniqueResult(
        "from ResourceRef where nodeId=? and indexId=? and resourceId=?",
        new Object[] {nodeId, indexId, resourceId});
  }

  public List getResourceRefByNodeIndexId(Long nodeId, Long indexId) {
    return this.executeFind("from ResourceRef where nodeId=? and indexId=?",
                            new Object[] {nodeId, indexId});
  }

  public void deleteResourceRefByNodeIndexId(Long nodeId, Long indexId) {
    this.executeUpdate("delete from ResourceRef where nodeId=? and indexId=?",
                       new Object[] {nodeId, indexId});
  }

}
