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
package org.openuap.cms.user.config;

import org.openuap.base.util.StringUtil;
import org.openuap.runtime.config.ApplicationConfigurationException;
import org.openuap.runtime.setup.BaseApplicationConfiguration;
import org.openuap.runtime.setup.BootstrapManager;
import org.openuap.runtime.setup.BootstrapUtils;

/**
 * <p>
 * 通行证相关配置
 * </p>
 * 
 * <p>
 * $Id: UserConfig.java 3992 2011-01-05 06:34:18Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class UserConfig {
	/** 通行证控制开关 */
	private boolean enablePassport;
	/** 自定义登录URL. */
	private String loginUrl;
	/** 自定义注销URL. */
	private String logoutUrl;
	/** 自定义注册URL. */
	private String regUrl;
	/** 安全校验码开关. */
	private boolean enableSecureCode;
	/** 校验码有效期. */
	private long secureLoginDuration;
	/** 通行证Cookie域. */
	private String passportDomain;
	/** 安全加密Key. */
	private String securityKey;

	private BaseApplicationConfiguration config;

	private BootstrapManager bootstrapManager;

	private static UserConfig _instance = null;

	protected UserConfig(BootstrapManager bootstrapManager) {
		this.bootstrapManager = bootstrapManager;
		this.config = this.bootstrapManager.getApplicationConfig();
		loadConfig();
	}

	public static UserConfig getInstance() {
		if (_instance == null) {
			synchronized (UserConfig.class) {
				BootstrapManager bsm = BootstrapUtils
						.getBootstrapManager("base");
				_instance = new UserConfig(bsm);
			}

		}
		return _instance;
	}

	public void reload() {
		loadConfig();
	}

	protected void loadConfig() {
		try {
			this.config.load();
			//
			this.enablePassport = this.config.getBoolean("passport.enable",
					false);
			this.loginUrl = this.config.getString("passport.login_url", "");
			this.regUrl = this.config.getString("passport.reg_url", "");
			this.logoutUrl = this.config.getString("passport.logout_url", "");
			this.passportDomain = this.config.getString("passport.domain");
			this.enableSecureCode = this.config.getBoolean(
					"passport.securecode.enable", false);
			this.secureLoginDuration = this.config.getInt(
					"security.secure_duration", 90);

			this.securityKey = this.config.getString("sys.security.key");
			if (securityKey == null) {
				this.securityKey = StringUtil.randomString(15);
				this.config.setProperty("sys.security.key", securityKey);
				this.config.save();
			}
		} catch (ApplicationConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			this.config.load();
			this.config.setProperty("passport.enable", enablePassport);
			this.config.setProperty("passport.login_url", loginUrl);
			this.config.setProperty("passport.logout_url", logoutUrl);
			this.config.setProperty("passport.domain", passportDomain);
			this.config.setProperty("passport.securecode.enable",
					enableSecureCode);
			this.config.setProperty("security.secure_duration",
					secureLoginDuration);
			this.config.setProperty("sys.security.key", securityKey);
			this.config.save();
		} catch (ApplicationConfigurationException e) {
			e.printStackTrace();
		}
	}

	public boolean isEnablePassport() {
		return enablePassport;
	}

	public void setEnablePassport(boolean enablePassport) {
		this.enablePassport = enablePassport;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public boolean isEnableSecureCode() {
		return enableSecureCode;
	}

	public void setEnableSecureCode(boolean enableSecureCode) {
		this.enableSecureCode = enableSecureCode;
	}

	public long getSecureLoginDuration() {
		return secureLoginDuration;
	}

	public void setSecureLoginDuration(long secureLoginDuration) {
		this.secureLoginDuration = secureLoginDuration;
	}

	public String getPassportDomain() {
		return passportDomain;
	}

	public void setPassportDomain(String passportDomain) {
		this.passportDomain = passportDomain;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getRegUrl() {
		return regUrl;
	}

	public void setRegUrl(String regUrl) {
		this.regUrl = regUrl;
	}

	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}
}
