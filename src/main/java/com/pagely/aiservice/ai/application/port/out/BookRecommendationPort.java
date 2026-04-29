package com.pagely.aiservice.ai.application.port.out;

import java.util.List;
import java.util.UUID;

public interface BookRecommendationPort {
    List<String> recommend(UUID userId);
}
