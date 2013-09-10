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
package org.openuap.cms.node.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.data.AbstractDataExporter;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.ObjectLocator;

import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * </p>
 * 
 * <p>
 * $Id: NodeDataExporter.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Weiping Ju
 * 
 */
public class NodeDataExporter extends AbstractDataExporter {


	public NodeDataExporter() {
	}

	public void exportData(Map parameters) {

		try {
			Date now = Calendar.getInstance().getTime();
			DateFormat df = new java.text.SimpleDateFormat(
					"yyyy-MM-dd-HHmmssSSS");
			String dnow = df.format(now);
			String fileName = this.getName() + "-" + dnow + ".xml";
			exportFile = new File(dataDir, fileName);
			NodeManager nodeManager = (NodeManager) ObjectLocator.lookup(
					"nodeManager", CmsPlugin.PLUGIN_ID);
			if (nodeManager != null) {
				List<Node> nodes = nodeManager.getAllNodes();
				XStream xstream = new XStream();
				ClassLoader cl = WebPluginManagerUtils
						.getPluginClassLoader(CmsPlugin.PLUGIN_ID);
				if (cl != null) {
					xstream.setClassLoader(cl);
				}
				xstream.alias("node", Node.class);
				xstream.setMode(XStream.NO_REFERENCES);
				Writer writer = new OutputStreamWriter(new FileOutputStream(
						exportFile), encoding);
				xstream.toXML(nodes, writer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getBackupFileNum() {
		return 0;
	}	
}
