/**
 * $Id: QuestionManagerImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.QuestionDao;
import org.openuap.cms.survey.dao.QuestionItemDao;
import org.openuap.cms.survey.dao.VoterDao;
import org.openuap.cms.survey.manager.QuestionManager;
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
 * Title:VoteManagerImpl
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
public class QuestionManagerImpl implements QuestionManager {

	private QuestionDao questionDao;
	private QuestionItemDao questionItemDao;
	private VoterDao voterDao;

	public QuestionManagerImpl() {
	}

	public List getQuestions(QueryInfo qi, PageBuilder pb) {
		return questionDao.getQuestions(qi, pb);
	}

	public Question getQuestionById(Long id) {
		return questionDao.getQuestionById(id);
	}

	public List getAllQuestions() {
		return questionDao.getAllQuestions();
	}

	public List searchQuestions(String key, QueryInfo qi, PageBuilder pb) {
		return questionDao.searchQuestions(key, qi, pb);
	}

	public Long addQuestion(Question question) {
		return questionDao.addQuestion(question);
	}

	public void saveQuestion(Question question) {
		questionDao.saveQuestion(question);
	}

	public void deleteQuestion(Question question) {
		questionDao.deleteQuestion(question);
	}

	public int getQuestionCount() {
		return questionDao.getQuestionCount();
	}

	public int getQuestionCount(Integer questionStatus) {
		return questionDao.getQuestionCount(questionStatus);
	}

	public List getQuestionList(String questionsId) {
		return questionDao.getQuestionList(questionsId);
	}

	public List getQuestionList(Integer questionStatus, QueryInfo qi,
			PageBuilder pb) {
		return questionDao.getQuestionList(questionStatus, qi, pb);
	}

	public List getQuestionItems(Long questionId) {
		return questionItemDao.getQuestionItems(questionId);
	}

	public QuestionItem getQuestionItemById(Long itemId) {
		return questionItemDao.getQuestionItemById(itemId);
	}

	public Long addQuestionItem(QuestionItem questionItem) {
		return questionItemDao.addQuestionItem(questionItem);
	}

	public void saveQuestionItem(QuestionItem questionItem) {
		questionItemDao.saveQuestionItem(questionItem);
	}

	public void deleteQuestionItem(QuestionItem questionItem) {
		questionItemDao.deleteQuestionItem(questionItem);
	}

	public void deleteQuestionItems(Long questionId) {
		questionItemDao.deleteQuestionItems(questionId);
	}

	public void deleteQuestionItemById(Long questionId) {
		questionItemDao.deleteQuestionItemById(questionId);
	}

	public int getQuestionItemsTotalCount(Long questionId) {
		return questionItemDao.getQuestionItemsTotalCount(questionId);
	}

	public Long addVoter(Voter voter) {
		return voterDao.addVoter(voter);
	}

	public Voter getVoterByName(String name) {
		return voterDao.getVoterByName(name);
	}

	public void saveVoter(Voter voter) {
		voterDao.saveVoter(voter);
	}

	public void deleteVoter(Voter voter) {
		voterDao.deleteVoter(voter);
	}

	public Voter searchVoter(String ipAddress, Long groupId) {
		return voterDao.searchVoter(ipAddress, groupId);
	}

	public void setVoterDao(VoterDao voterDao) {
		this.voterDao = voterDao;
	}

	public void setQuestionItemDao(QuestionItemDao questionItemDao) {
		this.questionItemDao = questionItemDao;
	}

	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	public void updateQuestionItems(String voteItemsId) {
		questionItemDao.updateQuestionItems(voteItemsId);
	}

	public void updateQuestionItems2(String questionItemsId) {
		questionItemDao.updateQuestionItems2(questionItemsId);
	}

	public List getQuestionList(Long surveyId, QueryInfo qi, PageBuilder pb) {
		return questionDao.getQuestionList(surveyId, qi, pb);
	}

	public List getQuestionList(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb) {
		return questionDao.getQuestionList(hql, hql_count, qi, pb);
	}

	public int getItemsCount(Long questionId) {
		return questionItemDao.getItemsCount(questionId);
	}

	public void executeHql(String hql, Object args[]) {
		questionDao.executeHql(hql, args);
	}

	public int getQuestionCountBySurvey(Long surveyId) {
		return questionDao.getQuestionCountBySurvey(surveyId);
	}

	public List getQuestionListByGroup(Long surveyId, Long groupId) {
		return questionDao.getQuestionListByGroup(surveyId, groupId);
	}

	public QuestionPage getQuestionPage(Long surveyId, Long pageId) {
		List questions = getQuestionListByPage(surveyId, pageId);
		String qids = "";
		if (questions != null && questions.size() > 0) {
			Question fq = (Question) questions.get(0);
			QuestionPage questionPage = new QuestionPage();
			questionPage.setPageId(pageId);
			// qids += fq.getQuestionId() + ",";
			//
			List groups = new ArrayList();
			//
			Long groupId = fq.getGroupId();

			int start = 0;
			int end = 0;
			for (int i = 0; i < questions.size(); i++) {
				Question q = (Question) questions.get(i);
				qids += q.getQuestionId() + ",";
				if (q.getGroupId().equals(groupId)) {
					end++;
				} else {
					if (end - start > 1) {
						// 一道题以上是1个组
						QuestionGroup tmpGroup = new QuestionGroup();
						List qs = new ArrayList();
						Question q_first = (Question) questions.get(start);
						for (int j = start; j < end; j++) {
							Question tmp_q = (Question) questions.get(j);
							qs.add(tmp_q);
						}
						tmpGroup.setGroupTitle(q_first.getGroupTitle());
						tmpGroup.setGroupType(new Integer(1));
						tmpGroup.setGroupId(q_first.getGroupId());
						tmpGroup.setQuestions(qs);
						groups.add(tmpGroup);

					} else {
						// 一道题是1个组
						Question q_prev = (Question) questions.get(start);
						QuestionGroup tmpGroup = new QuestionGroup();
						tmpGroup.setGroupTitle(q_prev.getQuestionTitle());
						tmpGroup.setGroupType(new Integer(0));
						tmpGroup.setGroupId(q_prev.getGroupId());
						List qs = new ArrayList();
						qs.add(q_prev);
						tmpGroup.setQuestions(qs);
						groups.add(tmpGroup);
					}

					start = end;
					end++;
					groupId = q.getGroupId();
				}
			}
			// Question eq = (Question) questions.get(questions.size() - 1);
			if (end - start > 1) {
				// 一道题以上是1个组
				QuestionGroup tmpGroup = new QuestionGroup();
				List qs = new ArrayList();
				Question q_first = (Question) questions.get(start);
				for (int j = start; j < end; j++) {
					Question tmp_q = (Question) questions.get(j);
					qs.add(tmp_q);
				}
				tmpGroup.setGroupTitle(q_first.getGroupTitle());
				tmpGroup.setGroupType(new Integer(1));
				tmpGroup.setGroupId(q_first.getGroupId());
				tmpGroup.setQuestions(qs);
				groups.add(tmpGroup);

			} else {
				// 一道题是1个组
				Question q_prev = (Question) questions.get(start);
				QuestionGroup tmpGroup = new QuestionGroup();
				tmpGroup.setGroupTitle(q_prev.getQuestionTitle());
				tmpGroup.setGroupType(new Integer(0));
				tmpGroup.setGroupId(q_prev.getGroupId());
				List qs = new ArrayList();
				qs.add(q_prev);
				tmpGroup.setQuestions(qs);
				groups.add(tmpGroup);
			}

			//
			questionPage.setGroups(groups);
			qids = qids.substring(0, qids.length() - 1);
			questionPage.setQuestionIds(qids);
			return questionPage;
		}
		return null;
	}

	public List getQuestionListByPage(Long surveyId, Long pageId) {
		return questionDao.getQuestionListByPage(surveyId, pageId);
	}

	public QuestionGroup getQuestionGroup(Long surveyId, Long groupId) {
		List questions = getQuestionListByGroup(surveyId, groupId);
		String qids = "";
		if (questions != null && questions.size() > 0) {
			Question fq = (Question) questions.get(0);
			QuestionGroup group = new QuestionGroup();
			group.setGroupId(fq.getGroupId());
			if (questions.size() > 1) {
				group.setGroupType(new Integer(1));
				group.setGroupTitle(fq.getGroupTitle());
			} else {
				group.setGroupType(new Integer(0));
				group.setGroupTitle(fq.getQuestionTitle());
			}
			qids = fq.getQuestionId() + ",";
			for (int i = 1; i < questions.size(); i++) {
				Question q = (Question) questions.get(i);
				qids += q.getQuestionId() + ",";
			}
			qids = qids.substring(0, qids.length() - 1);
			group.setQuestionIds(qids);
			group.setQuestions(questions);
			return group;
		}
		return null;
	}

	public List getPages(Long id) {
		return questionDao.getPages(id);
	}

	public Voter getVoterById(Long voterId) {
		return voterDao.getVoterById(voterId);
	}

	public void addAnswer(Answer Answer) {
		voterDao.addAnswer(Answer);
	}

	public void saveAnswer(Answer Answer) {
		voterDao.saveAnswer(Answer);
	}

	public void deleteAnswer(Answer Answer) {
		voterDao.deleteAnswer(Answer);
	}

	public void deleteAnswerByVoter(Long voterId) {
		voterDao.deleteAnswerByVoter(voterId);
	}

	public void deleteAnswerBySurvey(Long surveyId) {
		voterDao.deleteAnswerBySurvey(surveyId);
	}

	public List getAnswer(Long surveyId, QueryInfo qi, PageBuilder pb) {
		return voterDao.getAnswer(surveyId, qi, pb);
	}

	public List getAnswerByVoter(Long voterId, QueryInfo qi, PageBuilder pb) {
		return voterDao.getAnswerByVoter(voterId, qi, pb);
	}

	public List getAnswerByQuestion(Long questionId, QueryInfo qi,
			PageBuilder pb) {
		return voterDao.getAnswerByQuestion(questionId, qi, pb);
	}

	public Answer getAnswerById(Long surveyId, Long voterId, Long questionId) {
		return voterDao.getAnswerById(surveyId, voterId, questionId);
	}

	public void addAnswerItem(AnswerItem AnswerItem) {
		voterDao.addAnswerItem(AnswerItem);
	}

	public void saveAnswerItem(AnswerItem AnswerItem) {
		voterDao.saveAnswerItem(AnswerItem);
	}

	public void deleteAnswerItem(Answer AnswerItem) {
		voterDao.deleteAnswerItem(AnswerItem);
	}

	public void deleteAnswerItemByVoter(Long voterId) {
		voterDao.deleteAnswerByVoter(voterId);
	}

	public void deleteAnswerItemBySurvey(Long surveyId) {
		voterDao.deleteAnswerBySurvey(surveyId);
	}

	public List getAnswerItemBySurvey(Long surveyId, QueryInfo qi,
			PageBuilder pb) {
		return voterDao.getAnswerItemBySurvey(surveyId, qi, pb);
	}

	public List getAnswerItemByVoter(Long voterId, QueryInfo qi, PageBuilder pb) {
		return voterDao.getAnswerByVoter(voterId, qi, pb);
	}

	public List getAnswerItemByItem(Long itemId, QueryInfo qi, PageBuilder pb) {
		return voterDao.getAnswerItemByItem(itemId, qi, pb);
	}

	public AnswerItem getAnswerItemById(Long itemId, Long voterId, Long surveyId) {
		return voterDao.getAnswerItemById(itemId, voterId, surveyId);
	}

	public List getVoters(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb) {
		return voterDao.getVoters(surveyId, surveyRecordId, qi, pb);
	}

	public SurveyAnswer getSurveyAnswer(Long voterId) {
		Voter voter = voterDao.getVoterById(voterId);
		if (voter != null) {
			SurveyAnswer surveyAnswer = new SurveyAnswer();
			List answers = voterDao.getAnswerByVoter(voterId, null, null);
			Map answerMap = new HashMap();
			Map answerItemMap = new HashMap();
			if (answers != null) {
				answerMap = new HashMap();
				for (int i = 0; i < answers.size(); i++) {
					Answer answer = (Answer) answers.get(i);
					answerMap.put(answer.getQuestionId(), answer);
				}
			}
			List answerItems = voterDao.getAnswerItemByVoter(voterId, null,
					null);
			if (answerItems != null) {

				for (int i = 0; i < answerItems.size(); i++) {
					AnswerItem answerItem = (AnswerItem) answerItems.get(i);
					answerItemMap.put(answerItem.getQuestionItemId(),
							answerItem);
				}
			}
			//
			surveyAnswer.setAnswerDate(voter.getVoterPollDate());
			surveyAnswer.setSurveyId(voter.getVoterSurveyId());
			surveyAnswer.setVoter(voter);
			surveyAnswer.setQuestionAnswers(answerMap);
			surveyAnswer.setQuestionItemAnswers(answerItemMap);
			return surveyAnswer;
		}
		return null;
	}
	/**
	 * 
	 */
	public List getAnswer(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb) {
		return voterDao.getAnswer(surveyId, surveyRecordId, qi, pb);
	}
	/**
	 * 
	 */
	public List getAnswerByQuestion(Long questionId, Long surveyRecordId,
			QueryInfo qi, PageBuilder pb) {
		return voterDao.getAnswerByQuestion(questionId, surveyRecordId, qi, pb);
	}
}
