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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 问卷答案实体.
 * </p>
 * 
 * <p>
 * $Id: SurveyAnswer.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SurveyAnswer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4481308125153434505L;

	private Long surveyId;

	private Map questionAnswers;

	private Map questionItemAnswers;

	private Long answerDate;

	private Voter voter;

	/**
	 * 
	 */
	public SurveyAnswer() {
		//
		questionAnswers = Collections.synchronizedMap(new HashMap());
		questionItemAnswers = Collections.synchronizedMap(new HashMap());
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public Map getQuestionItemAnswers() {
		return questionItemAnswers;
	}

	public Map getQuestionAnswers() {
		return questionAnswers;
	}

	public Long getAnswerDate() {
		return answerDate;
	}

	public Voter getVoter() {
		return voter;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public void setAnswerDate(Long answerDate) {
		this.answerDate = answerDate;
	}

	public void setVoter(Voter voter) {
		this.voter = voter;
	}

	public void setQuestionItemAnswers(Map questionItemAnswers) {
		this.questionItemAnswers = questionItemAnswers;
	}

	public void setQuestionAnswers(Map questionAnswers) {
		this.questionAnswers = questionAnswers;
	}
}
