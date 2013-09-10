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
import org.openuap.cms.data.manager.DataImportManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 数据导入控制器
 * </p>
 * 
 * <p>
 * $Id: DataImportAction.java 3993 2011-01-05 11:32:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DataImportAction extends BaseController {

	private String defaultScreensPath;

	private String framesetViewName;

	private String headerViewName;

	private String listViewName;

	private String operationView;

	private DataImportManager dataImportManager;

	public DataImportAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {

		defaultScreensPath = "/plugin/cms/base/screens/data/import/";
		framesetViewName = defaultScreensPath + "import_list_frameset.html";
		headerViewName = defaultScreensPath + "import_list_header.html";
		listViewName = defaultScreensPath + "import_list.html";
		operationView = defaultScreensPath + "import_operation.html";
	}

	@Override
	public ModelAndView perform(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		//ModelAndView mv = new ModelAndView(framesetViewName, model);
		return this.doList(request, response, helper, model);
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
		List importers=dataImportManager.getDataImporters();
		model.put("importers", importers);
		return mv;
	}
	/**
	 * 执行导入操作
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doImport(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(operationView, model);
		model.put("op","import");
		try {
			String name=request.getParameter("name");
			String fileName=request.getParameter("fileName");
			if(name!=null){
				if(fileName!=null){
					dataImportManager.importData(name,fileName);
				}else{
					dataImportManager.importData(name);
				}
				
			}
			model.put("rs", "success");
		} catch (Exception e) {
			model.put("rs", "failed");
			model.put("msg", e);
		}
		return mv;
	}
	public void setDataImportManager(DataImportManager dataImportManager) {
		this.dataImportManager = dataImportManager;
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
