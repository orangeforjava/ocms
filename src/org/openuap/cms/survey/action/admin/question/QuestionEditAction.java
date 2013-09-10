/**
 * $Id: QuestionEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.question;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.resource.DirectoryListDataLoader;
import org.openuap.cms.core.action.UserAwareFormAction;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionItem;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Title:QuestionEditAction
 * </p>
 * 
 * <p>
 * Description:问题编辑控制器.
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
public class QuestionEditAction extends UserAwareFormAction {
	
	private String defaultScreensPath;

	private String resultViewName;

	//
	private QuestionManager questionManager;

	private SurveyManager surveyManager;

	//
	public QuestionEditAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/survey/screens/question/";
		resultViewName = defaultScreensPath + "question_result.html";
		this.setFormView(defaultScreensPath + "question_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(Question.class);
		this.setCommandName("question");
	}

	/**
	 * 
	 * @param request
	 *            
	 * @param response
	 *            
	 * @param command
	 *            
	 * @param errors
	 *            
	 * @param helper
	 *            
	 * @param model
	 *            
	 * @return 
	 * @throws 
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		ModelAndView mv = new ModelAndView(resultViewName, model);
		model.put("op", "edit");
		try {
			Question question = (Question) command;
			if (question.getQuestionId() == null) {
				question.setCreationUserId(getUser().getUserId());
				question.setCreationUserName(getUser().getName());
				question.setQuestionCreationDate(new Long(System
						.currentTimeMillis()));
				question.setQuestionStatus(new Integer(0));
				Long id = questionManager.addQuestion(question);
				//
				model.put("id", id);
				//

			} else {
				questionManager.saveQuestion(question);
				model.put("id", question.getQuestionId());
			}
			model.put("rs", "success");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("rs", "failed");
			model.put("msgs", ex);
		}
		return mv;
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {

	}

	protected Object formBackingObject(HttpServletRequest request) {
		String id = request.getParameter("questionId");
		String sid = request.getParameter("sid");
		Question question = null;
		if (id != null) {
			Long iid = new Long(id);
			question = questionManager.getQuestionById(iid);

		} else {
			question = new Question();
			if (sid != null) {
				Long lsid = new Long(sid);
				int count = questionManager.getQuestionCountBySurvey(lsid);
				question.setSurveyId(lsid);
				question.setQuestionPos(new Long(count + 1));
				question.setGroupId(new Long(count + 1));
			}
		}
		return question;
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map model = new HashMap();
		Question question = (Question) command;
		if (question.getQuestionId() == null) {
			model.put("mode", "add");
		} else {
			model.put("mode", "edit");
			List items = questionManager.getQuestionItems(question
					.getQuestionId());
			model.put("items", items);
		}
		//
		List questionTypes = getQuestionTypeConstants();
		model.put("questionTypes", questionTypes);
		List questionFilters = getQuestionFilterConstants();
		model.put("questionFilters", questionFilters);
		return model;
	}

	public ModelAndView doAddQuestion(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String id = request.getParameter("questionId");
		String sid = request.getParameter("sid");
		if (id != null) {
			Long iid = new Long(id);
			Question question = questionManager.getQuestionById(iid);
			if (sid != null && question != null) {
				Long lsid = new Long(sid);
				int count = questionManager.getQuestionCountBySurvey(lsid);
				Question q = new Question();
				q.setCreationUserId(this.getUser().getUserId());
				q.setCreationUserName(this.getUser().getName());
				q.setGroupId(question.getGroupId());
				q.setGroupTitle(question.getGroupTitle());
				q.setPageId(question.getPageId());
				q.setQuestionCreationDate(new Long(System.currentTimeMillis()));
				q.setQuestionPos(new Long(count + 1));
				q.setQuestionStatus(new Integer(0));
				q.setQuestionTitle("[复制]" + question.getQuestionTitle());
				q.setQuestionType(question.getQuestionType());
				q.setSurveyId(lsid);
				Long qid = questionManager.addQuestion(q);
				q.setQuestionId(qid);
				// 添加选项
				Set items = question.getQuestionItems();
				if (items != null) {
					Iterator it = items.iterator();
					while (it.hasNext()) {
						QuestionItem qi = (QuestionItem) it.next();
						QuestionItem qi2 = new QuestionItem();
						qi2.setQuestion(q);
						qi2.setQuestionItemPolledTimes(new Integer(0));
						qi2.setQuestionItemSort(qi.getQuestionItemSort());
						qi2.setQuestionItemText(qi.getQuestionItemText());
						qi2.setQuestionItemType(qi.getQuestionItemType());
						questionManager.addQuestionItem(qi2);
					}
				}
				//
				helper.sendRedirect(helper.getBaseURL()
						+ "plugin/survey/questionEdit.jhtml?sid=" + sid
						+ "&questionId=" + qid);
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
	public ModelAndView doAddItem(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String questionId = request.getParameter("questionId");
		if (questionId != null) {
			Long qid = new Long(questionId);
			Question q = questionManager.getQuestionById(qid);
			if (q != null) {
				QuestionItem item = new QuestionItem();
				int i = questionManager.getItemsCount(qid);
				item.setQuestion(q);
				item.setQuestionItemPolledTimes(new Integer(0));
				item.setQuestionItemText("请输入选项内容");
				item.setQuestionItemType(new Integer(0));
				item.setQuestionItemSort(new Integer(i + 1));
				questionManager.addQuestionItem(item);
			}
		}
		return this.showNewForm(request, response, helper, model);
	}

	public ModelAndView doSaveItem(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String questionItemId = request.getParameter("questionItemId");
		String questionItemText = request.getParameter("questionItemText");
		String questionItemType = request.getParameter("questionItemType");
		String image=helper.getString("image","");
		String url=helper.getString("url","");
		//
		if (questionItemId != null) {
			Long qid = new Long(questionItemId);
			QuestionItem qi = questionManager.getQuestionItemById(qid);
			if (qi != null) {
				qi.setQuestionItemText(questionItemText);
				qi.setQuestionItemType(new Integer(questionItemType));
				qi.setImage(image);
				qi.setUrl(url);
				//
				questionManager.saveQuestionItem(qi);
			}
		}
		return this.showNewForm(request, response, helper, model);
	}

	public ModelAndView doDelItem(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String questionItemId = request.getParameter("questionItemId");
		String questionId = request.getParameter("questionId");
		if (questionItemId != null) {
			Long qid = new Long(questionItemId);
			Long id = new Long(questionId);
			QuestionItem qi = questionManager.getQuestionItemById(qid);
			if (qi != null) {
				Integer pos = qi.getQuestionItemSort();
				updateItemPos("del", id, qid, pos.intValue());
				questionManager.deleteQuestionItem(qi);
			}
		}
		return this.showNewForm(request, response, helper, model);
	}

	public ModelAndView doUpItem(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String questionItemId = request.getParameter("questionItemId");
		String questionId = request.getParameter("questionId");
		if (questionItemId != null) {
			Long qid = new Long(questionItemId);
			Long id = new Long(questionId);
			QuestionItem qi = questionManager.getQuestionItemById(qid);
			if (qi != null) {
				Integer pos = qi.getQuestionItemSort();
				updateItemPos("up", id, qid, pos.intValue());
			}
		}
		return this.showNewForm(request, response, helper, model);

	}

	public ModelAndView doDownItem(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String questionItemId = request.getParameter("questionItemId");
		String questionId = request.getParameter("questionId");
		if (questionItemId != null) {
			Long qid = new Long(questionItemId);
			Long id = new Long(questionId);
			QuestionItem qi = questionManager.getQuestionItemById(qid);
			if (qi != null) {
				Integer pos = qi.getQuestionItemSort();
				updateItemPos("down", id, qid, pos.intValue());
			}
		}
		return this.showNewForm(request, response, helper, model);

	}

	private void updateItemPos(String action, Long questionId,
			Long questionItemId, int pos) {
		if (action == null) {
			return;
		}
		if (action.equals("del")) {
			int totalCount = questionManager.getItemsCount(questionId);
			if (pos >= 0 && pos < totalCount) {
				questionManager
						.executeHql(
								"update QuestionItem qi set qi.questionItemSort=qi.questionItemSort-1 where qi.question.questionId="
										+ questionId
										+ " and qi.questionItemSort>" + pos,
								null);
			}
		} else if (action.equals("up")) {
			int totalCount = questionManager.getItemsCount(questionId);
			if (pos > 1 && pos <= totalCount) {
				questionManager
						.executeHql(
								"update QuestionItem qi set qi.questionItemSort=qi.questionItemSort+1 where qi.questionItemSort="
										+ (pos - 1)
										+ " and qi.question.questionId="
										+ questionId, null);
				questionManager
						.executeHql(
								"update QuestionItem qi set qi.questionItemSort=qi.questionItemSort-1 where qi.questionItemId="
										+ questionItemId, null);
			}
		} else if (action.equals("down")) {
			int totalCount = questionManager.getItemsCount(questionId);
			if (pos >= 1 && pos < totalCount) {
				questionManager
						.executeHql(
								"update QuestionItem qi set qi.questionItemSort=qi.questionItemSort-1 where qi.questionItemSort="
										+ (pos + 1)
										+ " and qi.question.questionId="
										+ questionId, null);
				questionManager
						.executeHql(
								"update QuestionItem qi set qi.questionItemSort=qi.questionItemSort+1 where qi.questionItemId="
										+ questionItemId, null);
			}

		}

	}

	public List getQuestionTypeConstants() {
		List dd = DirectoryListDataLoader
				.load("/plugin/survey/questiontype_constant.xml");
		return dd;
	}

	public List getQuestionFilterConstants() {
		List dd = DirectoryListDataLoader
				.load("/plugin/survey/questionfilter_constant.xml");
		return dd;
	}

	public void setResultViewName(String resultViewName) {
		this.resultViewName = resultViewName;
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}
}
