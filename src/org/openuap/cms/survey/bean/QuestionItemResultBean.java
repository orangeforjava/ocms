/**
 * $Id: QuestionItemResultBean.java 4017 2011-03-13 13:55:50Z orangeforjava $
 */
package org.openuap.cms.survey.bean;

/**
 * <p>
 * Title: QuestionItemResultBean
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
public class QuestionItemResultBean {

	private String questionItemText;

	private float questionPercent;

	private int polledTimes;

	public QuestionItemResultBean() {
		questionItemText = "";
	}

	public int getPolledTimes() {
		return polledTimes;
	}

	public String getQuestionItemText() {
		return questionItemText;
	}

	public float getQuestionPercent() {
		return questionPercent;
	}

	public void setPolledTimes(int polledTimes) {
		this.polledTimes = polledTimes;
	}

	public void setQuestionItemText(String questionItemText) {
		this.questionItemText = questionItemText;
	}

	public void setQuestionPercent(float questionPercent) {
		this.questionPercent = questionPercent;
	}

}
