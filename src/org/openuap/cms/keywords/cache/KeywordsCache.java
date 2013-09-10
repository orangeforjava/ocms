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
package org.openuap.cms.keywords.cache;

import java.util.List;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.keywords.manager.KeywordsManager;
import org.openuap.cms.keywords.model.Keywords;
import org.openuap.runtime.log.Log;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 关键词缓存.
 * </p>
 * <p>
 * $Id: KeywordsCache.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class KeywordsCache {

	public static Log log = new Log("sys.cache");
	private static String spliter1 = "\\s";
	private static String spliter = ";";
	public static final Perl5Util perl = new Perl5Util();
	public static JCS keywordsCache;
	static {
		try {
			keywordsCache = JCS.getInstance("keywords");
			//
		} catch (CacheException ex) {
			ex.printStackTrace();
		}
	}
	public static List<Pattern> getAllPatterns(String scope) {
		List<Pattern> patterns = (List<Pattern>) keywordsCache.get(scope);
		if (patterns == null) {
			KeywordsManager keywordsManager = getKeywordsManager();
			if (keywordsManager != null) {
				List<Keywords> keywordList = keywordsManager.getKeywordsByScope(scope);
				for (Keywords keywords : keywordList) {
					String find = keywords.getKeyword();
					String[] words = find.split(spliter1);
					StringBuffer regexp = new StringBuffer();
					for (int x = 0; x < words.length; x++) {
						// Excape "|" and "/" to keep us out of trouble in our
						// regexp.
						String[] w2 = words[x].split(spliter);
						for (int y = 0; y < w2.length; y++) {
							// Excape "|" and "/" to keep us out of trouble in
							// our regexp.
							if (StringUtils.hasText(w2[y])) {
								w2[y] = perl.substitute("s#([\\|\\/\\.])#\\\\$1#g", w2[y].trim());
								if (regexp.length() > 0) {
									regexp.append("|");
								}
								regexp.append(w2[y]);
							}
						}
					}
					try {
						Pattern badWordsPattern = new Perl5Compiler().compile(regexp.toString(),
								Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.READ_ONLY_MASK);
						patterns.add(badWordsPattern);
					} catch (MalformedPatternException ex) {
						ex.printStackTrace();
					}
				}
				//
				try {
					keywordsCache.put(scope, patterns);
				} catch (CacheException e) {
					e.printStackTrace();
				}
			}
		}
		return patterns;
	}
	public static List<Keywords> getAllKeywords(String scope) {
		List<Keywords> keywordList = (List<Keywords>) keywordsCache.get("keywords-"+scope);
		if(keywordList==null){
			KeywordsManager keywordsManager = getKeywordsManager();
			if (keywordsManager != null) {
				keywordList = keywordsManager.getKeywordsByScope(scope);
				try {
					keywordsCache.put("keywords-"+scope, keywordList);
				} catch (CacheException e) {
					e.printStackTrace();
				}
			}
		}
		return keywordList;
	}
	public static void remove(){
		try {
			keywordsCache.clear();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得关键词管理器对象
	 * 
	 * @return
	 */
	public static KeywordsManager getKeywordsManager() {
		return (KeywordsManager) ObjectLocator.lookup("keywordsManager", CmsPlugin.PLUGIN_ID);
	}
}