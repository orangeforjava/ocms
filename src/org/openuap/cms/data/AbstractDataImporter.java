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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 抽象数据导入器实现
 * </p>
 * <p>
 * $Id: AbstractDataImporter.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractDataImporter implements DataImporter {

	protected File importFile;

	protected boolean zip;

	protected String encoding = "UTF-8";

	protected String name;

	protected File dataDir;

	protected String title;

	public File getDefaultImportFile() {
		List<File> files = getImportFiles();
		if (files != null) {
			return files.get(0);
		}
		return null;
	}

	public List<File> getImportFiles() {
		if (dataDir != null) {

			File[] files = dataDir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String prefix = getName();
					String suffix = ".xml";

					if (name.startsWith(prefix) && name.endsWith(suffix)) {
						return true;
					}
					return false;
				}
			});
			//
			if (files != null) {
				// 文件按照名称逆排序
				Arrays.sort(files, new Comparator<File>() {
					public int compare(File o1, File o2) {
						return o2.getName().compareTo(o1.getName());
					}
				});
				List<File> rs = new ArrayList<File>();
				for (int i = 0; i < files.length; i++) {
					rs.add(files[i]);
				}
				return rs;
			}
		}
		return null;
	}

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

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
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
