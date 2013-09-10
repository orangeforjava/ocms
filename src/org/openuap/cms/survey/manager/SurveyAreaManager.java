/**
 * $Id: SurveyAreaManager.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.manager;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.survey.model.SurveyArea;

/**
 * <p>
 * Title: SurveyAreaManager
 * </p>
 * 
 * <p>
 * Description:调查位管理接口定义.
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
 * @author Joseph
 * @version 1.0
 */
public interface SurveyAreaManager {
	
	public SurveyArea getAreaByName(String name);

	public SurveyArea getAreaById(Long id);

	public List<SurveyArea> getAllAreas();

	public List<SurveyArea> getAreas(QueryInfo qi, PageBuilder pb);
	
	public List<SurveyArea> getAreas(Long nodeId,QueryInfo qi, PageBuilder pb);

	public Long addArea(SurveyArea Area);

	public void saveArea(SurveyArea area);

	public void deleteArea(SurveyArea area);

	public void deleteAreaById(Long id);

}
