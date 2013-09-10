/*
 * Copyright 2002-2007 the original author or authors.
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
package org.openuap.cms.user.ui;

import org.openuap.cms.user.model.IUser;

/**
 * <p>
 * Title: UserType
 * </p>
 * 
 * <p>
 * Description:用户类型的常量数据
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: <a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: UserType.java 3918 2010-10-26 11:40:58Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class UserType {
	
	private int code;

	private String name;

	public static final UserType SYS_USER_TYPE = new UserType(IUser.SYS_USER_TYPE, "系统用户");

	public static final UserType ADMIN_USER_TYPE = new UserType(IUser.ADMIN_TYPE, "管理员");

	public static final UserType MEMBER_USER_TYPE = new UserType(IUser.MEMBER_TYPE, "会员");

	public static final UserType[] SYS_USER_TYPES = new UserType[] { ADMIN_USER_TYPE, SYS_USER_TYPE,MEMBER_USER_TYPE };

	/**
	 * 
	 * @param code
	 *            
	 * @param name
	 *            
	 */
	public UserType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}
}
