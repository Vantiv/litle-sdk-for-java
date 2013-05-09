package com.litle.sdk;


public class LitleBatchIOException extends LitleBatchException {

	private static final long serialVersionUID = 1L;

	public LitleBatchIOException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchIOException(String message) {
		super(message);
	}

}
