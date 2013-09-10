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
package org.openuap.cms.vfs.dao;

import java.util.List;

import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.vfs.model.FtpSetting;

/**
 * <p>
 * FTP设置DAO接口.
 * </p>
 * 
 * <p>
 * $Id: FtpSettingDao.java 3916 2010-10-26 09:35:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface FtpSettingDao {

	public Long addFtpSetting(FtpSetting ftpSetting);

	public void saveFtpSetting(FtpSetting ftpSetting);

	public void deleteFtpSetting(Long id);

	public FtpSetting getFtpSettingById(Long id);
	
	public List<FtpSetting> getAllFtpSettings();

	public List<FtpSetting> getFtpSettings(String hql, String hql_count,
			QueryInfo qi, PageBuilder pb);
	
	public List<FtpSetting> getFtpSettings(QueryInfo qi, PageBuilder pb);
}
