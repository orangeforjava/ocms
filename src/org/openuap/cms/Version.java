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
package org.openuap.cms;

/**
 * <p>
 * CMS版本信息
 * </p>
 * 
 * <p>
 * $Id: Version.java 3940 2010-10-27 11:08:37Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class Version {
	/**主版本号.*/
	public static final int MAJOR_VERSION = 4;

	/** 次版本号. */
	public static final int MINOR_VERSION = 0;

	/**	修订版本号.*/
	public static final int REVISION_VERSION = 0;

	/** 版本字符串.*/
	public static final String VERSION_STRING = "Beta";

	/** 产品版本.*/
	public static final Edition EDITION = Edition.PROFESSIONAL;

	/**
	 * 获取产品完整版本号.
	 * 
	 * @return String
	 */
	public static String getVersionNumber() {
		if (VERSION_STRING != null) {
			return MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION
					+ " " + VERSION_STRING;
		} else {
			return MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
		}
	}

	/**
	 * 版本类.
	 * 
	 * @return the edition of OpenUAP CMS.
	 */
	public static Edition getEdition() {
		return EDITION;
	}

	/**
	 * 版本类.
	 */
	public static class Edition {

		/**
		 * 专业版
		 */
		public static final Edition PROFESSIONAL = new Edition("Professional");

		/**
		 * 企业版
		 */
		public static final Edition ENTERPRISE = new Edition("Enterprise");

		/**
		 * 专家版
		 */

		public static final Edition EXPERT = new Edition("Expert Edition");

		private String name;

		private Edition(String name) {
			this.name = name;
		}

		/**
		 * 获取版本名
		 * 
		 * @return the name of the edition.
		 */
		public String getName() {
			return name;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			} else if (object instanceof Edition
					&& name.equals(((Edition) object).name)) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	@Override
	public String toString() {
		return getVersionNumber();
	}

	public Version() {
		// Not publically instantiable.
	}

}
