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
package org.openuap.cms.node.ui;

/**
 * <p>
 * 结点种类定义
 * </p>
 * 
 * <p>
 * $Id: NodeKind.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeKind implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7270277916203112893L;
	private Integer kind;
	private String title;
	public static final NodeKind NODE_KIND_SITE = new NodeKind(0, "站点");
	public static final NodeKind NODE_KIND_CHANNEL = new NodeKind(1, "频道");

	public static final NodeKind NODE_KIND_COLUMN = new NodeKind(2, "栏目");
	public static final NodeKind[] DEFAULT_NODE_KINDS = new NodeKind[] {
			NODE_KIND_SITE, NODE_KIND_CHANNEL, NODE_KIND_COLUMN };

	public NodeKind(Integer kind, String title) {
		this.kind = kind;
		this.title = title;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
