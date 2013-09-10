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
package org.openuap.cms.search.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.compass.core.CompassHit;
import org.compass.core.Property;
import org.openuap.cms.cm.cache.ContentModelCache;
import org.openuap.cms.cm.model.ContentField;

/**
 * <p>
 * Compass命中数帮助类.
 * </p>
 * 
 * <p>
 * $Id: CompassHitUtils.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CompassHitUtils {
	public static void convertHitsToMap(CompassHit[] hits, List rs) {
		convertHitsToMap(hits, rs, null);
	}

	public static void convertHitsToMap(CompassHit[] hits, List rs,
			String[] highlights) {
		//
		if (hits != null && rs != null) {
			for (CompassHit hit : hits) {
				Map objMap = new Hashtable();
				String alias = hit.getAlias();
				float score = hit.getScore();
				//
				Map<String, ContentField> fields = ContentModelCache
						.getFieldsMap(alias);
				Map<String, ValueConverter> valueConverterMap = getValueConverterMap();
				objMap.put("alias", alias);
				objMap.put("score", score);
				Property[] pAry = hit.getResource().getProperties();
				for (Property p : pAry) {

					String name = p.getName();

					Object value = p.getStringValue();
					// System.out.println("name="+name+";value="+value);
					// 处理搜索引擎特殊的null情况
					ValueConverter converter = valueConverterMap.get(name);
					if (value != null && value.equals("null")) {
						value = "";
					} else if (value != null & converter != null) {
						value = converter.convert(value);
					} else {
						ContentField field = fields.get(name);
						if (field != null) {
							value = field.convertFieldValue(value);
						}
					}

					// 处理高亮部分
					if (highlights != null) {
						for (String highlight : highlights) {
							if (name.equals(highlight)) {
								if (hit.getHighlightedText() != null) {
									String highlightedValue = hit
											.getHighlightedText()
											.getHighlightedText(highlight);
									if (highlightedValue != null) {
										objMap.put("highlighted-" + name,
												highlightedValue);
									}
								}
							}
						}
					}
					//
					objMap.put(name, value);
				}
				rs.add(objMap);
			}
		}
	}

	private static Map<String, ValueConverter> getValueConverterMap() {
		Map<String, ValueConverter> converterMap = new HashMap<String, ValueConverter>();
		LongValueConverter longConverter = new LongValueConverter();
		IntegerValueConverter integerConverter = new IntegerValueConverter();
		converterMap.put("indexId", longConverter);
		converterMap.put("contentId", longConverter);
		converterMap.put("nodeId", longConverter);
		converterMap.put("publishDate", longConverter);
		converterMap.put("creationDate", longConverter);
		converterMap.put("modifiedDate", longConverter);
		converterMap.put("creationUserId", longConverter);
		converterMap.put("lastModifiedUserId", longConverter);
		converterMap.put("contributionUserId", longConverter);
		converterMap.put("contributionId", longConverter);
		converterMap.put("top", integerConverter);
		converterMap.put("pink", integerConverter);
		converterMap.put("Sort", integerConverter);
		return converterMap;
	}

	static interface ValueConverter {
		public Object convert(Object src);
	}

	static class LongValueConverter implements ValueConverter {

		public Object convert(Object src) {
			return new Long(src.toString());
		}

	}

	static class IntegerValueConverter implements ValueConverter {

		public Object convert(Object src) {
			return new Integer(src.toString());
		}

	}
}
