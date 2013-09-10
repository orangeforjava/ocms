/**
 * $Id: SurveyManagerImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.manager.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.QuestionItemRecordDao;
import org.openuap.cms.survey.dao.SurveyDao;
import org.openuap.cms.survey.dao.SurveyRecordDao;
import org.openuap.cms.survey.dao.VoterDao;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionItem;
import org.openuap.cms.survey.model.QuestionItemRecord;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.model.SurveyRecord;

/**
 * <p>
 * Title:GroupManagerImpl
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class SurveyManagerImpl implements SurveyManager {

	private SurveyDao surveyDao;

	private SurveyRecordDao surveyRecordDao;

	private QuestionItemRecordDao questionItemRecordDao;

	private VoterDao voterDao;

	public SurveyManagerImpl() {
	}

	public Survey getSurveyByName(String name) {
		return surveyDao.getSurveyByName(name);
	}

	public Survey getSurveyById(Long id) {
		return surveyDao.getSurveyById(id);
	}

	public List getAllSurveys() {
		return surveyDao.getAllSurveys();
	}

	public List getSurveys(QueryInfo qi, PageBuilder pb) {
		return surveyDao.getSurveys(qi, pb);
	}

	public Long addSurvey(Survey survey) {
		return surveyDao.addSurvey(survey);
	}

	public void saveSurvey(Survey survey) {
		surveyDao.saveSurvey(survey);
	}

	public void deleteSurvey(Survey survey) {
		surveyDao.deleteSurvey(survey);
	}

	public void deleteSurveyById(Long id) {
		surveyDao.deleteSurveyById(id);
	}

	public void setSurveyDao(SurveyDao surveyDao) {
		this.surveyDao = surveyDao;
	}

	public void setQuestionItemRecordDao(
			QuestionItemRecordDao questionItemRecordDao) {
		this.questionItemRecordDao = questionItemRecordDao;
	}

	public void auditSurvey(Long id) {
		surveyDao.auditSurvey(id);
	}

	public void recycleSurvey(Long id) {
		surveyDao.recycleSurvey(id);
	}

	public void unAuditSurvey(Long id) {
		surveyDao.unAuditSurvey(id);
	}

	public void unRecycleSurvey(Long id) {
		surveyDao.unRecycleSurvey(id);
	}

	// //////////////////////////////////////////////////////////
	public Long addSurveyRecord(SurveyRecord surveyRecord) {
		return surveyRecordDao.addSurveyRecord(surveyRecord);
	}

	public void deleteSurveyRecord(SurveyRecord surveyRecord) {
		surveyRecordDao.deleteSurveyRecord(surveyRecord);
	}

	public void deleteSurveyRecordById(Long id) {
		surveyRecordDao.deleteSurveyRecordById(id);
	}

	public SurveyRecord getSurveyRecordById(Long id) {
		return surveyRecordDao.getSurveyRecordById(id);
	}

	public List getSurveyRecords(QueryInfo qi, PageBuilder pb) {
		return surveyRecordDao.getSurveyRecords(qi, pb);
	}

	public void pauseSurveyRecord(Long id) {
		surveyRecordDao.pauseSurveyRecord(id);

	}

	public void resumeSurveyRecord(Long id) {
		surveyRecordDao.resumeSurveyRecord(id);

	}

	public void saveSurveyRecord(SurveyRecord surveyRecord) {
		surveyRecordDao.saveSurveyRecord(surveyRecord);

	}

	public void startSurveyRecord(Long id) {
		surveyRecordDao.startSurveyRecord(id);

	}

	public void stopSurveyRecord(Long id) {
		surveyRecordDao.stopSurveyRecord(id);

	}

	public SurveyRecordDao getSurveyRecordDao() {
		return surveyRecordDao;
	}

	public void setSurveyRecordDao(SurveyRecordDao surveyRecordDao) {
		this.surveyRecordDao = surveyRecordDao;
	}

	public void initQuestionItemRecords(Long surveyRecordId, Long surveyId) {
		Survey survey = surveyDao.getSurveyById(surveyId);
		if (survey != null) {
			Set questions = survey.getQuestions();
			if (questions != null) {
				Iterator itQuestions = questions.iterator();
				while (itQuestions.hasNext()) {
					Question question = (Question) itQuestions.next();
					Set questionItems = question.getQuestionItems();
					if (questionItems != null) {
						Iterator itQuestionItems = questionItems.iterator();
						while (itQuestionItems.hasNext()) {
							QuestionItem item = (QuestionItem) itQuestionItems
									.next();
							Long itemId = item.getQuestionItemId();
							Long questionId = item.getQuestion()
									.getQuestionId();
							QuestionItemRecord itemRecord = questionItemRecordDao
									.getQuestionItemRecord(surveyRecordId,
											itemId);
							if (itemRecord != null) {
								itemRecord.setQuestionId(questionId);
								itemRecord.setQuestionItemPolledTimes(0);
								itemRecord.setQuestionItemId(itemId);
								itemRecord.setSurveyRecordId(surveyRecordId);
								questionItemRecordDao
										.saveQuestionItemRecord(itemRecord);
							} else {
								itemRecord = new QuestionItemRecord();
								itemRecord.setQuestionId(questionId);
								itemRecord.setQuestionItemPolledTimes(0);
								itemRecord.setQuestionItemId(itemId);
								itemRecord.setSurveyRecordId(surveyRecordId);
								questionItemRecordDao
										.addQuestionItemRecord(itemRecord);
							}
							//
						}
					}
				}
			}
		}
	}

	public void updateQuestionItems(Long surveyRecordId, String questionItemsId) {
		questionItemRecordDao.updateQuestionItems(surveyRecordId,
				questionItemsId);

	}

	public void updateQuestionItems2(Long surveyRecordId, String questionItemsId) {
		questionItemRecordDao.updateQuestionItems2(surveyRecordId,
				questionItemsId);

	}

	public int getQuestionItemsTotalCount(Long surveyRecordId, Long questionId) {
		return questionItemRecordDao.getQuestionItemsTotalCount(surveyRecordId,
				questionId);
	}

	public QuestionItemRecord getQuestionItemRecord(Long surveyRecordId,
			Long itemId) {
		return questionItemRecordDao.getQuestionItemRecord(surveyRecordId,
				itemId);
	}

	public int getQuestionAnswerTotalCount(Long surveyRecordId, Long questionId) {
		return voterDao.getQuestionAnswerTotalCount(surveyRecordId, questionId);
	}

	public void setVoterDao(VoterDao voterDao) {
		this.voterDao = voterDao;
	}

	public List<Survey> getSurveys(Long nodeId, QueryInfo qi, PageBuilder pb) {
		return surveyDao.getSurveys(nodeId, qi, pb);
	}
}
