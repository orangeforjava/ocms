/**
 * $Id: SurveyAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.survey;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.UserAwareAction;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionPage;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.xml.SurveyXMLService;
import org.openuap.passport.sso.UnauthorizedException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Title: SurveyAction
 * </p>
 * 
 * <p>
 * Description: 调查问卷主控制器.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class SurveyAction extends UserAwareAction {
	//
	private SurveyManager surveyManager;

	private QuestionManager questionManager;

	//
	private String defaultViewName;

	private String defaultScreensPath;

	private String framesetViewName;

	private String headerViewName;

	private String listViewName;

	private String surveyViewName;

	private String resultViewName;

	private String importViewName;

	private SurveyXMLService surveyXMLService;

	public void setSurveyXMLService(SurveyXMLService surveyXMLService) {
		this.surveyXMLService = surveyXMLService;
	}

	/**
	 * 
	 */
	public SurveyAction() {

		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/survey/screens/";
		defaultViewName = defaultScreensPath + "index.html";
		framesetViewName = defaultScreensPath + "survey_frameset.html";
		headerViewName = defaultScreensPath + "survey_header.html";
		listViewName = defaultScreensPath + "survey_list.html";
		surveyViewName = defaultScreensPath + "survey_view.html";
		resultViewName = defaultScreensPath + "survey_result.html";
		importViewName = defaultScreensPath + "survey_import.html";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(framesetViewName, model);
		return mv;
	}

	public ModelAndView doHeader(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(headerViewName, model);
		return mv;
	}

	/**
	 * 显示调查问卷列表
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doList(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(listViewName, model);
		//
		//
		//
		String column_condition = "";
		//
		String where = request.getParameter("where");
		String page = request.getParameter("page");
		String pageNum = request.getParameter("pageNum");
		String order = request.getParameter("order");
		String order_mode = request.getParameter("order_mode");
		String order_name = request.getParameter("order_name");
		//
		String audit = request.getParameter("audit");

		//
		String creationDate = request.getParameter("creationDate");
		String creationDate2 = request.getParameter("creationDate2");
		// get the keyword
		String tmp = request.getParameter("keyword");
		if (tmp == null) {
			tmp = "";
		}
		//
		//
		String keyword = tmp;
		//
		String fields = helper.getString("fields", "");
		//
		Integer start = new Integer(0);
		Integer limit = new Integer(10);
		if (where == null) {
			where = "";
		}
		if (order == null) {
			order = "";
		}
		if (order_mode == null) {
			order_mode = "";
		}
		if (order_name == null) {
			order_name = "";
		}
		order_name = order_name.replaceAll("\\^", "");
		//
		String final_order = "";
		if (!order.equals("") && !order_mode.equals("")) {
			final_order = order + " " + order_mode;
		}
		if (pageNum != null) {
			limit = new Integer(pageNum);
		} else {
			pageNum = "10";
		}
		if (page != null) {
			start = new Integer((Integer.parseInt(page) - 1) * limit.intValue());
		} else {
			page = "1";
		}

		//
		if (fields != null && !fields.equals("")) {
			String columns[] = fields.split(",");
			if (columns != null) {
				for (int i = 0; i < columns.length; i++) {
					column_condition += " or e." + columns[i] + " like '%"
							+ keyword + "%'";

				}
				if (!column_condition.equals("")) {
					column_condition = column_condition.substring(4);
					column_condition = " (" + column_condition + ")";
				}
			}
		}
		//
		where += column_condition;
		// other where condition
		// audit
		if (audit != null) {
			if (!audit.equals("-1")) {
				where += " and e.audit=" + audit;
			}
		} else {
			audit = "-1";
		}

		// creationDate
		if (creationDate != null && !creationDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate);
			where += " and e.surveyCreationDate>=" + dd.getTime() + "";
		} else {
			creationDate = "";
		}
		// creationDate2
		if (creationDate2 != null && !creationDate2.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dd = sdf.parse(creationDate2);
			where += " and e.surveyCreationDate<=" + dd.getTime() + "";
		} else {
			creationDate2 = "";
		}
		//
		where += " and e.deleted=0";
		//
		PageBuilder pb = new PageBuilder(limit.intValue());
		QueryInfo qi = new QueryInfo(where, final_order, limit, start);
		//
		List surveys = surveyManager.getSurveys(qi, pb);
		pb.page(Integer.parseInt(page));
		model.put("surveys", surveys);
		model.put("pb", pb);
		model.put("page", page);
		model.put("pageNum", pageNum);
		model.put("order", order);
		model.put("order_mode", order_mode);
		model.put("order_name", order_name);
		model.put("where", where);
		model.put("action", this);
		// add some search parameter
		model.put("keyword", keyword);
		model.put("audit", audit);
		//
		model.put("creationDate", creationDate);
		model.put("creationDate2", creationDate2);
		//
		//
		return mv;
	}

	public ModelAndView doAudit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		boolean success = true;
		PrintWriter writer = response.getWriter();
		try {
			Long id = helper.getLong("id", 0L);
			if (id != 0) {
				surveyManager.auditSurvey(id);
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		if (success) {
			writer.print("0");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	public ModelAndView doUnAudit(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		boolean success = true;
		PrintWriter writer = response.getWriter();
		try {
			Long id = helper.getLong("id", 0L);
			if (id != 0) {
				surveyManager.unAuditSurvey(id);
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		if (success) {
			writer.print("0");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	public ModelAndView doDelete(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		boolean success = true;
		PrintWriter writer = response.getWriter();
		try {
			Long id = helper.getLong("id", 0L);
			if (id != 0) {
				surveyManager.recycleSurvey(id);
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		if (success) {
			writer.print("0");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	public ModelAndView doUnDelete(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws IOException, UnauthorizedException {
		boolean success = true;
		PrintWriter writer = response.getWriter();
		try {
			Long id = helper.getLong("id", 0L);
			if (id != 0) {
				surveyManager.unRecycleSurvey(id);
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		if (success) {
			writer.print("0");
		} else {
			writer.print("-1");
		}
		writer.flush();
		writer.close();
		//
		return null;
	}

	/**
	 * 预览调查问卷
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
	public ModelAndView doPreviewSurvey(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String pageId = request.getParameter("pageId");
		String sid = request.getParameter("sid");

		if (sid != null) {
			//
			Long surveyId = new Long(sid);
			List pages = questionManager.getPages(surveyId);
			Long pid = null;
			if (pageId == null) {
				pid = (Long) pages.get(0);
				pageId = String.valueOf(pid);
			} else {
				pid = new Long(pageId);
			}
			ModelAndView mv = new ModelAndView(surveyViewName, model);

			QuestionPage qp = questionManager.getQuestionPage(surveyId, pid);
			Survey survey = surveyManager.getSurveyById(surveyId);
			model.put("survey", survey);
			model.put("questionPage", qp);
			model.put("pageId", new Long(pageId));
			model.put("pages", pages);
			return mv;
		}
		return null;
	}

	/**
	 * 重置调查问卷，把用户投票结果清零
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @param helper
	 * 
	 * @param model
	 *            Map
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView doResetSurvey(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String sid = request.getParameter("sid");
		String rid = request.getParameter("rid");
		if (sid != null) {
			Long surveId = new Long(sid);
			Long surveyRecordId = new Long(rid);
			QueryInfo qi = new QueryInfo();
			PageBuilder pb = new PageBuilder();
			List questions = questionManager.getQuestionList(surveId, qi, pb);
			if (questions != null) {
				for (int i = 0; i < questions.size(); i++) {
					//
					Question q = (Question) questions.get(i);
					String hql = "update QuestionItem qi set qi.questionItemPolledTimes=0 where qi.question.questionId="
							+ q.getQuestionId();
					questionManager.executeHql(hql, null);
				}
			}
		}
		return doList(request, response, helper, model);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doExportSurvey(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Long id = helper.getLong("sid", 0L);
		if (id != 0) {
			try {
				Survey survey = surveyManager.getSurveyById(id);

				String rs = surveyXMLService.exportSurvey(survey);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/xml");
				setNoCacheHeader(response);
				PrintWriter writer = response.getWriter();
				writer.print(rs);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doDownloadSurvey(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		Long id = helper.getLong("sid", 0L);
		if (id != 0) {
			try {
				Survey survey = surveyManager.getSurveyById(id);
				//
				String attName = new String((survey.getSurveyName() + ".xml")
						.getBytes(), "iso8859-1");
				response.setContentType("application/x-msdownload");

				String header = "attachment; filename=" + attName;
				response.setHeader("Content-Disposition", header);
				surveyXMLService.saveSurveyXMLFile(survey);
				File file = surveyXMLService.getSurveyXMLFile(survey);
				InputStream dbInput = new FileInputStream(file);
				response.setContentLength(dbInput.available());
				ServletOutputStream os = response.getOutputStream();
				byte buf[] = new byte[4096];
				BufferedInputStream bis = new BufferedInputStream(dbInput);
				int j;
				while ((j = bis.read(buf, 0, 4096)) != -1) {
					os.write(buf, 0, j);
				}
				bis.close();
				os.flush();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public ModelAndView doImportSurvey(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(importViewName);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param helper
	 * @param model
	 * @return
	 */
	public ModelAndView doUploadFile(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		ModelAndView mv = new ModelAndView(resultViewName);
		String op = "import";
		model.put("op", op);
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest
					.getFile("uploadFile");
			String contentType = FileUtil.getExtension(multipartFile
					.getOriginalFilename());
			if (!isValidFileType("xml", contentType)) {
				String rs = "failed";
				String msgs = "请选择正确的文件类型-(" + getAcceptFileType("xml") + ")";
				model.put("rs", rs);
				model.put("msgs", msgs);
				return mv;
			}
			// 上传文件
			File dir = surveyXMLService.getSurveyUploadDir();
			String fileName = multipartFile.getOriginalFilename() + "-"
					+ System.currentTimeMillis();
			// System.out.println("fileName="+fileName);
			File xmlFile = new File(dir, fileName);
			multipartFile.transferTo(xmlFile);
			//
			String validationRs = surveyXMLService.validateSurvey(xmlFile);
			if (!validationRs.equals("success")) {
				String rs = "failed";
				String msgs = "XML校验失败：<br/><textarea name='xml' cols='60' rows=5'>" + validationRs+"</textarea>";
				model.put("rs", rs);
				model.put("msgs", msgs);
			} else {
				//
				surveyXMLService.importSurvey(xmlFile);
				//
				String rs = "success";
				model.put("rs", rs);
			}
			return mv;
		} catch (Exception e) {
			String rs = "failed";
			String msgs = "出现意外错误-(" + e.getMessage() + ")";
			model.put("rs", rs);
			model.put("msgs", msgs);
			e.printStackTrace();
		}
		//
		return mv;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	public void setListViewName(String listViewName) {
		this.listViewName = listViewName;
	}

	public void setHeaderViewName(String headerViewName) {
		this.headerViewName = headerViewName;
	}

	public void setFramesetViewName(String framesetViewName) {
		this.framesetViewName = framesetViewName;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setSurveyViewName(String surveyViewName) {
		this.surveyViewName = surveyViewName;
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	/**
	 * 
	 * @param type
	 *            String
	 * @param contentType
	 *            String
	 * @return boolean
	 */
	protected boolean isValidFileType(String type, String contentType) {
		//
		String acceptTypes = getAcceptFileType(type);
		if (acceptTypes != null) {
			StringTokenizer tk = new StringTokenizer(acceptTypes, "|");
			while (tk.hasMoreTokens()) {
				String acType = tk.nextToken();
				if (contentType.indexOf(acType) > -1) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	protected String getAcceptFileType(String type) {
		if (type != null) {
			if (type.equals("xml")) {
				String acceptTypes = "xml";
				return acceptTypes;
			}
		}
		return null;
	}
}
