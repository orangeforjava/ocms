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
package org.openuap.cms.stat.manager;

import org.openuap.cms.stat.dao.CountDao;
import org.openuap.cms.stat.model.CmsCount;

/**
 * <p>
 * 计数管理接口.
 * </p>
 * 
 * <p>
 * $Id: CountManager.java 3920 2010-10-26 11:41:54Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface CountManager {

	public void setCountDao(CountDao countDao);

	public CmsCount getCountById(Long indexId);

	public void saveCount(CmsCount count);

	public void removeCount(CmsCount count);

	public void removeCountById(Long indexId);

}
