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
package org.openuap.cms.tpl.dao;

import java.util.List;

import org.openuap.cms.tpl.model.TemplateRef;

/**
 * <p>
 * 模板引用DAO.
 * </p>
 * 
 * <p>
 * $Id: TemplateRefDao.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface TemplateRefDao {

	public void addTemplateRef(TemplateRef templateRef);

	public void saveTemplateRef(TemplateRef templateRef);

	public TemplateRef getTemplateRefById(Long indexId, Long templateId);

	public List getTemplateRefByTemplateId(Long templateId);

	public List getTemplateRefByIndexId(Long indexId);

	public void deleteTemplateRef(Long indexId, Long templateId);

	public void deleteTemplateRefByTemplate(Long templateId);

	public void deleteTempalteRefByIndex(Long indexId);
}
