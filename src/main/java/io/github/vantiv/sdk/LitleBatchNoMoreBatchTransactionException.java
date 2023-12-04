package io.github.vantiv.sdk;


public class LitleBatchNoMoreBatchTransactionException extends LitleBatchException {

	private static final long serialVersionUID = 1L;

	public LitleBatchNoMoreBatchTransactionException(String message, Exception ume) {
		super(message, ume);
	}

	public LitleBatchNoMoreBatchTransactionException(String message) {
		super(message);
	}

}
