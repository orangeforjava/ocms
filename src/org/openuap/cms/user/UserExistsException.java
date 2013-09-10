/**
 * $Id: UserExistsException.java 3918 2010-10-26 11:40:58Z orangeforjava $
 */
package org.openuap.cms.user;

/**
 * <p>
 * Title: UserExistsException
 * </p>
 * 
 * <p>
 * Description: 用户已经存在异常.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: http://www.openuap.org
 * </p>
 * 
 * @author Weiping Ju
 * @version 1.0
 */
public class UserExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6863820013376942490L;

	public UserExistsException() {
	}

	/**
	 * Constructor for UserExistsException.
	 * 
	 * @param
	 *            
	 */
	public UserExistsException(String message) {
		super(message);
	}

}
