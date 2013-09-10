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
 * 编辑器类型.
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class EditorType {
	private String url;
	private String title;

	public static final EditorType DEFAULT_EDITOR_TYPE = new EditorType(
			"default_editor.html", "default");
	public static final EditorType[] DEFAULT_EDITOR_TYPES = new EditorType[] { DEFAULT_EDITOR_TYPE };

	public EditorType() {
	}

	public EditorType(String url, String title) {
		this.url = url;
		this.title = title;

	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
