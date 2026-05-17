package com.pagely.aiservice.ai.presentation.dto;

import com.pagely.aiservice.ai.application.dto.command.ReportCreatedCommand;
import java.util.UUID;

public record ReportCreatedDto(
        String bookId,
        String title,
        String author,
        String category,
        String description,
        UUID userId,
        UUID reportId,
        String reportContent
) {
    public ReportCreatedCommand toCommand() {
        return new ReportCreatedCommand(bookId, title, author, category, description, userId, reportId, reportContent);
    }
}
