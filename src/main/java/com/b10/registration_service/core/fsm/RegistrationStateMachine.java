package com.b10.registration_service.core.fsm;

import com.b10.registration_service.core.fsm.state.RegistrationContext;
import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.fsm.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationStateMachine {

    @Autowired
    private StateFactory stateFactory;

    /**
     * Обрабатывает событие и переводит процесс в новое состояние
     */
    public void processEvent(RegistrationContext context, RegistrationEvent event) {
        State currentState = stateFactory.getState(context.getCurrentState());

        // Проверяем, может ли состояние обработать событие
        if (!currentState.canHandle(event)) {
            throw new IllegalStateException(
                    "Event " + event + " cannot be handled in state " + context.getCurrentState()
            );
        }

        // Выход из текущего состояния
        currentState.onExit(context);

        // Обрабатываем событие и получаем новое состояние
        RegistrationState newState = currentState.handleEvent(event, context);

        // Обновляем контекст
        context.setCurrentState(newState);

        // Вход в новое состояние
        State nextState = stateFactory.getState(newState);
        nextState.onEnter(context);
    }

    /**
     * Проверяет, возможно ли обработать событие в текущем состоянии
     */
    public boolean canProcessEvent(RegistrationContext context, RegistrationEvent event) {
        try {
            State currentState = stateFactory.getState(context.getCurrentState());
            return currentState.canHandle(event);
        } catch (Exception e) {
            return false;
        }
    }
}
