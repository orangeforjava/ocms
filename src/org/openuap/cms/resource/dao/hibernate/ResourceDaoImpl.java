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
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.resource.dao.ResourceDao;
import org.openuap.cms.resource.model.Resource;

/**
 * <p>
 * 资源DAO实现.
 * </p>
 * 
 * <p>
 * $Id: ResourceDaoImpl.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 *
 * @author Joseph
 * @version 1.0 
 */
public class ResourceDaoImpl extends BaseDaoHibernate implements ResourceDao {

    public ResourceDaoImpl() {
    }

    public Long addResource(Resource resource) {
        return (Long)this.getHibernateTemplate().save(resource);
    }

    public void saveResource(Resource resource) {
        getHibernateTemplate().saveOrUpdate(resource);
        // necessary to throw a DataIntegrityViolation and catch it in PsnManager
        getHibernateTemplate().flush();

    }

    public void deleteResource(Long resourceId) {
        Resource rs = this.getResourceById(resourceId);
        this.getHibernateTemplate().delete(rs);
    }

    public Resource getResourceById(Long resourceId) {
        return (Resource)this.findUniqueResult(
                "from Resource where resourceId=?",
                new Object[] {resourceId});
    }

    public Resource getResourceByPath(String path) {
        return (Resource)this.findUniqueResult("from Resource where path=?",
                                               new Object[] {path});
    }

    public List getAllResource() {
        return this.executeFind("from Resource order by creationDate desc");
    }

    public List getAllResource(Long start, Long length) {
        QueryInfo qi = new QueryInfo();
        qi.setOffset(new Integer(start.intValue()));
        qi.setLimit(new Integer(length.intValue()));

        return this.executeFind("from Resource", qi);
    }

    public List getResourcesByNode(Long nodeId) {
        return this.executeFind(
                "from Resource where nodeId=? order by creationDate desc",
                new Object[] {nodeId});
    }

    public List getResourcesByNode(Long nodeId, Long start, Long length) {
        QueryInfo qi = new QueryInfo();
        qi.setOffset(new Integer(start.intValue()));
        qi.setLimit(new Integer(length.intValue()));
        return this.executeFind(
                "from Resource as rs where rs.nodeId=? order by rs.creationDate desc",
                qi,
                new Object[] {nodeId});
    }

    public List getResourcesByNodeCata(Long nodeId, String category,
                                       Long start, Long length) {
        QueryInfo qi = new QueryInfo();
        qi.setOffset(new Integer(start.intValue()));
        qi.setLimit(new Integer(length.intValue()));
        return this.executeFind("select rs,u.name from Resource as rs,BaseUser u where rs.creationUserId=u.id and rs.nodeId=? and rs.category=? order by rs.creationDate desc",
                                qi,
                                new Object[] {nodeId, category});

    }

    public List getResourcesByCata(String category, Long start, Long length) {
        QueryInfo qi = new QueryInfo();
        qi.setOffset(new Integer(start.intValue()));
        qi.setLimit(new Integer(length.intValue()));
        return this.executeFind(
                "select rs,u.name from Resource as rs,BaseUser u " +
                " where rs.creationUserId=u.id and  rs.category=? " +
                " order by rs.creationDate desc",
                qi,
                new Object[] {category});

    }

    public long getResourceCountByNodeCata(Long nodeId, String category) {
        return ((Number)this.getHibernateTemplate().iterate(
                "select count(*) from Resource where nodeId=? and category=?",
                new Object[] {nodeId, category}).
                next()).longValue();

    }

    public long getResourceCountByCata(String category) {
        return ((Number)this.getHibernateTemplate().iterate(
                "select count(*) from Resource where  category=?",
                new Object[] {category}).
                next()).longValue();

    }

    public List getResourceByContentRef(Long nodeId, Long indexId,
                                        String category) {
        return this.executeFind(
                "select rs from Resource as rs,ResourceRef as rf where " +
                "rs.resourceId=rf.resourceId and rf.nodeId=? and " +
                "rf.indexId=? and rs.category=?",
                new Object[] {nodeId, indexId, category});
    }

    public List getResources(String hql, String hql_count, QueryInfo qi,
                             PageBuilder pb) {
        return this.getObjects(hql, hql_count, qi, pb);
    }

    public List getResourcesByUser(Long userId, QueryInfo qi, PageBuilder pb) {
        String hql = "select rs from Resource as rs " +
                     "where rs.type=0 and rs.creationUserId=" + userId +
                     " order by rs.creationDate desc";
        String hql2 = "select count(rs.resourceId) from Resource as rs " +
                      "where rs.creationUserId=" + userId + " ";
        return this.getObjects(hql, hql2, qi, pb);
    }

    public long getTotalResourceSizeByUser(Long userId, QueryInfo qi) {
        String hql = "select sum(rs.size) from Resource as rs" +
                     " where rs.type=0 and rs.creationUserId=" + userId + "";
        Object obj = this.getObject(hql, qi);
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        } else if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        return 0L;
    }

    public long getResourceCountByUser(Long userId, QueryInfo qi,
                                       PageBuilder pb) {
        String hql2 = "select count(rs.resourceId) from Resource as rs " +
                      "wherers.type=0 and  rs.creationUserId=" + userId + " ";
        return this.getObjectCount(hql2, qi, pb).longValue();

    }

    public List getResourcesByMember(Long memberId, QueryInfo qi,
                                     PageBuilder pb) {

        String hql = "select rs from Resource as rs " +
                     "where rs.type=1 and rs.creationUserId=" + memberId +
                     " order by rs.creationDate desc";
        String hql2 = "select count(rs.resourceId) from Resource as rs " +
                      "where rs.type=1 and rs.creationUserId=" + memberId + " ";
        return this.getObjects(hql, hql2, qi, pb);

    }

    public long getResourceCountByMember(Long memberId, QueryInfo qi,
                                         PageBuilder pb) {
        String hql2 = "select count(rs.resourceId) from Resource as rs " +
                      "where rs.type=1 and rs.creationUserId=" + memberId + " ";
        return this.getObjectCount(hql2, qi, pb).longValue();

    }

    public long getTotalResourceSizeByMember(Long memberId, QueryInfo qi) {
        String hql = "select sum(rs.size) from Resource as rs" +
                     " where rs.type=1 and rs.creationUserId=" + memberId + "";
        Object obj = this.getObject(hql, qi);
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        } else if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        return 0L;

    }

    public List getResourcesByNodeCata(Long nodeId, String category,
                                       String customCategory, Long start,
                                       Long length) {
        QueryInfo qi = new QueryInfo();
        qi.setOffset(new Integer(start.intValue()));
        qi.setLimit(new Integer(length.intValue()));
        return this.executeFind(
                "select rs,u.name from Resource as rs,BaseUser u " +
                " where rs.creationUserId=u.id and rs.nodeId=? and rs.category=?" +
                " and rs.customCategory=? order by rs.creationDate desc",
                qi,
                new Object[] {nodeId, category, customCategory});

    }

    public List getResourcesByCata(String category, String customCategory,
                                   Long start, Long length) {
        QueryInfo qi = new QueryInfo();
        qi.setOffset(new Integer(start.intValue()));
        qi.setLimit(new Integer(length.intValue()));
        return this.executeFind(
                "select rs,u.name from Resource as rs,BaseUser u " +
                " where rs.creationUserId=u.id and  rs.category=? and rs.customCategory=?" +
                " order by rs.creationDate desc",
                qi,
                new Object[] {category, customCategory});

    }

    public long getResourceCountByNodeCata(Long nodeId, String category,
                                           String customCategory) {

        return ((Number)this.getHibernateTemplate().iterate(
                 "select count(*) from Resource where nodeId=? and category=? and customCategory=?",
                 new Object[] {nodeId, category,customCategory}).
                 next()).longValue();

    }

    public long getResourceCountByCata(String category, String customCategory) {
        return ((Number)this.getHibernateTemplate().iterate(
                "select count(*) from Resource where  category=? and customCategory=?",
                new Object[] {category,customCategory}).
                next()).longValue();

    }
}
