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
 * 每个问题选项的答题记录
 * 
 * <p>
 * $Id: QuestionItemRecord.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class QuestionItemRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2271288857041618328L;
	/** 对应的问题选项Id.*/
	private Long questionItemId;
	/** 调查记录ID.*/
	private Long surveyRecordId;
	
	/** 所属问题ID.*/
	private Long questionId;

	/** 选项被投票的次数. */
	private Integer questionItemPolledTimes;

	public Long getQuestionItemId() {
		return questionItemId;
	}

	public void setQuestionItemId(Long questionItemId) {
		this.questionItemId = questionItemId;
	}

	public Long getSurveyRecordId() {
		return surveyRecordId;
	}

	public void setSurveyRecordId(Long surveyRecordId) {
		this.surveyRecordId = surveyRecordId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Integer getQuestionItemPolledTimes() {
		return questionItemPolledTimes;
	}

	public void setQuestionItemPolledTimes(Integer questionItemPolledTimes) {
		this.questionItemPolledTimes = questionItemPolledTimes;
	}

}
