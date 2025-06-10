package com.b10.registration_service.core.fsm;

import com.b10.registration_service.core.entity.RegistrationProcessEntity;
import com.b10.registration_service.core.fsm.state.RegistrationContext;
import org.springframework.stereotype.Component;

@Component
public class RegistrationProcessConverter {

    /**
     * Конвертирует Entity в Context
     */
    public RegistrationContext toContext(RegistrationProcessEntity entity) {
        RegistrationContext context = new RegistrationContext(
                entity.processId(),
                entity.businessId(),
                entity.userId(),
                entity.processVersion()
        );

        context.setCurrentState(entity.currentState());
        context.setCreatedAt(entity.createdAt());
        context.setUpdatedAt(entity.updatedAt());
        context.setVariables(entity.getVariables());

        return context;
    }

    /**
     * Конвертирует Context в Entity
     */
    public RegistrationProcessEntity toEntity(RegistrationContext context) {
        return new RegistrationProcessEntity(
                context.getProcessId(),
                context.getBusinessId(),
                context.getUserId(),
                context.getProcessVersion(),
                context.getCurrentState(),
                null,
                context.getCreatedAt(),
                context.getUpdatedAt()
        ).updateVariables(context.getVariables());
    }

    /**
     * Создает обновленную Entity из Context
     * (records immutable, поэтому возвращаем новый объект)
     */
    public RegistrationProcessEntity updateFromContext(
            RegistrationProcessEntity entity,
            RegistrationContext context
    ) {
        return entity.updateStateAndVariables(context.getCurrentState(), context.getVariables());
    }
}
