package com.mscg.asf.exception;

public class GUIDFormatException extends GUIDException {

    private static final long serialVersionUID = -2005905927401417554L;

    public GUIDFormatException() {
        super();
    }

    public GUIDFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public GUIDFormatException(String message) {
        super(message);
    }

    public GUIDFormatException(Throwable cause) {
        super(cause);
    }

}
