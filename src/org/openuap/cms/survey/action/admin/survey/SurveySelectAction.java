/**
 * $Id: SurveySelectAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.survey;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.cms.core.action.UserAwareAction;
import org.springframework.web.servlet.ModelAndView;

/**
 * 调查活动选择控制器
 * @author Joseph
 *
 */
public class SurveySelectAction extends UserAwareAction{
	
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		return null;
	}
}
