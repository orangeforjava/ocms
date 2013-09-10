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
 * $Id: PermissionObject.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public class PermissionObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1125113191443532815L;
	/**
	 * 对象类型
	 */
	private String objectType;
	/**
	 * 对象Id
	 */
	private String objectId;

	public PermissionObject() {
	}

	public PermissionObject(String objectType, String objectId) {
		this.objectType = objectType;
		this.objectId = objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public boolean equals(Object obj) {
		if (obj != null & obj instanceof PermissionObject) {
			PermissionObject that = (PermissionObject) obj;
			if (that.getObjectType().equals(this.getObjectType()) && that.getObjectId().equals(this.getObjectId())) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + (getObjectType() == null ? 0 : this.getObjectType().hashCode());
		result = 37 * result + (getObjectId() == null ? 0 : this.getObjectId().hashCode());
		return result;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

}
