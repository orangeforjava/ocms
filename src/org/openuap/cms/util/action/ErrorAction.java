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
package org.openuap.cms.util.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.web.mvc.BaseController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 错误处理控制器.
 * </p>
 * 
 * <p>
 * $Id: ErrorAction.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class ErrorAction extends BaseController {

	private String errorViewName;

	public ErrorAction() {
		errorViewName = "/screens/error.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Object status_code = request
				.getAttribute("javax.servlet.error.status_code");
		Object exception_type = request
				.getAttribute("javax.servlet.error.exception_type");
		Object message = request.getAttribute("javax.servlet.error.message");
		Object exception = request
				.getAttribute("javax.servlet.error.exception");
		Object request_uri = request
				.getAttribute("javax.servlet.error.request_uri");
		Object servlet_name = request
				.getAttribute("javax.servlet.error.servlet_name");
		ModelAndView mv = new ModelAndView(errorViewName, model);
		model.put("status_code", status_code);
		model.put("exception_type", exception_type);
		model.put("message", message);
		model.put("exception", exception);
		model.put("request_uri", request_uri);
		model.put("servlet_name", servlet_name);
		return mv;
	}

	public void setErrorViewName(String errorViewName) {
		this.errorViewName = errorViewName;
	}

	public String getErrorViewName() {
		return errorViewName;
	}
}
