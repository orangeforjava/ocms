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

import org.openuap.runtime.setup.persistence.hibernate.schema.PluginSchemaHelper;

/**
 * <p>
 * CMS数字模型帮助类
 * </p>
 * 
 * <p>
 * $Id: CmsSchemaHelper.java 3948 2010-11-01 14:52:04Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class CmsSchemaHelper extends PluginSchemaHelper {

	@Override
	protected String getDomain() {
		return "base";
	}

	@Override
	protected String getPluginId() {
		return CmsPlugin.PLUGIN_ID;
	}

}
