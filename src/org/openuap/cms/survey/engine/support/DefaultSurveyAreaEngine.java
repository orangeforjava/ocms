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
package org.openuap.cms.survey.engine.support;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.survey.engine.SurveyAreaEngine;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyAreaManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.QuestionPage;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.model.SurveyArea;
import org.openuap.cms.survey.model.SurveyRecord;
import org.openuap.runtime.util.ObjectLocator;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateEngine;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 缺省调查问卷内容生成引擎
 * </p>
 * 
 * <p>
 * $Id: DefaultSurveyAreaEngine.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DefaultSurveyAreaEngine implements SurveyAreaEngine {

	private SurveyAreaManager surveyAreaManager;
	private SurveyManager surveyManager;
	protected QuestionManager questionManager;
	private NodeManager nodeManager;
	private PsnManager psnManager;
	private TemplateEngine templateEngine;

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public void setPsnManager(PsnManager psnManager) {
		this.psnManager = psnManager;
	}

	public void setSurveyAreaManager(SurveyAreaManager surveyAreaManager) {
		this.surveyAreaManager = surveyAreaManager;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	public String getAreaContent(String areaId, String surveyRecordId,
			String type) {
		SurveyArea area = surveyAreaManager.getAreaById(new Long(areaId));
		StringBuffer rs = new StringBuffer("");
		if (area != null) {
			Long nodeId = area.getNodeId();
			// 获得发布结点
			Long rid = new Long(surveyRecordId);
			SurveyRecord surveyRecord = surveyManager
					.getSurveyRecordById(new Long(rid));
			if (surveyRecord != null) {
				Long surveyId = surveyRecord.getSurveyId();
				QuestionPage qp = questionManager.getQuestionPage(surveyId, 1L);
				Survey survey = surveyManager.getSurveyById(surveyId);
				List pages = questionManager.getPages(surveyId);

				Node node = nodeManager.getNode(nodeId);
				TemplateContext context = new TemplateContext();
				if (area.getTplType().equals(TPL_TYPE_FILE)) {
					context.setTplName(area.getTplPath());
				} else {
					context.setTplContent(area.getTplContent());
				}
				List errors = new ArrayList();
				Map model = Collections.synchronizedMap(new HashMap());
				//
				// System.out.println("survey1="+survey);
				model.put("survey", survey);
				model.put("surveyRecord", surveyRecord);
				model.put("questionPage", qp);
				model.put("pageId", (1));
				model.put("pages", pages);
				model.put("sid", surveyId.toString());
				model.put("rid", rid.toString());
				//
				model.put("node", node);
				model.put("nodeId", nodeId);
				if (type != null && type.equals("inline")) {
					// 直接输出内容到页面中
					model.put("__direct_out__", "yes");
				} else {
					// 输出SSI指令
					File outFile = getDestSurveyAreaFile(area, node);
					model.put("outFile", outFile);
				}
				context.setModel(model);
				getTemplateEngine().renderTemplate(context, errors);
				if (type != null && type.equals("inline")) {
					String content = context.getTplContent();
					rs.append(content);
				} else {
					String ssiPath = getAreaSSIPath(area, node);
					rs.append(ssiPath);
				}
				if (errors.size() > 0) {
					// 调试错误输出
					for (Object o : errors) {
						// System.out.println("error:" + o);
					}
				}
			}

		}
		return rs.toString();
	}

	protected File getDestSurveyAreaFile(SurveyArea area, Node node) {
		String selfPsn = area.getSelfPsn();
		if (!StringUtils.hasText(selfPsn)) {
			selfPsn = node.getContentPsn();
		}
		String fullPath = psnManager.getFullPath(selfPsn);
		String publishFileName = area.getPublishFileName();
		if (!StringUtils.hasText(publishFileName)) {
			// 需要自动生成文件名
			publishFileName = "survey_area_" + area.getId() + "-"
					+ area.getLastModifiedDate() + ".html";
		}
		File dir = new File(fullPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File destFile = new File(publishFileName);
		return destFile;
	}

	public TemplateEngine getTemplateEngine() {
		if (templateEngine == null) {
			templateEngine = (TemplateEngine) ObjectLocator.lookup(
					"templateEngine", "org.openuap.tpl.engine");
		}
		return templateEngine;
	}

	public String getAreaSSIPath(SurveyArea area, Node node) {
		if (area != null) {

			String path = null;
			String psnUrl = area.getSelfPsnUrl();
			//
			String fileName = area.getPublishFileName();
			String pattern = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)*(:[a-zA-Z0-9]*)?([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
			String psnUrlInfo = psnManager.getPsnUrlInfo(psnUrl);
			Pattern p = Pattern.compile(pattern);
			// System.out.println("psnUrlInfo=" + psnUrlInfo);
			Matcher m = p.matcher(psnUrlInfo);
			boolean found = m.find();
			if (found) {
				path = m.group(4);
				// System.out.println("path="+path);
				//
			}
			if (path == null) {
				path = "";
			}
			path = "/" + path + "/" + fileName;
			path = path.replaceAll("\\/\\/", "/");
			//
			return path;

		}
		return "";
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}
}
