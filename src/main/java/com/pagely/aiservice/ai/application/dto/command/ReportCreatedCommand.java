package com.pagely.aiservice.ai.application.dto.command;

import java.util.UUID;

public record ReportCreatedCommand(
        String bookId,
        String title,
        String author,
        String category,
        String description,
        UUID userId,
        UUID reportId,
        String reportContent
) {
}
