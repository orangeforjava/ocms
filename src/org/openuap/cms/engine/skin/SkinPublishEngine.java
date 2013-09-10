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
package org.openuap.cms.engine.skin;

import java.util.List;

/**
 * <p>
 * 皮肤发布引擎.
 * </p>
 * 
 * 
 * <p>
 * $Id: SkinPublishEngine.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface SkinPublishEngine {
	/**
	 * publish the template skin(img/flash/attach/js/html/css) copy the skin
	 * replace the ${SKIN_PATH} to http://?
	 * 
	 * @param templateContent
	 *            String
	 * @param errors
	 *            List
	 * @return the replaced template content
	 */
	public String publishTemplateSkin(String templateContent, List errors);
}
