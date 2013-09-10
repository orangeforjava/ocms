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
package org.openuap.cms.survey.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 问题结果统计辅助Bean.
 * </p>
 * 
 * <p>
 * $Id: QuestionBean.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class QuestionBean {
	
	private List questions;

	private int voteCount;

	private int paperBallotCount;

	private int normalBallotCount;

	private int imageBallotCount;

	public QuestionBean() {
		questions = new ArrayList();

		voteCount = 0;
		paperBallotCount = 0;
		normalBallotCount = 0;
		imageBallotCount = 0;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public int getPaperBallotCount() {
		return paperBallotCount;
	}

	public void setPaperBallotCount(int paperBallotCount) {
		this.paperBallotCount = paperBallotCount;
	}

	public int getNormalBallotCount() {
		return normalBallotCount;
	}

	public void setNormalBallotCount(int normalBallotCount) {
		this.normalBallotCount = normalBallotCount;
	}

	public int getImageBallotCount() {
		return imageBallotCount;
	}

	public List getQuestions() {
		return questions;
	}

	public void setImageBallotCount(int imageBallotCount) {
		this.imageBallotCount = imageBallotCount;
	}

	public void setQuestions(List questions) {
		this.questions = questions;
	}

}
