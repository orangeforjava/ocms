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
package org.openuap.cms.engine.profile;

import org.openuap.cms.engine.profile.impl.PublishActionProfileImpl;
import org.springframework.util.Assert;

/**
 * <p>
 * 发布动作侦测本地线程维持策略.
 * </p>
 * 
 * <p>
 * $Id: PublishThreadLocalProfileHolderStrategy.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishThreadLocalProfileHolderStrategy implements PublishProfileHolderStrategy {

	private static ThreadLocal profileHolder = new ThreadLocal();

	public void clearProfile() {
		profileHolder.set(null);
	}

	public PublishActionProfile getProfile() {
		if (profileHolder.get() == null) {
			profileHolder.set(new PublishActionProfileImpl());
		}
		return (PublishActionProfile) profileHolder.get();
	}

	public void setProfile(PublishActionProfile profile) {
		Assert.notNull(profile, "Only non-null profile instances are permitted");
		profileHolder.set(profile);
	}

}
