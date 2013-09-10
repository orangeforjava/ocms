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
package org.openuap.cms.node.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.openuap.base.dao.hibernate.BaseObject;
import org.openuap.cms.CmsPlugin;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.runtime.util.ObjectLocator;

/**
 * 
 * <p>
 * 结点实体.
 * </p>
 * 
 * <p>
 * $Id: Node.java 4017 2011-03-13 13:55:50Z orangeforjava $
 * </p>
 * 
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class Node extends BaseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3130837026129518200L;

	private int hashValue = 0;

	/** 结点id. */
	private Long nodeId;

	/** 结点全局id. */
	private java.lang.String nodeGuid;

	/** 内容模型id. */
	private Long tableId;

	/** 父结点id. */
	private Long parentId;

	/** 根结点id. */
	private Long rootId;

	/** 继承结点id. */
	private Long inheritNodeId;

	/** 实结点,虚结点. */
	private Integer nodeType;

	/** 结点排序. */
	private Long nodeSort;

	/** 结点名称. */
	private String name;

	/** 内容发布点. */
	private String contentPsn;

	/** 内容发布url. */
	private String contentUrl;

	/** 资源发布点. */
	private String resourcePsn;

	/** 资源发布点url. */
	private String resourceUrl;

	/** 发布模式,不,静态,动态. */
	private Integer publishMode;

	/** 首页模板. */
	private String indexTpl;

	/** 首页发布文件名. */
	private String indexName;

	/** 内容页模板. */
	private String contentTpl;

	/** 图片页模板. */
	private String imageTpl;

	/** 发布目录格式. */
	private String subDir;

	/** 发布文件格式. */
	private String publishFileFormat;

	/** 是否允许评论. */
	private Integer comment;

	/** 最大评论长度. */
	private Integer commentLength;

	/** 是否允许打印. */
	private Integer print;

	/** 是否允许评价. */
	private Integer grade;

	/** 是否允许以email方式发送. */
	private Integer mail;

	/** 节点是否可用. */
	private Integer disabled;

	/** 是否自动发布. */
	private Integer autoPublish;

	/** 首页Portal URL. */
	private String indexPortalUrl;

	/** 内容页Portal URL. */
	private String contentPortalUrl;

	/** 附加发布页URL. */
	private String extraPortalUrl;

	/** 内容分页器. */
	private String pager;

	/** 内容编辑器. */
	private String editor;

	/** 审批工作流. */
	private Long workflow;

	/** 产生用户id. */
	private Long creationUserId;

	/** 产生日期. */
	private Long creationDate;

	/** 最后修改日期. */
	private Long lastModifyDate;

	/** 最后修改用户id. */
	private Long lastModifyUserId;

	/** 结点url. */
	private String nodeUrl;

	/** 是否是系统结点. */
	private Integer system;

	/** 管理url. */
	private String manageUrl;

	/** 发布url. */
	private String publishUrl;

	/** 内容表名. */
	private String tableName;

	private List navigation;

	private Set contentTables;
	/** 子结点数目. */
	private int child;
	/** 树型排序依据,parentId.childSort. */
	private String sorter;

	private String description;
	private String style;
	private String tplEncoding;
	private String outEncoding;
	// rss模板
	private String rssTpl;
	// rss条目数
	private Integer rssNums;
	private String seoTitle;
	private String seoKeywords;
	private String seoDescription;
	// 防止复制，盗链功能状态，0关闭，1启用
	private Integer protectStatus;
	// 是否加入导航菜单
	private Integer menuStatus;
	// 导航菜单顺序
	private Integer menuPos;
	// 菜单名称
	private Integer menuName;
	// 结点图片
	private String nodeIcon;
	// 结点是否允许添加内容,0允许，-1不允许
	private Integer addContentStatus;
	// 结点种类,站点，频道，栏目
	private Integer nodeKind;
	// 投稿状态，0不允许，1允许
	private Integer contributionStatus;
	/** 自动刷新模式，0-不刷新，1-同级刷新，2-父级刷新，3-全局刷新. */
	private Integer autoRefreshMode;

	/** 总内容数目. */
	private Long contentCount;
	/** 发布内容数目. */
	private Long publishedConentCount;
	/** 内容分组依据. */
	private String groupBy;

	/**
	 * 获取标题样式中的颜色信息,如color:#FF0000
	 * 
	 * @return
	 */
	public String getTitleColor() {
		if (style != null) {
			String[] styles = style.split(";");
			if (styles.length > 0) {
				return styles[0] + ";";
			}
		}
		return "";
	}
	/**
	 * 获取标题颜色,如#FF0000
	 * @return
	 */
	public String getColor() {
		if (style != null) {
			String[] styles = style.split(";");
			if (styles.length > 1) {
				String[] color = styles[0].split(":");
				if (color.length > 1) {
					return color[1];
				}
			}
		}
		return "";
	}

	/**
	 * 判断标题是否加粗
	 * 
	 * @return
	 */
	public boolean isTitleBold() {
		if (style != null) {
			String[] styles = style.split(";");
			if (styles.length > 1) {
				if (styles[1].equals("font-weight:bold")) {
					return true;
				}
			}
		}
		return false;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getTplEncoding() {
		return tplEncoding;
	}

	public void setTplEncoding(String tplEncoding) {
		this.tplEncoding = tplEncoding;
	}

	public String getOutEncoding() {
		return outEncoding;
	}

	public void setOutEncoding(String outEncoding) {
		this.outEncoding = outEncoding;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Simple constructor of AbstractCmsNode instances.
	 */
	public Node() {
	}

	/**
	 * Constructor of AbstractCmsNode instances given a simple primary key.
	 * 
	 * @param nodeid
	 * 
	 */
	public Node(Long nodeid) {
		this.setNodeId(nodeid);
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getNodeId() {

		return nodeId;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param nodeId
	 *            Integer
	 */
	public void setNodeId(Long nodeId) {
		this.hashValue = 0;

		this.nodeId = nodeId;
	}

	/**
	 * Return the value of the NodeGUID column.
	 * 
	 * @return java.lang.String
	 */
	public String getNodeGuid() {

		return nodeGuid;
	}

	/**
	 * Set the value of the NodeGUID column.
	 * 
	 * @param nodeGuid
	 *            String
	 */
	public void setNodeGuid(String nodeGuid) {

		this.nodeGuid = nodeGuid;
	}

	/**
	 * Return the value of the ParentID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getParentId() {

		return parentId;
	}

	/**
	 * Set the value of the ParentID column.
	 * 
	 * @param parentId
	 *            Integer
	 */
	public void setParentId(Long parentId) {

		this.parentId = parentId;
	}

	/**
	 * Return the value of the RootID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getRootId() {

		return rootId;
	}

	/**
	 * Set the value of the RootID column.
	 * 
	 * @param rootId
	 *            Integer
	 */
	public void setRootId(Long rootId) {

		this.rootId = rootId;
	}

	/**
	 * Return the value of the InheritNodeID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getInheritNodeId() {

		return inheritNodeId;
	}

	/**
	 * Set the value of the InheritNodeID column.
	 * 
	 * @param inheritNodeId
	 *            Integer
	 */
	public void setInheritNodeId(Long inheritNodeId) {

		this.inheritNodeId = inheritNodeId;
	}

	/**
	 * Return the value of the NodeType column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getNodeType() {

		return nodeType;
	}

	/**
	 * Set the value of the NodeType column.
	 * 
	 * @param nodeType
	 *            Byte
	 */
	public void setNodeType(Integer nodeType) {

		this.nodeType = nodeType;
	}

	/**
	 * Return the value of the NodeSort column.
	 * 
	 * @return java.lang.Short
	 */
	public Long getNodeSort() {

		return nodeSort;
	}

	/**
	 * Set the value of the NodeSort column.
	 * 
	 * @param nodeSort
	 *            Short
	 */
	public void setNodeSort(Long nodeSort) {

		this.nodeSort = nodeSort;
	}

	/**
	 * Return the value of the Name column.
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the value of the Name column.
	 * 
	 * @param name
	 *            String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the value of the ContentPSN column.
	 * 
	 * @return java.lang.String
	 */
	public String getContentPsn() {

		return contentPsn;
	}

	/**
	 * Set the value of the ContentPSN column.
	 * 
	 * @param contentPsn
	 *            String
	 */
	public void setContentPsn(String contentPsn) {

		this.contentPsn = contentPsn;
	}

	/**
	 * Return the value of the ContentURL column.
	 * 
	 * @return java.lang.String
	 */
	public String getContentUrl() {

		return contentUrl;
	}

	/**
	 * Set the value of the ContentURL column.
	 * 
	 * @param contentUrl
	 *            String
	 */
	public void setContentUrl(String contentUrl) {

		this.contentUrl = contentUrl;
	}

	/**
	 * Return the value of the ResourcePSN column.
	 * 
	 * @return java.lang.String
	 */
	public String getResourcePsn() {

		return resourcePsn;
	}

	/**
	 * Set the value of the ResourcePSN column.
	 * 
	 * @param resourcePsn
	 *            String
	 */
	public void setResourcePsn(String resourcePsn) {

		this.resourcePsn = resourcePsn;
	}

	/**
	 * Return the value of the ResourceURL column.
	 * 
	 * @return java.lang.String
	 */
	public String getResourceUrl() {

		return resourceUrl;
	}

	/**
	 * Set the value of the ResourceURL column.
	 * 
	 * @param resourceUrl
	 *            String
	 */
	public void setResourceUrl(String resourceUrl) {

		this.resourceUrl = resourceUrl;
	}

	/**
	 * Return the value of the PublishMode column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getPublishMode() {

		return publishMode;
	}

	/**
	 * Set the value of the PublishMode column.
	 * 
	 * @param publishMode
	 *            Byte
	 */
	public void setPublishMode(Integer publishMode) {

		this.publishMode = publishMode;
	}

	/**
	 * Return the value of the IndexTpl column.
	 * 
	 * @return java.lang.String
	 */
	public String getIndexTpl() {

		return indexTpl;
	}

	/**
	 * Set the value of the IndexTpl column.
	 * 
	 * @param indexTpl
	 *            String
	 */
	public void setIndexTpl(String indexTpl) {

		this.indexTpl = indexTpl;
	}

	/**
	 * Return the value of the IndexName column.
	 * 
	 * @return java.lang.String
	 */
	public String getIndexName() {

		return indexName;
	}

	/**
	 * Set the value of the IndexName column.
	 * 
	 * @param indexName
	 *            String
	 */
	public void setIndexName(String indexName) {

		this.indexName = indexName;
	}

	/**
	 * Return the value of the ContentTpl column.
	 * 
	 * @return java.lang.String
	 */
	public String getContentTpl() {

		return contentTpl;
	}

	/**
	 * Set the value of the ContentTpl column.
	 * 
	 * @param contentTpl
	 *            String
	 */
	public void setContentTpl(String contentTpl) {

		this.contentTpl = contentTpl;
	}

	/**
	 * Return the value of the ImageTpl column.
	 * 
	 * @return java.lang.String
	 */
	public String getImageTpl() {

		return imageTpl;
	}

	/**
	 * Set the value of the ImageTpl column.
	 * 
	 * @param imageTpl
	 *            String
	 */
	public void setImageTpl(String imageTpl) {

		this.imageTpl = imageTpl;
	}

	/**
	 * Return the value of the SubDir column.
	 * 
	 * @return java.lang.String
	 */
	public String getSubDir() {

		return subDir;
	}

	/**
	 * Set the value of the SubDir column.
	 * 
	 * @param subDir
	 *            String
	 */
	public void setSubDir(String subDir) {

		this.subDir = subDir;
	}

	/**
	 * Return the value of the PublishFileFormat column.
	 * 
	 * @return java.lang.String
	 */
	public String getPublishFileFormat() {

		return publishFileFormat;
	}

	/**
	 * Set the value of the PublishFileFormat column.
	 * 
	 * @param publishFileFormat
	 *            String
	 */
	public void setPublishFileFormat(String publishFileFormat) {

		this.publishFileFormat = publishFileFormat;
	}

	/**
	 * Return the value of the IsComment column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getComment() {

		return comment;
	}

	/**
	 * Set the value of the IsComment column.
	 * 
	 * @param comment
	 *            Byte
	 */
	public void setComment(Integer comment) {

		this.comment = comment;
	}

	/**
	 * Return the value of the CommentLength column.
	 * 
	 * @return java.lang.Integer
	 */
	public Integer getCommentLength() {

		return commentLength;
	}

	/**
	 * Set the value of the CommentLength column.
	 * 
	 * @param commentLength
	 *            Integer
	 */
	public void setCommentLength(Integer commentLength) {

		this.commentLength = commentLength;
	}

	/**
	 * Return the value of the IsPrint column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getPrint() {

		return print;
	}

	/**
	 * Set the value of the IsPrint column.
	 * 
	 * @param print
	 *            Byte
	 */
	public void setPrint(Integer print) {

		this.print = print;
	}

	/**
	 * Return the value of the IsGrade column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getGrade() {

		return grade;
	}

	/**
	 * Set the value of the IsGrade column.
	 * 
	 * @param grade
	 *            Byte
	 */
	public void setGrade(Integer grade) {

		this.grade = grade;
	}

	/**
	 * Return the value of the IsMail column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getMail() {

		return mail;
	}

	/**
	 * Set the value of the IsMail column.
	 * 
	 * @param mail
	 *            Byte
	 */
	public void setMail(Integer mail) {

		this.mail = mail;
	}

	/**
	 * Return the value of the Disabled column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getDisabled() {
		return this.disabled;
	}

	/**
	 * Set the value of the Disabled column.
	 * 
	 * @param disabled
	 *            Byte
	 */
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	/**
	 * Return the value of the AutoPublish column.
	 * 
	 * @return java.lang.Byte
	 */
	public Integer getAutoPublish() {

		return autoPublish;
	}

	/**
	 * Set the value of the AutoPublish column.
	 * 
	 * @param autoPublish
	 *            Byte
	 */
	public void setAutoPublish(Integer autoPublish) {

		this.autoPublish = autoPublish;
	}

	/**
	 * Return the value of the IndexPortalURL column.
	 * 
	 * @return java.lang.String
	 */
	public String getIndexPortalUrl() {

		return indexPortalUrl;
	}

	/**
	 * Set the value of the IndexPortalURL column.
	 * 
	 * @param indexPortalUrl
	 *            String
	 */
	public void setIndexPortalUrl(String indexPortalUrl) {

		this.indexPortalUrl = indexPortalUrl;
	}

	/**
	 * Return the value of the ContentPortalURL column.
	 * 
	 * @return java.lang.String
	 */
	public String getContentPortalUrl() {

		return contentPortalUrl;
	}

	/**
	 * Set the value of the ContentPortalURL column.
	 * 
	 * @param contentPortalUrl
	 *            String
	 */
	public void setContentPortalUrl(String contentPortalUrl) {

		this.contentPortalUrl = contentPortalUrl;
	}

	/**
	 * Return the value of the Pager column.
	 * 
	 * @return java.lang.String
	 */
	public String getPager() {
		return this.pager;
	}

	/**
	 * Set the value of the Pager column.
	 * 
	 * @param pager
	 *            String
	 */
	public void setPager(String pager) {
		this.pager = pager;
	}

	/**
	 * Return the value of the Editor column.
	 * 
	 * @return java.lang.String
	 */
	public String getEditor() {
		return this.editor;
	}

	/**
	 * Set the value of the Editor column.
	 * 
	 * @param editor
	 *            String
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}

	/**
	 * Return the value of the WorkFlow column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getWorkflow() {
		return this.workflow;
	}

	/**
	 * Set the value of the WorkFlow column.
	 * 
	 * @param workflow
	 *            Integer
	 */
	public void setWorkflow(Long workflow) {
		this.workflow = workflow;
	}

	/**
	 * Return the value of the CreationUserID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getCreationUserId() {

		return creationUserId;
	}

	/**
	 * Set the value of the CreationUserID column.
	 * 
	 * @param creationUserId
	 *            Integer
	 */
	public void setCreationUserId(Long creationUserId) {

		this.creationUserId = creationUserId;
	}

	/**
	 * Return the value of the CreationDate column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getCreationDate() {

		return creationDate;
	}

	/**
	 * Set the value of the CreationDate column.
	 * 
	 * @param creationDate
	 *            Integer
	 */
	public void setCreationDate(Long creationDate) {

		this.creationDate = creationDate;
	}

	/**
	 * Return the value of the LastModifyDate column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getLastModifyDate() {

		return lastModifyDate;
	}

	/**
	 * Set the value of the LastModifyDate column.
	 * 
	 * @param lastModifyDate
	 *            Integer
	 */
	public void setLastModifyDate(Long lastModifyDate) {

		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * Return the value of the LastModifyUserID column.
	 * 
	 * @return java.lang.Integer
	 */
	public Long getLastModifyUserId() {

		return lastModifyUserId;
	}

	public String getNodeUrl() {
		return nodeUrl;
	}

	public String getUrl() {
		return nodeUrl;
	}

	public Integer getSystem() {
		return system;
	}

	public String getManageUrl() {
		return manageUrl;
	}

	public String getPublishUrl() {
		return publishUrl;
	}

	public String getTableName() {
		return tableName;
	}

	public String getExtraPortalUrl() {
		return extraPortalUrl;
	}

	/**
	 * Set the value of the LastModifyUserID column.
	 * 
	 * @param lastModifyUserId
	 *            Integer
	 */
	public void setLastModifyUserId(Long lastModifyUserId) {

		this.lastModifyUserId = lastModifyUserId;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}

	public void setSystem(Integer system) {
		this.system = system;
	}

	public void setManageUrl(String manageUrl) {
		this.manageUrl = manageUrl;
	}

	public void setPublishUrl(String publishUrl) {
		this.publishUrl = publishUrl;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setExtraPortalUrl(String extraPortalUrl) {
		this.extraPortalUrl = extraPortalUrl;
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object rhs) {
		if (rhs == null) {
			return false;
		}
		if (!(rhs instanceof Node)) {
			return false;
		}
		Node that = (Node) rhs;
		if (this.getNodeId() == null || that.getNodeId() == null) {
			return false;
		}
		return (this.getNodeId().equals(that.getNodeId()));
	}

	public List<Node> getNavNodes(Long topNodeId) {
		//
		NodeManager nodeManager = (NodeManager) ObjectLocator.lookup(
				"nodeManager", CmsPlugin.PLUGIN_ID);
		if (nodeManager != null) {
			return nodeManager.getNavNodes(this, topNodeId);
		}
		return null;
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	public int hashCode() {
		if (this.hashValue == 0) {
			int result = 17;
			int nodeidValue = this.getNodeId() == null ? 0 : this.getNodeId()
					.hashCode();
			result = result * 37 + nodeidValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public List getNavigation() {
		return navigation;
	}

	public void setNavigation(List navigation) {
		this.navigation = navigation;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Set getContentTables() {
		return contentTables;
	}

	public void setContentTables(Set contentTables) {
		this.contentTables = contentTables;
	}

	public String getRssTpl() {
		return rssTpl;
	}

	public void setRssTpl(String rssTpl) {
		this.rssTpl = rssTpl;
	}

	public Integer getRssNums() {
		return rssNums;
	}

	public void setRssNums(Integer rssNums) {
		this.rssNums = rssNums;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public Integer getProtectStatus() {
		return protectStatus;
	}

	public void setProtectStatus(Integer protectStatus) {
		this.protectStatus = protectStatus;
	}

	public Integer getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(Integer menuStatus) {
		this.menuStatus = menuStatus;
	}

	public Integer getMenuPos() {
		return menuPos;
	}

	public void setMenuPos(Integer menuPos) {
		this.menuPos = menuPos;
	}

	public Integer getMenuName() {
		return menuName;
	}

	public void setMenuName(Integer menuName) {
		this.menuName = menuName;
	}

	public String getNodeIcon() {
		return nodeIcon;
	}

	public void setNodeIcon(String nodeIcon) {
		this.nodeIcon = nodeIcon;
	}

	public Integer getNodeKind() {
		return nodeKind;
	}

	public void setNodeKind(Integer nodeKind) {
		this.nodeKind = nodeKind;
	}

	public Integer getAddContentStatus() {
		return addContentStatus;
	}

	public void setAddContentStatus(Integer addContentStatus) {
		this.addContentStatus = addContentStatus;
	}

	public Integer getContributionStatus() {
		return contributionStatus;
	}

	public void setContributionStatus(Integer contributionStatus) {
		this.contributionStatus = contributionStatus;
	}

	public Integer getAutoRefreshMode() {
		return autoRefreshMode;
	}

	public void setAutoRefreshMode(Integer autoRefreshMode) {
		this.autoRefreshMode = autoRefreshMode;
	}

	public Long getContentCount() {
		return contentCount == null ? 0L : contentCount;
	}

	public void setContentCount(Long contentCount) {
		this.contentCount = contentCount;
	}

	public Long getPublishedConentCount() {
		return publishedConentCount == null ? 0L : publishedConentCount;
	}

	public void setPublishedConentCount(Long publishedConentCount) {
		this.publishedConentCount = publishedConentCount;
	}

}
