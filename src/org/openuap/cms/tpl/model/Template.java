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
package org.openuap.cms.tpl.model;

import org.openuap.base.dao.hibernate.*;

/**
 * 
 * <p>
 * 模板实体.
 * </p>
 * 
 * 
 * <p>
 * $Id: Template.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class Template extends BaseObject implements java.io.Serializable {

	// Constructors
	private int hashValue = 0;

	private Long id;

	private Long tcid;

	private String tplName;

	private String tplTitle;

	private String tplImg;

	private String tplPath;

	private String tplContent;

	private String tplIntro;

	private Long tplType;

	private Long tplStatus;

	private Long tplSort;

	private Long tplTop;

	private Long tplPink;

	private Long creationUid;

	private String creationUserName;

	private Long lastModifiedUid;

	private Long creationDate;

	private Long modifiedDate;

	// Constructors

	/** default constructor */
	public Template() {
	}

	/**
	 * minimal constructor
	 * 
	 * @param tplName
	 *            String
	 * @param tplPath
	 *            String
	 * @param tplType
	 *            long
	 * @param tplStatus
	 *            long
	 * @param creationUid
	 *            long
	 * @param lastModifiedUid
	 *            long
	 * @param creationDate
	 *            long
	 * @param modifiedDate
	 *            long
	 */
	public Template(String tplName, String tplPath, Long tplType,
			Long tplStatus, Long creationUid, Long lastModifiedUid,
			Long creationDate, Long modifiedDate) {
		this.tplName = tplName;
		this.tplPath = tplPath;
		this.tplType = tplType;
		this.tplStatus = tplStatus;
		this.creationUid = creationUid;
		this.lastModifiedUid = lastModifiedUid;
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
	}

	/**
	 * full constructor
	 * 
	 * @param tplName
	 *            String
	 * @param tplTitle
	 *            String
	 * @param tplImg
	 *            String
	 * @param tplPath
	 *            String
	 * @param tplContent
	 *            String
	 * @param tplIntro
	 *            String
	 * @param tplType
	 *            long
	 * @param tplStatus
	 *            long
	 * @param creationUid
	 *            long
	 * @param lastModifiedUid
	 *            long
	 * @param creationDate
	 *            long
	 * @param modifiedDate
	 *            long
	 */
	public Template(String tplName, String tplTitle, String tplImg,
			String tplPath, String tplContent, String tplIntro, Long tplType,
			Long tplStatus, Long creationUid, Long lastModifiedUid,
			Long creationDate, Long modifiedDate) {
		this.tplName = tplName;
		this.tplTitle = tplTitle;
		this.tplImg = tplImg;
		this.tplPath = tplPath;
		this.tplContent = tplContent;
		this.tplIntro = tplIntro;
		this.tplType = tplType;
		this.tplStatus = tplStatus;
		this.creationUid = creationUid;
		this.lastModifiedUid = lastModifiedUid;
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Template)) {
			return false;
		}
		Template that = (Template) o;
		if (this.getId().longValue() == 0 || that.getId().longValue() == 0) {
			return false;
		}
		return (this.getId() == (that.getId()));

	}

	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int idValue = this.getId().hashCode();
			result = result * 37 + idValue;
			this.hashValue = result;
		}
		return this.hashValue;

	}

	public Long getCreationDate() {
		return creationDate;
	}

	public Long getCreationUid() {
		return creationUid;
	}

	public String getCreationUserName() {
		return creationUserName;
	}

	public int getHashValue() {
		return hashValue;
	}

	public Long getLastModifiedUid() {
		return lastModifiedUid;
	}

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public Long getTcid() {
		return tcid;
	}

	public String getTplContent() {
		return tplContent;
	}

	public String getTplIntro() {
		return tplIntro;
	}

	public String getTplImg() {
		return tplImg;
	}

	public String getTplName() {
		return tplName;
	}

	public String getTplPath() {
		return tplPath;
	}

	public Long getTplPink() {
		return tplPink;
	}

	public Long getTplSort() {
		return tplSort;
	}

	public Long getTplStatus() {
		return tplStatus;
	}

	public String getTplTitle() {
		return tplTitle;
	}

	public Long getTplTop() {
		return tplTop;
	}

	public Long getTplType() {
		return tplType;
	}

	public Long getId() {
		return id;
	}

	public void setTplType(Long tplType) {
		this.tplType = tplType;
	}

	public void setTplTop(Long tplTop) {
		this.tplTop = tplTop;
	}

	public void setTplTitle(String tplTitle) {
		this.tplTitle = tplTitle;
	}

	public void setTplStatus(Long tplStatus) {
		this.tplStatus = tplStatus;
	}

	public void setTplSort(Long tplSort) {
		this.tplSort = tplSort;
	}

	public void setTplPink(Long tplPink) {
		this.tplPink = tplPink;
	}

	public void setTplPath(String tplPath) {
		this.tplPath = tplPath;
	}

	public void setTplName(String tplName) {
		this.tplName = tplName;
	}

	public void setTplIntro(String tplIntro) {
		this.tplIntro = tplIntro;
	}

	public void setTplImg(String tplImg) {
		this.tplImg = tplImg;
	}

	public void setTplContent(String tplContent) {
		this.tplContent = tplContent;
	}

	public void setTcid(Long tcid) {
		this.tcid = tcid;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setLastModifiedUid(Long lastModifiedUid) {
		this.lastModifiedUid = lastModifiedUid;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public void setCreationUid(Long creationUid) {
		this.creationUid = creationUid;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
