/**
 * $Id: UserStatus.java 3918 2010-10-26 11:40:58Z orangeforjava $
 */
package org.openuap.cms.user.ui;

import org.openuap.cms.user.model.IUser;

/**
 * <p>
 * Title:UserStatus
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: <a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class UserStatus {
	
	private int code;

	private String name;

	public static final UserStatus NORMAL_STATUS = new UserStatus(0, "正常");

	public static final UserStatus DISABLED_STATUS = new UserStatus(IUser.DISABLE_STATUS, "停用");

	public static final UserStatus LOCKED_STATUS = new UserStatus(IUser.LOCKED_STATUS, "锁定");

	public static final UserStatus EXPIRED_STATUS = new UserStatus(IUser.EXPIRED_STATUS, "过期");

	public static final UserStatus[] ALL_USER_STATUS = new UserStatus[] { NORMAL_STATUS, DISABLED_STATUS,
			LOCKED_STATUS, EXPIRED_STATUS };

	public UserStatus(int code, String name) {
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
