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
package org.openuap.cms.publish.security;

/**
 * <p>
 * 发布权限常量
 * </p>
 * 
 * 
 * <p>
 * $Id: PublishPermissionConstant.java 3922 2010-10-26 11:45:20Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishPermissionConstant {
	//
	public static final String OBJECT_TYPE = "org.openuap.cms.publish";
	public static final String ALL_OBJECT = "-1";
	public static final long Admin = Long.MAX_VALUE;
	// 查看内容
	public final static long ViewContent = 1L << 1;
	// 添加内容
	public final static long AddContent = 1L << 2;
	// 编辑内容
	public final static long EditContent = 1L << 3;
	// 删除内容
	public final static long DeleteContent = 1L << 4;
	// 复制内容
	public final static long CopyContent = 1L << 5;
	// 剪切内容
	public final static long CutContent = 1L << 6;
	// 发布内容
	public final static long Publish = 1L << 7;
	// 取消发布内容
	public final static long UnPublish = 1L << 8;
	// 更新内容
	public final static long Refresh = 1L << 9;
	// 查看回收站内容
	public final static long ViewRecycleBin = 1L << 10;
	// 清空回收站内容
	public final static long EmptyRecycleBin = 1L << 11;
	// 设置置顶
	public final static long SetTop = 1L << 12;
	// 设置精化
	public final static long SetPink = 1L << 13;
	// 设置排序
	public final static long SetSort = 1L << 14;
	// 产生虚链接
	public final static long CreateLink = 1L << 15;
	// 产生索引链接
	public final static long CreateIndexLink = 1L << 16;
	// 查看链接
	public final static long ViewLink = 1L << 17;
	// 内容搜索
	public final static long Search = 1L << 18;
	// 高级搜索
	public final static long SearchPro = 1L << 19;
	// 编辑首页模板
	public final static long EditIndexTpl = 1L << 20;
	// 编辑内容模板
	public final static long EditContentTpl = 1L << 21;
	// 编辑图片模板
	public final static long EditImgTpl = 1L << 22;
	// 刷新首页
	public final static long RefreshIndex = 1L << 23;
	// 刷新结点
	public final static long RefreshNode = 1L << 24;
	// 发布结点
	public final static long PublishNode = 1L << 25;
	// 查看附加发布
	public final static long ViewExtraPublish = 1L << 26;
	// 添加附加发布
	public final static long AddExtraPublish = 1L << 27;
	// 编辑附加发布
	public final static long EditExtraPublish = 1L << 28;
	// 删除附加发布
	public final static long DeleteExtraPublish = 1L << 29;
	// 重新发布结点内容
	public final static long RepublishNode = 1L << 30;
	// 恢复内容
	public final static long Restore = 1L << 31;
	// 取消发布结点
	public final static long UnpublishNode = 1L << 32;
	//刷新附加发布
	public final static long RefreshExtraPublish = 1L << 33;
	//站点会员
	public static final long ROLE_MEMBER =ViewContent;
	//站点撰稿人
	public static final long ROLE_INPUT = ViewContent | AddContent | EditContent
			| DeleteContent | CopyContent | CutContent | Restore | Search
			| SearchPro | CreateLink | CreateIndexLink | ViewLink
			| ViewRecycleBin;
	//站点编辑
	public static final long ROLE_EDITOR = ROLE_INPUT | Publish | UnPublish
			| Refresh | SetTop | SetPink | SetSort | RefreshIndex | RefreshNode
			| PublishNode | ViewExtraPublish | AddExtraPublish
			| EditExtraPublish | DeleteExtraPublish | RepublishNode
			| UnpublishNode|RefreshExtraPublish;
	//站点维护
	public static final long ROLE_MAINTAIN=EditIndexTpl|EditContentTpl|EditImgTpl;
	//站点主编
	
	//站点管理员
	public static final long ROLE_ADMIN=Admin;

	private long viewContent = ViewContent;
	private long addContent = AddContent;
	private long editContent = EditContent;
	private long deleteContent = DeleteContent;
	private long copyContent = CopyContent;
	private long cutContent = CutContent;
	private long publish = Publish;
	private long unPublish = UnPublish;
	private long refresh = Refresh;
	private long viewRecycleBin = ViewRecycleBin;
	private long emptyRecycleBin = EmptyRecycleBin;
	private long setTop = SetTop;
	private long setPink = SetPink;
	private long setSort = SetSort;
	private long createLink = CreateLink;
	private long createIndexLink = CreateIndexLink;
	private long viewLink = ViewLink;
	private long search = Search;
	private long searchPro = SearchPro;
	private long editIndexTpl = EditIndexTpl;
	private long editContentTpl = EditContentTpl;
	private long editImgTpl = EditImgTpl;
	private long refreshIndex = RefreshIndex;
	private long refreshNode = RefreshNode;
	private long publishNode = PublishNode;
	private long viewExtraPublish = ViewExtraPublish;
	private long addExtraPublish = AddExtraPublish;
	private long editExtraPublish = EditExtraPublish;
	private long deleteExtraPublish = DeleteExtraPublish;
	private long republishNode = RepublishNode;
	private long restore = Restore;
	private long unpublishNode = UnpublishNode;
	//
	private String objectType = OBJECT_TYPE;

	/**
	 * 
	 */
	public PublishPermissionConstant() {
	}

	public long getAddContent() {
		return addContent;
	}

	public long getAddExtraPublish() {
		return addExtraPublish;
	}

	public long getCopyContent() {
		return copyContent;
	}

	public long getCreateIndexLink() {
		return createIndexLink;
	}

	public long getCreateLink() {
		return createLink;
	}

	public long getCutContent() {
		return cutContent;
	}

	public long getDeleteContent() {
		return deleteContent;
	}

	public long getDeleteExtraPublish() {
		return deleteExtraPublish;
	}

	public long getEditContent() {
		return editContent;
	}

	public long getEditContentTpl() {
		return editContentTpl;
	}

	public long getEditExtraPublish() {
		return editExtraPublish;
	}

	public long getEditImgTpl() {
		return editImgTpl;
	}

	public long getEditIndexTpl() {
		return editIndexTpl;
	}

	public long getEmptyRecycleBin() {
		return emptyRecycleBin;
	}

	public String getObjectType() {
		return objectType;
	}

	public long getPublish() {
		return publish;
	}

	public long getPublishNode() {
		return publishNode;
	}

	public long getRefreshIndex() {
		return refreshIndex;
	}

	public long getSearch() {
		return search;
	}

	public long getSearchPro() {
		return searchPro;
	}

	public long getSetPink() {
		return setPink;
	}

	public long getSetSort() {
		return setSort;
	}

	public long getSetTop() {
		return setTop;
	}

	public long getUnPublish() {
		return unPublish;
	}

	public long getViewContent() {
		return viewContent;
	}

	public long getViewExtraPublish() {
		return viewExtraPublish;
	}

	public long getViewLink() {
		return viewLink;
	}

	public long getViewRecycleBin() {
		return viewRecycleBin;
	}

	public long getRefreshNode() {
		return refreshNode;
	}

	public long getRefresh() {
		return refresh;
	}

	public long getRepublishNode() {
		return republishNode;
	}

	public long getRestore() {
		return restore;
	}

	public long getUnpublishNode() {
		return unpublishNode;
	}
}
