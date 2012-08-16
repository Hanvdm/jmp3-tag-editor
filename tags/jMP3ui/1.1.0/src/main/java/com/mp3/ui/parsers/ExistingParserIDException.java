package com.mp3.ui.parsers;

public class ExistingParserIDException extends Exception {

    private static final long serialVersionUID = 2774481973933835899L;

    public ExistingParserIDException() {
        super();
    }

    public ExistingParserIDException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingParserIDException(String message) {
        super(message);
    }

    public ExistingParserIDException(Throwable cause) {
        super(cause);
    }

}
