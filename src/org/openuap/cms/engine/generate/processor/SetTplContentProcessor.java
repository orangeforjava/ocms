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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.cms.engine.profile.impl.PublishOperationProfileImpl;
import org.openuap.cms.node.model.Node;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateProcessor;
import org.openuap.tpl.engine.TemplateProcessorChain;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 设置模板内容处理器
 * </p>
 * 
 * <p>
 * $Id: SetTplContentProcessor.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SetTplContentProcessor implements TemplateProcessor {

	private int priority = 5;
	public final static String EXT_POINT_NAME = "tpl-processor-set-tplcontent";

	public String getName() {
		return EXT_POINT_NAME;
	}

	public void processTemplate(TemplateProcessorChain chain,
			TemplateContext context, List errors) {
		// 性能诊断-start
		PublishOperationProfileImpl op = null;
		Exception exception = null;
		if (PublishProfileInfoHolder.isEnableProfile()) {
			op = new PublishOperationProfileImpl();
			op.setOperation("SetTplContentProcessor");
			op.setStartTime(System.currentTimeMillis());
		}
		//
		String tplContent = context.getTplContent();
		//当模板内容为空时从设置的模板文件中读取
		if (tplContent == null) {
			String tplName = context.getTplName();
			// System.out.println("tplName="+tplName);
			String userTplPath = this.getConfig().getUserTemplatePath();
			String tplPath = this.getConfig().getTemplatePath();
			//
			userTplPath = StringUtil.normalizePath(userTplPath);
			tplPath = StringUtil.normalizePath(tplPath);
			//		
			String fullTplPath = userTplPath + "/" + tplName;
			fullTplPath = StringUtil.normalizePath(fullTplPath);
			File tplFile = new File(fullTplPath);
			String encoding = getConfig().getStringProperty("cms.tpl.encoding",
					"UTF-8");
			// 模板编码
			Object oNode = context.getModel().get("node");
                        Long nid=-1L;
			if (oNode != null && oNode instanceof Node) {
				Node node = (Node) oNode;
                                nid=node.getNodeId();
				String tplEncoding = node.getTplEncoding();
				if (StringUtils.hasText(tplEncoding)) {
					encoding = tplEncoding;
				}
			}

			if (!tplFile.exists()) {
				// 若用户目录不存在，则查找系统目录
				fullTplPath = tplPath + "/" + tplName;
				fullTplPath = StringUtil.normalizePath(fullTplPath);
				tplFile = new File(fullTplPath);
			}
			if (tplFile.exists() && tplFile.isFile()) {
				try {
					tplContent = FileUtil.readTextFile(tplFile, encoding);
                                        //
                                        tplContent+="<!--template=["+tplFile.getAbsolutePath()+"] nodeId=["+nid+"] publishId=[]-->";
				} catch (FileNotFoundException e) {
					exception = e;
					e.printStackTrace();
				} catch (IOException e) {
					exception = e;
					e.printStackTrace();
				}
			}
		}
		// 性能诊断-end
		if (PublishProfileInfoHolder.isEnableProfile()) {
			op.setEndTime(System.currentTimeMillis());
			op.setException(exception);
			PublishProfileInfoHolder.getProfile().addPublishOperation(op);
		}
		//
		if (tplContent != null && !tplContent.equals("")) {
			context.setTplContent(tplContent);
			chain.doProcess(context, errors);
		}
	}

	protected CMSConfig getConfig() {
		return CMSConfig.getInstance();
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
