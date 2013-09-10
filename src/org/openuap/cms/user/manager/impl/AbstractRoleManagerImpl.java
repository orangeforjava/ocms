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
package org.openuap.cms.user.manager.impl;

import java.util.List;

import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.user.dao.RoleDao;
import org.openuap.cms.user.event.RoleEvent;
import org.openuap.cms.user.event.UserEvent;
import org.openuap.cms.user.manager.IRoleManager;
import org.openuap.cms.user.model.AbstractUserRole;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.IUserRole;
import org.openuap.cms.user.model.UserRoleId;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * <p>
 * 抽象角色管理类.
 * </p>
 * 
 * <p>
 * $Id: AbstractRoleManagerImpl.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractRoleManagerImpl implements IRoleManager, ApplicationListener, ApplicationContextAware {

	private RoleDao roleDao;

	private ApplicationContext applicationContext;

	public AbstractRoleManagerImpl() {
	}

	public Long addRole(IRole role) throws UnauthorizedException {

		return roleDao.addRole(role);
	}

	public void saveRole(IRole role) throws UnauthorizedException {

		roleDao.saveRole(role);
	}

	public void deleteRole(IRole role) throws UnauthorizedException {

		try {
			roleDao.deleteRole(role);
			RoleEvent roleEvent = new RoleEvent(RoleEvent.ROLE_DELETED, role, null, this);
			if (applicationContext != null) {
				applicationContext.publishEvent(roleEvent);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void deleteRoleById(long roleId) throws UnauthorizedException {

		IRole role = this.getRoleById(roleId);
		if (role != null) {
			this.deleteRole(role);
		}
	}

	public IRole getRoleById(long roleId) {
		return roleDao.getRoleById(roleId);
	}
	/**
	 * 根据Guid获得角色对象
	 * @param guid
	 * @return
	 */
	public IRole getRoleByGuid(String guid){
		return roleDao.getRoleByGuid(guid);
	}
	public List getUserRoles(long userId) {
		return roleDao.getUserRoles(userId);
	}

	public List getRoleUsers(long roleId) {
		return roleDao.getRoleUsers(roleId);
	}

	public List getAllRole() {
		return roleDao.getAllRole();
	}

	public int getAllRoleCount() {
		return roleDao.getAllRoleCount();
	}

	public void setRoleDao(RoleDao dao) {
		this.roleDao = dao;
	}

	/**
	 * 
	 * @param start
	 *            
	 * @param length
	 *            
	 * @param where
	 *            
	 * @param order
	 *            
	 * @param pb
	 *            
	 * @return
	 */
	public List getRoles(long start, long length, String where, String order, PageBuilder pb) {
		return roleDao.getRoles(start, length, where, order, pb);
	}

	public void saveUserRole(IUserRole userRole) {
		roleDao.saveUserRole(userRole);
	}

	public void deleteUserRole(IUserRole userRole) {
		roleDao.deleteUserRole(userRole);
	}

	public void deleteAllRole(long userId) {
		roleDao.deleteAllRole(userId);
	}

	public void deleteAllUser(long roleId) {
		roleDao.deleteAllUser(roleId);
	}

	public void deleteUserRole(long userId, long roleId) {
		roleDao.deleteUserRole(userId, roleId);
	}

	/**
	 * 
	 * @param userId
	 *            
	 * @return int
	 */
	public int getUserRoleCount(long userId) {
		return roleDao.getUserRoleCount(userId);
	}

	/**
	 * 
	 * @param roleId
	 *            
	 * @return int
	 */
	public int getRoleUserCount(long roleId) {
		return roleDao.getRoleUserCount(roleId);
	}

	public void saveUserRole(long userId, long roleId) {
		UserRoleId burId = createUserRoleId();
		burId.setUserId(userId);
		burId.setRoleId(roleId);
		AbstractUserRole bur = createUserRole();
		bur.setId(burId);
		this.saveUserRole(bur);
	}
	public abstract UserRoleId createUserRoleId();
	public abstract AbstractUserRole createUserRole();
	
	public List getUserRoles(long userId, long start, long limit, String where, String order, PageBuilder pb) {
		return roleDao.getUserRoles(userId, start, limit, where, order, pb);
	}

	public List getRoleUsers(long roleId, long start, long limit, String where, String order, PageBuilder pb) {
		return roleDao.getRoleUsers(roleId, start, limit, where, order, pb);
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof UserEvent) {
			UserEvent userEvent = (UserEvent) event;
			//
			int eventType = userEvent.getEventType();
			if (eventType == UserEvent.USER_DELETED) {
				//
				IUser user = userEvent.getUser();
				//
				if (user != null) {
					this.deleteAllRole(user.getUserId());
				}
			}
		} else if (event instanceof RoleEvent) {
			RoleEvent roleEvent = (RoleEvent) event;
			int eventType = roleEvent.getEventType();
			if (eventType == RoleEvent.ROLE_DELETED) {
				IRole role = roleEvent.getRole();
				if (role != null) {
					this.deleteAllUser(role.getRoleId());
				}
			}
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}
}
