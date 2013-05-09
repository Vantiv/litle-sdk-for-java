package com.litle.sdk;


public class LitleBatchNoMoreBatchesException extends LitleBatchException {

	private static final long serialVersionUID = 1L;

	public LitleBatchNoMoreBatchesException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchNoMoreBatchesException(String message) {
		super(message);
	}

}
