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
package org.openuap.cms.survey.security;

/**
 * <p>
 * 调查问卷模块权限常量
 * </p>
 * 
 * <p>
 * $Id: SurveyPermissionConstant.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * 
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class SurveyPermissionConstant {
	public static final String OBJECT_TYPE = "org.openuap.cms.survey";
	public static final String ALL_OBJECT = "-1";
	public static final long Admin = Long.MAX_VALUE;
	// 查看调查
	public final static long ViewSurvey = 1L << 1;
	// 添加调查
	public final static long AddSurvey = 1L << 2;
	// 编辑调查
	public final static long EditSurvey = 1L << 3;
	// 删除调查
	public final static long DeleteSurvey = 1L << 4;
	// 查看调查活动
	public final static long ViewSurveyRecord = 1L << 5;

	public final static long AddSurveyRecord = 1L << 6;

	public final static long EditSurveyRecord = 1L << 7;

	public final static long DeleteSurveyRecord = 1L << 8;
	//
	private String objectType = OBJECT_TYPE;
}
