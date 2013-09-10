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
package org.openuap.cms.psn.manager;

import java.util.List;

import org.openuap.cms.psn.dao.PsnDao;
import org.openuap.cms.psn.model.Psn;

/**
 * 
 * <p>
 * Psn管理对象接口.
 * </p>
 * 
 * <p>
 * $Id: PsnManager.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface PsnManager {

	public void setPsnDao(PsnDao dao);

	public Long addPsn(Psn psn);

	public void savePsn(Psn psn);

	public void deletePsn(Long id);

	public List getAllPsn();

	public Psn getPsnById(Long id);

	public Psn getPsnByName(String name);

	public long getPsnCount();

	//////////////////////////////////////////////////////////////////////////////
	/**
	 * 获得PSN URL信息
	 * 
	 * @param psnUrl
	 *            String
	 * @return String
	 */
	public String getPsnUrlInfo(String psnUrl);

	/**
	 * 根据url信息获得PSN对象
	 * 
	 * @param psnUrl
	 *            String
	 * @return DataSourceModel
	 */
	public Psn getPsn(String psnUrl);
	
	public Psn getPsnFromCache(Long id);
	
	public String getRelativePath(String selfPsn);
	
	public String getFullPath(String selfPsn);
}
