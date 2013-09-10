/**
 * 
 */
package org.openuap.cms.setup.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.web.mvc.BaseFormController;
import org.openuap.cms.config.ConfigFactory;
import org.openuap.cms.user.manager.IUserManager;
import org.openuap.cms.user.model.AbstractUser;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.cms.user.security.permissions.UserPermissionConstant;
import org.openuap.cms.user.ui.UserStatus;
import org.openuap.cms.user.ui.UserType;
import org.openuap.passport.sso.UnauthorizedException;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author weiping.ju
 * 
 */
public class SupermanAction extends BaseFormController {

	private String BASIC_AUTH_USER;
	//
	private String BASIC_AUTH_PASS;
	private String defaultScreensPath;

	//
	private String operationViewName;

	//
	private IUserManager baseUserManager;

	public SupermanAction() {
		initDefaultProperty();
	}

	protected void initDefaultProperty() {
		defaultScreensPath = "/plugin/cms/base/screens/superman/";
		operationViewName = defaultScreensPath + "user_operation_result.html";
		this.setFormView(defaultScreensPath + "user_edit.html");
		this.setSuccessView(this.getFormView());
		this.setSessionForm(true);
		this.setBindOnNewForm(false);
		this.setCommandClass(IUser.class);
		this.setCommandName("user");
	}

	public ModelAndView beforePerform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		BaseApplicationConfiguration config = ConfigFactory.getInstance()
				.getBaseConfig();
		//
		BASIC_AUTH_USER = config.getString("sys.security.admin.username", "");
		BASIC_AUTH_PASS = config.getString("sys.security.admin.password", "");
		// 这里如果是中文会出现乱码
		String BASIC_AUTH_DESC = config.getString("sys.security.admin.desc",
				"CMS Super Administrator Control Center");
		if (!BASIC_AUTH_USER.equals("")
				&& helper.getAuthUsername().equals(BASIC_AUTH_USER)
				&& helper.getAuthPassword().equals(BASIC_AUTH_PASS)) {
		} else {
			try {
				helper.requireAuthentication(BASIC_AUTH_DESC);
			} catch (Exception ex) {
				log.fatal("Can't require Authentication", ex);
			}
			return errorPage(request, response, helper, "invalid_access", model);
		}
		return null;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors,
			ControllerHelper helper, Map model) throws Exception {
		//
		
		//
		ModelAndView mv = new ModelAndView(operationViewName, model);
		//
		try {
			IUser user = (IUser) command;
			String mode = request.getParameter("mode");
			String pwd = request.getParameter("pwd1");
			if (mode.equals("add")) {
				model.put("op", "add");
				user.setPos(new Integer(0));
				user.setPassword(pwd);
				user.setCreationDate(new Long(System.currentTimeMillis()));
				user.setLoginTimes(new Long(0));
				user.setLastLoginDate(new Long(-1));
				baseUserManager.addUser(user);
				model.put("rs", "success");
			} else {
				model.put("op", "edit");
				if (pwd != null && !pwd.trim().equals("")) {
					user.setPassword(pwd);
					baseUserManager.saveUserWithChangePwd(user);
				} else {
					baseUserManager.saveUser(user);
				}
				model.put("rs", "success");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			model.put("rs", "failed");
			model.put("ex", ex);
		}
		return mv;
	}

	/**
	 * 数据校验
	 */
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		

		String mode = request.getParameter("mode");
		String pwd1 = request.getParameter("pwd1");
		String pwd2 = request.getParameter("pwd2");
		AbstractUser user = (AbstractUser) command;
		if (mode.equals("add")) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
					"name_empty", "the name shouldn't be empty.");
			//
			if (!StringUtils.hasText(pwd1)) {
				errors.reject("password_empty",
						"the password shouldn't be empty");
			}
			if (pwd1 != null && !pwd1.equals(pwd2)) {
				errors.reject("password_not_equal",
						"the password is not equal.");
			}
			// http://jira.openuap.org/browse/CMS-15
			String name = request.getParameter("name");
			int count = baseUserManager.getUserByNameCount(name);
			if (count > 0) {
				errors.reject("name_is_exist", "用户名已经存在，请选择别的用户名.");
			}
		} else if (mode.equals("edit")) {
			if (pwd1 != null && !pwd1.equals(pwd2)) {
				errors.reject("password_not_equal",
						"the password is not equal.");
			}
		}
	}

	protected Object formBackingObject(HttpServletRequest request) {
		String mode = request.getParameter("mode");
		String userId = request.getParameter("userId");
		if (mode != null && mode.equals("edit")) {
			Long uid = new Long(userId);
			IUser user = baseUserManager.getUserById(uid);
			return user;
		} else {
			IUser user = baseUserManager.createUser();
			return user;
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {

		Map ref = new HashMap();
		String mode = request.getParameter("mode");
		if (mode == null) {
			mode = "add";
		}
		ref.put("mode", mode);
		ref.put("userStatues", UserStatus.ALL_USER_STATUS);
		ref.put("userTypes", UserType.SYS_USER_TYPES);
		return ref;
	}

	/**
	 * 用户名检查
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doCheckUserName(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String name = helper.getString("name");
		int count = baseUserManager.getUserByNameCount(name);
		//
		PrintWriter writer = response.getWriter();
		if (count == 0) {
			writer.print("1");
		} else {
			writer.print("0");
		}
		writer.flush();
		writer.close();
		return null;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setBaseUserManager(IUserManager baseUserManager) {
		this.baseUserManager = baseUserManager;
	}

}
