package com.b10.registration_service.core.fsm.state.states;

import com.b10.registration_service.core.fsm.state.RegistrationContext;
import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.fsm.state.State;
import com.b10.registration_service.core.fsm.state.StateHandler;

@StateHandler(RegistrationState.WAIT_PHONE)
public class WaitPhoneState implements State {

    @Override
    public RegistrationState handleEvent(RegistrationEvent event, RegistrationContext context) {
        switch (event) {
            case PHONE_ENTERED:
                String phoneNumber = context.getVariable("phoneNumber", String.class);
                if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                    throw new IllegalArgumentException("Phone number is required");
                }
                return RegistrationState.VIDEO_CALL;
            default:
                throw new IllegalStateException("Event " + event + " not allowed in state " + getCurrentState());
        }
    }

    @Override
    public RegistrationState getCurrentState() {
        return RegistrationState.WAIT_PHONE;
    }
}
