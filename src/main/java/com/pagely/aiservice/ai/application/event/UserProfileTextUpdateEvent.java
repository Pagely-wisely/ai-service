package com.pagely.aiservice.ai.application.event;

import java.util.UUID;

public record UserProfileTextUpdateEvent(
        UUID userId
){
}
