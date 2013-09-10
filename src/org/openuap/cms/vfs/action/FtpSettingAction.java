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
package org.openuap.cms.vfs.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.vfs.manager.FtpSettingManager;
import org.openuap.cms.vfs.model.FtpSetting;
import org.openuap.cms.vfs.service.FtpService;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

import com.enterprisedt.net.ftp.FTPClientInterface;
import com.enterprisedt.net.ftp.FTPFile;

/**
 * <p>
 * FTP设置控制器.
 * </p>
 * <p>
 * $Id: FtpSettingAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class FtpSettingAction extends AdminAction {

	private String defaultScreensPath;

	private String defaultViewName;

	private String rsViewName;
	
	private String testViewName;

	private FtpSettingManager ftpSettingManager;

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setRsViewName(String rsViewName) {
		this.rsViewName = rsViewName;
	}

	public void setFtpSettingManager(FtpSettingManager ftpSettingManager) {
		this.ftpSettingManager = ftpSettingManager;
	}

	public void setTestViewName(String testViewName) {
		this.testViewName = testViewName;
	}

	public FtpSettingAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/vfs/";
		defaultViewName = defaultScreensPath + "ftpsetting_list.html";
		rsViewName = defaultScreensPath + "ftpsetting_result.html";
		testViewName=defaultScreensPath + "ftpsetting_test.html";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		return super.beforePerform(request, response, helper, model);
	}

	public ModelAndView doTest(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rsViewName);
		model.put("op", "test");
		long id = helper.getLong("id", 0L);
		if (id != 0) {
			try {
				FTPClientInterface ftp = FtpService.getFtp(id);
				if (ftp!=null) {
					
					FTPFile[] files = ftp.dirDetails(".");
					ftp.quit();
					model.put("files",files);
					model.put("rs", "success");
					mv.setViewName(testViewName);
					
				} else {
					model.put("rs", "failed");
					model.put("msgs", "连接失败");
				}
			} catch (Exception e) {
				model.put("rs", "failed");
				model.put("msgs", e.getMessage());
				e.printStackTrace();
			}
		}
		return mv;
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		String order_mode = request.getParameter("order_mode");
		String order_name = request.getParameter("order_name");
		String where = request.getParameter("where");

		if (where == null) {
			where = "";
		}
		if (order == null) {
			order = "";
		}
		if (order_mode == null) {
			order_mode = "";
		}
		if (order_name == null) {
			order_name = "";
		}
		order_name = order_name.replaceAll("\\^", "");
		String final_order = "";
		if (!order.equals("") && !order_mode.equals("")) {
			final_order = order + " " + order_mode;
		}

		String orderBy = "";
		List argList = new ArrayList();
		Integer start = new Integer(0);
		Integer limit = new Integer(15);
		Object[] args = null;

		if (pageNum != null) {
			limit = new Integer(pageNum);
		} else {
			pageNum = "15";
		}
		if (page != null) {
			start = new Integer((Integer.parseInt(page) - 1) * limit.intValue());
		} else {
			page = "1";
		}
		if (order != null) {
			orderBy = final_order;
		}
		if (argList.size() > 0) {
			args = argList.toArray();
		}
		PageBuilder pb = new PageBuilder(limit.intValue());
		pb.page(Integer.parseInt(page));
		QueryInfo qi = new QueryInfo(where, final_order, limit, start);
		List<FtpSetting> ftps = ftpSettingManager.getFtpSettings(qi, pb);

		model.put("ftps", ftps);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("order_mode", order_mode);
		model.put("order_name", order_name);
		model.put("where", where);
		model.put("pb", pb);
		return mv;
	}
}
