package com.litle.sdk;


public class LitleBatchNoLitleResponseException extends LitleBatchException {

	private static final long serialVersionUID = 1L;

	public LitleBatchNoLitleResponseException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchNoLitleResponseException(String message) {
		super(message);
	}

}
