package com.mscg.jmp3.exception;

public class InvalidTagValueException extends Exception {

    private static final long serialVersionUID = 1150767674623448471L;

    public InvalidTagValueException() {
        super();
    }

    public InvalidTagValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTagValueException(String message) {
        super(message);
    }

    public InvalidTagValueException(Throwable cause) {
        super(cause);
    }

}
