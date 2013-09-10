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
package org.openuap.cms.util;

import org.openuap.base.orm.BaseEntity;
import org.openuap.base.util.StringUtil;
import org.openuap.base.orm.EntityEnvironment;
import org.openuap.base.orm.dialect.Dialect;
import org.openuap.base.orm.dialect.Oracle9Dialect;

/**
 * <p>
 * CMS实体基类.
 * </p>
 * 
 * <p>
 * $Id: CmsEntity.java 3961 2010-11-11 03:06:16Z orangeforjava $
 * </p>
 * 
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public abstract class CmsEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8464227984376158731L;
	/** SQL方言. */
	private Dialect dialect;

	/**
	 * 
	 * 
	 */
	public CmsEntity() {
		dialect = EntityEnvironment.getDialect();
	}

	/**
	 * 获得缺省的表名
	 */
	public String getTable() {

		return "cms_" + StringUtil.getClassName(getClass()).toLowerCase();

	}

	/**
	 * 获得缺省的主键名
	 */
	public String getPrimaryKey() {
		return "id";
	}

	/**
	 * 获得缺省的对象Id方法
	 */
	public String getOIDMethod() {
		if (dialect instanceof Oracle9Dialect) {
			return "sequence";
		} else {
			return "autoincrement";
		}
	}

}
