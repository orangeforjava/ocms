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
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * <p>
 * 调查试题.
 * </p>
 * 
 * <p>
 * $Id: Question.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7845150217607324018L;


	/** 问题Id. */
	private Long questionId;
	/** 问题标题. */
	private String questionTitle;
	/** 问题产生日期. */
	private Long questionCreationDate;

	/** 单选-1，多选-2，填空-3，下拉选择-4,5-单选+备注,6,多选+备注 */
	private Integer questionType;
	/** 问题状态. */
	private Integer questionStatus;

	/** 0-无,1-必填,2-必须是数字,3-必须是日期格式. */
	private Integer questionInputFilter;

	/** 问题选项集合. */
	private Set<QuestionItem> questionItems;
	/** 问题产生者Id. */
	private Long creationUserId;
	/** 问题产生者姓名. */
	private String creationUserName;

	/** 投票全局GUID. */
	private String questionGuid;
	/** 问题选项数目. */
	private int questionItemCount;
	/** 问题所属的调查id. */
	private Long surveyId;

	/** 题目分组Id. */
	private Long groupId;
	/** 问题排序位置. */
	private Long questionPos;

	/** 组题目. */
	private String groupTitle;

	/** 页码id */
	private Long pageId;

	/**
	 * 
	 * @param questionTitle
	 * @param questionCreationDate
	 * @param questionType
	 * @param questionStatus
	 * @param creationUserId
	 * @param questionGuid
	 * @param questionItems
	 */
	public Question(String questionTitle, Long questionCreationDate,
			Integer questionType, Integer questionStatus, Long creationUserId,
			String questionGuid, Set questionItems) {
		// this.questionItems = new LinkedHashSet();
		this.questionTitle = questionTitle;
		this.questionCreationDate = questionCreationDate;
		this.questionType = questionType;
		this.questionStatus = questionStatus;
		this.creationUserId = creationUserId;

		this.questionItems = questionItems;
		this.questionGuid = questionGuid;
	}

	public Question() {
		questionItems = new LinkedHashSet();
		questionType = new Integer(1);
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((questionId == null) ? 0 : questionId.hashCode());
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
		final Question other = (Question) obj;
		if (questionId == null) {
			if (other.questionId != null)
				return false;
		} else if (!questionId.equals(other.questionId))
			return false;
		return true;
	}

	public Long getCreationUserId() {
		return creationUserId;
	}

	public String getCreationUserName() {
		return creationUserName;
	}

	public Long getQuestionCreationDate() {
		return questionCreationDate;
	}

	public String getQuestionGuid() {
		return questionGuid;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public int getQuestionItemCount() {
		return questionItemCount;
	}
	public Set getItems(){
		return questionItems;
	}
	public Set getQuestionItems() {
		return questionItems;
	}

	public Integer getQuestionStatus() {
		return questionStatus;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public Long getQuestionPos() {
		return questionPos;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public Integer getQuestionInputFilter() {
		return questionInputFilter;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}

	public void setQuestionItems(Set questionItems) {
		this.questionItems = questionItems;
	}

	public void setQuestionItemCount(int questionItemCount) {
		this.questionItemCount = questionItemCount;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public void setQuestionGuid(String questionGuid) {
		this.questionGuid = questionGuid;
	}

	public void setQuestionCreationDate(Long questionCreationDate) {
		this.questionCreationDate = questionCreationDate;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public void setCreationUserId(Long creationUserId) {
		this.creationUserId = creationUserId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setQuestionPos(Long questionPos) {
		this.questionPos = questionPos;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public void setQuestionInputFilter(Integer questionInputFilter) {
		this.questionInputFilter = questionInputFilter;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

}
