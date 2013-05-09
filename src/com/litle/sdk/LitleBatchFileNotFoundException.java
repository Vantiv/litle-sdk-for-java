package com.litle.sdk;


public class LitleBatchFileNotFoundException extends LitleBatchException {

	private static final long serialVersionUID = 1L;

	public LitleBatchFileNotFoundException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchFileNotFoundException(String message) {
		super(message);
	}

}
