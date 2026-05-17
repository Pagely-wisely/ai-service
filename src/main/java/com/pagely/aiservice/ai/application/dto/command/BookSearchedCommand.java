package com.pagely.aiservice.ai.application.dto.command;

import java.util.UUID;

public record BookSearchedCommand(
        UUID userId,
        String bookId,
        String title,
        String author,
        String category,
        String searchKeyword
) {
}