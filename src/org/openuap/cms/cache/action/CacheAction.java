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
package org.openuap.cms.cache.action;

import java.text.DecimalFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jcs.admin.JCSAdminBean;
import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.AdminAction;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 缓存管理控制器.
 * </p>
 * <p>
 * $Id: CacheAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CacheAction extends AdminAction {
	// Keys for parameters

	private static final String CACHE_NAME_PARAM = "cacheName";

	private static final String ACTION_PARAM = "action";

	private static final String KEY_PARAM = "key";

	private static final String SILENT_PARAM = "silent";

	// Possible values for 'action' parameter

	private static final String CLEAR_ALL_REGIONS_ACTION = "clearAllRegions";

	private static final String CLEAR_REGION_ACTION = "clearRegion";

	private static final String REMOVE_ACTION = "remove";

	private static final String DETAIL_ACTION = "detail";

	private String defaultViewName = "/plugin/cms/base/screens/cache/index.html";

	private String regionDetailViewName = "/plugin/cms/base/screens/cache/regionDetail.html";

	public CacheAction() {
	}

	/**
	 * 
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		int refresh = helper.getInt("refresh", -1);

		JCSAdminBean admin = new JCSAdminBean();
		model.put("refresh", new Integer(refresh));
		model.put("cacheInfoRecords", admin.buildCacheInfo());
		putRuntimeInfo(model);
		return new ModelAndView(defaultViewName, model);
	}

	public ModelAndView doRunGarbageCollection(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		return perform(request, response, helper, model);
	}

	public ModelAndView doDetail(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String cacheName = request.getParameter(CACHE_NAME_PARAM);
		JCSAdminBean admin = new JCSAdminBean();
		model.put("cacheName", cacheName);
		model.put("elementInfoRecords", admin.buildElementInfo(cacheName));
		return new ModelAndView(regionDetailViewName, model);

	}

	public ModelAndView doClearAllRegions(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		JCSAdminBean admin = new JCSAdminBean();
		admin.clearAllRegions();
		return perform(request, response, helper, model);
	}

	public ModelAndView doClearRegion(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String cacheName = request.getParameter(CACHE_NAME_PARAM);
		JCSAdminBean admin = new JCSAdminBean();
		if (cacheName != null) {
			admin.clearRegion(cacheName);
		}
		return perform(request, response, helper, model);
	}

	public ModelAndView doRemove(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String[] keys = request.getParameterValues(KEY_PARAM);
		String cacheName = request.getParameter(CACHE_NAME_PARAM);
		JCSAdminBean admin = new JCSAdminBean();
		for (int i = 0; i < keys.length; i++) {
			admin.removeItem(cacheName, keys[i]);
		}
		return doDetail(request, response, helper, model);
	}
	/**
	 * 设置运行时信息
	 * @param model
	 */
	private void putRuntimeInfo(Map model) {
		Runtime runtime = Runtime.getRuntime();
		//
		DecimalFormat mbFormat = new DecimalFormat("#0.00");
		DecimalFormat percentFormat = new DecimalFormat("#0.0");
		double freeMemory = (double) runtime.freeMemory() / (1024 * 1024);
		double maxMemory = (double) runtime.maxMemory() / (1024 * 1024);
		double totalMemory = (double) runtime.totalMemory() / (1024 * 1024);
		double usedMemory = totalMemory - freeMemory;
		double percentFree = ((maxMemory - usedMemory) / maxMemory) * 100.0;
		double percentUsed = 100 - percentFree;
		int percent = 100 - (int) Math.round(percentFree);
		model.put("freeMemory", new Double(freeMemory));
		model.put("maxMemory", mbFormat.format(maxMemory));
		model.put("totalMemory", new Double(totalMemory));
		model.put("usedMemory", mbFormat.format(usedMemory));
		model.put("percentFree", new Double(percentFree));
		model.put("percentUsed", percentFormat.format(percentUsed));
		model.put("percent", new Double(percent));

	}

}
