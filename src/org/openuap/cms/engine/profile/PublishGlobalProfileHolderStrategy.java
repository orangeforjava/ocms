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
 * A <code>static</code> field-based implementation of .
 * </p>
 * 
 * <p>
 * This means that all instances in the JVM share the same
 * <code>SecurityContext</code>. This is generally useful with rich clients,
 * such as Swing.
 * </p>
 * 
 * <p>
 * $Id: PublishGlobalProfileHolderStrategy.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishGlobalProfileHolderStrategy implements PublishProfileHolderStrategy {
	// ~ Static fields/initializers
	// =====================================================================================

	private static PublishActionProfile profile;

	// ~ Methods
	// ========================================================================================================

	public void clearProfile() {
		profile = null;
	}

	public PublishActionProfile getProfile() {
		if (profile == null) {
			profile = new PublishActionProfileImpl();
		}

		return profile;
	}

	public void setProfile(PublishActionProfile profile) {
		Assert.notNull(profile, "Only non-null profile instances are permitted");
		PublishGlobalProfileHolderStrategy.profile = profile;
	}
}
