package com.vvs.demo.project.exception;

public class SendEmailException extends RuntimeException {
    public SendEmailException(String message) {
        super(message);
    }
}
