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
package org.openuap.cms.keywords.filter;

import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.filter.Filter;
import org.openuap.cms.keywords.cache.KeywordsCache;
import org.openuap.cms.keywords.manager.KeywordsManager;
import org.openuap.cms.keywords.model.Keywords;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 关键词过滤器.
 * </p>
 * 
 * <p>
 * $Id: KeywordsFilter.java 4037 2011-04-17 12:37:34Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class KeywordsFilter extends Filter {
	private static String spliter1 = "\\s";
	private static String spliter = ";";

	/**
	 * 缺省构造函数
	 */
	public KeywordsFilter() {
	}

	/**
	 * 过滤指定的内容
	 * 
	 * @param result
	 *            过滤后的结果
	 * @param input
	 *            输入内容
	 * @param scope
	 *            过滤范围
	 * @return 匹配的结果数
	 */
	public static int doFilter(StringBuffer result, String input, String scope) {
		int rs = 0;
		KeywordsManager keywordsManager = getKeywordsManager();
		if (keywordsManager == null) {
			return -1;
		}

		// 获得指定范围的关键词列表
		List<Keywords> keywordList = keywordsManager.getKeywordsByScope(scope);
		if (keywordList == null) {
			return 0;
		}
		String oinput = null;
		StringBuffer tmpRs = new StringBuffer(input);
		for (int i = 0; i < keywordList.size(); i++) {
			Keywords keywords = (Keywords) keywordList.get(i);
			oinput = tmpRs.toString();
			tmpRs = new StringBuffer();
			rs += doFilter(tmpRs, oinput, keywords);
		}
		result.append(tmpRs);
		return rs;
	}

	/**
	 * 
	 * @param result
	 *            经过过滤后的结果
	 * @param input
	 *            输入内容
	 * @param scope
	 *            过滤范围
	 * @param excludeUrl
	 *            需要排除的URL
	 * @return
	 */
	public static int doFilterByCache(StringBuffer result, String input,
			String scope, String excludeUrl) {
		int rs = 0;
		List<Keywords> keywordList = KeywordsCache.getAllKeywords(scope);
		if (keywordList == null) {
			return 0;
		}
		String oinput = null;
		StringBuffer tmpRs = new StringBuffer(input);
		for (int i = 0; i < keywordList.size(); i++) {

			Keywords keywords = (Keywords) keywordList.get(i);
			// 排除指定的excludeUrl
			if (excludeUrl.equalsIgnoreCase(keywords.getUrl())) {
				continue;
			}
			oinput = tmpRs.toString();
			tmpRs = new StringBuffer();
			rs += doFilter(tmpRs, oinput, keywords);
		}
		result.append(tmpRs);
		return rs;
	}

	public static int doFilter(StringBuffer result, String input,
			Keywords keywords) {

		String find = keywords.getKeyword();
		String[] words = find.split(spliter1);
		StringBuffer regexp = new StringBuffer();
		for (int x = 0; x < words.length; x++) {
			// Excape "|" and "/" to keep us out of trouble in our regexp.
			String[] w2 = words[x].split(spliter);
			for (int y = 0; y < w2.length; y++) {
				// Excape "|" and "/" to keep us out of trouble in our regexp.
				if (StringUtils.hasText(w2[y])) {
					w2[y] = perl.substitute("s#([\\|\\/\\.])#\\\\$1#g", w2[y]
							.trim());
					if (regexp.length() > 0) {
						regexp.append("|");
					}
					regexp.append(w2[y]);
				}
			}
		}
		//
		try {
			Pattern keywordsPattern = new Perl5Compiler().compile(regexp
					.toString(), Perl5Compiler.CASE_INSENSITIVE_MASK
					| Perl5Compiler.READ_ONLY_MASK);
			//
			Perl5Substitution KEY_WORDS_SUB;
			if (StringUtil.hasText(keywords.getStyle())) {
				KEY_WORDS_SUB = new Perl5Substitution("<a href=\""
						+ keywords.getUrl() + "\" style=\""
						+ keywords.getStyle() + "\" target=\"_blank\">$0</a>");
			} else {
				KEY_WORDS_SUB = new Perl5Substitution("<a href=\""
						+ keywords.getUrl() + "\" target=\"_blank\">$0</a>");
			}

			Perl5Matcher matcher = new Perl5Matcher();
			return Util.substitute(result, matcher, keywordsPattern,
					KEY_WORDS_SUB, input, Util.SUBSTITUTE_ALL);
		} catch (MalformedPatternException ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * 判断是否具有词
	 * 
	 * @param input
	 * @param scope
	 * @return
	 */
	public static boolean isMatcher(String input, String scope) {
		KeywordsManager keywordsManager = getKeywordsManager();
		if (keywordsManager == null) {
			return false;
		}
		List<Keywords> keywordList = keywordsManager.getKeywordsByScope(scope);

		StringBuffer regexp = new StringBuffer();
		for (int i = 0; i < keywordList.size(); i++) {
			Keywords keywords = (Keywords) keywordList.get(i);
			String find = keywords.getKeyword();
			String[] words = find.split(spliter1);

			for (int x = 0; x < words.length; x++) {
				// Excape "|" and "/" to keep us out of trouble in our regexp.

				String[] w2 = words[x].split(spliter);
				for (int y = 0; y < w2.length; y++) {
					// Excape "|" and "/" to keep us out of trouble in our
					// regexp.
					if (StringUtils.hasText(w2[y])) {
						w2[y] = perl.substitute("s#([\\|\\/\\.])#\\\\$1#g",
								w2[y].trim());
						if (regexp.length() > 0) {
							regexp.append("|");
						}
						regexp.append(w2[y]);
					}
				}
			}
		}
		//
		try {
			Pattern badWordsPattern = new Perl5Compiler().compile(regexp
					.toString(), Perl5Compiler.CASE_INSENSITIVE_MASK
					| Perl5Compiler.READ_ONLY_MASK);
			//
			Perl5Matcher matcher = new Perl5Matcher();
			return matcher.contains(input, badWordsPattern);
		} catch (MalformedPatternException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 执行全范围过滤
	 * 
	 * @param result
	 * @param input
	 * @param excludeUrl
	 * @return
	 */
	public static int doAllFilter(StringBuffer result, String input,
			String excludeUrl) {
		int i = 0;
		String scope = "-1";
		i += doFilterByCache(result, input, scope, excludeUrl);
		return i;
	}

	/**
	 * 获得敏感词管理器对象
	 * 
	 * @return
	 */
	public static KeywordsManager getKeywordsManager() {
		return (KeywordsManager) ObjectLocator.lookup("keywordsManager",
				CmsPlugin.PLUGIN_ID);
	}
}
