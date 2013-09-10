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
 * Handler for the get and post commands.
 * 
 * @version $Id: CommandHandler.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class CommandHandler {

	private String name;
	private static Map<String, CommandHandler> getCommands = new HashMap<String, CommandHandler>(
	        3);
	private static Map<String, CommandHandler> postCommands = new HashMap<String, CommandHandler>(
	        2);
	public static final CommandHandler GET_FOLDERS = new CommandHandler("GetFolders");
	public static final CommandHandler GET_FOLDERS_AND_FILES = new CommandHandler("GetFoldersAndFiles");
	public static final CommandHandler CREATE_FOLDER = new CommandHandler("CreateFolder");
	public static final CommandHandler FILE_UPLOAD = new CommandHandler("FileUpload");
	public static final CommandHandler QUICK_UPLOAD = new CommandHandler("QuickUpload");

	static {
		// initialize the get commands
		getCommands.put(GET_FOLDERS.getName(), GET_FOLDERS);
		getCommands.put(GET_FOLDERS_AND_FILES.getName(), GET_FOLDERS_AND_FILES);
		getCommands.put(CREATE_FOLDER.getName(), CREATE_FOLDER);
		
		// initialize the post commands
		postCommands.put(FILE_UPLOAD.getName(), FILE_UPLOAD);
		postCommands.put(QUICK_UPLOAD.getName(), QUICK_UPLOAD);
	}

	private CommandHandler(final String name) {
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
	 * Getter for an {@link CommandHandler} of a specified string.
	 * 
	 * @param name
	 * @return A {@link CommandHandler} object holding the value represented by the string
	 *         argument.
	 * @throws IllegalArgumentException
	 *             If 'name' is null or can't be parsed.
	 */
	public static CommandHandler valueOf(final String name) throws IllegalArgumentException {
		if (name == null)
			throw new IllegalArgumentException();

		if (!isValidForGet(name) && !isValidForPost(name))
			throw new IllegalArgumentException();
		return (getCommands.get(name) != null) ? getCommands.get(name) : postCommands.get(name);
	}
	

	/**
	 * Checks, if a specfied string is a valid representation of a get command.
	 * 
	 * @param name
	 * @return True, if the string representation is valid, or false.
	 */
	public static boolean isValidForGet(final String name) {
		return getCommands.containsKey(name);
	}

	/**
	 * Checks, if a specfied string is a valid representation of a post command.
	 * 
	 * @param name
	 * @return True, if the string representation is valid, or false.
	 */
	public static boolean isValidForPost(final String name) {
		return postCommands.containsKey(name);
	}
	
	
	/**
	 * A wrapper for {@link #valueOf(String)}. It returns null instead of throwing an exception.
	 * 
	 * @param name
	 * @return A {@link CommandHandler} object holding the value represented by the string
	 *         argument, or null.
	 */
	public static CommandHandler getCommand(final String name) {
		try {
			return CommandHandler.valueOf(name);
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
			CommandHandler rt = (CommandHandler) obj;
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
