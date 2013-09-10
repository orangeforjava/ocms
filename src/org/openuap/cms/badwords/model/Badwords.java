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
package org.openuap.cms.badwords.model;

import org.openuap.base.orm.BaseDao;
import org.openuap.base.orm.BaseEntity;
import org.openuap.runtime.util.ObjectLocator;

/**
 * <p>
 * 敏感词内容模型
 * </p>
 * 
 * <p>
 * $Id: Badwords.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0 
 */
public class Badwords extends BaseEntity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4728093256151133455L;

	public static final int SCOPE_ALL = -1;

	public static final int SCOPE_SIGNATURE = 3;

	public static final int SCOPE_REG = 2;

	public static final int SCOPE_POST = 1;

	public static final int TYPE_HIGHLIGNT = 0;

	public static final int TYPE_REPLACEMENT = 1;

	private int id;

	/** 敏感词项的title. */
	private String title;

	/** 敏感词作用范围-全局-1，签名3，注册2，贴文1. */
	private int scope;

	/** 查找的敏感词. */
	private String find;

	/** 替换的结果,支持perl5语法. */
	private String replacement;

	/** 敏感词类型,套红0，替换1. */
	private int type;

	/** 是否可用,0可用，-1不可用. */
	private int status;

	/** 排序. */
	private int pos;

	public Badwords() {
	}

	public BaseDao getDao() {
		return (BaseDao) ObjectLocator.lookup("badWordsDao");
	}

	public String getFind() {
		return find;
	}

	public int getId() {
		return id;
	}

	public int getScope() {
		return scope;
	}

	public int getType() {
		return type;
	}

	public int getStatus() {
		return status;
	}

	public String getReplacement() {
		return replacement;
	}

	public String getTitle() {
		return title;
	}

	public int getPos() {
		return pos;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFind(String find) {
		this.find = find;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	
	public String getOIDMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getTable() {
		// TODO Auto-generated method stub
		return null;
	}
}
