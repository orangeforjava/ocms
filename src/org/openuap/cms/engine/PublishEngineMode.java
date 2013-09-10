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
package org.openuap.cms.engine;

/**
 * <p>
 * 发布模式（发布的一些选项）
 * </p>
 * 
 * <p>
 * $Id: PublishEngineMode.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishEngineMode implements java.io.Serializable {

	private static final long serialVersionUID = -3104883395207475072L;

	/** 是否包含子结点. */
	private boolean containChildNode;

	/** 是否包含附加发布页面. */
	private boolean containExtraPublish;

	/** 是否包含内容页. */
	private boolean containContent;

	/** 是否包含首页. */
	private boolean containIndex;

	/** 每次处理内容页面数量. */
	private int processContentNums;

	/** 是否在发生错误时终止. */
	private boolean terminateOnError;

	/** 是否同时刷新内容. */
	private boolean refreshContent;

	/** 发布模式变量. */
	private int mode;

	/** 发布模式常量. */
	public static final int PUBLISH_MODE = 0;

	/** 更新模式常量. */
	public static final int REFRESH_MODE = 1;

	/** 取消发布模式. */
	public static final int UNPUBLISH_MODE = 2;

	/** 重新发布模式. */
	public static final int REPUBLISH_MODE = 3;

	/**
	 * 构造函数
	 */
	public PublishEngineMode() {
	}

	/**
	 * 是否包含子结点
	 * 
	 * @return 是返回true ,否返回false
	 */
	public boolean isContainChildNode() {
		return containChildNode;
	}

	/**
	 * 获得每次处理的内容数量
	 * 
	 * @return 每次处理的内容数量
	 */
	public int getProcessContentNums() {
		return processContentNums;
	}

	/**
	 * 是否在发生错误的时候终止处理
	 * 
	 * @return 是返回true,否返回false
	 */
	public boolean isTerminateOnError() {
		return terminateOnError;
	}

	/**
	 * 是否包含附加发布页面
	 * 
	 * @return 是返回true,否返回false
	 */
	public boolean isContainExtraPublish() {
		return containExtraPublish;
	}

	/**
	 * 是否包含内容页面
	 * 
	 * @return 是返回true,否返回false
	 */
	public boolean isContainContent() {
		return containContent;
	}

	/**
	 * 是否包含首页页面
	 * 
	 * @return 是返回true,否返回false
	 */
	public boolean isContainIndex() {
		return containIndex;
	}

	/**
	 * 返回发布模式
	 * 
	 * @return 发布模式
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * 设置是否包含子结点
	 * 
	 * @param containChildNode
	 *            是否包含子结点
	 */
	public void setContainChildNode(boolean containChildNode) {
		this.containChildNode = containChildNode;
	}

	/**
	 * 设置每次处理的内容数目
	 * 
	 * @param processContentNums
	 *            每次处理的内容数目
	 */
	public void setProcessContentNums(int processContentNums) {
		this.processContentNums = processContentNums;
	}

	/**
	 * 设置是否在遇到错误的时候终止
	 * 
	 * @param terminateOnError
	 *            是否在遇到错误的时候终止
	 */
	public void setTerminateOnError(boolean terminateOnError) {
		this.terminateOnError = terminateOnError;
	}

	/**
	 * 设置是否包含附加发布
	 * 
	 * @param containExtraPublish
	 *            是否包含附加发布
	 */
	public void setContainExtraPublish(boolean containExtraPublish) {
		this.containExtraPublish = containExtraPublish;
	}

	/**
	 * 设置是否包含内容页面
	 * 
	 * @param containContent
	 *            是否包含内容页面
	 */
	public void setContainContent(boolean containContent) {
		this.containContent = containContent;
	}

	/**
	 * 设置是否包含首页
	 * 
	 * @param containIndex
	 *            是否包含首页
	 */
	public void setContainIndex(boolean containIndex) {
		this.containIndex = containIndex;
	}

	/**
	 * 设置发布模式
	 * 
	 * @param mode
	 *            发布模式
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the refreshContent
	 */
	public boolean isRefreshContent() {
		return refreshContent;
	}

	/**
	 * @param refreshContent the refreshContent to set
	 */
	public void setRefreshContent(boolean refreshContent) {
		this.refreshContent = refreshContent;
	}
}
