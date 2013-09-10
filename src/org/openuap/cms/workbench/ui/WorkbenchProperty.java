/*
 * Copyright 2002-2009 the original author or authors.
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
package org.openuap.cms.workbench.ui;

/**
 * <p>
 * 工作台属性类.
 * </p>
 * 
 * <p>
 * $Id: WorkbenchProperty.java 3942 2010-10-28 04:15:34Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class WorkbenchProperty implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8016406956055197461L;
	
	private String title;

	private String icon;

	private String description;

	private String appTitle;

	private String appUrl;

	private String adminTitle;

	private String adminUrl;
	
	private String logoutUrl;

	private String statusUrl;

	public String getStatusUrl() {
		return statusUrl;
	}

	public void setStatusUrl(String statusUrl) {
		this.statusUrl = statusUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAdminTitle() {
		return adminTitle;
	}

	public void setAdminTitle(String adminTitle) {
		this.adminTitle = adminTitle;
	}

	public String getAdminUrl() {
		return adminUrl;
	}

	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
}
