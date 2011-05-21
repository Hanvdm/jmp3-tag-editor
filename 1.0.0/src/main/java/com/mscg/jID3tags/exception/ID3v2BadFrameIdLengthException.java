package com.mscg.jID3tags.exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2BadFrameIdLengthException extends ID3v2Exception {

    private static final long serialVersionUID = 3271612649795585215L;

    public static final String stdMsg = "The length of a ID3v2 frame id must be 4";

    public ID3v2BadFrameIdLengthException() {
        super(stdMsg);
    }

    public ID3v2BadFrameIdLengthException(Throwable cause) {
        super(stdMsg, cause);
    }
}
