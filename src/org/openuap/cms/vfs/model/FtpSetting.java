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
package org.openuap.cms.vfs.model;

/**
 * <p>
 * FTP设置对象
 * </p>
 * 
 * <p>
 * $Id: FtpSetting.java 3916 2010-10-26 09:35:20Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class FtpSetting implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3408044892696833321L;
	public static final String FTP_TYPE = "ftp";
	public static final String FTPS_TYPE = "ftps";
	public static final String SFTP_TYPE = "sftp";

	public static final String[] FTP_TYPES = new String[] { FTP_TYPE,
			FTPS_TYPE, SFTP_TYPE };

	public static final String FTP_MODE_PASV = "pasv";
	public static final String FTP_MODE_PORT = "port";

	public static final String[] FTP_MODES = new String[] { FTP_MODE_PASV,
			FTP_MODE_PORT };
	private Long id;
	/** FTP名称. */
	private String title;
	/** FTP描述. */
	private String description;
	/** 主机. */
	private String host;
	/** 端口. */
	private Integer port;
	/** 用户名. */
	private String username;
	/** 密码. */
	private String password;
	/** FTP类型(FTP,FTPS,SFTP). */
	private String type;
	/** FTP证书. */
	private String cert;
	/** FTP模式，PASV，PORT. */
	private String mode;
	/** FTP远程缺省路径. */
	private String remotePath;

	private String encoding;

	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
}
