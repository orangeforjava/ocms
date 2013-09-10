/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openuap.cms.survey.action.statics;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.UserAwareAction;
import org.openuap.cms.survey.bean.QuestionItemResultBean;
import org.openuap.cms.survey.bean.QuestionResultBean;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionItem;
import org.openuap.cms.survey.model.QuestionItemRecord;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.model.SurveyRecord;
import org.openuap.cms.survey.xml.SurveyXMLService;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 调查问卷统计控制器.
 * </p>
 * 
 * <p>
 * $Id: SurveyStaticsAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SurveyStaticsAction extends UserAwareAction {

	private QuestionManager questionManager;

	private SurveyManager surveyManager;

	private SurveyXMLService surveyXMLService;
	//
	private String defaultScreensPath;

	private String surveyResultViewName;

	private String questionDetailViewName;

	private String resultViewName;

	public SurveyStaticsAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/survey/screens/statics/";
		surveyResultViewName = defaultScreensPath + "survey_result.html";
		questionDetailViewName = defaultScreensPath + "question_result.html";
		resultViewName = defaultScreensPath + "statics_result.html";
	}

	/**
	 * 显示调查统计结果,以整个调查方式
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String sid = request.getParameter("sid");
		String rid = request.getParameter("rid");
		ModelAndView mv = new ModelAndView(surveyResultViewName, model);
		if (sid != null && rid != null) {

			Long id = new Long(sid);
			Long irid = new Long(rid);
			Survey survey = surveyManager.getSurveyById(id);
			SurveyRecord surveyRecord = surveyManager.getSurveyRecordById(irid);
			QueryInfo qi = new QueryInfo();
			PageBuilder pb = new PageBuilder();
			List questions = questionManager.getQuestionList(id, qi, pb);
			List questionResultList = new ArrayList();
			if (questions != null) {
				int size = questions.size();
				for (int i = 0; i < size; i++) {
					Question q = (Question) questions.get(i);
					// 获得统计结果
					QuestionResultBean qrs = this.getQuestionResult(irid, q);
					questionResultList.add(qrs);
				}
			}
			model.put("survey", survey);
			model.put("questionResultList", questionResultList);
			model.put("rid", rid);
		}
		return mv;
	}
	/**
	 * 导出调查统计结果
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doExportStatics(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String sid = request.getParameter("sid");
		String rid = request.getParameter("rid");
		if (sid != null && rid != null) {
			try {
				Long id = new Long(sid);
				Long irid = new Long(rid);
				String rs = surveyXMLService.exportSurveyStatics(irid, id);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/xml");
				setNoCacheHeader(response);
				PrintWriter writer = response.getWriter();
				writer.print(rs);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 从问题中获得结果
	 * 
	 * @param q
	 * @return
	 */
	private QuestionResultBean getQuestionResult(Long rid, Question q) {
		QuestionResultBean questionResultBean = new QuestionResultBean();
		//
		questionResultBean.setQuestion(q);
		int type = q.getQuestionType();
		Set items = q.getQuestionItems();
		int questionItemsTotalCount = 0;
		List rs = new ArrayList();
		if (type == 3) {
			questionItemsTotalCount = surveyManager
					.getQuestionAnswerTotalCount(rid, q.getQuestionId());
		} else {
			if (items != null) {

				questionItemsTotalCount = surveyManager
						.getQuestionItemsTotalCount(rid, q.getQuestionId());
				Iterator itemIterator = items.iterator();

				while (itemIterator.hasNext()) {
					QuestionItem qi = (QuestionItem) itemIterator.next();
					QuestionItemResultBean qiResult = new QuestionItemResultBean();
					qiResult.setQuestionItemText(qi.getQuestionItemText());
					QuestionItemRecord qiRecord = surveyManager
							.getQuestionItemRecord(rid, qi.getQuestionItemId());
					float votePercent = (float) Math
							.round(((float) qiRecord
									.getQuestionItemPolledTimes().intValue() / (float) questionItemsTotalCount) * 100F * 100F) / 100F;
					qiResult.setQuestionPercent(votePercent);
					qiResult.setPolledTimes(qiRecord
							.getQuestionItemPolledTimes().intValue());
					rs.add(qiResult);

				}
			}
		}
		questionResultBean.setQuestionItemResultList(rs);
		questionResultBean.setQuestionItemsCount(questionItemsTotalCount);
		return questionResultBean;
	}

	/**
	 * 显示输入问题的详情
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doShowQuestionDetail(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = null;
		// 目前只允许系统人员查看结果
		if (this.getUserSession() != null
				&& (this.getUserSession().isSysUser() || this.getUserSession()
						.isAdmin())) {
			mv = new ModelAndView(questionDetailViewName);
			// 问题id
			Long questionId = helper.getLong("qid", 0L);
			// 调查活动id
			Long surveyRecordId = helper.getLong("rid", 0L);
			if (questionId != 0 && surveyRecordId != 0) {
				Question question = questionManager.getQuestionById(questionId);
				QueryInfo qi = new QueryInfo();
				PageBuilder pb = new PageBuilder();
				List answers = questionManager.getAnswerByQuestion(questionId,
						surveyRecordId, qi, pb);
				model.put("answers", answers);
				model.put("question", question);
				model.put("pb", pb);
			}
		} else {
			mv = new ModelAndView(resultViewName);
		}
		return mv;
	}

	public void setSurveyResultViewName(String surveyResultViewName) {
		this.surveyResultViewName = surveyResultViewName;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setSurveyXMLService(SurveyXMLService surveyXMLService) {
		this.surveyXMLService = surveyXMLService;
	}

}
