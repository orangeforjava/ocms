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
package org.openuap.cms.resource.action;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.util.ImageUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 自动缩略控制器.
 * </p>
 * 
 * <p>
 * $Id: AutominiAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class AutominiAction extends AdminAction {
	public AutominiAction() {
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
	 * @throws 
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String rsRootDir = CMSConfig.getInstance().getResourceRootPath();
		String src = request.getParameter("src");
		String pixel = request.getParameter("pixel");
		String fullPath;
		if (src != null && pixel != null) {
			String width = pixel.substring(0, pixel.indexOf("*"));
			String height = pixel.substring(pixel.indexOf("*") + 1);
			Integer wi = new Integer(width);
			Integer he = new Integer(height);

			fullPath = rsRootDir + File.separator + src;
			fullPath = StringUtil.normalizePath(fullPath);
			File srcFile = new File(fullPath);
			//
			String contentType = FileUtil.getContentType(src);
			response.setContentType(contentType);
			//
			ImageUtil.scaleImage(srcFile, response.getOutputStream(), wi
					.intValue(), he.intValue());
		}
		return null;
	}

}
