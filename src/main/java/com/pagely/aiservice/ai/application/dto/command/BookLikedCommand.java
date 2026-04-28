package com.pagely.aiservice.ai.application.dto.command;

import java.util.UUID;

public record BookLikedCommand(
        UUID userId,
        String bookId,
        String title,
        String author,
        String category,
        String description
) {
}
