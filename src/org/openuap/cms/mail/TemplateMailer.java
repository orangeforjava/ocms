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
package org.openuap.cms.mail;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.openuap.cms.config.CMSConfig;
import org.openuap.util.HttpUtil;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

/**
 * <p>
 * 基于模版邮件.
 * </p>
 * 
 * <p>
 * $Id: TemplateMailer.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class TemplateMailer extends Mailer {
	/**
	 * 
	 * 
	 * @param email
	 *            
	 * @param templateName
	 *            
	 * @param context
	 *            
	 * @throws 
	 * @throws 
	 */
	public static void send(String email, String templateName, Map context,FreeMarkerConfigurer freemarkerConfigurer)
			throws MessagingException, UnsupportedEncodingException {
		// 即时更改生效
		CMSConfig config = CMSConfig.getInstance();
		service.configure(config);
		String body = "";
		try {
//			FreeMarkerConfigurer freemarkerConfigurer = (FreeMarkerConfigurer) ObjectLocator
//					.lookup("freemarkerConfigurer",CmsPlugin.PLUGIN_ID);
			
			freemarker.template.Configuration fcfg = freemarkerConfigurer
					.getConfiguration();
			Template tp = fcfg.getTemplate(templateName);
			//
			// 邮件主体
			body = FreeMarkerTemplateUtils.processTemplateIntoString(tp,
					context);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new MessagingException("mail_template_error", ex);
		}
		// 邮件主题
		String subject = (String) context.get("mail_subject");
		// 邮件内容类型
		String contentType = (String) context.get("mail_content_type");
		// 邮件编码
		String charset = HttpUtil.parseCharacterEncoding(contentType);

		if (subject == null) {
			subject = "From " + CMSConfig.getInstance().getTitle();
		}
		if (charset == null) {
			charset = MimeUtility.mimeCharset(DEFAULT_ENCODING);
			if (contentType == null) {
				contentType = "text/plain;charset=" + charset;
			} else {
				contentType = contentType + ";charset=" + charset;
			}
		} else {
			charset = MimeUtility.mimeCharset(charset);
		}
		//
		MimeMessage msg = service.createMimeMessage();
		// MimeMessageHelper helper = new MimeMessageHelper(msg);

		//
		// InternetAddress from = getFromAddress(charset);
		InternetAddress from = new InternetAddress(config.getStringProperty(
				"cms.mail.smtp.from_email", ""), config.getStringProperty(
				"cms.mail.smtp.from_name", ""), charset);
		msg.setFrom(from);
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		msg.setSubject(subject, charset);
		msg.setText(body, charset);
		msg.setHeader("Content-Type", contentType);
		msg.setHeader("Content-Transfer-Encoding", "8bit");
		msg.setHeader("X-mailer",
				"CMS--The powerful CMS by juweiping@gmail.com");
		log.debug("Send template email:" + templateName + " to " + email);
		service.send(msg);
	}

}
