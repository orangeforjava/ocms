/**
 * $Id: AreaDaoImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.dao.hibernate;

import java.util.List;

import org.openuap.base.dao.hibernate.BaseDaoHibernate;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.AreaDao;
import org.openuap.cms.survey.model.SurveyArea;

/**
 * <p>
 * Title: AreaDaoImpl
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
public class AreaDaoImpl extends BaseDaoHibernate implements AreaDao {
	
	public AreaDaoImpl() {
	}

	public SurveyArea getAreaByName(String name) {
		String hql = "from SurveyArea where areaName=?";
		return (SurveyArea) this.findUniqueResult(hql, new Object[] { name });
	}

	public SurveyArea getAreaById(Long id) {
		String hql = "from SurveyArea where id=?";

		return (SurveyArea) this.findUniqueResult(hql, new Object[] { id });
	}

	public List<SurveyArea> getAllAreas() {
		String hql = "from SurveyArea";
		return this.executeFind(hql);
	}

	/**
	 * 
	 */
	public List<SurveyArea> getAreas(QueryInfo qi, PageBuilder pb) {
		String hql = "from SurveyArea order by id";
		String hql_count = "select count(*) from SurveyArea";
		return getObjects(hql, hql_count, qi, pb);
	}

	public Long addArea(SurveyArea area) {
		return (Long) this.getHibernateTemplate().save(area);
	}

	public void saveArea(SurveyArea area) {
		this.saveObject(area);
	}

	public void deleteArea(SurveyArea area) {
		this.deleteObject(area);
	}

	public void deleteAreaById(Long id) {
		SurveyArea area = this.getAreaById(id);
		if (area != null) {
			this.deleteArea(area);
		}
	}

	public List<SurveyArea> getAreas(Long nodeId, QueryInfo qi, PageBuilder pb) {
		String hql = "from SurveyArea where nodeId=" + nodeId
				+ " order by id";
		String hql_count = "select count(*) from SurveyArea where nodeId=" + nodeId
				+ " ";
		return getObjects(hql, hql_count, qi, pb);
	}
}
