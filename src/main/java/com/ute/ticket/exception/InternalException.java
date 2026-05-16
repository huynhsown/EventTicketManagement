package com.urlshortener.common.exception;

public class InternalException extends RuntimeException {
    public InternalException() {
        super();
    }

    public InternalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InternalException(final String message) {
        super(message);
    }

    public InternalException(final Throwable cause) {
        super(cause);
    }
}
