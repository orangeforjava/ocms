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
package org.openuap.cms.keywords.security;

/**
 * <p>
 * 结点权限操作常量
 * </p>
 * 
 * 
 * <p>
 * $Id: KeywordsPermissionConstant.java 4034 2011-03-22 17:58:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class KeywordsPermissionConstant {
	public static final String OBJECT_TYPE = "org.openuap.cms.keywords";

	public static final String ALL_OBJECT = "-1";

	public static final long Admin = Long.MAX_VALUE;

	// 查看关键词
	public final static long ViewKeywords = 1L << 1;
	// 添加关键词
	public final static long AddKeywords = 1L << 2;
	//编辑关键词
	public final static long EditKeywords = 1L << 3;
	// 删除关键词
	public final static long DeleteKeywords = 1L << 4;
	
	//
	private long viewKeywords = ViewKeywords;

	private long addKeywords = AddKeywords;

	private long editKeywords = EditKeywords;

	private long delKeywords = DeleteKeywords;

	
	//
	private String objectType = OBJECT_TYPE;
	//操作员
	public static final long ROLE_MAINTAIN = ViewKeywords | AddKeywords|EditKeywords|DeleteKeywords;
	//管理员
	public static final long ROLE_ADMIN = Admin;
	
	public static final long ROLE_ANONYMOUS =0;
	
	public static final long ROLE_MEMBER =0;
	
	public static final long ROLE_INPUT =0;
	
	public static final long ROLE_EDITOR =0;
	
	public static final long ROLE_SUPER_EDITOR =ROLE_MAINTAIN;

	/**
	 * 
	 */
	public KeywordsPermissionConstant() {
	}
	
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public long getViewKeywords() {
		return viewKeywords;
	}

	public void setViewKeywords(long viewKeywords) {
		this.viewKeywords = viewKeywords;
	}

	public long getAddKeywords() {
		return addKeywords;
	}

	public void setAddKeywords(long addKeywords) {
		this.addKeywords = addKeywords;
	}

	public long getEditKeywords() {
		return editKeywords;
	}

	public void setEditKeywords(long editKeywords) {
		this.editKeywords = editKeywords;
	}

	public long getDelKeywords() {
		return delKeywords;
	}

	public void setDelKeywords(long delKeywords) {
		this.delKeywords = delKeywords;
	}
}
