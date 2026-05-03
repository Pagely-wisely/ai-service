package com.pagely.aiservice.ai.application.dto.result;

public record BookRecommendationResult(
        String bookId,
        String title,
        String author,
        String category
) {}
