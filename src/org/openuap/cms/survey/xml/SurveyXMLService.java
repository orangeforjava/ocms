/**
 * $Id: SurveyXMLService.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.xml;

import java.io.File;

import org.openuap.cms.survey.model.Survey;

/**
 * @author Joseph
 * 
 */
public interface SurveyXMLService {

	public String exportSurvey(Survey survey);
	
	public Survey importSurvey(String xml);
	
	public Survey importSurvey(File xmlFile) throws Exception;
	
	public void saveSurveyXMLFile(Survey survey) throws Exception;
	
	public File getSurveyXMLFile(Survey survey);
	
	public File getSurveyUploadDir();
	
	
	public String exportSurveyStatics(Long surveyRecordId,Long surveyId);
	
	public String validateSurvey(File xmlFile) throws Exception;
}
