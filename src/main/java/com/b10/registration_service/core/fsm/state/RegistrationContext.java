package com.b10.registration_service.core.fsm.state;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RegistrationContext {

    private String processId;
    private String businessId;
    private String userId;
    private String processVersion;
    private RegistrationState currentState;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, Object> variables;

    public RegistrationContext(String processId, String businessId, String userId, String processVersion) {
        this.processId = processId;
        this.businessId = businessId;
        this.userId = userId;
        this.processVersion = processVersion;
        this.currentState = RegistrationState.WAIT_SIGNATURE_TAX;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.variables = new HashMap<>();
    }

    // Getters & Setters
    public String getProcessId() { return processId; }
    public void setProcessId(String processId) { this.processId = processId; }

    public String getBusinessId() { return businessId; }
    public void setBusinessId(String businessId) { this.businessId = businessId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProcessVersion() { return processVersion; }
    public void setProcessVersion(String processVersion) { this.processVersion = processVersion; }

    public RegistrationState getCurrentState() { return currentState; }
    public void setCurrentState(RegistrationState currentState) {
        this.currentState = currentState;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Map<String, Object> getVariables() { return variables; }
    public void setVariables(Map<String, Object> variables) { this.variables = variables; }

    // Utility методы для работы с переменными
    public void putVariable(String key, Object value) {
        this.variables.put(key, value);
        this.updatedAt = LocalDateTime.now();
    }

    public Object getVariable(String key) {
        return this.variables.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getVariable(String key, Class<T> type) {
        Object value = this.variables.get(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        return null;
    }
}
