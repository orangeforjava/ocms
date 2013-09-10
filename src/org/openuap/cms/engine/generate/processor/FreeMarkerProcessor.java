/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.engine.generate.processor;

import java.util.List;
import java.util.Map;

import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateProcessor;
import org.openuap.tpl.engine.TemplateProcessorChain;

/**
 * <p>
 * FreeMarker模板处理
 * </p>
 * 
 * <p>
 * $Id: FreeMarkerProcessor.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class FreeMarkerProcessor implements TemplateProcessor {

	private FreeMarkerTplProcessorHelper freeMarkerHelper;
	private int priority = 20;
	public final static String EXT_POINT_NAME = "tpl-processor-freemarker";

	public String getName() {
		return EXT_POINT_NAME;
	}

	public void processTemplate(TemplateProcessorChain chain,
			TemplateContext context, List errors) {
		try {

			String tplContent = context.getTplContent();
			//
			String tplName = context.getTplName();
			Map model = context.getModel();
			//
			String rs = freeMarkerHelper.processTemplate(tplContent, tplName,
					model, errors);
			context.setTplContent(rs);
			if (rs != null) {
				chain.doProcess(context, errors);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFreeMarkerHelper(
			FreeMarkerTplProcessorHelper freeMarkerHelper) {
		this.freeMarkerHelper = freeMarkerHelper;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
