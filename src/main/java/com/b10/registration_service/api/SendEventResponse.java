package com.b10.registration_service.api;

import com.b10.registration_service.core.fsm.state.RegistrationState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ на отправку события")
public record SendEventResponse(

        @Schema(description = "ID процесса", example = "reg_business_123_user_456_a1b2c3d4")
        String processId,

        @Schema(description = "Предыдущее состояние", example = "WAIT_SIGNATURE_TAX")
        RegistrationState previousState,

        @Schema(description = "Новое состояние", example = "TAX_DATA_REQUESTED")
        RegistrationState currentState,

        @Schema(description = "Сообщение о результате", example = "Event processed successfully")
        String message
) {}