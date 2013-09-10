/**
 * $Id: SurveyManager.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.manager;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.model.QuestionItemRecord;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.model.SurveyRecord;

/**
 * <p>
 * Title:SurveyManager
 * </p>
 * 
 * <p>
 * Description:调查管理器接口定义
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
public interface SurveyManager {
	/**
	 * 根据名称获得调查对象
	 * 
	 * @param name
	 * @return
	 */
	public Survey getSurveyByName(String name);

	/**
	 * 根据Id获得调查对象
	 * 
	 * @param id
	 * @return
	 */
	public Survey getSurveyById(Long id);

	/**
	 * 获得所有调查对象列表
	 * 
	 * @return
	 */
	public List getAllSurveys();

	/**
	 * 按照指定的查询条件获得调查对象列表
	 * 
	 * @param qi
	 * @param pb
	 * @return
	 */
	public List getSurveys(QueryInfo qi, PageBuilder pb);
	/**
	 * 
	 * @param nodeId
	 * @param qi
	 * @param pb
	 * @return
	 */
	public List<Survey> getSurveys(Long nodeId,QueryInfo qi, PageBuilder pb);

	/**
	 * 新建调查对象
	 * 
	 * @param survey
	 * @return
	 */
	public Long addSurvey(Survey survey);

	/**
	 * 保存调查对象
	 * 
	 * @param survey
	 */
	public void saveSurvey(Survey survey);

	/**
	 * 删除调查对象
	 * 
	 * @param survey
	 */
	public void deleteSurvey(Survey survey);

	/**
	 * 根据Id删除调查对象
	 * 
	 * @param id
	 */
	public void deleteSurveyById(Long id);

	public void auditSurvey(Long id);

	public void unAuditSurvey(Long id);

	public void recycleSurvey(Long id);

	public void unRecycleSurvey(Long id);

	// ////////////////////////////////////////////
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

	// ///////////////////////////////////////////////
	/**
	 * 初始化此次调查活动需要的问题选项记录
	 * 
	 * @param surveyRecordId
	 * @param surveyId
	 */
	public void initQuestionItemRecords(Long surveyRecordId, Long surveyId);
	
	public void updateQuestionItems(Long surveyRecordId, String questionItemsId);
	
	public void updateQuestionItems2(Long surveyRecordId, String questionItemsId);
	
	public int getQuestionItemsTotalCount(Long surveyRecordId, Long questionId) ;
	
	public QuestionItemRecord getQuestionItemRecord(Long surveyRecordId,
			Long itemId);
	
	public int getQuestionAnswerTotalCount(Long surveyRecordId, Long questionId);

}
