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
package org.openuap.cms;

import org.openuap.base.web.mvc.view.BaseFreeMarkerView;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

/**
 * CMS FreeMarkerView
 * <p>
 * $Id: CmsFreeMarkerView.java 3913 2010-10-26 09:32:09Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * 
 */
public class CmsFreeMarkerView extends BaseFreeMarkerView {

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
