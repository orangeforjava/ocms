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
package org.openuap.cms.schedule.action;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.resource.ConstantLoader;
import org.openuap.cms.schedule.ScheduleService;
import org.openuap.cms.schedule.ScheduledJob;
import org.openuap.cms.schedule.manager.ScheduleManager;
import org.openuap.cms.schedule.model.Schedule;
import org.openuap.runtime.setup.BootstrapUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Description:计划任务管理控制器.
 * </p>
 * 
 * <p>
 * $Id: ScheduleAction.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class ScheduleAction extends ScheduleBaseAction {

	private ScheduleManager scheduleManager;

	private ScheduleService scheduleService;

	private String listViewName;

	private String showPropertiesViewName;

	private String jobQueueViewName;

	private String defaultScreensPath;

	private String showHelpViewName;
	
	private String rsViewName;
	
	

	public ScheduleAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/schedule/";
		listViewName = defaultScreensPath + "list.html";
		showPropertiesViewName = defaultScreensPath + "show_properties.html";
		jobQueueViewName = defaultScreensPath + "show_job_queue.html";
		showHelpViewName = defaultScreensPath + "show_help.html";
		rsViewName=defaultScreensPath + "schedule_operation_result.html";
		host = BootstrapUtils.getBootstrapManager("base").getApplicationConfig().getString(
				"sys.host.id", "default");
	}
	/**
	 * 显示本机的任务
	 */
	public ModelAndView perform(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		String showHost=helper.getString("host",host);
		model.put("tasks", scheduleManager.getTasksByHost(showHost));
		return new ModelAndView(listViewName, model);
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doShowAllTasks(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		model.put("tasks", scheduleManager.getAllTasks());
		return new ModelAndView(listViewName, model);
	}
	/**
	 * 保存计划任务定义
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws 
	 */
	public ModelAndView doSaveTask(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		int id = helper.getInt("id");
		Schedule schedule = scheduleManager.findById(id);
		if (schedule == null) {
			return errorPage(request, response, helper, "schedule_not_exist", model);
		}
		helper.setProperty(schedule);
		if (helper.getString("Host") == null) {
			schedule.setHost("NULL");
		}
		scheduleManager.saveSchedule(schedule);
		return perform(request, response, helper, model);
	}

	/**
	 * 添加新任务
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
	public ModelAndView doAddTask(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		Schedule schedule = new Schedule();
		schedule.setHost(host);
		schedule.setDayOfMonth(-1);
		schedule.setMinute(0);
		schedule.setHour(1);
		schedule.setWeekDay(-1);
		// Oracle empty String
		schedule.setTask(" ");
		scheduleManager.addSchedule(schedule);
		return perform(request, response, helper, model);
	}
	/**
	 * 删除任务
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doRemoveTask(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		int id = helper.getInt("id");
		Schedule schedule = scheduleManager.findById(id);
		if (schedule == null) {
			return errorPage(request, response, helper, "schedule_not_exist", model);
		}
		scheduleManager.deleteSchedule(schedule);
		return perform(request, response, helper, model);
	}
	/**
	 * 重新启动计划任务服务
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doRestart(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) throws Exception {
		scheduleService.destroy();
		scheduleService.init();
		return doShowJobQueue(request, response, helper, model);
	}

	/**
	 * 显示任务属性
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
	public ModelAndView doShowTaskProperties(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		int id = helper.getInt("id");
		Schedule schedule = scheduleManager.findById(id);
		if (schedule == null) {
			return errorPage(request, response, helper, "schedule_not_exist", model);
		}
		model.put("task", schedule);
		model.put("properties", StringUtil.str2hash(schedule.getProperty()));
		return new ModelAndView(showPropertiesViewName, model);
	}

	/**
	 * 执行任务
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
	public ModelAndView doExecuteTask(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		ModelAndView mv=new ModelAndView(rsViewName);
		int id = helper.getInt("id");
		Schedule schedule = null;
		String className = null;
		String rs="success";
		if (id > 0) {
			schedule = scheduleManager.findById(id);
			if (schedule == null) {
				rs="failed";
				model.put("rs", rs);
				model.put("msgs","schedule_not_exist");
				return mv;
			}
			className = schedule.getTask();
		} else {
			className = helper.getString("class");
		}

		try {
			Class task = this.getClass().forName(className);
			ScheduledJob job = (ScheduledJob) task.newInstance();
			job.run(schedule.toJobEntry());
			model.put("rs", rs);
		} catch (ClassNotFoundException e1) {
			rs="failed";
			model.put("rs", rs);
			model.put("msgs","Class " + className
					+ " is not found ! Please check the task name again.");
			return mv;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Can't execute task", ex);
			rs="failed";
			model.put("rs", rs);
			model.put("msgs",ex.getMessage());
			return mv;			
		}
		return mv;
	}

	/**
	 * 保存任务属性
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
	public ModelAndView doSaveTaskProperty(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws Exception {
		int id = helper.getInt("id");
		String key = helper.getString("key");
		String value = helper.getString("value");
		if (key.length() > 0 && value.length() > 0) {
			Schedule schedule = scheduleManager.findById(id);
			Hashtable hash = StringUtil.str2hash(schedule.getProperty());
			hash.put(key, value);
			schedule.setProperty(StringUtil.hash2str(hash));
			scheduleManager.saveSchedule(schedule);
		}
		return doShowTaskProperties(request, response, helper, model);
	}

	/**
	 * 保存所有任务属性
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
	public ModelAndView doSaveAllTaskProperties(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws Exception {
		String str = helper.getString("value", "");
		int id = helper.getInt("id");
		Schedule schedule = scheduleManager.findById(id);
		Hashtable hash = StringUtil.str2hash(schedule.getProperty());
		StringTokenizer st = new StringTokenizer(str, "&");
		String key, value;
		while (st.hasMoreTokens()) {
			String pairs = st.nextToken();
			int pos = pairs.indexOf("=");
			if (pos < 1) {
				continue;
			}
			key = pairs.substring(0, pos);
			value = pairs.substring(pos + 1, pairs.length());
			hash.put(key, value);
		}
		schedule.setProperty(StringUtil.hash2str(hash));
		scheduleManager.saveSchedule(schedule);
		return doShowTaskProperties(request, response, helper, model);
	}

	/**
	 * 删除任务属性
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
	public ModelAndView doRemoveTaskProperty(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) throws Exception {
		int id = helper.getInt("id");
		String key = helper.getString("key");
		if (key.length() > 0) {
			Schedule schedule = scheduleManager.findById(id);
			Hashtable hash = StringUtil.str2hash(schedule.getProperty());
			hash.remove(key);
			schedule.setProperty(StringUtil.hash2str(hash));
			scheduleManager.saveSchedule(schedule);
		}
		return doShowTaskProperties(request, response, helper, model);
	}

	/**
	 * 显示任务序列
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
	public ModelAndView doShowJobQueue(HttpServletRequest request, HttpServletResponse response,
			ControllerHelper helper, Map model) {
		model.put("jobs", scheduleService.getScheduleQueue().list());
		return new ModelAndView(this.jobQueueViewName, model);
	}

	/**
	 * 显示计划任务帮助
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
	public ModelAndView doShowHelp(HttpServletRequest request, HttpServletResponse response, ControllerHelper helper,
			Map model) {
		int id = helper.getInt("id");
		Schedule schedule = scheduleManager.findById(id);
		if (schedule == null) {
			return errorPage(request, response, helper, "schedule_not_exist", model);
		}
		model.put("schedule", schedule);
		String help = ConstantLoader.load("TaskHelp.xml").getString(schedule.getTask());
		if (help != null) {
			model.put("help", help);
			ConstantLoader.unload("TaskHelp.xml");
		}

		helper.setLayout("layouts/none.html");
		return new ModelAndView(showHelpViewName, model);
	}

	public void setShowPropertiesViewName(String showPropertiesViewName) {
		this.showPropertiesViewName = showPropertiesViewName;
	}

	public void setShowHelpViewName(String showHelpViewName) {
		this.showHelpViewName = showHelpViewName;
	}

	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public void setScheduleManager(ScheduleManager scheduleManager) {
		this.scheduleManager = scheduleManager;
	}

	public void setListViewName(String listViewName) {
		this.listViewName = listViewName;
	}

	public void setJobQueueViewName(String jobQueueViewName) {
		this.jobQueueViewName = jobQueueViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

}
