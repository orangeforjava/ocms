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
 * An <code>InheritableThreadLocal</code>
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 * 
 * @see java.lang.ThreadLocal
 */
public class PublishInheritableThreadLocalProfileHolderStrategy implements
		PublishProfileHolderStrategy {
	// ~ Static fields/initializers
	// =====================================================================================

	private static ThreadLocal profileContext = new InheritableThreadLocal();

	// ~ Methods
	// ========================================================================================================

	public void clearProfile() {
		profileContext.set(null);
	}

	public PublishActionProfile getProfile() {
		if (profileContext.get() == null) {
			profileContext.set(new PublishActionProfileImpl());
		}

		return (PublishActionProfile) profileContext.get();
	}

	public void setProfile(PublishActionProfile profile) {
		Assert
				.notNull(profile,
						"Only non-null profile instances are permitted");
		profileContext.set(profile);
	}
}
