/**
 * $Id: SurveyRecordAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.survey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.UserAwareAction;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Survey;
import org.springframework.web.servlet.ModelAndView;

/**
 * 调查实例控制器
 * @author Joseph
 * 
 */
public class SurveyRecordAction extends UserAwareAction {

	private SurveyManager surveyManager;

	private String defaultViewName;

	private String defaultScreensPath;

	private String framesetViewName;

	private String headerViewName;

	private String listViewName;

	private String surveyViewName;

	private String resultViewName;

	/**
	 * 
	 */
	public SurveyRecordAction() {

		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/survey/screens/record/";
		defaultViewName = defaultScreensPath + "index.html";
		framesetViewName = defaultScreensPath + "survey_record_frameset.html";
		headerViewName = defaultScreensPath + "survey_record_header.html";
		listViewName = defaultScreensPath + "survey_record_list.html";
		surveyViewName = defaultScreensPath + "survey_record_view.html";
		resultViewName = defaultScreensPath + "survey_record_result.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(framesetViewName, model);
		String sid = helper.getString("sid");
		model.put("sid", sid);
		return mv;
	}

	public ModelAndView doHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(headerViewName, model);
		String sid = helper.getString("sid");
		if (sid != null) {
			Survey survey = surveyManager.getSurveyById(new Long(sid));
			model.put("survey", survey);
		}
		model.put("sid", sid);
		return mv;
	}

	/**
	 * 显示调查问卷活动列表
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(listViewName, model);
		//
		String sid = helper.getString("sid");
		if (sid != null) {
			Survey survey = surveyManager.getSurveyById(new Long(sid));
			model.put("survey", survey);
		}
		model.put("sid", sid);
		//
		String column_condition = "";
		//
		String where = request.getParameter("where");
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		String order_mode = request.getParameter("order_mode");
		String order_name = request.getParameter("order_name");
		//
		String audit = request.getParameter("audit");

		//
		String creationDate = request.getParameter("creationDate");
		String creationDate2 = request.getParameter("creationDate2");
		// get the keyword
		String tmp = request.getParameter("keyword");
		if (tmp == null) {
			tmp = "";
		}
		//
		//
		String keyword = tmp;
		//
		String fields = helper.getString("fields", "");
		//
		Integer start = new Integer(0);
		Integer limit = new Integer(10);
		if (where == null) {
			where = "";
		}
		if (order == null) {
			order = "";
		}
		if (order_mode == null) {
			order_mode = "";
		}
		if (order_name == null) {
			order_name = "";
		}
		order_name = order_name.replaceAll("\\^", "");
		//
		String final_order = "";
		if (!order.equals("") && !order_mode.equals("")) {
			final_order = order + " " + order_mode;
		}
		if (pageNum != null) {
			limit = new Integer(pageNum);
		} else {
			pageNum = "10";
		}
		if (page != null) {
			start = new Integer((Integer.parseInt(page) - 1) * limit.intValue());
		} else {
			page = "1";
		}

		//
		if (fields != null && !fields.equals("")) {
			String columns[] = fields.split(",");
			if (columns != null) {
				for (int i = 0; i < columns.length; i++) {
					column_condition += " or e." + columns[i] + " like '%"
							+ keyword + "%'";

				}
				if (!column_condition.equals("")) {
					column_condition = column_condition.substring(4);
					column_condition = " (" + column_condition + ")";
				}
			}
		}
		//
		where += column_condition;
		// other where condition

		// creationDate
		if (creationDate != null && !creationDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate);
			where += " and e.startDate>=" + dd.getTime() + "";
		} else {
			creationDate = "";
		}
		// creationDate2
		if (creationDate2 != null && !creationDate2.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate2);
			where += " and e.startDate<=" + dd.getTime() + "";
		} else {
			creationDate2 = "";
		}
		//
		where += " and e.surveyId=" + sid;
		//
		PageBuilder pb = new PageBuilder(limit.intValue());
		QueryInfo qi = new QueryInfo(where, final_order, limit, start);
		//
		List surveys = surveyManager.getSurveyRecords(qi, pb);
		pb.page(Integer.parseInt(page));
		model.put("surveyRecords", surveys);
		model.put("pb", pb);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("order_mode", order_mode);
		model.put("order_name", order_name);
		model.put("where", where);
		model.put("action", this);
		// add some search parameter
		model.put("keyword", keyword);
		model.put("audit", audit);
		//
		model.put("creationDate", creationDate);
		model.put("creationDate2", creationDate2);
		//
		//
		return mv;
	}

	/**
	 * 初始化问题选项的定义
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doInitQuestion(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(resultViewName, model);
		String sid = helper.getString("sid");
		String id = request.getParameter("id");
		String op="initQuestion";
		String rs="success";
		try {
			if(id!=null&&sid!=null){
				Long iid=new Long(id);
				Long isid=new Long(sid);
				surveyManager.initQuestionItemRecords(iid, isid);
			}
		} catch (Exception e) {
			rs="failed";
			String msgs=e.getMessage();
			e.printStackTrace();
		}
		model.put("rs", rs);
		model.put("op", op);
		model.put("sid", sid);
		return mv;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setFramesetViewName(String framesetViewName) {
		this.framesetViewName = framesetViewName;
	}

	public void setHeaderViewName(String headerViewName) {
		this.headerViewName = headerViewName;
	}

	public void setListViewName(String listViewName) {
		this.listViewName = listViewName;
	}

	public void setSurveyViewName(String surveyViewName) {
		this.surveyViewName = surveyViewName;
	}
}
