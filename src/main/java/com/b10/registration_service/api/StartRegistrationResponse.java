package com.b10.registration_service.api;

import com.b10.registration_service.core.fsm.state.RegistrationState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "StartRegistrationResponse",
        description = "Ответ на запрос создания/получения статуса процесса регистрации"
)
public record StartRegistrationResponse(

        @Schema(
                description = "Уникальный идентификатор процесса регистрации",
                example = "reg_business_123_user_456_a1b2c3d4",
                nullable = true
        )
        String processId,

        @Schema(
                description = "Текущее состояние процесса регистрации",
                example = "WAIT_SIGNATURE_TAX",
                nullable = true
        )
        RegistrationState currentState,

        @Schema(
                description = "Сообщение с описанием результата операции",
                example = "Registration process started successfully"
        )
        String message
) {}