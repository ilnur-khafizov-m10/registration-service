package com.b10.registration_service.core.fsm.state.states;

import com.b10.registration_service.core.fsm.state.RegistrationContext;
import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.fsm.state.State;
import com.b10.registration_service.core.fsm.state.StateHandler;
import org.springframework.stereotype.Component;

@Component
@StateHandler(RegistrationState.FINISHED)
public class FinishedState implements State {

    @Override
    public RegistrationState handleEvent(RegistrationEvent event, RegistrationContext context) {
        throw new IllegalStateException("Process is already finished. No events can be processed.");
    }

    @Override
    public void onEnter(RegistrationContext context) {
        context.putVariable("processFinishedAt", System.currentTimeMillis());
    }

    @Override
    public boolean canHandle(RegistrationEvent event) {
        return false; // Финальное состояние не принимает события
    }
}
