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
package org.openuap.cms.survey.engine.support;

import org.openuap.base.util.StringUtil;
import org.openuap.cms.survey.engine.SurveyEngine;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.model.Question;

/**
 * <p>
 * 调查内容引擎缺少实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultSurveyEngine.java 4086 2012-11-26 04:25:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class DefaultSurveyEngine implements SurveyEngine {
	private QuestionManager questionManager;

	public Question getQuestion(Long id) {

		return questionManager.getQuestionById(id);
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	public Question getQuestion(String id) {
		if (StringUtil.isNumber(id)) {
			return questionManager.getQuestionById(new Long(id));
		}
		return null;
	}

}
