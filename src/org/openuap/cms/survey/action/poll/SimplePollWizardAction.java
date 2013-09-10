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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.survey.model.Answer;
import org.openuap.cms.survey.model.AnswerItem;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionItem;
import org.openuap.cms.survey.model.QuestionPage;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.model.SurveyAnswer;
import org.openuap.cms.survey.model.SurveyRecord;
import org.openuap.cms.survey.model.Voter;
import org.openuap.cms.user.model.IUser;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

/**
 * 简单投票向导控制器
 * 
 * <p>
 * $Id: SimplePollWizardAction.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class SimplePollWizardAction extends PollWizardAction {

	protected String defaultScreensPath;
	protected String resultViewName;
	protected String pollViewName;
	/** 若需要有最后一步填写用户信息，则设置这个视图.*/
	protected String endViewName;
	

	public static final String SESSION_AUTHORIZATION = "uap.authToken";
	public static final String SESSION_VOTER = "uap.voter";

	public SimplePollWizardAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		initScreensPath();	
		//
		setCommandName("voter");
		//单页面格式，在后面会重新设置其值
		setPages(new String[] { pollViewName });
	}

	protected void initScreensPath() {
		defaultScreensPath = "/plugin/cms/survey/screens/poll/";
		resultViewName = defaultScreensPath + "poll_result.html";
		pollViewName = defaultScreensPath + "poll.html";
		endViewName=null;
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = super.beforePerform(request, response, helper, model);
		if (mv != null) {
			return mv;
		}
		model.put("action", this);
		// 获得问卷id
		Long sid = helper.getLong("sid", 0L);
		if (sid == 0) {
			return this.errorPage(request, response, helper,
					"survey_not_exist", model);
		}

		Survey survey = getSurveyManager().getSurveyById(sid);
		if (survey == null) {
			return this.errorPage(request, response, helper,
					"survey_not_exist", model);
		}
		Long rid = helper.getLong("rid", 0L);
		if (rid == 0) {
			return this.errorPage(request, response, helper,
					"survey_record_not_exist", model);
		}
		SurveyRecord surveyRecord = getSurveyManager().getSurveyRecordById(rid);
		Integer type = surveyRecord.getType();
		if (type.equals(Survey.MEMBER_TYPE)) {
			// 目前不需要会员登录，但是如果是会员已经登录，则判断是否已经提交问卷
			if (!this.isLogin(request, response)) {
				return this.loginForm(request, response, helper);
			}
		}

		// 若已经填写调查问卷，则不能重复填写
		if (!isPermitPoll(request, response)) {
			//
			return this.errorPage(request, response, helper,
					"isnot_permit_poll", model);
		}
		return null;
	}

	/**
	 * 完成调查
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param command
	 * 
	 * @param bindException
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command,
			BindException bindException, ControllerHelper helper, Map model)
			throws Exception {
		HttpSession session = request.getSession(true);
		upateAnswer(request, session);
		//
		Object obj = session.getAttribute(SURVEY_ANSWER);
		String surveyId = request.getParameter("sid");
		String recordId = request.getParameter("rid");
		ModelAndView mv = new ModelAndView(resultViewName, model);
		//		
		Long voterId = null;
		Voter voter = (Voter) command;
		//
		try {
			if (obj != null && surveyId != null) {
				Long sid = new Long(surveyId);
				Long rid = new Long(recordId);
				IUser user = getUser();
				//
				if (user != null) {
					voter.setVoterCompany("");
					voter.setVoterContact(user.getTitle());
					voter.setVoterDepartment("");
					voter.setVoterEmail(user.getEmail());
					voter.setVoterFax("");
					voter.setVoterIpaddress(helper.getRealIP());
					voter.setVoterMobile("");
					voter.setVoterName(user.getName());
					voter.setVoterSurveyId(sid);
					voter.setSurveyRecordId(rid);
					voter.setVoterTel("");
					voter.setVoterUserId(new Long(user.getUserId().intValue()));
				}

				SurveyAnswer surveyAnswer = (SurveyAnswer) obj;
				surveyAnswer.setVoter(voter);
				//
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
						this.getSurveyManager().updateQuestionItems(rid,answer);
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
			}

			model.put("rs", "success");
			model.put("sid", surveyId);
			model.put("rid", recordId);
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("rs", "failed");
			model.put("msgs", ex);
			if (voterId != null) {
				this.getQuestionManager().deleteAnswerByVoter(voterId);
				this.getQuestionManager().deleteAnswerItemByVoter(voterId);
				// questionManager.updateQuestionItems2();
				voter.setVoterId(voterId);
				this.getQuestionManager().deleteVoter(voter);
			}
		}
		model.put("sid", surveyId);
		return mv;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws ModelAndViewDefiningException {
		String sid = request.getParameter("sid");
		String rid = request.getParameter("rid");
		Voter voter = new Voter();
		IUser user = getUser();
		// System.out.println("member="+member);
		if (user != null) {
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
		voter.setVoterSex("");
		voter.setVoterProp1("");
		voter.setVoterProp2("");
		voter.setVoterProp3("");
		voter.setVoterProp4("");
		voter.setVoterProp5("");
		voter.setVoterProp6("");
		voter.setVoterProp7("");
		voter.setVoterProp8("");
		voter.setVoterProp9("");
		voter.setVoterProp10("");
		//
		voter.setSurveyRecordId(new Long(rid));
		voter.setVoterIpaddress(request.getRemoteAddr());
		if (sid != null) {
			voter.setVoterSurveyId(new Long(sid));
		}
		Long id = new Long(sid);
		Survey survey = getSurveyManager().getSurveyById(id);
		List pages = getQuestionManager().getPages(id);
		int size = pages.size();
		String[] views = new String[size+1];
		for (int i = 0; i < size; i++) {
			views[i] = pollViewName;
		}
		//若最后一页需要填写用户信息
		if(endViewName!=null){
			views[views.length-1]=endViewName;
		}
		//重新设置页面信息
		this.setPages(views);
		return voter;
	}

	/**
	 * 数据校验
	 * 
	 * @param request
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 * @param page
	 *            int
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) {
		//TODO 需要检验填写的用户信息
		
	}

	protected Map referenceData(HttpServletRequest request, int page) {
		Map model = new HashMap();
		String sid = request.getParameter("sid");
		String rid = request.getParameter("rid");
		if (sid != null && rid != null) {
			//
			Long surveyId = new Long(sid);
			Long surveyRecordId = new Long(rid);
			List pages = getQuestionManager().getPages(surveyId);
			Long pid = null;

			pid = new Long(page + 1);
			QuestionPage qp = getQuestionManager().getQuestionPage(surveyId, pid);
			Survey survey = getSurveyManager().getSurveyById(surveyId);
			SurveyRecord surveyRecord = getSurveyManager()
					.getSurveyRecordById(new Long(rid));
			model.put("survey", survey);
			model.put("surveyRecord", surveyRecord);
			model.put("questionPage", qp);
			model.put("pageId", (page + 1));
			//
			model.put("pages", pages);
		}
		model.put("sid", sid);
		model.put("rid", rid);
		return model;
	}

	/**
	 * 校验页面
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 * @param page
	 *            int
	 * @param helper
	 * 
	 * @param model
	 *            Map
	 */
	protected void validatePage(Object command, Errors errors, int page,
			ControllerHelper helper, Map model) {
		String questionIds = helper.getString(QUESTION_IDS);
		if (questionIds != null) {
			String[] questionIdAry = questionIds.split(",");
			for (int i = 0; i < questionIdAry.length; i++) {
				String questionId = questionIdAry[i];
				Long qid = new Long(questionId);
				Question q = this.getQuestionManager().getQuestionById(qid);
				// q.getQuestionInputFilter();
				int type = q.getQuestionType().intValue();
				if (type == 1 || type == 2 || type == 5 || type == 6) {
					String items[] = helper.getStrings(questionId);
					if (items == null) {
						// 选项必须填写
						errors
								.reject("", q.getQuestionTitle() + ":"
										+ "必须选择选项");
					} else {
						for (int j = 0; j < items.length; j++) {
							String itemId = items[j];
							Long iid = new Long(itemId);
							QuestionItem qi = this.getQuestionManager()
									.getQuestionItemById(iid);
							int itemType = qi.getQuestionItemType().intValue();
							if (itemType == 1) {
								// 其他输入
								String iinput = helper.getString(itemId
										+ "_i_input");
								if (!StringUtils.hasText(iinput)) {
									// 其他输入必须填写
									errors.reject("", q.getQuestionTitle()
											+ "-" + qi.getQuestionItemText()
											+ ":" + "其他输入必须填写");

								}
							}
						}
					}
					if (type == 5 || type == 6) {
						String desc = helper.getString(questionId + "_q_input");
						if (!StringUtils.hasText(desc)) {
							// 题目备注必须填写
							errors.reject("", q.getQuestionTitle() + ":"
									+ "题目备注必须填写");
						}
					}
				} else if (type == 3 || type == 4) {
					String desc = helper.getString(questionId + "_q_input");
					if (!StringUtils.hasText(desc)) {
						// 题目必须填写
						errors
								.reject("", q.getQuestionTitle() + ":"
										+ "题目必须填写");
					}
				}
			}
		}

	}

	/**
	 * 处理每个页面的提交
	 * 
	 * @param request
	 * 
	 * @param command
	 * 
	 * @param errors
	 * 
	 * @param page
	 *            int
	 * @param helper
	 * 
	 * @param model
	 *            Map
	 * @throws Exception
	 */
	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page, ControllerHelper helper, Map model)
			throws Exception {
		HttpSession session = request.getSession(true);

		upateAnswer(request, session);
		//
	}

	/**
	 * 显示登录界面
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @return
	 */
	public ModelAndView loginForm(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper) {
		helper.sendRedirect(helper.getBaseURL() + "user/login.jhtml?done="
				+ helper.encodeURL(helper.getFullURI()));
		return null;
	}

	/**
	 * 判断用户是否登录
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return boolean
	 */
	public boolean isLogin(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			return this.getUserSession().isLogin();
		} catch (Exception ex) {
			return false;
		}
	}

	protected boolean isPermitPoll(HttpServletRequest request,
			HttpServletResponse response) {
		return true;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setResultViewName(String resultViewName) {
		this.resultViewName = resultViewName;
	}

	public void setPollViewName(String pollViewName) {
		this.pollViewName = pollViewName;
	}

	public void setEndViewName(String endViewName) {
		this.endViewName = endViewName;
	}
}
