package com.b10.registration_service.core.entity;

import com.b10.registration_service.core.fsm.state.RegistrationState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table("registration_process")
@With
public record RegistrationProcessEntity(
        @Id
        @Column("process_id")
        String processId,

        @Column("business_id")
        String businessId,

        @Column("user_id")
        String userId,

        @Column("process_version")
        String processVersion,

        @Column("current_state")
        RegistrationState currentState,

        @Column("variables")
        String variablesJson,

        @Column("created_at")
        LocalDateTime createdAt,

        @Column("updated_at")
        LocalDateTime updatedAt
) {

    // Статический ObjectMapper для JSON операций
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<>() {};

    // Фабричный метод для создания нового процесса
    public static RegistrationProcessEntity createNew(String processId, String businessId,
                                                      String userId, String processVersion) {
        LocalDateTime now = LocalDateTime.now();
        return new RegistrationProcessEntity(
                processId,
                businessId,
                userId,
                processVersion,
                RegistrationState.WAIT_SIGNATURE_TAX,
                "{}",
                now,
                now
        );
    }

    // Fluent API методы используя Lombok @With
    public RegistrationProcessEntity updateState(RegistrationState newState) {
        return withCurrentState(newState).touchUpdatedAt();
    }

    public RegistrationProcessEntity updateVariables(Map<String, Object> variables) {
        return withVariablesJson(serializeVariables(variables)).touchUpdatedAt();
    }

    public RegistrationProcessEntity updateStateAndVariables(RegistrationState newState,
                                                             Map<String, Object> variables) {
        return withCurrentState(newState)
                .withVariablesJson(serializeVariables(variables))
                .touchUpdatedAt();
    }

    // Цепочка методов для сложных обновлений
    public RegistrationProcessEntity addVariable(String key, Object value) {
        Map<String, Object> currentVars = getVariables();
        currentVars.put(key, value);
        return updateVariables(currentVars);
    }

    public RegistrationProcessEntity removeVariable(String key) {
        Map<String, Object> currentVars = getVariables();
        currentVars.remove(key);
        return updateVariables(currentVars);
    }

    // Приватные helper методы
    private RegistrationProcessEntity touchUpdatedAt() {
        return withUpdatedAt(LocalDateTime.now());
    }

    private String serializeVariables(Map<String, Object> variables) {
        try {
            return OBJECT_MAPPER.writeValueAsString(
                    variables != null ? variables : new HashMap<>()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize variables", e);
        }
    }

    // Getter для переменных как Map
    public Map<String, Object> getVariables() {
        if (variablesJson == null || variablesJson.trim().isEmpty()) {
            return new HashMap<>();
        }
        try {
            return OBJECT_MAPPER.readValue(variablesJson, MAP_TYPE_REF);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize variables", e);
        }
    }

    // Компактный метод для логирования
    @Override
    public String toString() {
        return String.format("RegistrationProcess{id='%s', businessId='%s', userId='%s', state=%s}",
                processId, businessId, userId, currentState);
    }
}
