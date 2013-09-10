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
package org.openuap.cms.editor.tool;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;




/**
 * Some static helper methods.
 * 
 * @version $Id: Utils.java 3924 2010-10-26 11:53:36Z orangeforjava $
 */
public class Utils {

	/**
	 * Constructs a set of uppercased strings from a 'delimiter' separated
	 * string.
	 * 
	 * @param stringList
	 * @param delimiter
	 *            The delimiter. It shouldn't be empty!
	 * @return An emtpy list, if 'stringList' is empty, or an lowercased set of
	 *         strings.
	 * @throws IllegalArgumentException
	 *             if 'delimiter' is empty.
	 */
	public static Set<String> getSet(final String stringList,
			final String delimiter) {
		if (isEmpty(delimiter))
			throw new IllegalArgumentException(
					"Argument 'delimiter' shouldn't be empty!");
		if (isEmpty(stringList))
			return new HashSet<String>();

		Set<String> set = new HashSet<String>();
		StringTokenizer st = new StringTokenizer(stringList, delimiter);
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			if (isNotEmpty(tmp)) // simple empty filter
				set.add(tmp.toLowerCase());
		}
		return set;
	}

	/**
	 * Just a wrapper to {@link #getSet(String, String)} for using '&#124;' as
	 * delimiter.
	 */
	public static Set<String> getSet(final String stringlist) {
		return getSet(stringlist, "|");
	}

	/**
	 * Checks if a string is null or empty.
	 * 
	 * @param str
	 *            to check
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(final String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * Just a wrapper to {@link #isEmpty(String)}.
	 * 
	 * @param str
	 * @return <code>true</code> if the String is not empty and not null.
	 */
	public static boolean isNotEmpty(final String str) {
		return !isEmpty(str);
	}

	/**
	 * Replaces all 'search' with 'replacement' in 'string'.<br>
	 * Usage:
	 * 
	 * <pre>
	 * Utils.replaceAll(null, *, *)		= &quot;&quot;
	 * Utils.replaceAll(&quot;&quot;, *, *)		= &quot;&quot;
	 * Utils.replaceAll(&quot;foo&quot;, null, *)	= &quot;foo&quot;
	 * Utils.replaceAll(&quot;foo&quot;, &quot;o&quot;, &quot;a&quot;)	= &quot;faa&quot;
	 * </pre>
	 * 
	 * @param string
	 * @param search
	 * @param replacement
	 * @return replaced String
	 */
	public static String replaceAll(final String string, final String search,
			final String replacement) {
		if (isEmpty(string))
			return "";
		if (isEmpty(search))
			return string;
		if (string.indexOf(search) == -1)
			return string;
		StringBuffer sb = new StringBuffer(string);
		int pos = sb.indexOf(search);

		while (pos != -1) {
			sb.replace(pos, pos + search.length(), replacement);
			pos = sb.indexOf(search);
		}
		return sb.toString();
	}
}
