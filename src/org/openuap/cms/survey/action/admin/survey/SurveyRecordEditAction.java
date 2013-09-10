/**
 * $Id: SurveyRecordEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.resource.DirectoryListDataLoader;
import org.openuap.cms.core.action.UserAwareFormAction;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.model.SurveyRecord;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Joseph
 * 
 */
public class SurveyRecordEditAction extends UserAwareFormAction {

	private SurveyManager surveyManager;

	//
	private String defaultScreensPath;

	private String resultViewName;

	//
	public SurveyRecordEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/survey/screens/record/";
		resultViewName = defaultScreensPath + "survey_record_result.html";
		this.setFormView(defaultScreensPath + "survey_record_edit.html");
		this.setSuccessView(resultViewName);
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(SurveyRecord.class);
		this.setCommandName("surveyRecord");
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		ModelAndView mv = new ModelAndView(resultViewName, model);
		model.put("op", "edit");
		try {
			SurveyRecord surveyRecord = (SurveyRecord) command;
			if (surveyRecord.getSurveyRecordId() == null) {
				//
				surveyRecord.setCreationUserId(this.getUser().getUserId());
				surveyRecord.setCreationUserName(this.getUser().getName());
				//
				if (surveyRecord.getStrStartDate() != null) {
					Long startDate = surveyRecord.toLongDate(surveyRecord
							.getStrStartDate());
					surveyRecord.setStartDate(startDate);
				}
				if (surveyRecord.getStrEndDate() != null) {
					Long endDate = surveyRecord.toLongDate(surveyRecord
							.getStrEndDate());
					surveyRecord.setEndDate(endDate);
				}
				surveyRecord.setStatus(2);
				surveyRecord.setViewResultStatus(0);
				//
				Long surveyRecordId = surveyManager
						.addSurveyRecord(surveyRecord);
				Long surveyId = surveyRecord.getSurveyId();
				// 初始化此次活动问题
				surveyManager.initQuestionItemRecords(surveyRecordId, surveyId);
			} else {
				surveyManager.saveSurveyRecord(surveyRecord);
			}
			model.put("sid", surveyRecord.getSurveyId());
			model.put("rs", "success");
		} catch (Exception ex) {
			model.put("rs", "failed");
			model.put("msgs", ex);
		}
		return mv;
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {

	}

	protected Object formBackingObject(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sid = request.getParameter("sid");
		SurveyRecord surveyRecord = null;
		if (id != null) {
			Long iid = new Long(id);
			surveyRecord = surveyManager.getSurveyRecordById(iid);
		} else {
			Long isid = new Long(sid);
			surveyRecord = new SurveyRecord();
			
			surveyRecord.setSurveyId(isid);
		}
		return surveyRecord;
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map model = new HashMap();
		List surveyTypes = getSurveyTypeConstants();
		model.put("surveyTypes", surveyTypes);
		SurveyRecord surveyRecord = (SurveyRecord) command;
		if (surveyRecord != null) {
			Long surveyId = surveyRecord.getSurveyId();
			Survey survey = surveyManager.getSurveyById(surveyId);
			Long nodeId=survey.getNodeId();
			surveyRecord.setNodeId(nodeId);
			model.put("survey", survey);
		}
		return model;
	}

	public List getSurveyTypeConstants() {
		List dd = DirectoryListDataLoader
				.load("/plugin/survey/surveytype_constant.xml");
		return dd;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	public void setResultViewName(String resultViewName) {
		this.resultViewName = resultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

}
