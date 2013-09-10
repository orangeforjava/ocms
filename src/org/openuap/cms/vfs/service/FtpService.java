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
package org.openuap.cms.vfs.service;

import java.io.File;

import org.openuap.cms.vfs.cache.VfsCache;
import org.openuap.cms.vfs.model.FtpSetting;
import org.springframework.util.StringUtils;

import com.enterprisedt.net.ftp.FTPClientInterface;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FTPMessageListener;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.pro.ProFTPClient;
import com.enterprisedt.net.ftp.ssh.SSHFTPClient;
import com.enterprisedt.net.ftp.ssl.SSLFTPClient;
import com.enterprisedt.util.debug.Level;
import com.enterprisedt.util.debug.Logger;

/**
 * <p>
 * Ftp 服务
 * </p>
 * 
 * <p>
 * $Id: FtpService.java 3916 2010-10-26 09:35:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class FtpService {
	/**
	 * 获得FTP客户端对象
	 * 
	 * @param id
	 *            ftp配置id
	 * @return FTPClientInterface
	 * @throws Exception
	 */
	public static FTPClientInterface getFtp(Long id) throws Exception {
		FtpSetting ftpSetting = VfsCache.getFtpSettingByRefresh(id);
		if (ftpSetting != null) {
			String type = ftpSetting.getType();
			String host = ftpSetting.getHost();
			int port = ftpSetting.getPort();
			String username = ftpSetting.getUsername();
			String password = ftpSetting.getPassword();
			String mode = ftpSetting.getMode();
			String remotePath = ftpSetting.getRemotePath();
			String encoding = ftpSetting.getEncoding();
			if (encoding == null || encoding.equals("")) {
				encoding = "utf-8";
			}
			if (type.equals(FtpSetting.FTP_TYPE)) {
				ProFTPClient ftp = new ProFTPClient();
				try {
					ftp.setRemoteHost(host);
					ftp.setRemotePort(port);
					ftp.setControlEncoding(encoding);
					ftp.setMessageListener(new LogMessageListener());
					//
					ftp.connect();
					
					ftp.login(username, password);

					if (StringUtils.hasText(remotePath)) {
						ftp.chdir(remotePath);
					} else {
						ftp.chdir(".");
					}
					return ftp;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {

				}

			} else if (type.equals(FtpSetting.FTPS_TYPE)) {
				SSLFTPClient ftp = new SSLFTPClient();
				int flag = SSLFTPClient.ConfigFlags.DISABLE_SSL_CLOSURE;
				ftp.setConfigFlags(flag);
				ftp.setRemoteHost(host);
				ftp.setRemotePort(port);
				ftp.setControlEncoding(encoding);
				ftp.setValidateServer(false);
				ftp.connect();
				ftp.auth(SSLFTPClient.AUTH_TLS);
				ftp.login(username, password);
				if (FtpSetting.FTP_MODE_PASV.equals(mode)) {
					ftp.setConnectMode(FTPConnectMode.PASV);
				} else if (FtpSetting.FTP_MODE_PORT.equals(mode)) {
					ftp.setConnectMode(FTPConnectMode.ACTIVE);
				}
				if (StringUtils.hasText(remotePath)) {
					ftp.chdir(remotePath);
				} else {
					ftp.chdir(".");
				}
				return ftp;
			} else if (type.equals(FtpSetting.SFTP_TYPE)) {
				SSHFTPClient ftp = new SSHFTPClient();
				ftp.setRemoteHost(host);
				ftp.setRemotePort(port);
				ftp.setControlEncoding(encoding);
				ftp.setAuthentication(username, password);
				ftp.connect();
				ftp.getValidator().setHostValidationEnabled(false);
				if (StringUtils.hasText(remotePath)) {
					ftp.chdir(remotePath);
				} else {
					ftp.chdir(".");
				}
				return ftp;
			}
		}
		return null;
	}

	/**
	 * 测试ftp配置
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static boolean testFtp(Long id) throws Exception {
		FTPClientInterface ftp = getFtp(id);
		if (ftp != null) {
			ftp.quit();
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {

		// we want remote host, user name and password
		if (args.length < 3) {
			System.out.println("Usage: run remote-host username password");
			// System.exit(1);
		}

		// extract command-line arguments
		String host = "211.157.3.202";
		String username = "root";
		String password = "mabcres070907";
		String filename = "C:\\UseSFTPWithoutServerValidation.java";

		// set up logger so that we get some output
		Logger log = Logger.getLogger(FtpService.class);
		Logger.setLevel(Level.INFO);

		try {
			// create client
			log.info("Creating SFTP client");
			SSHFTPClient ftp = new SSHFTPClient();

			// set remote host
			ftp.setRemoteHost(host);

			log.info("Setting user-name and password");
			ftp.setAuthentication(username, password);

			log.info("Turning off server validation");
			ftp.getValidator().setHostValidationEnabled(false);

			// connect to the server
			log.info("Connecting to server " + host);
			ftp.connect();

			log.info("Setting transfer mode to ASCII");
			ftp.setType(FTPTransferType.ASCII);
			FTPFile[] files = ftp.dirDetails(".");
			for (int i = 0; i < files.length; i++) {
				log.info(files[i].toString());
			}
			// putGetDelete(filename, ftp);
			log.info("Successfully transferred in ASCII mode");

			// Shut down client
			log.info("Quitting client");
			ftp.quit();

			log.info("Example complete");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Put a file, get it back as a copy and delete the local copy and the
	 * remote copy
	 * 
	 * @param name
	 *            original filename
	 * @param ftp
	 *            reference to FTP client
	 */
	private static void putGetDelete(String name, FTPClientInterface ftp)
			throws Exception {
		ftp.put(name, name);
		ftp.get(name + ".copy", name);
		// ftp.delete(name);
		File file = new File(name + ".copy");
		// file.delete();
	}

	static class LogMessageListener implements FTPMessageListener {
		public void logCommand(String cmd) {
			System.out.println("Command: " + cmd);
		}

		public void logReply(String reply) {
			System.out.println("Reply: " + reply);
		}
	}

}
