package com.servustech.carehome.util.exception;

public class DiskOperationException extends RuntimeException {

    public DiskOperationException(final String message) {
        this(message, null);
    }

    public DiskOperationException(final Throwable cause) {
        this(null, cause);
    }

    public DiskOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}