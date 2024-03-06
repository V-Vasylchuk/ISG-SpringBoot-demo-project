package com.vvs.demo.project.exception;

public class AuthenticationException extends Exception {
    public AuthenticationException(String message, Object... formatArgs) {
        super(message.formatted(formatArgs));
    }
}
