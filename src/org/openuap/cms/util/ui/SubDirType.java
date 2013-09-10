/*
 * Copyright 2005-2008 the original author or authors.
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
package org.openuap.cms.util.ui;

/**
 * <p>
 * 子目录类型.
 * </p>
 * 
 * <p>
 * $Id: SubDirType.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SubDirType {
	private String type;
	private String title;
	public static final SubDirType[] DEFAULT_SUBDIR_TYPES = new SubDirType[] {
			new SubDirType("", "-"), new SubDirType("none", "none"),
			new SubDirType("Y-m-d", "Y-m-d"), new SubDirType("Y/m/d", "Y/m/d"),
			new SubDirType("Ymd", "Ymd"), new SubDirType("Y-m", "Y-m"),
			new SubDirType("Y", "Y"), new SubDirType("auto", "auto") };

	public SubDirType() {
	}

	public SubDirType(String type, String title) {
		this.type = type;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}
}
