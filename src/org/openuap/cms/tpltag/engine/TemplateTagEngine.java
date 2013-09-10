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
package org.openuap.cms.tpltag.engine;

import java.util.Map;

import freemarker.template.TemplateHashModel;

/**
 * <p>
 * 模板标签引擎
 * </p>
 * 
 * <p>
 * $Id: TemplateTagEngine.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface TemplateTagEngine {
	/**
	 * 获得标签的内容
	 * 
	 * @param name
	 *            标签名
	 * @param params
	 *            宏参数
	 * @param tplModel
	 *            当前模板上下文参数
	 * @return
	 */
	public String getTagContent(String name, Map params,
			TemplateHashModel tplModel);
}
