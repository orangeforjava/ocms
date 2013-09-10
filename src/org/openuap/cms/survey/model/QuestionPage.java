/**
 * $Id: QuestionPage.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.model;

import java.util.List;

/**
 * <p>
 * Title:QuestionPage
 * </p>
 * 
 * <p>
 * Description:问题分页对象.
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
public class QuestionPage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6448769502363950502L;

	private Long pageId;

	private List groups;

	private Long previousPageId;

	private Long nextPageId;

	private String questionIds;

	public QuestionPage() {
	}

	public void setGroups(List groups) {
		this.groups = groups;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public void setPreviousPageId(Long previousPageId) {
		this.previousPageId = previousPageId;
	}

	public void setNextPageId(Long nextPageId) {
		this.nextPageId = nextPageId;
	}

	public void setQuestionIds(String questionIds) {
		this.questionIds = questionIds;
	}

	public Long getPageId() {
		return pageId;
	}

	public List getGroups() {
		return groups;
	}

	public Long getNextPageId() {
		return nextPageId;
	}

	public Long getPreviousPageId() {
		return previousPageId;
	}

	public String getQuestionIds() {
		return questionIds;
	}
}
