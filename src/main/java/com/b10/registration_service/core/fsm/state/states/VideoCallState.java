package com.b10.registration_service.core.fsm.state.states;

import com.b10.registration_service.core.fsm.state.RegistrationContext;
import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.fsm.state.State;
import com.b10.registration_service.core.fsm.state.StateHandler;

@StateHandler(RegistrationState.VIDEO_CALL)
public class VideoCallState implements State {

    @Override
    public RegistrationState handleEvent(RegistrationEvent event, RegistrationContext context) {
        switch (event) {
            case VIDEO_APPROVED:
                context.putVariable("videoCallResult", "APPROVED");
                context.putVariable("videoCallCompletedAt", System.currentTimeMillis());
                return RegistrationState.FINISHED;
            case VIDEO_REJECTED:
                context.putVariable("videoCallResult", "REJECTED");
                context.putVariable("videoCallCompletedAt", System.currentTimeMillis());
                return RegistrationState.FINISHED; // Процесс завершается при отклонении
            default:
                throw new IllegalStateException("Event " + event + " not allowed in state " + getCurrentState());
        }
    }

    @Override
    public RegistrationState getCurrentState() {
        return RegistrationState.VIDEO_CALL;
    }
}
