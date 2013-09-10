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
 * Title: PagerType
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * $Id: PagerType.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PagerType {
	private String name;
	private String title;
	public static final PagerType DEFAULT_PAGER = new PagerType("default", "缺省");
	public static final PagerType[] DEFAULT_PAGERS = new PagerType[] { DEFAULT_PAGER };

	public PagerType() {
	}

	public PagerType(String name, String title) {
		this.name = name;
		this.title = title;

	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
