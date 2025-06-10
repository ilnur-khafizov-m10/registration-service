package com.b10.registration_service.api;

import jakarta.validation.constraints.NotBlank;

public record StartRegistrationRequest(
        @NotBlank(message = "Process version is required")
        String processVersion,

        @NotBlank(message = "Business ID is required")
        String businessId,

        @NotBlank(message = "User ID is required")
        String userId
) {}
