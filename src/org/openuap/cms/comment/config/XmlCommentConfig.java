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
package org.openuap.cms.comment.config;

/**
 * <p>
 * 评论配置XML实现.
 * </p>
 * 
 * 
 * <p>
 * $Id: XmlCommentConfig.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class XmlCommentConfig extends AbstractXmlConfiguration implements
		ICommentConfig {
	/** 是否启用安全码.*/
	private boolean securityCodeEnabled;
	private boolean commentEnabled;
	private boolean anonymousEnabled;
	private boolean badwordsEnbaled;
	private long defaultBoardId;
	//
	private boolean htmlEnabled;
	private boolean markupEnabled;
	private boolean smileyEnabled;
	private boolean imgTagEnabled;
	
	public void reloadConfig() {
		if (configuration != null) {
			super.reload();
		}
		//
		securityCodeEnabled = this.getBoolean("comment.securitycode.enabled",
				false);
		commentEnabled = this.getBoolean("comment.enabled", false);
		anonymousEnabled = this.getBoolean("comment.anonymous.enabled", false);
		badwordsEnbaled = this.getBoolean("comment.badwords.enabled", false);
		defaultBoardId = this.getLong("comment.default.boardid", 0L);
		htmlEnabled = this.getBoolean("comment.html.enabled", false);
		markupEnabled = this.getBoolean("comment.markup.enabled", false);
		smileyEnabled = this.getBoolean("comment.smiley.enabled", false);
		imgTagEnabled = this.getBoolean("comment.imgtag.enabled", false);
	}

	public boolean isSecurityCodeEnabled() {
		return securityCodeEnabled;
	}

	public void setSecurityCodeEnabled(boolean securityCodeEnabled) {
		this.securityCodeEnabled = securityCodeEnabled;
	}

	public boolean isCommentEnabled() {
		return commentEnabled;
	}

	public void setCommentEnabled(boolean commentEnabled) {
		this.commentEnabled = commentEnabled;
	}

	public boolean isAnonymousEnabled() {
		return anonymousEnabled;
	}

	public void setAnonymousEnabled(boolean anonymousEnabled) {
		this.anonymousEnabled = anonymousEnabled;
	}

	public boolean isBadwordsEnbaled() {
		return badwordsEnbaled;
	}

	public void setBadwordsEnbaled(boolean badwordsEnbaled) {
		this.badwordsEnbaled = badwordsEnbaled;
	}

	public long getDefaultBoardId() {
		return defaultBoardId;
	}

	public void setDefaultBoardId(long defaultBoardId) {
		this.defaultBoardId = defaultBoardId;
	}

	public boolean isHtmlEnabled() {
		return htmlEnabled;
	}

	public void setHtmlEnabled(boolean htmlEnabled) {
		this.htmlEnabled = htmlEnabled;
	}

	public boolean isMarkupEnabled() {
		return markupEnabled;
	}

	public void setMarkupEnabled(boolean markupEnabled) {
		this.markupEnabled = markupEnabled;
	}

	public boolean isSmileyEnabled() {
		return smileyEnabled;
	}

	public void setSmileyEnabled(boolean smileyEnabled) {
		this.smileyEnabled = smileyEnabled;
	}

	public boolean isImgTagEnabled() {
		return imgTagEnabled;
	}

	public void setImgTagEnabled(boolean imgTagEnabled) {
		this.imgTagEnabled = imgTagEnabled;
	}
}
