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
 * 答案实体.
 * </p>
 * 
 * <p>
 * $Id: Answer.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class Answer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1144791810465589869L;

	/** 投票者id. */
	private Long voterId;

	/** 问题id. */
	private Long questionId;

	/** 此答案对应的调查记录号. */
	private Long surveyRecordId;

	/** 调查id. */
	private Long surveyId;

	/** 答案. */
	private String answer;

	/** 答案输入值(可选). */
	private String answerInputText;


	public Answer() {
	}

	public Long getVoterId() {
		return voterId;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public String getAnswerInputText() {
		return answerInputText;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public void setAnswerInputText(String answerInputText) {
		this.answerInputText = answerInputText;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public void setVoterId(Long voterId) {
		this.voterId = voterId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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
				+ ((questionId == null) ? 0 : questionId.hashCode());
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
		final Answer other = (Answer) obj;
		if (questionId == null) {
			if (other.questionId != null)
				return false;
		} else if (!questionId.equals(other.questionId))
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
