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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.cms.engine.profile.impl.PublishOperationProfileImpl;
import org.openuap.cms.keywords.filter.KeywordsFilter;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.cms.repo.model.ContentIndex;
import org.openuap.cms.util.PageInfo;
import org.openuap.cms.util.PositionInfo;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateProcessor;
import org.openuap.tpl.engine.TemplateProcessorChain;
import org.openuap.util.extractor.HtmlExtractor;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 输出文件处理器 处理内容分页功能.
 * </p>
 * 
 * <p>
 * 分页的导航，前台可以通过[#noparse]*[/#noparse]之中添加代码的方式处理
 * </p>
 * 
 * <p>
 * $Id: OutFileProcessor.java 4038 2011-04-28 05:58:13Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class OutFileProcessor implements TemplateProcessor {

	private PsnManager psnManager;

	private FreeMarkerTplProcessorHelper freeMarkerHelper;

	private int priority = 25;
	public final static String EXT_POINT_NAME = "tpl-processor-outfile";

	public String getName() {
		return EXT_POINT_NAME;
	}

	public void processTemplate(TemplateProcessorChain chain,
			TemplateContext context, List errors) {
		// 获得模板内容
		String fileContent = context.getTplContent();
		if (fileContent != null) {
			//
			Map model = context.getModel();
			if (model.get("__direct_out__") != null) {
				// 若是模型中放置“__direct_out__”参数，则不做文件生成处理
				// 也需要处理分页情形
				fileContent = processPreviewPageableConent(fileContent, model,
						errors);
				context.setTplContent(fileContent);
				chain.doProcess(context, errors);
			} else {
				// 防止包含方式出现问题
				// fileContent = "<!--#config ERRMSG=\" \" -->\n" + fileContent;
				// 获得模板输出编码
				String outEncoding = getTemplateOutEncoding(context);
				File destFile = getOutFile(context);
				try {
					// 处理可能存在的内容分页
					processPageableConent(destFile, fileContent, outEncoding,
							model, errors);
					chain.doProcess(context, errors);
				} catch (Exception e) {
					e.printStackTrace();
					errors.add(e);
				}
			}
		}

	}

	/**
	 * 获得内容输出路径
	 * 
	 * @param context
	 * @return
	 */
	protected File getOutFile(TemplateContext context) {
		Object oOutFile = context.getModel().get("outFile");
		// 若直接指定则使用指定的输出文件
		if (oOutFile != null && oOutFile instanceof File) {
			File outFile = (File) oOutFile;
			String parent = outFile.getParent();
			String fileName = outFile.getName();
			String page = (String) context.getModel().get("page");
			if (page == null) {
				page = "0";
			}
			//
			return getPageableFile(parent, fileName, page);
		} else {
			//
			return getDefaultCmsOutFile(context);
		}
	}

	/**
	 * 获得缺省的CMS内容输入文件
	 * 
	 * @param context
	 * @return
	 */
	protected File getDefaultCmsOutFile(TemplateContext context) {
		Object oContentIndex = context.getModel().get("ci");
		File destFile = null;
		if (oContentIndex != null && oContentIndex instanceof ContentIndex) {
			// 是内容页
			destFile = getDestContentFile(context);
		} else {
			// 是结点首页
			destFile = getDestNodeIndexFile(context);
		}
		return destFile;
	}

	protected String processPreviewPageableConent(String content, Map model,
			List errors) {
		if (content == null) {
			return "";
		}
		PublishOperationProfileImpl op = null;
		Exception exception = null;
		if (PublishProfileInfoHolder.isEnableProfile()) {
			op = new PublishOperationProfileImpl();
			op.setOperation("OutFileProcessor");
			op.setStartTime(System.currentTimeMillis());
		}
		//
		String pattern = "<cmsPager([\\s\\p{Print}&&[^>]]*)>([\\s\\p{Print}[^\\x00-\\xff]]*)?</cmsPager>";
		Pattern p = Pattern.compile(pattern);
		//
		Matcher m = p.matcher(content);
		boolean result = m.find();
		// int page = 0;
		int replace_start = 0;
		int replace_end = 0;

		try {
			if (result) {
				// 发现分页标识
				String parameter = m.group(1);
				String target = m.group(2);
				//
				replace_start = m.start();
				replace_end = m.end();
				// 需要分页的内容之前的内容
				String beforeContent = content.substring(0, replace_start);
				// 需要分页的内容之后的内容
				String afterContent = content.substring(replace_end);
				// 分页尺寸
				String size = getContentPagerSize(parameter);
				String type = getContentPagerType(parameter);
				String sizes[] = size.split("-");
				int maxLength = 0, minLength = 500;
				if (sizes.length == 2) {
					minLength = Integer.parseInt(sizes[0]);
					maxLength = Integer.parseInt(sizes[1]);
				} else {
					maxLength = Integer.parseInt(size);
				}
				// 处理内容关键词替换
				String url = (String) model.get("url");
				target = parseKeywords(target, url);
				// 获得分页信息
				PageInfo pi = getConentPagerInfo(target, minLength, maxLength,
						type);
				int pages = pi.pages();
				// System.out.println("欲分页内容="+target);
				Object oPage = model.get("page");
				if (oPage == null) {
					oPage = 1;
				}
				int page = new Integer(String.valueOf(oPage));
				if (page <= 0 || page > pages) {
					page = 1;
				}

				int i = page - 1;
				String pContent = target.substring(
						pi.getPositions().get(i).start, pi.getPositions()
								.get(i).end);

				// System.out.println("第"+i+"页："+pi.getPositions().get(i).start+";"+pi.getPositions()
				// .get(i).end);
				pi.page(i + 1);
				String fileName = String.valueOf(System.currentTimeMillis());
				String pFileName = getPagerFileName(fileName, i + 1);
				String tplName = fileName + (i + 1);
				//
				model.put("page", (i + 1));
				model.put("pageInfo", pi);
				model.put("pageTitle", pi.getTitle(i));
				// 处理内容前后模板内容
				String beforeContent2 = freeMarkerHelper.processTemplate(
						beforeContent, tplName, model, errors);
				String afterContent2 = freeMarkerHelper.processTemplate(
						afterContent, tplName, model, errors);
				String fullContent = beforeContent2 + pContent + afterContent2;
				return fullContent;

			} else {
				// 未发现分页标识
				return content;
			}
		} catch (Exception e) {
			exception = e;
			e.printStackTrace();
			return "";
		} finally {
			// 性能诊断-end
			if (PublishProfileInfoHolder.isEnableProfile()) {
				op.setEndTime(System.currentTimeMillis());
				op.setException(exception);
				op.setRecords(1);
				PublishProfileInfoHolder.getProfile().addPublishOperation(op);
			}
		}
	}

	/**
	 * 处理输出后的内容分页，根据内容中设定的“分页符”合或者内容字数自动分页
	 * 
	 * @param destFile
	 *            目标文件
	 * @param content
	 *            待分页内容
	 * @param encoding
	 *            文件编码
	 * @param model
	 *            模型
	 * @param errors
	 *            错误
	 * @throws Exception
	 */
	protected void processPageableConent(File destFile, String content,
			String encoding, Map model, List errors) throws Exception {
		if (destFile == null || content == null) {
			return;
		}
		PublishOperationProfileImpl op = null;
		Exception exception = null;
		if (PublishProfileInfoHolder.isEnableProfile()) {
			op = new PublishOperationProfileImpl();
			op.setOperation("OutFileProcessor");
			op.setStartTime(System.currentTimeMillis());
		}
		// 找到需要内容分页的内容，只有在<cmsPager></cmsPager>内的内容才被处理
		String pattern = "<cmsPager([\\s\\p{Print}&&[^>]]*)>([\\s\\p{Print}[^\\x00-\\xff]]*)?</cmsPager>";
		Pattern p = Pattern.compile(pattern);
		String fileName = destFile.getPath();
		Matcher m = p.matcher(content);
		boolean result = m.find();
		// int page = 0;
		int replace_start = 0;
		int replace_end = 0;

		try {
			if (result) {
				// 发现分页标识
				String parameter = m.group(1);
				String target = m.group(2);
				//
				replace_start = m.start();
				replace_end = m.end();
				// 需要分页的内容之前的内容
				String beforeContent = content.substring(0, replace_start);
				// 需要分页的内容之后的内容
				String afterContent = content.substring(replace_end);
				// 分页尺寸
				String size = getContentPagerSize(parameter);
				String type = getContentPagerType(parameter);
				String sizes[] = size.split("-");
				int maxLength = 0, minLength = 500;
				if (sizes.length == 2) {
					minLength = Integer.parseInt(sizes[0]);
					maxLength = Integer.parseInt(sizes[1]);
				} else {
					maxLength = Integer.parseInt(size);
				}
				// 处理内容关键词替换
				String url = (String) model.get("url");
				target = parseKeywords(target, url);
				// 获得分页信息
				PageInfo pi = getConentPagerInfo(target, minLength, maxLength,
						type);
				int pages = pi.pages();
				// System.out.println("欲分页内容="+target);
				for (int i = 0; i < pages; i++) {
					String pContent = null;
					if (i == 0) {
						// 特殊对待第一页，第一页不管分页标识符放置到哪里都认为可行
						pContent = target.substring(0,
								pi.getPositions().get(i).end);

						String ppattern = "(<p>)?#p#([\\s\\p{Print}[^\\x00-\\xff]&&[^#]]*?)#e#(</p>)?";
						// 替换掉分页标识符
						pContent = pContent.replaceAll(ppattern, "");
					} else {
						pContent = target.substring(
								pi.getPositions().get(i).start, pi
										.getPositions().get(i).end);
					}
					// System.out.println("第"+i+"页："+pi.getPositions().get(i).start+";"+pi.getPositions()
					// .get(i).end);
					pi.page(i + 1);
					String pFileName = getPagerFileName(fileName, i + 1);
					String tplName = fileName + (i + 1);
					//
					model.put("page", (i + 1));
					model.put("pageInfo", pi);
					model.put("pageTitle", pi.getTitle(i));
					// 处理内容前后模板内容
					String beforeContent2 = freeMarkerHelper.processTemplate(
							beforeContent, tplName, model, errors);
					String afterContent2 = freeMarkerHelper.processTemplate(
							afterContent, tplName, model, errors);
					String fullContent = beforeContent2 + pContent
							+ afterContent2;
					File file = new File(pFileName);
					FileUtil.writeTextFile(file, fullContent, encoding);
				}

			} else {
				// 未发现分页标识
				FileUtil.writeTextFile(destFile, content, encoding);
			}
		} catch (Exception e) {
			exception = e;
			e.printStackTrace();
		} finally {
			// 性能诊断-end
			if (PublishProfileInfoHolder.isEnableProfile()) {
				op.setEndTime(System.currentTimeMillis());
				op.setException(exception);
				op.setRecords(1);
				PublishProfileInfoHolder.getProfile().addPublishOperation(op);
			}
		}
	}

	/**
	 * 获得目标内容文件
	 * 
	 * @param context
	 * @return
	 */
	protected File getDestContentFile(TemplateContext context) {

		String fullPath = "";
		String sysRootPath = getConfig().getSysRootPath();
		String relativePath = "";
		//
		ContentIndex ci = null;
		Node node = null;
		//
		Object oCi = context.getModel().get("ci");
		Object oNode = context.getModel().get("node");
		//
		try {
			if (oCi == null || oNode == null) {
				return null;
			}
			// System.out.println("ci="+oCi);
			if (oCi instanceof ContentIndex) {
				ci = (ContentIndex) oCi;
				String selfPsn = ci.getSelfPsn();
				if (StringUtils.hasText(selfPsn)) {
					relativePath = getRelativePath(selfPsn);
				}
			}

			if (oNode instanceof Node) {
				node = (Node) oNode;
				if (relativePath.equals("")) {
					String pblPsn = node.getContentPsn();
					relativePath = getRelativePath(pblPsn);
				}
			}

			if (!relativePath.equals("")) {
				fullPath = sysRootPath + "/" + relativePath;
			} else {
				fullPath = sysRootPath;
			}
			// sub dir
			String fileName = "";
			if (ci != null) {
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
					if (node != null) {
						String subDir = node.getSubDir();
						String destDir = "";
						destDir = getDestDirName(subDir, ci);
						if (!destDir.equals("")) {
							fullPath += "/" + destDir;
						}

						fileName = getDestFileName(ci.getIndexId(), node, ci);
					}

				}
			}

			//
			Object oPage = context.getModel().get("page");

			String page = null;
			if (oPage != null) {
				page = oPage.toString();
			} else {
				page = "0";
			}

			//
			return getPageableFile(fullPath, fileName, page);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得目标结点首页文件
	 * 
	 * @param context
	 * @return
	 */
	protected File getDestNodeIndexFile(TemplateContext context) {
		String fullPath = "";
		String sysRootPath = getConfig().getSysRootPath();
		String relativePath = "";
		String indexFileName = "";
		// node
		Node node = null;
		Object oNode = context.getModel().get("node");
		if (oNode == null) {
			return null;
		}
		if (oNode instanceof Node) {
			node = (Node) oNode;
			if (relativePath.equals("")) {
				String pblPsn = node.getContentPsn();
				relativePath = getRelativePath(pblPsn);
			}
		}
		//
		if (!relativePath.equals("")) {
			fullPath = sysRootPath + "/" + relativePath;
		} else {
			fullPath = sysRootPath;
		}
		if (node != null) {
			indexFileName = node.getIndexName();
			indexFileName = indexFileName.replaceAll("\\{NodeID\\}", node
					.getNodeId().toString());
			// page
			Object oPage = context.getModel().get("page");
			String page = null;
			if (oPage != null) {
				page = oPage.toString();
			} else {
				page = "0";
			}
			return getPageableFile(fullPath, indexFileName, page);
		}
		return null;
	}

	/**
	 * 获得带有分页样式的文件
	 * 
	 * @param parentDir
	 *            目录名，全路径
	 * @param fileName
	 *            文件名
	 * @param page
	 *            页码
	 * @return
	 */
	protected File getPageableFile(String parentDir, String fileName,
			String page) {
		if (fileName == null) {
			return null;
		}
		String destFileName = fileName;
		if (page != null && !page.equals("0") && !page.equals("")
				&& !page.equals("1")) {
			int pos = destFileName.lastIndexOf(".");
			if (pos > -1) {
				String fileName_no_extension = destFileName.substring(0, pos);
				String file_extension = destFileName.substring(pos + 1);
				destFileName = fileName_no_extension + "_" + page + "."
						+ file_extension;
			} else {
				destFileName = destFileName + "_" + page;
			}
		}
		String fullPath = StringUtil.normalizePath(parentDir);
		File dir = new File(fullPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//
		File destFile = new File(dir, destFileName);
		return destFile;

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
			Psn psn = psnManager.getPsnFromCache(new Long(psnId));
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

	private String getDestDirName(String subDir, ContentIndex ci) {
		String destDir = "";
		if (subDir != null && !subDir.equals("") && !subDir.equals("none")) {
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

	protected CMSConfig getConfig() {
		return CMSConfig.getInstance();
	}

	protected String getTemplateOutEncoding(TemplateContext context) {
		// 缺省采用UTF-8编码
		String encoding = getConfig().getStringProperty("cms.tpl.out_encoding",
				"UTF-8");
		Object oNode = context.getModel().get("node");
		if (oNode != null && oNode instanceof Node) {
			// 每个结点可以自己覆盖缺省的编码
			Node node = (Node) oNode;
			String tplEncoding = node.getTplEncoding();
			if (StringUtils.hasText(tplEncoding)) {
				encoding = tplEncoding;
			}
		}
		return encoding;
	}

	/**
	 * 获得内容分页尺寸
	 * 
	 * @param macro
	 * @return
	 */
	private String getContentPagerSize(String macro) {
		String p = "size=\"([\\s\\p{Print}&&[^\"]]*)\"";
		Pattern pattern = Pattern.compile(p);
		Matcher m = pattern.matcher(macro);
		boolean rs = m.find();
		if (rs) {
			// 获得自动分页尺寸,允许指定最下分页尺寸-最大分页尺寸
			String size = m.group(1);
			return size;
		}
		return null;
	}

	/**
	 * 获得内容分页类型
	 * 
	 * @param macro
	 * @return
	 */
	private String getContentPagerType(String macro) {
		String p = "type=\"([\\s\\p{Print}&&[^\"]]*)\"";
		Pattern pattern = Pattern.compile(p);
		Matcher m = pattern.matcher(macro);
		boolean rs = m.find();
		if (rs) {
			String type = m.group(1);
			return type;
		}
		return null;

	}

	/**
	 * 获取分页信息
	 * 
	 * @param content
	 * @param minLength
	 * @param maxLength
	 * @param type
	 * @return
	 */
	public PageInfo getConentPagerInfo(String content, int minLength,
			int maxLength, String type) {
		// 首先进行人工分页
		PageInfo pi = this.getArtificialConentPagerInfo(content);
		if (pi == null) {
			pi = this.getAutoConentPagerInfo(content, minLength, maxLength,
					type);
		}
		return pi;
	}

	/**
	 * 获得内容分页信息
	 * 
	 * 若发现手工分页信息则取消自动分页
	 * 
	 * TODO 自动分页在计算字数的时候需要去掉html格式化文本
	 * 
	 * @param content
	 *            分割内容对象
	 * @param size
	 *            每页所含字数
	 * @param type
	 *            分页类型
	 * @return
	 */
	public PageInfo getAutoConentPagerInfo(String content, int minLength,
			int maxLength, String type) {

		//
		int start = 0;
		int delta = 0;
		int page = 0;
		int end = 0;
		//
		PageInfo pb = new PageInfo();
		List<PositionInfo> positions = new ArrayList<PositionInfo>();
		//
		String content2 = content;

		int totalLength = content2.length();
		if (totalLength > maxLength) {
			HtmlExtractor extractor = new HtmlExtractor();
			String ppattern = "(<P>|<p>|</p>|</P>|<BR>|<br>|<br/>|<BR/>|<br[\\s]*/>)";
			Pattern p2 = Pattern.compile(ppattern);
			while (totalLength - end > maxLength) {
				//				
				Matcher m2 = p2.matcher(content);
				boolean rs = m2.find(maxLength + delta);// 至少从指定长度开始匹配
				// 假定必须找到，否则就不分页
				if (rs) {
					end = m2.start();
					String pSplitter = m2.group(0);
					if (pSplitter.equalsIgnoreCase("</p>")) {
						end = end + 4;
					}
					// EXPERT:需要进行HTML内容清洗，若不满足字数要求，则不能进行分做一页
					// 计算差额，若差额大于50%，则不做分页，把下一页合并进来
					String extractedText = extractor.getText(content.substring(
							start, end));
					int totallen = extractedText.length();
					int margin = maxLength - totallen;
					float percent = (margin * 1.0f) / ((end - start) * 1.0f);
					if (percent < 0.5f) {
						page++;

						PositionInfo pi = new PositionInfo(start, end);
						positions.add(pi);
						pb.setTitle(page - 1, "第" + page + "页");
						start = end;
					}
					//
					delta = end;
				} else {
					// 未找到可以进行内容切分的位置，或者剩下的不足一页了
					end = content.length();
					page++;
					PositionInfo pi = new PositionInfo(start, end);
					positions.add(pi);
					pb.setTitle(page - 1, "第" + page + "页");
					break;
				}
			}
			if (totalLength - end > 0) {
				// 最后一页需要判断是否大于最小尺寸，若小于最小尺寸，最后一页将被归并到倒数第二页
				if (minLength > 0 && minLength >= totalLength - end) {
					if(page==0){
						page++;
						PositionInfo pi = new PositionInfo(start, content.length());
						positions.add(pi);
						pb.setTitle(page - 1, "第" + page + "页");
					}else{
						PositionInfo pi = positions.get(page - 1);
						pi.end = totalLength;
					}
				} else {
					page++;
					PositionInfo pi = new PositionInfo(start, content.length());
					positions.add(pi);
					pb.setTitle(page - 1, "第" + page + "页");
				}
			}

		} else {
			// 至少有一页的
			page++;
			PositionInfo pi = new PositionInfo(start, content.length());
			positions.add(pi);
			pb.setTitle(page - 1, "第" + page + "页");

		}
		pb.setPositions(positions);
		pb.items(page);
		pb.itemsPerPage(1);
		return pb;
	}

	/**
	 * 获得手工分页信息
	 * <p>
	 * 手工分页标识符为#p#分页标题#e#
	 * </p>
	 * 目前是找到几个即为几页，所以必须按照规则手工分页 要想使用手工分页符，开始的地方必须加入分页符，代表一页的开始
	 * <p>
	 * 对第一页要进行特殊处理，避免第一页放置不准确问题
	 * </p>
	 * 
	 * @param content
	 * @param size
	 * @param type
	 * @return
	 */
	public PageInfo getArtificialConentPagerInfo(String content) {
		String ppattern = "#p#([\\s\\p{Print}[^\\x00-\\xff]&&[^#]]*?)#e#";
		Pattern p = Pattern.compile(ppattern);
		Matcher m2 = p.matcher(content);
		boolean result = m2.find();
		int page = 0;
		int start = 0;
		int end = 0;
		//
		PageInfo pageInfo = new PageInfo();
		List<PositionInfo> positions = new ArrayList<PositionInfo>();
		while (result) {
			++page;// CPP FAN
			String title = m2.group(1);// 分页标题
			if (title == null) {
				title = "";
			}
			pageInfo.setTitle(page - 1, title);
			// 记录每页的内容起止点
			end = m2.end();// 本页起点
			start = m2.start();// 上一页的终点

			PositionInfo pi = new PositionInfo(start, end);
			positions.add(pi);
			result = m2.find();
		}
		if (page > 0) {
			// 最后的终点是内容最大的长度
			for (int i = 0; i < page - 1; i++) {
				PositionInfo pi = positions.get(i);
				PositionInfo pi2 = positions.get(i + 1);
				pi.start = pi.end;
				pi.end = pi2.start;
			}

			PositionInfo pi = positions.get(page - 1);
			pi.start = pi.end;
			pi.end = content.length();
			//
			pageInfo.items(page);
			pageInfo.itemsPerPage(1);
			pageInfo.setPositions(positions);
			return pageInfo;
		} else {
			// NOT MUST!
			pageInfo = null;
			positions = null;
		}
		return null;
	}

	/**
	 * 获得分页后的文件名
	 * 
	 * @param fileName
	 *            需要加分页信息的文件名
	 * @param page
	 *            页码
	 * @return 分页后的文件名
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

	protected String parseKeywords(String content, String excludeUrl) {
		if (CMSConfig.getInstance().isEnableKeywordsParse()) {
			StringBuffer result = new StringBuffer();
			KeywordsFilter.doAllFilter(result, content, excludeUrl);
			return result.toString();
		} else {
			return content;
		}
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setFreeMarkerHelper(
			FreeMarkerTplProcessorHelper freeMarkerHelper) {
		this.freeMarkerHelper = freeMarkerHelper;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public static void main(String[] args) {
		//
		String testContent = "#p#第一<br/>页#e#<br/><br/><br/><br/><br/><p>这是第一段测试内容，</p>不要乱了#L#P#p#第二页#e#<p>这是第二段内</p>容";
		OutFileProcessor o = new OutFileProcessor();
		PageInfo pi = o.getArtificialConentPagerInfo(testContent);
		System.out.println("pages=" + pi.pages());
		for (int i = 0; i < pi.pages(); i++) {
			//
			System.out
					.println("page (" + (i + 1) + ") Title=" + pi.getTitle(i));
			System.out.println("page content position (" + (i + 1) + ")="
					+ pi.getPositions().get(i).start + ","
					+ pi.getPositions().get(i).end);
			System.out.println("page content position ("
					+ (i + 1)
					+ ")="
					+ testContent.substring(pi.getPositions().get(i).start, pi
							.getPositions().get(i).end) + "");
		}
		//
		pi = o.getAutoConentPagerInfo(testContent, 5, 10, "");
		System.out.println("pages=" + pi.pages());
		for (int i = 0; i < pi.pages(); i++) {
			//
			System.out
					.println("page (" + (i + 1) + ") Title=" + pi.getTitle(i));
			System.out.println("page content position (" + (i + 1) + ")="
					+ pi.getPositions().get(i).start + ","
					+ pi.getPositions().get(i).end);
			System.out.println("page content position ("
					+ (i + 1)
					+ ")="
					+ testContent.substring(pi.getPositions().get(i).start, pi
							.getPositions().get(i).end) + "");
		}
	}
}
