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

import java.util.List;

import org.openuap.cms.survey.model.QuestionItem;

/**
 * <p>
 * 问题选项DAO.
 * </p>
 * 
 * <p>
 * $Id: QuestionItemDao.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface QuestionItemDao {

	public List getQuestionItems(Long questionId);

	public QuestionItem getQuestionItemById(Long itemId);

	public Long addQuestionItem(QuestionItem questionItem);

	public void saveQuestionItem(QuestionItem questionItem);

	public void deleteQuestionItem(QuestionItem questionItem);

	public void deleteQuestionItems(Long questionId);

	public void deleteQuestionItemById(Long questionId);

	public int getQuestionItemsTotalCount(Long questionId);

	public void updateQuestionItems(String questionItemsId);

	public void updateQuestionItems2(String questionItemsId);

	public int getItemsCount(Long questionId);

	public void executeHql(String hql, Object[] args);

}
