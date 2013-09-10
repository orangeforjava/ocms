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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.action.UserAwareWizardFormAction;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Answer;
import org.openuap.cms.survey.model.AnswerItem;
import org.openuap.cms.survey.model.SurveyAnswer;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 抽象投票向导控制器.
 * </p>
 * 
 * <p>
 * $Id: PollWizardAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PollWizardAction extends UserAwareWizardFormAction {

	public static final String SURVEY_ANSWER = "surveyAnswer";

	public static final String QUESTION_IDS = "questionIds";

	public static final String SURVEY_ID = "sid";

	protected QuestionManager questionManager;

	protected SurveyManager surveyManager;

	public PollWizardAction() {
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException bindException, ControllerHelper helper, Map model)
			throws Exception {
		return null;
	}
	/**
	 * 获得每个问题输入文本
	 * @param itemId
	 * @param session
	 * @return
	 */
	public String getItemInputText(Long itemId, HttpSession session) {
		Object obj = session.getAttribute(SURVEY_ANSWER);
		if (obj != null) {
			SurveyAnswer surveyAnswer = (SurveyAnswer) obj;
			AnswerItem AnswerItem = (AnswerItem) surveyAnswer.getQuestionItemAnswers().get(itemId);
			if (AnswerItem != null) {
				String result = AnswerItem.getAnswerItemInputText();
				int start = result.indexOf("-");
				if (start > 0 && start != result.length() - 1) {
					result = result.substring(start + 1, result.length());
				}
				return result;
			}
		}
		return "";
	}

	protected String upateAnswer(HttpServletRequest request, HttpSession session) {
		String questionIds = request.getParameter(QUESTION_IDS);
		String surveyId = request.getParameter(SURVEY_ID);
		String surveyRecordId=request.getParameter("rid");
		if (surveyId == null) {
			surveyId = "-1";
		}

		if (questionIds != null) {
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
				String question_input = request.getParameter(questionId + "_q_input");
				String[] itemIds = request.getParameterValues(questionId);
				String items = "";
				if (itemIds != null) {
					for (int j = 0; j < itemIds.length; j++) {
						String itemId = itemIds[j];
						if (!itemId.equals("")) {
							String item_input = request.getParameter(itemId + "_i_input");
							Long litemId = new Long(itemId);
							AnswerItem vai = new AnswerItem();
							vai.setQuestionId(new Long(questionId));
							vai.setQuestionItemId(litemId);
							vai.setSurveyId(new Long(surveyId));
							vai.setSurveyRecordId(new Long(surveyRecordId));
							if (item_input != null) {

								//
								vai.setAnswerItemInputText(item_input);
								surveyAnswer.getQuestionItemAnswers().put(litemId, vai);
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

				//
				Answer va = new Answer();
				va.setAnswer(items);
				va.setAnswerInputText(question_input);
				va.setQuestionId(new Long(questionId));
				va.setSurveyId(new Long(surveyId));
				va.setSurveyRecordId(new Long(surveyRecordId));
				//
				surveyAnswer.getQuestionAnswers().put(new Long(questionId), va);

			}
			session.setAttribute(SURVEY_ANSWER, surveyAnswer);
		}
		return "";
	}

	public String getItemInputTextChecked(Long itemId, String input, HttpSession session) {
		Object obj = session.getAttribute(SURVEY_ANSWER);
		if (obj != null) {
			SurveyAnswer surveyAnswer = (SurveyAnswer) obj;
			AnswerItem AnswerItem = (AnswerItem) surveyAnswer.getQuestionItemAnswers().get(itemId);
			if (AnswerItem != null) {
				String inputText = AnswerItem.getAnswerItemInputText();
				if (inputText != null) {
					if (input.endsWith("-")) {
						input.substring(0, input.length() - 1);
						if (inputText.startsWith(input)) {
							return " checked ";
						}
					} else {
						if (inputText.equals(input)) {
							return " checked ";
						}
					}
				}
			}
		}
		return "";
	}

	/**
	 * 返回选项是否已经被选择状态
	 * 
	 * @param questionId
	 *            问题id
	 * @param itemId
	 *            选项id
	 * @param session
	 *            用户session
	 * @return 选中返回checked，否则返回空字符串
	 */
	public String getItemChecked(Long questionId, Long itemId, HttpSession session) {
		//
		Object obj = session.getAttribute(SURVEY_ANSWER);
		if (obj != null) {
			SurveyAnswer surveyAnswer = (SurveyAnswer) obj;
			Answer Answer = (Answer) surveyAnswer.getQuestionAnswers().get(questionId);
			if (Answer != null) {
				String answer = Answer.getAnswer();
				if (answer != null) {
					String[] itemIds = answer.split(",");

					for (int i = 0; i < itemIds.length; i++) {
						String iid = itemIds[i];
						if (!iid.equals("")) {
							//
							Long lid = new Long(iid);
							if (itemId.equals(lid)) {
								return " checked ";
							}
						}
					}
				}
			}
		}
		return "";
	}

	/**
	 * 获得题目输入文本
	 * 
	 * @param questionId
	 *            问题id
	 * @param session
	 *            session对象
	 * @return String 
	 */
	public String getQuestionInputText(Long questionId, HttpSession session) {
		Object obj = session.getAttribute(SURVEY_ANSWER);
		if (obj != null) {
			SurveyAnswer surveyAnswer = (SurveyAnswer) obj;
			Answer Answer = (Answer) surveyAnswer.getQuestionAnswers().get(questionId);
			if (Answer != null) {
				return Answer.getAnswerInputText() != null ? Answer.getAnswerInputText() : "";
			}
		}
		return "";
	}

	public QuestionManager getQuestionManager() {
		if (questionManager == null) {
			questionManager = (QuestionManager) ObjectLocator.lookup("questionManager",
					CmsPlugin.PLUGIN_ID);
			//
		}
		return questionManager;
	}

	public SurveyManager getSurveyManager() {
		if (surveyManager == null) {
			surveyManager = (SurveyManager) ObjectLocator.lookup("surveyManager",
					CmsPlugin.PLUGIN_ID);
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

}
