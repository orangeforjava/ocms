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
package org.openuap.cms.survey.action.poll;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.action.UserAwareAction;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Answer;
import org.openuap.cms.survey.model.AnswerItem;
import org.openuap.cms.survey.model.SurveyAnswer;
import org.openuap.cms.survey.model.Voter;
import org.openuap.cms.user.model.IUser;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.web.servlet.ModelAndView;

/**
 * 单页面调查控制器
 * 
 * <p>
 * $Id: SimpleOnePagePollAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @ersion4.0
 */
public class SimpleOnePagePollAction extends UserAwareAction {

	public static final String SESSION_VOTER = "uap.voter";
	public static final String SURVEY_ANSWER = "surveyAnswer";

	public static final String QUESTION_IDS = "questionIds";

	public static final String SURVEY_ID = "sid";
	protected QuestionManager questionManager;

	protected SurveyManager surveyManager;
	protected String resultViewName;
	protected String defaultScreensPath;

	public SimpleOnePagePollAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/survey/screens/poll/";
		resultViewName = defaultScreensPath + "poll_result.html";
	}

	@Override
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		ModelAndView mv = new ModelAndView(resultViewName, model);
		HttpSession session = request.getSession(true);
		//更新答案
		upateAnswer(request, session);
		Object obj = session.getAttribute(SURVEY_ANSWER);
		String surveyId = request.getParameter("sid");
		String recordId = request.getParameter("rid");
		//
		Voter voter = this.getVoter(request);
		SurveyAnswer surveyAnswer = (SurveyAnswer) obj;
		surveyAnswer.setVoter(voter);
		//
		Long voterId = null;
		if (obj != null && surveyId != null) {
			try {
				Long sid = new Long(surveyId);
				Long rid = new Long(recordId);
				voter.setVoterPollDate(new Long(System.currentTimeMillis()));
				voterId = this.getQuestionManager().addVoter(voter);
				// 保存答案
				Map questionAnswers = surveyAnswer.getQuestionAnswers();
				Set qkey = questionAnswers.keySet();
				Iterator qiterator = qkey.iterator();

				while (qiterator.hasNext()) {
					Long questionId = (Long) qiterator.next();
					Answer voterAnswer = (Answer) questionAnswers
							.get(questionId);
					voterAnswer.setSurveyId(sid);
					voterAnswer.setVoterId(voterId);
					voterAnswer.setSurveyRecordId(rid);
					this.getQuestionManager().addAnswer(voterAnswer);
					// 更新选项选择次数
					String answer = voterAnswer.getAnswer();
					if (answer != null && !answer.equals("")) {
						//
						this.getSurveyManager()
								.updateQuestionItems(rid, answer);
					}
				}
				// 保存答案选项值
				Map questionAnswerItems = surveyAnswer.getQuestionItemAnswers();
				Set qikey = questionAnswerItems.keySet();
				Iterator qiIterator = qikey.iterator();
				while (qiIterator.hasNext()) {
					Long itemId = (Long) qiIterator.next();
					AnswerItem voterAnswerItem = (AnswerItem) questionAnswerItems
							.get(itemId);
					voterAnswerItem.setSurveyId(sid);
					voterAnswerItem.setVoterId(voterId);
					voterAnswerItem.setSurveyRecordId(rid);
					this.getQuestionManager().addAnswerItem(voterAnswerItem);
				}
				// 更新投票者状态
				session.setAttribute(SESSION_VOTER, voter);
				session.removeAttribute(SURVEY_ANSWER);
				model.put("rs", "success");
				model.put("sid", surveyId);
				model.put("rid", recordId);
			} catch (Exception e) {
				e.printStackTrace();
				model.put("rs", "failed");
				model.put("msgs", e);
				session.removeAttribute(SURVEY_ANSWER);
				if (voterId != null) {
					this.getQuestionManager().deleteAnswerByVoter(voterId);
					this.getQuestionManager().deleteAnswerItemByVoter(voterId);
					//
					voter.setVoterId(voterId);
					this.getQuestionManager().deleteVoter(voter);
				}
			}
		}
		model.put("sid", surveyId);
		return mv;
	}
	/**
	 * 更新答案
	 * @param request
	 * @param session
	 * @return
	 */
	protected String upateAnswer(HttpServletRequest request, HttpSession session) {
		String questionIds = request.getParameter(QUESTION_IDS);
		String surveyId = request.getParameter(SURVEY_ID);
		String surveyRecordId = request.getParameter("rid");
		if (surveyId == null) {
			surveyId = "-1";
		}

		if (questionIds != null) {
			//通过Session来保存由于换页导致的调查结果持久问题
			SurveyAnswer surveyAnswer = null;
			Object obj = session.getAttribute(SURVEY_ANSWER);
			if (obj != null) {
				surveyAnswer = (SurveyAnswer) obj;
			}
			if (surveyAnswer == null) {
				surveyAnswer = new SurveyAnswer();
			}
			// 获得题号
			String[] questionIdArray = questionIds.split(",");
			for (int i = 0; i < questionIdArray.length; i++) {
				String questionId = questionIdArray[i];
				//获得题目附加输入
				String question_input = request.getParameter(questionId
						+ "_q_input");
				//获得题目中选项ID数组
				String[] itemIds = request.getParameterValues(questionId);
				String items = "";
				if (itemIds != null) {
					for (int j = 0; j < itemIds.length; j++) {
						String itemId = itemIds[j];
						if (!itemId.equals("")) {
							//检查每个选项的输入
							String item_input = request.getParameter(itemId
									+ "_i_input");
							Long litemId = new Long(itemId);
							AnswerItem vai = new AnswerItem();
							vai.setQuestionId(new Long(questionId));
							vai.setQuestionItemId(litemId);
							vai.setSurveyId(new Long(surveyId));
							vai.setSurveyRecordId(new Long(surveyRecordId));
							if (item_input != null) {

								//
								vai.setAnswerItemInputText(item_input);
								surveyAnswer.getQuestionItemAnswers().put(
										litemId, vai);
							}

							//
							items += itemId + ",";
						}
					}
					if (!items.equals("")) {
						items = items.substring(0, items.length() - 1);
					}
				}
				//
				//题目答案
				Answer va = new Answer();
				va.setAnswer(items);
				va.setAnswerInputText(question_input);
				va.setQuestionId(new Long(questionId));
				va.setSurveyId(new Long(surveyId));
				va.setSurveyRecordId(new Long(surveyRecordId));
				//
				surveyAnswer.getQuestionAnswers().put(new Long(questionId), va);

			}
			//更新Session内答案对象
			session.setAttribute(SURVEY_ANSWER, surveyAnswer);
		}
		return "";
	}
	/**
	 * 从请求中获取用户信息
	 * @param request
	 * @return
	 */
	protected Voter getVoter(HttpServletRequest request) {
		String sid = request.getParameter("sid");
		String rid = request.getParameter("rid");
		Voter voter = new Voter();
		IUser user = getUser();
		// System.out.println("member="+member);
		if (user != null&&isLogin()) {
			voter.setVoterCompany("");
			voter.setVoterContact(user.getTitle());
			voter.setVoterDepartment("");
			voter.setVoterEmail(user.getEmail());
			voter.setVoterFax("");
			voter.setVoterMobile("");
			voter.setVoterName(user.getName());
			voter.setVoterTel("");
			voter.setVoterUserId(new Long(user.getUserId().intValue()));

		} else {
			voter.setVoterName("匿名");
			voter.setVoterUserId(new Long(0));
		}
		voter.setSurveyRecordId(new Long(rid));
		voter.setVoterIpaddress(request.getRemoteAddr());
		if (sid != null) {
			voter.setVoterSurveyId(new Long(sid));
		}
		return voter;
	}

	public QuestionManager getQuestionManager() {
		if (questionManager == null) {
			questionManager = (QuestionManager) ObjectLocator.lookup(
					"questionManager", CmsPlugin.PLUGIN_ID);
			//
		}
		return questionManager;
	}

	public SurveyManager getSurveyManager() {
		if (surveyManager == null) {
			surveyManager = (SurveyManager) ObjectLocator.lookup(
					"surveyManager", CmsPlugin.PLUGIN_ID);
			//
		}
		return surveyManager;
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
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
