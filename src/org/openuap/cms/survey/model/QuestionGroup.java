/**
 * $Id: QuestionGroup.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Title: QuestionGroup
 * </p>
 * 
 * <p>
 * Description: 问题组对象.
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
public class QuestionGroup implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2935081199310382634L;

	private Long groupId;

	private List questions;

	private String groupTitle;

	/** 组类型,0-等于题目，1-包含多个题目. */
	private Integer groupType;

	private String questionIds;

	/** 用来处理组横向的选项名. */
	private Set items;

	public QuestionGroup() {
	}

	public Long getGroupId() {
		return groupId;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public List getQuestions() {
		return questions;
	}

	public String getQuestionIds() {
		return questionIds;
	}

	public Set getItems() {
		if (questions != null) {
			Question q = (Question) questions.get(0);
			return q.getQuestionItems();
		}
		return items;
	}

	public void setQuestions(List questions) {
		this.questions = questions;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setQuestionIds(String questionIds) {
		this.questionIds = questionIds;
	}

	public void setItems(Set items) {
		this.items = items;
	}

	public Object clone() {
		QuestionGroup qg = new QuestionGroup();
		qg.setGroupId(groupId);
		qg.setGroupTitle(groupTitle);
		qg.setGroupType(groupType);
		List questions2 = new ArrayList();
		Collections.copy(questions, questions2);
		qg.setQuestions(questions2);
		return qg;
	}
}
