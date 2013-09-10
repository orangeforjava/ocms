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

/**
 * 
 * <p>
 * 用户权限Id
 * </p>
 * 
 * <p>
 * $Id: UserPermissionId.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserPermissionId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4591268518030858630L;

	private String objectType;

	private String objectId;

	private Long userId;

	public UserPermissionId() {
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return this.objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof UserPermissionId)) {
			return false;
		}
		UserPermissionId castOther = (UserPermissionId) other;

		return ((this.getObjectType() == castOther.getObjectType()) || (this
				.getObjectType() != null
				&& castOther.getObjectType() != null && this.getObjectType()
				.equals(castOther.getObjectType())))
				&& ((this.getObjectId() == castOther.getObjectId()) || (this
						.getObjectId() != null
						&& castOther.getObjectId() != null && this
						.getObjectId().equals(castOther.getObjectId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null
						&& castOther.getUserId() != null && this.getUserId()
						.equals(castOther.getUserId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getObjectType() == null ? 0 : this.getObjectType()
						.hashCode());
		result = 37 * result
				+ (getObjectId() == null ? 0 : this.getObjectId().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		return result;
	}

}
