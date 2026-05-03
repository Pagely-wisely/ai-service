package com.pagely.aiservice.ai.presentation.dto;

import com.pagely.aiservice.ai.application.dto.result.BookRecommendationResult;

public record BookRecommendationResponse(
        String bookId,
        String title,
        String author,
        String category
) {
    public static BookRecommendationResponse from(BookRecommendationResult result) {
        return new BookRecommendationResponse(
                result.bookId(),
                result.title(),
                result.author(),
                result.category()
        );
    }
}
