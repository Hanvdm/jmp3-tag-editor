package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class MP3TagIntegerBytesWrongLengthException extends MP3TagIntegerException {

    private static final long serialVersionUID = 3248712217564952723L;

    public static final String stdMsg = "The size of synchsafe integer bytes must be 4";

    public MP3TagIntegerBytesWrongLengthException() {
        super(stdMsg);
    }

    public MP3TagIntegerBytesWrongLengthException(Throwable cause) {
        super(stdMsg, cause);
    }

}
