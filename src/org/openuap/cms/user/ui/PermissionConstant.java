package org.openuap.cms.user.ui;

/**
 * <p>
 * Title:PermissionConstant
 * </p>
 * 
 * <p>
 * Description:权限常量.
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
public class PermissionConstant {
	private long permission;

	private String name;

	private boolean hold;

	public PermissionConstant() {
	}

	public PermissionConstant(long permission, String name, boolean hold) {
		this.permission = permission;
		this.name = name;
		this.hold = hold;
	}

	public String getName() {
		return name;
	}

	public long getPermission() {
		return permission;
	}

	public boolean isHold() {
		return hold;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPermission(long permission) {
		this.permission = permission;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

}
