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
package org.openuap.cms.psn.model;

import java.io.Serializable;

import org.openuap.base.dao.hibernate.BaseObject;
import org.springframework.util.StringUtils;

/**
 * 
 * <p>
 * 发布点实体.
 * </p>
 * 
 * <p>
 * $Id: Psn.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class Psn extends BaseObject implements Serializable {
	/** 本地路径.*/
	private String localPath;
	
	private String psnType;

	private PsnFtp psnFtp = new PsnFtp();

	public static final int LOCAL_PSN_TYPE = 1;

	public static final int REMOTE_PSN_TYPE = 2;

	private int type;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2016218124699002942L;

	private int hashValue = 0;

	/** Id. */
	private Long id;

	/** 发布点名称. */
	private String name;

	/** PSN值. */
	private String psn;

	/** URL属性. */
	private String url;

	/** PSN描述. */
	private String description;

	/**
	 * Simple constructor of AbstractCmsPsn instances.
	 */
	public Psn() {
	}

	/**
	 * Constructor of AbstractCmsPsn instances given a simple primary key.
	 * 
	 * @param id
	 *            Long
	 */
	public Psn(Long id) {
		this.setId(id);
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param id
	 *            Long
	 */
	public void setId(Long id) {
		this.hashValue = 0;
		this.id = id;
	}

	/**
	 * Return the value of the name column.
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the value of the name column.
	 * 
	 * @param name
	 *            String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the value of the psn column.
	 * 
	 * @return java.lang.String
	 */
	public String getPsn() {
		return this.psn;
	}

	/**
	 * Set the value of the psn column.
	 * 
	 * @param psn
	 *            String
	 */
	public void setPsn(String psn) {
		this.psn = psn;
	}

	/**
	 * Return the value of the url column.
	 * 
	 * @return java.lang.String
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Set the value of the url column.
	 * 
	 * @param url
	 *            String
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Return the value of the description column.
	 * 
	 * @return java.lang.String
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Set the value of the description column.
	 * 
	 * @param description
	 *            String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof Psn)) {
			return false;
		}
		Psn that = (Psn) rhs;
		if (this.getId() == null || that.getId() == null) {
			return false;
		}
		return (this.getId().equals(that.getId()));
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int idValue = this.getId() == null ? 0 : this.getId().hashCode();
			result = result * 37 + idValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public int getType() {
		String psn = this.getPsn();
		if (psn != null) {
			if (psn.startsWith("relative::")) {
				return LOCAL_PSN_TYPE;
			} else if (psn.startsWith("ftp://")) {
				return REMOTE_PSN_TYPE;
			}

		}
		return 0;
	}

	/* Add customized code below */
	public class PsnFtp {
		private String ftpHost;

		private String ftpPort;

		private String ftpUser;

		private String ftpPass;

		private String ftpPath;

		/**
		 * parse the field value to psn
		 * 
		 * @return the psn value
		 */
		public String toString() {
			String psn = "ftp://";
			psn += (StringUtils.hasText(ftpUser) ? (ftpUser.trim()
					+ ((StringUtils.hasText(ftpPass)) ? ":" + ftpPass : "") + "@")
					: "");
			psn += ftpHost
					+ (StringUtils.hasText(ftpPort) ? (":" + ftpPort.trim())
							: "");
			if (StringUtils.hasText(ftpPath)) {
				if (ftpPath.charAt(0) == '/') {
					// System.out.println(ftpPath.charAt(0));
					ftpPath = ftpPath.replaceFirst("\\/+", "");
				}
				psn += "/" + ftpPath;
			}
			return psn;
		}

		/**
		 * parse the psn value to the field
		 * 
		 * @param psn
		 *            the psn value
		 */
		public void setPsn(String psn) {

			if (psn != null && psn.startsWith("ftp://")) {
				psn = psn.substring(6);
				// check if exist the user
				int pos = psn.indexOf("@");
				if (pos > -1) {
					// check if exist the pass
					int pos2 = psn.indexOf(":");
					if (pos2 > -1 && pos2 < pos) {
						ftpUser = psn.substring(0, pos2);
						ftpPass = psn.substring(pos2, pos);
					} else {
						ftpUser = psn.substring(0, pos);
					}

				}
				// check the host and port
				psn = psn.substring(pos + 1);
				int pos3 = psn.indexOf(":");
				int pos4 = psn.indexOf("/");
				if (pos3 > -1) {
					ftpHost = psn.substring(0, pos3);
					if (pos4 > -1) {
						ftpPort = psn.substring(pos3 + 1, pos4);
						ftpPath = psn.substring(pos4 + 1);
					} else {
						ftpPort = psn.substring(pos3 + 1);
					}
				} else {
					if (pos4 > -1) {
						ftpHost = psn.substring(0, pos4);
						ftpPath = psn.substring(pos4 + 1);
					} else {
						ftpHost = psn.substring(0);
					}
				}
			}
		}

		public String getFtpHost() {
			return this.ftpHost;
		}

		public void setFtpHost(String ftpHost) {
			this.ftpHost = ftpHost;
		}

		public String getFtpPort() {
			return this.ftpPort;
		}

		public void setFtpPort(String ftpPort) {
			this.ftpPort = ftpPort;
		}

		public String getFtpUser() {
			return this.ftpUser;
		}

		public void setFtpUser(String ftpUser) {
			this.ftpUser = ftpUser;
		}

		public String getFtpPass() {
			return this.ftpPass;
		}

		public void setFtpPass(String ftpPass) {
			this.ftpPass = ftpPass;
		}

		public String getFtpPath() {
			return this.ftpPath;
		}

		public void setFtpPath(String ftpPath) {
			this.ftpPath = ftpPath;
		}
	}

	public static void main(String[] args) {
		Psn psn = new Psn();
		psn.psnFtp.setPsn("ftp://www.sohu.com/");
		System.out.println("host=" + psn.psnFtp.ftpHost);
		System.out.println("port=" + psn.psnFtp.ftpPort);
		System.out.println("user=" + psn.psnFtp.ftpUser);
		System.out.println("pass=" + psn.psnFtp.ftpPass);
		System.out.println("path=" + psn.psnFtp.ftpPath);
		System.out.println(psn.psnFtp);
		psn.psnFtp.setFtpHost("localhost");
		psn.psnFtp.setFtpPort("9090");
		psn.psnFtp.setFtpUser("wxd");
		psn.psnFtp.setFtpPass("123");
		psn.psnFtp.setFtpPath("/demo");
		System.out.println(psn.psnFtp);
	}

	public void setPsnFtp(Psn.PsnFtp psnFtp) {
		this.psnFtp = psnFtp;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public void setPsnType(String psnType) {
		this.psnType = psnType;
	}

	public PsnFtp getPsnFtp() {
		return psnFtp;
	}

	public String getLocalPath() {
		if(localPath != null){
			if(localPath.startsWith("relative::")){
				return localPath.substring(10);
			}else{
				return localPath;
			}
		}else{
			if (this.getType() == LOCAL_PSN_TYPE) {
				return this.getPsn().substring(10);
			}
		}		
		return "";
	}

	public String getPsnType() {
		return psnType;
	}
}
