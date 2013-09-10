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
package org.openuap.cms.badwords.filter;

import org.openuap.cms.core.filter.Filter;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 垃圾内容替换
 * </p>
 * <p>
 * $Id: GarbageFilter.java 4027 2011-03-22 15:00:42Z orangeforjava $
 * </p>
 * @author Joseph
 * @version 1.0
 */
public class GarbageFilter extends Filter{
	
	public static String doFilter(String input){
		String rs=input;
		if(StringUtils.hasText(input)){
			//替换掉连续的字母
			rs = Filter.perl.substitute("s#((\\S){8,})#$2...#ig", input);
		}
		return rs;
	}
	public static void main(String[] args) {
		//
		System.out.println("rs="+doFilter("一一一一一一一一一一一一一一一一 aaaaaaaaaaaa 11111111111111 222222"));
	}
}
