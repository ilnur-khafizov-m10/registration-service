package com.b10.registration_service.core.fsm;

import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.fsm.state.State;
import com.b10.registration_service.core.fsm.state.StateHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StateFactory {

    @Autowired
    private List<State> allStates;

    private Map<RegistrationState, State> stateMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (State state : allStates) {
            StateHandler annotation = state.getClass().getAnnotation(StateHandler.class);
            if (annotation == null) {
                throw new IllegalStateException(
                        "State implementation " + state.getClass().getSimpleName() +
                                " must be annotated with @StateHandler"
                );
            }

            RegistrationState registrationState = annotation.value();

            if (stateMap.containsKey(registrationState)) {
                throw new IllegalStateException(
                        "Duplicate state handler found for state: " + registrationState +
                                ". Existing: " + stateMap.get(registrationState).getClass().getSimpleName() +
                                ", New: " + state.getClass().getSimpleName()
                );
            }

            stateMap.put(registrationState, state);
        }

        // Проверяем, что все состояния имеют реализации
        for (RegistrationState state : RegistrationState.values()) {
            if (!stateMap.containsKey(state)) {
                throw new IllegalStateException(
                        "No state handler found for state: " + state
                );
            }
        }

        System.out.println("Initialized StateFactory with " + stateMap.size() + " states:");
        stateMap.forEach((key, value) ->
                System.out.println("  " + key + " -> " + value.getClass().getSimpleName())
        );
    }

    public State getState(RegistrationState registrationState) {
        State state = stateMap.get(registrationState);
        if (state == null) {
            throw new IllegalArgumentException("No state implementation found for: " + registrationState);
        }
        return state;
    }

    /**
     * Получить все доступные состояния
     */
    public Map<RegistrationState, State> getAllStates() {
        return new HashMap<>(stateMap);
    }

    /**
     * Проверить, что состояние зарегистрировано
     */
    public boolean hasState(RegistrationState state) {
        return stateMap.containsKey(state);
    }
}
