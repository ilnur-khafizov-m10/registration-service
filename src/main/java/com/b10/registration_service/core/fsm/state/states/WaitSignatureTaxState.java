package com.b10.registration_service.core.fsm.state.states;

import com.b10.registration_service.core.fsm.state.RegistrationContext;
import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.fsm.state.State;
import com.b10.registration_service.core.fsm.state.StateHandler;

@StateHandler(RegistrationState.WAIT_SIGNATURE_TAX)
public class WaitSignatureTaxState implements State {

    @Override
    public RegistrationState handleEvent(RegistrationEvent event, RegistrationContext context) {
        switch (event) {
            case SIGNED_TAX:
                context.putVariable("taxDocumentSignedAt", System.currentTimeMillis());
                return RegistrationState.TAX_DATA_REQUESTED;
            default:
                throw new IllegalStateException("Event " + event + " not allowed in state " + getCurrentState());
        }
    }

    @Override
    public RegistrationState getCurrentState() {
        return RegistrationState.WAIT_SIGNATURE_TAX;
    }

    @Override
    public void onEnter(RegistrationContext context) {
        // Логика при входе в состояние - например, отправка уведомления
        context.putVariable("waitingForTaxSignatureStarted", System.currentTimeMillis());
    }

    @Override
    public boolean canHandle(RegistrationEvent event) {
        return event == RegistrationEvent.SIGNED_TAX;
    }
}
