package com.litle.sdk;


public class LitleBatchJAXBException extends LitleBatchException {

	private static final long serialVersionUID = 1L;

	public LitleBatchJAXBException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchJAXBException(String message) {
		super(message);
	}

}
