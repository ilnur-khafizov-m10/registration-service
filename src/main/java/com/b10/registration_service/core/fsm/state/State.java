package com.b10.registration_service.core.fsm.state;

public interface State {

    /**
     * Обрабатывает событие и возвращает новое состояние
     */
    RegistrationState handleEvent(RegistrationEvent event, RegistrationContext context);

    /**
     * Возвращает текущее состояние (определяется через аннотацию)
     */
    default RegistrationState getCurrentState() {
        StateHandler annotation = this.getClass().getAnnotation(StateHandler.class);
        if (annotation == null) {
            throw new IllegalStateException("State class must be annotated with @StateHandler");
        }
        return annotation.value();
    }

    /**
     * Действия при входе в состояние
     */
    default void onEnter(RegistrationContext context) {
        // Переопределяется в конкретных состояниях при необходимости
    }

    /**
     * Действия при выходе из состояния
     */
    default void onExit(RegistrationContext context) {
        // Переопределяется в конкретных состояниях при необходимости
    }

    /**
     * Проверяет, возможно ли обработать событие
     */
    default boolean canHandle(RegistrationEvent event) {
        return true; // По умолчанию - можно обработать
    }
}
