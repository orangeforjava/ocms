/**
 * $Id: SurveyAreaManagerImpl.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.manager.impl;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.dao.AreaDao;
import org.openuap.cms.survey.manager.SurveyAreaManager;
import org.openuap.cms.survey.model.SurveyArea;

/**
 * <p>
 * Title:AreaManagerImpl
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
public class SurveyAreaManagerImpl implements SurveyAreaManager {

	private AreaDao areaDao;

	public SurveyAreaManagerImpl() {
	}

	public SurveyArea getAreaByName(String name) {
		return areaDao.getAreaByName(name);
	}

	public SurveyArea getAreaById(Long id) {
		return areaDao.getAreaById(id);
	}

	public List<SurveyArea> getAllAreas() {
		return areaDao.getAllAreas();
	}

	public List<SurveyArea> getAreas(QueryInfo qi, PageBuilder pb) {
		return areaDao.getAreas(qi, pb);
	}

	public Long addArea(SurveyArea Area) {
		return areaDao.addArea(Area);
	}

	public void saveArea(SurveyArea area) {
		areaDao.saveArea(area);
	}

	public void deleteArea(SurveyArea area) {
		areaDao.deleteArea(area);
	}

	public void deleteAreaById(Long id) {
		areaDao.deleteAreaById(id);
	}

	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	public List<SurveyArea> getAreas(Long nodeId, QueryInfo qi, PageBuilder pb) {
		return areaDao.getAreas(nodeId, qi, pb);
	}
}
