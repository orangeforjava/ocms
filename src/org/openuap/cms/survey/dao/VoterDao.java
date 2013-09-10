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
import org.openuap.cms.survey.model.Answer;
import org.openuap.cms.survey.model.AnswerItem;
import org.openuap.cms.survey.model.Voter;

/**
 * <p>
 * 答题DAO接口定义
 * </p>
 * 
 * <p>
 * $Id: VoterDao.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface VoterDao {

	public Long addVoter(Voter voter);

	public Voter getVoterByName(String name);

	public void saveVoter(Voter voter);

	public void deleteVoter(Voter voter);

	public void deleteVoterById(Long voterId);

	public Voter searchVoter(String ipAddress, Long surveyId);

	public Voter getVoterById(Long voterId);

	public List getVoters(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb);

	// //////////////////////////////////////////////////////////////////
	public void addAnswer(Answer Answer);

	public void saveAnswer(Answer Answer);

	public void deleteAnswer(Answer Answer);

	public void deleteAnswerByVoter(Long voterId);

	public void deleteAnswerBySurvey(Long surveyId);

	public List getAnswer(Long surveyId, QueryInfo qi, PageBuilder pb);

	public List getAnswer(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb);

	public List getAnswerByVoter(Long voterId, QueryInfo qi, PageBuilder pb);

	public List getAnswerByQuestion(Long questionId, QueryInfo qi,
			PageBuilder pb);

	public List getAnswerByQuestion(Long questionId, Long surveyRecordId,
			QueryInfo qi, PageBuilder pb);

	public Answer getAnswerById(Long surveyRecordId, Long voterId,
			Long questionId);

	public void addAnswerItem(AnswerItem AnswerItem);

	public void saveAnswerItem(AnswerItem AnswerItem);

	public void deleteAnswerItem(Answer AnswerItem);

	public void deleteAnswerItemByVoter(Long voterId);

	public void deleteAnswerItemBySurvey(Long surveyId);

	public List getAnswerItemBySurvey(Long surveyId, QueryInfo qi,
			PageBuilder pb);

	public List getAnswerItemByVoter(Long voterId, QueryInfo qi, PageBuilder pb);

	public List getAnswerItemByItem(Long itemId, QueryInfo qi, PageBuilder pb);

	public AnswerItem getAnswerItemById(Long itemId, Long voterId, Long surveyId);

	public int getQuestionAnswerTotalCount(Long surveyRecordId, Long questionId);
}
