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
package org.openuap.cms.config;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * <p>
 * Title: AbstractXmlConfiguration
 * </p>
 * 
 * <p>
 * Description:抽象Xml配置
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * <p>
 * $Id: AbstractXmlConfiguration.java 3925 2010-10-26 11:54:14Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public abstract class AbstractXmlConfiguration implements Configuration {
	/** 配置文件. */
	private File configFile;

	
	protected XMLConfiguration configuration = null;
	/** 是否已经配置. */
	private boolean isConfiged = false;

	public AbstractXmlConfiguration() {

	}

	/**
	 * 构造函数
	 * 
	 * @param configFile
	 *            File
	 * @throws ConfigurationException
	 */
	public AbstractXmlConfiguration(File configFile)
			throws ConfigurationException {
		this.configFile = configFile;
	}

	protected void initConfig() {
		if (isConfiged) {
			return;
		}
		//
		if (configFile != null) {
			try {
				configuration = new XMLConfiguration(configFile);
				configuration.setFile(configFile);
				configuration.setEncoding("UTF-8");
			} catch (ConfigurationException ex) {
			}
		}
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void reload() {
		this.configuration.reload();
	}

	public Configuration subset(String prefix) {
		return configuration.subset(prefix);
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean containsKey(String string) {
		return false;
	}

	public void addProperty(String key, Object value) {
		this.configuration.addProperty(key, value);
	}

	public void setProperty(String key, Object value) {
		this.configuration.setProperty(key, value);
	}

	public void clearProperty(String key) {
		configuration.clearProperty(key);
	}

	public void clear() {
		configuration.clear();
	}

	public Object getProperty(String key) {
		return configuration.getProperty(key);
	}

	public Object getProperty(String key, Object defaultValue) {
		Object tempValue = configuration.getProperty(key);
		if (tempValue == null)
			return defaultValue;
		return configuration.getProperty(key);
	}

	public Iterator getKeys(String string) {
		return configuration.getKeys(string);
	}

	public Iterator getKeys() {
		return configuration.getKeys();
	}

	public Properties getProperties(String string) {
		return configuration.getProperties(string);
	}

	public boolean getBoolean(String key) {
		return configuration.getBoolean(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	public byte getByte(String key) {
		return configuration.getByte(key);
	}

	public byte getByte(String key, byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	public Byte getByte(String key, Byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	public double getDouble(String key) {
		return configuration.getDouble(key);
	}

	public double getDouble(String key, double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	public Double getDouble(String key, Double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	public float getFloat(String key) {
		return configuration.getFloat(key);
	}

	public float getFloat(String key, float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	public Float getFloat(String key, Float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	public int getInt(String key) {
		return configuration.getInt(key);
	}

	public int getInt(String key, int defaultValue) {
		return configuration.getInt(key, defaultValue);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		return configuration.getInteger(key, defaultValue);
	}

	public long getLong(String key) {
		return configuration.getLong(key);
	}

	public long getLong(String key, long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	public Long getLong(String key, Long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	public short getShort(String key) {
		return configuration.getShort(key);
	}

	public short getShort(String key, short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	public Short getShort(String key, Short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	public BigDecimal getBigDecimal(String key) {
		return configuration.getBigDecimal(key);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return configuration.getBigDecimal(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return configuration.getBigInteger(key);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return configuration.getBigInteger(key, defaultValue);
	}

	public String getString(String key) {

		String value = configuration.getString(key);
		if (value != null) {
			return value;
		} else {
			return null;
		}
	}

	public String getString(String key, String defaultValue) {
		String value = configuration.getString(key);
		if (value != null) {
			return evaluate(value);
		} else {
			return defaultValue;
		}
	}

	public String[] getStringArray(String string) {
		return configuration.getStringArray(string);
	}

	public List getList(String string) {
		return configuration.getList(string);
	}

	public List getList(String string, List list) {
		return configuration.getList(string, list);
	}

	/**
	 * 处理属性引用方式的属性，本方法仅为了兼容Jute 按照Configuration规范，应该是不包括根标记，以及采用"."方式分割属性
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	public String evaluate(String value) {
		int pos = value.indexOf("${\"");
		if (pos < 0)
			return value;
		int end = value.indexOf("\"}");
		if (end < pos + 2)
			return value;
		String alias = value.substring(pos + 3, end).trim();
		int sys_pos = alias.indexOf("/sys/");
		if (sys_pos != -1) {
			alias = alias.substring(5);
		}
		alias = alias.replaceAll("/", ".");
		String alias_value = getString(alias, "");
		if (alias_value == null) {
			return value;
		} else {
			value = value.substring(0, pos) + alias_value
					+ value.substring(end + 2);
			return value;
		}
	}

	public XMLConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = (XMLConfiguration)configuration;
		
	}

}
