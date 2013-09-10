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
package org.openuap.cms.resource.security;

/**
 * <p>
 * 资源权限常量定义
 * </p>
 * 
 * <p>
 * $Id: ResourcePermissionConstant.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ResourcePermissionConstant {
	//
	public static final String OBJECT_TYPE = "org.openuap.cms.res";
	public static final String ALL_OBJECT = "-1";
	public static final long Admin = Long.MAX_VALUE;
	//
	public final static long ViewResource = 1L << 1;
	public final static long UploadResource = 1L << 2;
	public final static long EditResource = 1L << 3;
	public final static long DeleteResource = 1L << 4;
	public final static long SelResource = 1L << 5;
	//
	private long viewResource = ViewResource;
	private long uploadResource = UploadResource;
	private long editResource = EditResource;
	private long deleteResource = DeleteResource;
	private long selResource = SelResource;
	//
	private String objectType = OBJECT_TYPE;

	/**
	 * 
	 */
	public ResourcePermissionConstant() {
	}

	public long getDeleteResource() {
		return deleteResource;
	}

	public long getEditResource() {
		return editResource;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getSelResource() {
		return selResource;
	}

	public long getUploadResource() {
		return uploadResource;
	}

	public long getViewResource() {
		return viewResource;
	}
}
