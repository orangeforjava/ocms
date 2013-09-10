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
import java.util.Map;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.user.dao.UserDao;
import org.openuap.cms.user.model.IUser;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 抽象用户DAO类.
 * </p>
 * 
 * <p>
 * $Id: AbstractUserDaoImpl.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractUserDaoImpl extends BaseDaoHibernate implements
		UserDao {

	private String entityName;

	private Class entityClass;

	/**
	 * 
	 */
	public AbstractUserDaoImpl() {
	}

	public IUser getUserById(Long id) {
		return (IUser) this.findUniqueResult("from " + entityName
				+ " where id=?", new Object[] { id });

	}

	public Long addUser(IUser baseUser) {
		return (Long) this.getHibernateTemplate().save(baseUser);
	}

	public void saveUser(final IUser user) {
		getHibernateTemplate().saveOrUpdate(user);
		// necessary to throw a DataIntegrityViolation and catch it in
		// UserManager
		getHibernateTemplate().flush();
	}

	public IUser getUserByName(String name) {
		String hql = "from " + entityName + " where name='" + name + "'";
		List users = this.executeFind(hql);
		if (users != null && users.size() > 0) {
			return (IUser) users.get(0);
		}
		return null;
	}

	public void removeUser(IUser baseUser) {
		this.getHibernateTemplate().delete(baseUser);
		getHibernateTemplate().flush();
	}

	public void removeUserById(Long id) {
		IUser user = this.getUserById(id);
		this.removeUser(user);
	}

	public void removeUserByName(String name) {
		IUser user = this.getUserByName(name);
		this.removeUser(user);
	}

	public List getAllUsers() {
		return this.getHibernateTemplate().find("from " + entityName + "");
	}

	public List getUsers(QueryInfo qi, PageBuilder pb) {
		String hql = "from " + entityName + " ";
		String hql2 = "select count(*) from " + entityName + " ";
		String where = qi.getWhereClause();
		String order = qi.getOrderByClause();
		Map paramters = qi.getQueryParameters();
		if (StringUtils.hasText(where)) {
			hql += " where " + where;
			hql2 += " where " + where;
		}
		if (StringUtils.hasText(order)) {
			hql += " order by " + order;
		} else {
			// 缺省按照用户id排序
			hql += " order by id";
		}
		if (paramters != null && paramters.size() > 0) {
			Object[] args = paramters.values().toArray();
			int totalCount = ((Number) this.getHibernateTemplate().iterate(
					hql2, args).next()).intValue();
			pb.items(totalCount);
			return this.executeFind(hql, qi, args);

		} else {
			int totalCount = ((Number) this.getHibernateTemplate()
					.iterate(hql2).next()).intValue();
			pb.items(totalCount);
			return this.executeFind(hql, qi);
		}
	}

	public long getUserCount() {
		return getAllUserCount();
	}

	public long getAllUserCount() {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from " + entityName + "").next()).longValue();
	}

	public long getUserCount(QueryInfo qi) {
		return ((Number) this.getHibernateTemplate()
				.iterate(
						"select count(*) from " + entityName + ""
								+ qi.getWhereClause()).next()).longValue();
	}

	public List getSysUsers(Long start, Long length, String order) {
		QueryInfo qi = new QueryInfo();
		qi.setOffset(new Integer(start.intValue()));
		qi.setLimit(new Integer(length.intValue()));
		String hql = "from " + entityName + " where type<" + IUser.MEMBER_TYPE;
		if (order != null && !order.equals("")) {
			hql += " order by" + order;
		} else {
			hql += " order by pos desc,id";
		}
		return this.executeFind(hql, qi);
	}

	public void addUserLoginTimes(Long id) {
		this.executeUpdate("update " + entityName
				+ " set loginTimes=loginTimes+1 where id=?",
				new Object[] { id });
	}

	public void updateUserField(Long id, String fieldName, Object value) {
		this.executeUpdate("update " + entityName + " set " + fieldName + "=?"
				+ " where id=?", new Object[] { value, id });
	}

	public int getUserByNameCount(String name) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from " + entityName + " where name=?",
				new Object[] { name }).next()).intValue();
	}

	public IUser getUserByEmail(String email) {
		return (IUser) this.findUniqueResult("from " + entityName
				+ " as u where u.email=?", new Object[] { email });

	}

	public int getUserByEmailCount(String email) {
		return ((Number) this.getHibernateTemplate().iterate(
				"select count(*) from " + entityName + " where email=?",
				new Object[] { email }).next()).intValue();

	}

	public void batchUpdateUserField(String where, String fieldName,
			Object value) {

		String hql = "update " + entityName + " set " + fieldName + "=?";
		if (where != null && !where.equals("")) {
			hql += " where " + where;
		}
		this.executeUpdate(hql, new Object[] { value });

	}

	public IUser getUserByNameAndEid(String name, Long eid) {
		String hql = "from " + entityName + " where name=? and eid=?";
		return (IUser) this.findUniqueResult(hql, new Object[] { name, eid });
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

	/**
	 * 获取指定类型与状态的用户集合
	 * 
	 * @param type
	 *            用户类型，如管理员，系统用户，会员
	 * @param status
	 *            正常，停用等
	 * @return
	 */
	public List<IUser> getUsers(int type, int status) {
		String typeCondition="";
		if ((type & IUser.ADMIN_TYPE) == IUser.ADMIN_TYPE) {
			typeCondition+=" or type="+IUser.ADMIN_TYPE;
		}
		if ((type & IUser.SYS_USER_TYPE) == IUser.SYS_USER_TYPE) {
			typeCondition+=" or type="+IUser.SYS_USER_TYPE;
		}
		if ((type & IUser.MEMBER_TYPE) == IUser.MEMBER_TYPE) {
			typeCondition+=" or type="+IUser.MEMBER_TYPE;
		}
		if ((type & IUser.ANONYMOUS_TYPE) == IUser.ANONYMOUS_TYPE) {
			typeCondition+=" or type="+IUser.ANONYMOUS_TYPE;
		}
		if(typeCondition.length()>0){
			typeCondition=typeCondition.substring(3);
		}
		String hql = "from " + entityName
				+ " where status=? and ("+typeCondition+") ORDER BY pos";
		return this.executeFind(hql, new Object[] { status });
	}
}
