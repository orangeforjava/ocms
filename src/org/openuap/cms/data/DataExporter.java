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
package org.openuap.cms.data;

import java.io.File;
import java.util.Map;

/**
 * <p>
 * 数据导出接口
 * </p>
 * <p>
 * $Id: DataExporter.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public interface DataExporter {
	/**
	 * 返回是否采用zip压缩
	 * @return the zip
	 */
	public boolean isZip();

	/**
	 * 设置是否采用zip压缩
	 * @param zip
	 * 
	 */
	public void setZip(boolean zip);
	/**
	 * 设置数据目录
	 * @param dataDir
	 */
	public void setDataDir(File dataDir);
	/**
	 * 导出数据
	 * @param parameters
	 */
	public void exportData(Map parameters);
	/**
	 * 设置文件编码
	 * @param encoding
	 */
	public void setEncoding(String encoding);
	/**
	 * 设置文件名称
	 * @param name
	 */
	public void setName(String name);
	/**
	 * 获得标题
	 * @return
	 */
	public String getTitle();
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title);

	/**
	 * 获得备份文件数目
	 * @return
	 */
	public int getBackupFileNum();
}
