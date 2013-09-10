/**
 * $Id: QuestionDaoImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.QuestionDao;
import org.openuap.cms.survey.model.Question;

/**
 * <p>
 * Title: VoteDaoImpl
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
public class QuestionDaoImpl extends BaseDaoHibernate implements QuestionDao {
	public QuestionDaoImpl() {
	}

	public List getQuestions(QueryInfo qi, PageBuilder pb) {
		String hql = "FROM Question ORDER BY questionCreationDate DESC";
		String hql_count = "select count(*) from Question";
		return getObjects(hql, hql_count, qi, pb);
	}

	public Question getQuestionById(Long id) {
		String hql = "from Question where questionId=?";
		return (Question) this.findUniqueResult(hql, new Object[] { id });
	}

	public List getAllQuestions() {
		String hql = "from Question";
		return this.executeFind(hql);
	}

	public List searchQuestions(String key, QueryInfo qi, PageBuilder pb) {
		if (key != null) {
			key = key.trim();
			String hql = "from Question where questionTitle like '%" + key
					+ "%' order by questionCreationDate";
			String hql_count = "select count(*) from Question where questionTitle like '%"
					+ key + "%'";
			return this.getObjects(hql, hql_count, qi, pb);
		}
		return null;
	}

	public Long addQuestion(Question question) {
		return (Long) this.getHibernateTemplate().save(question);
	}

	public void saveQuestion(Question question) {
		this.getHibernateTemplate().saveOrUpdate(question);
	}

	public void deleteQuestion(Question question) {
		this.getHibernateTemplate().delete(question);
	}

	public int getQuestionCount() {
		String hql = "select count(*) from Question";
		return ((Number) this.getHibernateTemplate().iterate(hql).next())
				.intValue();

	}

	public int getQuestionCount(Integer questionType) {
		String hql = "select count(*) from Question where questionType = "
				+ questionType;
		return ((Number) this.getHibernateTemplate().iterate(hql).next())
				.intValue();
	}

	public int getQuestionCountBySurvey(Long surveyId) {
		String hql = "select count(*) from Question where surveyId = "
				+ surveyId;
		return ((Number) this.getHibernateTemplate().iterate(hql).next())
				.intValue();
	}

	public List getQuestionList(String questionsId) {
		String hql = "from Question where questionId in(" + questionsId + ")";
		return this.executeFind(hql);
	}

	public List getQuestionList(Integer questionStatus, QueryInfo qi,
			PageBuilder pb) {
		String hql = "FROM Question where questionStatus = " + questionStatus
				+ " order by questionCreationDate DESC";
		String hql_count = "SELECT count(*) FROM Question WHERE questionStatus = "
				+ questionStatus;
		return this.getObjects(hql, hql_count, qi, pb);
	}

	/**
	 * 
	 * @param guid
	 *            String
	 * @return Vote
	 */
	public Question getQuestionByGuid(String guid) {
		String hql = "from Question where questionGuid=?";
		return (Question) this.findUniqueResult(hql, new Object[] { guid });
	}

	public List getQuestionList(Long surveyId, QueryInfo qi, PageBuilder pb) {
		String hql = "from Question e where e.surveyId = " + surveyId
				+ "  order by e.questionPos,e.questionCreationDate DESC";
		String hql_count = "select count(*) from Question e where e.surveyId = "
				+ surveyId;
		return this.getObjects(hql, hql_count, qi, pb);

	}

	public List getQuestionList(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb) {
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public void executeHql(String hql, Object[] args) {
		this.executeUpdate(hql, args);
	}

	public List getQuestionListByGroup(Long surveyId, Long groupId) {
		String hql = "from Question e where e.surveyId = " + surveyId
				+ " and e.groupId=" + groupId + " order by e.questionPos";
		return executeFind(hql);
	}

	public List getQuestionListByPage(Long surveyId, Long pageId) {
		String hql = "from Question e where e.surveyId = " + surveyId
				+ " and e.pageId=" + pageId
				+ " order by e.groupId,e.questionPos";
		return executeFind(hql);
	}

	/**
	 * 返回调查包含的页号
	 * 
	 * @param surveyId
	 *            Long
	 * @return List
	 */
	public List getPages(Long surveyId) {
		String hql = "select distinct pageId from Question where surveyId="
				+ surveyId;
		return executeFind(hql);
	}

}
