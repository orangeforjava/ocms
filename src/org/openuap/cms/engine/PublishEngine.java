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
package org.openuap.cms.engine;

import java.util.List;

/**
 * <p>
 * 内容发布引擎.
 * </p>
 * 
 * <p>
 * $Id: PublishEngine.java 3966 2010-12-16 12:10:02Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0 
 */
public interface PublishEngine {
	/**
	 * 发布特定的内容页面
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            内容索引Id
	 * @param errors
	 *            发布过程中的错误
	 * @return boolean 是否发布成功
	 */
	public boolean publishContent(Long nodeId, Long indexId, boolean refreshContent, List errors);

	/**
	 * 取消内容发布
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            内容索引Id
	 * @param errors
	 *            取消发布过程中的错误
	 * @return boolean
	 */
	public boolean unPublishContent(Long nodeId, Long indexId, List errors);

	/**
	 * 刷新内容，模版不变，内容发生变化
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            内容索引Id
	 * @param errors
	 *            刷新过程中的错误
	 * @return 若出现错误返回false,成功返回true
	 */
	public boolean refreshContent(Long nodeId, Long indexId, List errors);

	/**
	 * publish the node page,and the subnode,and content page
	 * 结点发布，按照发布模式来选择是否发布附加页面，子结点，内容页面
	 * 
	 * @param parentId
	 *            发布的目标结点Id
	 * @param mode
	 *            发布模式
	 * @param errors
	 *            发布结点过程中的错误
	 * @return boolean 若出现错误返回false,成功返回true
	 */
	public boolean publishAllNodeContent(Long parentId, PublishEngineMode mode, List errors);

	/**
	 * 结点更新，按照发布模式来选择是否刷新附加页面，子结点，内容页面
	 * 
	 * @param parentId
	 *            更新的目标
	 * @param mode
	 *            更新模式
	 * @param errors
	 *            更新过程中的错误
	 * @return 若出现错误返回false,成功返回true
	 */
	public boolean refreshAllNodeContent(Long parentId, PublishEngineMode mode, List errors);

	/**
	 * 重新发布指定结点内容，相当于全部取消->全部发布
	 * 
	 * @param parentId
	 *            结点Id
	 * @param mode
	 *            发布模式
	 * @param errors
	 *            重新发布过程中的错误
	 * @return boolean 若出现错误返回false,成功返回true
	 */
	public boolean republishAllNodeContent(Long parentId, PublishEngineMode mode, List errors);

	/**
	 * 取消发布指定结点的内容，根据发布模式来处理
	 * 
	 * @param parentId
	 *            结点Id
	 * @param mode
	 *            发布模式
	 * @param errors
	 *            取消发布过程中出现的错误
	 * @return boolean 若出现错误返回false,成功返回true
	 */
	public boolean unpublishAllNodeContent(Long parentId, PublishEngineMode mode, List errors);

	/**
	 * 更新结点首页
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param errors
	 *            更新结点首页过程中出现的错误
	 * @return boolean 若出现错误返回false,成功返回true
	 */
	public boolean refreshNodeIndex(Long nodeId, List errors);

	/**
	 * publish the node extra pulish file. 更新结点附加页面
	 * 
	 * @param nodeId
	 *            结点id
	 * @param publishId
	 *            附加发布Id
	 * @param errors
	 *            更新结点附加页面过程中出现的错误
	 * @return boolean 若出现错误返回false,成功返回true
	 */
	public boolean refreshNodeExtraIndex(Long nodeId, Long publishId, List errors);

	
	/**
	 * 批量更新下的所有附加发布
	 * 
	 * @param nodeId
	 *            结点id
	 * @param errors
	 *            更新过程中的错误
	 * @return 若出现错误返回false,成功返回true
	 */
	public boolean refreshNodeAllExtraIndex(Long nodeId, List errors);

	/**
	 * 删除发布后的静态文件
	 * 
	 * @param nodeId
	 *            结点id
	 * @param indexId
	 *            索引id
	 * @param errors
	 *            操作过程中的错误
	 * @return 若出现错误返回false,成功返回true
	 */
	public boolean deletePublishedFile(Long nodeId, Long indexId, List errors);

	/**
	 * it is a method to previewTemplate 模版预览
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            索引Id
	 * @param extraPublishId
	 *            附加发布Id
	 * @param type
	 *            int 类型
	 * @param tplContent
	 *            模版内容
	 * @param page
	 *            int
	 * @param errors
	 *            List 预览过程中的错误
	 * @return 模版处理后的内容
	 */
	public String previewTemplate(Long nodeId, Long indexId, Long extraPublishId, int type, String tplContent,
			int page, List errors);

	/**
	 * 获得指定索引id的内容模版处理后结果 用于动态发布或者调试
	 * 
	 * @param nodeId
	 *            Long
	 * @param indexId
	 *            Long
	 * @param page
	 *            int
	 * @param errors
	 *            List
	 * @return String
	 */
	public String getContent(Long nodeId, Long indexId, int page, List errors);
	public String getPreviewContent(Long nodeId, Long indexId, int page, List errors);
	/**
	 * 获得指定结点首页的模板处理后内容
	 * 
	 * @param nodeId
	 *            结点id
	 * @param page
	 *            int
	 * @param errors
	 *            异常列表
	 * @return String
	 */
	public String getNodeIndex(Long nodeId, int page, List errors);

	/**
	 * 获得指定附加页面的模版处理后内容
	 * 
	 * @param nodeId
	 *            Long
	 * @param publishId
	 *            Long
	 * @param page
	 *            int
	 * @param errors
	 *            List
	 * @return String
	 */
	public String getExtraContent(Long nodeId, Long publishId, int page, List errors);

}
