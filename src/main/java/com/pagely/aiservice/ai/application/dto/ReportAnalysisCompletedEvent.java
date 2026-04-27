package com.pagely.aiservice.ai.application.dto;

public record ReportAnalysisCompletedEvent(
        String bookId,
        String title,
        String author,
        String category,
        String description
) {}
