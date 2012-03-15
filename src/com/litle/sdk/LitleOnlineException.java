package com.litle.sdk;


public class LitleOnlineException extends RuntimeException {

	public LitleOnlineException(String message, Exception ume) {
		super(message, ume);
	}

}
