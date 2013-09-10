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
package org.openuap.cms.data.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.web.mvc.BaseController;
import org.openuap.cms.data.manager.DataExportManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 数据导出控制器
 * </p>
 * 
 * <p>
 * $Id: DataExportAction.java 3993 2011-01-05 11:32:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @verison 1.0
 */
public class DataExportAction extends BaseController {

	private String defaultScreensPath;

	private String framesetViewName;

	private String headerViewName;

	private String listViewName;

	private String operationView;

	//
	private DataExportManager dataExportManager;

	public DataExportAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {

		defaultScreensPath = "/plugin/cms/base/screens/data/export/";
		framesetViewName = defaultScreensPath + "export_list_frameset.html";
		headerViewName = defaultScreensPath + "export_list_header.html";
		listViewName = defaultScreensPath + "export_list.html";
		operationView = defaultScreensPath + "export_operation.html";
	}

	/**
	 * 
	 */
	public ModelAndView perform(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		//ModelAndView mv = new ModelAndView(framesetViewName, model);
		return doList(request,response,helper,model);
	}
	
	/**
	 * 显示信息管理头
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doHeader(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {

		ModelAndView mv = new ModelAndView(headerViewName, model);
		return mv;
	}
	/**
	 * 显示导出者列表
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(listViewName, model);
		List exporters=dataExportManager.getDataExporters();
		model.put("exporters", exporters);
		return mv;
	}
	/**
	 * 执行导出操作
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doExport(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(operationView, model);
		model.put("op","export");
		try {
			String name=request.getParameter("name");
			if(name!=null){
				dataExportManager.exportData(name);
			}
			model.put("rs", "success");
		} catch (Exception e) {
			model.put("rs", "failed");
			model.put("msg", e);
		}
		return mv;
	}
	public void setDataExportManager(DataExportManager dataExportManager) {
		this.dataExportManager = dataExportManager;
	}

	

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setFramesetViewName(String framesetViewName) {
		this.framesetViewName = framesetViewName;
	}

	public void setHeaderViewName(String headerViewName) {
		this.headerViewName = headerViewName;
	}

	public void setListViewName(String listViewName) {
		this.listViewName = listViewName;
	}

	public void setOperationView(String operationView) {
		this.operationView = operationView;
	}

}
