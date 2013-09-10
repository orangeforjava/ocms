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
package org.openuap.cms.psn.event;

import org.openuap.event.Event;

/**
 * <p>
 * PSN事件
 * </p>
 * <p>
 * $Id: PsnEvent.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PsnEvent extends Event{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4503830320843719443L;
	
	public PsnEvent(Object source) {
		super(source);		
	}
	
}
