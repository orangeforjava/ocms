/**
 * $Id: Dom4jSurveyXMLService.java 4017 2011-03-13 13:55:50Z orangeforjava $
 */
package org.openuap.cms.survey.xml.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.survey.bean.QuestionItemResultBean;
import org.openuap.cms.survey.bean.QuestionResultBean;
import org.openuap.cms.survey.event.SurveyEvent;
import org.openuap.cms.survey.manager.QuestionManager;
import org.openuap.cms.survey.manager.SurveyManager;
import org.openuap.cms.survey.model.Answer;
import org.openuap.cms.survey.model.Question;
import org.openuap.cms.survey.model.QuestionItem;
import org.openuap.cms.survey.model.QuestionItemRecord;
import org.openuap.cms.survey.model.Survey;
import org.openuap.cms.survey.model.SurveyRecord;
import org.openuap.cms.survey.xml.SurveyXMLService;
import org.openuap.cms.user.model.IUser;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.openuap.runtime.util.ObjectLocator;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author Joseph
 * 
 */
public class Dom4jSurveyXMLService implements SurveyXMLService,
		ApplicationListener {

	public String exportSurvey(Survey survey) {
		Document document = DocumentHelper.createDocument();
		Element surveyElement = document.addElement("survey");
		surveyElement.addAttribute("id", survey.getSurveyId().toString());
		Element surveyNameElement = surveyElement.addElement("name");
		surveyNameElement.addCDATA(survey.getSurveyName());
		//
		Element surveyDescElement = surveyElement.addElement("description");
		surveyDescElement.addCDATA(survey.getSurveyDescription());
		//
		Element surveyCreationDateElement = surveyElement
				.addElement("creationDate");
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
		//
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(survey.getSurveyCreationDate());
		String creationDate = sdf.format(cal.getTime());
		surveyCreationDateElement.addText(creationDate);
		//
		Element surveyTypeElement = surveyElement.addElement("type");
		surveyTypeElement.setText(survey.getSurveyType().toString());
		//
		Element surveyStatusElement = surveyElement.addElement("status");
		surveyStatusElement.setText(survey.getSurveyStatus().toString());
		//
		Element surveyGuidElement = surveyElement.addElement("guid");
		surveyGuidElement.setText(survey.getSurveyGuid());
		//
		Element creationUserIdElement = surveyElement.addElement("userId");
		creationUserIdElement.setText(survey.getCreationUserId().toString());
		//
		Element creationUserNameElement = surveyElement.addElement("userName");
		creationUserNameElement.addCDATA(survey.getCreationUserName());
		//
		Element questionsElement = surveyElement.addElement("questions");
		Set questions = survey.getQuestions();
		if (questions != null) {
			Iterator itQuestions = survey.getQuestions().iterator();
			while (itQuestions.hasNext()) {
				Question question = (Question) itQuestions.next();
				Element questionElement = questionsElement
						.addElement("question");
				questionElement.addAttribute("id", question.getQuestionId()
						.toString());
				questionElement.addElement("title").addCDATA(
						question.getQuestionTitle());
				questionElement.addElement("guid").setText(
						question.getQuestionGuid());
				questionElement.addElement("status").setText(
						question.getQuestionStatus().toString());
				questionElement.addElement("type").setText(
						question.getQuestionType().toString());
				questionElement.addElement("filter").setText(
						question.getQuestionInputFilter().toString());
				//
				cal.setTimeInMillis(question.getQuestionCreationDate());
				questionElement.addElement("creationDate").setText(
						sdf.format(cal.getTime()));
				//
				questionElement.addElement("userId").setText(
						question.getCreationUserId().toString());
				//
				questionElement.addElement("userName").setText(
						question.getCreationUserName());
				questionElement.addElement("pos").setText(
						question.getQuestionPos().toString());
				questionElement.addElement("groupId").setText(
						question.getGroupId().toString());
				questionElement.addElement("groupTitle").addCDATA(
						question.getGroupTitle());
				questionElement.addElement("pageId").setText(
						question.getPageId().toString());
				Set items = question.getQuestionItems();
				if (items != null) {
					Element itemsElement = questionElement.addElement("items");
					Iterator itItems = items.iterator();
					while (itItems.hasNext()) {
						QuestionItem item = (QuestionItem) itItems.next();
						Element itemElement = itemsElement.addElement("item");
						itemElement.addAttribute("id", item.getQuestionItemId()
								.toString());
						itemElement.addElement("type").setText(
								item.getQuestionItemType().toString());
						itemElement.addElement("text").addCDATA(
								item.getQuestionItemText());
						itemElement.addElement("pos").setText(
								item.getQuestionItemSort().toString());
					}
				}

			}
		}
		//
		StringWriter rs = new StringWriter();
		try {
			/** 格式化输出,类型IE浏览一样 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(rs, format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs.toString();
	}

	public Survey importSurvey(String xml) {
		return null;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof SurveyEvent) {
			SurveyEvent surveyEvent = (SurveyEvent) event;
			int type = surveyEvent.getEventType();
			if (type == SurveyEvent.SUVEY_CREATED
					|| type == SurveyEvent.SUVEY_UPDATED) {
				Survey survey = surveyEvent.getSurvey();
				if (survey != null) {
					saveSurveyXMLFile(survey);
				}
			}
		}

	}

	public File getSurveyXMLFile(Survey survey) {
		String homePath = getPluginHomePath();
		String fullPath = homePath;
		if (fullPath != null) {
			fullPath += "/data";
			File dir = new File(fullPath);
			dir.mkdirs();
			String fileName = survey.getSurveyName() + "-"
					+ survey.getSurveyId() + ".xml";
			File file = new File(dir, fileName);
			return file;
		}
		return null;
	}

	public void saveSurveyXMLFile(Survey survey) {
		String xml = this.exportSurvey(survey);
		String homePath = getPluginHomePath();
		String fullPath = homePath;
		if (fullPath != null) {
			fullPath += "/data";
			File dir = new File(fullPath);
			dir.mkdirs();
			String fileName = survey.getSurveyName() + "-"
					+ survey.getSurveyId() + ".xml";
			File file = new File(dir, fileName);
			try {
				// 以UTF-8编码写入到文件
				IOUtils.write(xml, new FileOutputStream(file), "UTF-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getPluginHomePath() {
		WebApplicationPlugin surveyPlugin = (WebApplicationPlugin) WebPluginManagerUtils
				.getPlugin("base", CmsPlugin.PLUGIN_ID);
		String homePath = surveyPlugin.getPluginHomePath();
		return homePath;
	}

	public File getSurveyUploadDir() {
		String homePath = getPluginHomePath();
		String fullPath = homePath;
		if (fullPath != null) {
			fullPath += "/upload";
			File dir = new File(fullPath);
			dir.mkdirs();
			return dir;
		}
		return null;
	}

	public String validateSurvey(File xmlFile) throws Exception {
		SAXReader saxReader = new SAXReader();
		saxReader.setValidation(true);
		saxReader.setFeature("http://xml.org/sax/features/validation", true);
		saxReader.setFeature(
				"http://apache.org/xml/features/validation/schema", true);
		//
		saxReader
				.setProperty(
						"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
						"survey.xsd");

		XMLErrorHandler errorHandler = new XMLErrorHandler();
		saxReader.setErrorHandler(errorHandler);
		Document document = saxReader.read(xmlFile);
		if (errorHandler.getErrors().hasContent()) {
			return errorHandler.getErrors().asXML();
		} else {
			return "success";
		}
	}

	public Survey importSurvey(File xmlFile) throws Exception {
		SAXReader saxReader = new SAXReader();

		Document document = saxReader.read(xmlFile);

		Survey survey = null;
		try {
			//
			String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
			//
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
			//
			Element surveyElement = document.getRootElement();
			//
			survey = new Survey();
			//
			String surveyId = surveyElement.attributeValue("id");

			Element nameElement = surveyElement.element("name");
			if (nameElement != null) {
				String surveyName = nameElement.getText();
				survey.setSurveyName(surveyName);
			}

			Element descriptionElement = surveyElement.element("description");
			if (descriptionElement != null) {
				String surveyDescription = descriptionElement.getText();
				survey.setSurveyDescription(surveyDescription);
			}
			Element typeElement = surveyElement.element("type");
			String surveyType = typeElement.getText();
			survey.setSurveyType(new Integer(surveyType));
			//
			Element statusElement = surveyElement.element("status");
			String surveyStatus = statusElement.getText();
			survey.setSurveyStatus(new Integer(surveyStatus));
			//
			Element guidElement = surveyElement.element("guid");
			if (guidElement != null) {
				String surveyGuid = guidElement.getText();
				survey.setSurveyGuid(surveyGuid);
			}
			// 获得当前用户对象
			IUser user = SecurityUtil.getUser();
			// 用户信息可以为空
			Element userIdElement = surveyElement.element("userId");
			if (userIdElement != null) {
				String userId = userIdElement.getText();
				survey.setCreationUserId(new Long(userId));
			} else {
				survey.setCreationUserId(user.getUserId());
			}
			//
			Element userNameElement = surveyElement.element("userName");
			if (userNameElement != null) {
				String userName = userNameElement.getText();
				survey.setCreationUserName(userName);
			} else {
				survey.setCreationUserName(user.getName());
			}
			//
			Element creationDateElement = surveyElement.element("creationDate");
			if (creationDateElement != null) {
				String creationDate = creationDateElement.getText();
				try {
					survey.setSurveyCreationDate(sdf.parse(creationDate)
							.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				survey.setSurveyCreationDate(System.currentTimeMillis());
			}
			//
			survey.setDeleted(0);
			Long sid = getSurveyManager().addSurvey(survey);
			survey.setSurveyId(sid);
			// questions
			Element questionsElement = surveyElement.element("questions");
			List questionList = new ArrayList();
			if (questionsElement != null) {
				Iterator questionIterator = questionsElement
						.elementIterator("question");
				if (questionIterator != null) {
					while (questionIterator.hasNext()) {
						//
						Question question = new Question();
						//
						Element questionElement = (Element) questionIterator
								.next();
						Attribute idAttribute = questionElement.attribute("id");
						if (idAttribute != null) {
							String qid = idAttribute.getStringValue();
						}
						String qTitle = questionElement.element("title")
								.getText();
						question.setQuestionTitle(qTitle);
						//
						Element qGuidElement = questionElement.element("guid");
						if (qGuidElement != null) {
							String qGuid = qGuidElement.getText();
							question.setQuestionGuid(qGuid);
						}
						Element qStatusElement = questionElement
								.element("status");
						if (qStatusElement != null) {
							String qStatus = qStatusElement.getText();
							question.setQuestionStatus(new Integer(qStatus));
						}
						Element qTypeElement = questionElement.element("type");
						if (qTypeElement != null) {
							String qType = qTypeElement.getText();
							question.setQuestionType(new Integer(qType));
						}
						Element qFilterElement = questionElement
								.element("filter");
						if (qFilterElement != null) {
							String qFilter = qFilterElement.getText();
							question
									.setQuestionInputFilter(new Integer(qFilter));
						}
						Element qCreationDateElement = questionElement
								.element("creationDate");
						if (qCreationDateElement != null) {
							String qCreationDate = qCreationDateElement
									.getText();
							try {
								question.setQuestionCreationDate(sdf.parse(
										qCreationDate).getTime());
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} else {
							question.setQuestionCreationDate(System
									.currentTimeMillis());
						}
						//

						Element qUserIdElement = questionElement
								.element("userId");
						if (qUserIdElement != null) {
							String qUserId = qUserIdElement.getText();
							question.setCreationUserId(new Long(qUserId));
						} else {
							question.setCreationUserId(user.getUserId());
						}
						//
						Element qUserNameElement = questionElement
								.element("userName");
						if (qUserNameElement != null) {
							String qUserName = qUserNameElement.getText();
							question.setCreationUserName(qUserName);
						} else {
							question.setCreationUserName(user.getName());
						}
						//
						Element qPosElement = questionElement.element("pos");
						if (qPosElement != null) {
							String qPos = qPosElement.getText();
							question.setQuestionPos(new Long(qPos));
						}
						Element qGroupIdElement = questionElement
								.element("groupId");
						if (qGroupIdElement != null) {
							String qGroupId = qGroupIdElement.getText();
							question.setGroupId(new Long(qGroupId));
						}
						Element qGroupTitleElement = questionElement
								.element("groupTitle");
						if (qGroupTitleElement != null) {
							String qGroupTitle = qGroupTitleElement.getText();
							question.setGroupTitle(qGroupTitle);
						}
						//
						Element qPageIdElement = questionElement
								.element("pageId");
						if (qPageIdElement != null) {
							String qPageId = qPageIdElement.getText();
							question.setPageId(new Long(qPageId));
						}
						//
						question.setSurveyId(sid);
						Long qid = getQuestionManager().addQuestion(question);
						question.setQuestionId(qid);
						//						
						Element qItemsElement = questionElement
								.element("items");
						Set items = null;
						if (qItemsElement != null) {
							Iterator itemIterator = qItemsElement
									.elementIterator("item");
							List itemList = new ArrayList();
							if (itemIterator != null) {
								while (itemIterator.hasNext()) {
									//									
									//									
									QuestionItem questionItem = new QuestionItem();
									Element itemElement = (Element) itemIterator
											.next();
									Attribute iIdAttribute = itemElement
											.attribute("id");
									if (iIdAttribute != null) {
										String iId = iIdAttribute.getText();
									}
									Element iTypeElement = itemElement
											.element("type");
									if (iTypeElement != null) {
										String iType = iTypeElement.getText();
										questionItem
												.setQuestionItemType(new Integer(
														iType));
									}
									Element iPosElement = itemElement
											.element("pos");
									if (iPosElement != null) {
										String iPos = iPosElement.getText();
										questionItem
												.setQuestionItemSort(new Integer(
														iPos));
									}
									//
									Element iTextElement = itemElement
											.element("text");
									if (iTextElement != null) {
										String iText = iTextElement.getText();
										questionItem.setQuestionItemText(iText);
									}
									questionItem.setQuestionItemPolledTimes(0);
									questionItem.setQuestion(question);
									getQuestionManager().addQuestionItem(
											questionItem);

								}

							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return getSurveyManager().getSurveyById(survey.getSurveyId());
	}
	public SurveyManager getSurveyManager() {
		SurveyManager surveyManager = (SurveyManager) ObjectLocator.lookup(
				"surveyManager", CmsPlugin.PLUGIN_ID);
		return surveyManager;
	}

	public QuestionManager getQuestionManager() {
		QuestionManager questionManager = (QuestionManager) ObjectLocator
				.lookup("questionManager", CmsPlugin.PLUGIN_ID);
		return questionManager;
	}

	public String exportSurveyStatics(Long surveyRecordId, Long surveyId) {
		Survey survey = getSurveyManager().getSurveyById(surveyId);
		SurveyRecord surveyRecord = getSurveyManager().getSurveyRecordById(
				surveyRecordId);
		QueryInfo qi = new QueryInfo();
		PageBuilder pb = new PageBuilder();
		List questions = getQuestionManager().getQuestionList(surveyId, qi, pb);
		List<QuestionResultBean> questionResultList = new ArrayList<QuestionResultBean>();
		if (questions != null) {
			int size = questions.size();
			for (int i = 0; i < size; i++) {
				Question q = (Question) questions.get(i);
				// 获得统计结果
				QuestionResultBean qrs = this.getQuestionResult(surveyRecordId,
						q);
				questionResultList.add(qrs);
			}
		}
		Document document = DocumentHelper.createDocument();
		Element staticsElement = document.addElement("statics");
		Element titleElement = staticsElement.addElement("title");
		if (surveyRecord.getRecordTitle() != null) {
			titleElement.addCDATA(surveyRecord.getRecordTitle());
		} else {
			titleElement.addCDATA(survey.getSurveyName());
		}
		Element descriptionElement = staticsElement.addElement("description");
		if (surveyRecord.getRecordDesc() != null) {
			descriptionElement.addCDATA(surveyRecord.getRecordDesc());
		} else {
			descriptionElement.addCDATA(survey.getSurveyDescription());
		}
		//
		String dateFormat = "EEE, d MMM yyyy HH:mm:ss Z";
		//
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
		Calendar cal = Calendar.getInstance();

		Element startDateElement = staticsElement.addElement("startDate");
		cal.setTimeInMillis(surveyRecord.getStartDate());
		String startDate = sdf.format(cal.getTime());
		startDateElement.setText(startDate);
		Element endDateElement = staticsElement.addElement("endDate");
		cal.setTimeInMillis(surveyRecord.getEndDate());
		String endDate = sdf.format(cal.getTime());
		endDateElement.setText(endDate);
		//
		Element answersElement = staticsElement.addElement("answers");
		for (QuestionResultBean questionResultBean : questionResultList) {
			Element answerElement = answersElement.addElement("answer");
			Element questionElement = answerElement.addElement("question");
			Question question = questionResultBean.getQuestion();
			questionElement.addCDATA(question.getQuestionTitle());
			Integer type = question.getQuestionType();
			questionElement.addAttribute("type", type.toString());
			Element totalCountElement = answerElement.addElement("totalCount");
			totalCountElement.setText(new Integer(questionResultBean
					.getQuestionItemsCount()).toString());
			if (type == 3) {
				List<Answer> answerList = questionResultBean
						.getQuestionAnswers();
				Element itemsElement = answerElement.addElement("items");
				for (Answer answer : answerList) {
					Element itemElement = itemsElement.addElement("item");
					Element itemTitleElement = itemElement.addElement("title");
					itemTitleElement.addCDATA(question.getQuestionTitle());
					Element resultElement = itemElement.addElement("result");
					resultElement.addCDATA(answer.getAnswerInputText());
				}
			} else {
				Collection<QuestionItemResultBean> qiResults = questionResultBean
						.getQuestionItemResultList();
				Element itemsElement = answerElement.addElement("items");
				for (QuestionItemResultBean qiResult : qiResults) {
					Element itemElement = itemsElement.addElement("item");
					Element itemTitleElement = itemElement.addElement("title");
					itemTitleElement.addCDATA(qiResult.getQuestionItemText());
					Element resultElement = itemElement.addElement("result");
					resultElement
							.setText(new Integer(qiResult.getPolledTimes())
									.toString());
				}
			}
		}
		StringWriter rs = new StringWriter();
		try {
			/** 格式化输出,类型IE浏览一样 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(rs, format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs.toString();
	}

	/**
	 * 从问题中获得结果
	 * 
	 * @param q
	 * @return
	 */
	private QuestionResultBean getQuestionResult(Long rid, Question q) {
		QuestionResultBean questionResultBean = new QuestionResultBean();
		//
		questionResultBean.setQuestion(q);
		int type = q.getQuestionType();
		Set items = q.getQuestionItems();
		int questionItemsTotalCount = 0;
		List rs = new ArrayList();
		if (type == 3) {
			questionItemsTotalCount = getSurveyManager()
					.getQuestionAnswerTotalCount(rid, q.getQuestionId());
			QueryInfo qi = new QueryInfo();
			PageBuilder pb = new PageBuilder();
			List answers = getQuestionManager().getAnswerByQuestion(
					q.getQuestionId(), rid, qi, pb);
			questionResultBean.setQuestionAnswers(answers);
		} else {
			if (items != null) {

				questionItemsTotalCount = getSurveyManager()
						.getQuestionItemsTotalCount(rid, q.getQuestionId());
				Iterator itemIterator = items.iterator();

				while (itemIterator.hasNext()) {
					QuestionItem qi = (QuestionItem) itemIterator.next();
					QuestionItemResultBean qiResult = new QuestionItemResultBean();
					qiResult.setQuestionItemText(qi.getQuestionItemText());
					QuestionItemRecord qiRecord = getSurveyManager()
							.getQuestionItemRecord(rid, qi.getQuestionItemId());
					float votePercent = (float) Math
							.round(((float) qiRecord
									.getQuestionItemPolledTimes().intValue() / (float) questionItemsTotalCount) * 100F * 100F) / 100F;
					qiResult.setQuestionPercent(votePercent);
					qiResult.setPolledTimes(qiRecord
							.getQuestionItemPolledTimes().intValue());
					rs.add(qiResult);

				}
			}
		}
		questionResultBean.setQuestionItemResultList(rs);
		questionResultBean.setQuestionItemsCount(questionItemsTotalCount);
		return questionResultBean;
	}
}
