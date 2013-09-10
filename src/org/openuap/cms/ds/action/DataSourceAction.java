/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.ds.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.orm.EntityEnvironment;
import org.openuap.base.orm.dialect.Dialect;
import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.ds.manager.DataSourceManager;
import org.openuap.cms.ds.model.DataSourceModel;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 数据源管理控制器
 * </p>
 * 
 * <p>
 * $Id: DataSourceAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DataSourceAction extends AdminAction {

	private String defaultScreensPath;

	private String defaultViewName;

	private String rsViewName;
	/** 测试结果模板. */
	private String testViewName;
	/** 数据源管理器. */
	private DataSourceManager dataSourceManager;

	public DataSourceAction() {
		initDefaultViewName();
	}

	/**
	 * 初始化缺省的视图属性定义
	 */
	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/ds/";
		defaultViewName = defaultScreensPath + "ds_list.htm";
		rsViewName = defaultScreensPath + "ds_result.htm";
		testViewName = defaultScreensPath + "ds_test.htm";
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		// 必须是管理员权限
		boolean isAdmin = SecurityUtil.getUserSession().isAdmin();
		if (!isAdmin) {
			throw new UnauthorizedException();
		}
		return super.beforePerform(request, response, helper, model);
	}

	/**
	 * 测试数据源配置
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doTest(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rsViewName);
		model.put("op", "test");
		long id = helper.getLong("id", 0L);
		if (id != 0) {
			try {
				//
				DataSourceModel dsm = dataSourceManager.getDataSourceById(id);
				if (dsm != null) {
					DriverManagerDataSource dmd = new DriverManagerDataSource();
					dmd.setDriverClassName(dsm.getDriverClassName());
					dmd.setUrl(dsm.getUrl());
					dmd.setUsername(dsm.getUserName());
					dmd.setPassword(dsm.getPassword());
					Connection conn = null;
					try {
						conn = dmd.getConnection();
						// Success Connection;
						Dialect dialect = EntityEnvironment.getDialect(conn);
						if (dialect != null) {
							model.put("dialect", dialect);
							model.put("rs", "success");
							mv.setViewName(testViewName);
							return mv;
						} else {
							model.put("rs", "failed");
							model.put("msgs", "不能识别的数据库");
						}
					} catch (SQLException e) {
						e.printStackTrace();
						model.put("rs", "failed");
						model.put("msgs", e.getMessage());
					} finally {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					}

				}
			} catch (Exception e) {
				model.put("rs", "failed");
				model.put("msgs", e.getMessage());
				e.printStackTrace();
			}
		}
		return mv;
	}

	/**
	 * 数据源列表动作
	 */
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
		List<DataSourceModel> dses = dataSourceManager.getDataSourceModels(qi,
				pb);

		model.put("dses", dses);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("order_mode", order_mode);
		model.put("order_name", order_name);
		model.put("where", where);
		model.put("pb", pb);
		return mv;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setRsViewName(String rsViewName) {
		this.rsViewName = rsViewName;
	}

	public void setTestViewName(String testViewName) {
		this.testViewName = testViewName;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

}
