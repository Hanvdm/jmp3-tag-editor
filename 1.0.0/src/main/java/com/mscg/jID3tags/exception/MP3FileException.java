package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class MP3FileException extends Exception {

    private static final long serialVersionUID = 8904839775088149495L;

    public MP3FileException(Throwable cause) {
        super(cause);
    }

    public MP3FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public MP3FileException(String message) {
        super(message);
    }

    public MP3FileException() {
    }

}
