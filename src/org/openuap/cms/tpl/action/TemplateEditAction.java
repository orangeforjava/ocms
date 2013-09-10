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
package org.openuap.cms.tpl.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.cm.manager.ContentFieldManager;
import org.openuap.cms.cm.manager.ContentTableManager;
import org.openuap.cms.cm.model.ContentField;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.CMSBaseFormAction;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.cms.engine.macro.CmsMacroEngine;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.publish.manager.ExtraPublishManager;
import org.openuap.cms.publish.model.ExtraPublish;
import org.openuap.cms.tpl.security.TemplatePermissionConstant;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 模板编辑控制器.
 * </p>
 * 
 * <p>
 * $Id: TemplateEditAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateEditAction extends CMSBaseFormAction {

	private String templateEditFViewName;

	private String templateEditHViewName;

	private String templateEditViewName;

	private String defaultScreensPath;

	private String tplPreviewName;

	private String templateOperationViewName;

	//
	private String templateEditorJsViewName;

	private ContentTableManager contentTableManager;

	private ContentFieldManager contentFieldManager;
	private String tplPreviewSettingViewName;

	private NodeManager nodeManager;
	private ExtraPublishManager extraPublishManager;
	private CmsMacroEngine cmsMacroEngine;
	//
	private PublishEngine publishEngine;

	public TemplateEditAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/tpl/edit/";
		templateEditFViewName = defaultScreensPath
				+ "template_edit_frameset.html";
		templateEditHViewName = defaultScreensPath
				+ "template_edit_header.html";
		templateEditViewName = defaultScreensPath + "template_edit.html";
		templateOperationViewName = defaultScreensPath
				+ "template_operation_result.html";
		templateEditorJsViewName = "/html/common/editor/js/menu_cms.js";
		tplPreviewSettingViewName = defaultScreensPath
				+ "template_preview_setting.html";
		;
	}

	/**
	 * 
	 * 结点模板编辑
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws
	 */
	public ModelAndView doNodeTplEdit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		//

		String mode = request.getParameter("mode");
		String path = helper.getString("path");
		String targetFile = helper.getString("targetFile");
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		String publishId = request.getParameter("pid");
		String extra = request.getParameter("extra");
		//
		String defaultFileName = "/" + System.currentTimeMillis() + ".html";
		if (!SecurityUtil.hasPermission(TemplatePermissionConstant.OBJECT_TYPE
				.toString(), nodeId, TemplatePermissionConstant.EditTpl)) {
			throw new UnauthorizedException();
		}
		//
		//
		if (StringUtils.hasText(nodeId)) {
			ModelAndView mv = new ModelAndView(templateEditFViewName, model);
			Long nid = new Long(nodeId);
			Node node = nodeManager.getNodeById(nid);
			String indexTpl = node.getIndexTpl();
			String contentTpl = node.getContentTpl();
			String imgTpl = node.getImageTpl();
			if (extra.equals("index")) {
				if (indexTpl != null) {
					path = this.getPath(indexTpl);
					targetFile = this.getTplFileName(indexTpl);
				} else {
					mode = "add";
					path = "/";
					targetFile = "未命名文件.html*";
				}
			} else if (extra.equals("content")) {
				if (contentTpl != null) {
					path = this.getPath(contentTpl);
					targetFile = this.getTplFileName(contentTpl);
				} else {
					mode = "add";
					path = "/";
					targetFile = "未命名文件.html*";
				}
			} else if (extra.equals("img")) {
				if (imgTpl != null) {
					path = this.getPath(imgTpl);
					targetFile = this.getTplFileName(imgTpl);
				} else {
					mode = "add";
					path = "/";
					targetFile = "未命名文件.html*";
				}

			} else if (extra.equals("extra")) {
				if (publishId != null) {
					Long pid = new Long(publishId);
					ExtraPublish p = extraPublishManager.getPublishById(pid);
					if (p != null) {
						String tpl = p.getTpl();
						if (tpl != null) {
							path = this.getPath(tpl);
							targetFile = this.getTplFileName(tpl);
						} else {
							mode = "add";
							path = "/";
							targetFile = "未命名文件.html*";
						}

					}
				}
			}
			//
			model.put("mode", mode);
			model.put("path", StringUtil.encodeURL(path, "UTF-8"));
			model.put("targetFile", StringUtil.encodeURL(targetFile, "UTF-8"));
			model.put("decodeTargetFile", targetFile);
			model.put("nodeId", nodeId);
			model.put("indexId", indexId);
			model.put("tpl", defaultFileName);
			//
			return mv;

		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelAndView doEditorFrameset(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		String mode = request.getParameter("mode");
		String path = helper.getString("path");
		String targetFile = helper.getString("targetFile");
		String nodeId = helper.getString("nodeId","0");
		String indexId = helper.getString("indexId","0");
		String defaultFileName = "/" + System.currentTimeMillis() + ".html";
		ModelAndView mv = new ModelAndView(templateEditFViewName, model);
		//
		if (mode == null || mode.equals("add")) {
			mode = "add";
			targetFile = "未命名文件.html*";

			nodeId = "";
			indexId = "";
		}
		//
		model.put("mode", mode);
		model.put("path", StringUtil.encodeURL(path, "UTF-8"));
		model.put("targetFile", StringUtil.encodeURL(targetFile, "UTF-8"));
		model.put("decodeTargetFile", targetFile);
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		model.put("tpl", defaultFileName);
		//
		return mv;
	}

	public ModelAndView doEditorHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String mode = request.getParameter("mode");
		String path = helper.getString("path");
		String targetFile = helper.getString("targetFile");
		String nodeId = helper.getString("nodeId","0");
		String indexId = helper.getString("indexId","0");
		String tpl = helper.getString("tpl");
		ModelAndView mv = new ModelAndView(templateEditHViewName, model);
		//
		model.put("mode", mode);
		model.put("path", path);
		model.put("targetFile", targetFile);
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		model.put("tpl", tpl);
		//
		return mv;
	}

	/**
	 * 模板编辑页面
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doEdit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {

		String mode = request.getParameter("mode");
		String path = helper.getString("path");
		String targetFile = helper.getString("targetFile");
		String nodeId = helper.getString("nodeId","0");
		String indexId = helper.getString("indexId","0");
		String tpl = helper.getString("tpl");
		ModelAndView mv = new ModelAndView(templateEditViewName, model);
		if (mode != null && mode.equals("edit")) {
			// get the file content
			CMSConfig config = CMSConfig.getInstance();
			
			String encoding = config.getStringProperty("sys.tpl.encoding",
					"UTF-8");
			//
			Node node=nodeManager.getNode(new Long(nodeId));
			if(node!=null){
				String nodeTplEncoding=node.getTplEncoding();
				if(StringUtils.hasText(nodeTplEncoding)){
					encoding=nodeTplEncoding;
				}
			}
			String tplRootDir = config.getUserTemplatePath();
			String fullPath = tplRootDir + "/" + path + "/" + targetFile;
			fullPath = StringUtil.normalizePath(fullPath);
			File tplFile = new File(fullPath);
			if (tplFile.exists()) {
				try {
					String content = FileUtil.readTextFile(tplFile, encoding);
					//
					if(StringUtils.hasText(content)){
						
					}
					model.put("content", content);
				} catch (Exception ex) {
				}
			}
		}
		List contentTableList = contentTableManager.getAllContentTableFromCache();
		//
		model.put("contentTableList", contentTableList);
		model.put("contentFieldManager", contentFieldManager);
		//
		model.put("mode", mode);
		model.put("path", path);
		model.put("targetFile", targetFile);
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		model.put("tpl", tpl);
		//
		return mv;
	}

	/**
	 * 模板提交保存
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doEditSubmit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {

		//
		String targetFile = helper.getString("targetFile");
		String path = helper.getString("path");
		String finalContent = "";
		String[] content = request.getParameterValues("content");
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		if (content == null) {
			finalContent = "";
		} else {
			for (int i = 0; i < content.length; i++) {
				finalContent += content[i];
			}
		}
		//
		ModelAndView mv = new ModelAndView(templateOperationViewName, model);
		model.put("operation", "save");
		model.put("path", path);
		model.put("targetFile", targetFile);
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		//
		if (targetFile != null && path != null) {
			try {
				CMSConfig config = CMSConfig.getInstance();
				String encoding = config.getStringProperty("sys.tpl.encoding",
						"UTF-8");
				String userTplPath = config.getUserTemplatePath();
				String fullPath = userTplPath + "/" + path + "/" + targetFile;
				fullPath = StringUtil.normalizePath(fullPath);
				File file = new File(fullPath);
				FileUtil.writeTextFile(file, finalContent, encoding);
				model.put("result", "success");
				model.put("msg", "保存文件(" + targetFile + ")成功！");
			} catch (Exception ex) {
				model.put("result", "failed");
				model.put("msg", "保存文件(" + targetFile + ")失败,出现意外错误:"
						+ ex.getLocalizedMessage());
			}
		}
		return mv;
	}

	public ModelAndView doCmsMenu(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(templateEditorJsViewName, model);
		List contentTableList = contentTableManager.getAllContentTableFromCache();
		//
		model.put("contentTableList", contentTableList);
		model.put("contentFieldManager", contentFieldManager);
		//
		setNoCacheHeader(response);
		model.put("responseType", "text/javaScript");
		return mv;
	}

	public void setTemplateEditFViewName(String templateEditFViewName) {
		this.templateEditFViewName = templateEditFViewName;
	}

	public void setTemplateEditHViewName(String templateEditHViewName) {
		this.templateEditHViewName = templateEditHViewName;
	}

	public void setTemplateEditViewName(String templateEditViewName) {
		this.templateEditViewName = templateEditViewName;
	}

	public void setTemplateEditorJsViewName(String templateEditorJsViewName) {
		this.templateEditorJsViewName = templateEditorJsViewName;
	}

	public void setTplPreviewName(String tplPreviewName) {
		this.tplPreviewName = tplPreviewName;
	}

	/**
	 * $Date: 2006/08/31 02:25:59 $
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 */
	public ModelAndView doPreviewSetting(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		String publishId = request.getParameter("publishId");
		ModelAndView mv = new ModelAndView(tplPreviewSettingViewName, model);
		//
		model.put("nodeId", nodeId);
		model.put("indexId", indexId);
		model.put("publishId", publishId);
		//
		if (indexId != null && !indexId.trim().equals("0")
				&& !indexId.trim().equals("")) {
			List contentList = cmsMacroEngine.getCmsContent(indexId);
			if (contentList != null && contentList.size() > 0) {
				Map contentMap = (Map) contentList.get(0);
				Long nid = (Long) contentMap.get("nodeId");
				String nodeName = (String) contentMap.get("nodeName");
				Node node = nodeManager.getNodeById(nid);
				if (node != null) {
					ContentField cf = contentFieldManager.getTitleField(node
							.getTableId());
					Object displayValue = contentMap.get(cf.getFieldName());
					model.put("displayValue", displayValue);
					model.put("nodeId", node.getNodeId().toString());
					model.put("nodeName", nodeName);
					model.put("publishId", null);
				}
			}
			return mv;
		}
		if (publishId != null && !publishId.trim().equals("")
				&& !publishId.trim().equals("0")) {
			Long pid = new Long(publishId);
			ExtraPublish extraPublish = extraPublishManager.getPublishById(pid);
			if (extraPublish != null) {
				Long nid = extraPublish.getNodeId();
				Node node = nodeManager.getNodeById(nid);
				String displayValue = extraPublish.getPublishName();
				model.put("displayValue", displayValue);
				model.put("indexId", null);
				if (node != null) {
					model.put("nodeId", node.getNodeId().toString());
					model.put("nodeName", node.getName());
				}
			}
			return mv;
		}
		if (nodeId != null && !nodeId.trim().equals("")) {
			Long nid = new Long(nodeId);
			Node node = nodeManager.getNodeById(nid);
			if (node != null) {
				model.put("nodeId", node.getNodeId().toString());
				model.put("nodeName", node.getName());
				model.put("displayValue", node.getName());
				model.put("indexId", null);
				model.put("publishId", null);
			}
			return mv;
		}
		//
		return mv;
	}

	/**
	 * 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param helper
	 *            ControllerHelper
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws IOException
	 */
	public ModelAndView doPreviewTpl(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException {
		//
		String type = request.getParameter("type");
		String nodeId = request.getParameter("nodeId");
		String indexId = request.getParameter("indexId");
		String publishId = request.getParameter("publishId");
		//
		String[] content = request.getParameterValues("content");
		//
		String finalContent = "";
		String result = "";
		if (content == null) {
			finalContent = "";
		} else {
			for (int i = 0; i < content.length; i++) {
				finalContent += content[i];
			}
		}
		// System.out.println(finalContent);
		List errors = new ArrayList();
		//
		if (type != null) {
			if (type.equals("0")) {
				Long nid = new Long(nodeId);
				result = publishEngine.previewTemplate(nid, null, null, 0,
						finalContent, 0, errors);
			} else if (type.equals("1")) {
				Long iid = new Long(indexId);
				result = publishEngine.previewTemplate(null, iid, null, 1,
						finalContent, 0, errors);

			} else if (type.equals("2")) {
				Long pid = new Long(publishId);
				result = publishEngine.previewTemplate(null, null, pid, 2,
						finalContent, 0, errors);
			}
		}
		response.setContentType("text/html;charset=UTF-8");
		// response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(result);
		pw.flush();
		pw.close();
		return null;
	}

	private String getTplFileName(String tplName) {
		int pos = tplName.lastIndexOf("/");
		if (pos != -1) {
			return tplName.substring(pos + 1);
		} else {
			return tplName;
		}
	}

	private String getPath(String tplName) {
		int pos = tplName.lastIndexOf("/");
		if (pos != -1) {
			return tplName.substring(0, pos);
		} else {
			return "/";
		}
	}
	public void setExtraPublishManager(ExtraPublishManager extraPublishManager) {
		this.extraPublishManager = extraPublishManager;
	}
	public void setPublishEngine(PublishEngine publishEngine) {
		this.publishEngine = publishEngine;
	}

	public void setTplPreviewSettingViewName(String tplPreviewSettingViewName) {
		this.tplPreviewSettingViewName = tplPreviewSettingViewName;
	}
	public void setContentFieldManager(ContentFieldManager contentFieldManager) {
		this.contentFieldManager = contentFieldManager;
	}

	public void setContentTableManager(ContentTableManager contentTableManager) {
		this.contentTableManager = contentTableManager;
	}
	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public void setCmsMacroEngine(CmsMacroEngine cmsMacroEngine) {
		this.cmsMacroEngine = cmsMacroEngine;
	}

}
