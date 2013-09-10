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
package org.openuap.cms.user.manager;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.user.UserExistsException;
import org.openuap.cms.user.model.IUser;

/**
 * <p>
 * 用户管理接口.
 * </p>
 * 
 * <p>
 * $Id: IUserManager.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface IUserManager extends ISecurityUserManager {

	public long addUser(IUser baseUser) throws UserExistsException;

	public long addUser2(IUser baseUser) throws UserExistsException;

	/**
	 * 根据给定的用户名,密码,类型添加用户
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param type
	 *            用户类型
	 * @return 新增用户的id
	 * @throws 用户已存在异常
	 */
	public long addUser(String username, String password, int type)
			throws UserExistsException;
	/**
	 * 
	 * @param username
	 * @param password
	 * @param type
	 * @param md5Pwd
	 * @return
	 * @throws UserExistsException
	 */
	public long addUser(String username, String password, int type,
			boolean md5Pwd) throws UserExistsException;

	public void saveUser(IUser user) throws UserExistsException;

	public void saveUserWithChangePwd(IUser user) throws UserExistsException;

	public IUser getUserById(long id);

	public IUser getUserByName(String name);

	/**
	 * 
	 * 
	 * @param name
	 * 
	 * @param eid
	 * 
	 * @return
	 */
	public IUser getUserByNameAndEid(String name, long eid);

	public void removeUser(IUser baseUser);

	public void removeUserById(long id);

	public void removeUserByName(String name);

	public List getAllUsers();

	public List getUsers(QueryInfo qi, PageBuilder pb);

	public boolean validatePassword(String rawPwd, String hashPwd);
	/**
	 * 请使用getAllUserCount()
	 * @deprecated 
	 * @return
	 */
	public long getUserCount();
	
	public long getUserCount(QueryInfo qi);
	
	public long getAllUserCount();

	public int getUserByNameCount(String name);

	public List getSysUsers(long start, long length, String order);

	public void addUserLoginTimes(long id);

	public void updateUserField(long id, String fieldName, Object value);

	public int getUserByEmailCount(String email);

	public IUser getUserByEmail(String email);

	public void batchUpdateUserField(String where, String fieldName,
			Object value);

	public IUser createUser();
	/**
	 * 获取指定类型与状态的用户集合
	 * @param type 用户类型，如管理员，系统用户，会员
	 * @param status 正常，停用等
	 * @return
	 */
	public List<IUser> getUsers(int type,int status);
}
