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
package org.openuap.cms.data.manager;

import java.io.File;

import org.openuap.runtime.config.ApplicationConfiguration;
import org.openuap.runtime.plugin.WebPluginManagerUtils;

/**
 * <p>
 * 抽象数据管理实现.
 * </p>
 * 
 * <p>
 * $Id: AbstractDataManager.java 3947 2010-11-01 14:23:26Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractDataManager {

	protected File getDataDir() {

		ApplicationConfiguration config = WebPluginManagerUtils
				.getApplicationConfiguration("");

		String backupDir = config.getString(DataExportManager.BACKUP_PROP);
		// System.out.println("backupDir="+backupDir);
		File dataDir = new File(backupDir);
		return dataDir;
	}
}
