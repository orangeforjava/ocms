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

import java.util.Set;

/**
 * <p>
 * 用户接口
 * </p>
 * 
 * 
 * <p>
 * $Id: IUser.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface IUser {

	/** 系统管理员类型. */
	public static final int ADMIN_TYPE = 0x10;

	/** 系统用户类型. */
	public static final int SYS_USER_TYPE = 0x20;

	/** 会员用户类型. */
	public static final int MEMBER_TYPE = 0x1000;

	/** 匿名用户类型. */
	public static final int ANONYMOUS_TYPE = 0x01;

	/** 正常状态. */
	public static final int NORMAL_STATUS = 0;
	/** 禁用状态. */
	public static final int DISABLE_STATUS = 1;
	/** 锁定状态. */
	public static final int LOCKED_STATUS = 2;
	/** 过期状态. */
	public static final int EXPIRED_STATUS = 4;
	/** 密码过期状态. */
	public static final int CREDENTIALS_EXPIRED_STATUS = 8;

	/** 需要审核状态. */
	public static final int NEED_AUDIT_STATUS = -1;
	/** 删除状态. */
	public static final int DELETED_STATUS = -2;

	/**
	 * 得到用户id
	 * 
	 * @return
	 */
	public Long getUserId();

	/**
	 * 获得用户GUID
	 * 
	 * @return
	 */
	public String getGuid();

	/**
	 * 设置用户Id
	 * 
	 * @param id
	 */
	public void setUserId(Long id);

	/**
	 * 获得用户登录名
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 设置用户登录名
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * 得到用户密码，加密后的密码
	 * 
	 * @return
	 */
	public String getPassword();

	/**
	 * 设置用户密码
	 * 
	 * @param password
	 */
	public void setPassword(String password);

	/**
	 * 得到用户当前状态
	 * 
	 * @return
	 */
	public Integer getUserStatus();

	/**
	 * 设置用户当前状态
	 * 
	 * @param status
	 */
	public void setUserStatus(Integer status);

	/**
	 * 获得用户最后登录日期
	 * 
	 * @return
	 */
	public Long getLastLoginDate();

	/**
	 * 设置用户最后登录日期
	 * 
	 * @param lastLoginDate
	 */
	public void setLastLoginDate(Long lastLoginDate);

	/**
	 * 得到用户登录次数
	 * 
	 * @return
	 */
	public Long getLoginTimes();

	/**
	 * 得到用户类型
	 * 
	 * @return
	 */
	public Integer getType();

	/**
	 * 获得用户管理排序
	 * 
	 * @return
	 */
	public Integer getPos();

	/**
	 * 得到用户产生日期
	 * 
	 * @return
	 */
	public Long getCreationDate();

	/**
	 * 得到用户称谓
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 得到用户Email
	 * 
	 * @return
	 */
	public String getEmail();

	/**
	 * 得到用户最后修改日期
	 * 
	 * @return
	 */
	public Long getModificationDate();

	/**
	 * 得到用户最后登录Ip
	 * 
	 * @return
	 */
	public String getLastLoginIp();

	/**
	 * 得到用户昵称/真名
	 * 
	 * @return
	 */
	public String getNickName();

	/**
	 * 设置用户登录次数
	 * 
	 * @param logintimes
	 */
	public void setLoginTimes(Long logintimes);

	/**
	 * 设置用户类型
	 * 
	 * @param type
	 */
	public void setType(Integer type);

	/**
	 * 设置用户管理排序
	 * 
	 * @param sort
	 */
	public void setPos(Integer sort);

	/**
	 * 设置用户产生日期
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(Long creationDate);

	/**
	 * 设置用户称谓
	 * 
	 * @param title
	 */
	public void setTitle(String title);

	/**
	 * 设置用户电子信箱
	 * 
	 * @param email
	 */
	public void setEmail(String email);

	/**
	 * 设置用户修改日期
	 * 
	 * @param modificationDate
	 */
	public void setModificationDate(Long modificationDate);

	/**
	 * 设置用户最后登录Ip
	 * 
	 * @param lastLoginIp
	 */
	public void setLastLoginIp(String lastLoginIp);

	/**
	 * 设置用户昵称
	 * 
	 * @param nickName
	 */
	public void setNickName(String nickName);

	/**
	 * 获得用户的角色集合
	 * 
	 * @return
	 */
	public Set getRoles();

	/**
	 * 设置用户的角色集合
	 * 
	 * @param roles
	 */
	public void setRoles(Set roles);

	/**
	 * 设置用户的全局guid
	 * 
	 * @param guid
	 */
	public void setGuid(String guid);

	/**
	 * 获得手机号码
	 * 
	 * @return
	 */
	public String getMobile();

	/**
	 * 设置手机号码
	 * 
	 * @param mobile
	 *            手机号码
	 */
	public void setMobile(String mobile);

	/**
	 * 获得帐号产生来源
	 * 
	 * @return
	 */
	public String getCreatedBy();

	/**
	 * 设置帐号来源
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(String createdBy);

	/**
	 * 获得编码的会员名，主要为了解决非Ascii码编码问题
	 * 
	 * @return
	 */
	public String getEncodeUserName();

	/**
	 * 是否为临时登录
	 * 
	 * @return
	 */
	public boolean isTempLogin();
	/**
	 * 获得问题
	 * @return
	 */
	public String getQuestion();
	/**
	 * 设置问题
	 * @param answer
	 */
	public void setQuestion(String answer);
	/**
	 * 获得答案
	 * @return
	 */
	public String getAnswer();
	/**
	 * 设置答案
	 * @param answer
	 */
	public void setAnswer(String answer);
	/**
	 * 获得用户uid,这个uid是从UCenter进来的
	 * @return
	 */
	public Integer getUid();
	/**
	 * 设置用户的uid
	 * @param uid
	 */
	public void setUid(Integer uid);
}
