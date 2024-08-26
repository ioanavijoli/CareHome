package com.servustech.carehome.util.exception;

public class ServiceException extends RuntimeException {

    public ServiceException(final String message) {
        this(message, null);
    }

    public ServiceException(final Throwable cause) {
        this(null, cause);
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
