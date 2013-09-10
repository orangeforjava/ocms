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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.user.dao.PermissionDao;
import org.openuap.cms.user.model.AbstractRolePermission;
import org.openuap.cms.user.model.AbstractUserPermission;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.PermissionObject;
import org.openuap.cms.user.model.Permissions;
import org.openuap.cms.user.model.RolePermissionId;
import org.openuap.cms.user.model.UserPermissionId;

/**
 * <p>
 * 抽象权限DAO类.
 * </p>
 * 
 * <p>
 * $Id: AbstractPermissionDaoImpl.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractPermissionDaoImpl extends BaseDaoHibernate
		implements PermissionDao {

	private String userPermEntityName;

	private String rolePermEntityName;

	private String userRoleEntityName;

	private String roleEntityName = "BaseRole";

	private String userEntityName = "BaseUser";

	public String getRolePermEntityName() {
		return rolePermEntityName;
	}

	public void setRolePermEntityName(String rolePermEntityName) {
		this.rolePermEntityName = rolePermEntityName;
	}

	public AbstractPermissionDaoImpl() {
	}

	/**
	 * 获得用户最终权限集合
	 */
	public Map getUserFinalPermissions(Long userId) {
		//
		Map permMap = Collections.synchronizedMap(new HashMap());
		// 获得用户被直接授予的权限
		List userPermissions = this.executeFind("select p from "
				+ userPermEntityName + " as p where p.userId=?",
				new Object[] { userId });
		if (userPermissions != null) {
			for (int i = 0; i < userPermissions.size(); i++) {
				AbstractUserPermission bup = (AbstractUserPermission) userPermissions
						.get(i);
				//对象类型与对象id
				PermissionObject po = new PermissionObject();				
				po.setObjectType(bup.getObjectType());
				po.setObjectId(bup.getObjectId());
				if (permMap.containsKey(po)) {
					// 已经有了此权限的记录，对权限进行合并
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| bup.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(bup.getObjectType(), bup
							.getObjectId(), bup.getPermission().longValue()));
				}
			}
		}
		// 获得用户通过角色继承的权限
		List rolePermissions = this.executeFind("select rp from "
				+ userRoleEntityName + " as ur," + "" + rolePermEntityName
				+ " as rp where " + "ur.roleId=rp.roleId and ur.userId=?",
				new Object[] { userId });
		if (rolePermissions != null) {
			for (int i = 0; i < rolePermissions.size(); i++) {
				AbstractRolePermission brp = (AbstractRolePermission) rolePermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(brp.getObjectType());
				po.setObjectId(brp.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| brp.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(brp.getObjectType(), brp
							.getObjectId(), brp.getPermission().longValue()));
				}
			}

		}
		return permMap;
	}

	/**
	 * 获得用户直接具备的权限
	 */
	public Map getUserPermissions(Long userId, String objectType) {
		Map permMap = new HashMap();
		// get the user direct has permission
		List userPermissions = this.executeFind("select p from "
				+ userPermEntityName
				+ " as p where p.userId=? and objectType=?", new Object[] {
				userId, objectType });
		if (userPermissions != null) {
			for (int i = 0; i < userPermissions.size(); i++) {
				AbstractUserPermission bup = (AbstractUserPermission) userPermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(bup.getObjectType());
				po.setObjectId(bup.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| bup.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(bup.getObjectType(), bup
							.getObjectId(), bup.getPermission().longValue()));
				}
			}
		}
		return permMap;
	}

	/**
	 * 
	 * 获得角色具备的权限
	 * 
	 * @param roleId
	 *            角色Id
	 * @param objectType
	 *            对象类型
	 * @return Map
	 */
	public Map getRolePermissions(Long roleId, String objectType) {
		Map permMap = new HashMap();
		// get the role direct has permission
		List rolePermissions = this.executeFind("select p from "
				+ rolePermEntityName
				+ " as p where p.roleId=? and objectType=?", new Object[] {
				roleId, objectType });
		if (rolePermissions != null) {
			for (int i = 0; i < rolePermissions.size(); i++) {
				AbstractRolePermission brp = (AbstractRolePermission) rolePermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(brp.getObjectType());
				po.setObjectId(brp.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| brp.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(brp.getObjectType(), brp
							.getObjectId(), brp.getPermission().longValue()));
				}
			}
		}
		return permMap;
	}

	/**
	 * 获得具体对象（不是抽象对象)的权限
	 */
	public Map getRoleObjPermissions(Long roleId, String objectType) {
		Map permMap = new HashMap();
		// get the role direct has permission
		List rolePermissions = this.executeFind("select p from "
				+ rolePermEntityName + " as p where "
				+ " p.roleId=? and p.objectType=? and p.objectId<>-1",
				new Object[] { roleId, objectType });
		if (rolePermissions != null) {
			for (int i = 0; i < rolePermissions.size(); i++) {
				AbstractRolePermission brp = (AbstractRolePermission) rolePermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(brp.getObjectType());
				po.setObjectId(brp.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| brp.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(brp.getObjectType(), brp
							.getObjectId(), brp.getPermission().longValue()));
				}
			}
		}
		return permMap;

	}

	/**
	 * 
	 * 获得用户对指定对象类型的最终权限
	 * 
	 * @param userId
	 *            Integer
	 * @param objectType
	 *            Integer
	 * @return Map
	 */
	public Map getUserFinalPermissions(Long userId, String objectType) {
		Map permMap = new HashMap();
		// 获得用户直接具备的权限
		List userPermissions = this.executeFind("select p from "
				+ userPermEntityName
				+ " as p where p.userId=? and objectType=?", new Object[] {
				userId, objectType });
		if (userPermissions != null) {
			for (int i = 0; i < userPermissions.size(); i++) {
				AbstractUserPermission bup = (AbstractUserPermission) userPermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(bup.getObjectType());
				po.setObjectId(bup.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| bup.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(bup.getObjectType(), bup
							.getObjectId(), bup.getPermission().longValue()));
				}
			}
		}
		// 获得用户通过角色继承而来的权限
		List rolePermissions = this.executeFind("select rp from "
				+ userRoleEntityName + " as ur," + "" + rolePermEntityName
				+ " as rp where "
				+ "ur.roleId=rp.roleId and ur.userId=? and rp.objectType=?",
				new Object[] { userId, objectType });
		if (rolePermissions != null) {
			for (int i = 0; i < rolePermissions.size(); i++) {
				AbstractRolePermission brp = (AbstractRolePermission) rolePermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(brp.getObjectType());
				po.setObjectId(brp.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| brp.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(brp.getObjectType(), brp
							.getObjectId(), brp.getPermission().longValue()));
				}
			}

		}
		return permMap;
	}

	/**
	 * 获得用户对具体对象类型的最终权限
	 * 
	 * @param userId
	 *            Integer
	 * @param objectType
	 *            Integer
	 * @return Map
	 */
	public Map getUserFinalObjPermissions(Long userId, String objectType) {
		Map permMap = new HashMap();
		// get the user direct has permission
		List userPermissions = this.executeFind("select p from "
				+ userPermEntityName + " as p where"
				+ " p.userId=? and p.objectType=? and p.objectId<>-1",
				new Object[] { userId, objectType });
		if (userPermissions != null) {
			for (int i = 0; i < userPermissions.size(); i++) {
				AbstractUserPermission bup = (AbstractUserPermission) userPermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(bup.getObjectType());
				po.setObjectId(bup.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| bup.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(bup.getObjectType(), bup
							.getObjectId(), bup.getPermission().longValue()));
				}
			}
		}
		// get the user's roles' permission
		List rolePermissions = this.executeFind("select rp from "
				+ userRoleEntityName + " as ur," + "" + rolePermEntityName
				+ " as rp where " + "ur.roleId=rp.roleId and ur.userId=? "
				+ " and rp.objectType=? and rp.objectId<>-1", new Object[] {
				userId, objectType });
		if (rolePermissions != null) {
			for (int i = 0; i < rolePermissions.size(); i++) {
				AbstractRolePermission brp = (AbstractRolePermission) rolePermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(brp.getObjectType());
				po.setObjectId(brp.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| brp.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(brp.getObjectType(), brp
							.getObjectId(), brp.getPermission().longValue()));
				}
			}

		}
		return permMap;
	}

	/**
	 * 获得指定角色的所有权限
	 * 
	 * @param roleId
	 *            角色Id
	 * @return Map
	 */
	public Map getRolePermissions(Long roleId) {
		Map permMap = new HashMap();
		List rolePermissions = this.executeFind("select p from "
				+ rolePermEntityName + " as p where p.roleId=?",
				new Object[] { roleId });

		if (rolePermissions != null) {
			for (int i = 0; i < rolePermissions.size(); i++) {
				AbstractRolePermission brp = (AbstractRolePermission) rolePermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(brp.getObjectType());
				po.setObjectId(brp.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| brp.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(brp.getObjectType(), brp
							.getObjectId(), brp.getPermission().longValue()));
				}
			}

		}
		return permMap;

	}

	/**
	 * 获得用户直接具备的权限
	 */
	public Map getUserPermissions(Long userId) {
		Map permMap = new HashMap();
		// get the user direct has permission
		List userPermissions = this.executeFind("select p from "
				+ userPermEntityName + " as p where p.userId=?",
				new Object[] { userId });
		if (userPermissions != null) {
			for (int i = 0; i < userPermissions.size(); i++) {
				AbstractUserPermission bup = (AbstractUserPermission) userPermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(bup.getObjectType());
				po.setObjectId(bup.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| bup.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(bup.getObjectType(), bup
							.getObjectId(), bup.getPermission().longValue()));
				}
			}
		}
		return permMap;
	}

	/**
	 * 获得用户最终具备的指定对象的权限
	 */
	public Permissions getUserFinalPermissions(Long userId, String objectType,
			String objectId) {
		Permissions perms = null;
		// 用户直接
		Object obj = this.findUniqueResult("select p from "
				+ userPermEntityName + " as p "
				+ "where p.userId=? and p.objectType=? and p.objectId=?",
				new Object[] { userId, objectType, objectId });
		if (obj != null) {
			AbstractUserPermission bup = (AbstractUserPermission) obj;
			perms = new Permissions(bup.getObjectType(), bup.getObjectId(), bup
					.getPermission().longValue());
		}
		// 角色继承
		List rpList = this.executeFind("select p from " + userRoleEntityName
				+ " as ur," + rolePermEntityName + " as p "
				+ "where ur.userId=? and ur.roleId=p.roleId "
				+ "and p.objectType=? and p.objectId=?", new Object[] { userId,
				objectType, objectId });
		if (rpList != null && rpList.size() > 0) {
			for (int i = 0; i < rpList.size(); i++) {
				AbstractRolePermission brp = (AbstractRolePermission) rpList
						.get(i);
				if (perms == null) {
					perms = new Permissions(brp.getObjectType(), brp
							.getObjectId(), brp.getPermission().longValue());
				} else {
					long permssions = perms.getPermissions();
					permssions = permssions | brp.getPermission().longValue();
					perms.setPermissions(permssions);
				}
			}
		}
		return perms;
	}

	/**
	 * 获得用户对具体对象的直接权限
	 */
	public Permissions getUserPermissions(Long userId, String objectType,
			String objectId) {
		Permissions perms = null;
		Object obj = this.findUniqueResult("select p from "
				+ userPermEntityName + " as p "
				+ "where p.userId=? and p.objectType=? and p.objectId=?",
				new Object[] { userId, objectType, objectId });
		if (obj != null) {
			AbstractUserPermission bup = (AbstractUserPermission) obj;
			perms = new Permissions(bup.getObjectType(), bup.getObjectId(), bup
					.getPermission().longValue());
		}
		return perms;
	}

	/**
	 * 获得角色直接的具体对象权限
	 */
	public Permissions getRolePermissions(Long roleId, String objectType,
			String objectId) {
		Permissions perms = null;
		Object obj = this.findUniqueResult("select p from "
				+ rolePermEntityName + " as p "
				+ "where p.roleId=? and p.objectType=? and p.objectId=?",
				new Object[] { roleId, objectType, objectId });
		if (obj != null) {
			AbstractRolePermission brp = (AbstractRolePermission) obj;
			perms = new Permissions(brp.getObjectType(), brp.getObjectId(), brp
					.getPermission().longValue());
		}
		return perms;
	}

	/**
	 * 判断用户是否具备指定具体对象的权限
	 */
	public boolean hasPermission(Long userId, String objectType,
			String objectId, long perm) {
		boolean has = false;
		// 具体对象权限
		Permissions perms = getUserFinalPermissions(userId, objectType,
				objectId);
		if (perms != null) {
			has = perms.hasPermission(objectType, objectId, perm);
			if (has) {
				return has;
			}
		}
		// 指定类型对象的权限
		if (!objectId.equals(new Long(-1))) {
			//
			Permissions objPerms = getUserFinalPermissions(userId, objectType,
					"-1");
			if (objPerms != null) {
				has = objPerms.hasPermission(objectType, "-1", perm);
				if (has) {
					return has;
				}
			}
		}
		// 所有对象的权限
		if (!objectType.equals("-1") && !objectId.equals("-1")) {
			Permissions allPerms = getUserFinalPermissions(userId, "-1", "-1");

			if (allPerms != null) {
				has = allPerms.hasPermission("-1", "-1", perm);
				if (has) {
					return has;
				}
			}
		}
		//
		return has;
	}

	/**
	 * 设置用户权限
	 */
	public void setUserPermission(Long userId, String objectType,
			String objectId, long permissions) {
		//
		UserPermissionId id = createNewUserPemissionId();
		id.setUserId(userId);
		id.setObjectType(objectType);
		id.setObjectId(objectId);
		AbstractUserPermission bup = createNewUserPermission();
		bup.setId(id);
		bup.setPermission(new Long(permissions));
		getHibernateTemplate().saveOrUpdate(bup);
		//
		getHibernateTemplate().flush();
	}

	/**
	 * 移出用户权限
	 */
	public void removeUserPermission(Long userId, String objectType,
			String objectId) {
		this.executeUpdate("delete from " + userPermEntityName
				+ " where userId=?" + " and objectType=? and objectId=?",
				new Object[] { userId, objectType, objectId });
	}

	/**
	 * 设置角色权限
	 */
	public void setRolePermission(Long roleId, String objectType,
			String objectId, long permissions) {
		RolePermissionId id = createNewRolePermissionId();
		id.setRoleId(roleId);
		id.setObjectType(objectType);
		id.setObjectId(objectId);
		AbstractRolePermission brp = createNewRolePermission();
		brp.setId(id);
		brp.setPermission(new Long(permissions));
		getHibernateTemplate().saveOrUpdate(brp);
		//
		getHibernateTemplate().flush();

	}

	/**
	 * 删除角色对指定对象类型，指定对象的权限
	 */
	public void removeRolePermission(Long roleId, String objectType,
			String objectId) {
		this.executeUpdate("delete from " + rolePermEntityName
				+ " where roleId=?" + " and objectType=? and objectId=?",
				new Object[] { roleId, objectType, objectId });
	}

	/**
	 * 删除用户所有权限
	 */
	public void removeUserAllPermission(Long userId) {
		this.executeUpdate("delete from " + userPermEntityName
				+ " where userId=?" + " ", new Object[] { userId });

	}

	/**
	 * 删除角色所有权限
	 */
	public void removeRoleAllPermission(Long roleId) {
		this.executeUpdate("delete from " + rolePermEntityName
				+ " where roleId=?" + " ", new Object[] { roleId });

	}

	/**
	 * 获得用户的具体对象权限
	 */
	public Map getUserObjPermissions(Long userId, String objectType) {
		Map permMap = new HashMap();
		// get the user direct has permission
		List userPermissions = this.executeFind("select p from "
				+ userPermEntityName + " as p where p.userId=? "
				+ "and objectType=? and objectId<>-1", new Object[] { userId,
				objectType });
		if (userPermissions != null) {
			for (int i = 0; i < userPermissions.size(); i++) {
				AbstractUserPermission bup = (AbstractUserPermission) userPermissions
						.get(i);
				//
				PermissionObject po = new PermissionObject();
				po.setObjectType(bup.getObjectType());
				po.setObjectId(bup.getObjectId());
				if (permMap.containsKey(po)) {
					Permissions perm = (Permissions) permMap.get(po);
					long permissions = perm.getPermissions()
							| bup.getPermission().longValue();
					perm.setPermissions(permissions);
				} else {
					permMap.put(po, new Permissions(bup.getObjectType(), bup
							.getObjectId(), bup.getPermission().longValue()));
				}
			}
		}
		return permMap;

	}

	public String getUserPermEntityName() {
		return userPermEntityName;
	}

	public void setUserPermEntityName(String userPermEntityName) {
		this.userPermEntityName = userPermEntityName;
	}

	public String getUserRoleEntityName() {
		return userRoleEntityName;
	}

	public void setUserRoleEntityName(String userRoleEntityName) {
		this.userRoleEntityName = userRoleEntityName;
	}

	/**
	 * 获得指定对象的所拥有的角色
	 * 
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public List<IRole> getObjRoles(String objectId, String objectType) {
		String hql = "select role from "
				+ roleEntityName
				+ " as role,"
				+ rolePermEntityName
				+ " as rp "
				+ "where role.roleId=rp.roleId and rp.objectType=? and rp.objectId=?";
		return this.executeFind(hql, new Object[] { objectType, objectId });
	}

	/**
	 * 获得指定对象所拥有的用户
	 * 
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public List<IUser> getObjUsers(String objectId, String objectType) {
		String hql = "select user from "
				+ userEntityName
				+ " as user,"
				+ userPermEntityName
				+ " as up "
				+ "where user.userId=up.userId and up.objectType=? and up.objectId=?";
		return this.executeFind(hql, new Object[] { objectType, objectId });
	}

	public abstract AbstractUserPermission createNewUserPermission();

	public abstract UserPermissionId createNewUserPemissionId();

	public abstract RolePermissionId createNewRolePermissionId();

	public abstract AbstractRolePermission createNewRolePermission();

}
