package com.b10.registration_service.api;

import com.b10.registration_service.core.fsm.state.RegistrationEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "Запрос на отправку события в процесс")
public record SendEventRequest(

        @Schema(description = "Событие для обработки", example = "SIGNED_TAX", required = true)
        @NotNull(message = "Event is required")
        RegistrationEvent event,

        @Schema(description = "Дополнительные данные события", example = "{\"phoneNumber\": \"+994501234567\"}")
        Map<String, Object> eventData
) {}
