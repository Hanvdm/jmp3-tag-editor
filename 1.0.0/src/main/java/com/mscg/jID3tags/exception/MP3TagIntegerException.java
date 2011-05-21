package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class MP3TagIntegerException extends Exception {

    private static final long serialVersionUID = -691541000500659158L;

    public MP3TagIntegerException(Throwable cause) {
        super(cause);
    }

    public MP3TagIntegerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MP3TagIntegerException(String message) {
        super(message);
    }

    public MP3TagIntegerException() {
    }

}
