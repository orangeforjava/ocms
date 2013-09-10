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
package org.openuap.cms.workbench.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.plugin.Plugin;
import org.openuap.plugin.registry.Version;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 状态控制器,在Workbench中显示用户的状态信息
 * </p>
 * 
 * <p>
 * $Id: StatusAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class StatusAction extends CMSBaseAction {

	private String statusViewName = "/plugin/cms/base/screens/status.html";

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(statusViewName);
//		Plugin plugin = WebPluginManagerUtils.getPlugin("base",
//				CmsPlugin.PLUGIN_ID);
		//Version version = plugin.getDescriptor().getVersion();
		//model.put("version", version);
		return mv;

	}
}
