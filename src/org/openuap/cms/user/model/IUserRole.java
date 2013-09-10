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
 * 用户角色接口.
 * </p>
 * 
 * <p>
 * $Id: IUserRole.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface IUserRole {
	
	public Long getRoleId();

	public Long getUserId();

	public void setRoleId(Long roleId);

	public void setUserId(Long userId);
}
