package com.litle.sdk;


public class LitleOnlineException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LitleOnlineException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleOnlineException(String message) {
		super(message);
	}

}
