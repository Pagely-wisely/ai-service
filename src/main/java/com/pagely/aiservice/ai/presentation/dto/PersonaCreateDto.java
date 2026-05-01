package com.pagely.aiservice.ai.presentation.dto;

import java.util.UUID;

public record PersonaCreateDto(
        UUID userId,
        String profileText
) {
}
