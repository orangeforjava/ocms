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
import org.openuap.cms.survey.model.Survey;

/**
 * <p>
 * 调查DAO接口.
 * </p>
 * 
 * <p>
 * $Id: SurveyDao.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface SurveyDao {

	public Survey getSurveyByName(String name);

	public Survey getSurveyById(Long id);

	public List getAllSurveys();

	public List getSurveys(QueryInfo qi, PageBuilder pb);

	public List<Survey> getSurveys(Long nodeId, QueryInfo qi, PageBuilder pb);

	public Long addSurvey(Survey survey);

	public void saveSurvey(Survey survey);

	public void deleteSurvey(Survey survey);

	public void deleteSurveyById(Long id);

	/**
	 * 根据全局
	 * 
	 * @param guid
	 *            String
	 * @return Survey
	 */
	public Survey getSurveyByGuid(String guid);

	public void auditSurvey(Long id);

	public void unAuditSurvey(Long id);

	public void recycleSurvey(Long id);

	public void unRecycleSurvey(Long id);

}
