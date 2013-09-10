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

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openuap.cms.config.CMSConfig;
import org.openuap.runtime.profile.ProfileInfoHolder;
import org.springframework.util.ReflectionUtils;

/**
 * <p>
 * 发布侦测维持器.
 * </p>
 * 
 * <p>
 * $Id: PublishProfileInfoHolder.java 3924 2010-10-26 11:53:36Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class PublishProfileInfoHolder {
	
	private static Log logger = LogFactory
			.getLog(PublishProfileInfoHolder.class);

	public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";

	public static final String SYSTEM_PROPERTY = "publish.profile.strategy";

	public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";

	public static final String MODE_GLOBAL = "MODE_GLOBAL";

	private static String strategyName = System.getProperty(SYSTEM_PROPERTY);

	private static int initializeCount = 0;

	private static PublishProfileHolderStrategy strategy;
	static {
		initialize();
	}

	public static int getInitializeCount() {
		return initializeCount;
	}

	private static void initialize() {
		if ((strategyName == null) || "".equals(strategyName)) {
			// Set default
			strategyName = MODE_THREADLOCAL;
		}

		if (strategyName.equals(MODE_THREADLOCAL)) {
			strategy = new PublishThreadLocalProfileHolderStrategy();
		} else if (strategyName.equals(MODE_INHERITABLETHREADLOCAL)) {
			strategy = new PublishInheritableThreadLocalProfileHolderStrategy();
		} else if (strategyName.equals(MODE_GLOBAL)) {
			strategy = new PublishGlobalProfileHolderStrategy();
		} else {
			// Try to load a custom strategy
			try {
				Class clazz = Class.forName(strategyName);
				Constructor customStrategy = clazz
						.getConstructor(new Class[] {});
				strategy = (PublishProfileHolderStrategy) customStrategy
						.newInstance(new Object[] {});
			} catch (Exception ex) {
				ReflectionUtils.handleReflectionException(ex);
			}
		}

		initializeCount++;
	}

	public static void setStrategyName(String strategyName) {
		PublishProfileInfoHolder.strategyName = strategyName;
		initialize();
	}

	public static void clearProfile() {
		strategy.clearProfile();
	}

	public static PublishActionProfile getProfile() {
		return strategy.getProfile();
	}

	public static void setProfile(PublishActionProfile profile) {
		strategy.setProfile(profile);
	}

	public static boolean isEnableProfile() {
		return CMSConfig.getInstance().getBooleanProperty("cms.publish.profile", true);
	}

	/**
	 * 把分析信息记录到日志内
	 */
	public static void logProfile() {
		PublishActionProfile ap = getProfile();
		if (ap != null) {
			//
			System.out
					.println("//--------------------------profile start-------------------");
			System.out.println("request URL=" + ap.getActionURI());
			System.out.println("request Action=" + ap.getActionName());
			System.out.println("locatorActionTimes="
					+ ap.getLocatorActionTimes() + " ms");
			System.out.println("ProcessActionTimes="
					+ ap.getProcessActionTimes() + " ms");
			System.out.println("RenderTimes=" + ap.getRenderTimes() + " ms");
			System.out.println("---------------Operation start-------------");
			List dbs = ap.getAllPublishOperations();
			int size = dbs.size();
			System.out.println("total publish operation=" + size + " times");
			int totalDbTimes = 0;
			for (int i = 0; i < size; i++) {
				PublishOperationProfile db = (PublishOperationProfile) dbs.get(i);
				System.out.println("    publish operation[" + i + "]=(" + db.getOperation()
						+ ") spare " + db.getProccessTimes() + " ms");
				Exception ex = db.getException();
				if (ex != null) {
					System.out.println("    exception publish operation[" + i + "]="
							+ ex.getMessage());
				}
				totalDbTimes += db.getProccessTimes();
			}
			System.out.println("total publish operationy spare=" + totalDbTimes + " ms");
			
			System.out.println("---------------Operation end  -------------");
			if (ProfileInfoHolder.isEnableProfile()) {
				ProfileInfoHolder.logDBProfile();				
			}
			System.out
					.println("//--------------------------profile end  -------------------");
		}
	}
}
