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
 * <p>
 * 角色权限Id
 * </p>
 * 
 * <p>
 * $Id: RolePermissionId.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class RolePermissionId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 475740527898843692L;

	private Long roleId;

	private String objectType;

	private String objectId;

	public RolePermissionId() {
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof RolePermissionId)) {
			return false;
		}
		RolePermissionId castOther = (RolePermissionId) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null
				&& castOther.getRoleId() != null && this.getRoleId().equals(
				castOther.getRoleId())))
				&& ((this.getObjectType() == castOther.getObjectType()) || (this
						.getObjectType() != null
						&& castOther.getObjectType() != null && this
						.getObjectType().equals(castOther.getObjectType())))
				&& ((this.getObjectId() == castOther.getObjectId()) || (this
						.getObjectId() != null
						&& castOther.getObjectId() != null && this
						.getObjectId().equals(castOther.getObjectId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37
				* result
				+ (getObjectType() == null ? 0 : this.getObjectType()
						.hashCode());
		result = 37 * result
				+ (getObjectId() == null ? 0 : this.getObjectId().hashCode());
		return result;
	}

}
