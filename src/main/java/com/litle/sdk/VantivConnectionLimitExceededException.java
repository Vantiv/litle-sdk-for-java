package com.litle.sdk;

public class VantivConnectionLimitExceededException extends LitleOnlineException {

	private static final long serialVersionUID = 1L;

	public VantivConnectionLimitExceededException(String message, Exception ume) {
		super(message, ume);
	}
	
	public VantivConnectionLimitExceededException(String message) {
		super(message);
	}

}
