/**
 * $Id: SurveyAreaEngine.java 4017 2011-03-13 13:55:50Z orangeforjava $
 */
package org.openuap.cms.survey.engine;


/**
 * <p>
 * </p>
 * @author Joseph
 *
 */
public interface SurveyAreaEngine {
	
	public static final Integer TPL_TYPE_FILE=0;
	public static final Integer TPL_TYPE_DB=1;
	/**
	 * 获得调查位内容
	 * @param areaId 调查位id
	 * @param surveyRecordId 调查活动id
	 * @param type 输出类型，inline或者ssi
	 * @return
	 */
	public String getAreaContent(String areaId, String surveyRecordId,String type);
}
