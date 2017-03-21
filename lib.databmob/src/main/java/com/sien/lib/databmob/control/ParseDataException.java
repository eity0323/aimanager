/*
    Launch Android Client, HttpException
    Copyright (c) 2014 LAUNCH Tech Company Limited
    http:www.cnlaunch.com
 */
package com.sien.lib.databmob.control;

/**
 * 格式转化错误类型
 *	
 * @author sien
 * @version 1.0
 * @date 2016-9-29
 *
 **/
public class ParseDataException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4010634120321127684L;

	public ParseDataException() {
		super();
	}

	public ParseDataException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ParseDataException(String detailMessage) {
		super(detailMessage);
	}

	public ParseDataException(Throwable throwable) {
		super(throwable);
	}

	
}
