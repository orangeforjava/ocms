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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.openuap.cms.CmsPlugin;
import org.openuap.cms.data.AbstractDataImporter;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.ObjectLocator;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * $Id: NodeDataImporter.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * @author Weiping Ju
 * 
 */
public class NodeDataImporter extends AbstractDataImporter {



	public void importData(Map parameters) {
		String fileName = (String) parameters.get(FILE_NAME);
		
		File file=null;
		if (fileName == null) {
			// 如果没有指定哪个文件，则寻找最新的文件导入
			file=getDefaultImportFile();
		}else{
			file=new File(dataDir,fileName);
		}
		if(file!=null&&file.exists()){
			XStream xstream = new XStream();
			ClassLoader cl = WebPluginManagerUtils
					.getPluginClassLoader(CmsPlugin.PLUGIN_ID);
			if (cl != null) {
				xstream.setClassLoader(cl);
			}
			xstream.alias("node", Node.class);
			xstream.setMode(XStream.NO_REFERENCES);
			try {
				Reader reader = new InputStreamReader(new FileInputStream(
						file), encoding);
				//
				Object obj=xstream.fromXML(reader);
				NodeManager nodeManager = (NodeManager) ObjectLocator.lookup(
						"nodeManager", CmsPlugin.PLUGIN_ID);
				//
				if(obj instanceof List){
					List <Node>nodes=(List<Node>) obj;
					int size=nodes.size();
					for(int i=0;i<size;i++){
						Node node=nodes.get(i);
						nodeManager.saveNode(node);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
