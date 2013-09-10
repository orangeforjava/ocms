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
package org.openuap.cms.engine.macro.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.dao.jdbc.BaseDaoSupport;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.util.MultiResField;
import org.openuap.cms.cm.util.ResRefBean;
import org.openuap.cms.ds.manager.DataSourceManager;
import org.openuap.cms.ds.model.DataSourceModel;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.runtime.util.ObjectLocator;
import org.openuap.util.extractor.HtmlExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * <p>
 * 抽象宏引擎实现
 * </p>
 * 
 * <p>
 * $Id: AbstractCmsMacroEngine.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 3.0
 */
public abstract class AbstractCmsMacroEngine implements CmsMacroEngine {

	public Date getDate(long seconds) {
		if (seconds == 0) {
			return new Date(System.currentTimeMillis());
		} else {
			return new Date(seconds * 1000l);
		}
	}

	public Date getDate2(long seconds) {
		if (seconds == 0) {
			return new Date(System.currentTimeMillis());
		} else {
			return new Date(seconds);
		}
	}

	public Date getDate2(String seconds) {
		if (seconds == null) {
			return new Date(System.currentTimeMillis());
		} else {
			long s = Long.parseLong(seconds);
			return new Date(s);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ResRefBean> getMultiRes(String input) {
		MultiResField field = MultiResField.fieldFromString(input);
		if (field.getNums() > 0) {
			return field.getReses();
		}
		return Collections.EMPTY_LIST;
	}

	public PageBuilder getConentPagerInfo(String content, String size,
			String type) {
		int maxLength = Integer.parseInt(size);
		int start = 0;
		int page = 0;
		//
		String content2 = content;
		int totalLength = content2.length();
		if (totalLength > maxLength) {
			while (totalLength > maxLength) {
				int pos = totalLength - 1;
				int end = 0;
				String ppattern = "(<P>|<p>|</p>|</P>|<BR>|<br>|<br/>|<BR/>)";
				Pattern p2 = Pattern.compile(ppattern);
				Matcher m2 = p2.matcher(content2);
				boolean rs = m2.find(maxLength);
				// 假定必须找到，否则就不分页
				if (rs) {
					end = m2.start();
					page++;
					String pSplitter = m2.group(0);
					if (pSplitter.equalsIgnoreCase("</p>")) {
						end = end + 4;
					}
					content2 = content2.substring(end);
					totalLength = content2.length();
					//
				} else {
					page++;
					break;
				}
			}
			if (totalLength > 0) {
				page++;
			}

		} else {
			page++;
		}
		PageBuilder pb = new PageBuilder();
		pb.items(page);
		pb.itemsPerPage(1);
		return pb;
	}

	public List<Map> getSqlSearchResult(String db, String sql) {
		DataSourceManager dataSourceManager = (DataSourceManager) ObjectLocator
				.lookup("dataSourceManager", CmsPlugin.PLUGIN_ID);
		List<Map> rs = new ArrayList<Map>();
		if (dataSourceManager != null) {
			try {
				DataSourceModel dsm = dataSourceManager.getDataSourceByName(db);
				DriverManagerDataSource dmd = new DriverManagerDataSource();
				dmd.setDriverClassName(dsm.getDriverClassName());
				dmd.setUrl(dsm.getUrl());
				dmd.setUsername(dsm.getUserName());
				dmd.setPassword(dsm.getPassword());
				BaseDaoSupport baseDao = new BaseDaoSupport();
				// 动态设置数据源
				baseDao.setDataSource(dmd);
				baseDao.setUseDataSourceDialect(true);
				rs = baseDao.getDBMapObjectList(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	public String getExtraPublishContent(String id) {
		return "";
	}

	public Long getNow() {
		return System.currentTimeMillis() / 1000L;
	}

	public Long getToday() {
		// TODO 改进
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		//
		Calendar today = Calendar.getInstance();
		today.set(year + 1900, month, day);
		return today.getTimeInMillis() / 1000L;
	}

	public String highlightWords(String input, String keywords) {
		if (input != null) {
			return StringUtil.highlightWords(input, keywords);
		}
		return "";
	}

	/**
	 * FIX:需要替换掉分页标识
	 */
	public String getExtractHtmlContent(String content, String keyword,
			int length) {
		String partText = "";
		try {
			if (content != null) {
				HtmlExtractor extractor = new HtmlExtractor();
				String extractedText = extractor.getText(content);
				// 替换掉分页标识
				String ppattern = "#p#([\\s\\p{Print}[^\\x00-\\xff]&&[^#]]*?)#e#";
				extractedText = extractedText.replaceAll(ppattern, "");

				int totallen = extractedText.length();

				if (keyword != null) {
					int start = extractedText.indexOf(keyword);

					if (start >= 0) {
						// 发现关键字，替换变红
						if (start + length > totallen) {
							if (start >= (start + length - totallen)) {
								partText = extractedText.substring(totallen
										- length, totallen);

							} else {
								partText = extractedText.substring(0, totallen);
							}
						} else {
							partText = extractedText.substring(start, length
									+ start);
						}
					} else {
						partText = extractedText.substring(0,
								totallen > length ? length : totallen);
					}
					// 替换关键字变红
					if (partText != null && !partText.equals("")) {
						partText = partText.replaceAll(keyword,
								"<font color=red>" + keyword + "</font>");
					}

				} else {
					partText = extractedText.substring(0,
							totallen > length ? length : totallen);
				}
				//
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return partText;// + "...";
	}

	public String getExtraPublishPath(String id) {
		return "";
	}

	public String getExtraPublishUrl(String id) {
		return "";
	}
}
