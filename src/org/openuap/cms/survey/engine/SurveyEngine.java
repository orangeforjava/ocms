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
package org.openuap.cms.survey.engine;

import org.openuap.cms.survey.model.Question;

/**
 * <p>
 * 调查内容引擎
 * </p>
 * 
 * <p>
 * $Id: SurveyEngine.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public interface SurveyEngine {
	/**
	 * 获取问题对象
	 * @param id
	 * @return
	 */
	public Question getQuestion(Long id);
	/**
	 * 获取问题对象
	 * @param id
	 * @return
	 */
	public Question getQuestion(String id);
}
