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

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.user.UserExistsException;
import org.openuap.cms.user.dao.UserDao;
import org.openuap.cms.user.event.UserEvent;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.IUserSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * <p>
 * 抽象用户管理类.
 * </p>
 * 
 * <p>
 * $Id: AbstractUserManagerImpl.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractUserManagerImpl implements IUserManager,
		ApplicationListener, ApplicationContextAware {

	protected UserDao userDao;

	private ApplicationContext applicationContext;

	public AbstractUserManagerImpl() {
	}

	public void setUserDao(UserDao dao) {
		this.userDao = dao;
	}

	/**
	 * if success it should return the new user Id
	 * 
	 * @param baseUser
	 *            the new User object.
	 * @return the new User Id
	 * @throws UserExistsException
	 */
	public long addUser(IUser baseUser) throws UserExistsException {
		// first get the userByName if the user exist,it will throw a
		// UserExistsException
		// IUser euser = this.getUserByName(baseUser.getName());
		// if (euser != null) {
		// throw new UserExistsException("User '" + euser.getName() + "' already
		// exists!");
		// }
		// use hash arithmetic to digest the user password.
		// the arithmetic can be config in config file.
		// now,has two arithmetic:MD5,MySQL
		String ar = getPwdArithmetic();
		String digestedPwd = StringUtil.digest(baseUser.getPassword(), ar);
		baseUser.setPassword(digestedPwd);
		return userDao.addUser(baseUser).longValue();
	}

	public long addUser2(IUser baseUser) throws UserExistsException {
		return userDao.addUser(baseUser).longValue();
	}

	public void saveUser(IUser user) throws UserExistsException {
		try {
			userDao.saveUser(user);
		} catch (DataIntegrityViolationException e) {
			throw new UserExistsException("User '" + user.getName()
					+ "' already exists!");
		}
	}

	public void saveUserWithChangePwd(IUser user) throws UserExistsException {
		String ar = getPwdArithmetic();
		String digestedPwd = StringUtil.digest(user.getPassword(), ar);
		user.setPassword(digestedPwd);
		try {
			userDao.saveUser(user);
		} catch (DataIntegrityViolationException e) {
			throw new UserExistsException("User '" + user.getName()
					+ "' already exists!");
		}

	}

	private String getPwdArithmetic() {
		String ar = "md5"; // CMSConfig.getInstance().getPwdEncryptArithmetic();
		return ar;
	}

	public IUser getUserById(long id) {
		return userDao.getUserById(id);
	}

	public IUser getUserByName(String name) {
		return userDao.getUserByName(name);
	}

	public void removeUser(IUser baseUser) {
		try {
			userDao.removeUser(baseUser);
			if (this.applicationContext != null) {
				UserEvent userEvent = new UserEvent(UserEvent.USER_DELETED,
						baseUser, null, this);
				this.applicationContext.publishEvent(userEvent);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void removeUserById(Long id) {
		IUser user = this.getUserById(id);
		if (user != null) {
			this.removeUser(user);
		}
	}

	public void removeUserByName(String name) {
		IUser user = this.getUserByName(name);
		if (user != null) {
			this.removeUser(user);
		}
	}

	public List getAllUsers() {
		return userDao.getAllUsers();
	}

	public List getUsers(QueryInfo qi, PageBuilder pb) {
		return userDao.getUsers(qi, pb);
	}

	public boolean validatePassword(String rawPwd, String hashPwd) {
		if (rawPwd == null) {
			rawPwd = "";
		}
		String parsePwd = StringUtil.digest(rawPwd, this.getPwdArithmetic());
		return parsePwd.equals(hashPwd);
	}

	public long getUserCount() {
		return userDao.getUserCount();
	}

	public List getSysUsers(Long start, Long length, String order) {
		return userDao.getSysUsers(start, length, order);
	}

	public void updateUserField(long id, String fieldName, Object value) {
		userDao.updateUserField(id, fieldName, value);
	}

	public void onApplicationEvent(ApplicationEvent event) {
		//TODO 去掉Acegi相关内容
//		if (event instanceof AuthenticationSuccessEvent) {
//			AuthenticationSuccessEvent e = (AuthenticationSuccessEvent) event;
//			Object obj = e.getAuthentication().getPrincipal();
//			IUser bu = null;
//			if (obj instanceof AuthUser) {
//				AuthUser u = (AuthUser) obj;
//				bu = u.getUser();
//			} else {
//				String userName = (String) obj;
//				bu = this.getUserByName(userName);
//			}
//			Long userId = bu.getUserId();
//			this.addUserLoginTimes(userId);
//			this.updateUserField(userId, "lastLoginDate", new Long(System
//					.currentTimeMillis()));
//		}
	}

	public int getUserByNameCount(String name) {
		return userDao.getUserByNameCount(name);
	}

	public int getUserByEmailCount(String email) {
		return userDao.getUserByEmailCount(email);
	}

	public IUser getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	/**
	 * 
	 * 
	 * @param where
	 *            String
	 * @param fieldName
	 *            String
	 * @param value
	 *            Object
	 */
	public void batchUpdateUserField(String where, String fieldName,
			Object value) {
		userDao.batchUpdateUserField(where, fieldName, value);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 这种添加用户,对密码做了MD5操作
	 */
	public long addUser(String username, String password, int type)
			throws UserExistsException {
		
		return this.addUser(username,password,type,false);
	}

	public long addUser(String username, String password, int type,boolean md5Pwd)
			throws UserExistsException {
		IUser user = createUser();
		user.setName(username);
		if(!md5Pwd){
			String ar = getPwdArithmetic();
			String digestedPwd = StringUtil.digest(password, ar);
			user.setPassword(digestedPwd);
		}else{
			user.setPassword(password);
		}
		//
		user.setUserStatus(new Integer(IUser.NORMAL_STATUS));
		user.setType(new Integer(type));
		user.setCreationDate(new Long(System.currentTimeMillis()));
		user.setLastLoginDate(-1L);
		user.setLoginTimes(0L);
		user.setPos(0);
		return userDao.addUser(user);
	}

	public void addUserLoginTimes(long id) {
		userDao.addUserLoginTimes(id);

	}

	public List getSysUsers(long start, long length, String order) {
		return userDao.getSysUsers(start, length, order);
	}

	public IUser getUserByNameAndEid(String name, long eid) {
		return userDao.getUserByNameAndEid(name, eid);
	}

	public void removeUserById(long id) {
		IUser user = this.getUserById(id);
		if (user != null) {
			this.removeUser(user);
		}

	}

	public UserDao getUserDao() {
		return userDao;
	}

	public IUserSession getUserSessionByUserName(String userName) {
		IUser user = getUserByName(userName);
		if (user != null) {
			IUserSession userSession = getUserSession(user);
			return userSession;
		}
		return null;
	}

	/**
	 * 
	 */
	public void updateLoginInfo(String mid, String ip, Long loginDate) {
		IUser user = this.getUserById(new Long(mid));
		if (user != null) {
			user.setLastLoginIp(ip);
			long times = user.getLoginTimes();
			user.setLoginTimes(times + 1);
			user.setLastLoginDate(loginDate);
			try {
				this.saveUser(user);
			} catch (UserExistsException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取指定类型与状态的用户集合
	 * @param type 用户类型，如管理员，系统用户，会员
	 * @param status 正常，停用等
	 * @return
	 */
	public List<IUser> getUsers(int type,int status){
		return userDao.getUsers(type, status);
	}
	
	public long getAllUserCount(){
		return userDao.getAllUserCount();
	}
	public long getUserCount(QueryInfo qi) {
		return userDao.getUserCount(qi);
	}
}
