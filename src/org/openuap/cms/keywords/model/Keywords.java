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
package org.openuap.cms.keywords.model;

import org.openuap.base.orm.BaseDao;
import org.openuap.base.orm.BaseEntity;

/**
 * <p>
 * 关键词实体
 * </p>
 * 
 * <p>
 * $Id: Keywords.java 4034 2011-03-22 17:58:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @ersion 4.0
 */
public class Keywords extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1077470343807479490L;

	private int id;
	/** 关键词内容. */
	private String keyword;
	/** 关键词作用范围. */
	private int scope;
	/** 关键词所属结点，若为-1，则为全局.*/
	private long nodeId;
	/** 链接URL. */
	private String url;
	/** 样式信息. */
	private String style;
	/** 是否可用,0可用，-1不可用. */
	private int status;
	/** 排序. */
	private int pos;
	/** 关键词关联的内容数目. */
	private int nums;

	private long createdDate;
	private long editedDate;

	private String createUser;
	private String editUser;
	
	/**
	 * 获取标题样式中的颜色信息,如color:#FF0000
	 * 
	 * @return
	 */
	public String getTitleColor() {
		if (style != null) {
			String[] styles = style.split(";");
			if (styles.length > 0) {
				return styles[0] + ";";
			}
		}
		return "";
	}
	/**
	 * 获取标题颜色,如#FF0000
	 * @return
	 */
	public String getColor() {
		if (style != null) {
			String[] styles = style.split(";");
			if (styles.length >= 1) {
				String[] color = styles[0].split(":");
				if (color.length > 1) {
					return color[1];
				}
			}
		}
		return "";
	}

	/**
	 * 判断标题是否加粗
	 * 
	 * @return
	 */
	public boolean isTitleBold() {
		if (style != null) {
			String[] styles = style.split(";");
			if (styles.length > 1) {
				if (styles[1].equals("font-weight:bold")) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isTitleUnderline(){
		if (style != null) {
			String[] styles = style.split(";");
			if (styles.length > 2) {
				if (styles[2].equals("text-decoration:underline")) {
					return true;
				}
			}
		}
		return false;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public long getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(long editedDate) {
		this.editedDate = editedDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getEditUser() {
		return editUser;
	}

	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public BaseDao getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOIDMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Keywords other = (Keywords) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
