package com.mscg.jmp3.exception;

public class InvalidRegExTagValueException extends Exception {

    private static final long serialVersionUID = -3142851560405944359L;

    public InvalidRegExTagValueException() {
        super();
    }

    public InvalidRegExTagValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRegExTagValueException(String message) {
        super(message);
    }

    public InvalidRegExTagValueException(Throwable cause) {
        super(cause);
    }

}
