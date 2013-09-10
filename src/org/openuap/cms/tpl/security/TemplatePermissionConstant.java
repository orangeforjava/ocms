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
package org.openuap.cms.tpl.security;

/**
 * <p>
 * 模板权限常量.
 * </p>
 * 
 * <p>
 * $Id: TemplatePermissionConstant.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplatePermissionConstant {

	public static final String OBJECT_TYPE ="org.openuap.cms.tpl";
	public static final String ALL_OBJECT = "-1";
	public static final long Admin = Long.MAX_VALUE;
	//
	public final static long ViewTpl = 1L << 1;
	public final static long AddTpl = 1L << 2;
	public final static long EditTpl = 1L << 3;
	public final static long DeleteTpl = 1L << 4;
	public final static long SelTpl = 1L << 5;
	public final static long ChangeTplName = 1L << 6;
	public final static long UploadTpl = 1L << 7;
	public final static long MkDir = 1L << 8;
	public final static long Copy = 1L << 9;
	public final static long Cut = 1L << 10;
	public final static long ChangeDirName = 1L << 11;
	public final static long DeleteDir = 1L << 12;
	//
	private long viewTpl = ViewTpl;
	private long addTpl = AddTpl;
	private long editTpl = EditTpl;
	private long deleteTpl = DeleteTpl;
	private long selTpl = SelTpl;
	private long changeTplName = ChangeTplName;
	private long uploadTpl = UploadTpl;
	private long mkDir = MkDir;
	private long copy = Copy;
	private long cut = Cut;
	private long changeDirName = ChangeDirName;
	private long deleteDir = DeleteDir;
	//
	private String objectType = OBJECT_TYPE;

	//
	public TemplatePermissionConstant() {
	}

	public long getAddTpl() {
		return addTpl;
	}

	public long getChangeDirName() {
		return changeDirName;
	}

	public long getChangeTplName() {
		return changeTplName;
	}

	public long getCopy() {
		return copy;
	}

	public long getCut() {
		return cut;
	}

	public long getDeleteDir() {
		return deleteDir;
	}

	public long getDeleteTpl() {
		return deleteTpl;
	}

	public long getEditTpl() {
		return editTpl;
	}

	public long getMkDir() {
		return mkDir;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getSelTpl() {
		return selTpl;
	}

	public long getUploadTpl() {
		return uploadTpl;
	}

	public long getViewTpl() {
		return viewTpl;
	}
}
