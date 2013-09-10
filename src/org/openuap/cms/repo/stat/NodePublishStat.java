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
package org.openuap.cms.repo.stat;

/**
 * <p>
 * 结点发布统计信息，解决重复计算问题
 * </p>
 * 
 * <p>
 * $Id: NodePublishStat.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodePublishStat implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2396647956228566607L;
	/** 内容数目.*/
	private long contentCount;
	/** 发布内容数目.*/
	private long publishedConentCount;
	/** 结点Id.*/
	private long nodeId;

	public long getContentCount() {
		return contentCount;
	}

	public void setContentCount(long contentCount) {
		this.contentCount = contentCount;
	}

	public long getPublishedConentCount() {
		return publishedConentCount;
	}

	public void setPublishedConentCount(long publishedConentCount) {
		this.publishedConentCount = publishedConentCount;
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}
}
