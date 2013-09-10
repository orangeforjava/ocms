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

/**
 * <p>
 * 抽象数据导出器实现
 * </p>
 * <p>
 * $Id: AbstractDataExporter.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractDataExporter implements DataExporter {

	protected File exportFile;

	protected boolean zip;

	protected String encoding = "UTF-8";

	protected String name;

	protected File dataDir;

	protected String title;

	public File getDataDir() {
		return dataDir;
	}

	public void setDataDir(File dataDir) {
		this.dataDir = dataDir;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public File getExportFile() {
		return exportFile;
	}

	public void setExportFile(File exportFile) {
		this.exportFile = exportFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isZip() {
		return zip;
	}

	public void setZip(boolean zip) {
		this.zip = zip;
	}
}
