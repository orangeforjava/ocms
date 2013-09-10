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
package org.openuap.cms.survey.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openuap.base.dao.hibernate.BaseObject;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;

/**
 * 
 * <p>
 * 问题选项实体.
 * </p>
 * 
 * <p>
 * $Id: QuestionItem.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class QuestionItem extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6087727838563287783L;

	private int hashValue = 0;
	/** 问题选项唯一ID. */
	private Long questionItemId;
	/** 选项文本. */
	private String questionItemText;

	/** 选项被投票的次数. */
	private Integer questionItemPolledTimes;

	/** 选项图片. */
	private String image;
	/** 关联的内容URL. */
	private String url;
	/** 关联的内容ID,可选. */
	private String oid;
	/** 选项排序. */
	private Integer questionItemSort;

	/** 0-普通，1-输入 */
	private Integer questionItemType;
	/** 是否是正确答案. */
	private boolean rightAnswer;
	/** 对应的问题对象. */

	private transient Question question;

	public QuestionItem(String questionItemText,
			Integer questionItemPolledTimes, Question question) {
		this.questionItemText = questionItemText;
		this.questionItemPolledTimes = questionItemPolledTimes;
		this.question = question;
	}

	public QuestionItem() {
		questionItemPolledTimes = new Integer(0);
	}

	public QuestionItem(String questionItemText, Question question) {
		this.questionItemText = questionItemText;
		this.question = question;
	}

	public String toString() {
		return (new ToStringBuilder(this)).append("questionItemId",
				getQuestionItemId()).append("questionItemText",
				getQuestionItemText()).toString();
	}

	public int compareTo(Object object) {
		QuestionItem otherQuestionItem = (QuestionItem) object;
		return questionItemText.compareTo(otherQuestionItem.questionItemText);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof QuestionItem)) {
			return false;
		}
		QuestionItem that = (QuestionItem) rhs;
		if (this.getQuestionItemId() == null
				|| that.getQuestionItemId() == null) {
			return false;
		}
		return (this.getQuestionItemId().equals(that.getQuestionItemId()));

	}

	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int indexidValue = this.getQuestionItemId() == null ? 0 : this
					.getQuestionItemId().hashCode();
			result = result * 37 + indexidValue;
			this.hashValue = result;
		}
		return this.hashValue;

	}

	public Question getQuestion() {
		return question;
	}

	public Long getQuestionItemId() {
		return questionItemId;
	}

	public Integer getQuestionItemPolledTimes() {
		return questionItemPolledTimes;
	}

	public Integer getQuestionItemSort() {
		return questionItemSort;
	}

	public String getQuestionItemText() {
		return questionItemText;
	}

	public Integer getQuestionItemType() {
		return questionItemType;
	}

	public boolean isRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(boolean rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public void setQuestionItemType(Integer questionItemType) {
		this.questionItemType = questionItemType;
	}

	public void setQuestionItemText(String questionItemText) {
		this.questionItemText = questionItemText;
	}

	public void setQuestionItemSort(Integer questionItemSort) {
		this.questionItemSort = questionItemSort;
	}

	public void setQuestionItemPolledTimes(Integer questionItemPolledTimes) {
		this.questionItemPolledTimes = questionItemPolledTimes;
	}

	public void setQuestionItemId(Long questionItemId) {
		this.questionItemId = questionItemId;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	public String getBaseImage(){
		if(StringUtil.hasText(image)){
			//
			if(image.startsWith("../")){
				return CMSConfig.getInstance().getBaseUrl()+image.substring(3);
			}else{
				return image;
			}
		}
		return "";
	}
}
