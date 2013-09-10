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
package org.openuap.cms.node.model;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 结点集合.
 * </p>
 * 
 * 
 * <p>
 * $Id: Nodes.java 3923 2010-10-26 11:50:24Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class Nodes implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8326970851302655430L;

	private List nodes;

	public Nodes() {
	}

	public int getCount() {
		if (nodes != null) {
			return nodes.size();
		}
		return 0;
	}

	public List getNodes() {
		return nodes;
	}

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}
}
