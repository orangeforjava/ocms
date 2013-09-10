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
package org.openuap.cms.cm.model;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Title: ContentTables
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: <a href="http://www.openuap.org">http://www.openuap.org</a>
 * </p>
 * $Id: ContentTables.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * @preserve private
 * @author Weiping Ju
 * @version 1.0
 */
public class ContentTables implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1068270879569916380L;
	private List contentTables;

	public ContentTables() {
	}

	public int getContentTableCount() {
		if (contentTables != null) {
			return contentTables.size();
		}
		return 0;
	}

	public List getContentTables() {
		return contentTables;
	}

	public void setContentTables(List contentTables) {
		this.contentTables = contentTables;
	}
}
