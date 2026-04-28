package com.pagely.aiservice.ai.application.port;

import java.util.UUID;

public interface UserEmbeddingPort {

    public void update(UUID userId, String profileText);

    public void initialize(UUID userId, String profileText);
}
