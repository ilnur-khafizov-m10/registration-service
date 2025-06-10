package com.b10.registration_service.core.fsm;

import com.b10.registration_service.core.entity.RegistrationProcessEntity;
import com.b10.registration_service.core.fsm.state.RegistrationContext;
import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.b10.registration_service.core.repository.RegistrationProcessRepository;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrationProcessService {

    @Autowired
    private RegistrationStateMachine stateMachine;

    @Autowired
    private RegistrationProcessRepository repository;

    @Autowired
    private RegistrationProcessConverter converter;

    /**
     * Создает новый процесс регистрации
     */
    public String startRegistrationProcess(String processVersion, String businessId, String userId) {
        String processId = generateProcessId(businessId, userId);

        // Создаем новую Entity
        RegistrationProcessEntity entity = RegistrationProcessEntity.createNew(
                processId, businessId, userId, processVersion
        );

        // Сохраняем в БД
        repository.save(entity);

        return processId;
    }

    /**
     * Отправляет событие в процесс
     */
    public RegistrationState sendEvent(String processId, RegistrationEvent event) {
        // Загружаем процесс из БД
        RegistrationProcessEntity entity = repository.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Process not found: " + processId));

        RegistrationContext context = converter.toContext(entity);

        // Обрабатываем событие
        RegistrationState oldState = context.getCurrentState();
        stateMachine.processEvent(context, event);

        // Создаем обновленную Entity из Context
        RegistrationProcessEntity updatedEntity = converter.updateFromContext(entity, context);

        // Сохраняем обновленную Entity
        repository.save(updatedEntity);

        return context.getCurrentState();
    }

    /**
     * Отправляет событие с дополнительными данными
     */
    public RegistrationState sendEvent(String processId, RegistrationEvent event, Map<String, Object> eventData) {
        RegistrationProcessEntity entity = repository.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Process not found: " + processId));

        RegistrationContext context = converter.toContext(entity);

        // Добавляем данные события в контекст
        if (eventData != null) {
            eventData.forEach(context::putVariable);
        }

        // Обрабатываем событие
        stateMachine.processEvent(context, event);

        // Создаем и сохраняем обновленную Entity
        RegistrationProcessEntity updatedEntity = converter.updateFromContext(entity, context);
        repository.save(updatedEntity);

        return context.getCurrentState();
    }

    /**
     * Получает текущее состояние процесса
     */
    @Transactional(readOnly = true)
    public RegistrationState getCurrentState(String processId) {
        return repository.findById(processId)
                .map(RegistrationProcessEntity::currentState)
                .orElseThrow(() -> new IllegalArgumentException("Process not found: " + processId));
    }

    /**
     * Получает полный контекст процесса
     */
    @Transactional(readOnly = true)
    public RegistrationContext getProcessContext(String processId) {
        RegistrationProcessEntity entity = repository.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Process not found: " + processId));

        return converter.toContext(entity);
    }

    /**
     * Проверяет, возможно ли отправить событие
     */
    @Transactional(readOnly = true)
    public boolean canSendEvent(String processId, RegistrationEvent event) {
        try {
            RegistrationProcessEntity entity = repository.findById(processId)
                    .orElse(null);

            if (entity == null) {
                return false;
            }

            RegistrationContext context = converter.toContext(entity);
            return stateMachine.canProcessEvent(context, event);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверяет существование процесса
     */
    @Transactional(readOnly = true)
    public boolean processExists(String processId) {
        return repository.existsById(processId);
    }

    // Остальные методы остаются такими же...

    private String generateProcessId(String businessId, String userId) {
        return String.format("reg_%s_%s_%s", businessId, userId, UUID.randomUUID().toString().substring(0, 8));
    }
}
