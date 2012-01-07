package com.mscg.asf.exception;

public class ASFObjectAllocationException extends Exception {

    private static final long serialVersionUID = 2700343077285668254L;

    public ASFObjectAllocationException() {
        super();
    }

    public ASFObjectAllocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ASFObjectAllocationException(String message) {
        super(message);
    }

    public ASFObjectAllocationException(Throwable cause) {
        super(cause);
    }

}
