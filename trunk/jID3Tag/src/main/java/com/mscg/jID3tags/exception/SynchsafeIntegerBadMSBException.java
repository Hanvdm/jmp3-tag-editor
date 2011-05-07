package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class SynchsafeIntegerBadMSBException extends MP3TagIntegerException {

    private static final long serialVersionUID = 6076854892272648150L;

    public static final String stdMsg = "The MSB of any synchsafe integer must be 0";

    public SynchsafeIntegerBadMSBException(Throwable cause) {
        super(stdMsg, cause);
    }

    public SynchsafeIntegerBadMSBException() {
        super(stdMsg);
    }

}
