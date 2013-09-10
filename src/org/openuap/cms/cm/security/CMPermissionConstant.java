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
package org.openuap.cms.cm.security;

/**
 * <p>
 * Title: CMPermissionConstant
 * </p>
 * 
 * <p>
 * Description:内容模型权限常量.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: <a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: CMPermissionConstant.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class CMPermissionConstant {
	//
	public static final String OBJECT_TYPE ="org.openuap.cms.cm";

	public static final String ALL_OBJECT = "-1";

	public static final long Admin = Long.MAX_VALUE;

	//
	public final static long ViewModel = 1L << 1;

	public final static long AddModel = 1L << 2;

	public final static long EditModel = 1L << 3;

	public final static long DeleteModel = 1L << 4;

	public final static long UpdateModel = 1L << 5;

	public final static long ChangeStatus = 1L << 6;

	public final static long ChangeType = 1L << 7;

	//
	private long viewModel = ViewModel;

	private long addModel = AddModel;

	private long editModel = EditModel;

	private long deleteModel = DeleteModel;

	private long updateModel = UpdateModel;

	private long changeStatus = ChangeStatus;

	private long changeType = ChangeType;

	//
	private String objectType = OBJECT_TYPE;

	/**
	 * 
	 */
	public CMPermissionConstant() {
	}

	public long getAddModel() {
		return addModel;
	}

	public long getChangeStatus() {
		return changeStatus;
	}

	public long getChangeType() {
		return changeType;
	}

	public long getDeleteModel() {
		return deleteModel;
	}

	public long getEditModel() {
		return editModel;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getUpdateModel() {
		return updateModel;
	}

	public long getViewModel() {
		return viewModel;
	}
}
