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
package org.openuap.cms.node.cache;

import java.util.Collections;
import java.util.List;

import org.josql.Query;
import org.josql.QueryResults;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.node.model.Node;
import org.openuap.runtime.plugin.WebPluginManagerUtils;

/**
 * <p>
 * 结点快速查询，基于JOSQL
 * </p>
 * <p>
 * $Id: NodeQuickQuery.java 4005 2011-01-11 17:59:13Z orangeforjava $
 * </p>
 * 
 * @author joseph
 * @version 4.0
 */
public class NodeQuickQuery {
	public final static String NODE_CLASSNAME = "org.openuap.cms.node.model.Node";

	public static List<Node> getChildNodes(List<Node> nodes, Long parentId,
			Integer disabled) {
		Query q = new Query();
		q.setClassLoader(WebPluginManagerUtils
				.getPluginClassLoader(CmsPlugin.PLUGIN_ID));
		QueryResults results = null;
		List rs = null;
		String osql = "SELECT * FROM " + NODE_CLASSNAME + " WHERE  parentId="
				+ parentId + " and disabled=" + disabled
				+ " ORDER BY nodeSort DESC";
		try {
			q.parse(osql);
			results = q.execute(nodes);
			rs = results.getResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null) {
			return rs;
		} else {
			return Collections.EMPTY_LIST;
		}
	}
}
