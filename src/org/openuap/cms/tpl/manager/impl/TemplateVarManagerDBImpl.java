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

import java.util.List;

import org.openuap.cms.tpl.dao.TemplateVarDao;
import org.openuap.cms.tpl.manager.TemplateVarManager;
import org.openuap.cms.tpl.model.TemplateVar;

/**
 * <p>
 * 模板变量管理实现.
 * </p>
 * 
 * <p>
 * $Id: TemplateVarManagerDBImpl.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateVarManagerDBImpl implements TemplateVarManager {

	private TemplateVarDao templateVarDao;

	public TemplateVarManagerDBImpl() {
	}

	public Long addTemplateVar(TemplateVar templateVar) {
		return templateVarDao.addTemplateVar(templateVar);
	}

	public void saveTemplateVar(TemplateVar templateVar) {
		templateVarDao.saveTemplateVar(templateVar);
	}

	public void deleteTemplateVar(TemplateVar templateVar) {
		templateVarDao.deleteTemplateVar(templateVar);
	}

	public void deleteTemplateVarById(long id) {
		templateVarDao.deleteTemplateVarById(id);
	}

	public TemplateVar getTemplateVarById(long id) {
		return templateVarDao.getTemplateVarById(id);
	}

	public TemplateVar getTemplateVarByName(String templateVar) {
		return templateVarDao.getTemplateVarByName(templateVar);
	}

	public List getAllTemplateVars() {
		return templateVarDao.getAllTemplateVars();
	}

	public void setTemplateVarDao(TemplateVarDao templateVarDao) {
		this.templateVarDao = templateVarDao;
	}
}
