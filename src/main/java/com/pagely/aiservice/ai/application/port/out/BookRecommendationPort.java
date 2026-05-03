package com.pagely.aiservice.ai.application.port.out;

import com.pagely.aiservice.ai.application.dto.result.BookRecommendationResult;
import java.util.List;
import java.util.UUID;

public interface BookRecommendationPort {
    List<BookRecommendationResult> recommend(UUID userId);
}
