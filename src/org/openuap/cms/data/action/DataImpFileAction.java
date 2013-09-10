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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.web.mvc.BaseController;
import org.openuap.cms.data.DataImporter;
import org.openuap.cms.data.manager.DataImportManager;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 数据导入文件控制器
 * </p>
 * 
 * <p>
 * $Id: DataImpFileAction.java 3993 2011-01-05 11:32:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DataImpFileAction extends BaseController {

	private String framesetViewName;

	private String headerViewName;
	/** 列表模板名.*/
	private String listViewName;

	private String defaultScreensPath;
	
	private DataImportManager dataImportManager;

	public DataImpFileAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/data/import/file/";
		framesetViewName = defaultScreensPath + "file_list_frameset.html";
		headerViewName = defaultScreensPath + "file_list_header.html";
		listViewName = defaultScreensPath + "file_list.html";
	}
	/**
	 * 缺省动作，显示框架视图
	 */
	public ModelAndView perform(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		
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
		String name = request.getParameter("name");
		if (name != null) {
			DataImporter importer = dataImportManager.getDateImporter(name);
			model.put("importer", importer);
		}
		return mv;
	}

	/**
	 * 列出所有的备份文件
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doList(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		ModelAndView mv = new ModelAndView(listViewName, model);
		String name = request.getParameter("name");
		if (name != null) {
			DataImporter importer = dataImportManager.getDateImporter(name);
			model.put("importer", importer);
			List files = dataImportManager.getDataFiles(name);
			model.put("files", files);
		}

		return mv;
	}

	/**
	 * 查看具体文件的内容
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doView(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		
		String name = request.getParameter("name");
		String fileName = request.getParameter("fileName");
		if (name != null && fileName != null) {
			response.setContentType("text/xml;encoding=utf-8");
			DataImporter importer=dataImportManager.getDateImporter(name);
			if(importer!=null){
				File dataDir=importer.getDataDir();
				File file=new File(dataDir,fileName);
				if(file.exists()){
					InputStream dbInput = new FileInputStream(file);
					response.setContentLength(dbInput.available());
					ServletOutputStream os = response.getOutputStream();
					byte buf[] = new byte[4096];
					BufferedInputStream bis = new BufferedInputStream(dbInput);
					int j;
					while ((j = bis.read(buf, 0, 4096)) != -1) {
						os.write(buf, 0, j);
					}
					bis.close();
					os.flush();
					os.close();
				}
			}
		}
		return null;
	}
	/**
	 * 下载选定的数据文件
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doDownload(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		//下载的显示名
		String name = request.getParameter("name");
		//下载的文件名
		String fileName = request.getParameter("fileName");
		if (name != null && fileName != null) {
			String attName = new String(fileName.getBytes(), "iso8859-1");
			response.setContentType("application/x-msdownload");
			String header = "attachment; filename=" + attName;
			response.setHeader("Content-Disposition", header);
			DataImporter importer=dataImportManager.getDateImporter(name);
			if(importer!=null){
				File dataDir=importer.getDataDir();
				File file=new File(dataDir,fileName);
				if(file.exists()){
					InputStream dbInput = new FileInputStream(file);
					response.setContentLength(dbInput.available());
					ServletOutputStream os = response.getOutputStream();
					byte buf[] = new byte[4096];
					BufferedInputStream bis = new BufferedInputStream(dbInput);
					int j;
					while ((j = bis.read(buf, 0, 4096)) != -1) {
						os.write(buf, 0, j);
					}
					bis.close();
					os.flush();
					os.close();
				}
			}
		}
		return null;
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
}
