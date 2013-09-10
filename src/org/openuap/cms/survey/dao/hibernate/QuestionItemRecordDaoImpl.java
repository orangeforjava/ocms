/**
 * $Id: QuestionItemRecordDaoImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.dao.hibernate;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.survey.dao.QuestionItemRecordDao;
import org.openuap.cms.survey.model.QuestionItemRecord;

/**
 * 问题选项答题记录DAO实现.
 * 
 * @author Joseph
 * 
 */
public class QuestionItemRecordDaoImpl extends BaseDaoHibernate implements
		QuestionItemRecordDao {

	public void addQuestionItemRecord(QuestionItemRecord itemRecord) {
		this.getHibernateTemplate().save(itemRecord);
	}

	public void clearQuestionItemRecord(Long surveyRecordId) {
		String hql = "update QuestionItemRecord set questionItemPolledTimes=0 where surveyRecordId="
				+ surveyRecordId;
		this.executeUpdate(hql);
	}

	public void updateQuestionItems(Long surveyRecordId, String questionItemsId) {
		String hql = "update QuestionItemRecord set questionItemPolledTimes = questionItemPolledTimes + 1 where surveyRecordId="
				+ surveyRecordId
				+ " and questionItemId in("
				+ questionItemsId
				+ ")";
		this.executeUpdate(hql);
	}

	public void updateQuestionItems2(Long surveyRecordId, String questionItemsId) {
		String hql = "update QuestionItemRecord set questionItemPolledTimes = questionItemPolledTimes - 1 where surveyRecordId="
				+ surveyRecordId
				+ " and questionItemId in("
				+ questionItemsId
				+ ")";
		this.executeUpdate(hql);
	}

	public int getQuestionItemsTotalCount(Long surveyRecordId, Long questionId) {
		String hql = "select sum(qi.questionItemPolledTimes) from QuestionItemRecord  qi where qi.questionId=? and qi.surveyRecordId=?";
		Object obj = this.getHibernateTemplate().iterate(hql,
				new Object[] { questionId, surveyRecordId }).next();
		if (obj != null && obj instanceof Number) {
			return ((Number) obj).intValue();
		} else {
			return 0;
		}
	}

	public QuestionItemRecord getQuestionItemRecord(Long surveyRecordId,
			Long itemId) {
		String hql = "from QuestionItemRecord where surveyRecordId=? and questionItemId=?";
		return (QuestionItemRecord) this.findUniqueResult(hql, new Object[] { surveyRecordId, itemId });
	}

	public void saveQuestionItemRecord(QuestionItemRecord itemRecord) {
		this.getHibernateTemplate().saveOrUpdate(itemRecord);
		
	}
}
