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
package org.openuap.cms.user.security.manager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openuap.cms.user.model.PermissionObjectType;
import org.openuap.cms.user.security.manager.support.DefaultResourcePermissionControlPanel;
import org.openuap.cms.user.security.resource.PluginPermissionDataLoader;
import org.openuap.cms.user.security.resource.PluginPermissionDataLoader.PermissionData;
import org.openuap.runtime.plugin.WebApplicationPlugin;

/**
 * <p>
 * 插件权限控制面板抽象类.
 * </p>
 * 
 * <p>
 * $Id: AbstractPluginPermissionControlPanel.java 3939 2010-10-27 08:41:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractPluginPermissionControlPanel implements
		PluginPermissionControlPanel {
	public List<ResourcePermissionControlPanel> getResourcePermissionControlPanels() {
		// 装载权限类型
		if (getPluginHome() != null) {
			String securityDirectory = getPluginHome() + "/resource/security";
			File securityDir = new File(securityDirectory);
			if (securityDir.exists() && securityDir.isDirectory()) {
				File permissionFiles[] = securityDir
						.listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return true;
							}
						});
				if (permissionFiles != null) {
					
					List<ResourcePermissionControlPanel> rpcps = new ArrayList<ResourcePermissionControlPanel>();

					for (int i = 0; i < permissionFiles.length; i++) {
						try {
							// System.out.println("permissionFiles["+i+"]="+permissionFiles[i].getCanonicalPath());
							PluginPermissionDataLoader.PermissionTypeData ptd = PluginPermissionDataLoader
									.loadType(permissionFiles[i]
											.getCanonicalPath());
							Iterator ptIt = ptd.values().iterator();
							while (ptIt.hasNext()) {
								PermissionObjectType pot = (PermissionObjectType) ptIt
										.next();
								PermissionResourceType prt = new PermissionResourceType();
								String key = pot.getObjectType();
								prt.setKey(key);
								prt.setName(pot.getName());
								prt.setTitle(pot.getTitle());
								prt.setType(new Long(pot.getType()).toString());
								//
								WebApplicationPlugin plugin = getPlugin();
								if (plugin != null) {
									Object obj = plugin.lookup(key
											+ ".reflection");
									if (obj != null
											&& obj instanceof IResourceReflection) {
										IResourceReflection resReflection = (IResourceReflection) obj;
										prt
												.setResourceReflection(resReflection);
									}
								}
								PluginPermissionDataLoader.PermissionData pd = (PermissionData) ptd
										.getPermissionDataMap().get(key);
								List<PermissionResourceItem> prItems = new ArrayList<PermissionResourceItem>();
								Iterator mykeyIt = pd.keySet().iterator();
								while (mykeyIt.hasNext()) {
									// the permission value
									Long pkey = (Long) mykeyIt.next();
									// the permission title
									Object value = pd.get(pkey);
									PermissionResourceItem prItem = new PermissionResourceItem();
									prItem.setKey(pkey.toString());
									prItem.setTitle(value.toString());
									prItems.add(prItem);
								}
								DefaultResourcePermissionControlPanel rpcp = new DefaultResourcePermissionControlPanel();
								rpcp.setPermissionResourceType(prt);
								rpcp.setPermissionResourceItems(prItems);

								//
								rpcps.add(rpcp);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						//
					}
					return rpcps;
				}

			}
		}
		return null;
	}

	protected abstract String getPluginHome();

	protected abstract WebApplicationPlugin getPlugin();
}
