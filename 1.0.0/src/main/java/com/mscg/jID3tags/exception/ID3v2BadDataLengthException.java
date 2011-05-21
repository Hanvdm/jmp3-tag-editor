package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2BadDataLengthException extends ID3v2Exception {

    private static final long serialVersionUID = -8622267838278500603L;

    public static final String stdMsg = "Bad data length found while parsing tag's frame.";

    public ID3v2BadDataLengthException() {
        super(stdMsg);
    }

    public ID3v2BadDataLengthException(String message) {
        super(message);
    }

    public ID3v2BadDataLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public ID3v2BadDataLengthException(Throwable cause) {
        super(stdMsg, cause);
    }

}
