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
package org.openuap.cms.badwords.cache;

import java.util.List;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.badwords.manager.BadwordsManager;
import org.openuap.cms.badwords.model.Badwords;
import org.openuap.runtime.log.Log;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 敏感词缓存.
 * </p>
 * <p>
 * $Id: BadwordsCache.java 3966 2010-12-16 12:10:02Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public class BadwordsCache {

	public static Log log = new Log("sys.cache");
	private static String spliter1 = "\\s";
	private static String spliter = ";";
	public static final Perl5Util perl = new Perl5Util();
	public static JCS badwordsCache;
	static {
		try {
			badwordsCache = JCS.getInstance("badwords");
			//
		} catch (CacheException ex) {
			ex.printStackTrace();
		}
	}
	public static List<Pattern> getAllPatterns(String scope) {
		List<Pattern> patterns = (List<Pattern>) badwordsCache.get(scope);
		if (patterns == null) {
			BadwordsManager badwordsManager = getBadWordsManager();
			if (badwordsManager != null) {
				List<Badwords> badwordList = badwordsManager.getBadwordsByScope(scope);
				for (Badwords badwords : badwordList) {
					String find = badwords.getFind();
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
					badwordsCache.put(scope, patterns);
				} catch (CacheException e) {
					e.printStackTrace();
				}
			}
		}
		return patterns;
	}
	public static List<Badwords> getAllBadwords(String scope) {
		List<Badwords> badwordList = (List<Badwords>) badwordsCache.get("badwords-"+scope);
		if(badwordList==null){
			BadwordsManager badwordsManager = getBadWordsManager();
			if (badwordsManager != null) {
				badwordList = badwordsManager.getBadwordsByScope(scope);
				try {
					badwordsCache.put("badwords-"+scope, badwordList);
				} catch (CacheException e) {
					e.printStackTrace();
				}
			}
		}
		return badwordList;
	}
	public static void remove(){
		try {
			badwordsCache.clear();
		} catch (CacheException e) {
			e.printStackTrace();
		}
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