/**
 * $Id: SurveyRecordDaoImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.SurveyRecordDao;
import org.openuap.cms.survey.model.SurveyRecord;

/**
 * @author Joseph
 * 
 */
public class SurveyRecordDaoImpl extends BaseDaoHibernate implements
		SurveyRecordDao {

	public Long addSurveyRecord(SurveyRecord surveyRecord) {
		return (Long) this.getHibernateTemplate().save(surveyRecord);
	}

	public void deleteSurveyRecord(SurveyRecord surveyRecord) {
		this.getHibernateTemplate().delete(surveyRecord);
	}

	public void deleteSurveyRecordById(Long id) {
		SurveyRecord sr = getSurveyRecordById(id);
		if (sr != null) {
			this.getHibernateTemplate().delete(sr);
		}
	}

	public SurveyRecord getSurveyRecordById(Long id) {
		String hql = "from SurveyRecord where surveyRecordId=" + id;
		return (SurveyRecord) this.findUniqueResult(hql);
	}

	public List getSurveyRecords(QueryInfo qi, PageBuilder pb) {
		String hql = "from SurveyRecord e order by e.surveyRecordId desc";
		String hql_count = "select count(e.surveyRecordId) from SurveyRecord e";
		return this.getObjects(hql, hql_count, qi, pb);
	}

	public void pauseSurveyRecord(Long id) {
		String hql = "update SurveyRecord set status=1 where surveyRecordId="
				+ id;
		this.executeUpdate(hql);
	}

	public void resumeSurveyRecord(Long id) {
		String hql = "update SurveyRecord set status=2 where surveyRecordId="
				+ id;
		this.executeUpdate(hql);
	}

	public void saveSurveyRecord(SurveyRecord surveyRecord) {
		this.getHibernateTemplate().save(surveyRecord);
	}

	public void startSurveyRecord(Long id) {
		String hql = "update SurveyRecord set status=2 where surveyRecordId="
				+ id;
		this.executeUpdate(hql);
	}

	public void stopSurveyRecord(Long id) {
		String hql = "update SurveyRecord set status=-1 where surveyRecordId="
				+ id;
		this.executeUpdate(hql);
	}
	
}
