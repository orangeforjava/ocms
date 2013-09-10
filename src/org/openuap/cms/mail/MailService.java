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

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openuap.cms.config.CMSConfig;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * <p>
 * MailService
 * </p>
 * 
 * <p>
 * $Id: MailService.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * 
 * @author Joseph
 * @version 1.0
 */
public class MailService {

	private Properties props;
	private String host;
	private int port;
	private String user;
	private String password;
	private boolean auth;
	private boolean ssl;
	//
	private JavaMailSenderImpl sender = null; //

	/**
	 * 
	 */
	public MailService() {
		configure(CMSConfig.getInstance());
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public Properties getProps() {
		return props;
	}

	public String getUser() {
		return user;
	}

	public boolean isSsl() {
		return ssl;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public void configure(CMSConfig config) {
		if (config == null) {
			throw new IllegalArgumentException("smtp setting is empty!");
		}
		host = config.getStringProperty("cms.mail.smtp.host");
		port = config.getIntegerProperty("cms.mail.smtp.port");
		user = config.getStringProperty("cms.mail.smtp.user");
		password = config.getStringProperty("cms.mail.smtp.password");
		auth = config.getBooleanProperty("cms.mail.smtp.auth");

		// 是否使用SSL
		ssl = config.getBooleanProperty("cms.mail.smtp.ssl");
		if (host == null || port == 0) {
			throw new IllegalArgumentException("smtp setting is not valid");
		}
		sender = new JavaMailSenderImpl();
		Session sendMailSession = null;
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.port", port + "");
		props.setProperty("mail.smtp.user", user);
		if (ssl) {
			props.setProperty("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.socketFactory.port", port + "");

		}
		if (auth) {
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.ehlo", "true");
			sendMailSession = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, password);
				}
			});
		} else {
			sendMailSession = Session.getInstance(props);
		}

		//
		sender.setSession(sendMailSession);
		// sender.
	}

	public MimeMessage createMimeMessage() {
		return sender.createMimeMessage();
	}

	public void send(MimeMessage message) {
		//
		sender.send(message);
	}

	public static void main(String[] args) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		Session sendMailSession = null;
		// String host = "smtp.gmail.com";
		String host = "mail.hljwd.com";
		int port = 25;
		final String user = "public@hljwd.com";
		final String password = "8888";
		boolean auth = true;
		boolean ssl = false;
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.port", port + "");
		props.setProperty("mail.smtp.user", user);
		if (ssl) {
			props.setProperty("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.socketFactory.port", port + "");

		}
		if (auth) {
			props.setProperty("mail.smtp.auth", "true");
			// props.setProperty("mail.smtp.ehlo", "true");
			sendMailSession = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, password);
				}
			});
		} else {
			sendMailSession = Session.getInstance(props);
		}

		//
		sender.setSession(sendMailSession);
		// sender.
		MimeMessage msg = sender.createMimeMessage();
		try {
			msg.setFrom(new InternetAddress("public@hljwd.com"));
			msg.setSubject("这是测试邮件，对您的打扰请您谅解！");
			msg
					.setText("<a href=\"http://www.cjsdn.net\">���</a>���ѽ������</b>");
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
					"juweiping@gmail.com", false));
			msg.setSentDate(new Date());
			msg.setHeader("Content-Type", "text/html");
			msg.setHeader("Content-Transfer-Encoding", "8bit");
			msg.setHeader("X-mailer",
					"CMS--The powerful forum by juweiping@gmail.com");

			sender.send(msg);
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}

	}
}
