package com.ute.ticket.shared.exception;

public class DomainConflictException extends RuntimeException {

    public DomainConflictException() {
        super();
    }

    public DomainConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DomainConflictException(final String message) {
        super(message);
    }

    public DomainConflictException(final Throwable cause) {
        super(cause);
    }
}
