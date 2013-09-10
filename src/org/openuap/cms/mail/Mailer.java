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

import javax.mail.internet.InternetAddress;

import org.openuap.cms.config.CMSConfig;
import org.openuap.runtime.log.Log;

/**
 * <p>
 * 电子邮件服务.
 * </p>
 * 
 * <p>
 * $Id: Mailer.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class Mailer {
	static Log log = new Log("sys.mail");

	static InternetAddress FROM;

	static MailService service = new MailService();

	static String DEFAULT_ENCODING = CMSConfig.getInstance().getStringProperty(
			"sys.charset.encoding", "UTF-8");

	public static InternetAddress getFromAddress(String charset) {

		CMSConfig config = CMSConfig.getInstance();
		if (FROM == null) {
			FROM = new InternetAddress();
			try {
				// service.configure(config);
				FROM = new InternetAddress(config
						.getStringProperty("cms.mail.smtp.from_email"), config
						.getStringProperty("cms.mail.smtp.from_name"), charset);
				// config.UPDATE_STATUS |= 1;
			} catch (Exception ex) {
			}
		}
		return FROM;
	}

}
