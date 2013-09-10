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
package org.openuap.cms.resource.action;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.web.mvc.BaseController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 资源URI控制器.
 * </p>
 * 
 * 
 * <p>
 * $Id: URIResourceAction.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class URIResourceAction extends BaseController {
	public URIResourceAction() {
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String resource = request.getParameter("path");
		// the parameters?
		// Object[] keys = request.getParameters().getKeys();
		Enumeration names = request.getParameterNames();
		if (names != null) {
			while (names.hasMoreElements()) {
				String key = (String) names.nextElement();
				model.put(key, request.getParameter(key));
			}
		}
		// $Date: 2006/08/31 02:26:08 $ Weiping Ju
		// add some logical to decide the resource type
		// if is a text,it will be process to freemarker
		// otherwise,write to the response.
		// the path is relative to web root dir
		//
		// the layout should be no!
		model.put("noLayout", "yes");
		if (resource != null) {
			String contentType = FileUtil.getContentType(resource);
			if (contentType != null) {
				//model.put("baseUrl", helper.getBaseURL());
				model.put("contentType", contentType);
				ModelAndView mv = new ModelAndView(resource, model);
				return mv;
			}
		}
		return null;
	}
}
