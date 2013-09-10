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
package org.openuap.cms.psn.security;

/**
 * <p>
 * 发布点权限常量定义
 * </p>
 * 
 * <p>
 * $Id: PsnPermissionConstant.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PsnPermissionConstant {
	//
	public static final String OBJECT_TYPE ="org.openuap.cms.psn";
	public static final String ALL_OBJECT = "-1";
	public static final long Admin = Long.MAX_VALUE;
	//
	public final static long ViewPsn = 1L << 1;
	public final static long AddPsn = 1L << 2;
	public final static long EditPsn = 1L << 3;
	public final static long DeletePsn = 1L << 4;
	public final static long CheckPsn = 1L << 5;
	public final static long ListPsn = 1L << 6;
	//
	private long viewPsn = ViewPsn;
	private long addPsn = AddPsn;
	private long editPsn = EditPsn;
	private long deletePsn = DeletePsn;
	private long checkPsn = CheckPsn;
	private long listPsn = ListPsn;
	//
	private String objectType = OBJECT_TYPE;

	/**
	 * 
	 */
	public PsnPermissionConstant() {
	}

	public long getAddPsn() {
		return addPsn;
	}

	public long getCheckPsn() {
		return checkPsn;
	}

	public long getDeletePsn() {
		return deletePsn;
	}

	public long getEditPsn() {
		return editPsn;
	}

	public long getViewPsn() {
		return viewPsn;
	}

	public String  getObjectType() {
		return objectType;
	}

	public long getListPsn() {
		return listPsn;
	}
}
