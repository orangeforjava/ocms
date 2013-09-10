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

/**
 * <p>
 * 投票者对象
 * </p>
 * 
 * <p>
 * $Id: Voter.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 4.0
 */
public class Voter extends BaseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5673263510390582161L;

	private int hashValue = 0;

	/** */
	private Long voterId;

	private Long surveyRecordId;

	private Long voterSurveyId;

	private String voterName;

	private String voterCompany;

	private String voterDepartment;

	private String voterContact;

	private String voterEmail;

	private String voterTel;

	private String voterMobile;

	private String voterFax;

	private String voterIpaddress;

	private Long voterPollDate;

	private Long voterUserId;
	/** 性别. */
	private String voterSex;
	// 扩展属性
	private String voterProp1;
	private String voterProp2;
	private String voterProp3;
	private String voterProp4;
	private String voterProp5;
	private String voterProp6;
	private String voterProp7;
	private String voterProp8;
	private String voterProp9;
	private String voterProp10;

	public Voter(Long voterSurveyId, String voterName, String voterContact,
			String voterEmail, String voterIpaddress, Long voterPollDate) {
		this.voterSurveyId = voterSurveyId;
		this.voterName = voterName;
		this.voterIpaddress = voterIpaddress;
		this.voterPollDate = voterPollDate;
		this.voterContact = voterContact;
		this.voterEmail = voterEmail;
	}

	public Voter() {
	}

	public Voter(Long voterSurveyId, String voterIpaddress, Long voterPollDate) {
		this.voterSurveyId = voterSurveyId;
		this.voterIpaddress = voterIpaddress;
		this.voterPollDate = voterPollDate;
	}

	public Long getVoterId() {
		return voterId;
	}

	public void setVoterId(Long voterId) {
		this.voterId = voterId;
	}

	public String getVoterName() {
		return voterName;
	}

	public void setVoterName(String voterName) {
		this.voterName = voterName;
	}

	public String getVoterIpaddress() {
		return voterIpaddress;
	}

	public void setVoterIpaddress(String voterIpaddress) {
		this.voterIpaddress = voterIpaddress;
	}

	public Long getVoterPollDate() {
		return voterPollDate;
	}

	public String getVoterEmail() {
		return voterEmail;
	}

	public String getVoterContact() {
		return voterContact;
	}

	public Long getVoterUserId() {
		return voterUserId;
	}

	public Long getVoterSurveyId() {
		return voterSurveyId;
	}

	public String getVoterDepartment() {
		return voterDepartment;
	}

	public String getVoterCompany() {
		return voterCompany;
	}

	public String getVoterFax() {
		return voterFax;
	}

	public String getVoterTel() {
		return voterTel;
	}

	public String getVoterMobile() {
		return voterMobile;
	}

	public void setVoterPollDate(Long voterPollDate) {
		this.voterPollDate = voterPollDate;
	}

	public void setVoterEmail(String voterEmail) {
		this.voterEmail = voterEmail;
	}

	public void setVoterContact(String voterContact) {
		this.voterContact = voterContact;
	}

	public void setVoterUserId(Long voterUserId) {
		this.voterUserId = voterUserId;
	}

	public void setVoterSurveyId(Long voterSurveyId) {
		this.voterSurveyId = voterSurveyId;
	}

	public void setVoterDepartment(String voterDepartment) {
		this.voterDepartment = voterDepartment;
	}

	public void setVoterCompany(String voterCompany) {
		this.voterCompany = voterCompany;
	}

	public void setVoterFax(String voterFax) {
		this.voterFax = voterFax;
	}

	public void setVoterTel(String voterTel) {
		this.voterTel = voterTel;
	}

	public void setVoterMobile(String voterMobile) {
		this.voterMobile = voterMobile;
	}

	public String toString() {
		return (new ToStringBuilder(this)).append("voterId", getVoterId())
				.toString();
	}

	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof Voter)) {
			return false;
		}
		Voter that = (Voter) rhs;
		if (this.getVoterId() == null || that.getVoterId() == null) {
			return false;
		}
		return (this.getVoterId().equals(that.getVoterId()));

	}

	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int indexidValue = this.getVoterId() == null ? 0 : this
					.getVoterId().hashCode();
			result = result * 37 + indexidValue;
			this.hashValue = result;
		}
		return this.hashValue;

	}

	public Long getSurveyRecordId() {
		return surveyRecordId;
	}

	public void setSurveyRecordId(Long surveyRecordId) {
		this.surveyRecordId = surveyRecordId;
	}

	public String getVoterSex() {
		return voterSex;
	}

	public void setVoterSex(String voterSex) {
		this.voterSex = voterSex;
	}

	public String getVoterProp1() {
		return voterProp1;
	}

	public void setVoterProp1(String voterProp1) {
		this.voterProp1 = voterProp1;
	}

	public String getVoterProp2() {
		return voterProp2;
	}

	public void setVoterProp2(String voterProp2) {
		this.voterProp2 = voterProp2;
	}

	public String getVoterProp3() {
		return voterProp3;
	}

	public void setVoterProp3(String voterProp3) {
		this.voterProp3 = voterProp3;
	}

	public String getVoterProp4() {
		return voterProp4;
	}

	public void setVoterProp4(String voterProp4) {
		this.voterProp4 = voterProp4;
	}

	public String getVoterProp5() {
		return voterProp5;
	}

	public void setVoterProp5(String voterProp5) {
		this.voterProp5 = voterProp5;
	}

	public String getVoterProp6() {
		return voterProp6;
	}

	public void setVoterProp6(String voterProp6) {
		this.voterProp6 = voterProp6;
	}

	public String getVoterProp7() {
		return voterProp7;
	}

	public void setVoterProp7(String voterProp7) {
		this.voterProp7 = voterProp7;
	}

	public String getVoterProp8() {
		return voterProp8;
	}

	public void setVoterProp8(String voterProp8) {
		this.voterProp8 = voterProp8;
	}

	public String getVoterProp9() {
		return voterProp9;
	}

	public void setVoterProp9(String voterProp9) {
		this.voterProp9 = voterProp9;
	}

	public String getVoterProp10() {
		return voterProp10;
	}

	public void setVoterProp10(String voterProp10) {
		this.voterProp10 = voterProp10;
	}

}
