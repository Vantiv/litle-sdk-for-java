package com.litle.sdk;


public class LitleBatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LitleBatchException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchException(String message) {
		super(message);
	}

}

