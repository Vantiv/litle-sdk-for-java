package io.github.vantiv.sdk;

public class VantivInvalidCredentialException extends LitleOnlineException {

	private static final long serialVersionUID = 1L;
	
	public VantivInvalidCredentialException(String message, Exception ume) {
		super(message, ume);
	}
	
	public VantivInvalidCredentialException(String message) {
		super(message);
	}

}
