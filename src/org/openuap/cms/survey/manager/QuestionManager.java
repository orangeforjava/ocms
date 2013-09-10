/**
 *  
 */
package org.openuap.cms.survey.manager;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.model.Answer;
import org.openuap.cms.survey.model.AnswerItem;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionGroup;
import org.openuap.cms.survey.model.QuestionItem;
import org.openuap.cms.survey.model.QuestionPage;
import org.openuap.cms.survey.model.SurveyAnswer;
import org.openuap.cms.survey.model.Voter;

/**
 * <p>
 * 调查问题管理器.
 * </p>
 * 
 * <p>
 * $Id: QuestionManager.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface QuestionManager {
	/**
	 * 获得调查问题
	 * 
	 * @param qi
	 * @param pb
	 * @return
	 */
	public List getQuestions(QueryInfo qi, PageBuilder pb);

	/**
	 * 根据Id获得问题
	 * 
	 * @param id
	 * @return
	 */
	public Question getQuestionById(Long id);

	/**
	 * 获得所有问题
	 * 
	 * @return
	 */
	public List getAllQuestions();

	/**
	 * 搜索问题
	 * 
	 * @param key
	 * @param qi
	 * @param pb
	 * @return
	 */
	public List searchQuestions(String key, QueryInfo qi, PageBuilder pb);

	/**
	 * 添加问题
	 * 
	 * @param question
	 * @return
	 */
	public Long addQuestion(Question question);

	/**
	 * 保存问题
	 * 
	 * @param question
	 */
	public void saveQuestion(Question question);

	/**
	 * 删除问题
	 * 
	 * @param question
	 */
	public void deleteQuestion(Question question);

	/**
	 * 获得问题数目
	 * 
	 * @return
	 */
	public int getQuestionCount();

	/**
	 * 获得指定类型的问题数目
	 * 
	 * @param questionType
	 * @return
	 */
	public int getQuestionCount(Integer questionType);

	/**
	 * 获得调查所包含的问题数目
	 * 
	 * @param surveyId
	 * @return
	 */
	public int getQuestionCountBySurvey(Long surveyId);

	/**
	 * 根据指定id字符串获得问题列表
	 * 
	 * @param questionsId
	 * @return
	 */
	public List getQuestionList(String questionsId);

	public List getQuestionList(Integer questionStatus, QueryInfo qi,
			PageBuilder pb);

	public List getQuestionList(Long surveyId, QueryInfo qi, PageBuilder pb);

	public List getQuestionList(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb);

	/**
	 * 获得问题选项列表
	 * 
	 * @param questionId
	 * @return
	 */
	public List getQuestionItems(Long questionId);

	/**
	 * 获得问题选项
	 * 
	 * @param itemId
	 * @return
	 */
	public QuestionItem getQuestionItemById(Long itemId);

	/**
	 * 添加问题选项
	 * 
	 * @param questionItem
	 * @return
	 */
	public Long addQuestionItem(QuestionItem questionItem);

	public void saveQuestionItem(QuestionItem questionItem);

	public void deleteQuestionItem(QuestionItem questionItem);

	public void deleteQuestionItems(Long questionId);

	public void deleteQuestionItemById(Long itemId);

	public int getQuestionItemsTotalCount(Long questionId);

	public Long addVoter(Voter voter);

	public Voter getVoterByName(String name);

	public void saveVoter(Voter voter);

	public void deleteVoter(Voter voter);

	public Voter searchVoter(String ipAddress, Long groupId);

	public void updateQuestionItems(String questionItemsId);

	public void updateQuestionItems2(String questionItemsId);

	public int getItemsCount(Long questionId);

	public void executeHql(String hql, Object args[]);

	public List getQuestionListByGroup(Long surveyId, Long groupId);

	public List getQuestionListByPage(Long surveyId, Long pageId);

	/**
	 * 获得问题页
	 * 
	 * @param surveyId
	 * @param pageId
	 * @return
	 */
	public QuestionPage getQuestionPage(Long surveyId, Long pageId);

	public QuestionGroup getQuestionGroup(Long surveyId, Long groupId);

	public List getPages(Long id);

	//
	public Voter getVoterById(Long voterId);

	public void addAnswer(Answer Answer);

	public void saveAnswer(Answer Answer);

	public void deleteAnswer(Answer Answer);

	public void deleteAnswerByVoter(Long voterId);

	public void deleteAnswerBySurvey(Long surveyId);

	public List getAnswer(Long surveyId, QueryInfo qi, PageBuilder pb);

	public List getAnswerByVoter(Long voterId, QueryInfo qi, PageBuilder pb);

	public List getAnswerByQuestion(Long questionId, QueryInfo qi,
			PageBuilder pb);

	public Answer getAnswerById(Long surveyId, Long voterId, Long questionId);

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

	public List getVoters(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb);

	public SurveyAnswer getSurveyAnswer(Long voterId);

	// /////////////////////////////////////////////////////////////
	public List getAnswer(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb);

	public List getAnswerByQuestion(Long questionId, Long surveyRecordId,
			QueryInfo qi, PageBuilder pb);
}
