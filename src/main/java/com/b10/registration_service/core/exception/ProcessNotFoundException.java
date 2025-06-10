package com.b10.registration_service.core.exception;

public class ProcessNotFoundException extends RuntimeException {

    public ProcessNotFoundException(String processId) {
        super("Process not found: " + processId);
    }
}