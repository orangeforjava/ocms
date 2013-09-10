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
package org.openuap.cms.node.security;

/**
 * <p>
 * 结点权限操作常量
 * </p>
 * 
 * 
 * <p>
 * $Id: NodePermissionConstant.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodePermissionConstant {
	//
	public static final String OBJECT_TYPE = "org.openuap.cms.node";

	public static final String ALL_OBJECT = "-1";

	public static final long Admin = Long.MAX_VALUE;

	// 查看结点
	public final static long ViewNode = 1L << 1;
	// 添加子结点
	public final static long AddChildNode = 1L << 2;
	// 添加根结点
	public final static long AddRootNode = 1L << 3;
	// 编辑结点
	public final static long EditNode = 1L << 4;
	// 删除结点
	public final static long DeleteNode = 1L << 5;
	// 移动结点
	public final static long MoveNode = 1L << 6;
	// 清空结点
	public final static long EmptyNode = 1L << 7;
	// 结点排序
	public final static long SortNode = 1L << 8;
	// 清空回收站
	public final static long EmptyRecycleBin = 1L << 9;
	// 恢复结点
	public final static long RestoreNode = 1L << 10;
	// 查看站点权限
	public final static long ViewPermission = 1L << 11;
	//编辑站点权限
	public final static long EditPermission = 1L << 12;
	//
	private long viewNode = ViewNode;

	private long addChildNode = AddChildNode;

	private long addRootNode = AddRootNode;

	private long editNode = EditNode;

	private long deleteNode = DeleteNode;

	private long moveNode = MoveNode;

	private long emptyNode = EmptyNode;

	private long emptyRecycleBin = EmptyRecycleBin;

	private long restoreNode = RestoreNode;
	
	private long viewPermission=ViewPermission;
	
	private long editPermission=EditPermission;

	//
	private String objectType = OBJECT_TYPE;
	// 结点操作员
	public static final long ROLE_MAINTAIN = ViewNode | EditNode|ViewPermission;
	// 结点管理员
	public static final long ROLE_ADMIN = Admin;
	
	public static final long ROLE_ANONYMOUS =0;
	
	public static final long ROLE_MEMBER =0;
	
	public static final long ROLE_INPUT =0;
	
	public static final long ROLE_EDITOR =0;
	
	public static final long ROLE_SUPER_EDITOR =ROLE_MAINTAIN;

	/**
	 * 
	 */
	public NodePermissionConstant() {
	}

	public long getAddChildNode() {
		return addChildNode;
	}

	public long getAddRootNode() {
		return addRootNode;
	}

	public long getDeleteNode() {
		return deleteNode;
	}

	public long getEmptyNode() {
		return emptyNode;
	}

	public long getEditNode() {
		return editNode;
	}

	public long getMoveNode() {
		return moveNode;
	}

	public long getViewNode() {
		return viewNode;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getEmptyRecycleBin() {
		return emptyRecycleBin;
	}

	public long getRestoreNode() {
		return restoreNode;
	}

	public long getViewPermission() {
		return viewPermission;
	}

	public void setViewPermission(long viewPermission) {
		this.viewPermission = viewPermission;
	}

	public long getEditPermission() {
		return editPermission;
	}

	public void setEditPermission(long editPermission) {
		this.editPermission = editPermission;
	}
}
