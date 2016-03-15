package com.library.app.common.exception;

public class InvalidJsonException extends RuntimeException {
    private static final long serialVersionUID = 6087454351913028554L;

    public InvalidJsonException(String message) {
        super(message);
    }

    public InvalidJsonException(Throwable throwable) {
        super(throwable);
    }

}
