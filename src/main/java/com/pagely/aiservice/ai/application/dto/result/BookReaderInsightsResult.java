package com.pagely.aiservice.ai.application.dto.result;

import java.util.Map;

public record BookReaderInsightsResult(
        String bookId,
        int totalReviews,
        Map<String, Double> sentimentDistribution,
        Map<String, Long> topKeywords
) {
}