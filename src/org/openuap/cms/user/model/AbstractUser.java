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
package org.openuap.cms.user.model;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.openuap.base.dao.hibernate.BaseObject;

/**
 * 
 * <p>
 * 抽象用户类.
 * </p>
 * 
 * <p>
 * $Id: AbstractUser.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractUser extends BaseObject implements IUser,
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2096757466573749731L;

	private int hashValue = 0;

	/** 用户所属的角色集合. */
	private Set<AbstractRole> roles;

	/** 用户Id. */
	private Long id;
	/** 用户GUID. */
	private String guid;
	/** 父账号id. */
	private Long parentId;
	/** 企业帐号. */
	private String eid;
	/** 用户手机号码. */
	private String mobile;
	/** 用户名. */
	private String name;
	/** 用户密码. */
	private String password;
	/** 用户email密码. */
	private String email;
	/** 用户称谓. */
	private String title;
	/** 用户昵称. */
	private String nickName;
	/** 用户状态. */
	private Integer status;
	/** 帐号类型. */
	private Integer type;
	/** 管理用排序位置. */
	private Integer pos;
	/** 用户创建来源. */
	private String createdBy;
	/** 用户创建日期. */
	private Long creationDate;
	/** 用户修改日期. */
	private Long modificationDate;
	/** 用户最后登录日期. */
	private Long lastLoginDate;
	/** 用户登录次数. */
	private Long loginTimes;
	/** 用户最后登录ip. */
	private String lastLoginIp;
	/** 提示问题.*/
	private String question;
	/** 密码答案.*/
	private String answer;
	
	private Integer uid;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public AbstractUser() {
	}

	/**
	 * constructor with id
	 * 
	 * @param id
	 * 
	 */
	public AbstractUser(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Long lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Long getLoginTimes() {
		return this.loginTimes;
	}

	public Integer getType() {
		return type;
	}

	public Integer getPos() {
		return pos;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public String getTitle() {
		return title;
	}

	public String getEmail() {
		return email;
	}

	public Long getModificationDate() {
		return modificationDate;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public String getNickName() {
		return nickName;
	}

	public void setLoginTimes(Long loginTimes) {
		this.loginTimes = loginTimes;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setModificationDate(Long modificationDate) {
		this.modificationDate = modificationDate;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof AbstractRole)) {
			return false;
		}
		AbstractUser that = (AbstractUser) o;
		if (this.getId() == 0 || that.getId() == 0) {
			return false;
		}
		return (this.getId() == that.getId());

	}

	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int idValue = this.getId() == 0 ? 0 : (new Long(this.getId())
					.hashCode());
			result = result * 37 + idValue;
			this.hashValue = result;
		}
		return this.hashValue;

	}

	public Set getRoles() {
		return roles;
	}

	public String getEid() {
		return eid;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setRoles(Set roles) {
		this.roles = roles;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setHashValue(int hashValue) {
		this.hashValue = hashValue;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Long getUserId() {
		return this.id;
	}

	public Integer getUserStatus() {
		return this.status;
	}

	public void setUserId(Long id) {
		this.id = id;

	}

	public void setUserStatus(Integer status) {
		this.status = status;

	}

	public Long getUserLoginTimes() {
		return this.loginTimes;
	}

	public void setUserLoginTimes(Long logintimes) {
		this.loginTimes = logintimes;

	}

	public String getEncodeUserName() {
		try {
			return java.net.URLEncoder.encode(this.getName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return this.getName();
		}
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
