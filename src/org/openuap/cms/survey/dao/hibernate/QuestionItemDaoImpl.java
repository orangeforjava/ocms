/**
 * $Id: QuestionItemDaoImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.cms.survey.dao.QuestionItemDao;
import org.openuap.cms.survey.model.QuestionItem;

/**
 * <p>
 * Title: VoteItemDaoImpl
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
public class QuestionItemDaoImpl extends BaseDaoHibernate implements
		QuestionItemDao {
	public QuestionItemDaoImpl() {
	}

	public List getQuestionItems(Long questionId) {
		String hql = "from QuestionItem as qi where qi.question.questionId=? order by qi.questionItemSort";
		return this.executeFind(hql, new Object[] { questionId });
	}

	public QuestionItem getQuestionItemById(Long itemId) {
		String hql = "from QuestionItem where questionItemId=?";
		return (QuestionItem) this.findUniqueResult(hql,
				new Object[] { itemId });
	}

	public Long addQuestionItem(QuestionItem questionItem) {
		return (Long) this.getHibernateTemplate().save(questionItem);
	}

	public void saveQuestionItem(QuestionItem questionItem) {
		this.getHibernateTemplate().saveOrUpdate(questionItem);
	}

	public void deleteQuestionItem(QuestionItem questionItem) {
		this.getHibernateTemplate().delete(questionItem);
	}

	public void deleteQuestionItems(Long questionId) {
		String hql = "delete from QuestionItem as qi where qi.question.questionId ="
				+ questionId;
		this.executeUpdate(hql);
	}

	public void deleteQuestionItemById(Long itemId) {
		String hql = "delete from QuestionItem where questionItemId =" + itemId;
		this.executeUpdate(hql);
	}

	public int getItemsCount(Long questionId) {
		String hql = "select count(*) from QuestionItem qi where qi.question.questionId=?";
		return ((Number) this.getHibernateTemplate().iterate(hql,
				new Object[] { questionId }).next()).intValue();
	}

	public int getQuestionItemsTotalCount(Long questionId) {
		String hql = "select sum(qi.questionItemPolledTimes) from QuestionItem  qi where qi.question.questionId=?";
		Object obj = this.getHibernateTemplate().iterate(hql,
				new Object[] { questionId }).next();
		if (obj != null && obj instanceof Number) {
			return ((Number) obj).intValue();
		} else {
			return 0;
		}
	}

	public void updateQuestionItems(String questionItemsId) {
		String hql = "update QuestionItem set questionItemPolledTimes = questionItemPolledTimes + 1 where questionItemId in("
				+ questionItemsId + ")";
		this.executeUpdate(hql);
	}

	public void updateQuestionItems2(String questionItemsId) {
		String hql = "update QuestionItem set questionItemPolledTimes = questionItemPolledTimes - 1 where questionItemId in("
				+ questionItemsId + ")";
		this.executeUpdate(hql);
	}

	public void executeHql(String hql, Object[] args) {
		this.executeUpdate(hql, args);
	}

}
