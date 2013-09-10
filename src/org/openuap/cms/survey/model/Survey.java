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
package org.openuap.cms.survey.model;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Description:调查项目.
 * </p>
 * 
 * <p>
 * $Id: Survey.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class Survey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6387548443663078600L;

	public static final Integer ANIMOUS_TYPE = new Integer(0);

	public static final Integer MEMBER_TYPE = new Integer(1);

	/** 问卷Id. */
	private Long surveyId;
	/** 所属结点Id. */
	private Long nodeId;

	/** 问卷名称. */
	private String surveyName;

	/** 问卷产生日期. */
	private Long surveyCreationDate;

	/** 问卷类型，匿名-0，会员-1. */
	private Integer surveyType;

	/** 问卷描述. */
	private String surveyDescription;

	/** 问卷状态. */
	private Integer surveyStatus;

	/** 调查GUID. */
	private String surveyGuid;

	/** 问卷包含的问题. */
	private Set questions;

	/** 产生的用户Id. */
	private Long creationUserId;

	/** 产生的用户名. */
	private String creationUserName;
	/** 问卷是否被删除. */
	private Integer deleted;

	public Survey(String surveyName, Long surveyCreateDate,
			String surveyDescription, Integer groupType, Integer surveyStatus,
			String surveyGuid) {
		this.surveyName = surveyName;
		this.surveyCreationDate = surveyCreationDate;
		this.surveyType = surveyType;
		this.surveyDescription = surveyDescription;
		this.surveyStatus = surveyStatus;
		this.surveyGuid = surveyGuid;
	}

	public Survey() {
		surveyType = new Integer(0);
	}

	public Survey(Integer surveyStatus) {
		this.surveyStatus = surveyStatus;
	}

	public String toString() {
		return (new ToStringBuilder(this)).append("surveyId", getSurveyId())
				.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((surveyId == null) ? 0 : surveyId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Survey other = (Survey) obj;
		if (surveyId == null) {
			if (other.surveyId != null)
				return false;
		} else if (!surveyId.equals(other.surveyId))
			return false;
		return true;
	}

	public Integer getSurveyType() {
		return surveyType;
	}

	public Integer getSurveyStatus() {
		return surveyStatus;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public String getSurveyGuid() {
		return surveyGuid;
	}

	public String getSurveyDescription() {
		return surveyDescription;
	}

	public Long getSurveyCreationDate() {
		return surveyCreationDate;
	}

	public Set getQuestions() {
		return questions;
	}

	public String getCreationUserName() {
		return creationUserName;
	}

	public Long getCreationUserId() {
		return creationUserId;
	}

	public void setQuestions(Set questions) {
		this.questions = questions;
	}

	public void setSurveyCreationDate(Long surveyCreationDate) {
		this.surveyCreationDate = surveyCreationDate;
	}

	public void setSurveyDescription(String surveyDescription) {
		this.surveyDescription = surveyDescription;
	}

	public void setSurveyGuid(String surveyGuid) {
		this.surveyGuid = surveyGuid;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public void setSurveyStatus(Integer surveyStatus) {
		this.surveyStatus = surveyStatus;
	}

	public void setSurveyType(Integer surveyType) {
		this.surveyType = surveyType;
	}

	public void setCreationUserId(Long creationUserId) {
		this.creationUserId = creationUserId;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
}
