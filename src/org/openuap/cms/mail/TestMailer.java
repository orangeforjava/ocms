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

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * <p>
 * 邮件测试.
 * </p>
 * <p>
 * $Id: TestMailer.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public class TestMailer {
	public static void main(String[] args) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("juweiping@gmail.com");
		msg.setTo("juweiping@gmail.com");
		msg.setSubject("您好呀，请速与我联系确认邮件事宜！");
		msg.setText("您好呀，请速与我联系确认邮件事宜！");
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("mail.sina.net");
		try {
			mailSender.send(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
