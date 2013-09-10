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
package org.openuap.cms.badwords.filter;

import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.badwords.cache.BadwordsCache;
import org.openuap.cms.badwords.manager.BadwordsManager;
import org.openuap.cms.badwords.model.Badwords;
import org.openuap.cms.core.filter.Filter;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 敏感词过滤器.
 * </p>
 * 
 * <p>
 * $Id: BadwordsFilter.java 4027 2011-03-22 15:00:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class BadwordsFilter extends Filter {
	private static String spliter1 = "\\s";
	private static String spliter = ";";

	/**
	 * 缺省构造函数
	 */
	public BadwordsFilter() {
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
		BadwordsManager badwordsManager = getBadWordsManager();
		if (badwordsManager == null) {
			return -1;
		}

		// 获得指定范围的敏感词列表
		List<Badwords> badwordList = badwordsManager.getBadwordsByScope(scope);
		if (badwordList == null) {
			return 0;
		}
		String oinput = null;
		StringBuffer tmpRs = new StringBuffer(input);
		for (int i = 0; i < badwordList.size(); i++) {
			Badwords badwords = (Badwords) badwordList.get(i);
			oinput = tmpRs.toString();
			tmpRs = new StringBuffer();
			rs += doFilter(tmpRs, oinput, badwords);
		}
		result.append(tmpRs);
		return rs;
	}
	/**
	 * 
	 * @param result
	 * @param input
	 * @param scope
	 * @return
	 */
	public static int doFilterByCache(StringBuffer result, String input, String scope) {
		int rs = 0;
		List<Badwords> badwordList = BadwordsCache.getAllBadwords(scope);
		if (badwordList == null) {
			return 0;
		}
		String oinput = null;
		StringBuffer tmpRs = new StringBuffer(input);
		for (int i = 0; i < badwordList.size(); i++) {
			Badwords badwords = (Badwords) badwordList.get(i);
			oinput = tmpRs.toString();
			tmpRs = new StringBuffer();
			rs += doFilter(tmpRs, oinput, badwords);
		}
		result.append(tmpRs);
		return rs;
	}

	public static int doHighlightFilter(StringBuffer result, String input, String scope) {
		int rs = 0;
		BadwordsManager badwordsManager = getBadWordsManager();
		if (badwordsManager == null) {
			return -1;
		}

		List<Badwords> badwordList = badwordsManager.getBadwordsByScope(scope);
		if (badwordList == null) {
			return 0;
		}
		String oinput = null;
		StringBuffer tmpRs = new StringBuffer(input);
		for (int i = 0; i < badwordList.size(); i++) {
			Badwords badwords = (Badwords) badwordList.get(i);
			oinput = tmpRs.toString();
			tmpRs = new StringBuffer();
			rs += doFilter(tmpRs, oinput, badwords);
		}
		result.append(tmpRs);
		return rs;
	}

	public static int doFilter(StringBuffer result, String input, Badwords badwords) {

		String find = badwords.getFind();
		String[] words = find.split(spliter1);
		StringBuffer regexp = new StringBuffer();
		for (int x = 0; x < words.length; x++) {
			// Excape "|" and "/" to keep us out of trouble in our regexp.
			String[] w2 = words[x].split(spliter);
			for (int y = 0; y < w2.length; y++) {
				// Excape "|" and "/" to keep us out of trouble in our regexp.
				if (StringUtils.hasText(w2[y])) {
					w2[y] = perl.substitute("s#([\\|\\/\\.])#\\\\$1#g", w2[y].trim());
					if (regexp.length() > 0) {
						regexp.append("|");
					}
					regexp.append(w2[y]);
				}
			}
		}
		//
		try {
			Pattern badWordsPattern = new Perl5Compiler().compile(regexp.toString(),
					Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.READ_ONLY_MASK);
			//
			Perl5Substitution BAD_WORDS_SUB;
			if (badwords.getType() == 0) {
				// 套红
				BAD_WORDS_SUB = new Perl5Substitution(
						"<span style=\"background-color:red\" ><font color=white>$0</font></span>");
			} else {
				// 自定义替换
				BAD_WORDS_SUB = new Perl5Substitution(badwords.getReplacement());
			}

			Perl5Matcher matcher = new Perl5Matcher();
			return Util.substitute(result, matcher, badWordsPattern, BAD_WORDS_SUB, input, Util.SUBSTITUTE_ALL);
		} catch (MalformedPatternException ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * 判断是否具有敏感词
	 * 
	 * @param input
	 * @param scope
	 * @return
	 */
	public static boolean isMatcher(String input, String scope) {
		BadwordsManager badwordsManager = getBadWordsManager();
		List badwordList = badwordsManager.getBadwordsByScope(scope);
		if (badwordsManager == null) {
			return false;
		}
		StringBuffer regexp = new StringBuffer();
		for (int i = 0; i < badwordList.size(); i++) {
			Badwords badwords = (Badwords) badwordList.get(i);
			String find = badwords.getFind();
			String[] words = find.split(spliter1);

			for (int x = 0; x < words.length; x++) {
				// Excape "|" and "/" to keep us out of trouble in our regexp.

				String[] w2 = words[x].split(spliter);
				for (int y = 0; y < w2.length; y++) {
					// Excape "|" and "/" to keep us out of trouble in our
					// regexp.
					if (StringUtils.hasText(w2[y])) {
						w2[y] = perl.substitute("s#([\\|\\/\\.])#\\\\$1#g", w2[y].trim());
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
			Pattern badWordsPattern = new Perl5Compiler().compile(regexp.toString(),
					Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.READ_ONLY_MASK);
			//
			Perl5Matcher matcher = new Perl5Matcher();
			return matcher.contains(input, badWordsPattern);
		} catch (MalformedPatternException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean isMatcherPost(String input) {
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_POST;
		return isMatcher(input, scope);
	}

	public static boolean isMatcherID(String input) {
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_REG;
		return isMatcher(input, scope);
	}

	public static boolean isMatcherSignaturre(String input) {
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_SIGNATURE;
		return isMatcher(input, scope);
	}

	public static int doPostFilter(StringBuffer result, String input) {
		int i = 0;
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_POST;
		i += doFilter(result, input, scope);
		return i;
	}

	public static int doIDFilter(StringBuffer result, String input) {
		int i = 0;
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_REG;
		i += doFilter(result, input, scope);
		return i;
	}

	public static int doSignatureFilter(StringBuffer result, String input) {
		int i = 0;
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_SIGNATURE;
		i += doFilter(result, input, scope);
		return i;
	}

	public static int doPostHighlightFilter(StringBuffer result, String input) {
		int i = 0;
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_POST;
		i += doHighlightFilter(result, input, scope);
		return i;
	}

	public static int doIDHighlightFilter(StringBuffer result, String input) {
		int i = 0;
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_REG;
		i += doHighlightFilter(result, input, scope);
		return i;
	}

	public static int doSignatureHighlightFilter(StringBuffer result, String input) {
		int i = 0;
		String scope = Badwords.SCOPE_ALL + "," + Badwords.SCOPE_SIGNATURE;
		i += doHighlightFilter(result, input, scope);
		return i;
	}

	/**
	 * 获得敏感词管理器对象
	 * 
	 * @return
	 */
	public static BadwordsManager getBadWordsManager() {
		return (BadwordsManager) ObjectLocator.lookup("badwordsManager", CmsPlugin.PLUGIN_ID);
	}
}
