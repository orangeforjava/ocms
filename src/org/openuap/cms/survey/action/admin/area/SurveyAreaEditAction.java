/**
 * $Id: SurveyAreaEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.area;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.CMSBaseFormAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.survey.manager.SurveyAreaManager;
import org.openuap.cms.survey.model.SurveyArea;
import org.openuap.cms.util.ui.PublishMode;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Joseph
 * 
 */
public class SurveyAreaEditAction extends CMSBaseFormAction {

	private String defaultScreensPath;
	private String defaultViewName;
	//
	private String operationViewName;
	
	private SurveyAreaManager surveyAreaManager;
	private NodeManager nodeManager;

	public SurveyAreaEditAction() {
		initDefaultViewName();
	}

	public void setSurveyAreaManager(SurveyAreaManager surveyAreaManager) {
		this.surveyAreaManager = surveyAreaManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/survey/screens/area/";
		defaultViewName = defaultScreensPath + "area_list.html";
		operationViewName = defaultScreensPath + "area_operation_result.html";
		//
		this.setFormView(defaultScreensPath + "area_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(SurveyArea.class);
		this.setCommandName("area");
	}

	/**
	 * 提交附加发布
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		ModelAndView mv = new ModelAndView(operationViewName, model);
		SurveyArea surveyArea = (SurveyArea) command;
		String nodeId = request.getParameter("nodeId");
		
		model.put("nodeId", surveyArea.getNodeId());
		try {
			if(surveyArea.getId()!=null&&!surveyArea.getId().equals(0L)){
				//编辑
				model.put("op", "edit");
				long modifiedDate = System.currentTimeMillis();
				surveyArea.setLastModifiedDate(modifiedDate);
				surveyArea.setLastModifiedUserId(this.getUser().getUserId());
				surveyArea.setLastModifiedUserName(this.getUserSession().getName());
				
				surveyAreaManager.saveArea(surveyArea);
			}else{
				//新增
				model.put("op", "add");
				long now = System.currentTimeMillis();
				surveyArea.setCreationDate(now);
				surveyArea.setCreationUserId(this.getUser().getUserId());
				surveyArea.setCreationUserName(this.getUserSession().getName());
				surveyArea.setPublishState(0);
				surveyArea.setStatus(0);
				surveyArea.setLastModifiedDate(now);
				surveyArea.setLastModifiedUserId(this.getUser().getUserId());
				surveyArea.setLastModifiedUserName(this.getUserSession().getName());
				surveyArea.setPublishDate(0L);
				surveyArea.setPos(0);
				//
				Long id=surveyAreaManager.addArea(surveyArea);
				surveyArea.setId(id);
			}
			model.put("rs", "success");
		} catch (Exception e) {
			model.put("rs", "failed");
			model.put("msgs", e.getMessage());
		}
		return mv;
	}
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		//TODO
		
	}
	protected Object formBackingObject(HttpServletRequest request) {
		String mode = request.getParameter("mode");
		String nodeId = request.getParameter("nodeId");
		String id = request.getParameter("id");
		Long nid = new Long(nodeId);
		SurveyArea surveyArea=null;
		if (mode == null || mode.equals("add")) {
			surveyArea=new SurveyArea();
			surveyArea.setNodeId(nid);
		}else{
			Long aid = new Long(id);
			surveyArea=surveyAreaManager.getAreaById(aid);
		}
		return surveyArea;
	}
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		String mode = request.getParameter("mode");
		String nodeId = request.getParameter("nodeId");
		Node node = nodeManager.getNode(new Long(nodeId));
		Map model = new HashMap();
		model.put("mode", mode);
		model.put("nodeId", nodeId);
		model.put("node", node);
		model.put("publishModes", PublishMode.DEFAULT_MODES);
		model.put("nodeManager", nodeManager);
		return model;
	}
}
