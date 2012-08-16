package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2Exception extends Exception {

    private static final long serialVersionUID = 6001991204278095064L;

    public ID3v2Exception(Throwable cause) {
        super(cause);
    }

    public ID3v2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ID3v2Exception(String message) {
        super(message);
    }

    public ID3v2Exception() {
    }

}
