/**
 * $Id: QuestionResultBean.java 4017 2011-03-13 13:55:50Z orangeforjava $
 */
package org.openuap.cms.survey.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openuap.cms.survey.model.Question;

/**
 * <p>
 * Title: QuestionResultBean
 * </p>
 * 
 * <p>
 * Description:
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
public class QuestionResultBean {

	private int questionItemsCount;

	private Question question;

	private Collection questionItemResultList;
	
	private List questionAnswers=new ArrayList();

	public QuestionResultBean() {
		questionItemsCount = 0;
		questionItemResultList = new ArrayList();

	}

	public Question getQuestion() {
		return question;
	}

	public int getQuestionItemsCount() {
		return questionItemsCount;
	}

	public Collection getQuestionItemResultList() {
		return questionItemResultList;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setQuestionItemsCount(int questionItemsCount) {
		this.questionItemsCount = questionItemsCount;
	}

	public void setQuestionItemResultList(Collection questionItemResultList) {
		this.questionItemResultList = questionItemResultList;
	}

	public List getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(List questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

}
