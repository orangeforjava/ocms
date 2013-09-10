/**
 *  $Id: SurveyDaoImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.SurveyDao;
import org.openuap.cms.survey.model.Survey;

/**
 * <p>
 * Title: SurveyDaoImpl
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
public class SurveyDaoImpl extends BaseDaoHibernate implements SurveyDao {
	public SurveyDaoImpl() {
	}

	public Survey getSurveyByName(String name) {
		String hql = "from Survey where surveyName=?";
		return (Survey) this.findUniqueResult(hql, new Object[] { name });
	}

	public Survey getSurveyById(Long id) {
		String hql = "from Survey where surveyId=?";
		return (Survey) this.findUniqueResult(hql, new Object[] { id });
	}

	public List getAllSurveys() {
		String hql = "from Survey";
		return this.executeFind(hql);
	}

	public List getSurveys(QueryInfo qi, PageBuilder pb) {
		String hql = "from Survey e order by e.surveyId";
		String hql_count = "select count(e.surveyId) from Survey e";
		return this.getObjects(hql, hql_count, qi, pb);
	}
	
	public Long addSurvey(Survey survey) {
		return (Long) this.getHibernateTemplate().save(survey);
	}

	public void saveSurvey(Survey survey) {
		this.getHibernateTemplate().saveOrUpdate(survey);
	}

	public void deleteSurvey(Survey survey) {
		this.getHibernateTemplate().delete(survey);
	}

	public void deleteSurveyById(Long id) {
		Survey survey = this.getSurveyById(id);
		if (survey != null) {
			this.deleteSurvey(survey);
		}
	}

	public Survey getSurveyByGuid(String guid) {
		String hql = "from Survey where surveyGuid=?";
		return (Survey) this.findUniqueResult(hql, new Object[] { guid });
	}

	public void auditSurvey(Long id) {
		String hql = "update Survey set surveyStatus=1 where surveyId=" + id;
		this.executeUpdate(hql);
	}

	public void unAuditSurvey(Long id) {
		String hql = "update Survey set surveyStatus=0 where surveyId=" + id;
		this.executeUpdate(hql);
	}
	public void recycleSurvey(Long id){
		String hql = "update Survey set deleted=1 where surveyId=" + id;
		this.executeUpdate(hql);
	}
	public void unRecycleSurvey(Long id){
		String hql = "update Survey set deleted=0 where surveyId=" + id;
		this.executeUpdate(hql);
	}

	public List<Survey> getSurveys(Long nodeId, QueryInfo qi, PageBuilder pb) {
		String hql = "from Survey where nodeId="+nodeId+" order by surveyId";
		String hql_count = "select count(surveyId) from Survey where nodeId="+nodeId;
		return this.getObjects(hql, hql_count, qi, pb);
	}
}
