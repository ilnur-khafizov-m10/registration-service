package com.b10.registration_service.core.exception;

import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;

public class InvalidEventException extends RuntimeException {

    public InvalidEventException(RegistrationEvent event, RegistrationState state) {
        super(String.format("Event %s not allowed in state %s", event, state));
    }
}