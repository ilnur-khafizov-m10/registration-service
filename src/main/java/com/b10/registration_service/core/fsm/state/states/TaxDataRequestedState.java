package com.b10.registration_service.core.fsm.state.states;

import com.b10.registration_service.core.fsm.state.RegistrationContext;
import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.fsm.state.State;
import com.b10.registration_service.core.fsm.state.StateHandler;

@StateHandler(RegistrationState.TAX_DATA_REQUESTED)
public class TaxDataRequestedState implements State {

    @Override
    public RegistrationState handleEvent(RegistrationEvent event, RegistrationContext context) {
        switch (event) {
            case TAX_DATA_RECEIVED:
                context.putVariable("taxDataReceivedAt", System.currentTimeMillis());
                return RegistrationState.WAIT_PHONE;
            default:
                throw new IllegalStateException("Event " + event + " not allowed in state " + getCurrentState());
        }
    }

    @Override
    public RegistrationState getCurrentState() {
        return RegistrationState.TAX_DATA_REQUESTED;
    }

    @Override
    public void onEnter(RegistrationContext context) {
        // Например, запуск асинхронного процесса получения данных из налоговой
        context.putVariable("taxDataRequestStarted", System.currentTimeMillis());
    }
}
