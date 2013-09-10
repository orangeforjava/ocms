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
 * 
 * $Id: ICommentConfig.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * @author Joseph
 * 
 */
public interface ICommentConfig {
	
	public void reloadConfig();
	/**
	 * 是否开启安全校验码
	 * 
	 * @return
	 */
	public boolean isSecurityCodeEnabled();

	/**
	 * 是否开启评论功能
	 * 
	 * @return
	 */
	public boolean isCommentEnabled();

	/**
	 * 是否允许匿名评论
	 * 
	 * @return
	 */
	public boolean isAnonymousEnabled();

	/**
	 * 是否过滤敏感词
	 * 
	 * @return
	 */
	public boolean isBadwordsEnbaled();

	/**
	 * 获得缺省的论坛板块Id
	 * 
	 * @return
	 */
	public long getDefaultBoardId();

	/**
	 * 是否允许使用html标记
	 * 
	 * @return
	 */
	public boolean isHtmlEnabled();

	/**
	 * 是否允许使用markup标记
	 * 
	 * @return
	 */
	public boolean isMarkupEnabled();

	/**
	 * 是否允许笑脸标记
	 * 
	 * @return
	 */
	public boolean isSmileyEnabled();

	/**
	 * 是否允许使用图片标记
	 * 
	 * @return
	 */
	public boolean isImgTagEnabled();

}
