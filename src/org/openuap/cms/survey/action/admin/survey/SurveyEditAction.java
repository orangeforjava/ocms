/**
 * $Id: SurveyEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */

package org.openuap.cms.survey.action.admin.survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.resource.DirectoryListDataLoader;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.action.UserAwareFormAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.survey.event.SurveyEvent;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Survey;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Title: SurveyEditAction
 * </p>
 * 
 * <p>
 * Description: 问卷调查编辑控制器.
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
public class SurveyEditAction extends UserAwareFormAction {
	//
	private SurveyManager surveyManager;

	//
	private String defaultScreensPath;

	private String resultViewName;
	
	private NodeManager nodeManager;

	public NodeManager getNodeManager() {
		if(nodeManager==null){
			nodeManager=(NodeManager) ObjectLocator.lookup("nodeManager",CmsPlugin.PLUGIN_ID);
		}
		return nodeManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	//
	public SurveyEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/survey/screens/";
		resultViewName = defaultScreensPath + "survey_result.html";
		this.setFormView(defaultScreensPath + "survey_edit.html");
		this.setSuccessView(resultViewName);
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(Survey.class);
		this.setCommandName("survey");
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		ModelAndView mv = new ModelAndView(resultViewName, model);
		model.put("op", "edit");
		try {
			Survey survey = (Survey) command;
			if (survey.getSurveyId() == null) {
				//
				survey.setCreationUserId(this.getUser().getUserId());
				survey.setCreationUserName(this.getUser().getName());
				survey.setSurveyCreationDate(new Long(System
						.currentTimeMillis()));
				survey.setSurveyStatus(1);
				survey.setDeleted(0);
				//
				//
				Long id = surveyManager.addSurvey(survey);
				survey.setSurveyId(id);
				SurveyEvent event = new SurveyEvent(SurveyEvent.SUVEY_CREATED,
						survey, new HashMap(), this);
				WebPluginManagerUtils.dispatcherEvent(true, "base", event);
			} else {
				surveyManager.saveSurvey(survey);
				SurveyEvent event = new SurveyEvent(SurveyEvent.SUVEY_UPDATED,
						survey, new HashMap(), this);
				WebPluginManagerUtils.dispatcherEvent(true, "base", event);
			}
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
		String nodeId=request.getParameter("nodeId");
		Survey survey = null;
		if (id != null) {
			Long iid = new Long(id);
			survey = surveyManager.getSurveyById(iid);
		} else {
			survey = new Survey();
			if(nodeId==null){
				nodeId="0";
			}
			Long nid=new Long(nodeId);
			survey.setNodeId(nid);
		}
		return survey;
	}

	protected Map referenceData(HttpServletRequest request, ControllerHelper helper,Object command,
			Errors errors ) throws Exception {
		//
		Map model = new HashMap();
		Long nodeId=helper.getLong("nodeId", 0L);
		List surveyTypes = getSurveyTypeConstants();
		model.put("surveyTypes", surveyTypes);
		model.put("nodeId", nodeId);
		model.put("nodeManager",this.getNodeManager());
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
