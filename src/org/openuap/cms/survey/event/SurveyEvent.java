/**
 * $Id: SurveyEvent.java 3951 2010-11-02 10:13:17Z orangeforjava $
 */
package org.openuap.cms.survey.event;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.openuap.cms.survey.model.Survey;
import org.openuap.event.Event;

/**
 * 调查事件定义
 * @author Joseph
 *
 */
public class SurveyEvent extends Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -827510236989975303L;
	
	public static final int SUVEY_CREATED = 90000;

	public static final int SUVEY_UPDATED = 90001;
	
	public static final int SUVEY_DELETED = 90002;

	private int eventType;

	private Map params;

	private Survey survey;

	private Date created;

	private Exception failureException;
	
	/**
	 * 
	 * @param source
	 */
	public SurveyEvent(Object source) {
		super(source);
	}
	/**
	 * 
	 * @param eventType
	 * @param publish
	 * @param params
	 * @param source
	 */
	public SurveyEvent(int eventType, Survey survey, Map params, Object source) {
		super(source);
		this.eventType = -1;
		this.eventType = eventType;
		this.survey = survey;
		this.params = params != null ? Collections.unmodifiableMap(params) : null;
		this.created = new Date();
	}
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	public Map getParams() {
		return params;
	}
	public void setParams(Map params) {
		this.params = params;
	}
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Exception getFailureException() {
		return failureException;
	}
	public void setFailureException(Exception failureException) {
		this.failureException = failureException;
	}
}
