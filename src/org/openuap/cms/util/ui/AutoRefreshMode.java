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
package org.openuap.cms.util.ui;

/**
 * <p>
 * 自动刷新模式定义
 * </p>
 * @author Joseph
 * 
 */
public class AutoRefreshMode implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2054117552301806449L;
	private int mode;
	private String title;
	public static AutoRefreshMode NO_REFRESH_MODE = new AutoRefreshMode(0,
			"不自动关联刷新");
	public static AutoRefreshMode SELF_REFRESH_MODE = new AutoRefreshMode(1,
			"本结点自动关联刷新");
	public static AutoRefreshMode PARENT_REFRESH_MODE = new AutoRefreshMode(2,
			"父结点自动关联刷新");
	public static AutoRefreshMode GLOBAL_REFRESH_MODE = new AutoRefreshMode(3,
			"全局自动关联刷新");
	public static AutoRefreshMode[] ALL_REFRESH_MODES = new AutoRefreshMode[] {
			NO_REFRESH_MODE, SELF_REFRESH_MODE, PARENT_REFRESH_MODE,
			GLOBAL_REFRESH_MODE};

	public AutoRefreshMode(int mode, String title) {
		this.mode = mode;
		this.title = title;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AutoRefreshMode other = (AutoRefreshMode) obj;
		if (mode != other.mode)
			return false;
		return true;
	}
}
