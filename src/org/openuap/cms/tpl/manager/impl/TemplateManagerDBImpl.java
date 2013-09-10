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
package org.openuap.cms.tpl.manager.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.tpl.dao.TemplateDao;
import org.openuap.cms.tpl.dao.TemplateRefDao;
import org.openuap.cms.tpl.manager.TemplateManager;
import org.openuap.cms.tpl.model.Template;
import org.openuap.cms.tpl.model.TemplateFile;
import org.openuap.cms.tpl.model.TemplateRef;

/**
 * <p>
 * 模板管理实现.
 * </p>
 * 
 * <p>
 * $Id: TemplateManagerDBImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class TemplateManagerDBImpl implements TemplateManager {

	private TemplateDao templateDao;
	private TemplateRefDao templateRefDao;

	/**
	 * 
	 */
	public TemplateManagerDBImpl() {
	}

	/**
	 * get the assign folder' child folder numbers it is a simple thing,only
	 * rember the parentFoler is relative path to the user template path.
	 * 
	 * @param parentFolder
	 *            parent folder relative path
	 * @return the child folder numbers
	 */
	public long getChildFolderCount(String parentFolder) {
		String tplRootPath = CMSConfig.getInstance().getUserTemplatePath();
		String fullPath = tplRootPath;
		String decodeFolderName = parentFolder;
		if (!decodeFolderName.trim().equals("")) {
			fullPath += File.separator + decodeFolderName;
		}
		fullPath = StringUtil.normalizePath(fullPath);
		File folder = new File(fullPath);
		long i = 0l;
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (int j = 0; j < files.length; j++) {
					File file = files[j];
					if (file.isDirectory()) {
						i++;
					}
				}
			}
		}
		return i;
	}

	/**
	 * it only return the child folder,not include the file.
	 * 
	 * @param parentFolder
	 *            String
	 * @return List
	 */
	public List getChildFolders(String parentFolder) {

		String tplRootPath = CMSConfig.getInstance().getUserTemplatePath();
		String fullPath = tplRootPath;
		if (!parentFolder.trim().equals("")) {
			fullPath += File.separator + parentFolder;
		}
		fullPath = StringUtil.normalizePath(fullPath);

		File folder = new File(fullPath);
		List folders = new ArrayList();
		//
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (int j = 0; j < files.length; j++) {
					File file = files[j];
					if (file.isDirectory()) {
						folders.add(file);
					}
				}
			}
		}
		Collections.sort(folders, new Comparator() {
			public int compare(Object f1, Object f2) {
				File file1 = (File) f1;
				File file2 = (File) f2;
				return file1.getName().compareToIgnoreCase(file2.getName());
			}

			public boolean equals(Object obj) {
				return false;
			}
		});
		List tplList = new ArrayList();
		for (int k = 0; k < folders.size(); k++) {
			File f = (File) folders.get(k);
			TemplateFile tf = new TemplateFile();
			String name = f.getName();
			String path = parentFolder + "/" + name;
			String path2 = StringUtil.encodeURL(path, "UTF-8");
			tf.setName(name);
			tf.setEncodedPath(path2);
			tf.setPath(path);
			tf.setCreateDate(new Date(f.lastModified()));
			tf.setSize(f.length());
			// it only a folder,so it is "dir" icon
			tf.setIcon("dir");
			// the file property,now only set to normal
			// it will be complete later!
			// @todo ...
			tf.setProperty("normal");
			long count = getChildFolderCount(path);
			if (count > 0) {
				tf.setExistChildren(true);
			}
			tplList.add(tf);
		}
		return tplList;

	}

	public List getChildTemplates(String parentFolder) {

		String tplRootPath = CMSConfig.getInstance().getUserTemplatePath();
		String fullPath = tplRootPath;
		if (!parentFolder.trim().equals("")) {
			fullPath += File.separator + parentFolder;
		}
		fullPath = StringUtil.normalizePath(fullPath);

		File folder = new File(fullPath);
		List fileList = new ArrayList();
		//
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (int j = 0; j < files.length; j++) {
					File file = files[j];
					//
					fileList.add(file);
				}
			}
		}
		Collections.sort(fileList, new java.util.Comparator() {
			public int compare(Object o1, Object o2) {
				File file1 = (File) o1;
				File file2 = (File) o2;
				if (file1.isDirectory() && file2.isFile()) {
					return -1;
				}
				if (file1.isFile() && file2.isDirectory()) {
					return 1;
				}
				if ((file1.isDirectory() && file2.isDirectory())
						|| (file1.isFile() && file2.isFile())) {
					return file1.getName().compareToIgnoreCase(file2.getName());
				}
				return 0;
			}

			public boolean equals(Object obj) {
				return false;
			}
		});
		List tplList = new ArrayList();
		for (int k = 0; k < fileList.size(); k++) {
			File f = (File) fileList.get(k);
			TemplateFile tf = new TemplateFile();
			String name = f.getName();
			String path = parentFolder + "/" + name;
			tf.setName(name);
			tf.setPath(path);
			tf.setCreateDate(new Date(f.lastModified()));
			tf.setSize(f.length());
			boolean isFolder = f.isDirectory();
			tf.setFolder(isFolder);
			// the extension name is icon name

			if (isFolder) {
				tf.setIcon("dir");
			} else {
				int pos = name.lastIndexOf(".");
				String icon = name.substring(pos + 1);
				tf.setIcon(icon);
			}

			// the file property,now only set to normal
			// it will be complete later!
			// @todo ...
			tf.setProperty("normal");
			long count = getChildFolderCount(path);
			if (count > 0) {
				tf.setExistChildren(true);
			}
			tplList.add(tf);
		}
		return tplList;

	}

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public Long addTemplate(Template template) {
		return this.templateDao.addTemplate(template);
	}

	public void saveTemplate(Template template) {
		this.templateDao.saveTemplate(template);
	}

	public void deleteTemplate(Template template) {
		templateDao.deleteTemplate(template);
	}

	public void deleteTemplateById(Long id) {
		templateDao.deleteTemplateById(id);
	}

	public Template getTemplateById(Long id) {
		return templateDao.getTemplateById(id);
	}

	public Template getTemplateByName(String name) {
		return templateDao.getTemplateByName(name);
	}

	public List getTemplates(Long tcid) {
		return templateDao.getTemplates(tcid);
	}

	public List getTemplates(Long tcid, QueryInfo qi, PageBuilder pb) {
		return templateDao.getTemplates(tcid, qi, pb);
	}

	public List getTemplates(String hql, String hql_count, QueryInfo qi,
			PageBuilder pb) {
		return templateDao.getTemplates(hql, hql_count, qi, pb);
	}

	public void setTemplateRefDao(TemplateRefDao templateRefDao) {
		this.templateRefDao = templateRefDao;
	}

	public void addTemplateRef(TemplateRef templateRef) {
		this.templateRefDao.addTemplateRef(templateRef);
	}

	public void saveTemplateRef(TemplateRef templateRef) {
		this.templateRefDao.saveTemplateRef(templateRef);
	}

	public TemplateRef getTemplateRefById(Long indexId, Long templateId) {
		return templateRefDao.getTemplateRefById(indexId, templateId);
	}

	public List getTemplateRefByTemplateId(Long templateId) {
		return templateRefDao.getTemplateRefByTemplateId(templateId);
	}

	public List getTemplateRefByIndexId(Long indexId) {
		return templateRefDao.getTemplateRefByIndexId(indexId);
	}

	public void deleteTemplateRef(Long indexId, Long templateId) {
		templateRefDao.deleteTemplateRef(indexId, templateId);
	}

	public void deleteTemplateRefByTemplate(Long templateId) {
		templateRefDao.deleteTemplateRefByTemplate(templateId);
	}

	public void deleteTempalteRefByIndex(Long indexId) {
		templateRefDao.deleteTempalteRefByIndex(indexId);
	}
}
