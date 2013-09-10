/**
 * $Id: QuestionAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.question;

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
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionGroup;
import org.openuap.cms.survey.model.QuestionPage;
import org.openuap.cms.survey.model.Survey;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Title: QuestionAction
 * </p>
 * 
 * <p>
 * Description:问题控制器.
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
public class QuestionAction extends UserAwareAction {

	private QuestionManager questionManager;

	private SurveyManager surveyManager;

	private String defaultScreensPath;

	private String framesetViewName;

	private String headerViewName;

	private String listViewName;

	private String sortListViewName;

	private String viewQuestionViewName;

	private String viewGroupViewName;

	private String viewPageViewName;

	/**
	 * 
	 */
	public QuestionAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/survey/screens/question/";
		framesetViewName = defaultScreensPath + "question_frameset.html";
		headerViewName = defaultScreensPath + "question_header.html";
		listViewName = defaultScreensPath + "question_list.html";
		sortListViewName = defaultScreensPath + "question_sort.html";
		viewQuestionViewName = defaultScreensPath + "question_view.html";
		viewGroupViewName = defaultScreensPath + "group_view.html";
		viewPageViewName = defaultScreensPath + "page_view.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(framesetViewName, model);
		String sid = request.getParameter("sid");
		model.put("sid", sid);
		return mv;
	}

	public ModelAndView doHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(headerViewName, model);
		String sid = request.getParameter("sid");
		if (sid != null) {
			Long id = new Long(sid);
			Survey survey = surveyManager.getSurveyById(id);
			model.put("survey", survey);
		}
		return mv;
	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(listViewName, model);
		//
		String sid = request.getParameter("sid");

		if (sid != null) {
			Long id = new Long(sid);
			Survey survey = surveyManager.getSurveyById(id);
			model.put("survey", survey);
		}

		//

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
		Integer limit = new Integer(20);
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
			pageNum = "20";
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
		// audit
		if (audit != null) {
			if (!audit.equals("-1")) {
				where += " and e.audit=" + audit;
			}
		} else {
			audit = "-1";
		}

		// creationDate
		if (creationDate != null && !creationDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate);
			where += " and e.questionCreationDate>=" + dd.getTime() + "";
		} else {
			creationDate = "";
		}
		// creationDate2
		if (creationDate2 != null && !creationDate2.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate2);
			where += " and e.questionCreationDate<=" + dd.getTime() + "";
		} else {
			creationDate2 = "";
		}
		//
		//
		PageBuilder pb = new PageBuilder(limit.intValue());
		QueryInfo qi = new QueryInfo(where, final_order, limit, start);
		//
		List questions = null;
		if (sid != null) {
			questions = questionManager.getQuestionList(new Long(sid), qi, pb);
		} else {
			questions = questionManager.getQuestions(qi, pb);
		}
		pb.page(Integer.parseInt(page));
		model.put("questions", questions);
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
		return mv;
	}

	public ModelAndView doSortList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(sortListViewName, model);
		//
		String sid = request.getParameter("sid");

		if (sid != null) {
			Long id = new Long(sid);
			Survey survey = surveyManager.getSurveyById(id);
			model.put("survey", survey);
		}

		//

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
			pageNum = "20";
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
		// audit
		if (audit != null) {
			if (!audit.equals("-1")) {
				where += " and e.audit=" + audit;
			}
		} else {
			audit = "-1";
		}

		// creationDate
		if (creationDate != null && !creationDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate);
			where += " and e.questionCreationDate>=" + dd.getTime() + "";
		} else {
			creationDate = "";
		}
		// creationDate2
		if (creationDate2 != null && !creationDate2.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate2);
			where += " and e.questionCreationDate<=" + dd.getTime() + "";
		} else {
			creationDate2 = "";
		}
		//
		//
		PageBuilder pb = new PageBuilder(limit.intValue());
		QueryInfo qi = new QueryInfo(where, final_order, null, null);
		//
		List questions = null;
		if (sid != null) {
			questions = questionManager.getQuestionList(new Long(sid), qi, pb);
		} else {
			questions = questionManager.getQuestions(qi, pb);
		}
		pb.page(Integer.parseInt(page));
		model.put("questions", questions);
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
		return mv;
	}

	public ModelAndView doUp(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String sid = request.getParameter("sid");
		String questionId = request.getParameter("questionId");
		if (sid != null && questionId != null) {
			Long surveyId = new Long(sid);
			Long qid = new Long(questionId);
			Question q = questionManager.getQuestionById(qid);
			if (q != null) {

				updateQuestionPos("up", surveyId, qid, q.getQuestionPos()
						.intValue());
			}
		}
		return doSortList(request, response, helper, model);
	}

	public ModelAndView doDown(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String sid = request.getParameter("sid");
		String questionId = request.getParameter("questionId");
		if (sid != null && questionId != null) {
			Long surveyId = new Long(sid);
			Long qid = new Long(questionId);
			Question q = questionManager.getQuestionById(qid);
			if (q != null) {

				updateQuestionPos("up", surveyId, qid, q.getQuestionPos()
						.intValue());
			}
		}
		return doSortList(request, response, helper, model);
	}

	public ModelAndView doDel(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String sid = request.getParameter("sid");
		String questionId = request.getParameter("questionId");
		if (sid != null && questionId != null) {
			Long surveyId = new Long(sid);
			Long qid = new Long(questionId);
			Question q = questionManager.getQuestionById(qid);
			if (q != null) {
				updateQuestionPos("del", surveyId, qid, q.getQuestionPos()
						.intValue());
				questionManager.deleteQuestion(q);
			}
		}
		return doList(request, response, helper, model);
	}

	public ModelAndView doSaveQuestion(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String groupId = request.getParameter("groupId");
		String pageId = request.getParameter("pageId");
		String questionId = request.getParameter("questionId");
		if (groupId != null && questionId != null && pageId != null
				&& !pageId.equals("")) {
			Long gid = new Long(groupId);
			Long qid = new Long(questionId);
			Long pid = new Long(pageId);
			Question q = questionManager.getQuestionById(qid);
			if (q != null) {
				q.setGroupId(gid);
				q.setPageId(pid);
				questionManager.saveQuestion(q);
			}
		}
		return doSortList(request, response, helper, model);
	}

	/**
	 * 预览问题
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doPreviewQuestion(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		String questionId = request.getParameter("questionId");
		if (questionId != null && !questionId.equals("")) {
			Long qid = new Long(questionId);
			Question q = questionManager.getQuestionById(qid);
			if (q != null) {
				ModelAndView mv = new ModelAndView(viewQuestionViewName, model);
				model.put("question", q);
				return mv;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doPreviewGroup(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		String groupId = request.getParameter("groupId");
		String sid = request.getParameter("sid");
		//

		if (groupId != null && !groupId.equals("") && sid != null
				& !sid.equals("")) {
			Long gid = new Long(groupId);
			Long siid = new Long(sid);
			//
			QuestionGroup group = questionManager.getQuestionGroup(siid, gid);
			if (group != null) {
				ModelAndView mv = new ModelAndView(viewGroupViewName, model);
				model.put("questionGroup", group);
				return mv;
			}
		}
		return null;
	}

	/**
	 * 以页为单位预览
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doPreviewPage(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String pageId = request.getParameter("pageId");
		String sid = request.getParameter("sid");
		if (pageId != null && sid != null) {
			//
			ModelAndView mv = new ModelAndView(viewPageViewName, model);
			Long pid = new Long(pageId);
			Long surveyId = new Long(sid);
			QuestionPage qp = questionManager.getQuestionPage(surveyId, pid);
			model.put("questionPage", qp);
			return mv;
		}
		return null;
	}

	public ModelAndView doViewQuestionResult(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String questionId = request.getParameter("questionId");
		if (questionId != null && !questionId.equals("")) {
			Long qid = new Long(questionId);
			QueryInfo qi = new QueryInfo("", "", null, null);
			PageBuilder pb = new PageBuilder();
			// questionManager.getVoterById(new Long(1));
			List v = questionManager.getAnswerByQuestion(qid, qi, pb);
		}
		return null;
	}

	/**
	 * 
	 * @param action
	 *            String
	 * @param surveyId
	 *            Long
	 * @param questionId
	 *            Long
	 * @param pos
	 *            int
	 */
	private void updateQuestionPos(String action, Long surveyId,
			Long questionId, int pos) {
		if (action == null) {
			return;
		}
		if (action.equals("del")) {
			int totalCount = questionManager.getQuestionCountBySurvey(surveyId);
			if (pos >= 0 && pos < totalCount) {
				questionManager.executeHql(
						"update Question q set q.questionPos=q.questionPos-1 where q.questionPos>"
								+ pos + " and q.surveyId=" + surveyId, null);
			}
		} else if (action.equals("up")) {
			int totalCount = questionManager.getQuestionCountBySurvey(surveyId);
			if (pos > 1 && pos <= totalCount) {
				questionManager.executeHql(
						"update Question q set q.questionPos=q.questionPos+1 where q.questionPos="
								+ (pos - 1) + " and q.surveyId=" + surveyId,
						null);
				questionManager.executeHql(
						"update Question q set q.questionPos=q.questionPos-1 where q.questionId="
								+ questionId, null);
			}
		} else if (action.equals("down")) {
			int totalCount = questionManager.getItemsCount(questionId);
			if (pos >= 1 && pos < totalCount) {
				questionManager.executeHql(
						"update Question q set q.questionPos=q.questionPos-1 where q.questionPos="
								+ (pos + 1) + " and q.surveyId=" + surveyId,
						null);
				questionManager.executeHql(
						"update Question q set q.questionPos=q.questionPos+1 where q.questionId="
								+ questionId, null);
			}

		}

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

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}
}
