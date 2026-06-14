package com.ute.ticket.shared.exception;

public class DomainValidationException extends RuntimeException {

    public DomainValidationException() {
        super();
    }

    public DomainValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DomainValidationException(final String message) {
        super(message);
    }

    public DomainValidationException(final Throwable cause) {
        super(cause);
    }
}
