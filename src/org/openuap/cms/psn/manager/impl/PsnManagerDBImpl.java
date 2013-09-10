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
package org.openuap.cms.psn.manager.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.psn.cache.PsnCache;
import org.openuap.cms.psn.dao.PsnDao;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;

/**
 * 
 * <p>
 * Psn管理实现.
 * </p>
 * 
 * 
 * <p>
 * $Id: PsnManagerDBImpl.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class PsnManagerDBImpl implements PsnManager {

	private PsnDao psnDao;

	public PsnManagerDBImpl() {
	}

	public void setPsnDao(PsnDao dao) {
		this.psnDao = dao;
	}

	public Long addPsn(Psn psn) {
		return psnDao.addPsn(psn);
	}

	public void savePsn(Psn psn) {
		psnDao.savePsn(psn);
	}

	public void deletePsn(Long id) {
		psnDao.deletePsn(id);
	}

	public List getAllPsn() {
		return psnDao.getAllPsn();
	}

	public Psn getPsnById(Long id) {
		return psnDao.getPsnById(id);
	}

	public Psn getPsnByName(String name) {
		return psnDao.getPsnByName(name);
	}

	public long getPsnCount() {
		return psnDao.getPsnCount();
	}

	/**
	 * 获得psn url信息
	 * @param psnUrl
	 *            psn url信息
	 * @return String
	 */
	public String getPsnUrlInfo(String psnUrl) {
		if (psnUrl == null) {
			return null;
		}
		String url = psnUrl;
		String sp = "\\{PSN-URL:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(psnUrl);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();

		while (result) {
			String path = m.group(2);
			String psnId = m.group(1);
			Psn psn = this.getPsnFromCache(new Long(psnId));
			//获取url
			String mypsnUrl = psn.getUrl();
			m.appendReplacement(sb, mypsnUrl + path);
			result = m.find();
		} // end while result
		m.appendTail(sb);
		url = sb.toString();
		return url;
	}

	public Psn getPsn(String psnUrl) {
		if (psnUrl == null) {
			return null;
		}
		String sp = "\\{PSN-URL:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(psnUrl);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		if (result) {
			String psnId = m.group(1);
			Psn psn = this.getPsnFromCache(new Long(psnId));
			if (psn != null) {
				return psn;
			}
		}
		return null;
	}

	public Psn getPsnFromCache(Long id) {
		//
		return PsnCache.getPsn(id);
	}

	public String getRelativePath(String selfPsn) {
		String relativePath = "";
		String sp = "\\{PSN:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(selfPsn);
		boolean result = m.find();
		while (result) {
			String path = m.group(2);
			String psnId = m.group(1);
			Psn psn = this.getPsnFromCache(new Long(psnId));
			// String psnUrl = psn.getPsn();
			if (psn.getType() == Psn.LOCAL_PSN_TYPE) {
				// now,only process the local
				// remote will be do later.
				relativePath = psn.getLocalPath();
				relativePath += "/" + path;
			}
			result = m.find();
		} // end while result
		return relativePath;
	}

	public String getFullPath(String selfPsn) {
		String relativePath = getRelativePath(selfPsn);
		//系统根路径
		String sysRootPath = CMSConfig.getInstance().getSysRootPath();
		String fullPath=null;
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		return fullPath;
	}
	
}
