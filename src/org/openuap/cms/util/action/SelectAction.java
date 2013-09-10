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
import org.openuap.cms.core.action.AdminAction;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 选择控制器.
 * </p>
 * 
 * <p>
 * $Id: SelectAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SelectAction extends AdminAction {

	private String defaultScreensPath;

	private String inputDialogViewName;

	//

	/**
	 * 
	 */
	public SelectAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/select/";
		inputDialogViewName = defaultScreensPath + "input.html";
	}

	/**
	 * show the input dialog.
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doInputDialog(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		
		String title = helper.getDecodeString("title","UTF-8");
		String value = helper.getDecodeString("value","UTF-8");
		if (value == null) {
			value = "";
		}
		
		ModelAndView mv = new ModelAndView(inputDialogViewName, model);
		model.put("title", title);
		model.put("value", value);
		return mv;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setInputDialogViewName(String inputDialogViewName) {
		this.inputDialogViewName = inputDialogViewName;
	}
}
