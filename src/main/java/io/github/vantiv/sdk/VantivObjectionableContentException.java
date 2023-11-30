package io.github.vantiv.sdk;

/**
 * VantivConnectionLimitExceededException is an exception class
 * used to specify the type of error returned by Vantiv.
 *
 * Implementers of the Vantiv eCommerce SDK can catch a specific
 * exception and take appropriate action.
 *
 * This exception will be thrown when the system determines the
 * submission may contain objectionable content.
 *
 * @see <a href="https://developer.vantiv.com/docs/DOC-1190">Open access</a> for more information.
 */
public class VantivObjectionableContentException extends LitleOnlineException {
    private static final long serialVersionUID = 1L;

    public VantivObjectionableContentException(String message, Exception ume) {
        super(message, ume);
    }

    public VantivObjectionableContentException(String message) {
        super(message);
    }
}
