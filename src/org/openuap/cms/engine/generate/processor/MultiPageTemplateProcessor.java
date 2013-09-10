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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.cms.engine.profile.impl.PublishOperationProfileImpl;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateProcessor;
import org.openuap.tpl.engine.TemplateProcessorChain;

/**
 * <p>
 * 分页模板处理器
 * </p>
 * 
 * <p>
 * $Id: MultiPageTemplateProcessor.java 3293 2008-11-20 13:09:14Z orangeforjava$
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class MultiPageTemplateProcessor implements TemplateProcessor {

	private int priority = 15;
	public final static String EXT_POINT_NAME = "tpl-processor-multipage";

	private CmsMacroEngine cmsMacroEngine;

	public String getName() {
		return EXT_POINT_NAME;
	}

	public void processTemplate(TemplateProcessorChain chain,
			TemplateContext context, List errors) {
		String tplContent = context.getTplContent();
		//
		if (tplContent != null) {

			// 性能诊断-start
			PublishOperationProfileImpl op = null;
			Exception exception = null;
			if (PublishProfileInfoHolder.isEnableProfile()) {
				op = new PublishOperationProfileImpl();
				op.setOperation("MultiPageTemplateProcessor");
				op.setStartTime(System.currentTimeMillis());
			}
			String multiMacro = getMultiPageMacro(tplContent);
			if (multiMacro != null) {
				// 若找到需要分页的宏，则进行分页处理
				PageBuilder pi = getPageInfoFromMacro(multiMacro, context);
				// 性能诊断-end
				if (PublishProfileInfoHolder.isEnableProfile()) {
					op.setEndTime(System.currentTimeMillis());
					op.setException(exception);
					op.setRecords(pi.pages());
					PublishProfileInfoHolder.getProfile().addPublishOperation(
							op);
				}
				if (pi.pages() >= 1) {
					// 页数目大于1
					if (context.getModel().get("__direct_out__") == null) {
						//静态输出
						for (int i = 0; i < pi.pages(); i++) {
							// 替换每页中的分页宏为普通宏，加入页码信息
							int page = i + 1;
							String mytplContent = replaceMultiPageMacro(
									tplContent, context, String.valueOf(page));
							//
							context.getModel().put("page", page);
							context.setTplContent(mytplContent);
							// 注意这里需要克隆当前状态给每一个分页处理项
							((TemplateProcessorChain) chain.clone()).doProcess(
									context, errors);
						}
					}else{
						//动态输出
						Object oPage = context.getModel().get("page");
						if (oPage == null) {
							oPage = 1;
						}
						int page = new Integer(String.valueOf(oPage));
						String mytplContent = replaceMultiPageMacro(
								tplContent, context, String.valueOf(page));
						//
						
						context.setTplContent(mytplContent);
						// 注意这里需要克隆当前状态给每一个分页处理项
						((TemplateProcessorChain) chain.clone()).doProcess(
								context, errors);
					}
				}
			} else {
				// 性能诊断-end
				if (PublishProfileInfoHolder.isEnableProfile()) {
					op.setEndTime(System.currentTimeMillis());
					op.setException(exception);
					op.setRecords(1);
					PublishProfileInfoHolder.getProfile().addPublishOperation(
							op);
				}
				//
				if (context.getModel().get("page") == null) {
					context.getModel().put("page", "1");
				}
				chain.doProcess(context, errors);
			}
		}
	}

	/**
	 * 查找启用分页标识的标记
	 * 
	 * @param tplContent
	 * 
	 * @return String
	 */
	private String getMultiPageMacro(String tplContent) {
		String sp = "\\[@cms\\.list(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)Num=\"page-\\d+\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)extra=\"multipage\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		if (result) {
			String path = m.group(0);
			return path;
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @param macro
	 * 
	 * @param nodeId
	 *            String
	 * @return PageInfo
	 */
	protected PageBuilder getPageInfoFromMacro(String macro,
			TemplateContext context) {
		// NodeID
		String regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)NodeID=\"(self|\\d*|\\d+(,\\d+)*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String NodeID = getMacroInfo(regex, macro, 2);
		if (NodeID == null) {
			NodeID = "";
		} else {
			if (NodeID.equals("self")) {
				Object nodeId = context.getModel().get("nodeId");
				if (nodeId != null) {
					NodeID = nodeId.toString();
				}
			}
		}
		// num
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)Num=\"(page-\\d+)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String num = getMacroInfo(regex, macro, 2);
		if (num == null) {
			return null;
		}
		// NodeGUID
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)NodeGUID=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String NodeGUID = getMacroInfo(regex, macro, 2);
		if (NodeGUID == null) {
			NodeGUID = "";
		}
		// where
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)where=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String where = getMacroInfo(regex, macro, 2);
		if (where == null) {
			where = "";
		}
		// TableID
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)TableID=\"(\\d*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String TableID = getMacroInfo(regex, macro, 2);
		if (TableID == null) {
			TableID = "";
		}
		// ignore
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)ignore=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String ignore = getMacroInfo(regex, macro, 2);
		if (ignore == null) {
			ignore = "";
		}
		// url
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)url=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String url = getMacroInfo(regex, macro, 2);
		if (url == null) {
			url = "";
		}
		// page
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)page=\"(\\d*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String page = getMacroInfo(regex, macro, 2);
		if (page == null) {
			page = "";
		}
		// 获得分页信息，用来分页处理
		PageBuilder pb = cmsMacroEngine.getCmsListPageInfo(NodeID, num,
				NodeGUID, "", where, TableID, ignore, page, url);
		return pb;
	}

	private String getMacroInfo(String regex, String macro, int group) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(macro);
		boolean result = m.find();
		if (result) {
			String info = m.group(group);
			return info;
		}
		return null;
	}

	/**
	 * 
	 * @param tplContent
	 *            String
	 * @param url
	 *            String
	 * @param page
	 *            String
	 * @return String
	 */
	protected String replaceMultiPageMacro(String tplContent,
			TemplateContext context, String page) {
		String sp = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)Num=\"page-\\d+\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)extra=\"multipage\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)*(\\/)?\\]";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		if (result) {
			String macro = m.group(0);
			String replaceMacro = getReplaceMultiMacro(macro, context, page);
			m.appendReplacement(sb, replaceMacro);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * @param macro
	 *            String
	 * @param url
	 *            String
	 * @param page
	 *            String
	 * @return String
	 */
	protected String getReplaceMultiMacro(String macro,
			TemplateContext context, String page) {
		// NodeID
		String regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)NodeID=\"(self|\\d*|\\d+(,\\d+)*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String NodeID = getMacroInfo(regex, macro, 2);
		if (NodeID == null) {
			NodeID = "";
		}
		// num
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)Num=\"(page-\\d+)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String num = getMacroInfo(regex, macro, 2);
		if (num == null) {
			return null;
		}
		// NodeGUID
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)NodeGUID=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String NodeGUID = getMacroInfo(regex, macro, 2);
		if (NodeGUID == null) {
			NodeGUID = "";
		}
		// where
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)where=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String where = getMacroInfo(regex, macro, 2);
		if (where == null) {
			where = "";
		}
		// TableID
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)TableID=\"(\\d*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String TableID = getMacroInfo(regex, macro, 2);
		if (TableID == null) {
			TableID = "";
		}
		// ignore
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)ignore=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*\\s*)(\\/)?\\]";
		String ignore = getMacroInfo(regex, macro, 2);
		if (ignore == null) {
			ignore = "";
		}
		// OrderBy
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)OrderBy=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String OrderBy = getMacroInfo(regex, macro, 2);
		if (OrderBy == null) {
			OrderBy = "";
		}

		// macroreturn
		regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)return=\"([\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]\"]]*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String macroreturn = getMacroInfo(regex, macro, 2);
		if (macroreturn == null) {
			macroreturn = "";
		}
		StringBuffer sb = new StringBuffer("\\[@cms.list return=\""
				+ macroreturn + "\"");
		sb.append(" NodeID=\"" + NodeID + "\"");
		sb.append(" Num=\"" + num + "\"");
		sb.append(" OrderBy=\"" + OrderBy + "\"");
		sb.append(" where=\"" + where + "\"");
		sb.append(" TableID=\"" + TableID + "\"");
		sb.append(" NodeGUID=\"" + NodeGUID + "\"");
		sb.append(" ignore=\"" + ignore + "\"");
		sb.append(" page=\"" + page + "\"");
		Object url = context.getModel().get("url");
		sb.append(" url=\"" + url + "\"");
		sb.append(" /\\]");
		return sb.toString();
	}

	public CmsMacroEngine getCmsMacroEngine() {
		return cmsMacroEngine;
	}

	public void setCmsMacroEngine(CmsMacroEngine cmsMacroEngine) {
		this.cmsMacroEngine = cmsMacroEngine;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
