package com.pagely.aiservice.ai.application.dto.command;

public record BookProfileGenerateCommand(
        String bookId,
        String title,
        String author,
        String category,
        String description
) {
}