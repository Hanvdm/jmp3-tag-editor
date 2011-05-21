package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class SynchsafeBadIntegerValueException extends MP3TagIntegerException {

    private static final long serialVersionUID = -5406892636448515697L;

    public static final String stdMsg = "The integer value of a synchsafe "
                                        + "integer must be between 0 and 268435455 (0x00000000 and 0x0FFFFFFF)";

    public SynchsafeBadIntegerValueException() {
        super(stdMsg);
    }

    public SynchsafeBadIntegerValueException(Throwable cause) {
        super(stdMsg, cause);
    }

}
