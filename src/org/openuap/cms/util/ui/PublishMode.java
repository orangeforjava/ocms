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
 * 发布模式.
 * </p>
 * 
 * <p>
 * Description: the publish mode.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * 
 * </p>
 * $Id: PublishMode.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * @author Weiping Ju
 * @version 1.0
 */
public class PublishMode {
	private Integer mode;
	private String title;
	public static final PublishMode STATIC_MODE = new PublishMode(new Integer(
			"1"), "静态发布");
	public static final PublishMode DYNAMIC_MODE = new PublishMode(new Integer(
			"2"), "动态发布");
	public static final PublishMode NO_MODE = new PublishMode(new Integer("0"),
			"不发布");
	public static final PublishMode[] DEFAULT_MODES = new PublishMode[] {
			STATIC_MODE, DYNAMIC_MODE, NO_MODE };

	public PublishMode() {
	}

	public PublishMode(Integer mode, String title) {
		this.mode = mode;
		this.title = title;
	}

	public Integer getMode() {
		return mode;
	}

	public String getTitle() {
		return title;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
