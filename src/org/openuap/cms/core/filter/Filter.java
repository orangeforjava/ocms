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
package org.openuap.cms.core.filter;

import org.apache.oro.text.PatternCacheLRU;
import org.apache.oro.text.perl.Perl5Util;

/**
 * <p>
 * 正则表达式过滤器
 * </p>
 * 
 * <p>
 * $Id: Filter.java 4025 2011-03-22 14:57:57Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class Filter {

	public static final PatternCacheLRU regexp = new PatternCacheLRU(150);

	public static final Perl5Util perl = new Perl5Util();

	public static final String QUOTE_COLOR = "#0000A0";

	public static String substituteLeadingSpace(String str) {
		if (str != null) {
			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (i = 0; i < str.length(); i++) {
				if (str.charAt(i) != ' ') {
					break;
				}
				sb.append("&nbsp;");
			}

			if (i > 0) {
				sb.append(str.substring(i));
				str = sb.toString();
			}
		}
		return str;
	}

	public Filter() {
	}

	public static void main(String[] args) {
		//
//		String testStr0 = "where p.tags like'%湖光 水色%'";
//		testStr0 = Filter.perl.substitute(
//				"s#([\\s\\S]*)([\\s]+)like'%([^%]+)%'([\\s\\S]*)#$1:($3)$4#ig",
//				testStr0);
//		System.out.println("testStr=" + testStr0);
//		System.exit(0);
		//not like
		//!=
		//<>
		//in
		//
		String testStr00 = "where pcc.tags not like '%湖光 水色1%' and userName!='鞠伟平' and userName<>'make'";
		testStr00 = Filter.perl.substitute(
				"s#([\\s\\S]*?)([\\S]*?)[.]([\\S]*?)([\\s]*)([\\s\\S]*?)#$1$3#ig",
				testStr00);
		System.out.println("testStr00=" + testStr00);
		//not like implement
		String testStr01 = "where tags not like '%湖光 水色%'";
		testStr01 = Filter.perl.substitute(
				"s#([\\s\\S]*?)([\\s]*)([\\S]*?)([\\s]*)not like([\\s]*)'%([^%]+)%'([\\s\\S]*)#$1 !$3:($6)$7#ig",
				testStr00);
		System.out.println("testStr01=" + testStr01);
		//System.out.println("g3="+Filter.perl.group(2));
		
		//like implement
		String testStr = "where tags like '%湖光 水色%'";
		testStr = Filter.perl.substitute(
				"s#([\\s\\S]*?)([\\s]+)like([\\s]*)'%([^%]+)%'([\\s\\S]*)#$1:($4)$5#ig",
				testStr);
		
		System.out.println("testStr like=" + testStr);
		//!=
		String testStr2 = " Photo!='123' and Photo!='234'";
		testStr2 = Filter.perl.substitute(
				"s#([\\s\\S]*?)([\\s]*)([\\S]*?)([\\s]*)!='([^']+)'([\\s\\S]*?)#$1 !$3:($5)$6#ig",
				testStr2);
		System.out.println("testStr2=" + testStr2);
		//<>
		String testStr20 = Filter.perl.substitute(
				"s#([\\s\\S]*?)([\\s]*)([\\S]*?)([\\s]*)<>'([^']+)'([\\s\\S]*?)#$1 !$3:($5)$6#ig",
				testStr01);
		System.out.println("testStr20=" + testStr20);
		//
		String testStr21 = "where userName='鞠伟平'";
		testStr21 = Filter.perl.substitute(
				"s#([\\s\\S]*)([\\s]*)='([^']+)'([\\s\\S]*)#$1:($3)$4#ig",
				testStr21);
		System.out.println("testStr21=" + testStr21);
		String testStr3 = "where pubishType=0 ";
		testStr3 = Filter.perl.substitute(
				"s#([\\s\\S]*)([\\s]*)=([^\\s]+)([\\s\\S]*)#$1:($3)$4#ig",
				testStr3);
		System.out.println("testStr3=" + testStr3);

	}
}
