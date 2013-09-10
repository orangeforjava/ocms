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
package org.openuap.cms.search.index;

/**
 * <p>
 * 索引参数，决定如何构建索引.
 * </p>
 * 
 * 
 * <p>
 * $Id: IndexParameter.java 3921 2010-10-26 11:43:49Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class IndexParameter implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5332780286630199127L;

	/** 模型id字符串，多个Id之间用逗号分割. */
	private String modelIds;

	/** 索引模式. */
	private int mode;

	/** 内容索引Id字符串，多个Id之间用逗号分割. */
	private String indexIds;

	public static final int UPDATE_MODE = 0;

	public static final int REBUILD_MODE = 1;
	/** 单条内容模式.*/
	public static final int SINGLE_MODE=100;

	public IndexParameter() {

	}

	public IndexParameter(int mode, String modelIds) {
		this.mode = mode;
		this.modelIds = modelIds;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getModelIds() {
		return modelIds;
	}

	public void setModelIds(String modelIds) {
		this.modelIds = modelIds;
	}

	/**
	 * @return the indexIds
	 */
	public String getIndexIds() {
		return indexIds;
	}

	/**
	 * @param indexIds
	 *            the indexIds to set
	 */
	public void setIndexIds(String indexIds) {
		this.indexIds = indexIds;
	}

}
