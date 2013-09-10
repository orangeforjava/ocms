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
/**
 * 
 */
package org.openuap.cms.engine.generate;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.openuap.cms.engine.PublishEngineMode;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.repo.manager.DynamicContentManager;
import org.openuap.runtime.plugin.view.freemarker.PluginFreeMarkerConfigurer;

/**
 * <p>
 * 静态内容生成引擎.
 * </p>
 * 
 * <p>
 * $Id: StaticFileGenerateEngine.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 * @deprecated
 */
public interface StaticFileGenerateEngine {
	/**
	 * 获得宏中的服务对象Map
	 * 
	 * @return
	 */
	public Map getMacroServiceMap();

	/**
	 * 设置内容生成的模板引擎
	 * 
	 * @param configurer
	 *            内容生成的模板引擎
	 */
	public void setFreemarkerConfigurer(PluginFreeMarkerConfigurer configurer);

	/**
	 * 设置动态内容管理对象
	 * 
	 * @param dynamicContentManager
	 *            动态内容管理对象
	 */
	public void setDynamicContentManager(DynamicContentManager dynamicContentManager);

	/**
	 * 设置结点管理对象
	 * 
	 * @param nodeManager
	 *            结点管理对象
	 */
	public void setNodeManager(NodeManager nodeManager);

	/**
	 * 设置发布点管理对象
	 * 
	 * @param psnManager
	 *            发布点管理对象
	 */
	public void setPsnManager(PsnManager psnManager);

	/**
	 * 产生指定内容页面,对应刷新内容动作
	 * 
	 * @param nodeId
	 *            内容所在的结点Id
	 * 
	 * @param indexId
	 *            内容索引Id
	 * 
	 * @param errors
	 *            错误列表
	 * @return boolean
	 */
	public boolean generateContentStaticFile(Long nodeId, Long indexId, List errors);

	/**
	 * 产生模板预览内容
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            内容索引Id
	 * @param tplContent
	 *            模板内容
	 * @param extraPublishId
	 *            附加发布Id
	 * @param type
	 *            类型
	 * @param page
	 *            第几页，如果存在分页的话
	 * @param errors
	 *            错误列表
	 * @return 产生的模板预览内容
	 */
	public String previewTemplate(Long nodeId, Long indexId, String tplContent,
			Long extraPublishId, int type, int page, List errors);

	/**
	 * 生成指定结点的首页文件
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param errors
	 *            错误列表
	 * @return boolean
	 */
	public boolean generateNodeIndexStaticFile(Long nodeId, List errors);

	/**
	 * 
	 * 产生指定结点的所有附加发布内容
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param errors
	 *            错误列表
	 * @return boolean
	 */
	public boolean generateNodeAllExtraIndexStaticFile(Long nodeId, List errors);

	/**
	 * 产生指定结点的附加发布内容
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param publishId
	 *            附加发布Id
	 * @param errors
	 *            错误列表
	 * @return boolean
	 */
	public boolean generateNodeExtraIndexStaticFile(Long nodeId, Long publishId, List errors);

	public File getDestFile(Long nodeId, Long indexId);

	public File getDestDir(Long nodeId, Long indexId);

	/**
	 * 生成指定结点的所有子节点内容
	 * 
	 * @param parentId
	 *            父节点Id
	 * @param mode
	 *            生成模式
	 * @param errors
	 *            错误列表
	 * @return boolean
	 */
	public boolean generateAllNodeStaticFile(Long parentId, PublishEngineMode mode, List errors);

	/**
	 * 获得指定索引id的内容模版处理后结果 用于动态发布或者调试
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param indexId
	 *            内容索引Id
	 * @param page
	 *            页序号
	 * @param errors
	 *            错误列表
	 * @return 生成的内容
	 */
	public String getContent(Long nodeId, Long indexId, int page, List errors);

	/**
	 * 获得指定结点首页的模板处理后内容
	 * 
	 * @param nodeId
	 *            结点Id
	 * @param page
	 *            页序号
	 * @param errors
	 *            错误列表
	 * @return 生成的内容
	 */
	public String getNodeIndex(Long nodeId, int page, List errors);

	/**
	 * 获得指定附加页面的模版处理后内容
	 * 
	 * @param nodeId
	 *            结点
	 * @param publishId
	 *            发布Id
	 * @param page
	 *            页面序号
	 * @param errors
	 *            错误列表
	 * @return 生成的内容
	 */
	public String getExtraContent(Long nodeId, Long publishId, int page, List errors);
}
