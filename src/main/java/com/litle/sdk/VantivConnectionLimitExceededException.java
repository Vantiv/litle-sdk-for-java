package com.litle.sdk;

/**
 * VantivConnectionLimitExceededException is an exception class
 * used to specify the type of error returned by Vantiv.
 *
 * Implementers of the Vantiv eCommerce SDK can catch a specific
 * exception and take appropriate action.
 *
 * This exception will be thrown when an implementation has created more
 * connections to Vantiv then they are allowed.
 *
 * @see <a href="https://developer.vantiv.com/docs/DOC-1190">Open access</a> for more information.
 */
public class VantivConnectionLimitExceededException extends LitleOnlineException {
    private static final long serialVersionUID = 1L;

    public VantivConnectionLimitExceededException(String message, Exception ume) {
        super(message, ume);
    }

    public VantivConnectionLimitExceededException(String message) {
        super(message);
    }
}
