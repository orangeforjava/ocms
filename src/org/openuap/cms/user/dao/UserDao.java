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
package org.openuap.cms.user.dao;

import java.util.List;

import org.openuap.base.dao.hibernate.PolymorphicDao;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.user.model.IUser;

/**
 * <p>
 * 用户DAO接口.
 * </p>
 * 
 * <p>
 * $Id: UserDao.java 3964 2010-12-09 15:23:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface UserDao extends PolymorphicDao{
	/**
	 * 通过用户id获得用户
	 * @param id
	 * @return
	 */
	public IUser getUserById(Long id);
	/**
	 * 添加用户
	 * @param baseUser
	 * @return
	 */
	public Long addUser(IUser baseUser);
	/**
	 * 保存用户
	 * @param user
	 */
	public void saveUser(IUser user);
	/**
	 * 根据用户名获得用户
	 * @param name
	 * @return
	 */
	public IUser getUserByName(String name);
	

	/**
	 * get the baseuser by the username and eid
	 * 
	 * @param name
	 *            用户名
	 * @param eid
	 *            EID
	 * @return AbstractUser
	 */
	public IUser getUserByNameAndEid(String name, Long eid);
	/**
	 * 删除用户
	 * @param baseUser
	 */
	public void removeUser(IUser baseUser);
	/**
	 * 通过id删除用户
	 * @param id
	 */
	public void removeUserById(Long id);
	/**
	 * 通过用户名删除用户
	 * @param name
	 */
	public void removeUserByName(String name);
	/**
	 * 获得所有用户
	 * @return
	 */
	public List getAllUsers();

	/**
	 * 
	 * 获得用户列表
	 * @param qi
	 *            QueryInfo
	 * @param pb
	 *            PageBuilder
	 * @return List
	 */
	public List getUsers(QueryInfo qi, PageBuilder pb);
	/**
	 * 获得所有用户数量
	 * @return
	 */
	public long getAllUserCount();
	/**
	 * 获得用户数目
	 * @param qi
	 * @return
	 */
	public long getUserCount(QueryInfo qi);
	/**
	 * 获得所有用户数量
	 * @deprecated
	 * @return
	 */
	public long getUserCount();
	/**
	 * 
	 * 
	 * @param name
	 *            String
	 * @return int
	 */
	public int getUserByNameCount(String name);

	public int getUserByEmailCount(String email);

	public IUser getUserByEmail(String email);

	public List getSysUsers(Long start, Long length, String order);

	public void addUserLoginTimes(Long id);

	public void updateUserField(Long id, String fieldName, Object value);

	public void batchUpdateUserField(String where, String fieldName, Object value);
	
	/**
	 * 获取指定类型与状态的用户集合
	 * @param type 用户类型，如管理员，系统用户，会员
	 * @param status 正常，停用等
	 * @return
	 */
	public List<IUser> getUsers(int type,int status);
}
