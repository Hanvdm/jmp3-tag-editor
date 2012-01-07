package com.mscg.asf.exception;

public class InvalidObjectDataException extends Exception {

    private static final long serialVersionUID = 54727412683868206L;

    public InvalidObjectDataException() {
        super();
    }

    public InvalidObjectDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObjectDataException(String message) {
        super(message);
    }

    public InvalidObjectDataException(Throwable cause) {
        super(cause);
    }

}
