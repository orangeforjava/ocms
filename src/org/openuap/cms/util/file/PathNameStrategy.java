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
package org.openuap.cms.util.file;

/**
 * <p>
 * 路径名称策略.
 * </p>
 *
 * 
 * <p>
 * $Id: PathNameStrategy.java 3917 2010-10-26 11:39:48Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public interface PathNameStrategy {
	/**
	 * the strategy name
	 * 
	 * @return String
	 */
	public String getStrategyName();

	/**
	 * get the path name,relative the root dir
	 * 
	 * @return String
	 */
	public String getPathName();

	public String getFileName(String type);

	/**
	 * set the dest dir name
	 * 
	 * @param rootDir
	 *            
	 */
	public void setRootDir(String rootDir);

	/**
	 * decide the strategy is equal.
	 * 
	 * @param strategy
	 *            
	 * @return 
	 */
	public boolean isEqualStrategy(PathNameStrategy strategy);

	public void setUserName(String userName);
}
