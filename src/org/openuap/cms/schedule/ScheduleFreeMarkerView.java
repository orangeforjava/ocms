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
package org.openuap.cms.schedule;

import org.openuap.base.web.mvc.view.BaseFreeMarkerView;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

/**
 * 
 * <p>
 * 计划任务FreeMarker视图类
 * </p>
 * $Id: ScheduleFreeMarkerView.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * 
 * @author Weiping Ju
 * 
 */
public class ScheduleFreeMarkerView extends BaseFreeMarkerView {
	@Override
	protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
		try {

			return (FreeMarkerConfig) BeanFactoryUtils.beanOfType(
					getApplicationContext(), FreeMarkerConfig.class, true,
					false);
		} catch (NoSuchBeanDefinitionException ex) {
			throw new ApplicationContextException(
					"Must define a single FreeMarkerConfig bean in this web application context "
							+ "(may be inherited): FreeMarkerConfigurer is the usual implementation. "
							+ "This bean may be given any name.", ex);
		}
	}
}
