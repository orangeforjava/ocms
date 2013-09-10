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
package org.openuap.cms.user.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <p>
 * 权限对象.
 * </p>
 * 
 * <p>
 * $Id: Permissions.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class Permissions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4486119716986612821L;

	/** 所有对象类型.*/
	public static final String allObjectType = "-1";
	/** 所有对象Id.*/
	public static final String allObjectId = "-1";
	/** 所有权限.*/
	public static final long allPermissions = Long.MAX_VALUE;
	/** 没有权限.*/
	public static final long nonePermissions = 0L;
	/** 对象类型.*/
	private String objectType;
	/** 对象Id.*/
	private String objectId;
	/** 权限值.*/
	private long permissions;
	
	

	public Permissions(String objectType, String objectId, long permissions) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.permissions = permissions;
	}

	public String getObjectId() {
		return objectId;
	}

	public boolean hasPermission(String otype, String oid, long permission) {
		if (objectType.equals(allObjectType) || objectType.equals(otype)) {
			if (objectId.equals(new Integer(-1))) {
				return (permissions & permission) == permission;
			} else {
				if (objectId.equals(allObjectId) || objectId.equals(oid)) {
					return (permissions & permission) == permission;
				}
			}
		}
		return false;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getPermissions() {
		return permissions;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setPermissions(long permissions) {
		this.permissions = permissions;
	}

	public boolean equals(Object o) {
		if (o instanceof Permissions) {
			Permissions that = (Permissions) o;
			if (that.getObjectType().equals(this.objectType) && that.getObjectType().equals(this.getObjectId())
					&& that.getPermissions() == this.getPermissions()) {
				return true;
			}
		}
		return false;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}
}
