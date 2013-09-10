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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.resource.manager.ResourceManager;
import org.openuap.cms.resource.model.Resource;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 资源下载控制器.
 * </p>
 * 
 * <p>
 * $Id: ResourceDownloadAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ResourceDownloadAction extends CMSBaseAction {
	
	protected ResourceManager resourceManager;

	public ResourceDownloadAction() {
	}

	public ModelAndView perform(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws Exception {
		//
		String file = helper.getString("file");

		if (file != null) {
			try {
				if (file.startsWith("../resource/")) {
					file = file.substring(12, file.length());
					//
				}
				Resource r = getResourceManager().getResourceByPath(file);
				if (r != null) {
					String rootDir = CMSConfig.getInstance().getResourceRootPath();
					String mypath = rootDir + File.separator + file;
					String attName = new String(r.getTitle().getBytes(), "iso8859-1");
					response.setContentType("application/x-msdownload");
					String header = "attachment; filename=" + attName;
					response.setHeader("Content-Disposition", header);
					//
					String finalPath = StringUtil.normalizePath(mypath);

					//
					InputStream dbInput = new FileInputStream(finalPath);
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
					//
					Integer times = r.getDownloadTimes();
					if (times == null) {
						times = new Integer(0);
					}
					//更新下载计数
					int itimes = times.intValue() + 1;
					r.setDownloadTimes(new Integer(itimes));
					getResourceManager().saveResource(r);
					//
				}
			} catch (Exception ex1) {
				log.error("Can't download ", ex1);
				helper.sendError(404);
			}
		}
		return null;
	}

	/**
	 * 下载成功
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
	public ModelAndView afterPerform(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {

		return null;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	/**
	 * @return the resourceManager
	 */
	public ResourceManager getResourceManager() {
		if(resourceManager==null){
			resourceManager=(ResourceManager) ObjectLocator.lookup("cmsResourceManager",CmsPlugin.PLUGIN_ID);
		}
		return resourceManager;
	}
	
}
