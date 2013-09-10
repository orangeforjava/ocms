/**
 * $Id: VoterDaoImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.VoterDao;
import org.openuap.cms.survey.model.Answer;
import org.openuap.cms.survey.model.AnswerItem;
import org.openuap.cms.survey.model.Voter;

/**
 * <p>
 * Title: VoterDaoImpl
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
public class VoterDaoImpl extends BaseDaoHibernate implements VoterDao {
	public VoterDaoImpl() {
	}

	public Long addVoter(Voter voter) {
		return (Long) this.getHibernateTemplate().save(voter);
	}

	public Voter getVoterByName(String name) {
		String hql = "from Voter where voterName=?";
		return (Voter) this.findUniqueResult(hql, new Object[] { name });
	}

	public void saveVoter(Voter voter) {
		this.getHibernateTemplate().saveOrUpdate(voter);
	}

	public void deleteVoter(Voter voter) {
		this.getHibernateTemplate().delete(voter);
	}

	public Voter searchVoter(String ipAddress, Long groupId) {
		String hql = "FROM Voter AS vr WHERE vr.voterIpaddress=? AND vr.voterGroupId=?";
		return (Voter) this.findUniqueResult(hql, new Object[] { ipAddress,
				groupId });
	}

	public void deleteVoterById(Long voterId) {
		Voter voter = getVoterById(voterId);
		if (voter != null) {
			this.deleteVoter(voter);
		}
	}

	public void addAnswer(Answer answer) {
		this.getHibernateTemplate().save(answer);
	}

	public void saveAnswer(Answer answer) {
		this.getHibernateTemplate().saveOrUpdate(answer);
	}

	public void deleteAnswer(Answer answer) {
		this.getHibernateTemplate().delete(answer);
	}

	public void deleteAnswerByVoter(Long voterId) {
		String hql = "delete from Answer where voterId=" + voterId;
		this.executeUpdate(hql);
	}

	public void deleteAnswerBySurvey(Long surveyId) {
		String hql = "delete from Answer where surveyId=" + surveyId;
		this.executeUpdate(hql);
	}

	public List getAnswer(Long surveyId, QueryInfo qi, PageBuilder pb) {
		String hql = "select e from Answer e where e.surveyId=" + surveyId
				+ " order by e.questionId";
		String hql_count = "select count(*) from Answer where surveyId="
				+ surveyId;
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public List getAnswerByVoter(Long voterId, QueryInfo qi, PageBuilder pb) {
		String hql = "select e from Answer e where e.voterId=" + voterId
				+ " order by e.questionId";
		String hql_count = "select count(*) from Answer where voterId="
				+ voterId;
		return this.getObjects(hql, hql_count, qi, pb);

	}

	public void addAnswerItem(AnswerItem answerItem) {
		this.addObject(answerItem);
	}

	public void saveAnswerItem(AnswerItem answerItem) {
		this.saveObject(answerItem);
	}

	public void deleteAnswerItem(Answer answerItem) {
		this.deleteObject(answerItem);
	}

	public void deleteAnswerItemByVoter(Long voterId) {
		String hql = "delete from AnswerItem where voterId=" + voterId;
		this.executeUpdate(hql);
	}

	public void deleteAnswerItemBySurvey(Long surveyId) {
		String hql = "delete from AnswerItem where surveyId=" + surveyId;
		this.executeUpdate(hql);
	}

	public List getAnswerItemBySurvey(Long surveyId, QueryInfo qi,
			PageBuilder pb) {
		String hql = "select e from AnswerItem e where e.surveyId=" + surveyId
				+ " order by e.questionId";
		String hql_count = "select count(*) from AnswerItem where surveyId="
				+ surveyId;
		return this.getObjects(hql, hql_count, qi, pb);

	}

	public List getAnswerItemByVoter(Long voterId, QueryInfo qi, PageBuilder pb) {
		String hql = "select e from AnswerItem e where e.voterId=" + voterId
				+ " order by e.questionId";
		String hql_count = "select count(*) from AnswerItem where voterId="
				+ voterId;
		return this.getObjects(hql, hql_count, qi, pb);

	}

	public Voter getVoterById(Long voterId) {
		String hql = "from Voter where voterId=?";
		return (Voter) this.findUniqueResult(hql, new Object[] { voterId });
	}

	public List getAnswerByQuestion(Long questionId, QueryInfo qi,
			PageBuilder pb) {
		String hql = "select e from Answer e where e.questionId=" + questionId
				+ " order by e.questionId";
		String hql_count = "select count(*) from Answer where questionId="
				+ questionId;
		return this.getObjects(hql, hql_count, qi, pb);

	}

	public Answer getAnswerById(Long surveyRecordId, Long voterId,
			Long questionId) {
		String hql = "from Answer where surveyRecordId=" + surveyRecordId
				+ " and voterId=" + voterId + " and questionId=" + questionId;
		return (Answer) this.findUniqueResult(hql);
	}

	public List getAnswerItemByItem(Long itemId, QueryInfo qi, PageBuilder pb) {
		String hql = "select e from AnswerItem e where e.questionItemId="
				+ itemId + " ";
		String hql_count = "select count(*) from AnswerItem where questionItemId="
				+ itemId;
		return this.getObjects(hql, hql_count, qi, pb);

	}

	public AnswerItem getAnswerItemById(Long itemId, Long voterId, Long surveyId) {
		String hql = "from AnswerItem where surveyId=" + surveyId
				+ " and voterId=" + voterId + " and questionItemId=" + itemId;
		return (AnswerItem) this.findUniqueResult(hql);

	}

	public List getVoters(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb) {
		String hql = "select e from Voter e where e.voterSurveyId=" + surveyId
				+ " and e.surveyRecordId=" + surveyRecordId
				+ " order by e.voterPollDate desc";
		String hql_count = "select count(*) from Voter e where e.voterSurveyId="
				+ surveyId + " and e.surveyRecordId=" + surveyRecordId;

		return this.getObjects(hql, hql_count, qi, pb);
	}

	public List getAnswer(Long surveyId, Long surveyRecordId, QueryInfo qi,
			PageBuilder pb) {
		String hql = "select e from Answer e where e.surveyId=" + surveyId
				+ " and e.surveyRecordId=" + surveyRecordId
				+ " order by e.questionId";
		String hql_count = "select count(*) from Answer where surveyId="
				+ surveyId + "and surveyRecordId=" + surveyRecordId;
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public List getAnswerByQuestion(Long questionId, Long surveyRecordId,
			QueryInfo qi, PageBuilder pb) {
		String hql = "select e from Answer e where e.questionId=" + questionId
				+ " and e.surveyRecordId=" + surveyRecordId
				+ " order by e.questionId";
		String hql_count = "select count(*) from Answer e where e.questionId="
				+ questionId + " and e.surveyRecordId=" + surveyRecordId;
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public int getQuestionAnswerTotalCount(Long surveyRecordId, Long questionId) {
		String hql = "select count(*) from Answer e where e.questionId="
				+ questionId + " and e.surveyRecordId=" + surveyRecordId;
		return this.getIntFieldValue(hql);
	}
}
