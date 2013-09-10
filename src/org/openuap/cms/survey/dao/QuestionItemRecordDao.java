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
package org.openuap.cms.survey.dao;

import org.openuap.cms.survey.model.QuestionItemRecord;

/**
 * <p>
 * 问题选项答题记录DAO接口定义.
 * </p>
 * 
 * <p>
 * $Id: QuestionItemRecordDao.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public interface QuestionItemRecordDao {

	public void updateQuestionItems(Long surveyRecordId, String questionItemsId);

	public void updateQuestionItems2(Long surveyRecordId, String questionItemsId);

	public void addQuestionItemRecord(QuestionItemRecord itemRecord);

	public void saveQuestionItemRecord(QuestionItemRecord itemRecord);

	public void clearQuestionItemRecord(Long surveyRecordId);

	public int getQuestionItemsTotalCount(Long surveyRecordId, Long questionId);

	public QuestionItemRecord getQuestionItemRecord(Long surveyRecordId,
			Long itemId);
}
