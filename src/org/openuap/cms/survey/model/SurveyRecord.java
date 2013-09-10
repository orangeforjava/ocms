/**
 * 
 */
package org.openuap.cms.survey.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 调查记录
 * </p>
 * 
 * <p>
 * $Id: SurveyRecord.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 4.0
 */
public class SurveyRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2324589443908808933L;
	/** 调查记录id. */
	private Long surveyRecordId;
	/** 记录标题.*/
	private String recordTitle;
	/** 记录描述.*/
	private String recordDesc;
	/** 对应的调查id. */
	private Long surveyId;
	/** 所属结点id.*/
	private Long nodeId;
	/** 产生的用户Id. */
	private Long creationUserId;

	/** 产生的用户名. */
	private String creationUserName;
	/** 调查开始日期. */
	private Long startDate;
	/** 调查结束日期. */
	private Long endDate;
	/** 调查状态,0-停止，1-进行中. */
	private Integer status;
	/** 调查记录类型,0普通,1-会员. */
	private Integer type;
	
	private String strStartDate;
	
	private String strEndDate;
	/** 是否允许查看调查活动结果状态,0-都可以查看，1-查看统计性结果，2-查看统计与详细信息*/
	private Integer viewResultStatus;

	public String getStrStartDate() {
		SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		if(startDate==null||startDate==0){
			
			strStartDate=sdf.format(java.util.Calendar.getInstance().getTime());
		}else{
			Calendar c=Calendar.getInstance();
			c.setTimeInMillis(startDate);
			strStartDate=sdf.format(c.getTime());
		}
		return strStartDate;
	}

	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	public String getStrEndDate() {
		SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		if(endDate==null||endDate==0){
			//默认一个月期限
			Calendar nm=Calendar.getInstance();
			nm.setTimeInMillis(System.currentTimeMillis()+(24*60*60*1000*30L));
			strEndDate=sdf.format(nm.getTime());
		}else{
			Calendar c=Calendar.getInstance();
			c.setTimeInMillis(endDate);
			strEndDate=sdf.format(c.getTime());
		}
		return strEndDate;
	}

	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public Long getCreationUserId() {
		return creationUserId;
	}

	public void setCreationUserId(Long creationUserId) {
		this.creationUserId = creationUserId;
	}

	public String getCreationUserName() {
		return creationUserName;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getSurveyRecordId() {
		return surveyRecordId;
	}

	public void setSurveyRecordId(Long surveyRecordId) {
		this.surveyRecordId = surveyRecordId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((surveyRecordId == null) ? 0 : surveyRecordId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SurveyRecord other = (SurveyRecord) obj;
		if (surveyRecordId == null) {
			if (other.surveyRecordId != null)
				return false;
		} else if (!surveyRecordId.equals(other.surveyRecordId))
			return false;
		return true;
	}

	public String getRecordTitle() {
		return recordTitle;
	}

	public void setRecordTitle(String recordTitle) {
		this.recordTitle = recordTitle;
	}

	public String getRecordDesc() {
		return recordDesc;
	}

	public void setRecordDesc(String recordDesc) {
		this.recordDesc = recordDesc;
	}
	public Long toLongDate(String strDate){
		SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		if(strDate!=null){
			try {
				Date d=sdf.parse(strDate);
				return d.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis();
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getViewResultStatus() {
		return viewResultStatus;
	}

	public void setViewResultStatus(Integer viewResultStatus) {
		this.viewResultStatus = viewResultStatus;
	}
}
