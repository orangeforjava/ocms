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
package org.openuap.cms.util.ui;

/**
 * <p>
 * 结点类型.
 * </p>
 * 
 * <p>
 * $Id: NodeType.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeType {
	private Integer type;
	private String title;
	public static final NodeType ACTUAL_NODE_TYPE = new NodeType(1,
			"实结点");
	public static final NodeType VIRTUAL_NODE_TYPE = new NodeType(2, "虚结点");
	
	public static final NodeType EXTERN_NODE_TYPE=new NodeType(3, "外部结点");
	public static final NodeType[] DEFAULT_NODE_TYPES = new NodeType[] {
			ACTUAL_NODE_TYPE, VIRTUAL_NODE_TYPE,EXTERN_NODE_TYPE };
	
	

	public NodeType() {
	}

	public NodeType(Integer type, String title) {
		this.type = type;
		this.title = title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public Integer getType() {
		return type;
	}
}
