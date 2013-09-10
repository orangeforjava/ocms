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
package org.openuap.cms.user.security.permissions;

/**
 * <p>
 * 用户权限定义常量
 * </p>
 * 
 * <p>
 * $Id: UserPermissionConstant.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserPermissionConstant {
	//资源类型
	public static final String OBJECT_TYPE = "org.openuap.cms.user";

	public static final String ALL_OBJECT = "-1";

	//管理员权限
	public static final long Admin = Long.MAX_VALUE;

	// public final static int Admin = 1 << 0;
	public final static long ViewUser = 1L << 1;

	public final static long AddUser = 1L << 2;

	public final static long EditUser = 1L << 3;

	public final static long DeleteUser = 1L << 4;

	//
	public final static long ViewRole = 1L << 5;

	public final static long AddRole = 1L << 6;

	public final static long EditRole = 1L << 7;

	public final static long DeleteRole = 1L << 8;

	//
	public final static long ViewUserPermission = 1L << 9;

	public final static long EditUserPermission = 1L << 10;

	//
	public final static long ViewRolePermission = 1L << 11;

	public final static long EditRolePermission = 1L << 12;

	//
	public final static long ViewMember = 1L << 13;

	public final static long AddMember = 1L << 14;

	public final static long EditMember = 1L << 15;

	public final static long DeleteMember = 1L << 16;

	public final static long AuditMember = 1L << 17;

	public final static long ChangeMemberRank = 1L << 18;

	public final static long ViewUserRole = 1L << 19;

	public final static long ViewRoleUser = 1L << 20;

	public final static long AddUserToRole = 1L << 21;

	public final static long AddRoleToUser = 1L << 22;

	//
	private long viewUser = ViewUser;

	private long addUser = AddUser;

	private long editUser = EditUser;

	private long deleteUser = DeleteUser;

	//
	private long viewRole = ViewRole;

	private long addRole = AddRole;

	private long editRole = EditRole;

	private long deleteRole = DeleteRole;

	//
	private long viewUserPermission = ViewUserPermission;

	private long editUserPermission = EditUserPermission;

	//
	private long viewRolePermission = ViewRolePermission;

	private long editRolePermission = EditRolePermission;

	//
	private long viewMember = ViewMember;

	private long addMember = AddMember;

	private long editMember = EditMember;

	private long deleteMember = DeleteMember;

	private long auditMember = AuditMember;

	private long changeMemberRank = ChangeMemberRank;

	//
	private long viewUserRole = ViewUserRole;

	private long viewRoleUser = ViewRoleUser;

	//
	private long addUserToRole = AddUserToRole;

	private long addRoleToUser = AddRoleToUser;

	//
	private String objectType = OBJECT_TYPE;

	//
	public long getAddUser() {
		return addUser;
	}

	public long getViewUser() {
		return viewUser;
	}

	public long getAddMember() {
		return addMember;
	}

	public long getAddRole() {
		return addRole;
	}

	public long getAuditMember() {
		return auditMember;
	}

	public long getChangeMemberRank() {
		return changeMemberRank;
	}

	public long getDeleteMember() {
		return deleteMember;
	}

	public long getDeleteRole() {
		return deleteRole;
	}

	public long getDeleteUser() {
		return deleteUser;
	}

	public long getEditMember() {
		return editMember;
	}

	public long getEditRole() {
		return editRole;
	}

	public long getEditRolePermission() {
		return editRolePermission;
	}

	public long getEditUser() {
		return editUser;
	}

	public long getEditUserPermission() {
		return editUserPermission;
	}

	public long getViewMember() {
		return viewMember;
	}

	public long getViewRole() {
		return viewRole;
	}

	public long getViewRolePermission() {
		return viewRolePermission;
	}

	public long getViewUserPermission() {
		return viewUserPermission;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getViewRoleUser() {
		return viewRoleUser;
	}

	public long getViewUserRole() {
		return viewUserRole;
	}

	public long getAddUserToRole() {
		return addUserToRole;
	}

	public long getAddRoleToUser() {
		return addRoleToUser;
	}
	public static void main(String[] args) {
		System.out.println("1<<6="+(1<<6));
	}
}
