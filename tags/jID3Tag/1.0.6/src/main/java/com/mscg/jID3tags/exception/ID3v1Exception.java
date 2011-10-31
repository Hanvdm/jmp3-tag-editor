package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v1Exception extends Exception {

    private static final long serialVersionUID = 5212866573122290305L;

    public ID3v1Exception(Throwable cause) {
        super(cause);
    }

    public ID3v1Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ID3v1Exception(String message) {
        super(message);
    }

    public ID3v1Exception() {
    }

}
