package com.woony.core.domain.exception;

public abstract class DateException extends RuntimeException{
    protected DateException(String message) {
        super(message);
    }
}
