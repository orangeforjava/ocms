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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.util.ui.FileItem;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 路径选择控制器
 * </p>
 * 
 * <p>
 * $Id: PathSelectAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PathSelectAction extends AdminAction {

	private String pathSelDialogViewName;

	private String pathSelViewName;

	private String defaultScreensPath;

	public PathSelectAction() {
		initDefaultProperties();
	}

	protected void initDefaultProperties() {
		defaultScreensPath = "/plugin/cms/base/screens/resource/";
		//
		pathSelDialogViewName = defaultScreensPath + "path_select_dialog.html";
		pathSelViewName = defaultScreensPath + "path_select.html";
	}

	/**
	 * 显示Path选择对话框
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
	public ModelAndView doSelPathDialog(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(pathSelDialogViewName, model);		
		//
		putRootFiles(request,model);
		//		
		return mv;
	}
	protected void putRootFiles(HttpServletRequest request,Map model){
		List roots = new ArrayList();
		String webRootPath = this.getServletContext().getRealPath("");
		webRootPath = StringUtil.normalizePath(webRootPath);
		File webRootFile = new File(webRootPath);
		roots.add(webRootFile);
		File[] rootFiles = File.listRoots();
		for (File rootFile : rootFiles) {
			
			roots.add(rootFile);
		}
		model.put("roots", roots);
		model.put("rootDir", webRootFile);
	}
	/**
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
	public ModelAndView doPathListFile(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(pathSelViewName, model);
		//		
		String subdir = helper.getDecodeString("PATH", "UTF-8");
		String extra = request.getParameter("extra");
		String rootDir = helper.getDecodeString("rootDir", "UTF-8");
		String fullPath="";
		if (rootDir != null) {
			if (subdir != null) {
				if (extra != null) {
					if (extra.equals("updir")) {
						System.out.println("subdir=" + subdir);
					} else if (extra.equals("mkdir")) {
						String dirname = helper.getDecodeString("dirname", "UTF-8");
						if (dirname != null) {
							String destDirPath = rootDir + "/" + subdir + "/" + dirname;
							destDirPath = StringUtil.normalizePath(destDirPath);
							//
							File mkDir = new File(destDirPath);
							if (!mkDir.exists()) {
								mkDir.mkdir();
							}
						}
					}

				}
				fullPath = rootDir+File.separator + subdir;
			}
			//
			fullPath = StringUtil.normalizePath(fullPath);
			//System.out.println("fullPath=" + fullPath);
			//
			File dir = new File(fullPath);
			List fileList = new ArrayList();
			if (dir.isDirectory() && dir.canRead()) {
				File files[] = dir.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						// only accept the directory
						if (pathname.isDirectory()) {
							return true;
						}
						return false;
					}
				});
				//
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						File file = files[i];
						String name = file.getName();
						String type = FileUtil.getContentType(name);
						String icon = FileUtil.getIcon2(file);
						fileList.add(new FileItem(file.isDirectory(), name, type, icon));
					}
				} // end if files not null
				putRootFiles(request,model);
				model.put("fileList", fileList);
				model.put("PATH", subdir);
				model.put("rootDir", new File(rootDir));
				
				return mv;
			}
		}
		return null;
	}
}
