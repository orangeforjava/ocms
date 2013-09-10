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

/**
 * <p>
 * 选项答案实体.
 * </p>
 * 
 * <p>
 * $Id: AnswerItem.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class AnswerItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -430151421068070318L;
	/** 问题选项id. */
	private Long questionItemId;
	/** 投票者id. */
	private Long voterId;
	/** 调查记录id. */
	private Long surveyRecordId;
	/** 问题id. */
	private Long questionId;
	/** 调查id. */
	private Long surveyId;

	/** 问题选项输入文本. */
	private String answerItemInputText;


	/**
	 * 
	 */
	public AnswerItem() {
	}

	public Long getVoterId() {
		return voterId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public Long getQuestionItemId() {
		return questionItemId;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public String getAnswerItemInputText() {
		return answerItemInputText;
	}

	public void setVoterId(Long voterId) {
		this.voterId = voterId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public void setQuestionItemId(Long questionItemId) {
		this.questionItemId = questionItemId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public void setAnswerItemInputText(String answerItemInputText) {
		this.answerItemInputText = answerItemInputText;
	}

	public Long getSurveyRecordId() {
		return surveyRecordId;
	}

	public void setSurveyRecordId(Long surveyRecordId) {
		this.surveyRecordId = surveyRecordId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((questionItemId == null) ? 0 : questionItemId.hashCode());
		result = prime * result
				+ ((surveyRecordId == null) ? 0 : surveyRecordId.hashCode());
		result = prime * result + ((voterId == null) ? 0 : voterId.hashCode());
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
		final AnswerItem other = (AnswerItem) obj;
		if (questionItemId == null) {
			if (other.questionItemId != null)
				return false;
		} else if (!questionItemId.equals(other.questionItemId))
			return false;
		if (surveyRecordId == null) {
			if (other.surveyRecordId != null)
				return false;
		} else if (!surveyRecordId.equals(other.surveyRecordId))
			return false;
		if (voterId == null) {
			if (other.voterId != null)
				return false;
		} else if (!voterId.equals(other.voterId))
			return false;
		return true;
	}
}
