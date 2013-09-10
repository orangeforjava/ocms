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
package org.openuap.cms.user.action.admin;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.cms.user.action.UserBaseAction;
import org.openuap.cms.user.manager.IPermissionManager;
import org.openuap.cms.user.manager.IRoleManager;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.IRole;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.model.PermissionObject;
import org.openuap.cms.user.model.PermissionObjectType;
import org.openuap.cms.user.model.Permissions;
import org.openuap.cms.user.security.AuthUser;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.user.security.manager.PermissionPluginManager;
import org.openuap.cms.user.security.manager.PermissionResourceItem;
import org.openuap.cms.user.security.manager.PermissionResourceType;
import org.openuap.cms.user.security.manager.PluginPermissionControlPanel;
import org.openuap.cms.user.security.manager.ResourcePermissionControlPanel;
import org.openuap.cms.user.security.permissions.UserPermissionConstant;
import org.openuap.cms.user.security.resource.PermissionDataLoader;
import org.openuap.cms.user.ui.PermissionConstant;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 用户管理控制器.
 * </p>
 * 
 * <p>
 * $Id: UserAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserAction extends AdminAction {

	private String defaultViewName;

	private String defaultScreensPath;

	private String userHeaderViewName;

	private String userListViewName;

	//
	private String roleListViewName;

	//
	private String userPermissionViewName;

	private String userPermissionViewName2;

	private String userSelfPermissionViewName;

	private String operationViewName;

	private String rolePermissionViewName;
	private String rolePermissionViewName2;
	//
	private String selRoleFramesetViewName;

	private String selRoleHeaderViewName;

	private String selRoleListViewName;

	private String userRolesViewName;

	private String roleUsersViewName;

	//
	private String selUserFramesetViewName;

	private String selUserHeaderViewName;

	private String selUserListViewName;

	//
	private IUserManager baseUserManager;

	private IRoleManager baseRoleManager;

	private IPermissionManager permissionManager;

	//
	private PermissionPluginManager permissionPluginManager;

	/**
	 * 
	 */
	public UserAction() {
		initDefaultViewName();
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		model.put("userPC", new UserPermissionConstant());
		return super.beforePerform(request, response, helper, model);
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/user/";
		defaultViewName = defaultScreensPath + "user_frameset.html";
		userHeaderViewName = defaultScreensPath + "user_header.html";
		userListViewName = defaultScreensPath + "user_list.html";
		userPermissionViewName = defaultScreensPath + "user_permission.html";
		userSelfPermissionViewName = defaultScreensPath
				+ "user_self_permission.html";
		operationViewName = defaultScreensPath
				+ "permission_operation_result.html";
		roleListViewName = defaultScreensPath + "role_list.html";
		rolePermissionViewName = defaultScreensPath + "role_permission.html";
		//
		selRoleFramesetViewName = defaultScreensPath
				+ "role_select_frameset.html";
		selRoleHeaderViewName = defaultScreensPath + "role_select_header.html";
		selRoleListViewName = defaultScreensPath + "role_select_list.html";
		userRolesViewName = defaultScreensPath + "user_roles.html";
		roleUsersViewName = defaultScreensPath + "role_users.html";
		selUserFramesetViewName = defaultScreensPath
				+ "user_select_frameset.html";
		selUserHeaderViewName = defaultScreensPath + "user_select_header.html";
		selUserListViewName = defaultScreensPath + "user_select_list.html";

		userPermissionViewName2 = defaultScreensPath + "user_permission2.html";
		rolePermissionViewName2 = defaultScreensPath + "role_permission2.html";
	}

	/**
	 * show the user manage frameset window
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
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(defaultViewName, model);
		return mv;
	}

	/**
	 * show the user header window
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
	public ModelAndView doUserHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(userHeaderViewName, model);
		return mv;
	}

	/**
	 * 用户列表
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doUserList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		// you can check permission in controller.
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.ViewUser)) {
			throw new UnauthorizedException();
		}
		//
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		String status=helper.getString("status", "0");
		//
		Integer start = new Integer(0);
		Integer limit = new Integer(15);
		//
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
		
		ModelAndView mv = new ModelAndView(userListViewName, model);
		PageBuilder pb = new PageBuilder(limit.intValue());
		pb.page(Integer.parseInt(page));
		String where = " type<" + IUser.MEMBER_TYPE;
		where+=" and status="+status;
		QueryInfo qi = new QueryInfo(where, order,
				limit, start);
		List users = baseUserManager.getUsers(qi, pb);
		//
		pb.page(Integer.parseInt(page));
		model.put("users", users);
		model.put("status", status);
		model.put("pb", pb);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("action", this);
		model.put("roleManager", baseRoleManager);
		return mv;
	}

	/**
	 * 
	 * 角色列表
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
	public ModelAndView doRoleList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.ViewRole)) {
			throw new UnauthorizedException();
		}

		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		//
		Long start = new Long(0);
		Long limit = new Long(15);
		//
		if (pageNum != null) {
			limit = new Long(pageNum);
		} else {
			pageNum = "15";
		}
		if (page != null) {
			start = new Long((Long.parseLong(page) - 1) * limit.longValue());
		} else {
			page = "1";
		}

		ModelAndView mv = new ModelAndView(roleListViewName, model);

		PageBuilder pb = new PageBuilder(limit.intValue());

		List roles = baseRoleManager.getRoles(start, limit, null, order, pb);
		//
		pb.page(Integer.parseInt(page));
		model.put("roles", roles);
		model.put("pb", pb);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("action", this);
		model.put("roleManager", baseRoleManager);
		return mv;
	}

	/**
	 * 查看用户最终权限
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
	public ModelAndView doViewUserPermission(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		// String userId = request.getParameter("userId");
		// if (userId != null) {
		// Long uid = new Long(userId);
		// IUser user = baseUserManager.getUserById(uid);
		// model.put("user", user);
		// AuthUser authUser = new AuthUser(user, null);
		// model.put("authUser", authUser);
		// //
		// List roles = baseRoleManager.getUserRoles(uid);
		// model.put("roles", roles);
		// //
		// PermissionDataLoader.PermissionTypeData ptd = PermissionDataLoader
		// .loadType("/dd/permission_type.xml");
		// Iterator ptIt = ptd.values().iterator();
		// boolean admin = user.getType() == IUser.ADMIN_TYPE ? true : false;
		// //
		// List pList = new ArrayList();
		//
		// Map pMap = new HashMap();
		// Map pObjMap = new HashMap();
		// //
		// while (ptIt.hasNext()) {
		// // every permission type
		// PermissionObjectType pot = (PermissionObjectType) ptIt.next();
		// pList.add(pot);
		// String name = pot.getName();
		// // the permission_type permissions
		// Permissions permissions = permissionManager
		// .getUserFinalPermissions(uid, pot.getObjectType(), "-1");
		// // the every permission type definition file
		// PermissionDataLoader.PermissionData pd = PermissionDataLoader
		// .load("/dd/" + name + "_permission.xml");
		// List p_dd = new ArrayList();
		// Iterator keyIt = pd.keySet().iterator();
		// // get the concrete object permission
		// Map pObject_dd = permissionManager.getUserFinalObjPermissions(
		// uid, pot.getObjectType());
		// //
		// Map poc_map = new HashMap();
		// if (pObject_dd != null && pObject_dd.size() > 0) {
		// Iterator po_dd_it = pObject_dd.keySet().iterator();
		// while (po_dd_it.hasNext()) {
		// PermissionObject pobj = (PermissionObject) po_dd_it
		// .next();
		// Permissions mypermissions = (Permissions) pObject_dd
		// .get(pobj);
		// List po_p_list = new ArrayList();
		// Iterator mykeyIt = pd.keySet().iterator();
		// while (mykeyIt.hasNext()) {
		// // the permission value
		// Long key = (Long) mykeyIt.next();
		// // the permission title
		// Object value = pd.get(key);
		// PermissionConstant pc = null;
		// // decide if has the every permission
		//
		// if (mypermissions != null
		// && mypermissions.hasPermission(pobj
		// .getObjectType(), pobj
		// .getObjectId(), key.longValue())) {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, true);
		// } else {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, false);
		// }
		// po_p_list.add(pc);
		// }
		// //
		// poc_map.put(pobj.getObjectId(), po_p_list);
		// }
		// }
		// //
		// pObjMap.put(name, poc_map);
		// //
		// while (keyIt.hasNext()) {
		// // the permission value
		// Long key = (Long) keyIt.next();
		// // the permission title
		// Object value = pd.get(key);
		// PermissionConstant pc = null;
		// // decide if has the every permission
		// if (admin) {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, true);
		// } else {
		// if (permissions != null
		// && permissions
		// .hasPermission(pot.getObjectType(),
		// "-1", key.longValue())) {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, true);
		// } else {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, false);
		// }
		// }
		// p_dd.add(pc);
		// }
		// pMap.put(name, p_dd);
		// }
		// model.put("ptdList", pList);
		// model.put("pMap", pMap);
		// model.put("pObjMap", pObjMap);
		// model.put("pm", permissionManager);
		// model.put("action", this);
		// ModelAndView mv = new ModelAndView(userPermissionViewName, model);
		// return mv;
		// }
		// return null;
		return doViewUserPermission2(request, response, helper, model);
	}

	/**
	 * 查看用户最终权限改进版
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doViewUserPermission2(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(userPermissionViewName2);
		String userId = request.getParameter("userId");
		if (userId != null) {
			Long uid = new Long(userId);
			IUser user = baseUserManager.getUserById(uid);
			model.put("user", user);
			//
			AuthUser authUser = new AuthUser(user, null);
			model.put("authUser", authUser);
			List roles = baseRoleManager.getUserRoles(uid);
			model.put("roles", roles);
			// 从插件中装载权限类型
			List<PluginPermissionControlPanel> ppcps = permissionPluginManager
					.getPluginPermissionControlPanels();
			boolean admin = user.getType() == IUser.ADMIN_TYPE ? true : false;
			Map pMap = new HashMap();
			Map pObjMap = new HashMap();
			// 权限类型列表
			List pList = new ArrayList();
			//
			for (PluginPermissionControlPanel ppcp : ppcps) {
				// 每个插件权限控制面板
				List<ResourcePermissionControlPanel> rpcps = ppcp
						.getResourcePermissionControlPanels();
				for (ResourcePermissionControlPanel rpcp : rpcps) {
					List p_dd = new ArrayList();

					// 每个资源权限控制面板
					PermissionResourceType prt = rpcp
							.getPermissionResourceType();
					pList.add(prt);
					//
					// 获得用户对此类对象抽象权限
					Permissions permissions = permissionManager
							.getUserFinalPermissions(uid, prt.getKey(), "-1");
					//
					List<PermissionResourceItem> prItems = rpcp
							.getPermissionResourceItems();
					// 获得用户对此类对象的具体权限
					Map pObject_dd = permissionManager
							.getUserFinalObjPermissions(uid, prt.getKey());
					Map poc_map = new HashMap();
					if (pObject_dd != null && pObject_dd.size() > 0) {
						Iterator po_dd_it = pObject_dd.keySet().iterator();
						while (po_dd_it.hasNext()) {
							PermissionObject pobj = (PermissionObject) po_dd_it
									.next();
							Permissions mypermissions = (Permissions) pObject_dd
									.get(pobj);
							List po_p_list = new ArrayList();
							// 所有的权限定义项
							for (PermissionResourceItem prItem : prItems) {
								String key = prItem.getKey();
								String title = prItem.getTitle();
								PermissionConstant pc = null;
								if (mypermissions != null
										&& mypermissions.hasPermission(pobj
												.getObjectType(), pobj
												.getObjectId(), new Long(key))) {
									pc = new PermissionConstant(new Long(key),
											title, true);
								} else {
									pc = new PermissionConstant(new Long(key),
											title, false);
								}
								po_p_list.add(pc);
							}
							//
							poc_map.put(pobj.getObjectId(), po_p_list);
						}

					}
					//
					// 指定对象类型的权限
					pObjMap.put(prt.getName(), poc_map);
					for (PermissionResourceItem prItem : prItems) {
						Long key = new Long(prItem.getKey());
						// the permission title
						String title = prItem.getTitle();
						PermissionConstant pc = null;
						// decide if has the every permission
						if (admin) {
							pc = new PermissionConstant(key.longValue(), title,
									true);
						} else {
							if (permissions != null
									&& permissions.hasPermission(prt.getKey(),
											"-1", key.longValue())) {
								pc = new PermissionConstant(key.longValue(),
										title, true);
							} else {
								pc = new PermissionConstant(key.longValue(),
										title, false);
							}
						}
						p_dd.add(pc);
					}
					pMap.put(prt.getName(), p_dd);
				}
			}
			// 权限类型列表
			model.put("ptdList", pList);
			model.put("pMap", pMap);
			model.put("pObjMap", pObjMap);
			model.put("pm", permissionManager);
			model.put("action", this);
			return mv;
		}
		return null;
	}

	/**
	 * 查看用户最终权限
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
	public ModelAndView doUserSelfPermission(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		// ModelAndView mv = new ModelAndView(userSelfPermissionViewName,
		// model);
		// String userId = request.getParameter("userId");
		// if (userId != null) {
		// Long uid = new Long(userId);
		// IUser user = baseUserManager.getUserById(uid);
		// model.put("user", user);
		// AuthUser authUser = new AuthUser(user, null);
		// model.put("authUser", authUser);
		// // 装载权限类型
		// PermissionDataLoader.PermissionTypeData ptd = PermissionDataLoader
		// .loadType("/dd/permission_type.xml");
		// Iterator ptIt = ptd.values().iterator();
		// boolean admin = user.getType() == IUser.ADMIN_TYPE ? true : false;
		// //
		// List pList = new ArrayList();
		//
		// Map pMap = new HashMap();
		// Map pObjMap = new HashMap();
		// // 处理每类权限
		// while (ptIt.hasNext()) {
		// // every permission type
		// PermissionObjectType pot = (PermissionObjectType) ptIt.next();
		// pList.add(pot);
		// String name = pot.getName();
		// // 获得用户对此类对象抽象权限
		// Permissions permissions = permissionManager.getUserPermissions(
		// uid, pot.getObjectType(), "-1");
		// // the every permission type definition file
		// PermissionDataLoader.PermissionData pd = PermissionDataLoader
		// .load("/dd/" + name + "_permission.xml");
		// List p_dd = new ArrayList();
		// Iterator keyIt = pd.keySet().iterator();
		// // 获得用户对此类对象的具体权限
		// Map pObject_dd = permissionManager.getUserObjPermissions(uid,
		// pot.getObjectType());
		// //
		// Map poc_map = new HashMap();
		// if (pObject_dd != null && pObject_dd.size() > 0) {
		// Iterator po_dd_it = pObject_dd.keySet().iterator();
		// while (po_dd_it.hasNext()) {
		// PermissionObject pobj = (PermissionObject) po_dd_it
		// .next();
		// Permissions mypermissions = (Permissions) pObject_dd
		// .get(pobj);
		// List po_p_list = new ArrayList();
		// // 所有的权限定义项
		// Iterator mykeyIt = pd.keySet().iterator();
		// while (mykeyIt.hasNext()) {
		// // the permission value
		// Long key = (Long) mykeyIt.next();
		// // the permission title
		// Object value = pd.get(key);
		// PermissionConstant pc = null;
		// // decide if has the every permission
		//
		// if (mypermissions != null
		// && mypermissions.hasPermission(pobj
		// .getObjectType(), pobj
		// .getObjectId(), key.longValue())) {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, true);
		// } else {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, false);
		// }
		// po_p_list.add(pc);
		// }
		// //
		// poc_map.put(pobj.getObjectId(), po_p_list);
		// }
		// }
		// // 指定对象类型的权限
		// pObjMap.put(name, poc_map);
		// //
		// while (keyIt.hasNext()) {
		// // the permission value
		// Long key = (Long) keyIt.next();
		// // the permission title
		// Object value = pd.get(key);
		// PermissionConstant pc = null;
		// // decide if has the every permission
		// if (admin) {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, true);
		// } else {
		// if (permissions != null
		// && permissions
		// .hasPermission(pot.getObjectType(),
		// "-1", key.longValue())) {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, true);
		// } else {
		// pc = new PermissionConstant(key.longValue(),
		// (String) value, false);
		// }
		// }
		// p_dd.add(pc);
		// }
		// pMap.put(name, p_dd);
		// }
		// // 权限类型列表
		// model.put("ptdList", pList);
		// model.put("pMap", pMap);
		// model.put("pObjMap", pObjMap);
		// model.put("pm", permissionManager);
		// model.put("action", this);
		// return mv;
		// }
		// return null;
		return doUserSelfPermission2(request, response, helper, model);
	}

	/**
	 * 插件方式的用户自身权限
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doUserSelfPermission2(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(userSelfPermissionViewName, model);
		String userId = request.getParameter("userId");
		if (userId != null) {
			Long uid = new Long(userId);
			IUser user = baseUserManager.getUserById(uid);
			model.put("user", user);
			AuthUser authUser = new AuthUser(user, null);
			model.put("authUser", authUser);
			// 从插件中装载权限类型
			List<PluginPermissionControlPanel> ppcps = permissionPluginManager
					.getPluginPermissionControlPanels();
			boolean admin = user.getType() == IUser.ADMIN_TYPE ? true : false;
			Map pMap = new HashMap();
			Map pObjMap = new HashMap();
			// 权限类型列表
			List pList = new ArrayList();
			//
			for (PluginPermissionControlPanel ppcp : ppcps) {
				// 每个插件权限控制面板
				List<ResourcePermissionControlPanel> rpcps = ppcp
						.getResourcePermissionControlPanels();
				for (ResourcePermissionControlPanel rpcp : rpcps) {
					List p_dd = new ArrayList();

					// 每个资源权限控制面板
					PermissionResourceType prt = rpcp
							.getPermissionResourceType();
					pList.add(prt);
					//
					// 获得用户对此类对象抽象权限
					Permissions permissions = permissionManager
							.getUserPermissions(uid, prt.getKey(), "-1");
					//
					List<PermissionResourceItem> prItems = rpcp
							.getPermissionResourceItems();
					// 获得用户对此类对象的具体权限
					Map pObject_dd = permissionManager.getUserObjPermissions(
							uid, prt.getKey());
					Map poc_map = new HashMap();
					if (pObject_dd != null && pObject_dd.size() > 0) {
						Iterator po_dd_it = pObject_dd.keySet().iterator();
						while (po_dd_it.hasNext()) {
							PermissionObject pobj = (PermissionObject) po_dd_it
									.next();
							Permissions mypermissions = (Permissions) pObject_dd
									.get(pobj);
							List po_p_list = new ArrayList();
							// 所有的权限定义项
							for (PermissionResourceItem prItem : prItems) {
								String key = prItem.getKey();
								String title = prItem.getTitle();
								PermissionConstant pc = null;
								if (mypermissions != null
										&& mypermissions.hasPermission(pobj
												.getObjectType(), pobj
												.getObjectId(), new Long(key))) {
									pc = new PermissionConstant(new Long(key),
											title, true);
								} else {
									pc = new PermissionConstant(new Long(key),
											title, false);
								}
								po_p_list.add(pc);
							}
							//
							poc_map.put(pobj.getObjectId(), po_p_list);
						}

					}
					//
					// 指定对象类型的权限
					pObjMap.put(prt.getName(), poc_map);
					for (PermissionResourceItem prItem : prItems) {
						Long key = new Long(prItem.getKey());
						// the permission title
						String title = prItem.getTitle();
						PermissionConstant pc = null;
						// decide if has the every permission
						if (admin) {
							pc = new PermissionConstant(key.longValue(), title,
									true);
						} else {
							if (permissions != null
									&& permissions.hasPermission(prt.getKey(),
											"-1", key.longValue())) {
								pc = new PermissionConstant(key.longValue(),
										title, true);
							} else {
								pc = new PermissionConstant(key.longValue(),
										title, false);
							}
						}
						p_dd.add(pc);
					}
					pMap.put(prt.getName(), p_dd);
				}
			}
			// 权限类型列表
			model.put("ptdList", pList);
			model.put("pMap", pMap);
			model.put("pObjMap", pObjMap);
			model.put("pm", permissionManager);
			model.put("action", this);
			return mv;
		}
		return null;
	}

	/**
	 * show the role permission
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
	public ModelAndView doViewRolePermission(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(rolePermissionViewName, model);
		String roleId = request.getParameter("roleId");
		if (roleId != null) {
			Long rid = new Long(roleId);
			IRole role = baseRoleManager.getRoleById(rid);
			model.put("role", role);
			//
			//
			PermissionDataLoader.PermissionTypeData ptd = PermissionDataLoader
					.loadType("/dd/permission_type.xml");
			Iterator ptIt = ptd.values().iterator();
			//
			List pList = new ArrayList();

			Map pMap = new HashMap();
			Map pObjMap = new HashMap();

			while (ptIt.hasNext()) {
				// every permission type
				PermissionObjectType pot = (PermissionObjectType) ptIt.next();
				pList.add(pot);
				String name = pot.getName();
				// the permission_type permissions
				Permissions permissions = permissionManager.getRolePermissions(
						rid, pot.getObjectType(), "-1");
				// the every permission type definition file
				PermissionDataLoader.PermissionData pd = PermissionDataLoader
						.load("/dd/" + name + "_permission.xml");
				List p_dd = new ArrayList();
				Iterator keyIt = pd.keySet().iterator();
				// get the concrete object permission
				Map pObject_dd = permissionManager.getRoleObjPermissions(rid,
						pot.getObjectType());
				//
				Map poc_map = new HashMap();
				if (pObject_dd != null && pObject_dd.size() > 0) {
					Iterator po_dd_it = pObject_dd.keySet().iterator();
					while (po_dd_it.hasNext()) {
						PermissionObject pobj = (PermissionObject) po_dd_it
								.next();
						Permissions mypermissions = (Permissions) pObject_dd
								.get(pobj);
						List po_p_list = new ArrayList();
						Iterator mykeyIt = pd.keySet().iterator();
						while (mykeyIt.hasNext()) {
							// the permission value
							Long key = (Long) mykeyIt.next();
							// the permission title
							Object value = pd.get(key);
							PermissionConstant pc = null;
							// decide if has the every permission

							if (mypermissions != null
									&& mypermissions.hasPermission(pobj
											.getObjectType(), pobj
											.getObjectId(), key.longValue())) {
								pc = new PermissionConstant(key.longValue(),
										(String) value, true);
							} else {
								pc = new PermissionConstant(key.longValue(),
										(String) value, false);
							}
							po_p_list.add(pc);
						}
						//
						poc_map.put(pobj.getObjectId(), po_p_list);
					}
				}
				//
				pObjMap.put(name, poc_map);
				//
				while (keyIt.hasNext()) {
					// the permission value
					Long key = (Long) keyIt.next();
					// the permission title
					Object value = pd.get(key);
					PermissionConstant pc = null;
					// decide if has the every permission
					if (permissions != null
							&& permissions.hasPermission(pot.getObjectType(),
									"-1", key.longValue())) {
						pc = new PermissionConstant(key.longValue(),
								(String) value, true);
					} else {
						pc = new PermissionConstant(key.longValue(),
								(String) value, false);
					}
					p_dd.add(pc);
				}
				pMap.put(name, p_dd);
			}
			model.put("ptdList", pList);
			model.put("pMap", pMap);
			model.put("pObjMap", pObjMap);
			model.put("pm", permissionManager);
			model.put("action", this);
			return mv;
		}
		return null;
	}

	/**
	 * 新角色权限查看/设置
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doViewRolePermission2(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(rolePermissionViewName2, model);
		String roleId = request.getParameter("roleId");
		if (roleId != null) {
			Long rid = new Long(roleId);
			IRole role = baseRoleManager.getRoleById(rid);
			model.put("role", role);
			// 从插件中装载权限类型
			List<PluginPermissionControlPanel> ppcps = permissionPluginManager
					.getPluginPermissionControlPanels();

			Map pMap = new HashMap();
			Map pObjMap = new HashMap();
			// 权限类型列表
			List pList = new ArrayList();
			//
			for (PluginPermissionControlPanel ppcp : ppcps) {
				// 每个插件权限控制面板
				List<ResourcePermissionControlPanel> rpcps = ppcp
						.getResourcePermissionControlPanels();
				for (ResourcePermissionControlPanel rpcp : rpcps) {
					List p_dd = new ArrayList();

					// 每个资源权限控制面板
					PermissionResourceType prt = rpcp
							.getPermissionResourceType();
					pList.add(prt);
					//
					// 获得用户对此类对象抽象权限
					Permissions permissions = permissionManager
							.getRolePermissions(rid, prt.getKey(), "-1");
					//
					List<PermissionResourceItem> prItems = rpcp
							.getPermissionResourceItems();
					// 获得用户对此类对象的具体权限
					Map pObject_dd = permissionManager.getRoleObjPermissions(
							rid, prt.getKey());
					Map poc_map = new HashMap();
					if (pObject_dd != null && pObject_dd.size() > 0) {
						Iterator po_dd_it = pObject_dd.keySet().iterator();
						while (po_dd_it.hasNext()) {
							PermissionObject pobj = (PermissionObject) po_dd_it
									.next();
							Permissions mypermissions = (Permissions) pObject_dd
									.get(pobj);
							List po_p_list = new ArrayList();
							// 所有的权限定义项
							for (PermissionResourceItem prItem : prItems) {
								String key = prItem.getKey();
								String title = prItem.getTitle();
								PermissionConstant pc = null;
								if (mypermissions != null
										&& mypermissions.hasPermission(pobj
												.getObjectType(), pobj
												.getObjectId(), new Long(key))) {
									pc = new PermissionConstant(new Long(key),
											title, true);
								} else {
									pc = new PermissionConstant(new Long(key),
											title, false);
								}
								po_p_list.add(pc);
							}
							//
							poc_map.put(pobj.getObjectId(), po_p_list);
						}

					}
					//
					// 指定对象类型的权限
					pObjMap.put(prt.getName(), poc_map);
					for (PermissionResourceItem prItem : prItems) {
						Long key = new Long(prItem.getKey());
						// the permission title
						String title = prItem.getTitle();
						PermissionConstant pc = null;
						// decide if has the every permission

						if (permissions != null
								&& permissions.hasPermission(prt.getKey(),
										"-1", key.longValue())) {
							pc = new PermissionConstant(key.longValue(), title,
									true);
						} else {
							pc = new PermissionConstant(key.longValue(), title,
									false);
						}
						p_dd.add(pc);
					}
					pMap.put(prt.getName(), p_dd);
				}
			}
			// 权限类型列表
			model.put("ptdList", pList);
			model.put("pMap", pMap);
			model.put("pObjMap", pObjMap);
			model.put("pm", permissionManager);
			model.put("action", this);
			return mv;
		}
		return null;
	}

	public ModelAndView doSetUserPermission(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		model.put("op", "setUserPerm");
		//
		String objectType = request.getParameter("objectType");
		String objectId = request.getParameter("objectId");
		String userId = request.getParameter("userId");
		String name = request.getParameter("name");
		String ref = request.getParameter("ref");
		String[] sperms = request.getParameterValues(name + "_p");
		long finalPerm = 0L;
		if (sperms != null) {
			for (int i = 0; i < sperms.length; i++) {
				String sperm = sperms[i];
				long perm = Long.parseLong(sperm);
				finalPerm |= perm;
			}
		}
		try {

			Long uid = new Long(userId);
			permissionManager.setUserPermission(uid, objectType, objectId,
					finalPerm);
			model.put("rs", "1");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("ex", ex);
			model.put("rs", "0");
		}
		model.put("ref", ref);
		return mv;
	}

	/**
	 * 
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
	public ModelAndView doSetRolePermission(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		model.put("op", "setRolePerm");
		//
		String objectType = request.getParameter("objectType");
		String objectId = request.getParameter("objectId");
		String roleId = request.getParameter("roleId");
		String name = request.getParameter("name");
		String ref = request.getParameter("ref");
		String[] sperms = request.getParameterValues(name + "_p");
		long finalPerm = 0L;
		if (sperms != null) {
			for (int i = 0; i < sperms.length; i++) {
				String sperm = sperms[i];
				long perm = Long.parseLong(sperm);
				finalPerm |= perm;
			}
		}
		try {
			Long rid = new Long(roleId);
			permissionManager.setRolePermission(rid, objectType, objectId,
					finalPerm);
			model.put("rs", "1");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("ex", ex);
			model.put("rs", "0");
		}
		model.put("ref", ref);
		return mv;
	}

	/**
	 * 
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
	public ModelAndView doSelRoleFrameset(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		ModelAndView mv = new ModelAndView(selRoleFramesetViewName, model);
		return mv;
	}

	/**
	 * 
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
	public ModelAndView doSelRoleHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(selRoleHeaderViewName, model);
		return mv;
	}

	/**
	 * 
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
	public ModelAndView doSelRoleList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(selRoleListViewName, model);
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		//
		Long start = new Long(0);
		Long limit = new Long(15);
		//
		if (pageNum != null) {
			limit = new Long(pageNum);
		} else {
			pageNum = "15";
		}
		if (page != null) {
			start = new Long((Long.parseLong(page) - 1) * limit.intValue());
		} else {
			page = "1";
		}
		PageBuilder pb = new PageBuilder(limit.intValue());
		List roles = baseRoleManager.getRoles(start, limit, null, order, pb);
		//
		pb.page(Integer.parseInt(page));
		model.put("roles", roles);
		model.put("pb", pb);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("action", this);
		return mv;
	}

	/**
	 * 
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
	public ModelAndView doSelUserFrameset(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		ModelAndView mv = new ModelAndView(selUserFramesetViewName, model);
		return mv;
	}

	/**
	 * 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 */
	public ModelAndView doSelUserHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(selUserHeaderViewName, model);
		return mv;
	}

	/**
	 * 
	 * 用户选择
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
	public ModelAndView doSelUserList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		//
		ModelAndView mv = new ModelAndView(selUserListViewName, model);
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		//
		Integer start = new Integer(0);
		Integer limit = new Integer(15);
		//
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
		PageBuilder pb = new PageBuilder(limit.intValue());
		QueryInfo qi = new QueryInfo(null, order, limit, start);
		String where=" (type="+IUser.SYS_USER_TYPE+" or type="+IUser.ADMIN_TYPE+")";
		where+=" and status="+IUser.NORMAL_STATUS;
		qi.setWhereClause(where);
		List users = baseUserManager.getUsers(qi, pb);
		//
		pb.page(Integer.parseInt(page));
		model.put("users", users);
		model.put("pb", pb);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("action", this);
		return mv;
	}

	/**
	 * add roles to user.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doAddRole(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.AddRole)) {
			throw new UnauthorizedException();
		}

		String roleIds = request.getParameter("roleIds");
		String userId = request.getParameter("userId");
		boolean success = true;
		if (roleIds != null) {
			try {
				String[] roleIdAry = roleIds.split(",");
				for (int i = 0; i < roleIdAry.length; i++) {
					Long rid = new Long(roleIdAry[i]);
					Long uid = new Long(userId);
					//
					baseRoleManager.saveUserRole(uid, rid);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		} else {
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	public ModelAndView doAddUserToRole(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.AddUserToRole)) {
			throw new UnauthorizedException();
		}
		//
		String roleId = request.getParameter("roleId");
		String userIds = request.getParameter("userIds");
		boolean success = true;
		if (userIds != null) {
			try {
				String[] userIdAry = userIds.split(",");
				for (int i = 0; i < userIdAry.length; i++) {
					Long uid = new Long(userIdAry[i]);
					Long rid = new Long(roleId);
					//
					baseRoleManager.saveUserRole(uid, rid);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		} else {
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
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
	public ModelAndView doViewUserRoles(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String userId = request.getParameter("userId");
		if (userId != null) {
			Long uid = new Long(userId);
			IUser user = baseUserManager.getUserById(uid);
			model.put("user", user);
			AuthUser authUser = new AuthUser(user, null);
			model.put("authUser", authUser);
			//
			List roles = baseRoleManager.getUserRoles(uid);
			model.put("roles", roles);
			model.put("action", this);
			ModelAndView mv = new ModelAndView(userRolesViewName, model);
			return mv;
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return ModelAndView
	 */
	public ModelAndView doViewRoleUsers(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String roleId = request.getParameter("roleId");
		if (roleId != null) {
			Long rid = new Long(roleId);
			IRole role = baseRoleManager.getRoleById(rid);
			model.put("role", role);
			//
			List users = baseRoleManager.getRoleUsers(rid);
			model.put("users", users);
			ModelAndView mv = new ModelAndView(roleUsersViewName, model);
			return mv;
		}
		return null;
	}

	/**
	 * 
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
	public ModelAndView doRemoveUserFromRole(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String roleId = request.getParameter("roleId");
		String userId = request.getParameter("userId");
		boolean success = true;
		if (roleId != null && userId != null) {
			try {
				Long rid = new Long(roleId);
				Long uid = new Long(userId);
				//
				baseRoleManager.deleteUserRole(uid, rid);

			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		} else {
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doRemoveRoleFromUser(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String roleId = request.getParameter("roleId");
		String userId = request.getParameter("userId");
		boolean success = true;
		if (roleId != null && userId != null) {
			try {
				Long rid = new Long(roleId);
				Long uid = new Long(userId);
				//
				baseRoleManager.deleteUserRole(uid, rid);

			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		} else {
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	public ModelAndView doAddUserObjectPerm(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.EditUserPermission)) {
			throw new UnauthorizedException();
		}

		boolean success = true;
		//
		String objectType = request.getParameter("objectType");
		String objectId = request.getParameter("objectId");
		String userId = request.getParameter("userId");
		try {
			if (userId != null && objectId != null && objectType != null) {
				Long uid = new Long(userId);
				long perm = 0;
				// 处理批量的具体对象选择
				String[] oids = objectId.split(",");
				for (String oid : oids) {
					permissionManager.setUserPermission(uid, objectType, oid,
							perm);
				}
			} else {
				success = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doAddRoleObjectPerm(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//
		if (!SecurityUtil.hasPermission(UserPermissionConstant.OBJECT_TYPE,
				"-1", UserPermissionConstant.ViewRolePermission)) {
			throw new UnauthorizedException();
		}

		boolean success = true;
		//
		String objectType = request.getParameter("objectType");
		String objectId = request.getParameter("objectId");
		String roleId = request.getParameter("roleId");
		try {
			if (roleId != null && objectId != null && objectType != null) {
				Long rid = new Long(roleId);
				long perm = 0;
				// 处理批量的具体对象选择
				String[] oids = objectId.split(",");
				for (String oid : oids) {
					permissionManager.setRolePermission(rid, objectType, oid,
							perm);
				}
			} else {
				success = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		PrintWriter writer = response.getWriter();
		if (success) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setUserHeaderViewName(String userHeaderViewName) {
		this.userHeaderViewName = userHeaderViewName;
	}

	public void setUserListViewName(String userListViewName) {
		this.userListViewName = userListViewName;
	}

	public void setBaseUserManager(IUserManager baseUserManager) {
		this.baseUserManager = baseUserManager;
	}

	public void setPermissionManager(IPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

	public void setBaseRoleManager(IRoleManager baseRoleManager) {
		this.baseRoleManager = baseRoleManager;
	}

	public void setUserPermissionViewName(String userPermissionViewName) {
		this.userPermissionViewName = userPermissionViewName;
	}

	public void setOperationViewName(String operationViewName) {
		this.operationViewName = operationViewName;
	}

	public void setUserSelfPermissionViewName(String userSelfPermissionViewName) {
		this.userSelfPermissionViewName = userSelfPermissionViewName;
	}

	public void setRoleListViewName(String roleListViewName) {
		this.roleListViewName = roleListViewName;
	}

	public void setRolePermissionViewName(String rolePermissionViewName) {
		this.rolePermissionViewName = rolePermissionViewName;
	}

	public void setSelRoleFramesetViewName(String selRoleFramesetViewName) {
		this.selRoleFramesetViewName = selRoleFramesetViewName;
	}

	public void setSelRoleListViewName(String selRoleListViewName) {
		this.selRoleListViewName = selRoleListViewName;
	}

	public PermissionPluginManager getPermissionPluginManager() {
		return permissionPluginManager;
	}

	public void setPermissionPluginManager(
			PermissionPluginManager permissionPluginManager) {
		this.permissionPluginManager = permissionPluginManager;
	}
}
