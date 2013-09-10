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
package org.openuap.cms.engine.content;

import java.util.List;

import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.repo.model.ContentIndex;

/**
 * <p>
 * 内容发布引擎，在数据库层面做内容的处理.
 * </p>
 * 
 * <p>
 * $Id: ContentPublishEngine.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface ContentPublishEngine {
	/**
	 * publish the content,create/update the publish_? content and update the
	 * url info. only static publish should process!
	 * 
	 * @param nodeId
	 *            Integer
	 * @param indexId
	 *            Integer
	 * @param errors
	 *            the errors occurs when process.
	 * @return if should be process continue,it return true,else if should <br/>
	 *         terminate the process,it should return false.
	 */
	public boolean contentPublish(Long nodeId, Long indexId, boolean refreshContent, List errors);

	/**
	 * 内容取消发布
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            内容索引Id
	 * @param errors
	 *            操作中的错误
	 * @return 若无错误返回true,否则返回false
	 */
	public boolean contentUnPublish(Long nodeId, Long indexId, List errors);

	/**
	 * 结点内容发布
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param mode
	 *            发布模式
	 * @param errors
	 *            操作中的错误
	 * @return 若无错误返回true,否则返回false
	 */
	public boolean nodeAllContentPublish(Long nodeId, PublishEngineMode mode, List errors);

	/**
	 * 结点内容的取消发布
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param mode
	 *            发布模式
	 * @param errors
	 *            操作中的错误
	 * @return 若无错误返回true,否则返回false
	 */
	public boolean nodeAllContentUnPublish(Long nodeId, PublishEngineMode mode, List errors);

	/**
	 * 结点内容的重新发布
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param mode
	 *            发布模式
	 * @param errors
	 *            操作中的错误
	 * @return 若无错误返回true,否则返回false
	 */
	public boolean nodeAllContentRePublish(Long nodeId, PublishEngineMode mode, List errors);

	/**
	 * 获得子目录名称
	 * 
	 * @param subDir
	 *            String
	 * @param timeStamp
	 *            long
	 * @return java.lang.String
	 */
	public String getSubDirName(String subDir, long timeStamp);

	/**
	 * 获得发布文件名称
	 * 
	 * @param fileName
	 *            String
	 * @param timeStamp
	 *            long
	 * @param ci
	 *            ContentIndex
	 * @return java.lang.String
	 */
	public String getPublishFileName(String fileName, long timeStamp, ContentIndex ci);
}
