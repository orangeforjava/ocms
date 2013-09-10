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
package org.openuap.cms.tpltag.engine.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openuap.cms.tpltag.cache.TemplateTagCache;
import org.openuap.cms.tpltag.engine.TemplateTagEngine;
import org.openuap.cms.tpltag.model.TemplateTag;
import org.openuap.runtime.util.ObjectLocator;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateEngine;

import freemarker.template.TemplateHashModel;

/**
 * <p>
 * 缺省模板标签引擎实现
 * </p>
 * 
 * <p>
 * $Id: DefaultTemplateTagEngine.java 3919 2010-10-26 11:41:35Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultTemplateTagEngine implements TemplateTagEngine {
	/** 模板引擎. */
	private TemplateEngine templateEngine;

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	/**
	 * 1）判断是否已经生成了内容<br/>
	 * 2）若没有生成则生成
	 */
	public String getTagContent(String name, Map params,
			TemplateHashModel tplModel) {
		StringBuffer rs = new StringBuffer();
		//从缓存中获得模板标记
		TemplateTag tplTag = TemplateTagCache.getTagByName(name);
		// 
		if (tplTag != null) {
			TemplateContext context = new TemplateContext();
			//设置模板内容或者名称
			if (tplTag.getTplType().equals(TemplateTag.TPL_TYPE_FILE)) {
				context.setTplName(tplTag.getTemplatePath());
			} else {
				context.setTplContent(tplTag.getTemplateContent());
			}
			List errors = new ArrayList();
			Map model = Collections.synchronizedMap(new HashMap());
			model.putAll(params);
			model.put("__direct_out__", "yes");
			context.setModel(model);
			//渲染模板输出内容
			getTemplateEngine().renderTemplate(context, errors);
			String content = context.getTplContent();
			rs.append(content);
			if (errors.size() > 0) {
				for (Object o : errors) {
					System.out.println("error:" + o);
				}
			}
		}
		return rs.toString();
	}

	public TemplateEngine getTemplateEngine() {
		if (templateEngine == null) {
			templateEngine = (TemplateEngine) ObjectLocator.lookup(
					"templateEngine", "org.openuap.tpl.engine");
		}
		return templateEngine;
	}

}
