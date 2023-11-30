package io.github.vantiv.sdk;


public class LitleBatchFileFullException extends LitleBatchException {

	private static final long serialVersionUID = 1L;

	public LitleBatchFileFullException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchFileFullException(String message) {
		super(message);
	}

}
