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
package org.openuap.cms.setup.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.container.ContainerManager;
import org.openuap.runtime.config.ApplicationConfigurationException;
import org.openuap.runtime.setup.spring.DispatcherRefreshTool;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 反安装控制器.
 * </p>
 * 
 * <p>
 * $Id: UnInstallAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class UnInstallAction extends AdminAction {
	public UnInstallAction() {
	}

	/**
	 * 
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws ApplicationConfigurationException {
		if (this.getBootstrapManager().isSetupComplete()) {
			this.getBootstrapManager().setSetupComplete(false);
			this.getBootstrapManager().getSetupPersister().setSetupType(
					"initial");
			this.getBootstrapManager().getApplicationConfig()
					.setCurrentSetupStep("welcome");
			this.getBootstrapManager().getApplicationConfig().setProperty(
					"hibernate.setup", new Boolean(false));
			this.getBootstrapManager().getApplicationConfig().save();
			//
			ContainerManager.getInstance().getContainerContext("cms").refresh();
			CMSConfig.getInstance().refresh();
			//
			helper.sendRedirect(helper.getBaseURL());
			DispatcherRefreshTool refeshTool = (DispatcherRefreshTool) ObjectLocator
					.lookup("dispatcherRefreshTool");
			refeshTool.refreshDispatcher();
			//
		}
		return null;
	}

}
