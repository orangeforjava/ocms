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

import java.awt.Dimension;
import java.io.File;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.resource.manager.ResourceManager;
import org.openuap.cms.resource.model.Resource;
import org.openuap.cms.util.file.PathNameStrategy;
import org.openuap.cms.util.file.impl.DatePathNameStrategy;
import org.openuap.util.ImageUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 资源上传控制器.
 * </p>
 * 
 * <p>
 * $Id: UploadAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UploadAction extends AdminAction {

	protected String jsinfoViewName;

	protected PathNameStrategy pathNameStrategy;

	protected ResourceManager resourceManager;

	protected String uploadFileViewName;

	public UploadAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		jsinfoViewName = "/plugin/cms/base/screens/jsinfo.html";
		uploadFileViewName = "/plugin/cms/base/screens/resource/resource_upload.html";
	}

	protected ModelAndView beforeUploadFile(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		return null;
	}

	protected ModelAndView afterUploadFile(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		return null;
	}

	public ModelAndView doUploadFile(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		//
		//
		ModelAndView mv1 = beforeUploadFile(request, response, helper, model);
		if (mv1 != null) {
			return mv1;
		}
		//
		ModelAndView mv = new ModelAndView(jsinfoViewName, model);
		String type = request.getParameter("type");
		String category = request.getParameter("category");
		String nodeId = request.getParameter("nodeId");
		String changeName = request.getParameter("changeName");
		String msgView = request.getParameter("msgView");
		String customCategory = request.getParameter("customCategory");
		if (msgView == null) {
			msgView = "";
		}
		if (nodeId == null) {
			nodeId = "0";
		}
		if (customCategory == null) {
			customCategory = "";
		}
		model.put("msgView", msgView);
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			//
			MultipartFile multipartFile = multipartRequest.getFile("uploadFile");
			String contentType = FileUtil.getExtension(multipartFile.getOriginalFilename());
			// is valid type
			if (!isValidFileType(category, contentType)) {
				String msgType = "error";
				String msg = "请选择正确的文件类型-(" + getAcceptFileType(category) + ")";
				model.put("msgType", msgType);
				model.put("msg", msg);
				return mv;
			}
			// dir create,auto mode,every dir not more than 100 files
			// or simple the
			// year(4)/month(2)/day(2)/type+year(4)+month(2)+day(2)+hour(2)+minute(2)+second(2)+ms(2)
			if (pathNameStrategy == null) {
				pathNameStrategy = new DatePathNameStrategy();
				pathNameStrategy.setUserName(this.getUser().getName());
			}
			//
			String pathName = pathNameStrategy.getPathName();
			File dir = makeDir(category + File.separator + pathName);
			// transfer the file
			String destFileName = pathNameStrategy.getFileName(category);
			String fileName = destFileName + "."
					+ FileUtil.getExtension(multipartFile.getOriginalFilename());
			File file = new File(dir, fileName);

			// add to the database
			Resource rs = new Resource();
			rs.setCategory(category);
			// get the now seconds
			long now = System.currentTimeMillis();
			//
			// Integer inow = new Integer(String.valueOf(now));
			rs.setCreationDate(new Long(now));
			rs.setModifiedDate(new Long(now));
			Long nid;
			if (nodeId != null) {
				nid = new Long(nodeId);
			} else {
				nid = new Long(0L);
			}
			rs.setNodeId(nid);
			rs.setCreationUserId(this.getUser().getUserId());
			//
			if (category.equals("img")) {
				Dimension dm = ImageUtil.getDimension(multipartFile.getInputStream());
				//
				if (dm != null) {
					String info = dm.width + "*" + dm.height;
					rs.setInfo(info);
				} else {
					rs.setInfo("");
				}
			} else {
				rs.setInfo("");
			}
			//
			rs.setName(destFileName + "."
					+ FileUtil.getExtension(multipartFile.getOriginalFilename()));
			rs.setParentId(new Long(0));
			long size = multipartFile.getSize();
			size = size / 1024; // b->kb
			if (size == 0) {
				size = 1;
			}
			Integer kbsize = new Integer(String.valueOf(size));
			rs.setSize(kbsize);
			rs.setSrc(" ");
			rs.setType(new Integer(type));

			rs.setTitle(FileUtil.getFileName(multipartFile.getOriginalFilename()));
			rs.setPath(category + "/" + pathName + "/" + destFileName + "."
					+ FileUtil.getExtension(multipartFile.getOriginalFilename()));
			//
			rs.setDownloadTimes(new Integer(0));
			rs.setCustomCategory(customCategory);
			resourceManager.addResource(rs);
			//
			multipartFile.transferTo(file);
			//
			String msgType = "success";
			String msg = "上传文件成功，" + "文件已经成功改名为:" + fileName;
			model.put("msgType", msgType);
			model.put("msg", msg);
			return mv;

		} catch (Exception e) {
			e.printStackTrace();
			String msgType = "error";
			String msg = "上传文件出现意外错误：" + e.getMessage();
			model.put("msgType", msgType);
			model.put("msg", msg);

			return mv;

		}
	}

	/**
	 * show the editor upload dialog.
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
	public ModelAndView doShowUploadDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(uploadFileViewName, model);
		String nodeId = request.getParameter("nodeId");
		String category = request.getParameter("category");
		model.put("nodeId", nodeId);
		model.put("category", category);
		return mv;

	}

	protected File makeDir(String path) {
		String rootDir = CMSConfig.getInstance().getResourceRootPath();
		String mypath = rootDir + File.separator + path;
		mypath = StringUtil.normalizePath(mypath);
		File dir = new File(mypath);
		if (!dir.exists() || dir.isFile()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 
	 * @param type
	 *            String
	 * @param contentType
	 *            String
	 * @return boolean
	 */
	protected boolean isValidFileType(String type, String contentType) {
		//
		String acceptTypes = getAcceptFileType(type);
		if (acceptTypes != null) {
			StringTokenizer tk = new StringTokenizer(acceptTypes, "|");
			while (tk.hasMoreTokens()) {
				String acType = tk.nextToken();
				if (contentType.indexOf(acType) > -1) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	protected String getAcceptFileType(String type) {
		if (type != null) {
			if (type.equals("img")) {
				String acceptTypes = CMSConfig.getInstance().getUploadFileImageType();
				return acceptTypes;
			} else if (type.equals("flash")) {
				String acceptTypes = CMSConfig.getInstance().getUploadFileFlashType();
				return acceptTypes;
			} else if (type.equals("attach")) {
				String acceptTypes = CMSConfig.getInstance().getUploadFileAttachType();
				return acceptTypes;
			} else if (type.equals("sattach")) {
				String acceptTypes = CMSConfig.getInstance().getUploadFileAttachType();
				return acceptTypes;
			}else if(type.equals("media"))
			{
				String acceptTypes = CMSConfig.getInstance().getUploadFileMediaType();
				return acceptTypes;
			}
			
		}
		return null;
	}

	protected String getFullPath() {
		return "";
	}

	public void setJsinfoViewName(String jsinfoViewName) {
		this.jsinfoViewName = jsinfoViewName;
	}

	public void setPathNameStrategy(PathNameStrategy pathNameStrategy) {
		this.pathNameStrategy = pathNameStrategy;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setUploadFileViewName(String uploadFileViewName) {
		this.uploadFileViewName = uploadFileViewName;
	}

	public ResourceManager getResourceManager() {
		return this.resourceManager;
	}

	public String getJsinfoViewName() {
		return jsinfoViewName;
	}

	public PathNameStrategy getPathNameStrategy() {
		return pathNameStrategy;
	}

	public String getUploadFileViewName() {
		return uploadFileViewName;
	}
}
