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
package org.openuap.cms.publish.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.engine.PublishEngine;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 动态附加发布控制器.
 * </p>
 * 
 * 
 * <p>
 * $Id: DynamicExtraViewAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class DynamicExtraViewAction extends CMSBaseAction {

	private PublishEngine publishEngine;

	/**
	 * 
	 */
	public DynamicExtraViewAction() {
	}

	public ModelAndView perform(HttpServletRequest req, HttpServletResponse res,
			ControllerHelper helper, Map model) throws Exception {
		return this.doViewExtra(req, res, helper, model);
	}
	/**
	 * 动态显示附加发布内容
	 * @param req
	 * @param res
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doViewExtra(HttpServletRequest req, HttpServletResponse res,
			ControllerHelper helper, Map model) throws Exception {
		String nodeId = req.getParameter("nodeId");
		String publishId = req.getParameter("publishId");
		String page = req.getParameter("page");
		Integer p = new Integer(0);
		if (nodeId != null && publishId != null) {
			Long nid = new Long(nodeId);
			if (page != null) {
				p = new Integer(page);
			}
			Long pid = new Long(publishId);
			List errors = new ArrayList();
			String result = getPublishEngine().getExtraContent(nid, pid, p.intValue(), errors);

			res.setContentType("text/html;charset=" + getTemplateEncoding());
			//
			PrintWriter pw = res.getWriter();
			pw.print(result);
			pw.flush();
			pw.close();
			return null;
		}
		return null;
	}

	public void setPublishEngine(PublishEngine publishEngine) {
		this.publishEngine = publishEngine;
	}

	protected String getTemplateEncoding() {
		return getConfig().getString("sys.tpl.encoding", "UTF-8");
	}

	public PublishEngine getPublishEngine() {
		if(publishEngine==null){
			publishEngine=(PublishEngine) ObjectLocator.lookup("publishEngine", CmsPlugin.PLUGIN_ID);
		}
		return publishEngine;
	}

}
