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
package org.openuap.cms.engine.generate.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.FileUtil;
import org.openuap.base.util.MacroDefineService;
import org.openuap.base.util.MapService;
import org.openuap.base.util.StringUtil;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.base.util.resource.ConstantLoader;
import org.openuap.base.web.mvc.view.BaseClassTemplateLoader;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.engine.generate.StaticFileGenerateEngine;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.publish.manager.ExtraPublishManager;
import org.openuap.cms.publish.model.ExtraPublish;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.util.ui.PublishMode;
import org.openuap.runtime.plugin.view.freemarker.PluginFreeMarkerConfigurer;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.openuap.runtime.setup.BaseConfigUtil;
import org.openuap.tpl.engine.TemplateContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <p>
 * 缺省文件生成引擎实现.
 * </p>
 * 
 * <p>
 * $Id: DefaultStaticFileGenerateEngine.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 * @deprecated
 */
public class DefaultStaticFileGenerateEngine implements
		StaticFileGenerateEngine, InitializingBean, ApplicationContextAware {
	//
	private PluginFreeMarkerConfigurer freemarkerConfigurer;

	private DynamicContentManager dynamicContentManager;

	private NodeManager nodeManager;

	private Map macroServiceMap = new HashMap();

	private Map macroMap = new HashMap();

	//
	private PsnManager psnManager;

	//
	private ExtraPublishManager extraPublishManager;

	//
	private CmsMacroEngine cmsMacroEngine;

	private ApplicationContext applicationContext;

	/**
	 * 
	 */
	public DefaultStaticFileGenerateEngine() {
	}

	public void setFreemarkerConfigurer(PluginFreeMarkerConfigurer configurer) {
		this.freemarkerConfigurer = configurer;

	}

	public void setDynamicContentManager(
			DynamicContentManager dynamicContentManager) {
		this.dynamicContentManager = dynamicContentManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	protected CMSConfig getConfig() {
		return CMSConfig.getInstance();
	}

	public synchronized boolean generateContentStaticFile(Long nodeId,
			Long indexId, List errors) {

		boolean forward = false;
		//
		//
		try {
			// 获得内容索引对象
			ContentIndex ci = dynamicContentManager
					.getContentIndexById(indexId);
			if (ci == null) {
				return false;
			}
			//
			// 获得内容所在的结点
			Node node = nodeManager.getNodeById(nodeId);
			if (node == null) {
				return false;
			} else {
				Integer publishMode = node.getPublishMode();
				Integer selfPublishMode = ci.getPublishMode();
				//
				if (selfPublishMode != null
						&& !selfPublishMode.equals(new Integer(-1))) {
					publishMode = selfPublishMode;
				}
				// 检查结点的发布模式，非静态发布结点不需要处理
				if (!publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
					return false;
				}
			}

			String tpl = null;
			String url = null;
			// 获得内容自定义模板
			String selfTpl = ci.getSelfTemplate();
			if (StringUtils.hasText(selfTpl)) {
				tpl = selfTpl;
			} else {
				// get from the node info
				String conentTpl = node.getContentTpl();
				tpl = conentTpl;
			}
			// get the psn url info,it will be used to some url link
			url = ci.getUrl();
			// 纠正url
			String okUrl = getContentUrl(ci, node);
			if (okUrl != null && !okUrl.equals(url)) {
				url = okUrl;
				ci.setUrl(okUrl);
				dynamicContentManager.saveContentIndex(ci);
			}
			// 产生静态文件
			forward = generateStaticFile(tpl, url, node, ci, errors);
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			forward = false;
		}
		return forward;
	}

	public String getPagerUrl(String url, String page) {
		String rt = url;
		if (page != null && !page.equals("0") && !page.equals("")
				&& !page.equals("1")) {
			int pos = url.lastIndexOf(".");
			if (pos > -1) {
				String part1 = url.substring(0, pos);
				String part2 = url.substring(pos + 1);
				rt = part1 + "_" + page + "." + part2;
			} else {
				rt = url + "_" + page;
			}
		}
		return rt;
	}

	/**
	 * 产生静态文件
	 * 
	 * @param tplName
	 *            模板名字
	 * @param url
	 *            发布Url
	 * @param node
	 *            发布结点
	 * @param ci
	 *            内容索引
	 * @param errors
	 *            错误列表
	 * @return boolean
	 */
	public synchronized boolean generateStaticFile(String tplName, String url,
			Node node, ContentIndex ci, List errors) {
		boolean success = true;
		try {
			// create a new freemarker configuration object
			Configuration newConfiguration = freemarkerConfigurer
					.createConfiguration();
			// 获得用户模板路径
			String userTplPath = this.getConfig().getUserTemplatePath();
			userTplPath = StringUtil.normalizePath(userTplPath);
			// 设置模板引擎配置属性
			settingConfiguration(newConfiguration, userTplPath);

			// 1)get the template file
			String fullTplPath = userTplPath + "/" + tplName;
			fullTplPath = StringUtil.normalizePath(fullTplPath);
			File tplFile = new File(fullTplPath);
			// 获得模板文件编码
			String encoding = getTemplateEncoding();
			// read the template file content
			if (tplFile.exists() && tplFile.isFile()) {
				// 读取模板内容
				String tplContent = FileUtil.readTextFile(tplFile, encoding);
				// prefore process the tplContent,add [#ftl] tag
				tplContent = this.preProcessContent(tplContent);
				// check if need process
				// get the multiMacro,to process some pager info
				String multiMacro = getMultiPageMacro2(tplContent);
				if (multiMacro != null) {
					// 需要分页,获得页码信息
					PageBuilder pi = getPageInfoFromMacro2(multiMacro, node
							.getNodeId().toString());
					if (pi.pages() > 1) {
						// should process
						for (int i = 0; i < pi.pages(); i++) {
							String mytplContent = replaceMultiPageMacro2(
									tplContent, url, String.valueOf(i + 1));
							// process the skin file refrence
							String new_mytplContent = processSkinFile2(
									mytplContent, node, errors);
							File destFile = getDestFile(node, ci, getConfig(),
									String.valueOf(i + 1));
							//
							success = generatePageStaticFile(new_mytplContent,
									String.valueOf(i + 1), url, destFile,
									newConfiguration, node, ci, errors);
						}
					} else {
						tplContent = replaceAllPagerMacro2(tplContent, url, "1");
						// process the skin file refrence
						String new_tplContent = processSkinFile2(tplContent,
								node, errors);
						File destFile = getDestFile(node, ci, getConfig(), "1");

						success = generatePageStaticFile(new_tplContent, "1",
								url, destFile, newConfiguration, node, ci,
								errors);
					}
				} else {
					// 无列表分页
					// 判断是否有内容分页
					// process the skin file refrence
					String new_tplContent = processSkinFile2(tplContent, node,
							errors);
					File destFile = getDestFile(node, ci, getConfig(), "1");
					success = generatePageStaticFile(new_tplContent, "1", url,
							destFile, newConfiguration, node, ci, errors);
				}
				//
			}
		} catch (Exception ex1) {
			ex1.printStackTrace();
			errors.add(ex1);
			success = false;
		}
		return success;

	}

	public synchronized boolean generateNodeIndexStaticFile(String tplName,
			String url, Node node, List errors) {
		boolean success = true;
		try {
			Configuration newConfiguration = freemarkerConfigurer
					.createConfiguration();
			String userTplPath = this.getConfig().getUserTemplatePath();
			userTplPath = StringUtil.normalizePath(userTplPath);
			// set the newConfiguration property.
			settingConfiguration(newConfiguration, userTplPath);
			// 1)get the template file
			String fullTplPath = userTplPath + "/" + tplName;
			fullTplPath = StringUtil.normalizePath(fullTplPath);
			File tplFile = new File(fullTplPath);
			String encoding = getConfig().getStringProperty("sys.tpl.encoding",
					"UTF-8");
			if (tplFile.exists() && tplFile.isFile()) {
				String tplContent = FileUtil.readTextFile(tplFile, encoding);
				//
				tplContent = this.preProcessContent(tplContent);
				// check if need process
				// get the multiMacro
				String multiMacro = getMultiPageMacro2(tplContent);
				if (multiMacro != null) {
					PageBuilder pi = getPageInfoFromMacro2(multiMacro, node
							.getNodeId().toString());
					if (pi.pages() > 1) {
						// should process
						for (int i = 0; i < pi.pages(); i++) {
							String mytplContent = replaceMultiPageMacro2(
									tplContent, url, String.valueOf(i + 1));
							// process the skin file refrence
							String new_mytplContent = processSkinFile2(
									mytplContent, node, errors);
							File destFile = getDestNodeIndexFile(node,
									getConfig(), String.valueOf(i + 1));
							//
							success = generatePageStaticFile(new_mytplContent,
									String.valueOf(i + 1), url, destFile,
									newConfiguration, node, null, errors);
						}
					} else {
						tplContent = replaceAllPagerMacro2(tplContent, url, "1");
						// process the skin file refrence
						String new_tplContent = processSkinFile2(tplContent,
								node, errors);
						File destFile = getDestNodeIndexFile(node, getConfig(),
								"1");

						success = generatePageStaticFile(new_tplContent, "1",
								url, destFile, newConfiguration, node, null,
								errors);
					}
				} else {
					String mytplContent = replaceAllPagerMacro2(tplContent,
							url, "1");
					// process the skin file refrence
					String new_tplContent = processSkinFile2(mytplContent,
							node, errors);
					File destFile = getDestNodeIndexFile(node, getConfig(), "1");
					success = generatePageStaticFile(new_tplContent, "1", url,
							destFile, newConfiguration, node, null, errors);
				}
				//
			}
		} catch (Exception ex1) {
			ex1.printStackTrace();
			errors.add(ex1);
			success = false;
		}
		return success;

	}

	/**
	 * 产生实际的静态页面
	 * 
	 * @param tplContent
	 *            模板内容
	 * 
	 * @param page
	 *            页面序号
	 * 
	 * @param url
	 *            内容URL
	 * 
	 * @param destFile
	 *            目的文件
	 * 
	 * @param configuration
	 *            模板配置
	 * 
	 * @param node
	 *            结点
	 * 
	 * @param ci
	 *            内容索引页面
	 * 
	 * @param errors
	 *            错误列表
	 * 
	 * @return boolean
	 */
	protected synchronized boolean generatePageStaticFile(String tplContent,
			String page, String url, File destFile,
			Configuration configuration, Node node, ContentIndex ci, List errors) {
		File tmpTplFile = null;
		// boolean delTmp=true;
		try {
			//
			// 产生临时模板文件
			// String tmp_prefix = "/tmp/";
			// String rootPath = this.getConfig().getTemplatePath();
			// String tmpTplFileName = "/~" + System.currentTimeMillis() +
			// ".html";
			// String tmpTplPath = rootPath + tmp_prefix + tmpTplFileName;
			// tmpTplPath = StringUtil.normalizePath(tmpTplPath);
			// tmpTplFile = new File(tmpTplPath);
			// String encoding =
			// this.getConfig().getStringProperty("sys.tpl.encoding", "UTF-8");
			// //
			// FileUtil.writeTextFile(tmpTplFile, tplContent, encoding);
			Map model = new HashMap();
			// 装载语言包
			ConstantLoader.Constant lang = ConstantLoader
					.load("CMSInternalLanguage.xml");
			if (lang != null) {
				model.put("lang", lang);
			}

			// 60820799
			//
			Template template = new Template(url, new StringReader(tplContent),
					configuration);
			// Template template = configuration.getTemplate(tmpTplFileName);
			//
			//
			String baseUrl = CMSConfig.getInstance().getBaseUrl();
			//
			model.put("currentNodeId", node.getNodeId());
			model.put("currentIndexId", ci == null ? new Long(0) : ci
					.getIndexId());
			model.put("cmsMacroEngine", cmsMacroEngine);
			//
			Set keySet = this.macroServiceMap.keySet();
			if (keySet.size() > 0) {
				Iterator keyIterator = keySet.iterator();
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					model.put(key, macroServiceMap.get(key));

				}
			}

			model.put("currentPage", String.valueOf(page));
			model.put("currentUrl", url);
			model.put("baseUrl", baseUrl);
			// model.put("memberPresenceUrl",
			// CMSConfig.getInstance().getMemberPresenceUrl());
			// model.put("memberPresenceImagesUrl", CMSConfig.getInstance()
			// .getMemberPresenceImagesUrl());
			// model.put("imServerName",
			// CMSConfig.getInstance().getImServerName());
			//
			model.put("SKIN_PATH", "");
			//
			model.put("psn", psnManager.getPsn(node.getContentUrl()));

			String fileContent = FreeMarkerTemplateUtils
					.processTemplateIntoString(template, model);
			//
			//
			// fileContent = "<!--#config ERRMSG=\" \" -->\n" + fileContent;
			// 获得模板输出编码
			String outEncoding = getTemplateOutEncoding();
			postConentProcess(destFile, fileContent, outEncoding,
					configuration, node, ci, url, errors);
			// 删除临时模板文件
			// if (tmpTplFile.exists()) {
			// tmpTplFile.delete();
			// }
		} catch (Exception ex) {

			ex.printStackTrace();
			errors.add(ex);
			return false;
		} finally {
			// delete the temp tpl file
			// if (tmpTplFile != null && tmpTplFile.exists()) {
			// tmpTplFile.delete();
			// }
		}
		return true;
	}

	public synchronized void renderTemplate(TemplateContext context, List errors) {

	}

	public File getDestDir(Long nodeId, Long indexId) {
		Node node = nodeManager.getNodeById(nodeId);
		ContentIndex ci = dynamicContentManager.getContentIndexById(indexId);

		CMSConfig config = CMSConfig.getInstance();
		String fullPath = "";
		String sysRootPath = config.getSysRootPath();
		String relativePath = "";
		String selfPsn = ci.getSelfPsn();
		if (StringUtils.hasText(selfPsn)) {
			relativePath = getRelativePath(selfPsn);
		} else {
			String pblPsn = node.getContentPsn();
			relativePath = getRelativePath(pblPsn);
		}
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		// sub dir
		String fileName = "";
		String selfFileName = ci.getSelfPublishFileName();
		//
		if (StringUtils.hasText(selfFileName)) {
			//
			int pos = selfFileName.lastIndexOf("/");
			String subDir = "";

			if (pos > -1) {
				subDir = selfFileName.substring(0, pos);
				fullPath += "/" + subDir;
				fileName = selfFileName.substring(pos + 1);
			} else {
				fileName = selfFileName;
			}

		} else {
			String subDir = node.getSubDir();
			String destDir = "";
			destDir = getDestDirName(subDir, ci);
			if (!destDir.equals("")) {
				fullPath += "/" + destDir;
			}
			fileName = getDestFileName(indexId, node, ci);
		}
		//
		fullPath = StringUtil.normalizePath(fullPath);
		//
		File psnDir = new File(fullPath);
		return psnDir;
	}

	private File getDestFile(Node node, ContentIndex ci, CMSConfig config,
			String page) {
		String fullPath = "";
		String sysRootPath = config.getSysRootPath();
		String relativePath = "";
		String selfPsn = ci.getSelfPsn();
		if (StringUtils.hasText(selfPsn)) {
			relativePath = getRelativePath(selfPsn);
		} else {
			String pblPsn = node.getContentPsn();
			relativePath = getRelativePath(pblPsn);
		}
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		// sub dir
		String fileName = "";
		String selfFileName = ci.getSelfPublishFileName();
		//
		if (StringUtils.hasText(selfFileName)) {
			//
			int pos = selfFileName.lastIndexOf("/");
			String subDir = "";

			if (pos > -1) {
				subDir = selfFileName.substring(0, pos);
				fullPath += "/" + subDir;
				fileName = selfFileName.substring(pos + 1);

			} else {
				fileName = selfFileName;
			}

		} else {
			String subDir = node.getSubDir();
			String destDir = "";
			destDir = getDestDirName(subDir, ci);
			if (!destDir.equals("")) {
				fullPath += "/" + destDir;
			}
			fileName = getDestFileName(ci.getIndexId(), node, ci);
		}
		//
		if (page != null && !page.equals("0") && !page.equals("")
				&& !page.equals("1")) {
			int pos = fileName.lastIndexOf(".");
			if (pos > -1) {
				String fileName_no_extension = fileName.substring(0, pos);
				String file_extension = fileName.substring(pos + 1);
				fileName = fileName_no_extension + "_" + page + "."
						+ file_extension;
			} else {
				fileName = fileName + "_" + page;
			}
		}

		//
		fullPath = StringUtil.normalizePath(fullPath);
		File psnDir = new File(fullPath);
		if (!psnDir.exists()) {
			psnDir.mkdirs();
		}
		File destFile = new File(psnDir, fileName);
		// System.out.println("fullPath is333:"+fullPath);
		return destFile;

	}

	/**
	 * 
	 * @param node
	 *            Node
	 * @param config
	 *            CMSConfig
	 * @param page
	 *            String
	 * @return File
	 */
	public File getDestNodeIndexFile(Node node, CMSConfig config, String page) {
		String fullPath = "";
		String sysRootPath = config.getSysRootPath();
		String relativePath = "";
		String indexFileName = "";
		String pblPsn = node.getContentPsn();
		relativePath = getRelativePath(pblPsn);
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		indexFileName = node.getIndexName();
		indexFileName = indexFileName.replaceAll("\\{NodeID\\}", node
				.getNodeId().toString());
		if (page != null && !page.equals("0") && !page.equals("")
				&& !page.equals("1")) {
			int pos = indexFileName.lastIndexOf(".");
			if (pos > -1) {
				String fileName_no_extension = indexFileName.substring(0, pos);
				String file_extension = indexFileName.substring(pos + 1);
				indexFileName = fileName_no_extension + "_" + page + "."
						+ file_extension;
			} else {
				indexFileName = indexFileName + "_" + page;
			}
		}
		fullPath = StringUtil.normalizePath(fullPath);
		File dir = new File(fullPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// System.out.println("fullPath is222:"+fullPath);
		//
		File destFile = new File(dir, indexFileName);

		return destFile;
	}

	public File getDestNodeExtraFile(Node node, ExtraPublish publish,
			CMSConfig config, String page) {
		String fullPath = "";
		String sysRootPath = config.getSysRootPath();
		String relativePath = "";
		String indexFileName = "";
		String pblPsn = publish.getSelfPsn();
		if (StringUtils.hasText(pblPsn)) {

		} else {
			pblPsn = node.getContentPsn();
		}
		relativePath = getRelativePath(pblPsn);
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		//
		indexFileName = publish.getPublishFileName();
		//
		if (page != null && !page.equals("0") && !page.equals("")
				&& !page.equals("1")) {
			int pos = indexFileName.lastIndexOf(".");
			if (pos > -1) {
				String fileName_no_extension = indexFileName.substring(0, pos);
				String file_extension = indexFileName.substring(pos + 1);
				indexFileName = fileName_no_extension + "_" + page + "."
						+ file_extension;
			} else {
				indexFileName = indexFileName + "_" + page;
			}
		}
		fullPath = StringUtil.normalizePath(fullPath);
		File dir = new File(fullPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// System.out.println("fullPath is11:"+fullPath);
		//
		File destFile = new File(dir, indexFileName);
		return destFile;

	}

	public File getDestFile(Long nodeId, Long indexId) {
		Node node = nodeManager.getNodeById(nodeId);
		ContentIndex ci = dynamicContentManager.getContentIndexById(indexId);

		CMSConfig config = CMSConfig.getInstance();
		String fullPath = "";
		String sysRootPath = config.getSysRootPath();
		String relativePath = "";
		String selfPsn = ci.getSelfPsn();
		if (StringUtils.hasText(selfPsn)) {
			relativePath = getRelativePath(selfPsn);
		} else {
			String pblPsn = node.getContentPsn();
			relativePath = getRelativePath(pblPsn);
		}
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		// sub dir
		String fileName = "";
		String selfFileName = ci.getSelfPublishFileName();
		//
		if (StringUtils.hasText(selfFileName)) {
			//
			int pos = selfFileName.lastIndexOf("/");
			String subDir = "";

			if (pos > -1) {
				subDir = selfFileName.substring(0, pos);
				fullPath += "/" + subDir;
				fileName = selfFileName.substring(pos + 1);
			} else {
				fileName = selfFileName;
			}

		} else {
			String subDir = node.getSubDir();
			String destDir = "";
			destDir = getDestDirName(subDir, ci);
			if (!destDir.equals("")) {
				fullPath += "/" + destDir;
			}
			fileName = getDestFileName(indexId, node, ci);
		}
		//
		fullPath = StringUtil.normalizePath(fullPath);
		File psnDir = new File(fullPath);
		if (!psnDir.exists()) {
			psnDir.mkdirs();
		}
		File destFile = new File(psnDir, fileName);
		return destFile;

	}

	private String getDestFileName(Long indexId, Node node, ContentIndex ci) {
		String fileName = "";
		ci.getPublishDate().longValue();
		long timeStamp = ci.getPublishDate().longValue();
		String stimeStamp = "" + timeStamp;
		fileName = node.getPublishFileFormat();
		//
		fileName = fileName.replaceAll("\\{TimeStamp\\}", stimeStamp);
		fileName = fileName.replaceAll("\\{ContentID\\}", ci.getContentId()
				.toString());
		fileName = fileName.replaceAll("\\{IndexID\\}", indexId.toString());
		fileName = fileName.replaceAll("\\{NodeID\\}", ci.getNodeId()
				.toString());
		return fileName;
	}

	private String getDestDirName(String subDir, ContentIndex ci) {
		String destDir = "";
		if (!subDir.equals("")) {
			String ft = "yyyy-MM-dd";
			if (!subDir.equals("auto")) {
				subDir = subDir.replaceAll("Y", "yyyy");
				subDir = subDir.replaceAll("m", "MM");
				subDir = subDir.replaceAll("d", "dd");
				ft = subDir;
			}
			SimpleDateFormat sf = new SimpleDateFormat(ft);
			long publishDate = ci.getPublishDate().longValue(); // * 1000l;
			destDir = sf.format(new Date(publishDate));
		}
		return destDir;
	}

	private String getRelativePath(String selfPsn) throws NumberFormatException {
		String relativePath = "";
		String sp = "\\{PSN:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(selfPsn);
		boolean result = m.find();
		while (result) {
			String path = m.group(2);
			String psnId = m.group(1);
			Psn psn = psnManager.getPsnById(new Long(psnId));
			// String psnUrl = psn.getPsn();
			if (psn.getType() == Psn.LOCAL_PSN_TYPE) {
				// now,only process the local
				// remote will be do later.
				relativePath = psn.getLocalPath();
				relativePath += "/" + path;
			}
			result = m.find();
		} // end while result
		return relativePath;
	}

	/**
	 * 产生结点静态文件，对应结点更新动作
	 * 
	 * @param parentId
	 *            Long
	 * @param mode
	 *            PublishEngineMode
	 * @param errors
	 *            List
	 * @return boolean
	 */
	public boolean generateAllNodeStaticFile(Long parentId,
			PublishEngineMode mode, List errors) {
		boolean success = true;
		if (mode.isContainIndex()) {
			success = generateNodeIndexStaticFile(parentId, errors);
		}
		if (mode.isContainExtraPublish()) {
			success = generateNodeAllExtraIndexStaticFile(parentId, errors);
		}
		if (mode.isContainContent()) {
			//
			long publishNums = dynamicContentManager
					.getNodePublishContentCount(parentId);
			int pageSize = mode.getProcessContentNums();
			int totalPage = (int) (publishNums / pageSize);
			if (publishNums % pageSize > 0) {
				totalPage++;
			}
			//
			for (int i = 0; i < totalPage; i++) {
				List ciList = dynamicContentManager.getNodePublishContents(
						parentId, new Long(i * pageSize), new Long(pageSize));
				if (ciList != null) {
					for (int j = 0; j < ciList.size(); j++) {
						ContentIndex ci = (ContentIndex) ciList.get(j);
						success = generateContentStaticFile(parentId, ci
								.getIndexId(), errors);
					}
				}
			}
		}
		if (mode.isContainChildNode()) {
			List childNodes = nodeManager.getNodes(parentId, new Long(0),
					new Integer("0"));
			if (childNodes != null) {
				for (int i = 0; i < childNodes.size(); i++) {
					Node childNode = (Node) childNodes.get(i);
					//
					success = generateAllNodeStaticFile(childNode.getNodeId(),
							mode, errors);
				}
			}
		}
		return success;
	}

	public NodeManager getNodeManager() {
		return nodeManager;
	}

	public PluginFreeMarkerConfigurer getFreemarkerConfigurer() {
		return freemarkerConfigurer;
	}

	public DynamicContentManager getDynamicContentManager() {
		return dynamicContentManager;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setCmsMacroEngine(CmsMacroEngine cmsMacroEngine) {
		this.cmsMacroEngine = cmsMacroEngine;
	}

	public void setExtraPublishManager(ExtraPublishManager extraPublishManager) {
		this.extraPublishManager = extraPublishManager;
	}

	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 查找启用分页标识的标记
	 * 
	 * @param tplContent
	 *            String
	 * @return String
	 */
	private String getMultiPageMacro2(String tplContent) {
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

	protected String getContentPagerMacro(String tplContent) {
		String sp = "\\[@util\\.contentPager(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)content=\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)\"size=\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
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
	 * another freeMarker template
	 * 
	 * @param macro
	 *            String
	 * @param nodeId
	 *            String
	 * @return PageInfo
	 */
	protected PageBuilder getPageInfoFromMacro2(String macro, String nodeId) {
		// get the NodeID info
		// get the num info
		// get the NodeGUID info
		// get the where info
		// get the TableID info
		// get the ignore info
		// get the url info
		// get the page info
		//
		// NodeID
		String regex = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)NodeID=\"(self|\\d*|\\d+(,\\d+)*)\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
		String NodeID = getMacroInfo(regex, macro, 2);
		if (NodeID == null) {
			NodeID = "";
		} else {
			if (NodeID.equals("self")) {
				NodeID = nodeId;
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

	// protected ContentPageInfo getContentPageInfoFromMacro(String macro) {
	// String regex = "\\[@util\\.contentPagerlist
	// (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)content=\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)(\\/)?\\]";
	// String NodeID = getMacroInfo(regex, macro, 2);
	// if (NodeID == null) {
	// NodeID = "";
	// } else {
	// if (NodeID.equals("self")) {
	// NodeID = nodeId;
	// }
	// }
	//
	// return null;
	// }
	protected String getReplaceMultiMacro(String macro, String url, String page) {
		// NodeID
		String regex = "<@cms\\.list (\\s*\\p{Print}*\\s*)NodeID=\"(self|\\d*|\\d+(,\\d+)*)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String NodeID = getMacroInfo(regex, macro, 2);
		if (NodeID == null) {
			NodeID = "";
		}
		// num
		regex = "<@cms\\.list (\\s*\\p{Print}*\\S*)Num=\"(page-\\d+)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String num = getMacroInfo(regex, macro, 2);
		if (num == null) {
			return null;
		}
		// NodeGUID
		regex = "<@cms\\.list (\\s*\\p{Print}*\\s*)NodeGUID=\"([^\"&&[\\s\\p{Print}]]*)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String NodeGUID = getMacroInfo(regex, macro, 2);
		if (NodeGUID == null) {
			NodeGUID = "";
		}
		// where
		regex = "<@cms\\.list (\\s*\\p{Print}*\\s*)where=\"([^\"&&[\\s\\p{Print}]]*)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String where = getMacroInfo(regex, macro, 2);
		if (where == null) {
			where = "";
		}
		// TableID
		regex = "<@cms\\.list (\\s*\\p{Print}*\\s*)TableID=\"(\\d*)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String TableID = getMacroInfo(regex, macro, 2);
		if (TableID == null) {
			TableID = "";
		}
		// ignore
		regex = "<@cms\\.list (\\s*\\p{Print}*\\s*)ignore=\"([^\"&&[\\s\\p{Print}]]*)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String ignore = getMacroInfo(regex, macro, 2);
		if (ignore == null) {
			ignore = "";
		}
		// OrderBy
		regex = "<@cms\\.list (\\s*\\p{Print}*\\s*)OrderBy=\"([^\"&&[\\s\\p{Print}]]*)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String OrderBy = getMacroInfo(regex, macro, 2);
		if (OrderBy == null) {
			OrderBy = "";
		}

		// macroreturn
		regex = "<@cms\\.list (\\s*\\p{Print}*\\s*)return=\"([^\"&&[\\s\\p{Print}]]*)\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		String macroreturn = getMacroInfo(regex, macro, 2);
		if (macroreturn == null) {
			macroreturn = "";
		}
		StringBuffer sb = new StringBuffer("<@cms.list return=\"" + macroreturn
				+ "\"");
		sb.append(" NodeID=\"" + NodeID + "\"");
		sb.append(" Num=\"" + num + "\"");
		sb.append(" OrderBy=\"" + OrderBy + "\"");
		sb.append(" where=\"" + where + "\"");
		sb.append(" TableID=\"" + TableID + "\"");
		sb.append(" NodeGUID=\"" + NodeGUID + "\"");
		sb.append(" ignore=\"" + ignore + "\"");
		sb.append(" page=\"" + page + "\"");
		sb.append(" url=\"" + url + "\"");
		sb.append(" />");
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
	protected String getReplaceMultiMacro2(String macro, String url, String page) {
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
		sb.append(" url=\"" + url + "\"");
		sb.append(" /\\]");
		return sb.toString();
	}

	protected String replaceMultiPageMacro(String tplContent, String url,
			String page) {
		String sp = "<@cms\\.list (\\s*\\p{Print}*\\s*)Num=\"page-\\d+\" (\\s*\\p{Print}*\\s*) extra=\"multipage\"(\\s*\\p{Print}*\\s*)(\\/)?>";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		if (result) {
			String macro = m.group(0);
			String replaceMacro = getReplaceMultiMacro(macro, url, page);
			m.appendReplacement(sb, replaceMacro);
		}
		m.appendTail(sb);
		return sb.toString();
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
	protected String replaceMultiPageMacro2(String tplContent, String url,
			String page) {
		String sp = "\\[@cms\\.list (\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)Num=\"page-\\d+\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)extra=\"multipage\"(\\s*[\\p{Print}[^\\x00-\\xff]&&[^\\[\\/\\]]]*\\s*)*(\\/)?\\]";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		if (result) {
			String macro = m.group(0);
			String replaceMacro = getReplaceMultiMacro2(macro, url, page);
			m.appendReplacement(sb, replaceMacro);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private String getMacroInfo(String regex, String macro, int group) {
		// String sp = "<@cms\\.list (\\S*\\p{Print}*\\S*) num=\"page-\\d+\"
		// extra=\"multipage\" \\S*(\\/)?>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(macro);
		boolean result = m.find();
		if (result) {
			String info = m.group(group);
			return info;
		}
		return null;
	}

	protected String replaceAllPagerMacro(String tplContent, String url,
			String page) {
		String sp = "<@cms\\.list (\\s*\\p{Print}*\\s*) Num=\"page-\\d+\"(\\s*\\p{Print}*\\s*) (\\/)?>";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		while (result) {
			String macro = m.group(0);
			String replaceMacro = getReplaceMultiMacro(macro, url, page);
			m.appendReplacement(sb, replaceMacro);
			result = m.find();
		}
		m.appendTail(sb);
		return sb.toString();
	}

	protected String replaceAllPagerMacro2(String tplContent, String url,
			String page) {
		String sp = "\\[@cms\\.list (\\s*\\p{Print}*\\s*) Num=\"page-\\d+\"(\\s*\\p{Print}*\\s*) (\\/)?\\]";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		while (result) {
			String macro = m.group(0);
			String replaceMacro = getReplaceMultiMacro2(macro, url, page);
			m.appendReplacement(sb, replaceMacro);
			result = m.find();
		}
		m.appendTail(sb);
		return sb.toString();
	}

	// ////////////////////////////////////////////////////////////////////////////
	/**
	 * process the template resource refrence. 1)the template resource may be
	 * need another path
	 * 
	 * @param tplContent
	 *            String
	 * @param node
	 *            Node
	 * @param errors
	 *            List
	 * @return String
	 */
	public String processSkinFile(String tplContent, Node node, List errors) {
		CMSConfig config = CMSConfig.getInstance();
		String imgExtensions = config.getUploadFileImageType();
		String flashExtensions = config.getUploadFileFlashType();
		String attachExtensions = config.getUploadFileAttachType();
		String sAllowExt = imgExtensions + "|" + flashExtensions + "|"
				+ attachExtensions + "|js|html|htm|shtml|css|icon";
		String sp = "\\$\\{SKIN_PATH\\}((\\/\\w+)*\\.(" + sAllowExt + "))";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		String skinRootPath = config.getTemplateSkinPath();
		String sysRootPath = config.getSysRootPath();
		// List errors = new ArrayList();
		while (result) {
			String skin = m.group(1);
			//
			File srcFile = getSkinSrcFile(skinRootPath, skin, errors);
			if (srcFile != null) {
				File destFile = getSkinDestFile(sysRootPath, skin, node, errors);
				try {
					FileUtil.copy(srcFile, destFile);
					//

				} catch (Exception ex) {
					errors.add(ex);
				}
			}
			String destSkinUrl = getDestSkinUrl(node);
			destSkinUrl += "/" + "skins/" + skin;
			m.appendReplacement(sb, destSkinUrl);

			result = m.find();
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * 
	 * @param tplContent
	 *            String
	 * @param node
	 *            Node
	 * @param errors
	 *            List
	 * @return String
	 */
	public String processSkinFile2(String tplContent, Node node, List errors) {
		CMSConfig config = CMSConfig.getInstance();
		String imgExtensions = config.getUploadFileImageType();
		String flashExtensions = config.getUploadFileFlashType();
		String attachExtensions = config.getUploadFileAttachType();
		String sAllowExt = imgExtensions + "|" + flashExtensions + "|"
				+ attachExtensions + "|js|html|htm|shtml|css|icon";
		// 1)process the resource refrence
		List tplRsList = new ArrayList();
		//
		String sp = "\\.\\.\\/resource\\/((img|flash|attach)(\\/\\w+)+\\.("
				+ sAllowExt + "))";
		String rsRootDir = config.getResourceRootPath();
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(tplContent);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		String skinRootPath = config.getTemplateSkinPath();
		String sysRootPath = config.getSysRootPath();
		//
		String destResourceUrl = "";
		String destSkinUrl = "";
		String rsUrl = node.getResourceUrl();
		//
		destResourceUrl = psnManager.getPsnUrlInfo(rsUrl);
		destSkinUrl = getDestSkinUrl(node);
		// List errors = new ArrayList();
		while (result) {
			//
			String path = m.group(1);
			if (!tplRsList.contains(path)) {
				tplRsList.add(path);
				//
				String fullPath = rsRootDir;
				fullPath += File.separator + path;
				fullPath = StringUtil.normalizePath(fullPath);
				File srcFile = new File(fullPath);
				File destFile = getResourceDestFile(sysRootPath, path, node,
						errors);
				try {
					// decide if should copy file.
					if (shouldCopyFile(srcFile, destFile)) {
						FileUtil.copy(srcFile, destFile);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			//
			String destResourceUrl2 = destResourceUrl + "/" + path;
			m.appendReplacement(sb, destResourceUrl2);
			result = m.find();
		}
		m.appendTail(sb);
		tplRsList = null;
		// 2)process the template skin refrence
		String skinContent = sb.toString();
		// System.out.println(skinContent);
		return processTemplateSkins(node, errors, sAllowExt, skinRootPath,
				sysRootPath, destSkinUrl, skinContent);
	}

	private String processTemplateSkins(Node node, List errors,
			String sAllowExt, String skinRootPath, String sysRootPath,
			String destSkinUrl, String skinContent) {
		//
		List tplSkinList = new ArrayList();
		//
		String sp_skin = "\\.\\.\\/templates\\/skins(([\\/\\w\\-\\~]+)+\\.("
				+ sAllowExt + "))";
		Pattern p_skin = Pattern.compile(sp_skin);
		Matcher m_skin = p_skin.matcher(skinContent);
		boolean result_skin = m_skin.find();
		StringBuffer sb_skin = new StringBuffer();
		while (result_skin) {
			// String full = m_skin.group(0);
			String path = m_skin.group(1);
			if (!tplSkinList.contains(path)) {
				tplSkinList.add(path);
				String fullPath = skinRootPath;
				fullPath += File.separator + path;
				fullPath = StringUtil.normalizePath(fullPath);
				File srcFile = new File(fullPath);
				File destFile = getSkinDestFile(sysRootPath, path, node, errors);
				try {
					if (shouldCopyFile(srcFile, destFile)) {
						FileUtil.copy(srcFile, destFile);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			//
			String mydestSkinUrl = destSkinUrl;
			mydestSkinUrl += "/skins" + path;
			m_skin.appendReplacement(sb_skin, mydestSkinUrl);
			result_skin = m_skin.find();
		}
		m_skin.appendTail(sb_skin);
		tplSkinList = null;
		// System.out.println(sb_skin.toString());
		return sb_skin.toString();
	}

	protected File getSkinSrcFile(String skinRootDir, String srcSkinFileName,
			List errors) {
		String fullPath = skinRootDir + "/" + srcSkinFileName;
		fullPath = StringUtil.normalizePath(fullPath);
		File srcFile = new File(fullPath);
		if (srcFile.exists() && srcFile.isFile()) {
			return srcFile;
		} else {
			return null;
		}
	}

	protected File getSkinDestFile(String rootDir, String destFileName,
			Node node, List errors) {
		String fullPath = rootDir;
		// Node node = nodeManager.getNodeById(nodeId);
		String rsPsn = node.getResourcePsn();
		String relativePath = getRelativePath(rsPsn);
		if (!relativePath.equals("")) {
			fullPath += "/" + relativePath;
		}
		fullPath += "/" + "skins";
		int pos = destFileName.lastIndexOf("/");
		String destPath = "";
		String destFile = "";
		if (pos > -1) {
			destPath = destFileName.substring(0, pos);
			destFile = destFileName.substring(pos + 1);
		} else {
			destFile = destFileName;
		}
		if (!destPath.equals("")) {
			fullPath += "/" + destPath;
		}
		fullPath = StringUtil.normalizePath(fullPath);
		File destDir = new File(fullPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		File dest = new File(destDir, destFile);
		return dest;
	}

	protected String getDestSkinUrl(Node node) {
		String destResourceUrl = "";
		String rsUrl = node.getResourceUrl();
		String sp = "\\{PSN-URL:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
		Pattern p = Pattern.compile(sp);
		Matcher m = p.matcher(rsUrl);
		boolean result = m.find();
		StringBuffer sb = new StringBuffer();
		while (result) {
			String path = m.group(2);
			String psnId = m.group(1);
			Psn psn = psnManager.getPsnById(new Long(psnId));
			String psnUrl = psn.getUrl();
			m.appendReplacement(sb, psnUrl + path);
			result = m.find();
		} // end while result
		m.appendTail(sb);
		destResourceUrl = sb.toString();
		return destResourceUrl;
	}

	/**
	 * 刷新首页动作 1)get the nodeIndex template 2)generate the node index file and
	 * save.
	 * 
	 * @param nodeId
	 *            Integer
	 * @param errors
	 *            List
	 * @return boolean
	 */
	public synchronized boolean generateNodeIndexStaticFile(Long nodeId,
			List errors) {
		boolean success = false;
		Node node = nodeManager.getNodeById(nodeId);
		Integer publishMode = node.getPublishMode();
		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			// 如果是静态发布模式
			String indexTpl = node.getIndexTpl();
			//
			// fix bug:node.getNodeUrl() should be node.getIndexPortalUrl();
			String url = psnManager.getPsnUrlInfo(node.getContentUrl());
			String indexFileName = node.getIndexName();
			indexFileName = indexFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());
			url = url + "/" + indexFileName;
			// $Date: 2006/08/31 02:25:57 $ by Weiping Ju
			// add function,if the url has change,should update the nodeUrl
			// value
			String nodeUrl = node.getNodeUrl();
			if (nodeUrl == null || !nodeUrl.equals(url)) {
				node.setNodeUrl(url);
				nodeManager.saveNode(node);
			}
			//
			success = generateNodeIndexStaticFile(indexTpl, url, node, errors);
		}
		return success;
	}

	/**
	 * 附加发布
	 * 
	 * @todo 需要添加附件发布的模式,允许静动态
	 * @param nodeId
	 *            Long
	 * @param errors
	 *            List
	 * @return boolean
	 */
	public boolean generateNodeAllExtraIndexStaticFile(Long nodeId, List errors) {
		boolean success = true;
		List publishes = extraPublishManager.getPublishes(nodeId);
		if (publishes != null) {
			for (int i = 0; i < publishes.size(); i++) {
				success = generateNodeExtraIndexStaticFile(nodeId,
						(ExtraPublish) publishes.get(i), errors);
			}
		}
		return success;
	}

	/**
	 * generate the node extra index static file.
	 * 
	 * @param nodeId
	 *            Integer
	 * @param publishId
	 *            Integer
	 * @param errors
	 *            List
	 * @return boolean
	 */
	public synchronized boolean generateNodeExtraIndexStaticFile(Long nodeId,
			Long publishId, List errors) {
		boolean success = false;
		Node node = nodeManager.getNodeById(nodeId);
		ExtraPublish publish = extraPublishManager.getPublishById(publishId);
		Integer publishMode = node.getPublishMode();
		Integer selfPublishMode = publish.getPublishMode();
		if (selfPublishMode != null && !selfPublishMode.equals(new Integer(-1))) {
			publishMode = selfPublishMode;
		}

		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			String tpl = publish.getTpl();
			//
			String psnUrl = publish.getSelfPsnUrl();
			if (psnUrl == null) {
				// 如果自定义psnUrl
				psnUrl = node.getContentPsn();
			}
			//
			String url = psnManager.getPsnUrlInfo(psnUrl);
			//
			String publishFileName = publish.getPublishFileName();
			publishFileName = publishFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());

			publishFileName = publishFileName.replaceAll("\\{PublishID\\}",
					publish.getPublishId().toString());
			url = url + "/" + publishFileName;

			//
			success = generateNodeExtraIndexStaticFile(tpl, url, node, publish,
					errors);
		}
		return success;
	}

	/**
	 * 附加发布更新
	 * 
	 * @param nodeId
	 *            Long
	 * @param publish
	 *            ExtraPublish
	 * @param errors
	 *            List
	 * @return boolean
	 */
	public synchronized boolean generateNodeExtraIndexStaticFile(Long nodeId,
			ExtraPublish publish, List errors) {
		boolean success = false;
		Node node = nodeManager.getNodeById(nodeId);
		// ExtraPublish publish = extraPublishManager.getPublishById(publishId);
		Integer publishMode = node.getPublishMode();
		Integer selfPublishMode = publish.getPublishMode();
		if (selfPublishMode != null && !selfPublishMode.equals(new Integer(-1))) {
			publishMode = selfPublishMode;
		}
		if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
			// 只处理静态发布模式
			String tpl = publish.getTpl();

			//
			String psnUrl = publish.getSelfPsnUrl();
			//
			if (psnUrl == null) {
				// 如果自定义psnUrl
				psnUrl = node.getContentPsn();
			}

			//
			String url = psnManager.getPsnUrlInfo(psnUrl);
			String publishFileName = publish.getPublishFileName();
			publishFileName = publishFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());

			publishFileName = publishFileName.replaceAll("\\{PublishID\\}",
					publish.getPublishId().toString());
			url = url + "/" + publishFileName;
			//
			success = generateNodeExtraIndexStaticFile(tpl, url, node, publish,
					errors);
		}
		return success;
	}

	/**
	 * 产生
	 * 
	 * @param tplName
	 *            String
	 * @param url
	 *            String
	 * @param node
	 *            Node
	 * @param publish
	 *            ExtraPublish
	 * @param errors
	 *            List
	 * @return boolean
	 */
	protected synchronized boolean generateNodeExtraIndexStaticFile(
			String tplName, String url, Node node, ExtraPublish publish,
			List errors) {

		boolean success = true;
		try {
			Configuration newConfiguration = freemarkerConfigurer
					.createConfiguration();
			String userTplPath = this.getConfig().getUserTemplatePath();
			userTplPath = StringUtil.normalizePath(userTplPath);
			// set the newConfiguration property.
			settingConfiguration(newConfiguration, userTplPath);

			// 1)get the template file
			String fullTplPath = userTplPath + "/" + tplName;
			fullTplPath = StringUtil.normalizePath(fullTplPath);
			File tplFile = new File(fullTplPath);
			System.out.println("fullTplPath is:" + fullTplPath);
			//
			String encoding = getConfig().getStringProperty("sys.tpl.encoding",
					"UTF-8");
			//
			if (tplFile.exists() && tplFile.isFile()) {
				// get the tpl text content
				String tplContent = FileUtil.readTextFile(tplFile, encoding);
				//
				tplContent = preProcessContent(tplContent);
				// check if need process
				// get the multiMacro
				String multiMacro = getMultiPageMacro2(tplContent);
				if (multiMacro != null) {
					PageBuilder pi = getPageInfoFromMacro2(multiMacro, node
							.getNodeId().toString());
					if (pi.pages() > 1) {
						// should process
						for (int i = 0; i < pi.pages(); i++) {
							String mytplContent = replaceMultiPageMacro2(
									tplContent, url, String.valueOf(i + 1));
							// process the skin file refrence
							String new_mytplContent = processSkinFile2(
									mytplContent, node, errors);
							File destFile = getDestNodeExtraFile(node, publish,
									getConfig(), String.valueOf(i + 1));
							//
							success = generatePageStaticFile(new_mytplContent,
									String.valueOf(i + 1), url, destFile,
									newConfiguration, node, null, errors);
						}
					} else {
						tplContent = replaceAllPagerMacro2(tplContent, url, "1");
						// process the skin file refrence
						String new_tplContent = processSkinFile2(tplContent,
								node, errors);
						File destFile = getDestNodeExtraFile(node, publish,
								getConfig(), "1");

						success = generatePageStaticFile(new_tplContent, "1",
								url, destFile, newConfiguration, node, null,
								errors);
					}
				} else {
					tplContent = replaceAllPagerMacro2(tplContent, url, "1");
					// process the skin file refrence
					String new_tplContent = processSkinFile2(tplContent, node,
							errors);
					File destFile = getDestNodeExtraFile(node, publish,
							getConfig(), "1");
					success = generatePageStaticFile(new_tplContent, "1", url,
							destFile, newConfiguration, node, null, errors);
				}
				//
			}
		} catch (Exception ex1) {
			ex1.printStackTrace();
			errors.add(ex1);
			success = false;
		}
		return success;
	}

	/**
	 * for freeMarker to use an alternative syntax should put [#tpl] to the
	 * content at the very first thing in the file
	 * 
	 * @param tplContent
	 *            模板内容
	 * @return String
	 */
	protected String preProcessContent(String tplContent) {
		String mytplContent = "[#ftl]\n" + tplContent;
		return mytplContent;
	}

	private void settingConfiguration(Configuration newConfiguration,
			String userTplPath) throws IOException {
		// 1,get the dest template file name.
		String rootPath = this.getConfig().getTemplatePath();

		String sys_prefix = "/systems/";
		String tmp_prefix = "/tmp/";
		// String users_prefix = "/users/";
		String sysTplPath = rootPath;
		sysTplPath += sys_prefix;
		sysTplPath = StringUtil.normalizePath(sysTplPath);
		File sysTplDir = new File(sysTplPath);
		if (!sysTplDir.exists()) {
			sysTplDir.mkdirs();
		}
		// 临时模板路径
		String tmpTplPath = rootPath + tmp_prefix;
		tmpTplPath = StringUtil.normalizePath(tmpTplPath);
		File tmpTplDir = new File(tmpTplPath);
		if (!tmpTplDir.exists()) {
			tmpTplDir.mkdirs();
		}
		//
		File userTplDir = new File(userTplPath);
		if (!userTplDir.exists()) {
			userTplDir.mkdirs();
		}
		//
		FileTemplateLoader ftl1 = new FileTemplateLoader(sysTplDir);

		FileTemplateLoader ftl2 = new FileTemplateLoader(userTplDir);

		FileTemplateLoader ftl3 = new FileTemplateLoader(tmpTplDir);
		//

		//
		TemplateLoader[] loaders = new TemplateLoader[] {
				this.freemarkerConfigurer.getTemplateClassLoader(),
				new BaseClassTemplateLoader(), ftl1, ftl2, ftl3 };
		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
		newConfiguration.setTemplateLoader(mtl);
		newConfiguration.removeAutoImport("cms");
		// @todo this may be config
		//
		BaseApplicationConfiguration baseConfig = BaseConfigUtil.getConfig();
		String encoding = baseConfig.getString("sys.tpl.encoding", "UTF-8");
		// System.out.println("encoding2="+encoding);
		if (encoding.equalsIgnoreCase("gbk")) {
			newConfiguration.addAutoImport("cms",
					"/plugin/cms/base/macros/cms-macros-gbk.html");
		} else {
			newConfiguration.addAutoImport("cms",
					"/plugin/cms/base/macros/cms-macros.html");
		}
		//
		newConfiguration.addAutoImport("util",
				"/plugin/cms/base/macros/util-macros.html");
		// newConfiguration.addAutoImport("forum", "/macros/forum-macros.html");
		//
		Set keySet = this.macroMap.keySet();
		if (keySet.size() > 0) {
			Iterator keyIterator = keySet.iterator();
			while (keyIterator.hasNext()) {
				String key = (String) keyIterator.next();
				String value = (String) macroMap.get(key);
				value = value.trim();
				newConfiguration.addAutoImport(key, value);

			}
		}

		// 设置系统输入模板编码与内容输出编码
		// 系统可以设置模版的编码，这样可以适用于不同的模版编码
		// 内容输出的编码与模板编码相同
		// 在在线模版编辑器内将采用编码转换统一使用UTF-8来编辑模版
		newConfiguration.setDefaultEncoding(encoding);
		newConfiguration.setEncoding(Locale.getDefault(), encoding);
		newConfiguration.setEncoding(Locale.SIMPLIFIED_CHINESE, encoding);
		newConfiguration.setOutputEncoding(encoding);
	}

	private String getContentUrl(ContentIndex contentIndex, Node node) {
		String url = "";
		// first look up the self url,if exist,it is,otherwise
		// from the node info,get the url info
		// after get the url,need get the file name
		// first get the self filename,if not exist,need get from the node info
		// from node info,include the path name and file name
		// cat the url and the file name is the finla url
		String selfUrl = contentIndex.getSelfUrl();
		String selfFileName = contentIndex.getSelfPublishFileName();
		long timeStamp = contentIndex.getPublishDate().longValue();
		if (StringUtils.hasText(selfUrl)) {
			// get from self url
			url = selfUrl;
			return url;
		} else {
			// get from node info,psn url defintion.
			String nodeUrl = node.getContentUrl();
			url = psnManager.getPsnUrlInfo(nodeUrl);
		}
		if (StringUtils.hasText(selfFileName)) {
			url += "/" + selfFileName;
		} else {
			String subDir = node.getSubDir();
			String destDir = this.getSubDirName(subDir, timeStamp);
			if (!destDir.equals("")) {
				url += "/" + destDir;
			}
			//
			// String stimeStamp = "" + timeStamp;
			String fileName = node.getPublishFileFormat();
			//
			fileName = getPublishFileName(fileName, timeStamp, contentIndex);
			if (!fileName.equals("")) {
				url += "/" + fileName;
			}
		}
		return url;
	}

	public String getSubDirName(String subDir, long timeStamp) {
		String destDir = "";
		if (!subDir.equals("")) {
			String ft = "yyyy-MM-dd";
			if (!subDir.equals("auto")) {
				subDir = subDir.replaceAll("Y", "yyyy");
				subDir = subDir.replaceAll("m", "MM");
				subDir = subDir.replaceAll("d", "dd");
				ft = subDir;
			}
			SimpleDateFormat sf = new SimpleDateFormat(ft);
			destDir = sf.format(new Date(timeStamp));
		}
		return destDir;
	}

	public String getPublishFileName(String fileName, long timeStamp,
			ContentIndex ci) {
		String destFileName = fileName;
		destFileName = destFileName.replaceAll("\\{TimeStamp\\}", ""
				+ timeStamp);
		destFileName = destFileName.replaceAll("\\{ContentID\\}", ci
				.getContentId().toString());

		destFileName = destFileName.replaceAll("\\{IndexID\\}", ci.getIndexId()
				.toString());
		destFileName = destFileName.replaceAll("\\{NodeID\\}", ci.getNodeId()
				.toString());
		return destFileName;
	}

	// ////////////////////////////////////////////////////////////////////////////
	private File getResourceDestFile(String sysRootDir, String destFileName,
			Node node, List errors) {
		String fullPath = sysRootDir;
		String rsPsn = node.getResourcePsn();
		String relativePath = getRelativePath(rsPsn);
		if (!relativePath.equals("")) {
			fullPath += "/" + relativePath;
		}
		int pos = destFileName.lastIndexOf("/");
		String destPath = "";
		String destFile = "";
		if (pos > -1) {
			destPath = destFileName.substring(0, pos);
			destFile = destFileName.substring(pos + 1);
		} else {
			destFile = destFileName;
		}
		if (!destPath.equals("")) {
			fullPath += "/" + destPath;
		}
		fullPath = StringUtil.normalizePath(fullPath);
		File destDir = new File(fullPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		File dest = new File(destDir, destFile);
		return dest;
	}

	private boolean shouldCopyFile(File srcFile, File destFile) {
		if (srcFile.exists()) {
			if (!destFile.exists()
					|| (destFile.exists() && (destFile.lastModified() < srcFile
							.lastModified() || destFile.length() != srcFile
							.length()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 模板预览
	 * 
	 * @param nodeId
	 *            Long
	 * @param indexId
	 *            Long
	 * @param tplContent
	 *            String
	 * @param extraPublishId
	 *            Long
	 * @param type
	 *            int
	 * @param page
	 *            int
	 * @param errors
	 *            List
	 * @return String
	 */
	public String previewTemplate(Long nodeId, Long indexId, String tplContent,
			Long extraPublishId, int type, int page, List errors) {
		//
		try {
			if (type == 0) {
				// it will be a node index template
				Node node = nodeManager.getNodeById(nodeId);
				Integer publishMode = node.getPublishMode();
				if (publishMode.equals(PublishMode.STATIC_MODE.getMode())) {
					// String indexTpl = node.getIndexTpl();
					//
					String url = psnManager.getPsnUrlInfo(node.getContentUrl());
					String indexFileName = node.getIndexName();
					indexFileName = indexFileName.replaceAll("\\{NodeID\\}",
							node.getNodeId().toString());
					url = url + "/" + indexFileName;
					// $Date: 2006/08/31 02:25:57 $ by Weiping Ju
					// add function,if the url has change,should update the
					// nodeUrl value
					String nodeUrl = node.getNodeUrl();
					if (nodeUrl == null || !nodeUrl.equals(url)) {
						node.setNodeUrl(url);
						nodeManager.saveNode(node);
					}
					//
					return generateStaticContent(tplContent, url, node, null,
							page, errors);
				}
			} else if (type == 1) {
				ContentIndex ci = dynamicContentManager
						.getContentIndexById(indexId);
				if (ci != null) {
					Node node = nodeManager.getNodeById(ci.getNodeId());
					// get the psn url info,it will be used to some url link
					String url = ci.getUrl();
					String okUrl = getContentUrl(ci, node);
					if (okUrl != null && !okUrl.equals(url)) {
						url = okUrl;
						ci.setUrl(okUrl);
						dynamicContentManager.saveContentIndex(ci);
					}
					//
					return generateStaticContent(tplContent, url, node, ci,
							page, errors);
				}
			} else if (type == 2) {
				ExtraPublish publish = extraPublishManager
						.getPublishById(extraPublishId);
				if (publish != null) {
					Node node = nodeManager.getNodeById(publish.getNodeId());
					String psnUrl = publish.getSelfPsnUrl();
					//
					String url = psnManager.getPsnUrlInfo(psnUrl);
					//
					String publishFileName = publish.getPublishFileName();
					publishFileName = publishFileName.replaceAll(
							"\\{NodeID\\}", node.getNodeId().toString());

					publishFileName = publishFileName.replaceAll(
							"\\{PublishID\\}", publish.getPublishId()
									.toString());
					url = url + "/" + publishFileName;
					return generateStaticContent(tplContent, url, node, null,
							page, errors);
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		//
		return "";
	}

	/**
	 * 根据给定的模板内容产生页面内容
	 * 
	 * @param tplContent
	 * 
	 * @param url
	 * 
	 * @param node
	 * 
	 * @param ci
	 * 
	 * @param page
	 * 
	 * @param errors
	 * 
	 * @return
	 */
	protected synchronized String generateStaticContent(String tplContent,
			String url, Node node, ContentIndex ci, int page, List errors) {
		//
		String exportContent = "";
		//
		try {
			Configuration newConfiguration = freemarkerConfigurer
					.createConfiguration();
			String userTplPath = this.getConfig().getUserTemplatePath();
			userTplPath = StringUtil.normalizePath(userTplPath);
			// set the newConfiguration property.
			settingConfiguration(newConfiguration, userTplPath);

			//
			tplContent = this.preProcessContent(tplContent);
			// check if need process
			// get the multiMacro
			String multiMacro = getMultiPageMacro2(tplContent);
			if (multiMacro != null) {
				PageBuilder pi = getPageInfoFromMacro2(multiMacro, node
						.getNodeId().toString());
				if (pi.pages() > 1) {
					// should process
					for (int i = 0; i < pi.pages(); i++) {
						String mytplContent = replaceMultiPageMacro2(
								tplContent, url, String.valueOf(i + 1));
						// process the skin file refrence
						String new_mytplContent = processSkinFile2(
								mytplContent, node, errors);
						//
						//
						exportContent = generateStaticContent(new_mytplContent,
								String.valueOf(i + 1), url, newConfiguration,
								node, null, pi, errors);
					}
				} else {
					tplContent = replaceAllPagerMacro2(tplContent, url, "1");
					// process the skin file refrence
					String new_tplContent = processSkinFile2(tplContent, node,
							errors);
					//

					exportContent = generateStaticContent(new_tplContent, "1",
							url, newConfiguration, node, null, pi, errors);
				}
			} else {
				String mytplContent = replaceAllPagerMacro2(tplContent, url,
						"1");
				// process the skin file refrence
				String new_tplContent = processSkinFile2(mytplContent, node,
						errors);
				//
				exportContent = generateStaticContent(new_tplContent, "1", url,
						newConfiguration, node, null, null, errors);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
		}
		return exportContent;
	}

	/**
	 * 返回产生的文件内容, 1)不处理引用资源发布 2)分页的处理将采用动态的url
	 * 
	 * @param tplContent
	 *            String
	 * @param page
	 *            String
	 * @param url
	 *            String
	 * @param configuration
	 *            Configuration
	 * @param node
	 *            Node
	 * @param ci
	 *            ContentIndex
	 * @param pb
	 *            PageInfo
	 * @param errors
	 *            List
	 * @return String
	 */
	private String generateStaticContent(String tplContent, String page,
			String url, Configuration configuration, Node node,
			ContentIndex ci, PageBuilder pb, List errors) {
		File tmpTplFile = null;
		try {
			//
			String tmp_prefix = "/tmp/";
			String rootPath = this.getConfig().getTemplatePath();

			String tmpTplFileName = "/~" + System.currentTimeMillis() + ".html";
			String tmpTplPath = rootPath + tmp_prefix + tmpTplFileName;
			tmpTplPath = StringUtil.normalizePath(tmpTplPath);
			tmpTplFile = new File(tmpTplPath);
			String encoding = getTemplateEncoding();
			FileUtil.writeTextFile(tmpTplFile, tplContent, encoding);

			Template template = configuration.getTemplate(tmpTplFileName);
			//
			String baseUrl = CMSConfig.getInstance().getBaseUrl();
			// @todo 放入的变量内容，要扩展成后台可维护的
			Map model = new HashMap();
			ConstantLoader.Constant lang = ConstantLoader
					.load("CMSInternalLanguage.xml");
			if (lang != null) {
				model.put("lang", lang);
			}
			model.put("currentNodeId", node.getNodeId());
			model.put("currentIndexId", ci == null ? new Long(0) : ci
					.getIndexId());
			model.put("cmsMacroEngine", cmsMacroEngine);
			// @todo 放入模型对象要做成插件模式
			Set keySet = this.macroServiceMap.keySet();
			if (keySet.size() > 0) {
				Iterator keyIterator = keySet.iterator();
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					model.put(key, macroServiceMap.get(key));

				}
			}

			model.put("currentPage", String.valueOf(page));
			model.put("currentUrl", url);
			model.put("baseUrl", baseUrl);
			//
			model.put("memberPresenceUrl", CMSConfig.getInstance()
					.getMemberPresenceUrl());
			model.put("memberPresenceImagesUrl", CMSConfig.getInstance()
					.getMemberPresenceImagesUrl());
			model
					.put("imServerName", CMSConfig.getInstance()
							.getImServerName());
			//
			model.put("SKIN_PATH", "");
			//
			model.put("psn", psnManager.getPsn(node.getContentUrl()));
			//
			model.put("pageInfo", pb);
			//
			String fileContent = FreeMarkerTemplateUtils
					.processTemplateIntoString(template, model);
			//
			fileContent = "<!--#config ERRMSG=\" \" -->\n" + fileContent;
			// destFile = getDestFile(node, ci, getConfig(), page);
			if (tmpTplFile.exists()) {
				tmpTplFile.delete();
			}
			return fileContent;
		} catch (Exception ex) {
			// ex.printStackTrace();
			errors.add(ex);
			return "";
		} finally {
			// delete the temp tpl file
			if (tmpTplFile != null && tmpTplFile.exists()) {
				tmpTplFile.delete();
			}
		}
	}

	protected void postFreeMarkerConfig() {

	}

	protected String getTemplateEncoding() {
		return getConfig().getStringProperty("sys.tpl.encoding", "UTF-8");
	}

	protected String getTemplateOutEncoding() {
		return getConfig().getStringProperty("sys.tpl.out_encoding", "UTF-8");
	}

	public String getContent(Long nodeId, Long indexId, int page, List errors) {
		String result = "";

		try {
			// 获得内容所在的结点
			Node node = nodeManager.getNodeById(nodeId);
			if (node == null) {
				return "";
			}
			// 获得内容索引
			ContentIndex ci = dynamicContentManager
					.getContentIndexById(indexId);
			if (ci == null) {
				return "";
			}
			String tpl = null;
			String url = null;
			// get the content template file name
			String selfTpl = ci.getSelfTemplate();
			if (StringUtils.hasText(selfTpl)) {
				tpl = selfTpl;
			} else {
				// get from the node info
				String conentTpl = node.getContentTpl();
				tpl = conentTpl;
			}
			// get the psn url info,it will be used to some url link
			url = ci.getUrl();

			// 产生静态内容
			result = generateContent(tpl, url, node, ci, page, errors);
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			result = "";
		}
		return result;

	}

	public String getNodeIndex(Long nodeId, int page, List errors) {
		String result = "";

		try {
			// 获得内容所在的结点
			Node node = nodeManager.getNodeById(nodeId);
			if (node == null) {
				return "";
			}
			// 获得内容索引

			String tpl = null;
			String url = null;
			// get the content template file name
			String indexTpl = node.getIndexTpl();
			if (StringUtils.hasText(indexTpl)) {
				tpl = indexTpl;
			}
			// get the psn url info,it will be used to some url link
			url = node.getIndexPortalUrl();

			url = url.replaceAll("\\{NodeID\\}", node.getNodeId().toString());

			//
			String baseUrl = CMSConfig.getInstance().getBaseUrl();
			if (baseUrl.endsWith("/")) {
				baseUrl.substring(0, baseUrl.length() - 1);
			}
			//
			if (!url.startsWith("http")) {
				url = baseUrl + "/" + url;
			}
			//
			// 产生静态内容
			result = generateNodeIndex(tpl, url, node, page, errors);
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			result = "";
		}
		return result;
	}

	public String getExtraContent(Long nodeId, Long publishId, int page,
			List errors) {
		String result = "";

		try {
			// 获得内容所在的结点
			Node node = nodeManager.getNodeById(nodeId);
			if (node == null) {
				return "";
			}
			// 获得内容索引
			ExtraPublish publish = extraPublishManager
					.getPublishById(publishId);
			if (publish == null) {
				return "";
			}
			String tpl = null;
			String url = null;
			// get the content template file name
			String selfTpl = publish.getTpl();
			if (StringUtils.hasText(selfTpl)) {
				tpl = selfTpl;
			} else {
				// get from the node info
				String indexTpl = node.getIndexTpl();
				tpl = indexTpl;
			}
			// get the psn url info,it will be used to some url link
			url = node.getExtraPortalUrl();
			String selfUrl = publish.getExtraPortalUrl();
			if (selfUrl != null && !selfUrl.equals("")) {
				url = selfUrl;
			}
			url = url
					.replaceAll("\\{PublishID\\}", node.getNodeId().toString());
			url = url.replaceAll("\\{NodeID\\}", node.getNodeId().toString());
			// 产生静态内容
			result = generateExtra(tpl, url, node, publish, page, errors);
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			result = "";
		}
		return result;

	}

	/**
	 * 产生内容
	 * 
	 * @param tplName
	 *            String
	 * @param url
	 *            String
	 * @param node
	 *            Node
	 * @param ci
	 *            ContentIndex
	 * @param page
	 *            int
	 * @param errors
	 *            List
	 * @return 模版处理后的内容
	 */
	protected synchronized String generateContent(String tplName, String url,
			Node node, ContentIndex ci, int page, List errors) {
		String result = "";
		try {
			// create a new freemarker configuration object
			Configuration newConfiguration = freemarkerConfigurer
					.createConfiguration();
			// 获得用户模板路径
			String userTplPath = this.getConfig().getUserTemplatePath();
			userTplPath = StringUtil.normalizePath(userTplPath);
			// set the newConfiguration property.
			settingConfiguration(newConfiguration, userTplPath);

			// 1)get the template file
			String fullTplPath = userTplPath + "/" + tplName;
			fullTplPath = StringUtil.normalizePath(fullTplPath);
			File tplFile = new File(fullTplPath);
			String encoding = getTemplateEncoding();
			// read the template file content
			if (tplFile.exists() && tplFile.isFile()) {
				// 读取模板内容
				String tplContent = FileUtil.readTextFile(tplFile, encoding);
				// prefore process the tplContent,add [#ftl] tag
				tplContent = this.preProcessContent(tplContent);
				// check if need process
				// get the multiMacro,to process some pager info
				String multiMacro = getMultiPageMacro2(tplContent);
				if (multiMacro != null) {
					// 获得页码信息
					PageBuilder pi = getPageInfoFromMacro2(multiMacro, node
							.getNodeId().toString());

					if (pi.pages() > 1) {
						if (page < 1) {
							page = 1;
						}
						if (page > pi.pages()) {
							page = (int) pi.pages();
						}
						// should process

						String mytplContent = replaceMultiPageMacro2(
								tplContent, url, String.valueOf(page));
						// process the skin file refrence
						//
						result = generatePageContent(mytplContent, String
								.valueOf(page), url, newConfiguration, node,
								ci, null, errors);

					} else {
						tplContent = replaceAllPagerMacro2(tplContent, url, "1");
						//
						result = generatePageContent(tplContent, "1", url,
								newConfiguration, node, ci, null, errors);
					}
				} else {
					// 无分页
					tplContent = replaceAllPagerMacro2(tplContent, url, "1");

					result = generatePageContent(tplContent, "1", url,
							newConfiguration, node, ci, null, errors);
				}
				//
			}
		} catch (Exception ex1) {
			ex1.printStackTrace();
			errors.add(ex1);
			result = "";
		}
		return result;

	}

	protected synchronized String generateExtra(String tplName, String url,
			Node node, ExtraPublish publish, int page, List errors) {
		String result = "";
		try {
			// create a new freemarker configuration object
			Configuration newConfiguration = freemarkerConfigurer
					.createConfiguration();
			// 获得用户模板路径
			String userTplPath = this.getConfig().getUserTemplatePath();
			userTplPath = StringUtil.normalizePath(userTplPath);
			// set the newConfiguration property.
			settingConfiguration(newConfiguration, userTplPath);

			// 1)get the template file
			String fullTplPath = userTplPath + "/" + tplName;
			fullTplPath = StringUtil.normalizePath(fullTplPath);
			File tplFile = new File(fullTplPath);
			String encoding = getTemplateEncoding();
			// read the template file content
			if (tplFile.exists() && tplFile.isFile()) {
				// 读取模板内容
				String tplContent = FileUtil.readTextFile(tplFile, encoding);
				// prefore process the tplContent,add [#ftl] tag
				tplContent = this.preProcessContent(tplContent);
				// check if need process
				// get the multiMacro,to process some pager info
				String multiMacro = getMultiPageMacro2(tplContent);
				if (multiMacro != null) {
					// 获得页码信息
					PageBuilder pi = getPageInfoFromMacro2(multiMacro, node
							.getNodeId().toString());

					if (pi.pages() > 1) {
						if (page < 1) {
							page = 1;
						}
						if (page > pi.pages()) {
							page = (int) pi.pages();
						}
						// should process

						String mytplContent = replaceMultiPageMacro2(
								tplContent, url, String.valueOf(page));
						// process the skin file refrence
						//
						result = generatePageContent(mytplContent, String
								.valueOf(page), url, newConfiguration, node,
								null, publish, errors);

					} else {
						tplContent = replaceAllPagerMacro2(tplContent, url, "1");
						//
						result = generatePageContent(tplContent, "1", url,
								newConfiguration, node, null, publish, errors);
					}
				} else {
					// 无分页
					tplContent = replaceAllPagerMacro2(tplContent, url, "1");

					result = generatePageContent(tplContent, "1", url,
							newConfiguration, node, null, publish, errors);
				}
				//
			}
		} catch (Exception ex1) {
			ex1.printStackTrace();
			errors.add(ex1);
			result = "";
		}
		return result;

	}

	protected synchronized String generateNodeIndex(String tplName, String url,
			Node node, int page, List errors) {
		String result = "";
		try {
			Configuration newConfiguration = freemarkerConfigurer
					.createConfiguration();
			String userTplPath = this.getConfig().getUserTemplatePath();
			userTplPath = StringUtil.normalizePath(userTplPath);
			// set the newConfiguration property.
			settingConfiguration(newConfiguration, userTplPath);
			// 1)get the template file
			String fullTplPath = userTplPath + "/" + tplName;
			fullTplPath = StringUtil.normalizePath(fullTplPath);
			File tplFile = new File(fullTplPath);
			String encoding = getConfig().getStringProperty("sys.tpl.encoding",
					"UTF-8");
			if (tplFile.exists() && tplFile.isFile()) {
				String tplContent = FileUtil.readTextFile(tplFile, encoding);
				//
				tplContent = this.preProcessContent(tplContent);
				// check if need process
				// get the multiMacro
				String multiMacro = getMultiPageMacro2(tplContent);
				if (multiMacro != null) {
					PageBuilder pi = getPageInfoFromMacro2(multiMacro, node
							.getNodeId().toString());
					if (pi.pages() > 1) {
						if (page < 1) {
							page = 1;
						}
						if (page > pi.pages()) {
							page = (int) pi.pages();
						}

						tplContent = replaceAllPagerMacro2(tplContent, url,
								String.valueOf(page));
						// process the skin file refrence

						result = generatePageContent(tplContent, String
								.valueOf(page), url, newConfiguration, node,
								null, null, errors);
					}
				} else {
					String mytplContent = replaceAllPagerMacro2(tplContent,
							url, "1");
					// process the skin file refrence

					result = generatePageContent(mytplContent, "1", url,
							newConfiguration, node, null, null, errors);
				}
				//
			}
		} catch (Exception ex1) {
			ex1.printStackTrace();
			errors.add(ex1);
		}
		return result;

	}

	/**
	 * 产生页面内容
	 * 
	 * @param tplContent
	 *            模版内容
	 * @param page
	 *            当前页码
	 * @param url
	 *            页面url
	 * @param configuration
	 *            FreeMarker配置对象
	 * @param node
	 *            结点
	 * @param ci
	 *            内容索引
	 * @param publish
	 *            ExtraPublish
	 * @param errors
	 *            错误列表
	 * @return 产生的内容
	 */
	protected synchronized String generatePageContent(String tplContent,
			String page, String url, Configuration configuration, Node node,
			ContentIndex ci, ExtraPublish publish, List errors) {
		File tmpTplFile = null;
		try {
			//
			// @todo 更改生成临时模板文件的方法，直接用字符串处理
			String tmp_prefix = "/tmp/";
			String rootPath = this.getConfig().getTemplatePath();

			String tmpTplFileName = "/~" + System.currentTimeMillis() + ".html";
			String tmpTplPath = rootPath + tmp_prefix + tmpTplFileName;
			tmpTplPath = StringUtil.normalizePath(tmpTplPath);
			tmpTplFile = new File(tmpTplPath);
			String encoding = getTemplateEncoding();
			FileUtil.writeTextFile(tmpTplFile, tplContent, encoding);
			//

			Template template = configuration.getTemplate(tmpTplFileName);
			//
			//
			String baseUrl = CMSConfig.getInstance().getBaseUrl();
			//
			Map model = new HashMap();
			model.put("currentNodeId", node.getNodeId());
			model.put("currentIndexId", ci == null ? new Long(0) : ci
					.getIndexId());
			model.put("currentPublishId", publish == null ? new Long(0)
					: publish.getPublishId());
			// 宏服务定义注入模型之中
			model.put("cmsMacroEngine", cmsMacroEngine);
			//
			Set keySet = this.macroServiceMap.keySet();
			if (keySet.size() > 0) {
				Iterator keyIterator = keySet.iterator();
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					model.put(key, macroServiceMap.get(key));

				}
			}
			//
			model.put("currentPage", String.valueOf(page));
			model.put("currentUrl", url);
			model.put("baseUrl", baseUrl);
			model.put("memberPresenceUrl", CMSConfig.getInstance()
					.getMemberPresenceUrl());
			model.put("memberPresenceImagesUrl", CMSConfig.getInstance()
					.getMemberPresenceImagesUrl());
			model
					.put("imServerName", CMSConfig.getInstance()
							.getImServerName());

			//
			model.put("SKIN_PATH", "");
			//
			Psn psn = new Psn();
			psn.setUrl(baseUrl);
			model.put("psn", psn);

			String fileContent = FreeMarkerTemplateUtils
					.processTemplateIntoString(template, model);
			//
			fileContent = "<!--#config ERRMSG=\" \" -->\n" + fileContent;
			//
			if (tmpTplFile.exists()) {
				tmpTplFile.delete();
			}
			return fileContent;
		} catch (Exception ex) {
			ex.printStackTrace();
			errors.add(ex);
			return "";
		} finally {
			// delete the temp tpl file
			if (tmpTplFile != null && tmpTplFile.exists()) {
				tmpTplFile.delete();
			}
		}
	}

	public Map getMacroServiceMap() {
		return macroServiceMap;
	}

	/**
	 * 按照类类型得到宏服务定义
	 * 
	 * @throws Exception
	 */
	public void afterPropertiesSet() throws Exception {
		Map beans = applicationContext.getBeansOfType(MapService.class, false,
				false);
		if (beans != null) {
			Iterator beanIt = beans.values().iterator();
			while (beanIt.hasNext()) {
				MapService mapService = (MapService) beanIt.next();
				Map servieMap = mapService.getMapService();
				if (servieMap != null) {
					macroServiceMap.putAll(servieMap);
				}
			}
		}
		Map macroBeans = applicationContext.getBeansOfType(
				MacroDefineService.class, false, false);
		if (macroBeans != null) {
			Iterator beanIt = macroBeans.values().iterator();
			while (beanIt.hasNext()) {
				MacroDefineService macroService = (MacroDefineService) beanIt
						.next();
				Map macroServieMap = macroService.getMacroService();
				if (macroServieMap != null) {
					macroMap.putAll(macroServieMap);
				}
			}

		}
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 
	 * @param destFile
	 * 
	 * @param content
	 *            String
	 * @param encoding
	 *            String
	 * @param configuration
	 *            Configuration
	 * @param node
	 *            Node
	 * @param ci
	 *            ContentIndex
	 * @param url
	 *            String
	 * @param errors
	 *            List
	 * @throws Exception
	 */
	protected void postConentProcess(File destFile, String content,
			String encoding, Configuration configuration, Node node,
			ContentIndex ci, String url, List errors) throws Exception {
		String pattern = "<cmsPager([\\s\\p{Print}&&[^>]]*)>([\\s\\p{Print}[^\\x00-\\xff]]*)?</cmsPager>";
		Pattern p = Pattern.compile(pattern);
		String fileName = destFile.getPath();
		Matcher m = p.matcher(content);

		boolean result = m.find();
		int page = 0;
		int replace_start = 0;
		int replace_end = 0;

		if (result) {
			String parameter = m.group(1);
			String target = m.group(2);
			// System.out.println("m.group(2) "+target);
			//
			replace_start = m.start();
			replace_end = m.end();
			String beforeContent = content.substring(0, replace_start);
			String afterContent = content.substring(replace_end);
			//
			String size = getContentPagerSize(parameter);
			String type = getContentPagerType(parameter);
			int maxLength = Integer.parseInt(size);
			int start = 0;
			//
			String content2 = target;
			int totalLength = content2.length();
			//
			PageBuilder pi = getConentPagerInfo(content2, size, type);
			if (totalLength > maxLength) {
				while (totalLength > maxLength) {
					int pos = totalLength - 1;
					int end = 0;
					String ppattern = "(<P>|<p>|</p>|</P>|<BR>|<br>|<br/>|<BR/>)";
					Pattern p2 = Pattern.compile(ppattern);
					Matcher m2 = p2.matcher(content2);
					boolean rs = m2.find(maxLength);
					// 假定必须找到，否则就不分页
					if (rs) {
						end = m2.start();
						page++;
						String pSplitter = m2.group(0);
						if (pSplitter.equalsIgnoreCase("</p>")) {
							end = end + 4;
						}
						String pContent = content2.substring(start, end);
						content2 = content2.substring(end);
						totalLength = content2.length();
						String pFileName = getPagerFileName(fileName, page);
						//
						pi.page(page);
						String beforeContent2 = this.generateStaticContent(
								beforeContent, (page) + "", url, configuration,
								node, ci, pi, errors);
						//
						String afterContent2 = this.generateStaticContent(
								afterContent, (page) + "", url, configuration,
								node, ci, pi, errors);
						//
						String fullContent = beforeContent2 + pContent
								+ afterContent2;
						File file = new File(pFileName);
						FileUtil.writeTextFile(file, fullContent, encoding);

					} else {
						page++;
						pi.page(page);
						String beforeContent2 = this.generateStaticContent(
								beforeContent, (page) + "", url, configuration,
								node, ci, pi, errors);
						//
						String afterContent2 = this.generateStaticContent(
								afterContent, (page) + "", url, configuration,
								node, ci, pi, errors);
						//
						String fullContent = beforeContent2 + content2
								+ afterContent2;
						String pFileName = getPagerFileName(fileName, page);
						File file = new File(pFileName);
						FileUtil.writeTextFile(file, fullContent, encoding);

						break;
					}
				}
				if (totalLength > 0) {
					page++;
					pi.page(page);
					String beforeContent2 = this.generateStaticContent(
							beforeContent, (page) + "", url, configuration,
							node, ci, pi, errors);
					//
					String afterContent2 = this.generateStaticContent(
							afterContent, (page) + "", url, configuration,
							node, ci, pi, errors);
					//

					String fullContent = beforeContent2 + content2
							+ afterContent2;
					String pFileName = getPagerFileName(fileName, page);
					File file = new File(pFileName);
					FileUtil.writeTextFile(file, fullContent, encoding);

				}
			} else {
				pi.page(page);
				String beforeContent2 = this.generateStaticContent(
						beforeContent, (page) + "", url, configuration, node,
						ci, pi, errors);
				//
				String afterContent2 = this.generateStaticContent(afterContent,
						(page) + "", url, configuration, node, ci, pi, errors);

				String fullContent = beforeContent2 + content2 + afterContent2;
				String pFileName = getPagerFileName(fileName, page);
				File file = new File(pFileName);
				FileUtil.writeTextFile(file, fullContent, encoding);
			}

		} else {
			//
			FileUtil.writeTextFile(destFile, content, encoding);

		}
	}

	private String getContentPagerSize(String macro) {
		String p = "size=\"([\\s\\p{Print}&&[^\"]]*)\"";
		Pattern pattern = Pattern.compile(p);
		Matcher m = pattern.matcher(macro);
		boolean rs = m.find();
		if (rs) {
			String size = m.group(1);
			return size;
		}
		return null;
	}

	private String getContentPagerType(String macro) {
		String p = "type=\"([\\s\\p{Print}&&[^\"]]*)\"";
		Pattern pattern = Pattern.compile(p);
		Matcher m = pattern.matcher(macro);
		boolean rs = m.find();
		if (rs) {
			String size = m.group(1);
			return size;
		}
		return null;

	}

	/**
	 * 
	 * @param fileName
	 *            String
	 * @param page
	 *            String
	 * @return String
	 */
	private String getPagerFileName(String fileName, int page) {
		String path = fileName; // FilenameUtils.getBaseName(fileName);
		// System.out.println("path="+path);
		int pos = path.lastIndexOf(".");
		String part1 = "";
		String part2 = "";
		String result = null;
		if (pos > 0) {
			if (page > 1) {
				part1 = path.substring(0, pos);
				part2 = path.substring(pos + 1);
				result = part1 + "_" + (page) + "." + part2;
			} else {
				result = fileName;
			}
		}
		return result;
	}

	protected PageBuilder getConentPagerInfo(String content, String size,
			String type) {
		int maxLength = Integer.parseInt(size);
		int start = 0;
		int page = 0;
		//
		String content2 = content;
		int totalLength = content2.length();
		if (totalLength > maxLength) {
			while (totalLength > maxLength) {
				int pos = totalLength - 1;
				int end = 0;
				String ppattern = "(<P>|<p>|</p>|</P>|<BR>|<br>|<br/>|<BR/>)";
				Pattern p2 = Pattern.compile(ppattern);
				Matcher m2 = p2.matcher(content2);
				boolean rs = m2.find(maxLength);
				// 假定必须找到，否则就不分页
				if (rs) {
					end = m2.start();
					page++;
					String pSplitter = m2.group(0);
					if (pSplitter.equalsIgnoreCase("</p>")) {
						end = end + 4;
					}
					content2 = content2.substring(end);
					totalLength = content2.length();
					//
				} else {
					page++;
					break;
				}
			}
			if (totalLength > 0) {
				page++;
			}

		} else {
			page++;
		}
		PageBuilder pb = new PageBuilder();
		pb.items(page);
		pb.itemsPerPage(1);
		return pb;
	}

	/**
	 * 
	 * @param args
	 *            String[]
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File f = new File("C:/mysql/mysqlbug.txt");
		// System.out.println("f path="+f.getPath());
		// System.out.println("f file="+f.getName());
		// String path=FilenameUtils.getName(f.getPath());
		// String extension=FilenameUtils.getExtension(f.getPath());
		// System.out.println("extension="+extension);
		// System.out.println("path="+path);
		// getPagerFileName(f.getPath(),"1");
		DefaultStaticFileGenerateEngine g = new DefaultStaticFileGenerateEngine();
		String test = "dadsadsadsadjhsjdhsada<cmsPager size=\"2002\" type=\"auto\"> \n\r我的测试了,额和,<p><cmsPager size=\"1000\" type=\"auto\">fdfdsfdsn你是谁,为了谁</p>Helloer你这个小子<br/></cmsPager><p>额和</p></cmsPager>dfdfdfdf</cmsPager>";
		System.out.println("length="
				+ new String("Helloer你这个小子<br/></cmsPager><p>额和</p>").length());
		// g.postConentProcess(null, test, null);
	}
}
