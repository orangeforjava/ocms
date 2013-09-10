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
import org.openuap.cms.survey.model.SurveyRecord;

/**
 * <p>
 * 调查记录DAO接口.
 * </p>
 * 
 * <p>
 * $Id: SurveyRecordDao.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface SurveyRecordDao {

	public SurveyRecord getSurveyRecordById(Long id);

	public List getSurveyRecords(QueryInfo qi, PageBuilder pb);

	public Long addSurveyRecord(SurveyRecord surveyRecord);

	public void saveSurveyRecord(SurveyRecord surveyRecord);

	public void deleteSurveyRecord(SurveyRecord surveyRecord);

	public void deleteSurveyRecordById(Long id);

	public void stopSurveyRecord(Long id);

	public void startSurveyRecord(Long id);

	public void pauseSurveyRecord(Long id);

	public void resumeSurveyRecord(Long id);

}
