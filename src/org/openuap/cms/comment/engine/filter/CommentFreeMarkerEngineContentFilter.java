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
package org.openuap.cms.comment.engine.filter;

import java.util.HashMap;
import java.util.Map;

import org.openuap.cms.comment.engine.CommentMacroEngine;
import org.openuap.tpl.engine.plugin.FreeMarkerEngineContentFilter;

import freemarker.template.Configuration;

/**
 * <p>
 * 评论模板引擎内容过滤器.
 * </p>
 * 
 * <p>
 * $Id: CommentFreeMarkerEngineContentFilter.java 3950 2010-11-02 09:10:01Z orangeforjava $
 * </p>
 * 
 * @author joseph
 * @version 1.0
 */
public class CommentFreeMarkerEngineContentFilter implements
		FreeMarkerEngineContentFilter {
	
	private int priority = 5;
	private CommentMacroEngine commentMacroEngine;
	
	

	public void setCommentMacroEngine(CommentMacroEngine commentMacroEngine) {
		this.commentMacroEngine = commentMacroEngine;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String afterProcessContent(String arg0, Map arg1, Configuration arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public String beforeProcessContent(String arg0, Map arg1, Configuration arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getMacroDefinitions() {
		Map tagMacros = new HashMap();
		tagMacros.put("comment", "/plugin/cms/comment/macros/comment-macros.html");
		//
		return tagMacros;
	}

	public int getPriority() {
		return priority;
	}

	public Map getSharedVariables(String arg0, Map arg1, Configuration arg2) {
		Map<String, Object> inModel = new HashMap<String, Object>();
		inModel.put("commentMacroEngine", commentMacroEngine);
		return inModel;
	}

}
