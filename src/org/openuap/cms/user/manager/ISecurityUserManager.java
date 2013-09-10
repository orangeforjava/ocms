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

import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.IUserSession;

/**
 * <p>
 * 用户安全管理接口，从一般的用户接口中分离
 * </p>
 * 
 * <p>
 * $Id: ISecurityUserManager.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ISecurityUserManager {
	/**
	 * 根据用户名获得用户Session对象
	 * @param userName
	 * @return
	 */
	public IUserSession getUserSessionByUserName(String userName);
	/**
	 * 更新用户登录信息
	 * @param mid
	 * @param ip
	 * @param loginDate
	 */
	public void updateLoginInfo(String uid, String ip, Long loginDate);
	
	/**
	 * 根据用户对象生成UserSession对象
	 * @param user
	 * @return
	 */
	public IUserSession getUserSession(IUser user);
}
