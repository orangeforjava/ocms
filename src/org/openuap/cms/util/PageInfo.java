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
package org.openuap.cms.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分页信息.
 * </p>
 * 
 * <p>
 * $Id: PageInfo.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 *  
 * @preserve private
 * @author Joseph
 * @version 1.0
 */
public class PageInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3739009774334331847L;

	public static final int DEFAULT_ITEMS_PER_PAGE = 10;

	public static final int DEFAULT_SLIDER_SIZE = 7;

	public static final int UNKNOWN_ITEMS = 0x7fffffff;

	private int page;

	private int items;

	private int itemsPerPage;

	private String url;
	/** 分页的标题.*/
	private List<String> titles=new ArrayList<String>();
	/** 分页的位置信息.*/
	public List<PositionInfo> positions=new ArrayList<PositionInfo>();
	
	public int pages() {
		return (int) Math.ceil((double) items / (double) itemsPerPage);
	}

	public int page() {
		return page;
	}

	public int page(int n) {
		return page = calcPage(n);
	}

	public int page(String n) {
		int nn = Integer.parseInt(n);
		return page = calcPage(nn);
	}

	public int items() {
		return items;
	}

	public int items(int n) {
		items = n < 0 ? 0 : n;
		page(page);
		return items;
	}

	public int itemsPerPage() {
		return itemsPerPage;
	}

	public int itemsPerPage(int n) {
		int tmp = itemsPerPage;
		itemsPerPage = n <= 0 ? 10 : n;
		if (page > 0) {
			page((int) (((double) (page - 1) * (double) tmp) / (double) itemsPerPage) + 1);
		}
		return itemsPerPage;
	}

	public int offset() {
		return page <= 0 ? 0 : itemsPerPage * (page - 1);
	}

	public int length() {
		if (page > 0) {
			return Math.min(itemsPerPage * page, items) - itemsPerPage
					* (page - 1);
		} else {
			return 0;
		}
	}

	public int beginIndex() {
		if (page > 0) {
			return itemsPerPage * (page - 1) + 1;
		} else {
			return 0;
		}
	}

	public int endIndex() {
		if (page > 0) {
			return Math.min(itemsPerPage * page, items);
		} else {
			return 0;
		}
	}

	public int showItem(int n) {
		return page(n / itemsPerPage + 1);
	}

	public int firstPage() {
		return calcPage(1);
	}

	public int lastPage() {
		return calcPage(pages());
	}

	public int previousPage() {
		return calcPage(page - 1);
	}

	public int previousPage(int n) {
		return calcPage(page - n);
	}

	public int nextPage() {
		return calcPage(page + 1);
	}

	public int nextPage(int n) {
		return calcPage(page + n);
	}

	public boolean isDisabledPage(int n) {
		return n < 1 || n > pages() || n == page;
	}

	public Integer[] slider() {
		return slider(7);
	}

	public Integer[] slider(int n) {
		int pages = pages();
		if (pages < 1 || n < 1) {
			return new Integer[0];
		}
		if (n > pages) {
			n = pages;
		}
		Integer slider[] = new Integer[n];
		int first = page - (n - 1) / 2;
		if (first < 1) {
			first = 1;
		}
		if ((first + n) - 1 > pages) {
			first = (pages - n) + 1;
		}
		for (int i = 0; i < n; i++) {
			slider[i] = new Integer(first + i);
		}

		return slider;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("PageBuilder: page ");
		if (pages() < 1) {
			sb.append(page());
		} else {
			Integer slider[] = slider();
			for (int i = 0; i < slider.length; i++) {
				if (isDisabledPage(slider[i].intValue())) {
					sb.append('[').append(slider[i]).append(']');
				} else {
					sb.append(slider[i]);
				}
				if (i < slider.length - 1) {
					sb.append('\t');
				}
			}
		}
		sb.append(" of ").append(pages()).append(",\n");
		sb.append("    Showing items ").append(beginIndex()).append(" to ")
				.append(endIndex()).append(" (total ").append(items()).append(
						" items), ");
		sb.append("offset=").append(offset()).append(", length=").append(
				length());
		return sb.toString();
	}

	protected int calcPage(int n) {
		int pages = pages();
		if (pages > 0) {
			return n >= 1 ? n <= pages ? n : pages : 1;
		} else {
			return 0;
		}
	}

	public PageInfo() {
		this(0);
	}

	public PageInfo(int n) {
		page = 0;
		items = 0;
		itemsPerPage = n <= 0 ? 10 : n;
	}

	public String getUrl() {
		return url;
	}

	public List<PositionInfo> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionInfo> positions) {
		this.positions = positions;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCurrentPage() {
		return page;
	}

	public int getPageNum() {
		return itemsPerPage;
	}

	public long getTotalNum() {
		return items;
	}

	public long getTotalPage() {
		return this.pages();
	}
	public void setTitle(int index,String title){
		titles.add(index, title);
	}
	public String getTitle(int index){
		return titles.get(index);
	}
	
}
