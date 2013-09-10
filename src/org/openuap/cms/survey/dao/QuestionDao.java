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

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.model.Question;

/**
 * <p>
 * 问题DAO接口.
 * </p>
 * 
 * <p>
 * $Id: QuestionDao.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface QuestionDao {

	public List getQuestions(QueryInfo qi, PageBuilder pb);

	public Question getQuestionById(Long id);

	public List getAllQuestions();

	public List searchQuestions(String key, QueryInfo qi, PageBuilder pb);

	public Long addQuestion(Question question);

	public void saveQuestion(Question question);

	public void deleteQuestion(Question question);

	public int getQuestionCount();

	public int getQuestionCount(Integer questionType);

	public int getQuestionCountBySurvey(Long surveyId);

	public List getQuestionList(String questionsId);

	public List getQuestionList(Integer questionStatus, QueryInfo qi,
			PageBuilder pb);

	public List getQuestionList(Long surveyId, QueryInfo qi, PageBuilder pb);

	public List getQuestionList(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb);

	public List getQuestionListByGroup(Long surveyId, Long groupId);

	public List getQuestionListByPage(Long surveyId, Long pageId);

	/**
	 * 根据全局Guid对象获得投票对象
	 * 
	 * @param guid
	 *            String
	 * @return Vote
	 */
	public Question getQuestionByGuid(String guid);

	public void executeHql(String hql, Object args[]);

	public List getPages(Long id);
}
