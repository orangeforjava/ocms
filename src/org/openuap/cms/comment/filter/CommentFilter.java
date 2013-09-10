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
package org.openuap.cms.comment.filter;

import org.openuap.base.filter.Filter;
import org.openuap.cms.badwords.filter.BadwordsFilter;
import org.openuap.cms.comment.ICommentPost;
import org.openuap.cms.comment.config.CommentConfigFactory;
import org.openuap.cms.comment.config.ICommentConfig;

/**
 * <p>
 * 评论内容过滤器
 * </p>
 * 
 * 
 * <p>
 * $Id: CommentFilter.java 3961 2010-11-11 03:06:16Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CommentFilter extends Filter {
	/**
	 * 存储过滤
	 * 
	 * @param post
	 */
	public static void doStoreFilter(ICommentPost post) {
		ICommentConfig commentConfig = CommentConfigFactory.getInstance()
				.getXmlCommentConfig();
		String content = post.getContent();
		String title = post.getTitle();
		if (title != null) {
			//
			title = Filter.perl.substitute("s/[\n\r]//g", title);
			title = Filter.perl.substitute("s/>/&gt;/g", title);
			title = Filter.perl.substitute("s/</&lt;/g", title);
		}
		//
		if (content != null) {
			if (commentConfig.isHtmlEnabled()) {
				content = Filter.perl.substitute(
						"s/<.?SCRIPT>|<.?EMBED>|<.?OBJECT>|<.?APPLET>//ig",
						content);
			} else {
				content = Filter.perl.substitute("s/>/&gt;/g", content);
				content = Filter.perl.substitute("s/</&lt;/g", content);
			}
		}
		//
		content = Filter.perl.substitute("s/\t/&nbsp;&nbsp;/g", content);
		content = Filter.substituteLeadingSpace(content);
		content = Filter.perl.substitute("s/\r//g", content);
		content = Filter.perl.substitute("s/\n{3,}/\n\n/g", content);
		content = Filter.perl.substitute("s/^\n+//g", content);
		content = Filter.perl.substitute("s/\n+$//g", content);
		//敏感词过滤
		StringBuffer rs = new StringBuffer();
		BadwordsFilter.doFilterByCache(rs, content, "-1");
		//
		content=rs.toString();
		//
		post.setTitle(title);
		post.setContent(content);
	}
}
