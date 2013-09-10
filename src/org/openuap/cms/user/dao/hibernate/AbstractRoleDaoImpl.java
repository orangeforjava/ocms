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
package org.openuap.cms.user.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.user.dao.RoleDao;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUserRole;

/**
 * <p>
 * 抽象角色DAO实现.
 * </p>
 * 
 * <p>
 * $Id: AbstractRoleDaoImpl.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractRoleDaoImpl extends BaseDaoHibernate implements RoleDao {

	private String entityName;

	private Class entityClass;

	private String userEntityName;
	
	private String userRoleEntityName;

	public AbstractRoleDaoImpl() {
	}

	public long addRole(IRole role) {
		return (Long) this.getHibernateTemplate().save(role);
	}

	public void saveRole(IRole role) {
		getHibernateTemplate().saveOrUpdate(role);
		// necessary to throw a DataIntegrityViolation and catch it in
		// UserManager
		getHibernateTemplate().flush();
	}

	public void deleteRole(IRole role) {
		this.getHibernateTemplate().delete(role);
		getHibernateTemplate().flush();
	}

	public void deleteRoleById(long roleId) {
		this.executeUpdate("delete from "+entityName+" where roleId=?", new Object[] { roleId });
	}

	public IRole getRoleById(long roleId) {
		return (IRole) this.findUniqueResult("from "+entityName+" where roleId=?", new Object[] { roleId });
	}
	public IRole getRoleByGuid(String guid){
		String hql="from "+entityName+" where guid=?";
		return (IRole) this.findUniqueResult(hql,new Object[]{guid});
	}
	/**
	 * 
	 * @param userId
	 *            Integer
	 * @return List
	 */
	public List getUserRoles(long userId) {

		return this.executeFind("select role from "+entityName+" as role,"+userRoleEntityName+" as userrole"
				+ " where role.roleId=userrole.roleId and userrole.userId=?", new Object[] { userId });
	}

	public List getRoleUsers(long roleId) {
		return this.executeFind("select user from "+userEntityName+" as user,"+userRoleEntityName+" as userrole"
				+ " where user.id=userrole.userId and userrole.roleId=?", new Object[] { roleId });

	}

	public List getAllRole() {
		return this.executeFind("from "+entityName+"");
	}

	public int getAllRoleCount() {
		return ((Number) this.getHibernateTemplate().iterate("select count(*) from "+entityName+"").next()).intValue();
	}

	public List getRoles(long start, long length, String where, String order, PageBuilder pb) {
		QueryInfo qi = new QueryInfo();
		qi.setOffset(new Integer((int) start));
		qi.setLimit(new Integer((int) length));
		//
		String hql = "from "+entityName+" ";
		String hql2 = "select count(roleId) from "+entityName+" ";
		//
		if (where != null && !where.equals("")) {
			hql += " where " + where;
			hql2 += " where " + where;
		}
		int total = ((Number) this.getHibernateTemplate().iterate(hql2).next()).intValue();
		pb.items(total);

		//
		if (order != null && !order.equals("")) {
			hql += " order by" + order;
		} else {
			hql += " order by pos desc,roleId";
		}
		return this.executeFind(hql, qi);
	}

	public void saveUserRole(IUserRole userRole) {
		getHibernateTemplate().saveOrUpdate(userRole);
		// necessary to throw a DataIntegrityViolation and catch it in
		// UserManager
		getHibernateTemplate().flush();
	}

	public void deleteUserRole(IUserRole userRole) {
		this.getHibernateTemplate().delete(userRole);
		getHibernateTemplate().flush();
	}

	public void deleteAllRole(long userId) {
		this.executeUpdate("delete from "+userRoleEntityName+" where userId=?", new Object[] { userId });
	}

	public void deleteAllUser(long roleId) {
		this.executeUpdate("delete from "+userRoleEntityName+" where roleId=?", new Object[] { roleId });
	}

	public void deleteUserRole(long userId, long roleId) {
		this.executeUpdate("delete from "+userRoleEntityName+" where userId=? and roleId=?", new Object[] { userId, roleId });
	}

	public int getUserRoleCount(long userId) {
		return ((Number) this.getHibernateTemplate().iterate("select count(*) from" + " "+userRoleEntityName+" where userId=?",
				new Object[] { userId }).next()).intValue();
	}

	public int getRoleUserCount(long roleId) {
		return ((Number) this.getHibernateTemplate().iterate("select count(*) from" + " "+userRoleEntityName+" where roleId=?",
				new Object[] { roleId }).next()).intValue();

	}

	public List getUserRoles(long userId, long start, long limit, String where, String order, PageBuilder pb) {

		String hql = "select role from "+entityName+" as role,"+userRoleEntityName+" as userrole"
				+ " where role.roleId=userrole.roleId and userrole.userId=?";
		String hql2 = "select role from "+entityName+" as role,"+userRoleEntityName+" as userrole"
				+ " where role.roleId=userrole.roleId and userrole.userId=?";
		if (where != null && !where.equals("")) {
			hql += " " + where;
			hql2 += " " + where;
		}
		int total = ((Number) this.getHibernateTemplate().iterate(hql2).next()).intValue();
		pb.items(total);

		if (order != null && !order.equals("")) {
			hql += " order by " + order;
		} else {
			hql += " order by role.pos desc,role.roleId ";
		}

		QueryInfo qi = new QueryInfo();

		if (limit != 0) {
			qi.setOffset(((int) start));
			qi.setLimit((int) limit);
			return this.executeFind(hql, qi, new Object[] { userId });

		} else {
			return this.executeFind(hql, new Object[] { userId });
		}
	}

	public List getRoleUsers(long roleId, long start, long limit, String where, String order, PageBuilder pb) {
		String hql = "select user from "+userEntityName+" as user,"+userRoleEntityName+" as userrole"
				+ " where user.id=userrole.userId and userrole.roleId=?";
		String hql2 = "select count(user.id) from "+userEntityName+" as user,"+userRoleEntityName+" as userrole"
				+ " where user.id=userrole.userId and userrole.roleId=?";
		if (where != null && !where.equals("")) {
			hql += " " + where;
			hql2 += " " + where;
		}
		int total = ((Number) this.getHibernateTemplate().iterate(hql2).next()).intValue();
		pb.items(total);

		if (order != null && !order.equals("")) {
			hql += " order by " + order;
		} else {
			hql += " order by user.pos desc,user.id ";
		}
		QueryInfo qi = new QueryInfo();

		if (limit != 0) {
			qi.setOffset((int) start);
			qi.setLimit((int) limit);
			return this.executeFind(hql, qi, new Object[] { roleId });

		}
		return this.executeFind(hql, new Object[] { roleId });

	}

	public Class getEntityClass() {
		return this.entityClass;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;

	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;

	}

	public String getUserEntityName() {
		return userEntityName;
	}

	public void setUserEntityName(String userEntityName) {
		this.userEntityName = userEntityName;
	}

	public void setUserRoleEntityName(String userRoleEntityName) {
		this.userRoleEntityName = userRoleEntityName;
	}

}
