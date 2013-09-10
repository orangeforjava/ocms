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

/**
 * Handler for the different resource types.
 * 
 * @version $Id: ResourceTypeHandler.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class ResourceTypeHandler {

	private String name;
	private static Map<String, ResourceTypeHandler> types = new HashMap<String, ResourceTypeHandler>(
	        4);
	private static Map<ResourceTypeHandler, String> paths = new HashMap<ResourceTypeHandler, String>(
	        4);
	public static final ResourceTypeHandler FILE = new ResourceTypeHandler("File");
	public static final ResourceTypeHandler FLASH = new ResourceTypeHandler("Flash");
	public static final ResourceTypeHandler IMAGE = new ResourceTypeHandler("Image");
	public static final ResourceTypeHandler MEDIA = new ResourceTypeHandler("Media");

	static {
		// initialize the resource types
		types.put(FILE.getName(), FILE);
		types.put(FLASH.getName(), FLASH);
		types.put(IMAGE.getName(), IMAGE);
		types.put(MEDIA.getName(), MEDIA);

		// initialize the sub folders for each resource type
		paths.put(FILE, PropertiesLoader.getProperty("connector.resourceType.file.path"));
		paths.put(IMAGE, PropertiesLoader.getProperty("connector.resourceType.image.path"));
		paths.put(FLASH, PropertiesLoader.getProperty("connector.resourceType.flash.path"));
		paths.put(MEDIA, PropertiesLoader.getProperty("connector.resourceType.media.path"));
	}

	private ResourceTypeHandler(final String name) {
		this.name = name;
	}

	/**
	 * Getter for the name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the path (sub folder).
	 * 
	 * @return The path (sub folder).
	 */
	public String getPath() {
		return paths.get(this);
	}

	/**
	 * Getter for an {@link ResourceTypeHandler} of a specified string.
	 * 
	 * @param name
	 * @return A {@link ResourceTypeHandler} object holding the value represented by the string
	 *         argument.
	 * @throws IllegalArgumentException
	 *             If 'name' is null can't be parsed.
	 */
	public static ResourceTypeHandler valueOf(final String name) throws IllegalArgumentException {
		if (name == null)
			throw new IllegalArgumentException();

		ResourceTypeHandler rt = types.get(name);
		if (rt == null)
			throw new IllegalArgumentException();
		return rt;
	}

	/**
	 * Checks, if a specfied string is valid representation of a {@link ResourceTypeHandler}.
	 * 
	 * @param name
	 * @return True, if the string represrntation is valid, or false.
	 */
	public static boolean isValid(final String name) {
		return types.containsKey(name);
	}

	public static ResourceTypeHandler getDefaultResourceType(final String name) {
		ResourceTypeHandler rt = getResourceType(name);
		return rt == null ? FILE : rt;
	}

	/**
	 * A wrapper for {@link #valueOf(String)}. It returns null instead of throwing an exception.
	 * 
	 * @param name
	 * @return A {@link ResourceTypeHandler} object holding the value represented by the string
	 *         argument, or null.
	 */
	public static ResourceTypeHandler getResourceType(final String name) {
		try {
			return ResourceTypeHandler.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			ResourceTypeHandler rt = (ResourceTypeHandler) obj;
			return name.equals(rt.getName());
		} catch (ClassCastException e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
