/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.config.security;

/**
 * <p>
 * 系统配置权限常量定义.
 * </p>
 * 
 * <p>
 * $Id: ConfigPermissionConstant.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public class ConfigPermissionConstant {
	//
	public static final String OBJECT_TYPE ="org.openuap.cms.config";

	public static final String ALL_OBJECT ="-1";

	public static final long Admin = Long.MAX_VALUE;

	//
	public final static long ViewConfig = 1L << 1;

	public final static long EditConfig = 1L << 2;

	/** 查看配置.*/
	private long viewConfig = ViewConfig;
	/** 编辑配置.*/
	private long editConfig = EditConfig;

	private String objectType = OBJECT_TYPE;

	/**
	 * 
	 */
	public ConfigPermissionConstant() {
	}

	public long getEditConfig() {
		return editConfig;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getViewConfig() {
		return viewConfig;
	}
}
