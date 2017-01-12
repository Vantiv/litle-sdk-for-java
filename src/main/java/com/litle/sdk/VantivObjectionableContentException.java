package com.litle.sdk;

public class VantivObjectionableContentException extends LitleOnlineException {

	private static final long serialVersionUID = 1L;
	
	public VantivObjectionableContentException(String message, Exception ume) {
		super(message, ume);
	}

	public VantivObjectionableContentException(String message) {
		super(message);
	}

}
