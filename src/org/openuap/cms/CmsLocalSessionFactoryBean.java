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

import org.openuap.runtime.setup.hibernate.PluginLocalSessionFactoryBean;

/**
 * CMS HibernateSession工厂
 * <p>
 * $Id: CmsLocalSessionFactoryBean.java 3477 2009-03-05 08:56:31Z orangeforjava
 * $
 * </p>
 * 
 * @author Joseph
 * 
 */
public class CmsLocalSessionFactoryBean extends PluginLocalSessionFactoryBean {

	@Override
	protected String getDomain() {
		return "base";
	}

	@Override
	protected String getPluginId() {
		return CmsPlugin.PLUGIN_ID;
	}

}
