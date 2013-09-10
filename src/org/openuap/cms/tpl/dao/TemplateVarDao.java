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

import org.openuap.cms.tpl.model.TemplateVar;

/**
 * <p>
 * 模版变量Dao
 * </p>
 * 
 * <p>
 * $Id: TemplateVarDao.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface TemplateVarDao {

	public Long addTemplateVar(TemplateVar templateVar);

	public void saveTemplateVar(TemplateVar template);

	public void deleteTemplateVar(TemplateVar template);

	public void deleteTemplateVarById(long id);

	public TemplateVar getTemplateVarById(long id);

	public TemplateVar getTemplateVarByName(String name);

	public List getAllTemplateVars();

}
