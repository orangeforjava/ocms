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
package org.openuap.cms.util;

/**
 * <p>
 * 分页位置信息
 * </p>
 * <p>
 * $Id: PositionInfo.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 3.0
 */
public class PositionInfo{
	public int start;
	public int end;
	public PositionInfo(){
		
	}
	public PositionInfo(int start,int end){
		this.start=start;
		this.end=end;
	}
	
}