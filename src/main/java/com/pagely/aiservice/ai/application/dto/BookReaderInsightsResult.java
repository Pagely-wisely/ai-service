package com.pagely.aiservice.ai.application.dto;

import java.util.List;
import java.util.Map;

public record BookReaderInsightsResult(
        String bookId,
        int totalReviews,
        Map<String, Double> sentimentDistribution,
        Map<String, Long> topKeywords
) {}