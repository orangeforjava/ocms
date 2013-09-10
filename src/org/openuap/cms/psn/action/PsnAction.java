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
package org.openuap.cms.psn.action;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.psn.security.PsnPermissionConstant;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.util.ui.FileItem;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * PSN管理控制器.
 * </p>
 * 
 * <p>
 * $Id: PsnAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PsnAction extends AdminAction {

	private String psnViewName;

	private String psnAddViewName;

	private String psnEditViewName;

	//
	private String psnSelDialogViewName;

	private String psnSelViewName;

	private String defaultScreensPath;

	private PsnManager psnManager;

	public PsnAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/psn/";
		psnViewName = defaultScreensPath + "psn.html";
		psnAddViewName = defaultScreensPath + "psn_add.html";
		psnEditViewName = defaultScreensPath + "psn_edit.html";
		//
		psnSelDialogViewName = defaultScreensPath + "psn_select_dialog.html";
		psnSelViewName = defaultScreensPath + "psn_select.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		//
		if (!SecurityUtil.hasPermission(PsnPermissionConstant.OBJECT_TYPE
				.toString(), "-1", PsnPermissionConstant.ListPsn)) {
			throw new UnauthorizedException();
		}
		//
		ModelAndView mv = new ModelAndView(psnViewName, model);
		List psns = this.psnManager.getAllPsn();

		model.put("psns", psns);
		return mv;
	}

	/**
	 * delete the psn
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
	 * @throws
	 */
	public ModelAndView doDelete(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws UnauthorizedException {
		String psn_id = request.getParameter("psnid");
		// 权限检查
		if (!SecurityUtil.hasPermission(PsnPermissionConstant.OBJECT_TYPE
				.toString(), psn_id, PsnPermissionConstant.DeletePsn)) {
			throw new UnauthorizedException();
		}
		if (psn_id != null) {
			try {
				Long id = new Long(psn_id);
				this.psnManager.deletePsn(id);
			} catch (Exception ex) {
				model.put("exception", ex);
				return this.errorPage(request, response, helper,
						"delete_psn_failed", model);
			}
		}
		return this.successPage(request, response, helper,
				"delete_psn_success", model);
	}

	/**
	 * 检查发布点 本地发布点检查是否存在，远程发布点检查是否可以连接
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws UnauthorizedException
	 * @throws IOException
	 */
	public ModelAndView doCheck(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		long psn_id = helper.getLong("psnid", 0L);
		boolean success = false;
		if (psn_id != 0L) {
			Psn psn = psnManager.getPsnById(psn_id);
			if (psn != null) {
				int psnType = psn.getType();
				if (psnType == Psn.LOCAL_PSN_TYPE) {
					// 本地类型
					String localPath = psn.getLocalPath();
					String root = this.getServletContext().getRealPath("");
					String fullPath = root + "/" + localPath;
					fullPath = FilenameUtils.normalize(fullPath);
					File f = new File(fullPath);
					//
					if (f.exists() && f.canRead() && f.canWrite()) {
						//
						success = true;
					}
				}
			}
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 显示PSN选择对话框
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
	public ModelAndView doSelPsnDialog(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(psnSelDialogViewName, model);
		List psns = psnManager.getAllPsn();
		model.put("psns", psns);
		return mv;
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
	public ModelAndView doPsnListFile(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(psnSelViewName, model);
		//
		String psnId = request.getParameter("psnId");
		String subdir = helper.getDecodeString("PATH", "UTF-8");
		String extra = request.getParameter("extra");

		if (psnId != null) {
			Long id = new Long(psnId);
			Psn psn = psnManager.getPsnById(id);
			if (psn.getType() == Psn.LOCAL_PSN_TYPE) {
				String path = psn.getLocalPath();
				//
				String realPath = this.getServletContext().getRealPath(path);
				//
				if (subdir != null) {
					if (extra != null) {
						if (extra.equals("updir")) {
							int pos = subdir.lastIndexOf("/");
							if (pos > -1) {
								subdir = subdir.substring(0, pos);
							}
							// System.out.println("subdir="+subdir);
						} else if (extra.equals("mkdir")) {
							String dirname = helper.getDecodeString("dirname",
									"UTF-8");
							if (dirname != null) {
								String destDirPath = realPath + "/" + subdir
										+ "/" + dirname;
								destDirPath = StringUtil
										.normalizePath(destDirPath);
								//
								File mkDir = new File(destDirPath);
								if (!mkDir.exists()) {
									mkDir.mkdir();
								}
							}
						}

					}
					realPath += File.separator + subdir;
				}
				//
				realPath = StringUtil.normalizePath(realPath);
				// System.out.println("realPath="+realPath);
				//
				File dir = new File(realPath);
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
							fileList.add(new FileItem(file.isDirectory(), name,
									type, icon));
						}
					} // end if files not null
					model.put("fileList", fileList);
					model.put("PATH", subdir);
					model.put("psnId", psnId);
					return mv;
				}
			}
		}
		return null;
	}

	public void setPsnAddViewName(String psnAddViewName) {
		this.psnAddViewName = psnAddViewName;
	}

	public void setPsnEditViewName(String psnEditViewName) {
		this.psnEditViewName = psnEditViewName;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setPsnViewName(String psnViewName) {
		this.psnViewName = psnViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

}
