/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2003-2008 Frederico Caldeira Knabben
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 */
package org.openuap.cms.editor.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openuap.cms.editor.tool.Utils;

/**
 * Handler which manages the allowed and denied extensions for each resource type. The
 * extensions are preset by the properties managed by {@link PropertiesLoader}.<br>
 * <br>
 * Hint: It's recommend to use either allowed or denied extensions for one file type.
 * Never use both at the same time! That's why denied extensions of a file type will be 
 * deleted, if you set the allowed one and vice versa.
 * 
 * @version $Id: ExtensionsHandler.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class ExtensionsHandler {

	private static Map<ResourceTypeHandler, Set<String>> extensionsAllowed = new HashMap<ResourceTypeHandler, Set<String>>();
	private static Map<ResourceTypeHandler, Set<String>> extensionsDenied = new HashMap<ResourceTypeHandler, Set<String>>();

	static {
		// load defaults
		extensionsAllowed.put(ResourceTypeHandler.FILE, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.file.extensions.allowed")));
		extensionsDenied.put(ResourceTypeHandler.FILE, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.file.extensions.denied")));
		extensionsAllowed.put(ResourceTypeHandler.MEDIA, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.media.extensions.allowed")));
		extensionsDenied.put(ResourceTypeHandler.MEDIA, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.media.extensions.denied")));
		extensionsAllowed.put(ResourceTypeHandler.IMAGE, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.image.extensions.allowed")));
		extensionsDenied.put(ResourceTypeHandler.IMAGE, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.image.extensions.denied")));
		extensionsAllowed.put(ResourceTypeHandler.FLASH, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.flash.extensions.allowed")));
		extensionsDenied.put(ResourceTypeHandler.FLASH, Utils.getSet(PropertiesLoader
		    .getProperty("connector.resourceType.flash.extensions.denied")));
	}

	/**
	 * Getter for the allowed extensions of a file type.
	 * 
	 * @param type
	 *          The file type.
	 * @return Set of allowed extensions or an empty set.
	 */
	public static Set<String> getExtensionsAllowed(final ResourceTypeHandler type) {
		return extensionsAllowed.get(type);
	}

	/**
	 * Setter for the allowed extensions of a file type. The denied extensions will be cleared.<br>
	 * If 'extensionsList' is null, allowed extensions kept untouched.
	 * 
	 * @param type
	 *          The file type.
	 * @param extensionsList
	 *          Required format: <code>ext1&#124;ext2&#124;ext3</code>
	 */
	public static void setExtensionsAllowed(final ResourceTypeHandler type, final String extensionsList) {
		if (extensionsList != null) {
			extensionsAllowed.put(type, Utils.getSet(extensionsList));
			extensionsDenied.get(type).clear();
		}
	}

	/**
	 * Getter for the denied extensions of a file type.
	 * 
	 * @param type
	 *          The file type.
	 * @return Set of denied extensions or an empty set.
	 */
	public static Set<String> getExtensionsDenied(final ResourceTypeHandler type) {
		return extensionsDenied.get(type);
	}

	/**
	 * Setter for the denied extensions of a file type. The allowed extensions will be cleared.<br>
	 * If 'extensionsList' is null, denied extensions kept untouched.
	 * 
	 * @param type
	 *          The file type.
	 * @param extensionsList
	 *          Required format: <code>ext1&#124;ext2&#124;ext3</code>
	 */
	public static void setExtensionsDenied(final ResourceTypeHandler type, final String extensionsList) {
		if (extensionsList != null) {
			extensionsDenied.put(type, Utils.getSet(extensionsList));
			extensionsAllowed.get(type).clear();
		}
	}

	/**
	 * Checks, if an extension is allowed for a file type.
	 * 
	 * @param type
	 * @param extension
	 * @return True, false. False is returned too, if 'type' or 'extensions' is null.
	 */
	public static boolean isAllowed(final ResourceTypeHandler type, final String extension) {
		if (type == null || extension == null)
			return false;
		String ext = extension.toLowerCase();
		Set<String> allowed = extensionsAllowed.get(type);
		Set<String> denied = extensionsDenied.get(type);
		if (allowed.isEmpty())
			return !denied.contains(ext);
		if (denied.isEmpty())
			return allowed.contains(ext);
		return false;
	}
}
