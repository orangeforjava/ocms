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
package org.openuap.cms.stat.action;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.DateUtil;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.stat.manager.CountManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 内容计数控制器.
 * </p>
 * 
 * <p>
 * $Id: CounterAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CounterAction extends CMSBaseAction {
	/**@deprecated **/
	private CountManager countManager;
	
	/** 动态内容管理. */
	private DynamicContentManager dynamicContentManager;
	

	private String displayCounterViewName;

	private String defaultScreensPath;

	public CounterAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/stat/";
		displayCounterViewName = defaultScreensPath + "counter_js.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(displayCounterViewName, model);
		model.put("responseType", "text/javaScript");
		String indexId = request.getParameter("indexId");
		String hits = request.getParameter("hits");
		if (indexId != null) {
			Long iid = new Long(indexId);
			ContentIndex counter = dynamicContentManager.getContentIndexById(iid);
			if (counter != null) {
				long hitsDate = counter.getHitsDate().longValue();
				long currentDate = System.currentTimeMillis();
				//
				int hitsTotal = counter.getHitsTotal().intValue();
				int hitsWeek = counter.getHitsWeek().intValue();
				int hitsMonth = counter.getHitsMonth().intValue();
				int hitsToday = counter.getHitsToday().intValue();
				counter.setHitsDate(new Long(currentDate));
				counter.setHitsTotal(new Long(hitsTotal + 1));
				hitsTotal = hitsTotal + 1;
				//
				if (hitsDate == -1 || hitsDate == 0) {
					counter.setHitsMonth(new Long(1));
					counter.setHitsWeek(new Long(1));
					counter.setHitsToday(new Long(1));

					hitsWeek = 1;
					hitsMonth = 1;
					hitsToday = 1;
				} else {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(hitsDate);
					Calendar cal2 = Calendar.getInstance();
					cal2.setTimeInMillis(currentDate);
					boolean sameDay = DateUtil.isSameDay(cal1, cal2);
					if (sameDay) {
						counter.setHitsMonth(new Long(hitsMonth + 1));
						counter.setHitsWeek(new Long(hitsWeek + 1));
						counter.setHitsToday(new Long(hitsToday + 1));
						//
						hitsWeek += 1;
						hitsMonth += 1;
						hitsToday += 1;

					} else {
						boolean sameWeek = DateUtil.isSameWeek(cal1, cal2);
						boolean sameMonth = DateUtil.isSameMonth(cal1, cal2);
						counter.setHitsToday(new Long(1));
						if (sameWeek) {
							counter.setHitsWeek(new Long(hitsWeek + 1));
							hitsWeek += 1;
						} else {
							counter.setHitsWeek(new Long(1));
							hitsWeek = 1;
						}
						if (sameMonth) {
							counter.setHitsMonth(new Long(hitsMonth + 1));
							hitsMonth += 1;
						} else {
							counter.setHitsMonth(new Long(1));
							hitsMonth = 1;
						}
					}
				}
				counter.setLastModifiedDate(System.currentTimeMillis());
				dynamicContentManager.saveContentIndex(counter);
				if (hits == null || hits.equals("total")) {
					model.put("hits", new Integer(hitsTotal));
				} else if (hits.equals("month")) {
					model.put("hits", new Integer(hitsMonth));
				} else if (hits.equals("week")) {
					model.put("hits", new Integer(hitsWeek));
				} else if (hits.equals("today")) {
					model.put("hits", new Integer(hitsToday));
				}
				return mv;
			}
		}
		return null;
	}
//	public ModelAndView perform(HttpServletRequest request,
//			HttpServletResponse response, ControllerHelper helper, Map model) {
//		//
//		ModelAndView mv = new ModelAndView(displayCounterViewName, model);
//		model.put("responseType", "text/javaScript");
//		String indexId = request.getParameter("indexId");
//		String hits = request.getParameter("hits");
//		if (indexId != null) {
//			Long iid = new Long(indexId);
//			CmsCount counter = countManager.getCountById(iid);
//			if (counter != null) {
//				long hitsDate = counter.getHitsDate().longValue();
//				long currentDate = System.currentTimeMillis();
//				//
//				int hitsTotal = counter.getHitsTotal().intValue();
//				int hitsWeek = counter.getHitsWeek().intValue();
//				int hitsMonth = counter.getHitsMonth().intValue();
//				int hitsToday = counter.getHitsToday().intValue();
//				counter.setHitsDate(new Long(currentDate));
//				counter.setHitsTotal(new Long(hitsTotal + 1));
//				hitsTotal = hitsTotal + 1;
//				//
//				if (hitsDate == -1 || hitsDate == 0) {
//					counter.setHitsMonth(new Long(1));
//					counter.setHitsWeek(new Long(1));
//					counter.setHitsToday(new Long(1));
//
//					hitsWeek = 1;
//					hitsMonth = 1;
//					hitsToday = 1;
//				} else {
//					Calendar cal1 = Calendar.getInstance();
//					cal1.setTimeInMillis(hitsDate);
//					Calendar cal2 = Calendar.getInstance();
//					cal2.setTimeInMillis(currentDate);
//					boolean sameDay = DateUtil.isSameDay(cal1, cal2);
//					if (sameDay) {
//						counter.setHitsMonth(new Long(hitsMonth + 1));
//						counter.setHitsWeek(new Long(hitsWeek + 1));
//						counter.setHitsToday(new Long(hitsToday + 1));
//						//
//						hitsWeek += 1;
//						hitsMonth += 1;
//						hitsToday += 1;
//
//					} else {
//						boolean sameWeek = DateUtil.isSameWeek(cal1, cal2);
//						boolean sameMonth = DateUtil.isSameMonth(cal1, cal2);
//						counter.setHitsToday(new Long(1));
//						if (sameWeek) {
//							counter.setHitsWeek(new Long(hitsWeek + 1));
//							hitsWeek += 1;
//						} else {
//							counter.setHitsWeek(new Long(1));
//							hitsWeek = 1;
//						}
//						if (sameMonth) {
//							counter.setHitsMonth(new Long(hitsMonth + 1));
//							hitsMonth += 1;
//						} else {
//							counter.setHitsMonth(new Long(1));
//							hitsMonth = 1;
//						}
//					}
//				}
//				countManager.saveCount(counter);
//				if (hits == null || hits.equals("total")) {
//					model.put("hits", new Integer(hitsTotal));
//				} else if (hits.equals("month")) {
//					model.put("hits", new Integer(hitsMonth));
//				} else if (hits.equals("week")) {
//					model.put("hits", new Integer(hitsWeek));
//				} else if (hits.equals("today")) {
//					model.put("hits", new Integer(hitsToday));
//				}
//				return mv;
//			}
//		}
//		return null;
//	}
	public void setCountManager(CountManager countManager) {
		this.countManager = countManager;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDisplayCounterViewName(String displayCounterViewName) {
		this.displayCounterViewName = displayCounterViewName;
	}

	public void setDynamicContentManager(DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

}
