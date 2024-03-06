package com.vvs.demo.project.exception;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, Object... formatArgs) {
        super(message.formatted(formatArgs));
    }
}
